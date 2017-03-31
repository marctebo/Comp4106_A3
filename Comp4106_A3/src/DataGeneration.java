import java.text.DecimalFormat;
import java.util.ArrayList;

public class DataGeneration {

	private static final int[] p10 = {20, 34, 51, 77, 72, 14, 70, 40, 81, 12};
	private static final int[] p11 = {-1, 67, 27, 43, 40, 47, 20, 38, 47, 5};
	
	private static final int[] p20 = {71, 59, 82, 55, 31, 41, 83, 24, 22, 36};
	private static final int[] p21 = {-1, 23, 49, 81, 45, 89, 38, 50, 59, 41};
	
	private static final int[] p30 = {65, 18, 11, 9, 22, 25, 66, 21, 60, 17};
	private static final int[] p31 = {-1, 70, 36, 37, 18, 61, 42, 38, 52, 56};
	
	private static final int[] p40 = {05, 95, 64, 94, 87, 20, 79, 46, 73, 65};
	private static final int[] p41 = {-1, 32, 52, 58, 23, 50, 63, 93, 40, 39};
	
	private static final int[] foldStart = {0,400,800,1200,1600,2000};
	/*
	 * Probabilities represent the chance of getting a 0
	 */
	
	public static void crossValidationIndependent(ArrayList<Omega> classes){
		System.out.println("\nIndependent 5-fold");
		int fold;
		ArrayList<double[]> wProb;
		DecimalFormat two = new DecimalFormat("0.00");
		double[] averages = new double[5];
		for(fold = 0;fold<5;fold++){
			wProb = new ArrayList<>();
			for(Omega o: classes){
				o.generateTT(foldStart, fold);
				wProb.add(o.countTraining());			
			}
			int count = 0;
			double rollingAverage=0.0;
			for(Omega o: classes){
				int sum = 0;
				for(int i=0;i<400;i++){
					double[] probs = {1.0,1.0,1.0,1.0};
					for(int j=0;j<10;j++){
						if(o.getTesting()[i][j] ==0){
							probs[0]*=wProb.get(0)[j];
							probs[1]*=wProb.get(1)[j];
							probs[2]*=wProb.get(2)[j];
							probs[3]*=wProb.get(3)[j];
						}
						else{
							probs[0]*=(1.0-wProb.get(0)[j]);
							probs[1]*=(1.0-wProb.get(1)[j]);
							probs[2]*=(1.0-wProb.get(2)[j]);
							probs[3]*=(1.0-wProb.get(3)[j]);
						}
					}
					double max = probs[0];
					for(int k=1;k<4;k++){
						if(probs[k]>max){
							max = probs[k];
						}
					}
					if(probs[count] == max){
						sum++;
					}
				}
				rollingAverage+=sum/400.0;
				count++;
			}
			averages[fold] = rollingAverage/4.0;
			System.out.println("Fold " + (fold+1) + " Accuracy: " + two.format(100.0*averages[fold]) +"%");
		}
		double totalAverage = 0.0;
		for(int z=0;z<5;z++){
			totalAverage+=averages[0];
		}
		System.out.println("Average for all folds: "+ two.format(100.0*totalAverage/5.0)+"%");

	}

	public static void crossValidationDependent(ArrayList<Omega> classes, DepTree tree){
		System.out.println("\nDependent 5-fold");
		int fold;
		ArrayList<double[][]> wProb;
		DecimalFormat two = new DecimalFormat("0.00");
		double[] averages = new double[5];
		for(fold = 0;fold<5;fold++){
			wProb = new ArrayList<>();
			for(Omega o: classes){
				o.generateTT(foldStart, fold);
				wProb.add(o.countTrainingDependent(tree));			
			}
			int count = 0;
			double rollingAverage=0.0;
			for(Omega o: classes){
				int sum = 0;
				for(int i=0;i<400;i++){
					double[] probs = {1.0,1.0,1.0,1.0};
					for(int j=0;j<10;j++){
						if(tree.getNodeId(j+1).isRoot()){
							probs[0]*=wProb.get(0)[0][j];
							probs[1]*=wProb.get(1)[0][j];
							probs[2]*=wProb.get(2)[0][j];
							probs[3]*=wProb.get(3)[0][j];
						}
						else{
							int k = tree.getNodeId(j+1).getParent().getId()-1;
							if(o.getTesting()[i][j] == 0 && o.getTesting()[i][k] == 0){
								probs[0]*=wProb.get(0)[0][j];
								probs[1]*=wProb.get(1)[0][j];
								probs[2]*=wProb.get(2)[0][j];
								probs[3]*=wProb.get(3)[0][j];
							}
							else if(o.getTesting()[i][j] == 0 && o.getTesting()[i][k] == 1){
								probs[0]*=wProb.get(0)[1][j];
								probs[1]*=wProb.get(1)[1][j];
								probs[2]*=wProb.get(2)[1][j];
								probs[3]*=wProb.get(3)[1][j];
							}
							else if(o.getTesting()[i][j] == 1 && o.getTesting()[i][k] == 0){
								probs[0]*=(1.0-wProb.get(0)[0][j]);
								probs[1]*=(1.0-wProb.get(1)[0][j]);
								probs[2]*=(1.0-wProb.get(2)[0][j]);
								probs[3]*=(1.0-wProb.get(3)[0][j]);
							}
							else if(o.getTesting()[i][j] == 1 && o.getTesting()[i][k] == 1){
								probs[0]*=(1.0-wProb.get(0)[1][j]);
								probs[1]*=(1.0-wProb.get(1)[1][j]);
								probs[2]*=(1.0-wProb.get(2)[1][j]);
								probs[3]*=(1.0-wProb.get(3)[1][j]);
							}
						}
					}
					double max = probs[0];
					for(int k=1;k<4;k++){
						if(probs[k]>max){
							max = probs[k];
						}
					}
					if(probs[count] == max){
						sum++;
					}
				}
				rollingAverage+=sum/400.0;
				count++;
			}
			averages[fold] = rollingAverage/4.0;
			System.out.println("Fold " + (fold+1) + " Accuracy: " + two.format(100.0*averages[fold]) +"%");
		}
		double totalAverage = 0.0;
		for(int z=0;z<5;z++){
			totalAverage+=averages[0];
		}
		System.out.println("Average for all folds: "+ two.format(100.0*totalAverage/5.0)+"%");
	}
	
	public static void main(String args[]){
		
		Omega w1 = new Omega(p10,p11);
		Omega w2 = new Omega(p20,p21);
		Omega w3 = new Omega(p30,p31);
		Omega w4 = new Omega(p40,p41);
		
		int[][] allData = new int[8000][10];
		ArrayList<Omega> classes = new ArrayList<>();
		classes.add(w1);
		classes.add(w2);
		classes.add(w3);
		classes.add(w4);
		int count = 0;
		for(Omega o: classes){
			for(int i=0;i<2000;i++){
				for(int j=0;j<10;j++){
					allData[count][j] = o.getData()[i][j];
				}
				count++;
			}
		}
		/*w1.printData();
		w1.generateTT(foldStart, 4);
		System.out.println("\n\n\n\n\n");
		for(int i=0;i<400;i++){
			for(int j=0;j<10;j++){
				System.out.print(w1.getTesting()[i][j] + " ");
			}
			System.out.println();
		}
		*/
		System.out.println("Omega 1 feature rate");
		w1.printTotals();
		System.out.println("Omega 2 feature rate");
		w2.printTotals();
		System.out.println("Omega 3 feature rate");
		w3.printTotals();
		System.out.println("Omega 4 feature rate");
		w4.printTotals();
		
		DepTree estimatedTree = DepTree.generateDepTree(allData);
		crossValidationIndependent(classes);
		crossValidationDependent(classes, estimatedTree);
		
		//w1.generateDepTree();
		//w2.printData();
		//w3.printData();
		//w4.printData();
	}
}
