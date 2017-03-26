import java.util.Random;

public class Omega{
		int[] probs = new int[10];
		int[] p0,p1;
		int[][] data;
		DepTree tree;
		
		public Omega(int[] probs){
			this.probs = probs;
		}
		
		public Omega(int[] p0, int[] p1){
			this.p0 = p0;
			this.p1 = p1;
			tree = new DepTree(p0,p1);
			data = tree.generateData();
		}
		
		public void populateData(){
			data =  new int[2000][10];
			Random rand = new Random();
			int temp;
			for(int j=0;j<probs.length;j++){
				for(int i=0;i<2000;i++){
					temp = rand.nextInt(100)+1;
					if(temp> probs[j]){
						data[i][j] = 1;
					}
				}
			}
		}
		
		public int[] countData(){
			int[] totals = new int[10];
			
			for(int j = 0;j<probs.length;j++){
				for(int i=0;i<2000;i++){
					totals[j]+=data[i][j];
				}
			}
			return totals;
		}
		
		public void printTotals(){
			int[] temp = countData();
			for(int i = 0;i<temp.length;i++){
				double per1 = (2000 - temp[i]);
				double per2 = per1/2000;
				System.out.println("X" + (i+1) +": " + per2 + "%");
			}
		}
		public void printData(){
			for(int i=0;i<2000;i++){
				for(int j=0;j<probs.length;j++){
					System.out.print(data[i][j] + " ");
				}
				System.out.println();
			}
			printTotals();
		}
	}
