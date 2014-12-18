/* Programming assignment of Algorithm II on Coursera
Burrows-Wheeler Data Compression
http://coursera.cs.princeton.edu/algs4/assignments/burrows.html
*/
public class CircularSuffixArray {

    private static final int R = 256;
    private static final int CUTOFF = 1;
    private int[] index; // index of the 1st char of sorted suffixes
    private String s0;
    private int N;

    public CircularSuffixArray(String s)  // circular suffix array of s
    {
	N = s.length();
	s0 = s+s;
	index = new int[N];

	for (int i = 0; i < N; i++)
	    index[i] = i;

	//int[] aux = new int[N];
	//MSDsort(index, aux, 0, N-1, 0);
	Quick3string(index, 0, N-1, 0);
    }


    // Quick3string three-way string quick sort
    private void Quick3string(int[] a, int lo, int hi, int d)
    {
	if (hi <= lo) return;
	if (d >= N) return;
	if (hi - lo < CUTOFF) {
	    insertionsort(a, lo, hi, d);
	    return;
	}

	int lt = lo, gt = hi;
	int v = s0.charAt(a[lo] + d);
	int i = lo + 1;

	//StdOut.printf("%d  %d  %d  %d\n", lt, gt, a[lo], d);

	while (i <= gt) {
	    int t = s0.charAt(a[i] + d);
	    if      (t < v) exch(a, lt++, i++);
	    else if (t > v) exch(a, i, gt--);
	    else i++;
	}
	Quick3string(a, lo, lt-1, d);
	if (v >= 0) Quick3string(a, lt, gt, d+1);
	Quick3string(a, gt+1, hi, d);
    }




    // MSD string sort using key-indexed counting. Integer array represents
    // the starting index of suffixes.
    private void MSDsort(int[] a, int[] aux, int lo, int hi, int d)
    {
	if (hi <= lo) return;
	if (hi - lo < CUTOFF) {
	    insertionsort(a, lo, hi, d);
	    return;
	}

	int[] count = new int[R+2];
	for (int i = lo; i <= hi; i++)
	    count[s0.charAt(a[i] + d) + 2]++;
	for (int r = 0; r < R+1; r++)
	    count[r+1] += count[r];
	for (int i = lo; i <= hi; i++)
	    aux[count[s0.charAt(a[i] + d) + 1]++] = a[i];
	for (int i = lo; i <= hi; i++)
	    a[i] = aux[i - lo];
	// sort R subarrays recursively
	for (int r = 0; r < R; r++)
	    MSDsort(a, aux, lo + count[r], lo + count[r+1] - 1, d+1);
    }

    private void insertionsort(int[] a, int lo, int hi, int d)
    {
	for (int i = lo; i <= hi; i++)
	    for (int j = i; j > lo && less(a[j], a[j-1], d, hi-lo+1); j--)
		exch(a, j, j-1);
    }

    private void exch(int[] a, int i, int j)
    {
	int x = a[i];
	a[i] = a[j];
	a[j] = x;
    }

    private boolean less(int i, int j, int d, int M)
    {
	return csuffix(i+d, M).compareTo(csuffix(j+d, M)) < 0;
    }

    private String csuffix(int i, int M)
    {
	return s0.substring(i, i+M);
    }

    public int length()                   // length of s
    {
	return N;
    }

    public int index(int i)               // returns index of ith sorted suffix
    {
	return index[i];
    }

    

    // public void printSuffixes()
    // {
    // 	for (int i = 0; i < N; i++)
    // 	    StdOut.println(csuffix(i));
    // }

    // public void printSortedSuffixes()
    // {
    //  	for (int i = 0; i < N; i++) 
    // 	    StdOut.printf("%s  %d\n", csuffix(index[i], N), index(i));
    // }

    // public static String randombinary(int n)
    // {
    // 	StringBuilder s = new StringBuilder();
    // 	for (int i = 0; i < n; i++) {
    // 	    s.append(StdRandom.uniform(0, 2));
    // 	}
    // 	return s.toString();
    // }


    // unit testing of the methods (optional)
    public static void main(String[] args)
    {
	In in = new In(args[0]);
	String s = in.readString();


	// for (int i = 0; i < s.length(); i++) {
	//     int v = s.charAt(i);
	//     StdOut.printf("%c  %d\n", s.charAt(i), v);

	// }

	//String s = randombinary(100);


	CircularSuffixArray csa = new CircularSuffixArray(s);
	StdOut.println(csa.length());
	// for (int i = 0; i < csa.length(); i++) 
	//     StdOut.print(csa.index(i) + " ");
	// StdOut.println();

	// csa.printSuffixes();
	// StdOut.println();
	//csa.printSortedSuffixes();


    }
}
