import java.math.*;
import java.lang.Boolean;

public class Polynomial {
	
	//fields
	double [] coeffs;
	

	public Polynomial() {
		coeffs = new double[] {0};
	}
	
	
	public Polynomial(double [] arr) {
		int n = arr.length;
		coeffs = new double[n];
		
		for (int i = 0; i < n; i++) {
			coeffs[i] = arr[i];
		}
	}
	
	
	/*
	 * adds the given polynomial y to the called object.
	 */
	public Polynomial add(Polynomial y) {
		int n = y.coeffs.length;
		int m = coeffs.length;
		double [] a = new double[Math.max(n, m)];
		
		for (int i = 0; i < a.length; i++) {
			a[i] = 0;
		}
		for (int i = 0; i < n; i++) {
			a[i] += y.coeffs[i];
		}
		for (int i = 0; i < m; i++) {
			a[i] += coeffs[i];
		}
		
		return new Polynomial(a);
	}
	
	
	/*
	 * evaluates f(x) at the given double x.
	 */
	public double evaluate(double x) {
		double y = 0;
		int n = coeffs.length;
		
		for (double i = 0; i < n; i++) {
			y += coeffs[(int)i] * Math.pow(x, i);
		}
		
		return y;
	}
	
	
	/*
	 * takes a double x determines if f(x) = 0,
	 * which would indicate x is a root of f.
	 */
	public boolean hasRoot(double x) {
		if (this.evaluate(x) == 0) return true;
		return false;
	}
	
	
	
}