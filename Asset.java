import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Asset {

	
	BufferedImage img;
	Rectangle bounds;
	private boolean removeMe = false;
	
	public Asset(String loc, Point startLoc) {
		try {
		    img = ImageIO.read(new File(loc));
		} catch (IOException e) {
			System.out.println("IMAGE \"" + loc + "\" NOT FOUND");
			img = new BufferedImage(100,100, BufferedImage.TYPE_INT_RGB);
			
			Graphics2D    graphics = img.createGraphics();

			graphics.setPaint (new Color(255,0,255));
			graphics.fillRect ( 0, 0, img.getWidth(), img.getHeight() );
			graphics.setPaint(Color.BLACK);
			graphics.setFont(new Font("Consolas", Font.BOLD, 10)); 
			graphics.drawString("IMG NOT FOUND:", 0, 25);
			if(loc.length() > 14)
				graphics.drawString("..." + loc.substring(loc.length() - 14), 0, 50);
			else
				graphics.drawString(loc, 0, 50);

			
		}
		
		this.bounds = new Rectangle(startLoc.x, startLoc.y, img.getWidth(), img.getHeight());
	}
	
	public void setTexture(String loc) {
		
		BufferedImage tmp = img;
		try {
		    img = ImageIO.read(new File(loc));
		} catch (IOException e) {
			System.out.println("NOT FOUND");
			img = tmp;
			return;
		}
		this.bounds = new Rectangle(bounds.x, bounds.y, img.getWidth(), img.getHeight());

	}
	
	public void delete() {
		removeMe = true;
	}
	
	public Rectangle getBounds() {
		return this.bounds;
	}
	public int getX() {
		return bounds.x;
	}
	
	public int getY() {
		return bounds.y;
	}
	
	public int getW() {
		return bounds.width;
	}
	
	public int getH() {
		return bounds.height;
	}
	
	public void move(int x, int y) {
		this.bounds.x+=x;
		this.bounds.y+=y;
	}

	public boolean intersects(Asset other) {
		
		if(this.bounds.intersects(other.bounds))
			return true;
		
		return false;
		
	}

	public boolean isRemoveMe() {
		return removeMe;
	}

	
}
