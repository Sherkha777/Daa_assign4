import java.util.*;

/**
 * Missions 3 and 4. Find a word in a long text.
 */
public class Marker {

    /** Mission 3a. pi[i] = length of the longest proper prefix of p[0..i] that is also its suffix. */
    public static int[] prefixFunction(String p) {
        int m = p.length();
        int[] pi = new int[m];
        int k = 0;
        for (int i = 1; i < m; i++) {
            while (k > 0 && p.charAt(i) != p.charAt(k)) k = pi[k - 1];
            if (p.charAt(i) == p.charAt(k)) k++;
            pi[i] = k;
        }
        return pi;
    }

    /** Mission 3b. KMP: all start positions of p in text (overlaps allowed). The text pointer never goes back. */
    public static int[] findAll(String text, String p) {
        int m = p.length(), n = text.length();
        if (m == 0 || m > n) return new int[0];
        int[] pi = prefixFunction(p);
        int[] buf = new int[16];
        int cnt = 0, k = 0;
        for (int i = 0; i < n; i++) {
            while (k > 0 && text.charAt(i) != p.charAt(k)) k = pi[k - 1];
            if (text.charAt(i) == p.charAt(k)) k++;
            if (k == m) {
                if (cnt == buf.length) buf = Arrays.copyOf(buf, cnt * 2);
                buf[cnt++] = i - m + 1;
                k = pi[k - 1];      // the next match may overlap this one
            }
        }
        return Arrays.copyOf(buf, cnt);
    }

    private static final long MOD = 1_000_000_007L;
    private static final long BASE = 131;

    private static int code(char c) {
        switch (c) { case 'A': return 1; case 'C': return 2; case 'G': return 3; case 'T': return 4; default: return c; }
    }

    /**
     * Mission 4. Rabin-Karp with a rolling hash. Counts positions i of a whose window a[i..i+k-1] occurs in b.
     * Windows of b are stored by hash; when a hash is found, the letters are compared (hashes can collide).
     */
    public static int sharedWords(String a, String b, int k) {
        int n = a.length(), m = b.length();
        if (k <= 0 || n < k || m < k) return 0;
        long high = 1;                                   // BASE^(k-1) mod MOD
        for (int i = 1; i < k; i++) high = high * BASE % MOD;

        // hashes of all windows of b -> start positions with that hash
        Map<Long, List<Integer>> table = new HashMap<>();
        long h = 0;
        for (int i = 0; i < k; i++) h = (h * BASE + code(b.charAt(i))) % MOD;
        table.computeIfAbsent(h, x -> new ArrayList<>()).add(0);
        for (int i = 1; i + k <= m; i++) {
            h = (h - code(b.charAt(i - 1)) * high % MOD + MOD) % MOD;
            h = (h * BASE + code(b.charAt(i + k - 1))) % MOD;
            table.computeIfAbsent(h, x -> new ArrayList<>()).add(i);
        }

        int count = 0;
        h = 0;
        for (int i = 0; i < k; i++) h = (h * BASE + code(a.charAt(i))) % MOD;
        for (int i = 0; ; i++) {
            List<Integer> cand = table.get(h);
            if (cand != null) {
                for (int j : cand) {
                    if (sameLetters(a, i, b, j, k)) { count++; break; }
                }
            }
            if (i + k >= n) break;
            h = (h - code(a.charAt(i)) * high % MOD + MOD) % MOD;
            h = (h * BASE + code(a.charAt(i + k))) % MOD;
        }
        return count;
    }

    private static boolean sameLetters(String a, int i, String b, int j, int k) {
        for (int t = 0; t < k; t++) if (a.charAt(i + t) != b.charAt(j + t)) return false;
        return true;
    }
}
