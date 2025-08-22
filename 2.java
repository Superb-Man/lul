import java.util.*;

class FifteenPuzzle {

    static final int N = 4;
    static final int[] GOAL = new int[16];

    static {
        for (int i = 0; i < 15; i++) GOAL[i] = i + 1;
        GOAL[15] = 0;
    }

    static int blankPos(int[] state) {
        for (int i = 0; i < 16; i++) if (state[i] == 0) return i;
        return -1;
    }

    static List<Integer> moves(int pos) {
        List<Integer> m = new ArrayList<>();
        int r = pos / N, c = pos % N;
        if (r > 0) m.add(pos - N);
        if (r < N - 1) m.add(pos + N);
        if (c > 0) m.add(pos - 1);
        if (c < N - 1) m.add(pos + 1);
        return m;
    }

    static int[] swap(int[] state, int i, int j) {
        int[] newState = state.clone();
        int t = newState[i];
        newState[i] = newState[j];
        newState[j] = t;
        return newState;
    }

    static boolean isGoal(int[] state) {
        return Arrays.equals(state, GOAL);
    }

    // Simple DFS/backtracking
    static boolean dfs(int[] state, int blank, int depth, Set<String> visited) {
        if (isGoal(state)) return true;
        if (depth == 0) return false;
        String key = Arrays.toString(state);
        visited.add(key);
        for (int next : moves(blank)) {
            int[] newState = swap(state, blank, next);
            String nKey = Arrays.toString(newState);
            if (!visited.contains(nKey)) {
                if (dfs(newState, next, depth - 1, visited)) return true;
            }
        }
        visited.remove(key);
        return false;
    }

    // DP with memoization
    static int dpSolve(int[] state, int blank, int limit, Map<String, Integer> memo) {
        if (isGoal(state)) return 0;
        if (limit < 0) return -1;
        String key = Arrays.toString(state) + "|" + blank + "|" + limit;
        if (memo.containsKey(key)) return memo.get(key);

        int best = Integer.MAX_VALUE;
        for (int next : moves(blank)) {
            int[] newState = swap(state, blank, next);
            int sub = dpSolve(newState, next, limit - 1, memo);
            if (sub != -1) best = Math.min(best, 1 + sub);
        }
        int ans = (best == Integer.MAX_VALUE) ? -1 : best;
        memo.put(key, ans);
        return ans;
    }

    public static void main(String[] args) {
        int[] start = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 15, 14, 0};

        System.out.println("DFS (depth 20): " + dfs(start, blankPos(start), 20, new HashSet<>()));
        System.out.println("DP (depth 20): " + dpSolve(start, blankPos(start), 20, new HashMap<>()));
    }
}
