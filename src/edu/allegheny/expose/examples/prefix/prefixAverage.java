/*
 * Copyright 2014, Michael T. Goodrich, Roberto Tamassia, Michael H. Goldwasser
 *
 * Developed for use with the book:
 *
 *    Data Structures and Algorithms in Java, Sixth Edition
 *    Michael T. Goodrich, Roberto Tamassia, and Michael H. Goldwasser
 *    John Wiley & Sons, 2014
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.allegheny.expose.examples.prefix;

import java.util.Arrays;

/**
 * Demonstration of algorithms for testing element prefixAverage.
 *
 * @author Michael T. Goodrich
 * @author Roberto Tamassia
 * @author Michael H. Goldwasser
 */
class prefixAverage {

  /** Returns an array a such that, for all j, a[j] equals the average of x[0], ..., x[j].*/
  public static int[] prefix1(int[] x) {
    int n = x.length;
    int[] a = new int[n];		// filled with zeros by default
    for (int j=0; j<n; j++){
	int total = 0;		// begin computing x[0]+...+x[j]
	for (int i=0; i<=j; i++)
	  total += x[i];
	a[j] = total/(j+1);		// record the average
	}
	return a;
  }

  /** Returns an array a such that, for all j, a[j] equals the average of x[0], ..., x[j].*/
  public static int[] prefix2(int[] x) {
    int n = x.length;
    int[] a = new int[n];		// filled with zeros by default
    int total = 0;			// compute prefix sum as x[0]+x[1]+...
    for (int j=0; j<n; j++){
	total += x[j];			// update prefix sum to include x[j]
	a[j] = total/(j+1);		// record the average
	}
	return a;
  }


}
