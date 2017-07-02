package Utility;

/**
 * Created by FraccaMan on 02/11/16.
 */
public final class Utility {

    private Utility() {}

    // Private Methods

    private static double _invSqrt(double x) {
        final double xhalf = 0.5d * x;
        long i = Double.doubleToLongBits(x);
        i = 0x5fe6eb50c7b537a9L - (i >> 1);
        x = Double.longBitsToDouble(i);
        x *= (1.5d - xhalf * x * x); // pass 1
        x *= (1.5d - xhalf * x * x); // pass 2
        x *= (1.5d - xhalf * x * x); // pass 3
        x *= (1.5d - xhalf * x * x); // pass 4
        return x;
    }

    public static double sqrt(final double d) {
        return d * _invSqrt(d); // ~10% faster than Math.sqrt.
    }

    public static double pow(double a, double b) {
        final long tmp = Double.doubleToLongBits(a);
        final long tmp2 = (long) (b * (tmp - 4606921280493453312L)) + 4606921280493453312L;
        return Double.longBitsToDouble(tmp2);
    }

}
