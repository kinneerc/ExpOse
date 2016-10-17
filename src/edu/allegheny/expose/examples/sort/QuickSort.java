package edu.allegheny.expose.examples.sort;

// Routines to sort arrays of integers.
// (c) 1997, 2001 duane a. bailey

import structure.*;
import java.lang.SuppressWarnings;

public class QuickSort
{
  static int compares;
  static int swaps;
  public static void main(String args[])
  {
    ReadStream r = new ReadStream();
    int n = r.readInt();
    int data[] = new int[n];
    int i;

    for (i = 0; i < n; i++)
    {
      data[i] = r.readInt();
    }
    compares=swaps=0;
    quickSort(data,n);
    for (i = 0; i < n; i++)
    {
      System.out.print(data[i]+" ");
      if (((i+1) % 15) == 0) System.out.println();
    }
    System.out.println();
    //System.out.println("swaps="+swaps+" compares="+compares);
  }
  public static void quickSortIterative(int data[], int n)
    // pre: n <= data.length
    // post: data[0..n-1] in ascending order
  {
    Stack callStack = new StackList();
    callStack.push(new callFrame(0,n-1));
    while (!callStack.isEmpty())
    {   // some "virtual" method outstanding
      callFrame curr = (callFrame)callStack.get();
      /*
         for (int i = 0; i < callStack.size(); i++) {
         System.out.print(' ');
         }
         System.out.println(curr);
         */
      if (curr.PC == 1) { // partition and sort lower
        // return if trivial
        if (curr.low >= curr.high) { callStack.pop(); continue; }
        // place the pivot at the correct location
        curr.pivot = partition(data,curr.low,curr.high);
        curr.PC++;
        // sort the smaller values...
        callStack.push(new callFrame(curr.low,curr.pivot-1));
      } else if (curr.PC == 2) { // sort upper
        curr.PC++;
        // sort the larger values....
        callStack.push(new callFrame(curr.pivot+1,curr.high));
      } else { callStack.pop(); continue; } // return
    }
  }

  public static void quickSort(int data[], int n)
    // post: the values in data[0..n-1] are in ascending order
  {
    quickSortRecursive(data,0,n-1);
  }

  private static int partition2(int data[], int left, int right)
    // pre: left <= right
    // post: data[left] placed in the correct (returned) location
  {
    int pivot = left;
    while (left < right)
    {
      // move left toward the right
      while (left <= right && data[left] <= data[pivot]) left++;
      // move right toward the left
      while (left <= right && data[pivot] < data[right]) right--;
      // if left < right then data[left] > data[right], so swap
      if (left < right) swap(data,left,right);
      // else right is ideal location for pivot
    }
    // Assertion: left >= right, and left points to pivot destination
    swap(data,pivot,right);
    return right;
  }

  private static int partition(int data[], int left, int right)
    // pre: left <= right
    // post: data[left] placed in the correct (returned) location
  {
    while (true)
    {
      // move right "pointer" toward left
      while (left < right && data[left] < data[right]) right--;
      if (left < right) swap(data,left++,right);
      else return left;
      // move left pointer toward right
      while (left < right && data[left] < data[right]) left++;
      if (left < right) swap(data,left,right--);
      else return right;
    }
  }

  private static void quickSortRecursive(int data[],int left,int right)
    // pre: left <= right
    // post: data[left..right] in ascending order
  {
    int pivot;   // the final location of the leftmost value
    if (left >= right) return;
    pivot = partition(data,left,right);    /* 1 - place pivot */
    quickSortRecursive(data,left,pivot-1); /* 2 - sort small */
    quickSortRecursive(data,pivot+1,right);/* 3 - sort large */
    /* done! */
  }

  public static void quickSort(Vector data,int n)
    // pre: 0 <= n <= data.size()
    // post: the values in data[0..n-1] are in ascending order
  {
    quickSortRecursive(data,0,n-1);
  }

  @SuppressWarnings("unchecked")  
  private static int partition(Vector data, int left, int right)
  // pre: 0 <= left <= right < data.size()
  // post: data[left] placed in the correct location
  {
    while (true)
    {
      // move right "pointer" toward left
      while (left < right && 
          ((Comparable)data.get(left))
          .compareTo(data.get(right)) < 0) right--;
      if (left < right) swap(data,left++,right);
      else return left;
      // move left pointer toward right
      while (left < right && 
          ((Comparable)data.get(left)).compareTo((Comparable)data.get(right)) < 0) left++;
      if (left < right) swap(data,left,right--);
      else return right;
    }
  }

  private static void quickSortRecursive(Vector data, int left, int right)
    // pre: 0 <= left <= right < data.size()
    // post: data[left..right] in order
  {
    int pivot;
    if (left >= right) return;
    // position leftmost value
    pivot = partition(data,left,right);
    // sort smaller values
    quickSortRecursive(data,left,pivot-1);
    // sort larger values
    quickSortRecursive(data,pivot+1,right);
  }

  public static void swap(int data[], int i, int j)
    // pre: 0 <= i,j < data.length
    // post: data[i] and data[j] are exchanged
  {
    int temp;
    swaps++;
    temp = data[i];
    data[i] = data[j];
    data[j] = temp;
  }


  public static void swap(Vector data, int i, int j)
    // pre: 0 <= i,j < data.size()
    // post: i-th j-th elements of data are exchanged
  {
    Object temp;
    temp = data.get(i);
    data.set(i,data.get(j));
    data.set(j,temp);
  }
}

class callFrame
{
  int pivot;// location of pivot
  int low;  // left index
  int high; // right index
  int PC; // next statement (see numbering in recursive code)

  public callFrame(int l, int h)
    // post: generate new call frame with low and high as passed
  {
    low = l; high = h; PC = 1;
  }
  public String toString()
  {
    return "<["+low+","+high+"] pivot="+pivot+" PC="+PC+">";
  }
}
/*
   100
   73 92 40 38 51 17 8 31 56 84 21 34 29 16 61 31 7 63 70 32 
   69 38 98 93 31 84 45 74 8 33 30 32 76 69 21 27 85 81 9 92 
   16 82 26 45 97 38 75 3 53 97 86 21 34 35 65 64 71 9 89 30 
   42 70 13 69 90 33 47 75 13 56 18 29 89 95 25 85 33 99 39 37 
   95 76 9 29 62 74 44 32 82 85 61 75 54 73 96 44 58 42 70 70 
   3 7 8 8 9 9 9 13 13 16 16 17 18 21 21 
   21 25 26 27 29 29 29 30 30 31 31 31 32 32 32 
   33 33 33 34 34 35 37 38 38 38 39 40 42 42 44 
   44 45 45 47 51 53 54 56 56 58 61 61 62 63 64 
   65 69 69 69 70 70 70 70 71 73 73 74 74 75 75 
   75 76 76 81 82 82 84 84 85 85 85 86 89 89 90 
   92 92 93 95 95 96 97 97 98 99 
   */
