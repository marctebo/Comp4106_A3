import java.util.ArrayList;
import java.util.Random;

public class DepTree {
	
	private ArrayList<DepNode> tree;
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
}
