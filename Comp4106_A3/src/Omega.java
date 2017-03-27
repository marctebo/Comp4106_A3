import java.text.DecimalFormat;
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
		
		public void generateDepTree(){
			double numI,numJ,numIJ;
			double prIJ,prI,prJ;
			double total;
			double[][] vals = new double[10][10];
			for(int i = 0; i<10;i++){
				for(int j = i+1; j< 10; j++){
					total= 0.0;
					for(int k = 0;k<2;k++){
						for(int l = 0;l<2;l++){
							numI = numJ = numIJ = 0;
							for(int m = 0;m<2000;m++){
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
							prI = numI/2000;
							prJ = numJ/2000;
							prIJ = numIJ/2000;	
							total+=prIJ*Math.log(prIJ/(prI*prJ));
						}
					}
					vals[i][j] = total;
					DecimalFormat two = new DecimalFormat("0.000");
					System.out.print(two.format(vals[i][j])+ "|" + i  + j + " ");
				}
				System.out.println();
				for(int z=0;z<=i;z++){
					System.out.print("         ");
				}
			}
			
		}
	}
