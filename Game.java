import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.Timer;

public class Game {
/*
 
 This is the Game class. 
 
 */
	
	private Timer gameTimer;
	private GUI gui;
	
	private LeaderBoard leaderBoard;

	private TextAsset gameTimetxt;
	private TextAsset userInputtxt;
	private TextAsset misctxt;
	
	//Asset instance vars
	private Ship player;
	
	private ArrayList<Alien> aliens; 
	private ArrayList<BarrierBlock> blocks;
	
	private boolean moveShipLeft;
	private boolean moveShipRight;
	private boolean isShipFiring;
	
	private int shipFireRate;
	private int shipFireTimer;
	private int alienSpeed;
	
	private int gameState;
	private int gameTime;
	private int tmpCount;
	


	
	
	public Game() {
		
		//Default constructor is separate from init() because the gui does not need to be recreated on every reset.
		gui = new GUI(this);
		gui.setResizable(false);
		
		gameTimer = new Timer(10, null);
		gameTimer.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	        	loop();
	        }
	    });		
		
		init();
	}
	
	private void init() {
		//Reinitialize all of the variables to reset the game. 
		gameState = 0; // Gamestate 0 = play
		gameTime = 100;
		tmpCount = 0;
		
		blocks = new ArrayList<BarrierBlock>();
		aliens = new ArrayList<Alien>();
		
		moveShipLeft = false;
		moveShipRight = false;
		isShipFiring = false;
		shipFireRate = 10;
		shipFireTimer = 0;
		alienSpeed = 1;
		
		leaderBoard = new LeaderBoard("res/leaderBoard.txt");
		
		//Reset the gui without starting from scratch.
		gui.removeAllAssets();
		
		//Create the timer display
		gameTimetxt = new TextAsset("100",new Point(25,50));
		gameTimetxt.font = new Font ("Monospace", Font.BOLD | Font.ITALIC , 24);
		gameTimetxt.color = Color.GREEN;
		gui.addTextAsset(gameTimetxt);
		
		//Init assets and ad them to the gui.
		player = new Ship("res/ship.png",new Point(gui.getWidth() / 2 , gui.getHeight()-30));
		
		aliens.addAll(makeAliens((new Rectangle(0,0,gui.getWidth() - (gui.getWidth() / 2), gui.getHeight() - (gui.getHeight() / 2)))));
		for(int i = 0; i < aliens.size(); i++) {
			gui.addAsset(aliens.get(i));
		}
		
		for(int i = 0; i < gui.getWidth() / 100; i++) {
			//Add blocks
			blocks.addAll(makeBarrier(new Rectangle( 25 + ((gui.getWidth() / (gui.getWidth() / 100)) * i), (gui.getHeight() - 100), 50 , 50)));
		}
		
		for(int i = 0; i < blocks.size(); i++) {
			gui.addAsset(blocks.get(i));
		}
		
		gui.addAsset(player);


		//Start the game loop.
		gameTimer.start();
		
	}
	
	public void loop() {
		//main game loop.
		loopPlayerLazers();
		loopPlayer();
		loopBlocks();
		loopAliens();
		
		tmpCount++;
		if(tmpCount == 100) {
			tmpCount=0;
			gameTime--;
			gameTimetxt.setText(gameTime+"");
		}
		
		if(checkWin()) {
			gameState = 1;
			gameTimer.stop();
			
		}
		
		loopAlienLazer(); // Make sure this comes last it can restart the game
	}
	
	private boolean checkWin() {
		if(aliens.size() == 0) {
			
			gameTimetxt.setLoc(new Point(150,250));
			gameTimetxt.setText("SCORE: " + gameTime);
			gameTimetxt.font = new Font ("Monospace", Font.BOLD | Font.ITALIC , 36);
			gameTimetxt.color = Color.GREEN;
			
			misctxt = new TextAsset("ENTER YOUR INITIALS:", new Point(100,300));
			misctxt.font = new Font ("Monospace", Font.BOLD | Font.ITALIC , 24);
			misctxt.color = Color.GREEN;
			gui.addTextAsset(misctxt );
			
			userInputtxt = new TextAsset("", new Point(200,350));
			userInputtxt.font = new Font ("Monospace", Font.BOLD | Font.ITALIC , 36);
			userInputtxt.color = Color.GREEN;
			
			gui.addTextAsset(gameTimetxt);
			gui.addTextAsset(userInputtxt);
			
			gui.addAssetTOP(new Asset("res/win.jpg", new Point(0,0)));
			return true;
		}
		
		return false;
	}

	
	private void loopPlayer() {
		if(moveShipLeft) {
			//for(int i = 0; i < 10; i++)
				player.move(-5, 0);
		}
		if(moveShipRight) {
			player.move(5, 0);
		}
		
		if(isShipFiring) {
			
			if(shipFireTimer == 0) {
				player.fireLazer(gui);
			}
			
			shipFireTimer++;
			if(shipFireTimer >= shipFireRate) {
				shipFireTimer = 0;
			}
		}
	}
	
	private void loopBlocks() {
		for(int i = blocks.size() - 1; i >=0 ; i--) {
			for(int x = player.lazers.size() - 1;x >= 0; x--) {
					if(i < blocks.size() && x < player.lazers.size() && x >= 0 && i >= 0) {
						if(blocks.get(i).intersects(player.lazers.get(x))) {
							
							player.lazers.get(x).delete();
							player.lazers.remove(x);
							
							blocks.get(i).delete();
							blocks.remove(i);
							
						}
					}
			}
			
			for(int x = aliens.size() - 1; x >= 0; x--) {
				for(int l = aliens.get(x).lazers.size() - 1; l >= 0; l--) {
					
					if(i < blocks.size() && l < aliens.get(x).lazers.size() && l >= 0 && i >= 0) {
						if(blocks.get(i).intersects(aliens.get(x).lazers.get(l))) {
							blocks.get(i).delete();
							blocks.remove(i);
							
							aliens.get(x).lazers.get(l).delete();
							aliens.get(x).lazers.remove(l);

						}

					}
	
				}
			}
		}
	}
	
	private void loopPlayerLazers() {
		for(int i = player.lazers.size() - 1; i >= 0; i--) {
			player.lazers.get(i).move(0, -10);
			
			for(int x = aliens.size() - 1; x >= 0; x--) {
				if(i < player.lazers.size() && x < aliens.size() && player.lazers.get(i).intersects(aliens.get(x))) {
					
					if(x != 0) {
						aliens.get(0).lazers.addAll(aliens.get(x).lazers);
					}
										
					aliens.get(x).delete();
					aliens.remove(x);
					
					player.lazers.get(i).delete();
					player.lazers.remove(i);
					
				}
			}
			
			if(i < player.lazers.size()  && player.lazers.get(i).getY() < 0) {
				player.lazers.get(i).delete();
				player.lazers.remove(i);
			}
		}
	}
	
	private void loopAliens() {
		
		boolean hit = false;
		
		for(int i = 0; i < aliens.size(); i++) {
			aliens.get(i).move(alienSpeed, 0);
			
			if(aliens.get(i).getX() > gui.getWidth() || aliens.get(i).getX() < 0) {		
				hit = true;
				break;
			}
		}
		
		if(hit) {
			alienSpeed = alienSpeed * -1;
			for(int i = 0; i < aliens.size(); i++) {
				aliens.get(i).move(alienSpeed, 20);

			}
		}
	}
	
	private void loopAlienLazer() {
		for(int i = 0;i < aliens.size(); i++) {
			if(Math.random() < .001) {  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				aliens.get(i).fireLazer(gui);
			}
		}

		for(int i = aliens.size() - 1; i >= 0; i--) {
			for(int l = aliens.get(i).lazers.size() - 1; l >= 0; l--) {
				aliens.get(i).lazers.get(l).move(0, 5);
				
				if(aliens.get(i).lazers.get(l).intersects(player) || aliens.get(i).getY() > 400) {
					//Game Over
					gui.removeAllAssets();
					gameState = -1;
					gui.addAssetTOP(new Asset("res/gameOver.jpg", new Point(0,0)));
					gui.addAssetTOP(new AnimatedAsset(AnimatedAsset.getFrameList("res/redKey#.jpg", 0, 1), new Point(125,400), 500));
					gameTimer.stop();
					
					
					//init();
					return;
				}
				
				
			}
		}
		
	}
	
	
	public void keyPressed(KeyEvent e) {
		
		
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			moveShipLeft=true;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			moveShipRight=true;
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			isShipFiring = true;
		}
		
		if(gameState == -1) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				init();
			}
		}		
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER && gameState == 2) {
			init();
		}
		
		if(gameState == 1) {
			//Get user initials

			if(e.getKeyCode() != KeyEvent.VK_BACK_SPACE) {
				if(userInputtxt.getText().length() < 3  && (e.getKeyChar() > 'a' && e.getKeyChar() < 'z') || (e.getKeyChar() > 'A' && e.getKeyChar() < 'Z')) {
					userInputtxt.setText(userInputtxt.getText() + e.getKeyChar());
				}
			}else {
				if(userInputtxt.getText().length() != 0)
					userInputtxt.setText(userInputtxt.getText().substring(0,userInputtxt.getText().length() - 1));
			}
			if(e.getKeyCode() == KeyEvent.VK_ENTER && userInputtxt.getText().length() == 3 ) {
				//Display the leader board
				leaderBoard.addScore(userInputtxt.getText(), gameTime);
				leaderBoard.writeLeaderBoard("res/leaderBoard.txt");
								
				gui.removeAllAssets();
				misctxt = new TextAsset(leaderBoard.toString(), new Point(125,100));
				misctxt.color = Color.GREEN;
				misctxt.font = new Font ("Monospace", Font.BOLD | Font.ITALIC , 24);
				gui.addTextAsset(misctxt);
				
				AnimatedAsset press = new AnimatedAsset(AnimatedAsset.getFrameList("res/greenKey#.jpg", 0, 1), new Point(125,400),500);
				gui.addAsset(press);
				
				gameState = 2;

			}
		}
		
		
		

		
		
	
	}
	
	
	public void keyReleased(KeyEvent e) {
		
		
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			moveShipLeft=false;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			moveShipRight=false;
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			isShipFiring = false;
			
		}
		
		
		
	}

	
	public void keyTyped(KeyEvent e) {
		
	}
	
	public ArrayList<BarrierBlock> makeBarrier(Rectangle bounds) {
		ArrayList<BarrierBlock> blocks = new ArrayList<BarrierBlock>();
		
		for(int x = bounds.x; x < bounds.width + bounds.x; x+= 10) {
			for(int y = bounds.y; y < bounds.height + bounds.y; y+= 10) {
				blocks.add(new BarrierBlock("res/block.png", new Point(x,y)));
			}
		}
		
		return blocks;
	}
	
	public ArrayList<Alien> makeAliens(Rectangle bounds) {
		ArrayList<Alien> blocks = new ArrayList<Alien>();
		
		for(int x = bounds.x; x < bounds.width + bounds.x; x+= 32) {
			for(int y = bounds.y; y < bounds.height + bounds.y; y+= 32) {
				blocks.add(new Alien("res/Alien.png", new Point(x,y)));
			}
		}
		
		return blocks;
	}
	

	
}
