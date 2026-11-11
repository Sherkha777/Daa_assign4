import java.util.*;

/**
 * Mission 5. Dynamic programming on the tree of infections, rooted in person 0.
 */
public class Outbreak {

    /**
     * Minimum-cost vertex cover. in[v] = cheapest cost of v's subtree when v is tested,
     * out[v] = cheapest cost when v is not tested (then every child must be tested).
     */
    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        boolean[] tested = new boolean[n];
        if (n == 0) return tested;
        int[] parent = new int[n];
        int[] order = bfsOrder(n, nb, parent);
        long[] in = new long[n], out = new long[n];
        for (int idx = n - 1; idx >= 0; idx--) {           // children before parents
            int v = order[idx];
            in[v] = cost[v];
            out[v] = 0;
            for (int c : nb[v]) {
                if (c == parent[v]) continue;
                in[v] += Math.min(in[c], out[c]);
                out[v] += in[c];
            }
        }
        // rebuild: walk down from the root with the same rule
        boolean[] parentTested = new boolean[n];
        for (int idx = 0; idx < n; idx++) {
            int v = order[idx];
            if (idx == 0) tested[v] = in[v] <= out[v];
            else {
                int p = parent[v];
                if (!tested[p]) tested[v] = true;           // a free parent forces the child
                else tested[v] = in[v] <= out[v];
            }
        }
        return tested;
    }

    /** Longest path in links: for every person the two longest ways down. */
    public static int longestChain(int n, int[][] nb) {
        if (n == 0) return 0;
        int[] parent = new int[n];
        int[] order = bfsOrder(n, nb, parent);
        int[] best1 = new int[n], best2 = new int[n];       // two longest ways down (in links)
        int ans = 0;
        for (int idx = n - 1; idx >= 0; idx--) {
            int v = order[idx];
            ans = Math.max(ans, best1[v] + best2[v]);
            if (idx == 0) break;
            int p = parent[v], len = best1[v] + 1;
            if (len > best1[p]) { best2[p] = best1[p]; best1[p] = len; }
            else if (len > best2[p]) best2[p] = len;
        }
        return ans;
    }

    /** BFS order from person 0; fills parent[] (parent[0] = -1). */
    private static int[] bfsOrder(int n, int[][] nb, int[] parent) {
        int[] order = new int[n];
        boolean[] seen = new boolean[n];
        int head = 0, tail = 0;
        order[tail++] = 0; seen[0] = true; parent[0] = -1;
        while (head < tail) {
            int v = order[head++];
            for (int c : nb[v]) if (!seen[c]) { seen[c] = true; parent[c] = v; order[tail++] = c; }
        }
        return order;
    }
}
