// Least Significant Digit string sort
// 
public class LSD {

    public static void sort(String[] a, int W) { // fixed-length W strings
	int R= 256;
	int N= a.length;
	String[] aux = new String[N];

	for (int d = W-1; d >= 0; d--) {
	    int[] count = new int[R+1];
	    for (int i = 0; i < N; i++)
		count[a[i].charAt(d) + 1]++;
	    for (int r = 0; r < R; r++)
		count[r+1] += count[r];
	    for (int i = 0; i < N; i++)
		aux[count[a[i].charAt(d)]++] = a[i];
	    for (int i = 0; i < N; i++)
		a[i] = aux[i];

	    print_string(a);
	}
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

	LSD lsd= new LSD();

	print_string(a);
	StdOut.println();

	lsd.sort(a, 4);
	print_string(a);

    }


}



