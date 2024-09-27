import java.io.File;

public class Driver {
	public static void main(String [] args) {
		/*Polynomial p = new Polynomial();
		System.out.println(p.evaluate(3));
		double [] c1 = {6,0,0,5};
		Polynomial p1 = new Polynomial(c1);
		double [] c2 = {0,-2,0,0,-9};
		Polynomial p2 = new Polynomial(c2);
		Polynomial s = p1.add(p2);
		System.out.println("s(0.1) = " + s.evaluate(0.1));
		if(s.hasRoot(1))
			System.out.println("1 is a root of s");
		else
			System.out.println("1 is not a root of s");
		 */
		
	
		//Lab 2 testing
		double[] co_a = new double[] {-1, 0, 1, 100, 23};
		int[] ex_a = new int[] {7, 500, 1, 3, 500};
		Polynomial a = new Polynomial(ex_a, co_a);
		
		double[] co_b = new double[] {1, 99, 1, 0};
		int[] ex_b = new int[] {0, 3, 1, 500};
		Polynomial b = new Polynomial(ex_b, co_b);
		
			//test that a was created properly
			System.out.println("a in driver:");
			for (int i = 0; i < a.coeffs.length; i++) {
				System.out.print(a.coeffs[i] + ", ");
			}
			System.out.println("");
			for (int i = 0; i < a.exps.length; i++) {
				System.out.print(a.exps[i] + ", ");
			}
			System.out.println("");
			
			//test that b was created properly
			System.out.println("b in driver:");
			for (int i = 0; i < b.coeffs.length; i++) {
				System.out.print(b.coeffs[i] + ", ");
			}
			System.out.println("");
			for (int i = 0; i < b.exps.length; i++) {
				System.out.print(b.exps[i] + ", ");
			}
			System.out.println("");
		
		/*
		Polynomial c = a.multiply(b);
			//test that c was created properly
			for (int i = 0; i < c.coeffs.length; i++) {
				System.out.print(c.coeffs[i] + ", ");
			}
			System.out.println("");
			for (int i = 0; i < c.exps.length; i++) {
				System.out.print(c.exps[i] + ", ");
			}*/

		Polynomial d = a.add(b);
			//test that d was created properly
			for (int i = 0; i < d.coeffs.length; i++) {
				System.out.print(d.coeffs[i] + ", ");
			}
			System.out.println("");
			for (int i = 0; i < d.exps.length; i++) {
				System.out.print(d.exps[i] + ", ");
			}
		
		File example = new File("example_p.txt");
		Polynomial x = new Polynomial(example);
			/*//test that x was created properly
			for (int i = 0; i < x.coeffs.length; i++) {
				System.out.print(x.coeffs[i] + ", ");
			}
			System.out.println("");
			for (int i = 0; i < x.exps.length; i++) {
				System.out.print(x.exps[i] + ", ");
			}*/

		x.saveToFile("test");
			
	}
}