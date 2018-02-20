import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

public class TextAsset{

	private String text;
	private Point loc;
	
	Font font = new Font ("TimesNewRoman", Font.BOLD , 12);
	Color color = Color.WHITE;
	
	public TextAsset(String text, Point loc) {
		super();
		this.text = text;
		this.loc = loc;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Point getLoc() {
		return loc;
	}
	public void setLoc(Point loc) {
		this.loc = loc;
	}
	
	

}
