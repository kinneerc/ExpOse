/**
 * @author Cody Kinneer
 *
 * The purpose of this file is to determine the 'bigOh' value of a given algorithm
 */

package edu.allegheny.expose;

/**
 * Declaring the class BigOh.
 */
public class BigOh{

    private ComplexityClass compClass;  //Declares new private variables
    private int exponent;  //Declares a new private integer, exponent

    /**
     *The bigOh method calls the setCompClass method
     *@param compClass the compClass that will be set
     *@return Nothing -- results in a function call
     */
    public BigOh(ComplexityClass compClass){
        setCompClass(compClass);
    }

    public BigOh(){}

    /**
     * @return compClass
     */
    public ComplexityClass getCompClass() {
        return compClass;
    }

    /**
     * Method to set all but POWER class
     * @param compClass the compClass to set
     */
    public void setCompClass(ComplexityClass compClass) {
        if (compClass != ComplexityClass.POWER){

            this.compClass = compClass;
            /**
             * Determines exponent number based on whether or not compClass is quadratic, cubic, etc.Create
             */
            if (compClass == ComplexityClass.QUADRADIC)
                exponent = 2;
            else if (compClass == ComplexityClass.CUBIC)
                exponent = 3;
            else if (compClass == ComplexityClass.LINEAR || compClass == ComplexityClass.LINEARITHMIC)
                exponent = 1;
            else if (compClass == ComplexityClass.EXPONENTIAL)
                exponent = Integer.MAX_VALUE;
            else
                exponent = 0;

        }else{

            throw new IllegalArgumentException("Use setPower to set POWER"); //Throw exception if compClass equals ComplexityClass.POWER

        }

    }


    /**
     * @return the exponent
     */
    public int getExponent() {
        return exponent;
    }

    /**
     * Set a power complexity class
     * @param exponent the exponent to set
     */
    public void setPower(int exponent) {
        compClass = ComplexityClass.POWER;
        this.exponent = exponent;
    }

    @Override
    public String toString(){
        if (compClass == ComplexityClass.POWER)
            return compClass.toString() + " " + exponent;
        else
            return compClass.toString();
    }

    /**
     * boolean function returns true or false.
     * @param o Object that contains compClass and exponent variables
     */
    public boolean equals(BigOh o){
        if (o.compClass == this.compClass && o.exponent == this.exponent){
            return true;
        }else{
            return false;
        }
    }

}
