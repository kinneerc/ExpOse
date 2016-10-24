package edu.allegheny.expose.examples;

public class testAlgs{
    public static void main(String[] args){

        System.out.println("Testing.");
        linear(10000);
        log(10000);

    }
//Denotes linear to be approximately as fast as a single for loop
    public static int linear(long n){
        int face=1;
        for (int count = 0; count < n; count++){
            try{
                Thread.sleep(20);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        return face;
    }
//Denotes logarithmic to be a for loop whose assignment is by product instead of sum as seen above
    public static int log(long n){
        int face=1;
        for (int count = 1; count < n; count*=3){
            face = 0;
            try{
                Thread.sleep(20);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        return face;
    }
//Denotes constant to be a program that will run the same length and is not dependent on our variable n
    public static void constant(long n){
        try{
            Thread.sleep(20);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
//Denotes linearithmic to be a program which runs both a linear and a logarithmic function in conjunction with one another
    public static void linearithmic(long n){
        linear(n);
        log(n);
    }
//Denotes quadratic to be a program which runs approximately as fast as a double nested for loop, this is an addition to the program
    public static void quadratic(long n){
	for (int count = 0; count < n; count++){
	    for (int count2 = 0; count2 < n; count2++){
		    try{
                Thread.sleep(20);
            }catch(InterruptedException e){
                e.printStackTrace();
            }

                }
	    }
	}
//Denotes cubic to be approximately as fast as a triple nested for loop
    public static void cubic(long n){
        for (int count = 0; count < n; count++){
            for (int count2 = 0; count2 < n; count2++){
                for (int count3 = 0; count3 < n; count3++){

                    try{
                Thread.sleep(20);
            }catch(InterruptedException e){
                e.printStackTrace();
            }

                }
            }
        }
    }
//Denotes factorial to be a recursively defined for loop which could also approximately be considered an n! nested for loop
    public static void factorial(long n) {
        for(long i=0; i<n; i++) {
            factorial(n-1);
        }
    }

}
