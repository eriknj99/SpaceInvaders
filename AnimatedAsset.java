import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import gif_convert.Converter;

public class AnimatedAsset extends Asset implements ActionListener {

	//AnimatedAsset aa = new AnimatedAsset(AnimatedAsset.getFrameList("res/loading/frame_#_delay-0.1s.png", 0, 11), new Point(200,200), 50);

	private ArrayList<BufferedImage> imgs;
	private Timer t;
	private int currentFrame;
	
	
	//Use animated gif
	public AnimatedAsset(String gifLoc, Point startLoc, int frameUpdateRate) {
		super(gifLoc, startLoc);
		
		imgs = new ArrayList<BufferedImage>();


		if(new File(gifLoc).exists()) {
			try {
				imgs = Converter.convertGif(gifLoc);
			} catch (IOException e) {
				
			}
		}else {
			System.out.println("IMAGE \"" + gifLoc + "\" NOT FOUND");

			for(int i = 0; i < 255; i++) {
				BufferedImage error = new BufferedImage(100,100, BufferedImage.TYPE_INT_RGB);
				Graphics2D    graphics = error.createGraphics();
	
				graphics.setPaint (new Color(255,0,i));
				graphics.fillRect ( 0, 0, img.getWidth(), img.getHeight() );
				graphics.setPaint(Color.BLACK);
				graphics.setFont(new Font("Consolas", Font.BOLD, 10)); 
				graphics.drawString("IMG NOT FOUND:", 0, 25);
				if(gifLoc.length() > 14)
					graphics.drawString("..." + gifLoc.substring(gifLoc.length() - 14), 0, 50);
				else
					graphics.drawString(gifLoc, 0, 50);
				
				imgs.add(error);
			}
			
		}
				
		
		img = imgs.get(0);
		this.bounds = new Rectangle(startLoc.x, startLoc.y, imgs.get(0).getWidth(), imgs.get(0).getHeight());
		
		currentFrame = 0;
		
		t = new Timer(frameUpdateRate, this);
		t.start();
		
		
	}
	
	//Use an array of frame locations
	public AnimatedAsset(ArrayList<String> frames, Point startLoc, int frameUpdateRate) {
		super(frames.get(0), startLoc);
		
		imgs = new ArrayList<BufferedImage>();

		
		for(int i = 0; i < frames.size(); i++) {
			try {
			    imgs.add(ImageIO.read(new File(frames.get(i))));
			} catch (IOException e) {
				System.out.println("IMAGE \"" + frames.get(i) + "\" NOT FOUND");

					BufferedImage error = new BufferedImage(100,100, BufferedImage.TYPE_INT_RGB);
					Graphics2D    graphics = error.createGraphics();
		
					graphics.setPaint (new Color(255,0,i));
					graphics.fillRect ( 0, 0, img.getWidth(), img.getHeight() );
					graphics.setPaint(Color.BLACK);
					graphics.setFont(new Font("Consolas", Font.BOLD, 10)); 
					graphics.drawString("IMG NOT FOUND:", 0, 25);
					if(frames.get(i).length() > 14)
						graphics.drawString("..." + frames.get(i).substring(frames.get(i).length() - 14), 0, 50);
					else
						graphics.drawString(frames.get(i), 0, 50);
	
		
					imgs.add(error);
				
			}
		}
		
		img = imgs.get(0);
		this.bounds = new Rectangle(startLoc.x, startLoc.y, imgs.get(0).getWidth(), imgs.get(0).getHeight());
		
		currentFrame = 0;
		
		t = new Timer(frameUpdateRate, this);
		t.start();
	}
	
	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(currentFrame == imgs.size()) {
			currentFrame = 0;
		}
		
		
		super.img = imgs.get(currentFrame);
		currentFrame++;
	}
	

	public static ArrayList<String> getFrameList(String input,int min, int max){
		
		if(!input.contains("#")) {
			System.out.println("ERROR NO '#' IN INPUT ABORTING");
			return null;
		}
		
		ArrayList<String> out = new ArrayList<String>();
		
		for(int i = min; i <= max; i++) {
			out.add(input.replaceAll("#", i+""));
			
			//System.out.println(out.get(out.size() - 1));
		}
		
		
		
		return out;
	}

	
}
