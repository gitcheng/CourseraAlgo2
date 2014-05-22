// Coursera Algorithm II programming assignment.
// http://coursera.cs.princeton.edu/algs4/assignments/baseball.html
import java.util.Hashtable;
import java.util.Arrays;

public class BaseballElimination {
    private final int N;  // number of teams
    private final int GV, V;  // game vertices and total vertices in FlowNetwork
    private final String[] T;  // teams
    private Hashtable<String, Integer> teamid;
    private int[] W, L, R;
    private int[][] A;
    private boolean[][] cert;  // for each team, flag others for its certificate

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        N = in.readInt();
        // Total number of vertices is N(N-1)/2+2:
        // source vertex -> (N-1)(N-2)/2 game vertices -> N-1 team vertices -> target
        GV = (N-1)*(N-2)/2;   // number of game vertices
        V = 1 + GV + (N-1) + 1;
        T = new String[N];
        this.teamid = new Hashtable<String, Integer>();
        W = new int[N];
        L = new int[N];
        R = new int[N];
        A = new int[N][N];
        cert = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            String st = in.readString();
            T[i] = st;
            this.teamid.put(st, i);
            W[i] = in.readInt();
            L[i] = in.readInt();
            R[i] = in.readInt();
            for (int j = 0; j < N; j++) {
                A[i][j] = in.readInt();
                cert[i][j] = false;
            }
        }
    }

    // check team is valid
    private void checkteam(String team) {
        if (this.teamid.get(team) == null)
            throw new IllegalArgumentException("invalid team " + team);
    }

    // number of teams
    public              int numberOfTeams() {
        return N;
    }

    // all teams
    public Iterable<String> teams() {
        return Arrays.asList(T);
    }

    // number of wins for given team
    public              int wins(String team) {
        checkteam(team);
        return W[teamid.get(team)];
    }

    // number of losses for given team
    public              int losses(String team) {
        checkteam(team);
        return L[teamid.get(team)];
    }

    // number of remaining games for given team
    public              int remaining(String team) {
        checkteam(team);
        return R[teamid.get(team)];
    }

    // number of remaining games between team1 and team2
    public              int against(String team1, String team2) {
        checkteam(team1);
        checkteam(team2);
        return A[teamid.get(team1)][teamid.get(team2)];
    }

    // is it trivially eliminated?
    private boolean isTrivial(String team) {
        checkteam(team);
        int ti = teamid.get(team);
        for (int i = 0; i < N; i++) {
            if (W[ti] + R[ti] < W[i]) {
                cert[ti][i] = true;  // only record the first one
                return true;
            }
        }
        return false;
    }

    // translate the team id: The original id is from 0 to N-1. Given a team
    // in question, the id's of the teams after it need to move up by one
    // so that the new id's are from 0 to N-2. These id's are then used in
    // a FlowNetwork.
    private int[] oldids(int ti) {
        int[] id = new int[N-1];   // the original indices of the other teams
        for (int i = 0; i < id.length; i++) {
            if (i < ti)
                id[i] = i;
            else
                id[i] = i+1;
        }
        return id;
    }

    // team vertex
    private int teamvtx(int i) {
        return i + GV + 1;
    }

    // Build a FlowNetwork
    private FlowNetwork buildFW(String team) {
        checkteam(team);
        if (isTrivial(team)) {
            throw new IllegalArgumentException(team + " is trivially eliminated");
        }

        int ti = teamid.get(team);
        int[] id = oldids(ti);

        // Build a FlowNetwork
        FlowNetwork FW = new FlowNetwork(V);
        // add edges from source to game vertices
        int g = 0;
        for (int i = 0; i < N-2; i++) {
            for (int j = i+1; j < N-1; j++) {
                g++;
                FlowEdge edge = new FlowEdge(0, g, A[id[i]][id[j]]);
                FW.addEdge(edge);
                edge = new FlowEdge(g, i+GV+1, Double.POSITIVE_INFINITY);
                FW.addEdge(edge);
                edge = new FlowEdge(g, j+GV+1, Double.POSITIVE_INFINITY);
                FW.addEdge(edge);
            }
        }
        assert g == GV;
        // add edges from team vertices to target
        for (int i = 0; i < N-1; i++) {
            FlowEdge edge = new FlowEdge(teamvtx(i), V-1, W[ti]+R[ti]-W[id[i]]);
            FW.addEdge(edge);
        }
        return FW;
    }

    // is given team eliminated?
    public          boolean isEliminated(String team) { 
        if (isTrivial(team)) 
            return true;

        FlowNetwork FW = this.buildFW(team);
        FordFulkerson FF = new FordFulkerson(FW, 0, FW.V()-1);

        int ti = teamid.get(team);
        int[] id = oldids(ti);
        boolean isElim = false;
        for (int i = 0; i < N-1; i++) {
            if (FF.inCut(teamvtx(i))) {
                cert[ti][id[i]] = true;
                isElim = true;
            } else {
                cert[ti][id[i]] = false;
            }
        }
        return isElim;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        checkteam(team);
        if (isEliminated(team)) {
            Bag<String> CE = new Bag<String>();
            int ti = teamid.get(team);
            for (int i = 0; i < N; i++) {
                if (cert[ti][i]) {
                    CE.add(T[i]);
                }
            }
            return CE;
        }
        return null;
    }

    // print record table
    private void printRecord() {
        StdOut.println(N);
        for (String team : this.teams()) {
            StdOut.print(team + "   ");
            StdOut.print(this.wins(team) + " ");
            StdOut.print(this.losses(team) + " ");
            StdOut.print(this.remaining(team) + "   ");
            for (int j = 0; j < N; j++) {
                StdOut.print(this.against(team, this.T[j]) + " ");
            }
            StdOut.println();
        }
        StdOut.println();
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team))
                    StdOut.print(t + " ");
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
