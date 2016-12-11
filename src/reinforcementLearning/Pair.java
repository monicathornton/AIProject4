package reinforcementLearning;

public class Pair {
	int x;
	int y;
	public Pair(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	@Override
	  public boolean equals(Object o) {
	    if (!(o instanceof Pair)) return false;
	    Pair pairo = (Pair) o;
	    if(pairo.x != this.x || pairo.y != this.y){
	    	return false;
	    }
	    return true;
	  }

}
