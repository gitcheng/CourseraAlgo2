// Progamming assignment for Coursera Algorithm II
// http://coursera.cs.princeton.edu/algs4/assignments/boggle.html
// Boggle board solver
// 
//------------------------------------------------------------------
import java.util.TreeSet;

public class BoggleSolver
{

    private TrieSET26 dictset;  // dictionary data type

    private boolean[] marked;
    private int M, N;
    private TreeSet<String> validWords;
    private StringBuilder pathstring;

    // Initializes the data structure using the given array of strings as the 
    // dictionary. (You can assume each word in the dictionary contains only 
    // the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        // Create a dictionary tree
        dictset = new TrieSET26();
        for (String word : dictionary) {
            dictset.add(word);
        }
    }



    // Depth first search the board
    private void dfsPaths(BoggleBoard board, int v, Stack<String> words) {
        marked[v] = true;
        int vj = v % N;
        int vi = (v-vj)/N;

        // append the letter to a string
        char vc = board.getLetter(vi, vj);
        pathstring.append(vc);
        if (vc == 'Q') pathstring.append('U');

        String word = pathstring.toString();
        if (!dictset.isPrefix(word)) return;

        if (word.length() >= 3) {
            if (dictset.contains(word))
                words.push(word);
        }

        // Expand the path: going to the neighbors
        for (int i = Math.max(0, vi-1); i < Math.min(vi+2, M); i++) {
            for (int j = Math.max(0, vj-1); j < Math.min(vj+2, N); j++) {
                int w = i*N + j;
                if (!marked[w]) {
                    dfsPaths(board, w, words);
                } else {
                    continue;
                }
                // back track, set the marker to false so the next path can find
                // it.
                marked[w] = false;
                pathstring.deleteCharAt(pathstring.length()-1);
                // If after deletion, the new last letter is Q, delete that.
                if (pathstring.charAt(pathstring.length()-1) == 'Q')
                    pathstring.deleteCharAt(pathstring.length()-1);
            }
        }
    }


    // Return all valid words on the board starting from position s
    private Iterable<String> getAllValidWords(BoggleBoard board, int s) {
        // Create an array to mark board
        M = board.rows();
        N = board.cols();
        marked = new boolean[M*N];
        pathstring = new StringBuilder("");
        // All valid words starting at position s.
        Stack<String> words = new Stack<String>();
        dfsPaths(board, s, words);
        return words;
    }
        
    // Returns the set of all valid words in the given Boggle board, as an 
    // Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {

        validWords = new TreeSet<String>();
        for (int v = 0; v < board.rows()*board.cols(); v++) {
            for (String word : getAllValidWords(board, v)) {
                if (word.length() < 3) continue;
                if (dictset.contains(word)) {
                    validWords.add(word);
                }
            }
        }
        return validWords;
    }
        
    // Returns the score of the given word if it is in the dictionary, zero 
    // otherwise. (You can assume the word contains only the uppercase letters
    //  A through Z.)
    public int scoreOf(String word) {
	if (!dictset.contains(word)) return 0;
        if (word.length() < 3) return 0;
        if (word.length() < 5) return 1;
        if (word.length() < 6) return 2;
        if (word.length() < 7) return 3;
        if (word.length() < 8) return 5;
        return 11;
    }


    // Unit test: takes the filename of a dictionary and the filename of a 
    // Boggle board as command-line arguments and prints out all valid words 
    // for the given board using the given dictionary.
    public static void main(String[] args)
    {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();

        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);

        StdOut.println(board.toString()+"\n");

        int score = 0;
        for (String word : solver.getAllValidWords(board))
            {
                int sc = solver.scoreOf(word);
                StdOut.println(sc + "  " + word);
                score += sc;
            }
        StdOut.println("Score = " + score);

    }
}


