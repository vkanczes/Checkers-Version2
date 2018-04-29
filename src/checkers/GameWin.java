package checkers;

import javax.swing.*;
import java.awt.*;

public class GameWin extends JDialog{
    Point p;
    JLabel message=new JLabel();
    
    GameWin(String winner,Point p){
        this.p=p;
        message.setText("          "+winner+" Wins!");
        setupGUI();
	}

	private void setupGUI()
	{
        new PlaySound("src//sounds//Win.wav").start();
        message.setFont(new Font("dialog",Font.BOLD,16));
        add(message);

        setAlwaysOnTop(true);
        setLocation((int)p.getX()+100,(int)p.getY()+200);
        setResizable(false);
        setSize(200,80);
        setTitle("Game Over");
        setVisible(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
}
