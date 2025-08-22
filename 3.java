import java.util.*;

class FifteenPuzzle2D {

    static final int N = 4;
    static final int[][] GOAL = new int[N][N];
    static final int[] dx = {-1, 1, 0, 0};
    static final int[] dy = {0, 0, -1, 1};

    static {
        int v = 1;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i == N - 1 && j == N - 1) GOAL[i][j] = 0;
                else GOAL[i][j] = v++;
            }
        }
    }

    static int[] blankPos(int[][] state) {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (state[i][j] == 0) return new int[]{i, j};
        return null;
    }

    static int[][] swap(int[][] state, int r1, int c1, int r2, int c2) {
        int[][] newState = copy(state);
        int t = newState[r1][c1];
        newState[r1][c1] = newState[r2][c2];
        newState[r2][c2] = t;
        return newState;
    }

    static int[][] copy(int[][] state) {
        int[][] res = new int[N][N];
        for (int i = 0; i < N; i++)
            res[i] = state[i].clone();
        return res;
    }

    static boolean isGoal(int[][] state) {
        return Arrays.deepEquals(state, GOAL);
    }

    static String key(int[][] state) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : state) sb.append(Arrays.toString(row));
        return sb.toString();
    }

    static boolean dfs(int[][] state, int r, int c, int depth, Set<String> visited) {
        if (isGoal(state)) return true;
        if (depth == 0) return false;

        String k = key(state);
        visited.add(k);

        for (int d = 0; d < 4; d++) {
            int nr = r + dx[d], nc = c + dy[d];
            if (nr < 0 || nr >= N || nc < 0 || nc >= N) continue;

            int[][] newState = swap(state, r, c, nr, nc);
            String nk = key(newState);

            if (!visited.contains(nk)) {
                if (dfs(newState, nr, nc, depth - 1, visited)) return true;
            }
        }

        visited.remove(k);
        return false;
    }

    static int dpSolve(int[][] state, int r, int c, int limit, Map<String, Integer> memo) {
        if (isGoal(state)) return 0;
        if (limit < 0) return -1;

        String k = key(state) + "|" + r + "|" + c + "|" + limit;
        if (memo.containsKey(k)) return memo.get(k);

        int best = Integer.MAX_VALUE;
        for (int d = 0; d < 4; d++) {
            int nr = r + dx[d], nc = c + dy[d];
            if (nr < 0 || nr >= N || nc < 0 || nc >= N) continue;

            int[][] newState = swap(state, r, c, nr, nc);
            int sub = dpSolve(newState, nr, nc, limit - 1, memo);
            if (sub != -1) best = Math.min(best, 1 + sub);
        }

        int ans = (best == Integer.MAX_VALUE) ? -1 : best;
        memo.put(k, ans);
        return ans;
    }

    public static void main(String[] args) {
        int[][] start = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 15, 14, 0}
        };

        int[] blank = blankPos(start);

        System.out.println("DFS (depth 20): " + dfs(start, blank[0], blank[1], 20, new HashSet<>()));
        System.out.println("DP (depth 20): " + dpSolve(start, blank[0], blank[1], 20, new HashMap<>()));
    }
}
