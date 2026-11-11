/**
 * Missions 1 and 2. Compare two strains with dynamic programming.
 */
public class Strains {

    /** Mission 1. Edit distance: d[i][j] = cheapest way to turn a[0..i) into b[0..j). */
    public static int editDistance(String a, String b) {
        int n = a.length(), m = b.length();
        int[][] d = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++) d[i][0] = i;   // delete i letters
        for (int j = 0; j <= m; j++) d[0][j] = j;   // insert j letters
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int replace = d[i - 1][j - 1] + (a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1);
                int delete = d[i - 1][j] + 1;
                int insert = d[i][j - 1] + 1;
                d[i][j] = Math.min(replace, Math.min(delete, insert));
            }
        }
        return d[n][m];
    }

    /** Mission 2. LCS: d[i][j] = length of the longest common subsequence of a[0..i) and b[0..j). */
    public static int lcsLength(String a, String b) {
        int n = a.length(), m = b.length();
        int[][] d = new int[n + 1][m + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) d[i][j] = d[i - 1][j - 1] + 1;
                else d[i][j] = Math.max(d[i - 1][j], d[i][j - 1]);
            }
        }
        return d[n][m];
    }
}
