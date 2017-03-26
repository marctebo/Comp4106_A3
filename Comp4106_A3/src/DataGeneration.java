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
		w1.printData();
		//w2.printData();
		//w3.printData();
		//w4.printData();
	}
}
