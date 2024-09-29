/* Polynomial.java
 * @author Andrey Skvortsov
 * 
 * Works with single-variable polynomials represented as arrays of coefficients 
 * corresponding to exponents of the polynomial's variable.
 * 
 * Note: exponents are assumed to be non-negative.
 */

import java.math.*;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;

public class Polynomial {
	
	//fields
	public double [] coeffs;
	public int [] exps;
	

	/*
	 * empty constructor
	 */
	public Polynomial() {
		coeffs = new double[] {};
		exps = new int[] {};
	}
	
	
	/* Constructor(double [] arr)
	 * copies the polynomial represented by the given array into a Polynomial object
	 */
	public Polynomial(double [] co, int [] ex) {
		int n = Math.min(co.length, ex.length);
		coeffs = new double[n];
		exps = new int[n];
		
		for (int i = 0; i < n; i++) {
			coeffs[i] = co[i];
			exps[i] = ex[i];
		}
	}
	
	
	/* Constructor(File)
	 * generates polynomial based on the contents of the argument file
	 */
	public Polynomial(File in) {		
		this();  //inits Polynomial to empty for case that file [in] is empty (*)
		try {
			Scanner input = new Scanner(in);
			String text = input.nextLine();
				if (text.isEmpty()) return;  //(*)
			String [] terms = formatInputString(text);  //formats the string for parsing
			String [] parts;  //used to store the parts (coeff & exp) of each term
			
			exps = new int[terms.length];
			coeffs = new double[terms.length];
			for (int i = 0; i < terms.length; i++) {
				parts = terms[i].split("x");
				//now first # of parts is coefficient, second is exponent
				if (parts.length == 1) {  //edge case for x^0 terms
					exps[i] = 0;
					coeffs[i] = Double.parseDouble(parts[0]);
				} else {
					exps[i] = Integer.parseInt(parts[1]);
					coeffs[i] = Double.parseDouble(parts[0]);
				}
			}
			
			Polynomial x = combineLikeTerms1(exps, coeffs);
			exps = x.exps;
			coeffs = x.coeffs;
			
		} catch (FileNotFoundException e) {
			System.out.println("issue reading file");
			System.exit(1);
		}
	}
	
	
	
	
	/*
	 * saves the calling object polynomial in a text file of name [name] 
	 */
	public void saveToFile(String name) {
		String result = "";
		//concatenate coefficient, then x, then exponent
		//if coefficient is positive and result non-empty, concatenat "+" first
		for (int i = 0; i < exps.length; i++) {			
			if (coeffs[i] > 0 && !result.isEmpty()) result = result.concat("+");
			if (exps[i] == 0) result = result.concat(Double.toString(coeffs[i]));
			else if (exps[i] == 1) result = result.concat(coeffs[i] + "x");
			else result = result.concat(coeffs[i] + "x" + exps[i]);
		}
		
		//save result to external file
		try {
			FileWriter writer = new FileWriter(name);
			writer.write(result);
			writer.close();
			return;
		} catch (IOException e) {
			System.out.println("issue saving to file");
			System.exit(1);
		}
		return;
	}
	
	
	/*
	 * adds the given polynomial y to the called object.
	 * Note: "1" suffix is for exponent array, "2" suffix for coefficient array
	 */
	public Polynomial add(Polynomial y) {		
		int [] e = new int[exps.length + y.exps.length];
		double [] c = new double[coeffs.length + y.coeffs.length];
		for (int i = 0; i < exps.length; i++) {
			e[i] = exps[i];
			c[i] = coeffs[i];
		}
		for (int i = exps.length; i < exps.length + y.exps.length; i++) {
			e[i] = y.exps[i - exps.length];
			c[i] = y.coeffs[i - exps.length];
		}
		
		return combineLikeTerms1(e, c);
	}
	
	
	/*
	 * DEPRECATED; REPLACED BY [add]
	 */
	/*public Polynomial add(Polynomial y) {
		int [] a1 = new int[exps.length + y.exps.length];
		double [] a2 = new double[coeffs.length + y.coeffs.length];
		
		for (int i = 0; i < a1.length; i++) {  //init new polynomial fields
			a1[i] = 0;
			a2[i] = 0;
		}
		for (int i = 0; i < exps.length; i++) {  //load with first polynomial
			a1[i] = exps[i];
			a2[i] = coeffs[i];
		}
		/* For each exponent in second poly., add its coeff to existing exponent
		 * in new array (if there is a match) or insert it as a new exponent with
		 * coefficient in next available index of new array (if no match)
		 *
		for (int i = 0; i < y.exps.length; i++) {
			for (int j = 0; j < exps.length; j++) {
				if (y.exps[i] == a1[j]) {
				//if match; combine coeffs and break loop
					a2[j] += y.coeffs[i];
					break;
				}
				else if (j == exps.length - 1) {
				//if no match & end of (inner) loop; append to end of new array
					a1[exps.length + i] = y.exps[i];
					a2[coeffs.length + i] = y.coeffs[i];
				}
			}
		}
		
		return removeZeroCoeffs(a1, a2);	
	}*/
	
	
	/*
	 * evaluates f(x) at the given double x.
	 */
	public double evaluate(double x) {
		double y = 0;
		int n = Math.min(coeffs.length, exps.length);
		
		for (int i = 0; i < n; i++) {
			y += coeffs[i] * Math.pow(x, (double)exps[i]);
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
	
	
	/*
	 * returns the Polynom. resulting from multiplying argument & calling object
	 */
	public Polynomial multiply(Polynomial a) {
		int [] e = new int[exps.length * a.exps.length];
		double [] c = new double[coeffs.length * a.coeffs.length];
		//multiplying all terms
		for (int i = 0; i < exps.length; i++) {
			for (int j = 0; j < a.exps.length; j++) {
				e[i*a.exps.length + j] = exps[i] + a.exps[j];
				c[i*a.coeffs.length + j] = coeffs[i] * a.coeffs[j];				
			}
		}
		
		Polynomial result = combineLikeTerms1(e, c);
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//HELPER FUNCTIONS BELOW
	
	
	/*
	 * What else to add: for continuous sequences of +-+-..., evaluate them
	 * to single + or - chars (not needed for Lab 2)
	 */
	private String [] formatInputString(String text) {
		text = text.replaceAll("-\\+|\\+-", "-");  //note "\\" for regex char "+"
		text = text.replaceAll(("x\\+"), "x1+");  //to make reading coeffs [1] easier
		text = text.replaceAll("x-", "x1-");
		text = text.replaceAll("\\+x", "+1x");
		text = text.replaceAll("-x", "-1x");
		if (text.charAt(text.length() - 1) == 'x') text = text.concat("1");
		if (text.charAt(0) == 'x') text = text.replaceFirst("x", "1x");
		text = text.replaceAll("-", "+-");
		text = text.replace("+", " ");  //changing all + to spaces
		text = text.strip();  //get rid of surrounding unneeded spaces
		
		return text.split(" ");
	}
	
	
	/*
	 * DEPRECATED, REPLACED BY [combineLikeTerms1]
	 */
	/*private void combineLikeTerms(int [] e, double [] c) {		
		int num_distincts = getNumDistincts(e);
		
		//sets exps and coeffs sizes based on # of distinct terms in e
		exps = new int[num_distincts];
		coeffs = new double[num_distincts];
		exps[0] = e[0];  //sets the first term, then will iterate through the rest
		coeffs[0] = c[0];
		//for each term in e, add it to exps if not already there. (*)
		//if already in exps, add the corresponding coefficient to coeffs (**)
		for (int i = 1, k = 1; i < e.length; i++) {
			for (int j = 0; j < k; j++) {
				if (e[i] == exps[j]) {  //(**)
					coeffs[j] += c[i];
					break;
				}
				else if (j == k - 1) {  //(*), on the last loop iter'n
					exps[k] = e[i];
					coeffs[k] = c[i];
					k++;
					break;
				}
			}
		}
		
		Polynomial x = removeZeroCoeffs(exps, coeffs);
		exps = x.exps;
		coeffs = x.coeffs;
		return;
	}*/
	
	
	/*
	 * 
	 */
	private Polynomial combineLikeTerms1(int [] e, double [] c) {		
		int num_distincts = getNumDistincts(e);
		
		//sets exps and coeffs sizes based on # of distinct terms in e
		int [] e0 = new int[num_distincts];
		double [] c0 = new double[num_distincts];
		e0[0] = e[0];  //sets the first term, then will iterate through the rest
		c0[0] = c[0];
		//for each term in e, add it to exps if not already there. (*)
		//if already in exps, add the corresponding coefficient to coeffs (**)
		for (int i = 1, k = 1; i < e.length; i++) {
			for (int j = 0; j < k; j++) {
				if (e[i] == e0[j]) {  //(**)
					c0[j] += c[i];
					break;
				}
				else if (j == k - 1) {  //(*), on the last loop iter'n
					e0[k] = e[i];
					c0[k] = c[i];
					k++;
					break;
				}
			}
		}
		
		return removeZeroCoeffs(e0, c0);
	}
	
	
	/*
	 * 
	 */
	private Polynomial removeZeroCoeffs(int [] e, double [] c) {
		int num_zeros = 0;
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 0) num_zeros++;
		}
		
		int [] e0 = new int[e.length - num_zeros];
		double [] c0 = new double[c.length - num_zeros];
		for (int i = 0, j = 0; i < e.length; i++) {
			if (c[i] == 0) {
				continue;
			}
			else {
				e0[j] = e[i];
				c0[j] = c[i];
				j++;
			}
		}

		return new Polynomial(c0, e0);
	}
	
	
	/*
	 * 
	 */
	private int getNumDistincts(int [] e) {
		int [] distincts = new int[e.length];
		distincts[0] = e[0];
		int num_distincts = 1;
		int zero_exp = 0;  //accounts for java defaulting ints of distincts to 0
		for (int i = 1; i < e.length; i++) {
			for (int j = 0; j < i; j++) {
				if (e[i] == 0) {
					zero_exp = 1;
					break;
				}
				if (e[i] == distincts[j]) break;
				else if (j == i - 1) {
					distincts[i] = e[i];
					num_distincts++;
				}
			}
		}
		num_distincts += zero_exp;
		
		return num_distincts;
	}
	
	
	
}