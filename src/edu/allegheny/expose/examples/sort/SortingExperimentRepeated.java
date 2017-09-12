package edu.allegheny.expose.examples.sort;

import org.junit.*;

import edu.allegheny.expose.BigOh;
import edu.allegheny.expose.ComplexityClass;

public class SortingExperimentRepeated{

    private static final BigOh quadratic = new BigOh(ComplexityClass.QUADRATIC);    // changed "QUADRADIC"to "QUADRATIC"
    private static final BigOh linearithmic = new BigOh(ComplexityClass.LINEARITHMIC);


   public static void main(String[] args){

       int experiments = 0;
       int correct = 0;

       for (int count = 0; count < Integer.parseInt(args[0]); count++){


      for (String a : SortingExperiment.algs) {
          BigOh ans = SortingExperiment.doubleExp(a);
          System.out.println("Ran "+a+" and got "+ans);

          experiments++;

          if (correct(ans,a))
              correct++;

      }
       }
       System.out.println("Got "+correct+" out of "+experiments+".\n Acheived "+(double) correct / (double) experiments * 100.00 + "% accuracy.");
   }


   public static boolean correct(BigOh ans, String alg){
       switch (alg){
            case "quick": return ans.equals(linearithmic);
            case "insertion": return ans.equals(quadratic);
            case "merge": return ans.equals(linearithmic);
            case "selection": return ans.equals(quadratic);
            case "bubble": return ans.equals(quadratic);
            default: System.out.println("Invalid choice, use: quick, insertion, merge, selection, or bubble");
                     return false;
        }

   }

}
