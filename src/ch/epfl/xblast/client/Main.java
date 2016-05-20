package ch.epfl.xblast.client;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;

public final class Main {
    private static XBlastComponent component = new XBlastComponent();
    public final static int MAX_BUFFER_SIZE=410;
<<<<<<< HEAD
    public final static int DEFAULT_PORT=2016;
=======
>>>>>>> dde160bf390544d4f3e4d8c1f89d4159ff6b7da1
    public static void main(String[] args) {
        try {
            DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET);
            SocketAddress address = new InetSocketAddress(args.length==0 ? "localhost": args[0],DEFAULT_PORT);
            
            channel.configureBlocking(false);
            
            ByteBuffer bjoin = joinGame(channel, address);
            PlayerID id = PlayerID.values()[bjoin.get()];
                        
            List<Byte> firstState = new ArrayList<>();
            while(bjoin.hasRemaining())
                firstState.add(bjoin.get());
            component.setGameState(GameStateDeserializer.deserializeGameState(firstState), id);
            SwingUtilities.invokeAndWait(() -> createUI(channel, address));
            
            ByteBuffer currentState = ByteBuffer.allocate(MAX_BUFFER_SIZE);
            List<Byte> list = new ArrayList<>();
            channel.configureBlocking(true);
            while (true){
                channel.receive(currentState);
                currentState.flip();
                while (currentState.hasRemaining())
                    list.add(currentState.get());
                component.setGameState(GameStateDeserializer.deserializeGameState(list.subList(1, list.size())), id);
                currentState.clear();
                list.clear();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        
    }
    
    /**
     * Create an User Interface
     * 
     * @param channel
     *      The channel of communication with the server
     *              
     * @param address
     *      The SocketAddress of the server
     */
    private static void createUI(DatagramChannel channel, SocketAddress address){
        JFrame frame = new JFrame("XBlast 2016");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(component);
        frame.pack();
        
        Map<Integer, PlayerAction> kb = new HashMap<>();
        kb.put(KeyEvent.VK_UP, PlayerAction.MOVE_N);
        kb.put(KeyEvent.VK_DOWN, PlayerAction.MOVE_S);
        kb.put(KeyEvent.VK_LEFT, PlayerAction.MOVE_W);
        kb.put(KeyEvent.VK_RIGHT, PlayerAction.MOVE_E);
        kb.put(KeyEvent.VK_SPACE, PlayerAction.DROP_BOMB);
        kb.put(KeyEvent.VK_SHIFT, PlayerAction.STOP);
        Consumer<PlayerAction> c = x -> {    
            try {
                ByteBuffer senderBuffer = ByteBuffer.allocate(1);
                senderBuffer.put((byte)x.ordinal());
                senderBuffer.flip();
                channel.send(senderBuffer, address);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        component.addKeyListener(new KeyboardEventHandler(kb, c));
        component.requestFocusInWindow();
        frame.setVisible(true);
    }
    
    /**
     * Send to the server the intention to join the game, get and return the first state from the server
     * 
     * @param channel
     *      The channel of communication with the server
     *              
     * @param address
     *      The SocketAddress of the server
     *      
     * @return
     *      The Buffer containing the first state of the game
     */
    private static ByteBuffer joinGame(DatagramChannel channel,SocketAddress address) {
        ByteBuffer join = ByteBuffer.allocate(1);
        ByteBuffer firstState = ByteBuffer.allocate(MAX_BUFFER_SIZE);
        join.put((byte)PlayerAction.JOIN_GAME.ordinal()).flip();
        System.out.println("Connecting the server ...");
        try {
            do {
                channel.send(join, address);
                Thread.sleep(1000);
            }while(channel.receive(firstState)==null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch(InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("Connected");
        firstState.flip();
        return firstState;
    }

}
