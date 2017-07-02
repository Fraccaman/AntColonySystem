package Utility;

/**
 * A random number generator based on the simple and fast xor-shift pseudo
 * random number generator (RNG) specified in:
 * Marsaglia, George. (2003). Xorshift RNGs.
 * http://www.jstatsoft.org/v08/i14/xorshift.pdf
 * Translated from:
 * http://www.codeproject.com/Articles/9187/A-fast-equivalent-for-System-Random.
 */
@SuppressWarnings("SuspiciousNameCombination")
public class XSRandom extends java.util.Random {
    final double REAL_UNIT_INT = 1.0 / (0x7FFFFFFFL);
    final double REAL_UNIT_UINT = 1.0 / (0xFFFFFFFFL);
    final long Y = 842502087L, Z = 3579807591L, W = 273326509L;
    long x, y, z, w;

    public XSRandom(int s) {
        seed(s);
    }

    @Override
    public void setSeed(long seed) {
        seed((int) seed);
    }

    public void seed(int seed) {
        // The only stipulation stated for the xorshift RNG is that at least one of
        // the seeds x,y,z,w is non-zero. We fulfill that requirement by only allowing
        // resetting of the x seed
        x = seed;
        y = Y;
        z = Z;
        w = W;
    }

    @Override
    public double nextDouble() {
        long t = (x ^ (x << 11));
        x = y;
        y = z;
        z = w;

        // Here we can gain a 2x speed improvement by generating a value that can be cast to
        // an int instead of the more easily available uint. If we then explicitly cast to an
        // int the compiler will then cast the int to a double to perform the multiplication,
        // this final cast is a lot faster than casting from a uint to a double. The extra cast
        // to an int is very fast (the allocated bits remain the same) and so the overall effect
        // of the extra cast is a significant performance improvement.
        //
        // Also note that the loss of one bit of precision is equivalent to what occurs within
        // System.Random.
        return (REAL_UNIT_INT * (int) (0x7FFFFFFF & (w = (w ^ (w >> 19)) ^ (t ^ (t >> 8)))));
    }

    @Override
    public int nextInt() {
        long t = (x ^ (x << 11));
        x = y;
        y = z;
        z = w;
        return (int) (0x7FFFFFFF & (w = (w ^ (w >> 19)) ^ (t ^ (t >> 8))));
    }
}
