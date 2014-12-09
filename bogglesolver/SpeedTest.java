public class SpeedTest
{

    public static void main(String[] args)
    {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
	BoggleSolver solver = new BoggleSolver(dictionary);
	int nboards = Integer.parseInt(args[1]);

	StdOut.println("Test on randomly generated " + nboards + " boards");

	Stopwatch timer = new Stopwatch();
	for (int i = 0; i < nboards; i++) {
	    BoggleBoard board = new BoggleBoard();
	    solver.getAllValidWords(board);
	}

	double et = timer.elapsedTime();
	StdOut.println("Elapsed time = " + et + " s");
	double bps = nboards/et;
	StdOut.println("boards per second = " + bps);

    }

}
