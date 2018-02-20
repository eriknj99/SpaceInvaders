import java.awt.Point;
import java.util.ArrayList;

public class Ship extends Asset{

	ArrayList<Lazer> lazers;
	private final String LAZER_TEXTURE = "res/lazer/lazer#.png";
	
	public Ship(String loc, Point startLoc) {
		super(loc, startLoc);
		
		lazers = new ArrayList<Lazer>();
	}
	
	public void fireLazer(GUI g) {
		lazers.add(new Lazer(AnimatedAsset.getFrameList(LAZER_TEXTURE, 0, 1 ), new Point(this.bounds.x + 14, this.bounds.y)));
		g.addAsset(lazers.get(lazers.size() - 1));
	}
	
	

}
