import java.util.Random;

public class Omega{
		int[] probs = new int[10];
		int[][] data;
		
		public Omega(int[] probs){
			this.probs = probs;
		}
		
		public void populateData(){
			data =  new int[2000][10];
			Random rand = new Random();
			int temp;
			for(int j=0;j<probs.length;j++){
				for(int i=0;i<2000;i++){
					temp = rand.nextInt(100)+1;
					if(temp<=probs[j]){
						data[i][j] = 0;
					}
					else{
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
				System.out.println("X" + (i+1) +": " + temp[i]);
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
