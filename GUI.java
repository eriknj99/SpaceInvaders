import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.Timer;

public class GUI extends JFrame implements KeyListener{
	
	
	private ArrayList<BufferedImage> frameBuffer;
	private ArrayList<Asset> assets;
	private ArrayList<TextAsset> txtAssets;
	private Timer buffer;
	private Timer draw;
	private Game game;
	
	
	

	
	public GUI(Game g) {
		
		frameBuffer = new ArrayList<BufferedImage>();
		assets = new ArrayList<Asset>();
		txtAssets = new ArrayList<TextAsset>();
		
		this.game = g;
		this.addKeyListener(this);
		
		this.setBounds(0,0,500,500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		initTimers();		
	}
	
	public void setTitle(String title) {
		this.setTitle(title);
	}
	
	public void addTextAsset(TextAsset a) {
		txtAssets.add(a);
	}
	
	public void addAsset(Asset a) {
		assets.add(a);
	}
	
	public void addAssetTOP(Asset a) {
		assets.add(0,a);
	}
	
	
	public void removeAllAssets() {
		assets = new ArrayList<Asset>();
		txtAssets = new ArrayList<TextAsset>();
		frameBuffer = new ArrayList<BufferedImage>();
		
		
		renderFrame();
		repaint();
	}
	
	private void initTimers() {
		buffer = new Timer(10, null);
		buffer.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	        	renderFrame();
	        }
	    });
		
		
		draw = new Timer(10, null);
		draw.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	        	repaint();

	        }
	    });
		
		buffer.start();
		draw.start();
	}
	
	public void paint(Graphics g) {
		//super.paint(g);
		if(frameBuffer.size() > 0) {
			g.drawImage(frameBuffer.get(0), 0, 0, null);
			frameBuffer.remove(0);
		}
		

	}
	
	public void renderFrame() {

		
		BufferedImage t = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		
		
		Graphics2D gO = t.createGraphics();

		
		
		for(int i = assets.size() - 1; i >= 0 ;i--) {
			
			if(assets.get(i).isRemoveMe()) {
				assets.remove(i);
				i--;
			}
			
			if(i >= 0 && i < assets.size())
				gO.drawImage(assets.get(i).img, assets.get(i).bounds.x, assets.get(i).bounds.y, assets.get(i).bounds.width , assets.get(i).bounds.height, null);		
		
		}
		
		for(int i = 0; i < txtAssets.size(); i++) {
			gO.setFont(txtAssets.get(i).font);
			gO.setColor(txtAssets.get(i).color);
			int xOff = 0;
			int yOff = 0;
			
			String[] s = txtAssets.get(i).getText().split("\n");
			for(String tmp : s) {
				gO.drawString(tmp,(int)txtAssets.get(i).getLoc().getX() + xOff, (int)txtAssets.get(i).getLoc().getY() + yOff);
				yOff+=gO.getFontMetrics().getHeight();
			}
			
			//gO.drawString(txtAssets.get(i).getText(), (int)txtAssets.get(i).getLoc().getX(), (int)txtAssets.get(i).getLoc().getY());
		}

		frameBuffer.add(t);
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		game.keyPressed(arg0);
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		game.keyReleased(arg0);
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		game.keyTyped(arg0);
	}

}
