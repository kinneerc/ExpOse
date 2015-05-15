package edu.allegheny.expose.tune.josephus;

import net.datastructures.*;
import java.util.Random;

public class JosephusSolver {

  /** Solution of the Josephus problem using a queue. */
  public static <E> E Josephus(Queue<E> Q, int k) {
    if (Q.isEmpty()) return null;
    while (Q.size() > 1) {
      /* System.out.println("  Queue: " + Q + "  k = " + k); */
      for (int i=0; i < k; i++) 
        Q.enqueue(Q.dequeue());  // move the front element to the end
      Q.dequeue(); // remove the front element from the collection 
      /* System.out.println("    " + e + " is out");       */
      }
    return Q.dequeue();  // the winner
  }

  /** Build a queue from an array of objects */
  public static <E> Queue<E> buildQueue(E a[]) {
    Queue<E> Q = new NodeQueue<E>();
    for (int i=0; i<a.length; i++)
      Q.enqueue(a[i]);
    return Q;
  }

  /** Tester method */
  public static void main(String[] args) {
      int n = Integer.parseInt(args[0]);
      int k = Integer.parseInt(args[1]);
      String[] victims = getChildren(n);
      System.out.println("The winner is " + Josephus(buildQueue(victims), k));
     }

  public static String[] getChildren(int n){
      Random rand = new Random();
      String[] possible = {"Alice", "Bob", "Cindy", "Doug", "Ed", "Fred", "Gene", "Hope", "Irene",
                           "Jack", "Kim", "Lance", "Mike", "Roberto", "Cody", "Luke", "Abby",
                           "Shu Yi","Dr. Kapfhammer"};

      String[] chosen = new String[n];

      for (int count = 0; count < n; count++){
        int index = rand.nextInt(possible.length-1);
        chosen[count] = possible[index];
      }

      return chosen;
  }
}
