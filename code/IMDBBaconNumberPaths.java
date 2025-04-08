import bridges.connect.Bridges;
import bridges.connect.DataSource;
import bridges.base.GraphAdjListSimple;
import bridges.base.Element;
import bridges.data_src_dependent.ActorMovieIMDB;

import java.util.*;

public class IMDBBaconNumberPaths {

    public static void main(String[] args) throws Exception {
        Bridges bridges = new Bridges(20, "RamleyHirn", "575356762377");
        bridges.setTitle("IMDB Actor/Movie Graph with Bacon Number Paths");
        bridges.setDescription("Visualizes the shortest paths from selected actors using BFS.");

        DataSource ds = bridges.getDataSource();
        List<ActorMovieIMDB> imdbData = new ArrayList<>();
        imdbData.addAll(ds.getActorMovieIMDBData(2016));
        imdbData.addAll(ds.getActorMovieIMDBData(2017));
        imdbData.addAll(ds.getActorMovieIMDBData(2018));
        imdbData.addAll(ds.getActorMovieIMDBData(2019));

        GraphAdjListSimple<String> graph = new GraphAdjListSimple<>();
        HashMap<String, List<String>> neighborsMap = new HashMap<>();
        Set<String> actorNames = new HashSet<>();

        for (ActorMovieIMDB record : imdbData) {
            String actor = record.getActor();
            String movie = record.getMovie();

            actorNames.add(actor);

            graph.addVertex(actor, actor);
            graph.addVertex(movie, movie);
            graph.addEdge(actor, movie);
            graph.addEdge(movie, actor);

            neighborsMap.computeIfAbsent(actor, k -> new ArrayList<>()).add(movie);
            neighborsMap.computeIfAbsent(movie, k -> new ArrayList<>()).add(actor);
        }

        runBaconPath("Kevin_Bacon_(I)", 8, graph, neighborsMap, actorNames, "green");
        runBaconPath("James_Stewart_(I)", 6, graph, neighborsMap, actorNames, "orange");
        runBaconPath("Alan_Rickman", 4, graph, neighborsMap, actorNames, "purple");

        bridges.setDataStructure(graph);
        bridges.visualize();
    }

    public static void runBaconPath(String startActor, int targetDistance,
                                    GraphAdjListSimple<String> graph,
                                    HashMap<String, List<String>> neighborsMap,
                                    Set<String> actorNames,
                                    String pathColor) {

        if (!graph.getVertices().containsKey(startActor)) {
            System.out.println(startActor + " not found.");
            return;
        }

        Queue<String> queue = new LinkedList<>();
        HashMap<String, Integer> distance = new HashMap<>();
        HashMap<String, String> parent = new HashMap<>();

        queue.add(startActor);
        distance.put(startActor, 0);
        parent.put(startActor, null);
        graph.getVertex(startActor).setColor("red");
        graph.getVertex(startActor).setSize(20);
        graph.getVertex(startActor).setLabel(startActor + " (0)");

        String targetActor = null;

        while (!queue.isEmpty()) {
            String current = queue.poll();
            int currDist = distance.get(current);

            for (String neighbor : neighborsMap.get(current)) {
                if (!distance.containsKey(neighbor)) {
                    distance.put(neighbor, currDist + 1);
                    parent.put(neighbor, current);
                    graph.getVertex(neighbor).setLabel(neighbor + " (" + (currDist + 1) + ")");
                    queue.add(neighbor);

                    if (distance.get(neighbor) == targetDistance &&
                        actorNames.contains(neighbor) &&
                        targetActor == null) {

                        targetActor = neighbor;
                    }
                }
            }
        }

        System.out.println("Actors found at distance " + targetDistance + " from " + startActor + ":");
        for (String name : distance.keySet()) {
            if (distance.get(name) == targetDistance && actorNames.contains(name)) {
                System.out.println(" - " + name);
            }
        }


        if (targetActor != null) {
            System.out.println("Found actor at distance " + targetDistance + " from " + startActor + ": " + targetActor);
            highlightPath(graph, parent, targetActor, pathColor);
        } else {
            System.out.println("No actor found at distance " + targetDistance + " from " + startActor);
        }
    }

    public static void highlightPath(GraphAdjListSimple<String> graph,
                                     HashMap<String, String> parent,
                                     String endNode, String color) {
        String current = endNode;
        while (current != null) {
            Element<String> v = graph.getVertex(current);
            v.setColor(color);
            v.setSize(15);

            String prev = parent.get(current);
            if (prev != null) {
                var link = graph.getLinkVisualizer(prev, current);
                if (link != null) {
                    link.setColor(color);
                    link.setThickness(4.0f);
                }
            }
            current = parent.get(current);
        }
    }
}
