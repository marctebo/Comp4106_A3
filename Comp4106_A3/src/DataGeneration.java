public class DataGeneration {

	/*
	 * Probabilities represent the chance of getting a 0
	 */
	public static void main(String args[]){
		int[] w1probs = {25,45,75,65,35,10,80,50,20,30};
		Omega w1 = new Omega(w1probs);
		w1.populateData();
		w1.printData();
	}
}
