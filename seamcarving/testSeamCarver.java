import java.awt.Color;

public class testSeamCarver {

    private static Color randomColor() {
	int r = StdRandom.uniform(255);
	int g = StdRandom.uniform(255);
	int b = StdRandom.uniform(255);
	return new Color(r, g, b);
    }

    private static Picture randomPic(int W, int H) {
	Picture P = new Picture(W, H);
	for (int i = 0; i < W; i++) {
	    for (int j = 0; j < H; j++) {
		P.set(i, j, randomColor());
	    }
	}
	return P;
    }

    private static int[] randomSeam(int length, int range) {
	int[] a = new int[length];
	a[0] = StdRandom.uniform(range);
	for (int i = 1; i < a.length; i++) {
	    int x = -1;
	    while (x < 0 || x >= range)
		x =  a[i-1] + StdRandom.uniform(3) - 1;

	    a[i] = x;
	}
	return a;
    }

    private static void printSeam(int[] a) {
	for (int i = 0; i < a.length; i++)
	    StdOut.print(a[i] + " ");
	StdOut.println();
    }

    public static void main(String[] args) {
	
	if (args.length != 3) {
	    StdOut.println("Usage:\njava testSeamCarver [width] [height] [# of calls]");
	    return;
	}

	int W = Integer.parseInt(args[0]);
	int H = Integer.parseInt(args[1]);
	int N = Integer.parseInt(args[2]);

	Picture picture = randomPic(W, H);
	SeamCarver sc = new SeamCarver(picture);
	for (int i = 0; i < N; i++) {
	    int r = StdRandom.uniform(4);
	    StdOut.println(i + "  " + r);
	    if (r == 0) 
		sc.findVerticalSeam();
	    else if (r == 1)
		sc.findHorizontalSeam();
	    else if (r == 2) {  // vertical
		int[] a = randomSeam(sc.height(), sc.width());
		StdOut.println("W= " + sc.width() + " H= " + sc.height());
		printSeam(a);
		sc.removeVerticalSeam(a);
	    }
	    else {
		int[] a = randomSeam(sc.width(), sc.height());
		StdOut.println("W= " + sc.width() + " H= " + sc.height());
		printSeam(a);
		sc.removeHorizontalSeam(a);
	    }
	}


    }
}
