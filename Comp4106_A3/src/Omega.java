import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class Omega{
		int[] probs = new int[10];
		int[] p0,p1;
		int[][] data;
		int[][] wineData;
		int size,inc;
		DepTree tree;
		int[][] testing;
		int[][] training;
		
		public Omega(int[] probs){
			this.probs = probs;
		}
		
		public Omega(int[] p0, int[] p1){
			this.p0 = p0;
			this.p1 = p1;
			tree = new DepTree(p0,p1);
			data = tree.generateData();
		}
		
		public Omega(int[][] wineData, int size,int inc){
			this.wineData = wineData;
			this.size = size;
			this.inc = inc;
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
		public double[] countTraining(){
			int[] totals = new int[10];
			for(int j = 0;j<10;j++){
				for(int i=0;i<1600;i++){
					if(training[i][j]==0){
						totals[j]++;
					}
				}
			}
			double[] per = new double[10];
			for(int i=0;i<10;i++){
				per[i] = totals[i]/1600.0;
			}
			return per;
		}
		
		public void printTotals(){
			int[] temp = countData();
			for(int i = 0;i<temp.length;i++){
				double per1 = (2000 - temp[i]);
				double per2 = per1/2000;
				System.out.println("X" + (i+1) +": " + per2 + "%");
			}
			System.out.println();
		}
		
		public void printData(){
			for(int i=0;i<2000;i++){
				for(int j=0;j<probs.length;j++){
					System.out.print(data[i][j] + " ");
				}
				System.out.println();
			}
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
								if(data[m][j] == l){
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
		
		public int[][] getData(){
			return data;
		}
		
		public void generateTT(int[] foldStart, int fold){
			testing = new int[400][10];
			training = new int[1600][10];

			for(int i=0;i<2000;i++){
				for(int j=0;j<10;j++){
					if(i<foldStart[fold]){
						training[i][j] = data[i][j];
					}
					else if(i>= foldStart[fold] && i< foldStart[fold+1]){
						testing[i-(foldStart[fold])][j] = data[i][j];
					}
					else if(i>foldStart[fold+1]){
						getTraining()[i-foldStart[1]][j] = data[i][j];
					}
				}
			}
		}
			
		public void generateWineTT(int[] foldStart, int fold, int max){
			testing = new int[inc][14];
			training = new int[max-inc+1][14];
			

			for(int i=0;i<size;i++){
				for(int j=0;j<14;j++){
					if(i<foldStart[fold]){
						training[i][j] = wineData[i][j];
					}
					else if(i>= foldStart[fold] && i< foldStart[fold+1]){
						testing[i-(foldStart[fold])][j] = wineData[i][j];
					}
					else if(i>foldStart[fold+1]){
						getTraining()[i-inc][j] = wineData[i][j];
					}
					
				}
			}
		}
		public double[] countWineTraining(){
			int[] totals = new int[14];
			for(int j = 0;j<14;j++){
				for(int i=0;i<size-inc;i++){
					if(training[i][j]==0){
						totals[j]++;
					}
				}
			}
			double[] per = new double[14];
			for(int i=0;i<10;i++){
				per[i] = totals[i]/((size-inc)*1.0);
			}
			return per;
		}
		public double[][] countTrainingDependent(DepTree tree){
			double[][] prob = new double[2][10];
			int count;
			for(DepNode node: tree.getTree()){
				if(node.isRoot()){
					count = 0;
					for(int i = 0; i<1600;i++){
						if(training[i][node.getId()-1] == 0){
							count++;
						}
					}
					prob[0][node.getId()-1] = count/1600.0;
					prob[1][node.getId()-1] = count/1600.0;
				}
				
				else{
					int cP0 = 0;
					int cP1 = 0;
					int nP0 = 0;
					int nP1 = 0;
					for(int i = 0; i<1600;i++){
						if(training[i][node.getParent().getId()-1]==0){
							nP0++;
						}
						if(training[i][node.getParent().getId()-1]==1){
							nP1++;
						}
						if(training[i][node.getId()-1] == 0 && training[i][node.getParent().getId()-1]==0){
							cP0++;
						}
						else if(training[i][node.getId()-1] == 0 && training[i][node.getParent().getId()-1]==1){
							cP1++;
						}
					}
					prob[0][node.getId()-1] = cP0/nP0;
					prob[1][node.getId()-1] = cP1/nP1;
				}
			}
			return prob;
			
		}
		
		public int[][] getWineData() {
			return wineData;
		}

		public void setWineData(int[][] wineData) {
			this.wineData = wineData;
		}
		
		public int[][] getTesting() {
			return testing;
		}

		public void setTesting(int[][] testing) {
			this.testing = testing;
		}

		public int[][] getTraining() {
			return training;
		}

		public void setTraining(int[][] training) {
			this.training = training;
		}

	}
