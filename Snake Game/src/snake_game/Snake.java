package snake_game;

import javax.swing.JFrame;

public class Snake extends JFrame {
	
	Snake(){
		super("Snake Game");
		//setTitle("Snake Game");
		
		add(new Board());
		pack();
		
		setLocationRelativeTo(null);
		setResizable(false);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Snake().setVisible(true);
	}

}
