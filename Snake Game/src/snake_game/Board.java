package snake_game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;


import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener{

	private Image fish;
	private Image tail;
	private Image head;
	private Image Background;
	
	private int dots;
	private int Score;
	
	private final int DOT_SIZE=15;	// 1366*768= 10,49,088, 10,49,088/225=4,662.613333333333(double value)
	private final int MAX_DOT_SIZE=4662;
	private final int RANDOM_POSITION = 30;	// it should within the limit of frame
	
	private final int[] x = new int[MAX_DOT_SIZE];
	private final int[] y = new int[MAX_DOT_SIZE];
	
	private boolean leftDirection = false;
	private boolean rightDirection = true;
	private boolean upDirection = false;
	private boolean downDirection = false;
	
	private int fish_x ;
	private int fish_y ;
	
	private boolean inGame=true;
	private Timer timer;
	
	Board(){
		addKeyListener(new ReceiveKeyBoardEvents());
		setPreferredSize(new Dimension(1366,768));
		setBackground(new Color(65, 105, 225)); // royal blue
		
		loadImage();
		initializeGame();
		
		setFocusable(true); //KeyListener events won't work until it setFocusable value as true
	}
	
	public void loadImage(){
		ImageIcon ib1 = new ImageIcon(ClassLoader.getSystemResource("snake_game/icons/Background1.jpg"));
		Background = ib1.getImage().getScaledInstance(1366,768 , Image.SCALE_DEFAULT);
		
		ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("snake_game/icons/fish.png"));
		fish = i1.getImage();
		
		ImageIcon i2 = new ImageIcon(ClassLoader.getSystemResource("snake_game/icons/tail.png"));
		tail = i2.getImage();
		
		ImageIcon i3 = new ImageIcon(ClassLoader.getSystemResource("snake_game/icons/head.png"));
		head = i3.getImage();
	}
	
	public void initializeGame(){	// image location
		dots =3;
		for (int i = 0; i < dots; i++) {
			x[i] = 30 - (i*DOT_SIZE);
			y[i] = 30;
		}
		locateFish();
		
		timer  = new Timer(130,  this);
		timer.start();
	}
	
	public void locateFish(){
		int r = (int) ((Math.random()) * RANDOM_POSITION); // 0.5*30= 15.0
		fish_x = r * DOT_SIZE; //15*10 = 150
		
		r = (int) ((Math.random()) * RANDOM_POSITION);
		fish_y = r * DOT_SIZE; 
	}
	private class ReceiveKeyBoardEvents extends KeyAdapter{
		 
		 public void keyPressed(KeyEvent e){
			 int key = e.getKeyCode();
			 
			 if (key == KeyEvent.VK_LEFT && (!rightDirection)) {
				 leftDirection = true;
				 upDirection = false;
				 downDirection = false;
			}
			 if (key == KeyEvent.VK_RIGHT  && (!leftDirection)) {
				 rightDirection = true;
				 upDirection = false;
				 downDirection = false;
			}
			 if (key == KeyEvent.VK_UP  && (!downDirection)) {
				 leftDirection = false;
				 upDirection = true;
				 rightDirection = false;
			}
			 if (key == KeyEvent.VK_DOWN  && (!upDirection)) {
				 leftDirection = false;
				 rightDirection = false;
				 downDirection = true;	
			}
		 }
	}
	
	public void checkFish(){	//if snake has eaten apple then its size will increase & apple will locate in new position
		if (x[0]==fish_x && y[0]== fish_y ) {
			dots++;
			Score++;
			locateFish();
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (inGame) {
			checkFish();
			checkCollission();
			snakeMovement();
		}
		repaint();
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		draw(g);
	}
	
	public void draw(Graphics g){
		if (inGame) {
			g.drawImage(Background, 0, 0, this);
	
			g.drawImage(fish, fish_x, fish_y, this);
			
			for (int i = 0; i < dots; i++) {
				if (i==0) {
					g.drawImage(head, x[i], y[i], this);
				}
				else{
					g.drawImage(tail, x[i], y[i], this);
				}
			}
			Toolkit.getDefaultToolkit().sync();
		}
		else{
			gameOver(g);
		}
	}
	public void gameOver(Graphics g){
		String msg = "Game Over";
		Font font = new Font("TimesRoman", Font.BOLD, 40);
		FontMetrics metrics = getFontMetrics(font);
		
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString(msg, (1366 - metrics.stringWidth(msg))/2, (768-35)/2);
		
		String sc = "Your Score: "+ Score;
		Font score_font = new Font("TimesRoman", Font.BOLD, 40);
		FontMetrics score_metrics = getFontMetrics(score_font);
		
		g.setColor(Color.WHITE);
		g.setFont(score_font);
		g.drawString(sc, (1366 - score_metrics.stringWidth(sc))/2, (768+30)/2);
	}
	private void snakeMovement() {
		//tail part
		for (int i = dots; i > 0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		//head part
		if (leftDirection) {
			x[0] = x[0] - DOT_SIZE;
		}
		if(rightDirection){
			x[0] += DOT_SIZE;
		}
		if (upDirection) {
			y[0] = y[0] - DOT_SIZE;
		}
		if(downDirection){
			y[0] += DOT_SIZE;
		}
	}

	public void checkCollission() {
		// TODO Auto-generated method stub
		for (int i = dots; i >0; i--) {
			if ((i>4) && (x[0]== x[i]) && (y[0]== y[i])) {
				inGame =false;
			}
		}
		
		//if snake collides frame
		if ((x[0] >= 1358) || (y[0] >= 748) ) {
			inGame = false;
		}
		if ((x[0] < 0) || (y[0] < 0) ) {
			inGame = false;
		}
		if (!inGame) {
			timer.stop();
		}
	}
}