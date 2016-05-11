package ch.epfl.xblast.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.painter.BoardPainter;

public final class Main {

    public static void main(String[] args) {
        int numberOfPlayers = GameState.PLAYER_NUMBER;
                    
        if(args.length==1)
            numberOfPlayers = Integer.parseInt(args[0]);
        
        try {
            ByteBuffer receivingBuffer = ByteBuffer.allocate(1);
            DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET);
            channel.bind(new InetSocketAddress(2016));
            
            Map<SocketAddress, PlayerID> players = joiningGame(numberOfPlayers, receivingBuffer, channel);
            
            long startTime;
            GameState g = Level.DEFAULT_LEVEL.gameState();
            BoardPainter b = Level.DEFAULT_LEVEL.boardPainter();
            Map<PlayerID, Optional<Direction>> speedChangeEvents = new HashMap<>();
            Set<PlayerID> bombDropEvents = new HashSet<>();
            
            channel.configureBlocking(false);
            
            while(!g.isGameOver()){
                startTime = System.nanoTime();
                
                List<Byte> serializedGameState = GameStateSerializer.serialize(b, g);
                ByteBuffer sendingBuffer = ByteBuffer.allocate(serializedGameState.size()+1);
                ByteBuffer playerBuffer;
                
                for(Byte bytes : serializedGameState)
                    sendingBuffer.put(bytes);
                
                //sendingBuffer.flip();
                  
                for(Map.Entry<SocketAddress, PlayerID> player : players.entrySet()){
                    playerBuffer = sendingBuffer.duplicate();
                    playerBuffer.put(0, (byte) player.getValue().ordinal());
                    playerBuffer.flip();
                    channel.send(playerBuffer, player.getKey());
                }
                
                SocketAddress senderAdress;
                speedChangeEvents.clear();
                bombDropEvents.clear();
                while((senderAdress = channel.receive(receivingBuffer)) != null){
                    if(receivingBuffer.get(0)==PlayerAction.DROP_BOMB.ordinal()){
                        bombDropEvents.add(players.get(senderAdress));
                    }else{
                        if(receivingBuffer.get(0)==PlayerAction.STOP.ordinal())
                            speedChangeEvents.put(players.get(senderAdress), Optional.empty());
                        else
                            speedChangeEvents.put(players.get(senderAdress), Optional.of(Direction.values()[receivingBuffer.get(0)-1]));
                    }
                }
                Thread.sleep(0L, (int)(Ticks.TICK_NANOSECOND_DURATION-(System.nanoTime()-startTime)));
                g = g.next(speedChangeEvents, bombDropEvents);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e){
            e.printStackTrace();
        }

    }
    
    private static Map<SocketAddress, PlayerID> joiningGame(int numberOfplayers, ByteBuffer receivingBuffer, DatagramChannel channel) throws IOException {
        Map<SocketAddress, PlayerID> players = new HashMap<>();
        int currentPlayerID = 0;
        
        SocketAddress senderAddress;
        
        while(players.size()!= numberOfplayers){
            senderAddress = channel.receive(receivingBuffer);
            if(receivingBuffer.get(0)== PlayerAction.JOIN_GAME.ordinal()){
                players.put(senderAddress, PlayerID.values()[currentPlayerID]);
                receivingBuffer.clear();
                ++currentPlayerID;
            }
        }
        return players;        
    }

}