
public class DepNode {

	private DepNode parent;
	private int p0;
	private int p1;
	
	public DepNode(DepNode parent, int p0, int p1){
		this.parent = parent;
		this.p0 = p0;
		this.p1 = p1;
	}
	
	public void setParent(DepNode parent){
		this.parent = parent;
	}
	
	public DepNode getParent(){
		return parent;
	}

	public int getP0() {
		return p0;
	}

	public void setP0(int p0) {
		this.p0 = p0;
	}

	public int getP1() {
		return p1;
	}

	public void setP1(int p1) {
		this.p1 = p1;
	}
}
