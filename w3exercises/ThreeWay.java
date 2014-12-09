// 3-way string quicksort
// 
public class ThreeWay {

    private static int charAt(String s, int d) {
	if (d < s.length()) return s.charAt(d);
	else return -1;
    }

    private static void exch(String[] a, int i, int j) {
	String s = a[i];
	a[i] = a[j];
	a[j] = s;
    }

    public static void sort(String[] a) {
	sort(a, 0, a.length - 1, 0);
    }

    private static void sort(String[] a, int lo, int hi, int d) {
	if (hi <= lo) return;
	int lt = lo, gt = hi;
	int v = charAt(a[lo], d);
	int i = lo + 1;
	while (i <= gt) {
	    int t = charAt(a[i], d);
	    if      (t < v) exch(a, lt++, i++);
	    else if (t > v) exch(a, i, gt--);
	    else            i++;
	    
	    StdOut.print(lt);
	    StdOut.print(" ");
	    StdOut.print(i);
	    StdOut.print(" ");
	    StdOut.print(gt);
	    StdOut.print(" ");
	    print_string(a);
	}

	StdOut.println();
	print_string(a);
	StdOut.println();

	sort(a, lo, lt-1, d);
	if (v >= 0) sort(a, lt, gt, d+1);
	sort(a, gt+1, hi, d);
    }

    private static void print_string(String[] a) {
	for (int i = 0; i < a.length; i++) {
	    StdOut.print(a[i]+ " ");
	}
	StdOut.println();
    }

    // for unit testing of this class
    public static void main(String[] args) {
	In in = new In(args[0]);
	int N = in.readInt();
	String[] a = new String[N];
	for (int i = 0; i < N; i++) {
	    a[i] = in.readString();
	}
	print_string(a);
	StdOut.println();

	ThreeWay w3 = new ThreeWay();
	

	w3.sort(a);
	print_string(a);

    }

}
