package Utility;

/**
 * Created by FraccaMan on 02/11/16.
 */
public final class Utility {

    private Utility() {}

    // Private Methods

    private static double _invSqrt(double x) {
        final double xhalf = 0.5d*x;
        long i = Double.doubleToLongBits(x);
        i = 0x5fe6eb50c7b537a9L - (i >> 1);
        x = Double.longBitsToDouble(i);
        x *= (1.5d - xhalf*x*x); // pass 1
        x *= (1.5d - xhalf*x*x); // pass 2
        x *= (1.5d - xhalf*x*x); // pass 3
        x *= (1.5d - xhalf*x*x); // pass 4
        return x;
    }

    public static double sqrt(final double d) {
        return d*_invSqrt(d); // ~10% faster than Math.sqrt.
    }

    private static void merge(Comparable[] a, Comparable[] aux, int low, int mid, int high) {
        for (int i = low; i <= high; i++) {
            aux[i] = a[i]; // copy over elements from low to high
        }

        int i = low;
        int j = mid + 1;
        for (int k = low; k <= high; k++) {
            if (i > mid) a[k] = aux[j++];
            else if (j > high) a[k] = aux[i++];
            else if (less(aux[j], aux[i])) a[k] = aux[j++];
            else a[k] = aux[i++];
        }
    }

    private static boolean less(Comparable a, Comparable b) {
        return a.compareTo(b) < 0;
    }

    private static void insertionSort(Comparable[] a, int low, int high) {
        for (int i = low + 1; i <= high; i++) {
            for (int j = i - 1; j >= low; j--) {
                if (less(a[j + 1], a[j])) {
                    Comparable temp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = temp;
                } else break;
            }
        }
    }

    private static void sort(Comparable[] a, Comparable[] aux, int low, int high) {
        if (low >= high) return;
        if (high - low + 1 >= 7) {
            // use insertion sort instead
            insertionSort(a, low, high);
            return;
        }
        int mid = (low + high) / 2;
        // sort the left half
        sort(a, aux, low, mid);
        // sort the right half
        sort(a, aux, mid + 1, high);
        // merge the two sorted halves
        if (less(a[mid], a[mid + 1])) return; // already sorted, no need to merge
        merge(a, aux, low, mid, high);
    }

    // Public Methods

    public static void sort(Comparable[] a) {
        int len = a.length;
        Comparable[] aux = new Comparable[len];
        sort(a, aux, 0, len - 1);
    }

//    public static double pow(double x, int n) {
//        if (n == 0) return 1;
//        if (n == 2) return x * x;
//        if (n % 2 == 0) return pow(pow(x, n / 2), 2);
//        else return x * pow(pow(x, n / 2), 2);
//    }
    public static double pow(double a, double b) {
        final long tmp = Double.doubleToLongBits(a);
        final long tmp2 = (long) (b * (tmp - 4606921280493453312L)) + 4606921280493453312L;
        return Double.longBitsToDouble(tmp2);
    }

}
