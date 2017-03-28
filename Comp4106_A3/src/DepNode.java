import java.util.ArrayList;


public class DepNode {

	private DepNode parent;
	private int id;
	private int p0;
	private int p1;
	private boolean root;
	private ArrayList<DepNode> pairs;
	
	public DepNode(DepNode parent, int p0, int p1){
		this.parent = parent;
		this.p0 = p0;
		this.p1 = p1;
	}
	
	public boolean connectedTo(int id, int pairId){
		Boolean b = false;
		for(DepNode node: pairs){
			if(node.getId()!= pairId){
				if(node.getId() == id){
					return true;
				}
				if(!node.getPairs().isEmpty()){
					b = node.connectedTo(id,this.getId());
				}
			}
		}
		return b;
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public DepNode(int id){
		this.id = id;
		root = false;
		pairs = new ArrayList<DepNode>();
	}
	
	public void addPair(DepNode node){
		if(!pairs.contains(node)){
			pairs.add(node);
			node.addPair(this);
		}
	}
	public ArrayList<DepNode> getPairs(){
		return pairs;
	}
	public boolean isRoot() {
		return root;
	}

	public void setRoot(boolean root) {
		this.root = root;
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
	
	public static void main(String args[]){
		DepNode a,b,c,d;
		a = new DepNode(5);
		b = new DepNode(6);
		c = new DepNode(3);
		d = new DepNode(7);
		a.addPair(b);
		b.addPair(c);
		c.addPair(d);
		
		System.out.print(a.connectedTo(7,0));
	}
}
