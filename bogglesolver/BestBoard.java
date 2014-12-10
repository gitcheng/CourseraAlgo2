public class BestBoard
{

    public static void main(String[] args)
    {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
	BoggleSolver solver = new BoggleSolver(dictionary);
	int nboards = Integer.parseInt(args[1]);

	int max = 0;
	int threshold = 1000;
	BoggleBoard best = null;

	StdOut.println("Test on randomly generated " + nboards + " boards");

	Stopwatch timer = new Stopwatch();
	for (int i = 0; i < nboards; i++) {
	    BoggleBoard board = new BoggleBoard();
	    int score = 0;
	    for (String word : solver.getAllValidWords(board)) {
		int sc = solver.scoreOf(word);
                score += sc;
	    }

	    if (score > max) {
		max = score;
		best = board;

		if (score > threshold) {
		    StdOut.println("Best board score so far is " + max);
		    StdOut.println(best.toString());
		    StdOut.println("");
		}
	    }
	}

	StdOut.println("Best board score is " + max);
	StdOut.println(best.toString());
	StdOut.println("");

	double et = timer.elapsedTime();
	StdOut.println("Elapsed time = " + et + " s");
	double bps = nboards/et;
	StdOut.println("boards per second = " + bps);

    }

}
