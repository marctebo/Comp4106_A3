import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class DepTree {
	
	private ArrayList<DepNode> tree;
	private DepNode root;
	
	public DepTree(int[] p0, int[] p1){
		tree = new ArrayList<>();
		for(int i = 0; i<10;i++){
			tree.add(new DepNode(null, p0[i],p1[i]));
		}
		tree.get(1).setParent(tree.get(0));
		tree.get(2).setParent(tree.get(1));
		tree.get(3).setParent(tree.get(1));
		tree.get(4).setParent(tree.get(0));
		tree.get(5).setParent(tree.get(4));
		tree.get(6).setParent(tree.get(2));
		tree.get(7).setParent(tree.get(2));
		tree.get(8).setParent(tree.get(3));
		tree.get(9).setParent(tree.get(5));
	}
	
	public DepTree(double[][] arr, int size){
		int iLoc, jLoc;
		tree = new ArrayList<>();
		System.out.print("\nOrder of Connections");
		while(tree.size()<size){
			iLoc = 0;
			jLoc = 0;
			double max = 0;
			for(int i = 0; i<size;i++){
				for(int j=i+1;j<size;j++){
					if(arr[i][j]>max){
						max = arr[i][j];
						iLoc = i;
						jLoc = j;
					}
				}
			}
			DepNode a = new DepNode(iLoc+1);
			DepNode b = new DepNode(jLoc+1);
			
			if(tree.isEmpty()){
				a.setRoot(true);
				root = a;
				a.addPair(b);
				a.addChild(b);
				System.out.println("\n" + a.getId() + " added pair: " + b.getId());
				tree.add(a);
				tree.add(b);
			}
			
			else if(containsId(a.getId()) &&!containsId(b.getId())){
				DepNode temp = getNodeId(a.getId());
				temp.addPair(b);
				temp.addChild(b);
				System.out.println(a.getId() + " added pair: " + b.getId());
				tree.add(b);
			}
			
			else if(!containsId(a.getId()) && containsId(b.getId())){
				DepNode temp = getNodeId(b.getId());
				temp.addPair(a);
				temp.addChild(a);
				System.out.println(b.getId() + " added pair: " + a.getId());
				tree.add(a);
			}
			
			else if(!containsId(a.getId()) && !containsId(b.getId())){
				a.addPair(b);
				System.out.println(a.getId() + " added pair: " + b.getId() + "(not connected to root)");
				tree.add(a);
				tree.add(b);
			}
			else if(containsId(a.getId()) && containsId(b.getId())){
				DepNode temp1 = getNodeId(a.getId());
				DepNode temp2 = getNodeId(b.getId());
				if(!temp1.connectedTo(temp2.getId(),0)){
					temp1.addPair(temp2);
					System.out.println(a.getId() + " added pair: " + b.getId());
					/*if(temp1.connectedTo(root.getId(), 0)){
						temp1.addChild(temp2);
						if(temp2.getPairs().size()>1){
							temp2.addChild(temp2.getPairs().get(0));
						}
					}
					else if(temp2.connectedTo(root.getId(), 0)){
						temp2.addChild(temp1);
						if(temp1.getPairs().size()>1){
							temp1.addChild(temp1.getPairs().get(0));
						}
					}*/
					//if(temp2.getParent()!=null){}
					temp1.addChild(temp2);
					if(temp2.getPairs().size()>1){
						temp2.addChild(temp2.getPairs().get(0));
					}
				}
			}
			arr[iLoc][jLoc] = -1;
		}
		System.out.println();
		
		ArrayList<Integer> nums = new ArrayList<>();
		nums.add(tree.get(0).getId());
		
		System.out.println("DEPENDENCY TREE");

		for(DepNode d: tree){
//			for(DepNode c: d.getChildren()){
//				if(c.getChildren().contains(d)){
//					c.getChildren().remove(d);
//				}
//			}
			System.out.print("Node "+ d.getId() + " children: ");
			for(DepNode c: d.getChildren()){
				System.out.print(c.getId() + " ");
			}
			System.out.println();
		}
	}
	
	public int[][] generateData(){
		int[][] data = new int[2000][10];
		Random rand = new Random();
		int temp;
		for(int i = 0;i<2000;i++){
			for(int j=0;j<10;j++){
				temp = rand.nextInt(100) + 1;
				if(j==0){
					data[i][j] = (temp<=tree.get(0).getP0()) ? 0:1;
				}
				else{
					if(data[i][tree.indexOf(tree.get(j).getParent())]==0){
						data[i][j] = (temp<=tree.get(j).getP0()) ? 0:1;
					}
					else{
						data[i][j] = (temp<=tree.get(j).getP1()) ? 0:1;
					}
				}
			}
		}
		
		return data;
	}
	
	public static DepTree generateDepTree(int[][] data){
		double numI,numJ,numIJ;
		double prIJ,prI,prJ;
		double total;
		double[][] vals = new double[10][10];
		System.out.println("\nStrengths between Nodes");
		for(int i = 0; i<10;i++){
			System.out.print("-------- ");
			for(int j = i+1; j< 10; j++){
				total= 0.0;
				for(int k = 0;k<2;k++){
					for(int l = 0;l<2;l++){
						numI = numJ = numIJ = 0;
						for(int m = 0;m<8000;m++){
							if(data[m][i] == k){
								numI++;
							}
							else if(data[m][j] == l){
								numJ++;
							}
							if(data[m][i] == k && data[m][j] == l){
								numIJ++;
							}
						}
						prI = numI/8000;
						prJ = numJ/8000;
						prIJ = numIJ/8000;	
						total+=prIJ*Math.log10(prIJ/(prI*prJ));
					}
				}
				vals[i][j] = total;
				DecimalFormat two = new DecimalFormat("0.000");
				System.out.print(two.format(vals[i][j])+ "|" + i + j + " ");
			}
			System.out.println();
			for(int z=0;z<=i;z++){
				System.out.print("-------- ");
			}
		}
		System.out.println();
		return new DepTree(vals,10);
	}
	
	public static DepTree generateWineDepTree(int[][] data){
		double numI,numJ,numIJ;
		double prIJ,prI,prJ;
		double total;
		double[][] vals = new double[14][14];
		System.out.println("\nStrengths between Nodes");
		for(int i = 1; i<14;i++){
			System.out.print("-------- ");
			for(int j = i+1; j< 14; j++){
				total= 0.0;
				for(int k = 0;k<2;k++){
					for(int l = 0;l<2;l++){
						numI = numJ = numIJ = 0;
						for(int m = 0;m<178;m++){
							if(data[m][i] == k){
								numI++;
							}
							else if(data[m][j] == l){
								numJ++;
							}
							if(data[m][i] == k && data[m][j] == l){
								numIJ++;
							}
						}
						prI = numI/178;
						prJ = numJ/178;
						prIJ = numIJ/178;	
						total+=prIJ*Math.log10(prIJ/(prI*prJ));
					}
				}
				vals[i][j] = total;
				DecimalFormat two = new DecimalFormat("0.000");
				System.out.print(two.format(vals[i][j])+ "|" + i + j + " ");
			}
			System.out.println();
			for(int z=0;z<=i;z++){
				System.out.print("-------- ");
			}
		}
		System.out.println();
		return new DepTree(vals,14);
	}
	public boolean containsId(int id){
		for(DepNode node: tree){
			if(node.getId() == id){
				return true;
			}
		}
		return false;
	}
	
	public DepNode getNodeId(int id){
		for(DepNode node: tree){
			if(node.getId() == id){
				return node;
			}
		}
		return null;
	}
	public ArrayList<DepNode> getTree() {
		return tree;
	}

}

