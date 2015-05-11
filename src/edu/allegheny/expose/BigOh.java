package edu.allegheny.expose;

public class BigOh{

    private ComplexityClass compClass;
    private int exponent;

    public BigOh(ComplexityClass compClass){
        setCompClass(compClass);
    }

    public BigOh(){}

    /**
     * @return the compClass
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

            throw new IllegalArgumentException("Use setPower to set POWER");

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
    
    public boolean equals(BigOh o){
        if (o.compClass == this.compClass && o.exponent == this.exponent){
            return true;
        }else{
            return false;
        }
    }

}


