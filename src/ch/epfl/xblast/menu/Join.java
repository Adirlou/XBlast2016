package ch.epfl.xblast.menu;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.epfl.xblast.client.XBlastComponent;

public final class Join {
    public final static void joinMenu(){
        JFrame frame = new JFrame("XBlast2016");
        frame.setPreferredSize(new Dimension(XBlastComponent.XB_COMPONENT_WIDTH,XBlastComponent.XB_COMPONENT_HEIGHT));
        joinMenu(frame);
    }
    
    public final static void joinMenu(JFrame frame){
        JFrame frame1 = new JFrame();
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setPreferredSize(frame.getSize());
        frame1.setLocation(frame.getLocation());
        frame1.pack();
        frame1.setVisible(true);
        
        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, 1));
        
        JLabel ip = new JLabel("Enter the IP");
        
        JTextField field = new JTextField("IP");
        pane.add(field);
        
        JButton join = new JButton("Join");
        
        frame1.add(pane,null,0);
        frame1.add(ip,null,0);
        
        ActionListener l = (e ->{
            
        });
        
    }
}
