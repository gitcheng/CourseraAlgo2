// Most Significant Digit string sort
// 
public class MSD {

    private static int charAt(String s, int d) {
	if (d < s.length()) return s.charAt(d);
	else return -1;
    }

    public static void sort(String[] a) {
	String[] aux = new String[a.length];
	sort(a, aux, 0, a.length-1, 0);
    }

    public static void sort(String[] a, String[] aux, int lo, int hi, int d) { 
	int R= 256;
	if (hi <= lo) return;
	int[] count = new int[R+2];
	for (int i = lo; i <= hi; i++)
	    count[charAt(a[i], d) + 2]++;
	for (int r = 0; r < R+1; r++)
	    count[r+1] += count[r];
	for (int i = lo; i <= hi; i++)
	    aux[count[charAt(a[i], d)+1]++] = a[i];
	for (int i = lo; i <= hi; i++)
	    a[i] = aux[i - lo];
	
	print_string(a);

	for (int r = 0; r < R; r++)
	    sort(a, aux, lo + count[r], lo + count[r+1] - 1, d+1);

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

	MSD msd= new MSD();

	print_string(a);
	StdOut.println();

	msd.sort(a);
	print_string(a);

    }


}



