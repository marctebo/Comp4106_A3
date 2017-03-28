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
	
	/*
	 * Probabilities represent the chance of getting a 0
	 */
	
	
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
		System.out.println("Omega 1 feature rate");
		w1.printTotals();
		System.out.println("Omega 2 feature rate");
		w2.printTotals();
		System.out.println("Omega 3 feature rate");
		w3.printTotals();
		System.out.println("Omega 4 feature rate");
		w4.printTotals();
		
		DepTree.generateDepTree(allData);
		//w1.generateDepTree();
		//w2.printData();
		//w3.printData();
		//w4.printData();
	}
}
