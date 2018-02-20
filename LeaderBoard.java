import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class LeaderBoard {

	ArrayList<String> names;
	ArrayList<Integer> scores;
	
	
	public LeaderBoard(String loc) {
		super();
		this.names = new ArrayList<String>();
		this.scores = new ArrayList<Integer>();
		
		readLeaderBoardFile(loc);
		
		sort();
		
	}
	
	public void addScore(String name, int score) {
		names.add(name);
		scores.add(score);
		sort();
	}

	public void sort() {
		
		for(int i = 0; i < scores.size() - 1; i++) {
			for(int j = 0; j < scores.size() - i - 1; j++) {
				if(scores.get(j) > scores.get(j + 1)) {
					Integer tmp = scores.get(j);
					scores.set(j, scores.get(j+1));
					scores.set(j+1,tmp);
					
					String tmpS = names.get(j);
					names.set(j, names.get(j+1));
					names.set(j+1,tmpS);
				}
			}
		}
		
	}
	public void writeLeaderBoard(String loc) {
		
			sort();
		 	String str = "";
		 	
		 	for(int i = 0; i < names.size(); i++) {
				str+=(names.get(i) + " " + scores.get(i) + "\n");
		 	}
		    BufferedWriter writer;
			try {
				writer = new BufferedWriter(new FileWriter(loc));
				 writer.write(str);
				 writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//System.out.println(str);
		   
	}
	
	private void readLeaderBoardFile(String loc) {
		
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<Integer> scores = new ArrayList<Integer>();

		File file = new File(loc);
		 
		try {
		
		  BufferedReader br;
		  br = new BufferedReader(new FileReader(file));
		
		  String st;
		  while ((st = br.readLine()) != null)
			if(st != null && st.contains(" ")) {
				names.add(st.substring(0,st.indexOf(" ")));
		    	scores.add(Integer.parseInt(st.substring(st.indexOf(" ")).trim()));
			}
		  
		  br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		this.names = names;
		this.scores = scores;
		
		/*for(int i = 0; i < names.size(); i++) {
			System.out.println(names.get(i) + " " + scores.get(i));
		}*/
	}
	
	public String toString() {
		String str = "LEADER BOARD\n\n";
	 	
	 	for(int i = names.size() - 1;i >= 0 && i >= names.size() - 5; i--) {
			str+=((names.size() - i) + ". " + names.get(i) + " " + scores.get(i) + "\n" + "---------------\n" );
	 	}
	 	
	 	return str;
	}
}
