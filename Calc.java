import java.util.function.Function;

public class Calc {
    public static void main(String[] args) {
        // Ellipse ellipse1 = new Ellipse(1.5, 0.75);
        // System.out.printf("%.6f\n", ellipse1.calcCircumference());
        
        // Ellipse ellipse2 = new Ellipse(2.25, 1.625);
        // System.out.printf("%.6f\n", ellipse2.calcCircumference());

        // System.out.println(root(-3, 3));
        // System.out.println(ln(0.025));
        // System.out.println(log(2,7.5));

        // System.out.println(sin(pi((double)6/7)));
        // System.out.println(cos(pi(1)));
        // System.out.println(tan(pi(1)));
        // System.out.println(csc(pi(1)));
        // System.out.println(sec(pi(1)));
        // System.out.println(cot(pi(1)));
    }

    public static double pi(double i) {
        return Math.PI * i;
    }

    public static double sum(double start, double stop, Function<Double, Double> f) {
        double error = 0.0000001;
        double dx = error/Math.abs(stop-start);

        double total = 0;
        for (int i = 0; i < (int) ((stop-start)/dx); i++) {
            total += f.apply(i*dx)*dx;
        }
        return total;
    }


    public static double getEstimate(double last, Function<Double, Double> slope, Function<Double, Double> f) {
        double m = slope.apply(last);
        double b = f.apply(last) - m*last;
        double estimate = -b/m;

        if (Math.abs(estimate - last) < 0.0001) {
            return estimate;
        }
        return getEstimate(estimate, slope, f);
    }

    public static double root(double x, int n) {
        if (n % 2 == 0 && x < 0 || n < 1) {
            return Double.NaN;
        }

        if (n == 1) {
            return x;
        }

        double overestimate = 0;
        while (true) {
            int currentPerfectSquare = (int) Math.pow(overestimate, n);
            if (currentPerfectSquare == Math.abs(x)) {
                return overestimate;
            } if (currentPerfectSquare > Math.abs(x)) {
                break;
            }
            overestimate++;
        }

        Function<Double, Double> slope = (i) -> {return n*Math.pow(i, n-1);};
        Function<Double, Double> f = (i) -> {return (Math.pow(i, n) - x);};
        return getEstimate(overestimate, slope, f);

    }

    public static double ln(double x) {
        if (x < 0) {
            return Double.NaN;
        }
        if (x == 1) {
            return 0;
        }

        if (x < 1) {
            double total = 0;
            for (int i = 0; i < 100; i++) {
                total += (Math.pow(-1, i)*Math.pow(x-1, i+1))/(i+1);
            }
            return total;
        }

        return sum(1, x, i -> 1/(1 + i));

    }

    public static double log(int b, double x) {
        assert b > 1;

        if (Math.floor(x) == x) {
            int i = 0;
            while (i < x) {
                if (Math.pow(b,i) == x) {
                    return i;
                }
                i++;
            }
        }

        return ln(x)/ln(b);
    }

    public static int factorial(int n) {
        if (n < 0) {
            return -1;
        }

        if (n < 2) {
            return 1;
        }

        int total = 1;
        for (int i = n; i > 1; i--) {
            total *= i;
        }
        return total;
    }

    public static double constrainAngle(double x) {

        double constrainedAngle = x;
        while (constrainedAngle < 0 || constrainedAngle >= pi(2)) {
            constrainedAngle += (x < 0 ? 1 : -1) * pi(2);
        }

        return constrainedAngle;
    }

    public static double getReferenceAngle(double x) {
        if (x >= pi(0.5) && x < pi(1)) {
            return pi(1)-x;
        }
        if(x >= pi(1) && x < pi(1.5)) {
            return x - pi(1);
        }
        if (x >= pi(1.5) && x < (pi(2))) {
            return pi(2) - x;
        }
        return x;
    }

    public static double sin(double x) {

        if (Math.abs(x - pi((double)1/6)) < 0.001) {
            return 0.5;
        }

        if (x % pi(1) == 0) {
            return 0;
        }
        if (x % pi(0.5) == 0) {
            return 1;
        }

        if (x >= pi(2) || x < 0) {
            return sin(constrainAngle(x));
        }

        if (x > pi(0.5)) {
            return sin(getReferenceAngle(x))  * (x > pi(1) ? -1 : 1);
        }

        double total = 0;
        for (int i = 0; i < 6; i++) {
            total += (Math.pow(-1, i)*Math.pow(x, 2*i+1)/factorial(2*i+1));
        }
        return total;
    }

    public static double cos(double x) {
        return sin(pi(0.5)-getReferenceAngle(constrainAngle(x))) * (x < pi(1.5) && x > pi(0.5) ? -1 : 1);
    }
    public static double tan(double x) {
        return sin(x)/cos(x);
    }
    public static double csc(double x) {
        return cos(x)/sin(x);
    }
    public static double sec(double x) {
        return 1/cos(x);
    }
    public static double cot(double x) {
        return cos(x)/sin(x);
    }

}

class Ellipse {
    private double a;
    private double b;

    public Ellipse(double a, double b) {
        this.a = a;
        this.b = b;
    }

    public double slopeAt(double x) {
        return (((b*-1*x)/(a*a)) * (1/Math.sqrt(1-((x*x)/(a*a)))));
    }

    public double calcArcLength(double start, double stop) {
        return Calc.sum(start, stop, i ->  Math.sqrt(1+Math.pow(slopeAt(start + i), 2)));
    }

    public double calcCircumference() {
        return 4*calcArcLength(0, a);
    }
}