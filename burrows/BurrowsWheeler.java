/* Programming assignment of Algorithm II on Coursera
Burrows-Wheeler Data Compression
http://coursera.cs.princeton.edu/algs4/assignments/burrows.html
*/

public class BurrowsWheeler {




    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode()
    {
	// read the input
	String s = BinaryStdIn.readString();
	// Circular suffix array
	CircularSuffixArray csa = new CircularSuffixArray(s);
	int first = zeroindex(csa);

	// write the index csa where the original string is.
	BinaryStdOut.write(first);
	for (int i = 0; i < csa.length(); i++) {
	    if (i == first)
		BinaryStdOut.write(s.charAt(csa.length() - 1));
	    else
		BinaryStdOut.write(s.charAt(csa.index(i) - 1));
	}
	BinaryStdOut.close();
    }

    // find the index where csa[i] == 0
    private static int zeroindex(CircularSuffixArray csa)
    {
	for (int i = 0; i < csa.length(); i++) {
	    if (csa.index(i) == 0)
		return i;
	}
	throw new IllegalArgumentException("Broken CSA");
    }


    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode()
    {
	int first = BinaryStdIn.readInt();
	char[] t = new char[1];
	int N = 0;
	while (!BinaryStdIn.isEmpty()) {
	    char c = BinaryStdIn.readChar();
	    if (N == t.length) t = resize(t);
	    t[N++] = c;
	}
	int[] next = new int[N];
	for (int i = 0; i < N; i++) next[i] = i;
	// sort t array, exchange next array elements accordingly
	for (int i = 0; i < N; i++) {
	    for (int j = i; j > 0 && t[j] < t[j-1]; j--) {
		char c = t[j];  t[j] = t[j-1];  t[j-1] = c;
		int n = next[j]; next[j] = next[j-1]; next[j-1] = n;
	    }
	}
	int k = first;
	for (int i = 0; i < N; i++) {
	    BinaryStdOut.write(t[k], 8);
	    k = next[k];
	}
	BinaryStdOut.close();
    }

    private static char[] resize(char[] t)
    {
	char[] temp = new char[t.length * 2];
	for (int i = 0; i < t.length; i++) temp[i] = t[i];
	return temp;
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args)
    {
	if (args[0].equals("-")) encode();
	else if (args[0].equals("+")) decode();
	else throw new IllegalArgumentException("Illegal command line argument");
    }

}
