// Programming assignment for Coursera.org Algorithm II week 1: WordNet.
// http://coursera.cs.princeton.edu/algs4/assignments/wordnet.html
public class Outcast {

    private WordNet W;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        W = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int deg = -1;
        int out = -1;
        for (int i = 0; i < nouns.length; i++) {
            int di = 0;
            for (int j = 0; j < nouns.length; j++) {
                di += W.distance(nouns[i], nouns[j]);
            }
            if (di > deg) {
                deg = di;
                out = i;
            }
        }
        return nouns[out];
    }

// for unit testing of this class (such as the one below)
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }

}
    
    
