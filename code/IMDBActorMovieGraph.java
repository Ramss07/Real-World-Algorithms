import bridges.connect.Bridges;
import bridges.connect.DataSource;
import bridges.base.GraphAdjListSimple;
import bridges.data_src_dependent.ActorMovieIMDB;
import java.util.List;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;

public class IMDBActorMovieGraph 
{
    public static void main(String[] args) throws Exception 
    {
        Bridges bridges = new Bridges(20, "RamleyHirn", "575356762377");
        bridges.setTitle("IMDB Actor/Movie Graph with BFS and Visual Attributes");
        bridges.setDescription("Graph representing actors and movies from the IMDB dataset.");
        DataSource ds = bridges.getDataSource();
        List<ActorMovieIMDB> imdbData = ds.getActorMovieIMDBData(1813);
        GraphAdjListSimple<String> imdbGraph = new GraphAdjListSimple<>();
        HashMap<String, String> vertices = new HashMap<>();
        HashMap<String, List<String>> neighborsMap = new HashMap<>();
        for (ActorMovieIMDB record : imdbData) 
        {
            String actor = record.getActor();
            String movie = record.getMovie();
            if (!vertices.containsKey(actor)) 
            {
                imdbGraph.addVertex(actor, actor);
                vertices.put(actor, actor);
                neighborsMap.put(actor, new ArrayList<>());
            }
            if (!vertices.containsKey(movie)) 
            {
                imdbGraph.addVertex(movie, movie);
                vertices.put(movie, movie);
                neighborsMap.put(movie, new ArrayList<>());
            }
            imdbGraph.addEdge(actor, movie);
            imdbGraph.addEdge(movie, actor);
            neighborsMap.get(actor).add(movie);
            neighborsMap.get(movie).add(actor);
        }
        HashMap<String, Integer> levels = new HashMap<>();
        String startVertex = "Kevin_Bacon_(I)";
        if (vertices.containsKey(startVertex)) 
        {
            bfs(startVertex, imdbGraph, neighborsMap, levels);
        } else 
        {
            //System.out.println(startVertex + " not found in the graph.");
        }
        for (String v : vertices.keySet()) 
        {
            if (!levels.containsKey(v)) 
            {
                bfs(v, imdbGraph, neighborsMap, levels);
            }
        }
        bridges.setDataStructure(imdbGraph);
        bridges.visualize();
    }

    public static void bfs(String start, GraphAdjListSimple<String> graph,
                           HashMap<String, List<String>> neighborsMap,
                           HashMap<String, Integer> levels) 
                           {
        String[] colors = {"red", "green", "blue", "cyan", "magenta", "yellow", "mistyrose", "orange", "purple"};
        Queue<String> queue = new LinkedList<>();
        levels.put(start, 0);
        var startVertex = graph.getVertex(start);
        startVertex.setColor(colors[0]);
        startVertex.setLabel(startVertex.getLabel() + " (0)");
        queue.add(start);
        while (!queue.isEmpty()) 
        {
            String current = queue.poll();
            int currentLevel = levels.get(current);
            //System.out.println(current + " at level " + currentLevel);
            List<String> neighbors = neighborsMap.get(current);
            for (String neighborKey : neighbors) {
                if (!levels.containsKey(neighborKey)) 
                {
                    int neighborLevel = currentLevel + 1;
                    levels.put(neighborKey, neighborLevel);
                    var neighborVertex = graph.getVertex(neighborKey);
                    if (neighborLevel < colors.length) 
                    {
                        neighborVertex.setColor(colors[neighborLevel]);
                    } else 
                    {
                        neighborVertex.setColor("beige");
                    }
                    neighborVertex.setLabel(neighborVertex.getLabel() + " (" + neighborLevel + ")");
                    String linkColor = (currentLevel < colors.length) ? colors[currentLevel] : "beige";
                    var linkViz = graph.getLinkVisualizer(current, neighborKey);
                    if (linkViz != null) 
                    {
                        linkViz.setColor(linkColor);
                        linkViz.setThickness(2.0f);
                    }
                    queue.add(neighborKey);
                }
            }
        }
    }
}
