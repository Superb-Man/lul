import java.util.*;

class GraphTheoryAssignment {

    static class Edge {
        int to;
        int w;
        Edge(int t, int w) {
            this.to = t;
            this.w = w;
        }
    }

    static class Graph {
        final int n;
        boolean directed;
        List<List<Edge>> g;
        Graph(int n, boolean directed) {
            this.n = n;
            this.directed = directed;
            g = new ArrayList<>(n + 1);
            for (int i = 0; i <= n; i++) {
                g.add(new ArrayList<>());
            }
        }
        void addEdge(int u, int v, int w) {
            g.get(u).add(new Edge(v, w));
            if (!directed) {
                g.get(v).add(new Edge(u, w));
            }
        }
    }

    static class DijkstraResult {
        long[] dist;
        int[] par;
        DijkstraResult(long[] d, int[] p) {
            dist = d;
            par = p;
        }
    }

    static DijkstraResult dijkstra(Graph G, int src) {
        int n = G.n;
        long INF = Long.MAX_VALUE / 4;
        long[] dist = new long[n + 1];
        int[] par = new int[n + 1];
        Arrays.fill(dist, INF);
        Arrays.fill(par, -1);
        dist[src] = 0;
        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a[0]));
        pq.add(new long[]{0, src});
        boolean[] done = new boolean[n + 1];
        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            long d = cur[0];
            int u = (int) cur[1];
            if (done[u]) continue;
            done[u] = true;
            if (d != dist[u]) continue;
            for (Edge e : G.g.get(u)) {
                if (dist[u] != INF && dist[u] + e.w < dist[e.to]) {
                    dist[e.to] = dist[u] + e.w;
                    par[e.to] = u;
                    pq.add(new long[]{dist[e.to], e.to});
                }
            }
        }
        return new DijkstraResult(dist, par);
    }

    static int bestMeetingVertex(Graph G, int a, int b) {
        DijkstraResult da = dijkstra(G, a);
        DijkstraResult db = dijkstra(G, b);
        long best = Long.MAX_VALUE;
        int bestV = -1;
        for (int v = 1; v <= G.n; v++) {
            long s = da.dist[v] + db.dist[v];
            if (s < best) {
                best = s;
                bestV = v;
            }
        }
        return bestV;
    }

    static int bestKStop(Graph G, int start, int end, Set<Integer> K) {
        DijkstraResult dStart = dijkstra(G, start);
        DijkstraResult dEnd = dijkstra(G, end);
        long best = Long.MAX_VALUE;
        int bestX = -1;
        for (int x : K) {
            long s = dStart.dist[x] + dEnd.dist[x];
            if (s < best) {
                best = s;
                bestX = x;
            }
        }
        return bestX;
    }

    static DijkstraResult modifiedLabelCorrectingPQ(Graph G, int src) {
        int n = G.n;
        long INF = Long.MAX_VALUE / 4;
        long[] dist = new long[n + 1];
        int[] par = new int[n + 1];
        Arrays.fill(dist, INF);
        Arrays.fill(par, -1);
        dist[src] = 0;
        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a[0]));
        pq.add(new long[]{0, src});
        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            long d = cur[0];
            int u = (int) cur[1];
            if (d != dist[u]) continue;
            for (Edge e : G.g.get(u)) {
                long nd = dist[u] + e.w;
                if (nd < dist[e.to]) {
                    dist[e.to] = nd;
                    par[e.to] = u;
                    pq.add(new long[]{nd, e.to});
                }
            }
        }
        return new DijkstraResult(dist, par);
    }

    static Graph buildProblem3Graph() {
        Graph G = new Graph(7, true);
        G.addEdge(1, 2, 8);
        G.addEdge(2, 3, -8);
        G.addEdge(1, 3, 1);
        G.addEdge(3, 4, 4);
        G.addEdge(4, 5, -4);
        G.addEdge(3, 5, 1);
        G.addEdge(5, 6, 2);
        G.addEdge(6, 7, -2);
        G.addEdge(5, 7, 1);
        return G;
    }

    public static void main(String[] args) {
        Graph city = new Graph(6, false);
        city.addEdge(1, 2, 4);
        city.addEdge(2, 3, 2);
        city.addEdge(3, 4, 5);
        city.addEdge(4, 5, 1);
        city.addEdge(5, 6, 3);
        city.addEdge(1, 6, 10);
        System.out.println("Problem1 meet at: " + bestMeetingVertex(city, 1, 6));
        Set<Integer> K = new HashSet<>(Arrays.asList(2, 3, 5));
        System.out.println("Problem2 KFC stop: " + bestKStop(city, 1, 6, K));

        Graph neg = buildProblem3Graph();
        DijkstraResult wrong = dijkstra(neg, 1);
        System.out.println("Problem3 Normal Dijkstra dist: " + Arrays.toString(wrong.dist));
        DijkstraResult corr = modifiedLabelCorrectingPQ(neg, 1);
        System.out.println("Problem3 Modified algorithm dist: " + Arrays.toString(corr.dist));
    }
}
