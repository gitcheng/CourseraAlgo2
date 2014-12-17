/* Programming assignment of Algorithm II on Coursera
Burrows-Wheeler Data Compression
http://coursera.cs.princeton.edu/algs4/assignments/burrows.html
*/
public class CircularSuffixArray {

    private final int R = 256;
    private final int CUTOFF = 10;
    private Integer[] index; // index of the 1st char of sorted suffixes
    private String s0;
    private int N;

    public CircularSuffixArray(String s)  // circular suffix array of s
    {
	N = s.length();
	s0 = s;
	index = new Integer[N];

	for (int i = 0; i < N; i++)
	    index[i] = i;

	Integer[] aux = new Integer[N];
	sort(index, aux, 0, N-1, 0);
    }


    // MSD string sort using key-indexed counting. Integer array represents
    // the starting index of suffixes.
    private void sort(Integer[] a, Integer[] aux, int lo, int hi, int d)
    {
	if (hi <= lo) return;
	if (hi - lo < CUTOFF) {
	    insertionsort(a, lo, hi, d);
	    return;
	}

	int[] count = new int[R+2];
	for (int i = lo; i <= hi; i++)
	    count[s0.charAt((a[i] + d) % N) + 2]++;
	for (int r = 0; r < R+1; r++)
	    count[r+1] += count[r];
	for (int i = lo; i <= hi; i++)
	    aux[count[s0.charAt((a[i] + d) % N) + 1]++] = a[i];
	for (int i = lo; i <= hi; i++)
	    a[i] = aux[i - lo];
	// sort R subarrays recursively
	for (int r = 0; r < R; r++)
	    sort(a, aux, lo + count[r], lo + count[r+1] - 1, d+1);
    }

    private void insertionsort(Integer[] a, int lo, int hi, int d)
    {
	for (int i = lo; i <= hi; i++)
	    for (int j = i; j > lo && less(a[j], a[j-1], d); j--)
		exch(a, j, j-1);
    }

    private void exch(Integer[] a, int i, int j)
    {
	int x = a[i];
	a[i] = a[j];
	a[j] = x;
    }

    private boolean less(int i, int j, int d)
    {
	return csuffix((i+d)%N).compareTo(csuffix((j+d)%N)) < 0;
    }

    private String csuffix(int i)
    {
	return s0.substring(i, N) + s0.substring(0, i);
    }


    // public void printSuffixes()
    // {
    // 	for (int i = 0; i < N; i++)
    // 	    StdOut.println(csuffix(i));
    // }

    // public void printSortedSuffixes()
    // {
    // 	for (int i = 0; i < N; i++) {
    // 	    StdOut.print(csuffix(index[i]));
    // 	    StdOut.println("  " + index[i]);
    // 	}
    // }


    public int length()                   // length of s
    {
	return N;
    }

    public int index(int i)               // returns index of ith sorted suffix
    {
	return index[i];
    }

    // unit testing of the methods (optional)
    public static void main(String[] args)
    {
	In in = new In(args[0]);
	String s = in.readString();

	CircularSuffixArray csa = new CircularSuffixArray(s);
	StdOut.println(csa.length());
	// for (int i = 0; i < csa.length(); i++) 
	//     StdOut.print(csa.index(i) + " ");
	// StdOut.println();

	// csa.printSuffixes();
	// StdOut.println();
	// csa.printSortedSuffixes();

    }
}
