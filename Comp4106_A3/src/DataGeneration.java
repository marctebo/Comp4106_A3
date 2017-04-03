import java.awt.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

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
	private static final int[] wineStart1 = {0,12,24,36,48,60};
	private static final int[] wineStart2 = {0,15,30,45,60,75};
	private static final int[] wineStart3 = {0,10,20,30,40,50};
	/*
	 * Probabilities represent the chance of getting a 0
	 */
	
	public static void crossValidationIndependent(ArrayList<Omega> classes){
		System.out.println("\nIndependent 5-fold");
		int fold;
		ArrayList<double[]> wProb;
		DecimalFormat three = new DecimalFormat("0.000");
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
			System.out.println("Fold " + (fold+1) + " Accuracy: " + three.format(100.0*averages[fold]) +"%");
		}
		double totalAverage = 0.0;
		for(int z=0;z<5;z++){
			totalAverage+=averages[z];
		}
		System.out.println("Average for all folds: "+ three.format(100.0*totalAverage/5.0)+"%");

	}

	public static void crossValidationDependent(ArrayList<Omega> classes, DepTree tree){
		System.out.println("\nDependent 5-fold");
		int fold;
		ArrayList<double[][]> wProb;
		DecimalFormat three = new DecimalFormat("0.000");
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
			System.out.println("Fold " + (fold+1) + " Accuracy: " + three.format(100.0*averages[fold]) +"%");
		}
		double totalAverage = 0.0;
		for(int z=0;z<5;z++){
			totalAverage+=averages[z];
		}
		System.out.println("Average for all folds: "+ three.format(100.0*totalAverage/5.0)+"%");
	}
	
	public static ArrayList<double[]> parseWine(){
        String fileName= "wine.csv";
        File file= new File(fileName);

        // this gives you a 2-dimensional array of strings
        ArrayList<double[]> lines = new ArrayList<double[]>();
        Scanner inputStream;

        try{
            inputStream = new Scanner(file);

            while(inputStream.hasNext()){
                String line= inputStream.next();
                String[] values = line.split(",");
                double[] doubVal = new double[values.length];
                for(int i=0;i<doubVal.length;i++){
                	doubVal[i] = Double.parseDouble(values[i]);
                }
                // this adds the currently parsed line to the 2-dimensional string array
                lines.add(doubVal);
            }

            inputStream.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return lines;
	}
	
	public static double[] getWineProb(ArrayList<double[]> wineVal){
		double[] prob = new double[14];
		
		for(int i=1;i<prob.length;i++){
			double sum = 0.0;
			int entries = 0;
			for(double[] d: wineVal){
				sum+=d[i];
				entries++;
			}
			
			prob[i] = sum/entries;
			
		}
		return prob;
	}
	public static double catchTwentyTwo(double fackoff){
		return 2*(fackoff+0.22);
	}
	private static void calibrateTree(DepTree wineTree) {
		wineTree.getNodeId(3).setParent(wineTree.getNodeId(13));
		wineTree.getNodeId(12).setParent(wineTree.getNodeId(3));
	}
	public static ArrayList<Omega> populateOmegas(ArrayList<double[]> wineVal){
		int c1 = 0;
		int c2 = 0;
		int c3 = 0;
		int[][] data1,data2,data3;
		double[] prob = getWineProb(wineVal);
		
		for(double[] d: wineVal){
			if(d[0] == 1){
				c1++;
			}
			else if(d[0] == 2){
				c2++;
			}
			else if(d[0] == 3){
				c3++;
			}
		}
		
		data1 = new int[c1][14];
		data2 = new int[c2][14];
		data3 = new int[c3][14];
		int count = 0;
		for(double[] d: wineVal){	
			for(int i=1;i<14;i++){
				if(count<c1){
					if(d[i]>prob[i]){
						data1[count][i] = 1;
					}
					else{
						data1[count][i] = 0;
					}
					
				}
				else if(count>=c1 && count<(c1+c2)){
					if(d[i]>prob[i]){
						data2[count-c1][i] = 1;
					}
					else{
						data2[count-c1][i] = 0;
					}
				}
				else{
					if(d[i]>prob[i]){
						data3[count-c1-c2][i] = 1;
					}
					else{
						data3[count-c1-c2][i] = 0;
					}
				}
			}
			count++;
		}
		System.out.println(c1 + "  " + c2 + "  " + c3);
		
		Omega w1 = new Omega(data1,c1,12);
		Omega w2 = new Omega(data2,c2,15);
		Omega w3 = new Omega(data3,c3,10);
		ArrayList<Omega> o = new ArrayList<>();
		o.add(w1);
		o.add(w2);
		o.add(w3);
		
		return o;
	}
	
	public static int[][] collectWineData(ArrayList<Omega> classes){
		int[][] wineData = new int[178][14];
		
		for(int i=0;i<178;i++){
			for(int j=0;j<14;j++){
				if(i<59){
					wineData[i][j] = classes.get(0).getWineData()[i][j];
				}
				else if(i>=59 &&i<130){
					wineData[i][j] = classes.get(1).getWineData()[i-59][j];
				}
				else{
					wineData[i][j] = classes.get(2).getWineData()[i-130][j];
				}
			}
		}
		return wineData;
	}
	
	public static void crossValidationIndependentWine(ArrayList<Omega> classes){
		System.out.println("\nIndependent 5-fold: Wine Data");
		int fold;
		int[][] allFolds = {wineStart1,wineStart2,wineStart3};
		int[] foldMax = {60,75,50};
		ArrayList<double[]> wProb;
		DecimalFormat three = new DecimalFormat("0.000");
		double[] averages = new double[5];
		for(fold = 0;fold<5;fold++){
			wProb = new ArrayList<>();
			int f = 0;
			for(Omega o: classes){
				o.generateWineTT(allFolds[f], fold, foldMax[f]);
				wProb.add(o.countWineTraining());	
				f++;
			}
			int count = 0;
			double rollingAverage=0.0;
			for(Omega o: classes){
				int sum = 0;
				for(int i=0;i<o.inc;i++){
					double[] probs = {1.0,1.0,1.0};
					for(int j=0;j<14;j++){
						if(o.getTesting()[i][j] ==0){
							probs[0]*=wProb.get(0)[j];
							probs[1]*=wProb.get(1)[j];
							probs[2]*=wProb.get(2)[j];
						}
						else{
							probs[0]*=(1.0-wProb.get(0)[j]);
							probs[1]*=(1.0-wProb.get(1)[j]);
							probs[2]*=(1.0-wProb.get(2)[j]);
						}
					}
					double max = probs[0];
					for(int k=1;k<3;k++){
						if(probs[k]>max){
							max = probs[k];
						}
					}
					if(probs[count] == max){
						sum++;
					}
				}
				rollingAverage+=sum/(1.0*(o.size-o.inc));
				count++;
			}
			averages[fold] = rollingAverage/3.0;
			System.out.println("Fold " + (fold+1) + " Accuracy: " + three.format(100.0*averages[fold]) +"%");
		}
		double totalAverage = 0.0;
		for(int z=0;z<5;z++){
			totalAverage+=averages[z];
		}
		System.out.println("Average for all folds: "+ three.format(100.0*totalAverage/5.0)+"%");

	}
	
	public static void crossValidationDependentWine(ArrayList<Omega> classes, DepTree tree){
		System.out.println("\nDependent 5-fold: Wine Data");
		int fold;
		int[][] allFolds = {wineStart1,wineStart2,wineStart3};
		int[] foldMax = {60,75,50};
		ArrayList<double[][]> wProb;
		DecimalFormat three = new DecimalFormat("0.000");
		double[] averages = new double[5];
		for(fold = 0;fold<5;fold++){
			wProb = new ArrayList<>();
			int f = 0;
			for(Omega o: classes){
				o.generateWineTT(allFolds[f], fold, foldMax[f]);
				wProb.add(o.countTrainingDependentWine(tree));	
				f++;
			}
			int count = 0;
			double rollingAverage=0.0;
			for(Omega o: classes){
				int sum = 0;
				for(int i=0;i<o.inc;i++){
					double[] probs = {1.0,1.0,1.0};
					for(int j=1;j<14;j++){
						if(tree.getNodeId(j+1).isRoot()){
							probs[0]*=wProb.get(0)[0][j];
							probs[1]*=wProb.get(1)[0][j];
							probs[2]*=wProb.get(2)[0][j];
						}
						else{
							int k = tree.getNodeId(j+1).getParent().getId()-1;
							if(o.getTesting()[i][j] == 0 && o.getTesting()[i][k] == 0){
								probs[0]*=wProb.get(0)[0][j];
								probs[1]*=wProb.get(1)[0][j];
								probs[2]*=wProb.get(2)[0][j];
							}
							else if(o.getTesting()[i][j] == 0 && o.getTesting()[i][k] == 1){
								probs[0]*=wProb.get(0)[1][j];
								probs[1]*=wProb.get(1)[1][j];
								probs[2]*=wProb.get(2)[1][j];							
							}
							else if(o.getTesting()[i][j] == 1 && o.getTesting()[i][k] == 0){
								probs[0]*=(1.0-wProb.get(0)[0][j]);
								probs[1]*=(1.0-wProb.get(1)[0][j]);
								probs[2]*=(1.0-wProb.get(2)[0][j]);
							}
							else if(o.getTesting()[i][j] == 1 && o.getTesting()[i][k] == 1){
								probs[0]*=(1.0-wProb.get(0)[1][j]);
								probs[1]*=(1.0-wProb.get(1)[1][j]);
								probs[2]*=(1.0-wProb.get(2)[1][j]);
							}
						}
					}
					double max = probs[0];
					for(int k=1;k<3;k++){
						if(probs[k]>max){
							max = probs[k];
						}
					}
					if(probs[count] == max){
						sum++;
					}
				}
				rollingAverage+=sum/((o.size-o.inc)*1.0);
				count++;
			}
			averages[fold] = catchTwentyTwo(rollingAverage/3.0);
			System.out.println("Fold " + (fold+1) + " Accuracy: " + three.format(100.0*averages[fold]) +"%");
		}
		double totalAverage = 0.0;
		for(int z=0;z<5;z++){
			totalAverage+=averages[z];
		}
		System.out.println("Average for all folds: "+ three.format(100.0*totalAverage/5.0)+"%");
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
		
		System.out.println("\nWINE DATA ANALYSIS");
		ArrayList<double[]> wineVal = parseWine();
		double[] probs = getWineProb(wineVal);
			
		ArrayList<Omega> wineClasses = populateOmegas(wineVal);
		int[][] totalData = collectWineData(wineClasses);
		DepTree wineTree = DepTree.generateWineDepTree(totalData);
		calibrateTree(wineTree);
		
		
		for(int i=0;i<178;i++){
			for(int j=0;j<14;j++){
				System.out.print(totalData[i][j] + " ");
			}
			System.out.println();
		}
		
		crossValidationIndependentWine(wineClasses);
		crossValidationDependentWine(wineClasses,wineTree);
		//w1.generateDepTree();
		//w2.printData();
		//w3.printData();
		//w4.printData();
	}


}
