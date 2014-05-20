// Programming assignment for Coursera.org Algorithm II week 1: WordNet.
// http://coursera.cs.princeton.edu/algs4/assignments/wordnet.html
import java.util.Hashtable;
import java.util.Vector;

public class WordNet {

    // Mapping a noun to its id.
    private Hashtable<String, Bag<Integer>> synsetids;
    // Store nouns 
    private Vector<String> synsetnouns;
    // Hypernym
    private Digraph DG;
    // Shortest ancestral path
    private SAP S;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        synsetids = new Hashtable<String, Bag<Integer>>();
        synsetnouns = new Vector<String>();

        // read in synsets
        In in = new In(synsets);
        String s = in.readLine();
        int id = 0;
        while (s != null) {
            String[] fields = s.split("\\,");
            assert id == Integer.parseInt(fields[0]);
            String[] words = fields[1].split("\\s+");
            for (int i = 0; i < words.length; i++) {
                String w = words[i];
                if (synsetids.get(w) == null) {
                    synsetids.put(w, new Bag<Integer>());
                }
                synsetids.get(w).add(id);
            }
            synsetnouns.add(fields[1]);

            s = in.readLine();
            id++;
        }

        // Build a digraph
        // read the entire hypernyms file in first
        Vector<String> hns = new Vector<String>();
        int maxd = 0;
        in = new In(hypernyms);
        s = in.readLine();
        while (s != null) {
            String[] fields = s.split("\\,");
            for (int i = 0; i < fields.length; i++) {
                int d = Integer.parseInt(fields[i]);
                if (d > maxd) 
                    maxd = d;
            }
            hns.add(s);
            s = in.readLine();
        }
        //StdOut.println(hns.size());
        DG = new Digraph(maxd+1);
        for (int i = 0; i < hns.size(); i++) {
            s = hns.get(i);
            String[] fields = s.split("\\,");
            int v = Integer.parseInt(fields[0]);
            for (int j = 1; j < fields.length; j++) {
                DG.addEdge(v, Integer.parseInt(fields[j]));
            }
        }
        // check if it is a DAG (no cycle)
        if (new DirectedCycle(DG).hasCycle())
            throw new IllegalArgumentException("Not a DAG");
        // check if it has only one root
        int roots = 0;
        for (int i = 0; i < DG.V(); i++) {
            if (!DG.adj(i).iterator().hasNext())
                roots++;
        }
        if (roots != 1) {
            throw new IllegalArgumentException("Has " + roots + " roots");
        }

        // SAP
        S = new SAP(DG);
    }

    // the set of nouns (no duplicates), returned as an Iterable
    public Iterable<String> nouns() {
        return synsetids.keySet();
    }
    
    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return synsetids.get(word) != null;
    }
    
    // distance between nounA and nounB
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA))
            throw new IllegalArgumentException("cannot find "+nounA);
        if (!isNoun(nounB))
            throw new IllegalArgumentException("cannot find "+nounB);
        return S.length(synsetids.get(nounA), synsetids.get(nounB));
    }
    
    // a synset (second field of synsets.txt) that is the common ancestor of 
    // nounA and nounB in a shortest ancestral path
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA))
            throw new IllegalArgumentException("cannot find "+nounA);
        if (!isNoun(nounB))
            throw new IllegalArgumentException("cannot find "+nounB);
        int sid = S.ancestor(synsetids.get(nounA), synsetids.get(nounB));
        return synsetnouns.get(sid);
    }
    
    // for unit testing of this class
    public static void main(String[] args) {
        WordNet W = new WordNet(args[0], args[1]);
        while (!StdIn.isEmpty()) {
            String v = StdIn.readString();
            String w = StdIn.readString();
            if (!W.isNoun(v))
                StdOut.println(v + " is not a noun.");
            if (!W.isNoun(w))
                StdOut.println(w + " is not a noun.");
            StdOut.println("distance = " + W.distance(v, w));
            StdOut.println("sap = " + W.sap(v, w));
        }
    }    
}
