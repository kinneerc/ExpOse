package edu.allegheny.schemaexperiment.schemagen;

import java.util.*;

public class Util{


    public static void main(String[] args){
        int[] grouping = group(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
        for (int i : grouping)
            System.out.print(i+" ");
        System.out.println();
    }

    protected static int[] group(int groups, int individuals){

        int ans[]= new int[groups];

        ArrayList<Integer> splitpoints = groupSplitPoints(groups,individuals);

        // given a list of split points, calculate population for each group
        int gi = 0;
        int ii = 0;
        for (int sp : splitpoints){
            ans[gi++] = sp - ii;
            ii = sp;
        }
        // assign anything after last sp to final group
        ans[gi] = individuals - ii;

        return ans;

    }

    private static ArrayList<Integer> groupSplitPoints(int groups,int individuals){

        ArrayList<Integer> ans = new ArrayList<Integer>();

        Random rand = new Random();
        for (int i = 0; i < groups-1; i++){
            ans.add(rand.nextInt(individuals+1));
        }

        Collections.sort(ans);

        return ans;

    }


}
