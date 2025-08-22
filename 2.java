import java.util.*;

class FifteenPuzzleAssignment {

    static final int N = 4;
    static final int[] GOAL = makeGoal();

    static int[] makeGoal() {
        int[] g = new int[16];
        for (int i = 0; i < 15; i++) {
            g[i] = i + 1;
        }
        g[15] = 0;
        return g;
    }

    static int blankPos(int[] s) {
        for (int i = 0; i < 16; i++) {
            if (s[i] == 0) return i;
        }
        return -1;
    }

    static List<Integer> moves(int pos) {
        int x = pos / 4;
        int y = pos % 4;
        List<Integer> m = new ArrayList<>(4);
        if (x > 0) m.add(pos - 4);
        if (x < 3) m.add(pos + 4);
        if (y > 0) m.add(pos - 1);
        if (y < 3) m.add(pos + 1);
        return m;
    }

    static int[] swap(int[] s, int i, int j) {
        int[] t = s.clone();
        int tmp = t[i];
        t[i] = t[j];
        t[j] = tmp;
        return t;
    }

    static boolean isGoal(int[] s) {
        return Arrays.equals(s, GOAL);
    }

    static boolean dfs(int[] s, int blank, int depth, Set<String> vis) {
        if (isGoal(s)) return true;
        if (depth == 0) return false;
        String k = Arrays.toString(s);
        vis.add(k);
        for (int np : moves(blank)) {
            int[] ns = swap(s, blank, np);
            String nk = Arrays.toString(ns);
            if (!vis.contains(nk)) {
                if (dfs(ns, np, depth - 1, vis)) return true;
            }
        }
        vis.remove(k);
        return false;
    }

    static int dpSolve(int[] s, int blank, int limit, Map<String, Integer> memo) {
        if (isGoal(s)) return 0;
        if (limit < 0) return -1;
        String key = Arrays.toString(s) + "|" + blank + "|" + limit;
        if (memo.containsKey(key)) return memo.get(key);
        int best = Integer.MAX_VALUE;
        for (int np : moves(blank)) {
            int[] ns = swap(s, blank, np);
            int sub = dpSolve(ns, np, limit - 1, memo);
            if (sub != -1) best = Math.min(best, 1 + sub);
        }
        int ans = (best == Integer.MAX_VALUE) ? -1 : best;
        memo.put(key, ans);
        return ans;
    }

    public static void main(String[] args) {
        int[] start = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 15, 14, 0};
        System.out.println("Backtracking (depth20): " + dfs(start, blankPos(start), 20, new HashSet<>()));
        System.out.println("DP (depth20): " + dpSolve(start, blankPos(start), 20, new HashMap<>()));
    }
}
