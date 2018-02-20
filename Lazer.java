import java.awt.Point;
import java.util.ArrayList;

public class Lazer extends AnimatedAsset{

	public Lazer(String gifLoc, Point startLoc) {
		super(gifLoc, startLoc, 50);
	}
	
	public Lazer(ArrayList<String> frames, Point startLoc) {
		super(frames, startLoc, 50);
	}

}
