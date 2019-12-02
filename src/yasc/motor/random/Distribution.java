package yasc.motor.random;

import java.util.Random;

/**
 * This class generates various random variables for
 * distributions not directly supported in Java
 */
public class Distribution extends Random {

    public Distribution() {
        super();
    }

    public Distribution(long seed) {
        super(seed);
    }
    
    //http://www.math.csusb.edu/faculty/stanton/probstat/poisson/Distribution.java
    public int nextPoisson(double lambda) {
        double elambda = Math.exp(-1 * lambda);
        double product = 1;
        int count = 0;
        int result = 0;
        while (product >= elambda) {
            product *= nextDouble();
            result = count;
            count++;
        }
        return result;
    }

    public double nextExponential(double b) {
        double randx;
        double result;
        randx = nextDouble();
        result = -1 * b * Math.log(randx);
        return result;
    }

    //Code iSPD 1.0
    public double nextNormal(double media, double desvioPadrao) {
        double soma12 = 0.0;
        for (int i = 0; i < 12; i++) {
            soma12 = soma12 + this.nextDouble();
        }
        return (media + (desvioPadrao * (soma12 - 6.0)));
    }

    //http://www.cs.huji.ac.il/labs/parallel/workload/m_lublin99/m_lublin99.c
    public double twoStageUniform(double low, double med, double hi) {
        double a;
        double b;
        double tsu;
        double u;
        
        double prob = (med - low)/(hi-low);

        u = this.nextDouble();
        if (u <= prob) { /* uniform(low , med) */
            a = low;
            b = med;
        } else { /* uniform(med , hi) */
            a = med;
            b = hi;
        }

        // Generate a value of a random variable from distribution uniform(a,b) 
        tsu = (this.nextDouble() * (b - a)) + a;
        return tsu;
    }

    public double nextLogNormal(double media, double desvioPadrao) {
        double y = Math.exp(this.nextNormal(media, desvioPadrao));
        return y;
    }

    public double nextWeibull(double scale, double shape) {
        return scale * Math.pow(-Math.log(1 - this.nextDouble()), 1 / shape);
    }
}