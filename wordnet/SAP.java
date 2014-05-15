// Programming assignment for Coursera.org Algorithm II week 1: WordNet.
// http://coursera.cs.princeton.edu/algs4/assignments/wordnet.html
import java.util.TreeSet;

public class SAP {

    private final Digraph DG;
    private int lastv, lastw;
    private int lastAncestor, lastLength;
    private TreeSet<Integer> lastvs;
    private TreeSet<Integer> lastws;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        DG = new Digraph(G);
        lastv = -1;
        lastw = -1;
        lastAncestor = -1;
        lastLength = -1;
        lastvs = new TreeSet<Integer>();
        lastws = new TreeSet<Integer>();
    }

    private void updateAncestor(int v, int w) {
        if ((v == lastv && w == lastw) || (v == lastw && w == lastv))
            return;

        BreadthFirstDirectedPaths bv = new BreadthFirstDirectedPaths(DG, v);
        BreadthFirstDirectedPaths bw = new BreadthFirstDirectedPaths(DG, w);
        int path = Integer.MAX_VALUE;
        int a = -1;
        for (int x = 0; x < DG.V(); x++) {
            if (bv.hasPathTo(x) && bw.hasPathTo(x)) {
                if (bv.distTo(x) + bw.distTo(x) < path) {
                    path = bv.distTo(x) + bw.distTo(x);
                    a = x;
                }
            }
        }
        lastv = v;
        lastw = w;
        lastAncestor = a;
        if (a == -1)
            path = -1;
        lastLength = path;
    }

    private void updateAncestor(Iterable<Integer> v, Iterable<Integer> w) {
        TreeSet<Integer> tmpv = new TreeSet<Integer>();
        TreeSet<Integer> tmpw = new TreeSet<Integer>();
        for (int x : v)
            tmpv.add(x);
        for (int x : w)
            tmpw.add(x);
        if (lastvs.equals(tmpv) && lastws.equals(tmpw)) 
            return;
        BreadthFirstDirectedPaths bv = new BreadthFirstDirectedPaths(DG, v);
        BreadthFirstDirectedPaths bw = new BreadthFirstDirectedPaths(DG, w);
        int path = Integer.MAX_VALUE;
        int a = -1;
        for (int x = 0; x < DG.V(); x++) {
            if (bv.hasPathTo(x) && bw.hasPathTo(x)) {
                if (bv.distTo(x) + bw.distTo(x) < path) {
                    path = bv.distTo(x) + bw.distTo(x);
                    a = x;
                }
            }
        }
        lastvs.clear();
        lastvs.addAll(tmpv);
        lastws.clear();
        lastws.addAll(tmpw);
        lastAncestor = a;
        if (a == -1)
            path = -1;
        lastLength = path;
    }


    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        updateAncestor(v, w);
        return lastLength;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        updateAncestor(v, w);
        return lastAncestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        updateAncestor(v, w);
        return lastLength;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        updateAncestor(v, w);
        return lastAncestor;
    }

    // for unit testing of this class (such as the one below)
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
