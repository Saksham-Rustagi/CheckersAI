package checkers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class Main {

	private static JButton newGame = new JButton(); 
	private static JLabel message = new JLabel();

	public static void main(String[] args) {
		JFrame window = new JFrame("Checkers");

		window.pack();
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		window.setLocation( (screensize.width - window.getWidth())/2, (screensize.height - window.getHeight())/2 );
		window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		window.setBackground(new Color(100,100,100));
		window.setSize(480,360);
		Board board = new Board();
		window.add(board);




		window.setVisible(true);
	}
	
}
