package edu.allegheny.schemaexperiment.schemagen;

import java.util.*;

public class Util{


    public static void main(String[] args){
        int[] grouping = group(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
        for (int i : grouping)
            System.out.print(i+" ");
        System.out.println();
    }

    /**
     *  Given a list of split points, return a frequency table
     */
    protected static int[] group(int[] splitPoints){

        Arrays.sort(splitPoints);

        int[] ans = new int[splitPoints.length];

         // given a list of split points, calculate population for each group
        int gi = 0;
        int ii = 0;
        for (int sp : splitPoints){
            ans[gi++] = sp - ii;
            ii = sp;
        }

        return ans;

    }

    protected static int[] group(int groups, int individuals){

        int[] splitpoints = groupSplitPoints(groups,individuals);
       
        return group(splitpoints);

    }

    protected static int[] groupSplitPoints(int groups,int individuals){

        int[] ans = new int[groups];

        Random rand = new Random();
        for (int i = 0; i < groups-1; i++){
            ans[i] = (rand.nextInt(individuals+1));
        }

        // need at least one split point at the end to guarentee the entire set is included
        ans[groups-1] = individuals;

        Arrays.sort(ans);

        return ans;

    }


}
