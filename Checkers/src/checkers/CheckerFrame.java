package checkers;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

public class CheckerFrame extends JFrame implements ActionListener{
    JButton startGameButtonText=new JButton("Start Game");
    JPanel gmP=new StartPanel();
  
    CheckerFrame(){
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this); //changing the appearance of the frame
        }
        catch (Exception e) {
           //no need to handle exception as it only affect the appearance
        }
        setupGUI();
        new PlaySound("Checkers/src/sounds/Start.wav").start();
    }

    private void setupGUI() {
        setLayout(null);
        gmP.setBounds(0,0,400,401);

        // may need to remove this
        gmP.imageUpdate(null, 0, 0, 608, 401, getDefaultCloseOperation());
        add(gmP);
        startGameButtonText.setHorizontalAlignment(SwingConstants.LEADING);
        startGameButtonText.setIcon(new ImageIcon(getClass().getResource("/images/checker.jpg")));
        startGameButtonText.setBackground(Color.LIGHT_GRAY);
        startGameButtonText.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startGameButtonText.setBounds(100,405,300,90);  // original 154,420,200,60
        startGameButtonText.setFont(new Font("Times new roman",Font.BOLD,20));
        startGameButtonText.addActionListener(this);
        startGameButtonText.setFocusPainted(false);
        add(startGameButtonText);

        this.setIconImage(new ImageIcon(getClass().getResource("/images/icon.jpg")).getImage());

        setSize(508,520);
        setLocation((int)getToolkit().getScreenSize().getWidth()/2-254,(int)getToolkit().getScreenSize().getHeight()/2-310);
        setResizable(false);
        setVisible(true);
        setTitle("Play Checkers");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equalsIgnoreCase("Start Game")){
            ((JButton)e.getSource()).setText("New Game");
            new PlaySound("Checkers/src/sounds/button.wav").start();
            gmP=new Checkers();
            gmP.setBounds(0,0,608,501);
            this.setContentPane(gmP);
        }
    }
}
