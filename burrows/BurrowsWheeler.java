/* Programming assignment of Algorithm II on Coursera
Burrows-Wheeler Data Compression
http://coursera.cs.princeton.edu/algs4/assignments/burrows.html
*/

public class BurrowsWheeler {

    private static final int R = 256;


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
	int[] count = new int[R+1];  // key-index count 
	int N = 0;
	while (!BinaryStdIn.isEmpty()) {
	    char c = BinaryStdIn.readChar();
	    if (N == t.length) t = resize(t);
	    t[N++] = c;
	    count[(int) c + 1]++;
	}
	// cumulative counts (starting position of each character in sorted order)
	for (int r = 0; r < R; r++)
	    count[r+1] += count[r];

	int[] next = new int[N];
	char[] sortedT = new char[N];
	// Iterate over t. Find the position of t[i] in the sortedT, which is 
	// count[t[i]]++. Then assign i to next[count[t[i]++]
	for (int i = 0; i < N; i++) {
	    sortedT[count[t[i]]] = t[i];
	    next[count[t[i]]++] = i;
	}
	int k = first;
	for (int i = 0; i < N; i++) {
	    BinaryStdOut.write(sortedT[k], 8);
	    k = next[k];
	}
	BinaryStdOut.close();
    }

    private static char[] resize(char[] t)
    {
	char[] temp = new char[t.length * 2];
	for (int i = 0; i < t.length; i++)
	    temp[i] = t[i];
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
