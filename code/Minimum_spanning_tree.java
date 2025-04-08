import bridges.base.GraphAdjList;
import bridges.base.Edge;
import bridges.connect.Bridges;
import bridges.connect.DataSource;
import bridges.data_src_dependent.City;
import bridges.validation.RateLimitException;

import java.io.IOException;
import java.util.*;

public class Minimum_spanning_tree {

    public static void main(String[] args) throws IOException, RateLimitException {
       
        Bridges testBridges = new Bridges(23, "RamleyHirn", "575356762377");
        testBridges.setTitle("MST Test Graph (a–f)");
        GraphAdjList<String, String, Double> testGraph = createTestGraph();
        GraphAdjList<String, String, Double> testMST = primMST(testGraph, "a", new HashMap<>());

        double testCost = 0.0;
        for (String v : testMST.getVertices().keySet())
            for (Edge<String, Double> e : testMST.outgoingEdgeSetOf(v))
                testCost += e.getEdgeData();
        testCost /= 2;

        testBridges.setTitle(String.format("MST Test Graph (a–f): Total Cost: %.2f", testCost));
        testBridges.setDataStructure(testMST);
        testBridges.visualize();

        int[] populationThresholds = {25000, 50000, 100000};
        int assignmentNumber = 24;

        for (int threshold : populationThresholds) {
            Bridges bridges = new Bridges(assignmentNumber++, "RamleyHirn", "575356762377");

            bridges.setCoordSystemType("albersusa");
            bridges.setMapOverlay(true);

            DataSource ds = bridges.getDataSource();
            HashMap<String, String> params = new HashMap<>();
            params.put("min_pop", String.valueOf(threshold));

            Vector<City> allCities = ds.getUSCitiesData(params);
            Vector<City> cities = new Vector<>();
            String charlotteKey = "Charlotte_NC";

            for (City city : allCities) {
                String name = city.getCity().replaceAll(" ", "") + "_" + city.getState();
                if (name.equals(charlotteKey)) {
                    cities.add(city);
                    break;
                }
            }

            for (City city : allCities) {
                String name = city.getCity().replaceAll(" ", "") + "_" + city.getState();
                if (!name.equals(charlotteKey) && cities.size() < 1000) {
                    cities.add(city);
                }
            }

            GraphAdjList<String, String, Double> cityGraph = new GraphAdjList<>();
            Map<String, double[]> coordinates = new HashMap<>();

            for (City city : cities) {
                String name = city.getCity().replaceAll(" ", "") + "_" + city.getState();
                cityGraph.addVertex(name, name);
                double lon = city.getLongitude();
                double lat = city.getLatitude();
                cityGraph.getVisualizer(name).setLocation(lon, lat);
                cityGraph.getVisualizer(name).setSize(1);
                cityGraph.getVisualizer(name).setColor("black");
                coordinates.put(name, new double[]{lon, lat});
            }

            for (int i = 0; i < cities.size(); i++) {
                for (int j = i + 1; j < cities.size(); j++) {
                    City city1 = cities.get(i);
                    City city2 = cities.get(j);
                    double dist = getDist(city1.getLatitude(), city1.getLongitude(),
                                          city2.getLatitude(), city2.getLongitude());
                    String name1 = city1.getCity().replaceAll(" ", "") + "_" + city1.getState();
                    String name2 = city2.getCity().replaceAll(" ", "") + "_" + city2.getState();
                    cityGraph.addEdge(name1, name2, dist);
                    cityGraph.addEdge(name2, name1, dist);
                }
            }

            GraphAdjList<String, String, Double> mst = primMST(cityGraph, "Charlotte_NC", coordinates);

            double totalCost = 0.0;
            for (String v : mst.getVertices().keySet())
                for (Edge<String, Double> e : mst.outgoingEdgeSetOf(v))
                    totalCost += e.getEdgeData();
            totalCost /= 2;

            int vertexCount = mst.getVertices().size();
            int edgeCount = 0;
            for (String v : mst.getVertices().keySet()) {
                for (Edge<String, Double> edge : mst.outgoingEdgeSetOf(v)) {
                    edgeCount++;
                }
            }
            edgeCount /= 2;

            bridges.setTitle(String.format("MST (US Cities): Pop Threshold: %d, Vertices: %d, Edges: %d, Cost: %.6f",
                    threshold, vertexCount, edgeCount, totalCost));

            bridges.setDataStructure(mst);
            bridges.visualize();
        }
    }

    static GraphAdjList<String, String, Double> primMST(GraphAdjList<String, String, Double> graph, String start, Map<String, double[]> coordinates) {
        GraphAdjList<String, String, Double> mst = new GraphAdjList<>();
        PriorityQueue<Edge<String, Double>> minHeap = new PriorityQueue<>(Comparator.comparingDouble(Edge::getEdgeData));
        Set<String> visited = new HashSet<>();

        visited.add(start);
        mst.addVertex(start, start);
        if (coordinates.containsKey(start))
            mst.getVisualizer(start).setLocation(coordinates.get(start)[0], coordinates.get(start)[1]);

        for (Edge<String, Double> edge : graph.outgoingEdgeSetOf(start))
            minHeap.add(edge);

        while (!minHeap.isEmpty()) {
            Edge<String, Double> edge = minHeap.poll();
            String from = edge.getFrom();
            String to = edge.getTo();

            if (visited.contains(to))
                continue;

            visited.add(to);
            mst.addVertex(to, to);
            mst.addEdge(from, to, edge.getEdgeData());
            if (coordinates.containsKey(to))
                mst.getVisualizer(to).setLocation(coordinates.get(to)[0], coordinates.get(to)[1]);

            for (Edge<String, Double> nextEdge : graph.outgoingEdgeSetOf(to))
                if (!visited.contains(nextEdge.getTo()))
                    minHeap.add(nextEdge);
        }

        for (String vertex : mst.getVertices().keySet()) {
            mst.getVisualizer(vertex).setSize(1);
            mst.getVisualizer(vertex).setColor("black");
        }

        if (mst.getVertices().containsKey("Charlotte_NC")) {
            mst.getVisualizer("Charlotte_NC").setColor("blue");
            mst.getVisualizer("Charlotte_NC").setSize(6.0);
        }

        HashSet<String> styled = new HashSet<>();
        for (String from : mst.getVertices().keySet()) {
            for (Edge<String, Double> edge : mst.outgoingEdgeSetOf(from)) {
                String to = edge.getTo();
                String key = from.compareTo(to) < 0 ? from + to : to + from;

                if (!styled.contains(key)) {
                    mst.getLinkVisualizer(from, to).setColor("red");
                    mst.getLinkVisualizer(from, to).setThickness(1);
                    styled.add(key);
                }
            }
        }

        return mst;
    }

    static GraphAdjList<String, String, Double> createTestGraph() {
        GraphAdjList<String, String, Double> gr = new GraphAdjList<>();
        String[] vertices = {"a", "b", "c", "d", "e", "f"};
        for (String v : vertices) gr.addVertex(v, v);

        addEdge(gr, "a", "b", 3);
        addEdge(gr, "a", "e", 6);
        addEdge(gr, "a", "f", 5);
        addEdge(gr, "b", "c", 1);
        addEdge(gr, "b", "f", 4);
        addEdge(gr, "c", "d", 6);
        addEdge(gr, "c", "f", 4);
        addEdge(gr, "d", "e", 8);
        addEdge(gr, "d", "f", 4);
        addEdge(gr, "e", "f", 2);

        return gr;
    }

    static void addEdge(GraphAdjList<String, String, Double> gr, String u, String v, double weight) {
        gr.addEdge(u, v, weight);
        gr.addEdge(v, u, weight);
        gr.getLinkVisualizer(u, v).setLabel(String.valueOf(weight));
        gr.getLinkVisualizer(v, u).setLabel(String.valueOf(weight));
    }

    static double getDist(double lat1, double long1, double lat2, double long2) {
        final int R = 6371;
        final double phi1 = Math.toRadians(lat1);
        final double phi2 = Math.toRadians(lat2);
        final double delPhi = Math.toRadians(lat2 - lat1);
        final double delLambda = Math.toRadians(long2 - long1);

        final double a = Math.sin(delPhi / 2) * Math.sin(delPhi / 2) +
                Math.cos(phi1) * Math.cos(phi2) *
                        Math.sin(delLambda / 2) * Math.sin(delLambda / 2);

        final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
