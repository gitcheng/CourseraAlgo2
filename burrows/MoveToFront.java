/* Programming assignment of Algorithm II on Coursera
Burrows-Wheeler Data Compression
http://coursera.cs.princeton.edu/algs4/assignments/burrows.html
*/

public class MoveToFront {
    private static int R = 256;
    private static char[] achars;


    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode()
    {
	// initialize 256 characters in order
	achars = new char[R];
	for (int i = 0; i < R; i++)
	    achars[i] = (char) i;

	// read the input
	String s = BinaryStdIn.readString();
	char[] input = s.toCharArray();

	for (char c : input) 
	    BinaryStdOut.write(searchAndMove(achars, c));
	BinaryStdOut.close();

    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode()
    {
	// initialize 256 characters in order
	achars = new char[R];
	for (int i = 0; i < R; i++)
	    achars[i] = (char) i;
	// read the input
	while (!BinaryStdIn.isEmpty()) {
	    char c = BinaryStdIn.readChar();
	    BinaryStdOut.write(achars[(int) c], 8);
	    c = searchAndMove(achars, achars[c]);
	}
	BinaryStdOut.close();
    }

    // find the character in an array, return the index, and move to front
    private static char searchAndMove(char[] a, char c)
    {
	int i = 0;
	char ac = a[i++];
	while (ac != c && i < a.length) {
	    char ac2 = a[i];
	    a[i++] = ac;
	    ac = ac2;
	}
	if (i > a.length)
	    throw new IllegalArgumentException("Contains illegal characters");
	// move to front
	a[0] = c;
	return (char) (i-1);
    }



    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
	if (args[0].equals("-")) encode();
	else if (args[0].equals("+")) decode();
	else throw new IllegalArgumentException("Illegal command line argument");
    }
}
