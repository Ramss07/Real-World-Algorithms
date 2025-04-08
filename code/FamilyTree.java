import bridges.connect.Bridges;
import bridges.base.GraphAdjListSimple;

public class FamilyTree {
    public static void main(String[] args) throws Exception 
	{
        Bridges bridges = new Bridges(18, "RamleyHirn", "575356762377");

        bridges.setTitle("Family Tree Visualization");
        bridges.setDescription("A graph representation of a family tree using BRIDGES.");

        GraphAdjListSimple<String> familyTree = new GraphAdjListSimple<>();

        String grandpa = "Quint K.", grandma = "Ruth K.";
        String father = "Randy H.", mother = "Laurie H.";
        String uncle = "Brahnam S.", aunt = "Penni S.";
        String me = "Ramley H.", sibling = "Jeffrey H.";
        String cousin1 = "Ryder S.", cousin2 = "Savannah S.";
        String greatGrandpa = "Bill K.", greatGrandma = "Cass K.";
        String greatGrandpa2 = "John M.", greatGrandma2 = "Eleanor M.";
        String greatGreatGrandpa = "Henry M.";

        String[] familyMembers = {grandpa, grandma, father, mother, uncle, aunt, me, sibling, cousin1, cousin2, greatGrandpa, greatGrandma, greatGrandpa2, greatGrandma2, greatGreatGrandpa};
        
        for (String member : familyMembers) 
		{
            familyTree.addVertex(member, member);
        }

        familyTree.addEdge(greatGrandpa, greatGrandma);
        familyTree.addEdge(greatGrandpa, grandpa);
        familyTree.addEdge(greatGrandma, grandpa);
        familyTree.addEdge(grandpa, grandma);
        familyTree.addEdge(grandpa, father);
        familyTree.addEdge(grandma, father);
        familyTree.addEdge(father, mother);
        familyTree.addEdge(father, me);
        familyTree.addEdge(father, sibling);
        familyTree.addEdge(mother, me);
        familyTree.addEdge(mother, sibling);
        familyTree.addEdge(grandpa, uncle);
        familyTree.addEdge(grandma, uncle);
        familyTree.addEdge(uncle, aunt);
        familyTree.addEdge(uncle, cousin1);
        familyTree.addEdge(aunt, cousin2);
        familyTree.addEdge(greatGrandpa2, greatGrandma2);
        familyTree.addEdge(greatGrandpa2, grandma);
        familyTree.addEdge(greatGrandma2, grandma);
        familyTree.addEdge(greatGreatGrandpa, greatGrandpa2);

        familyTree.getVertex(me).setColor("blue");
        familyTree.getVertex(father).setColor("green");
        familyTree.getVertex(mother).setColor("green");
        familyTree.getVertex(grandpa).setColor("orange");
        familyTree.getVertex(grandma).setColor("orange");
        familyTree.getVertex(uncle).setColor("purple");
        familyTree.getVertex(aunt).setColor("purple");
        familyTree.getVertex(cousin1).setColor("red");
        familyTree.getVertex(cousin2).setColor("red");
        familyTree.getVertex(greatGrandpa2).setColor("yellow");
        familyTree.getVertex(greatGrandma2).setColor("yellow");
        familyTree.getVertex(greatGreatGrandpa).setColor("brown");
        
        familyTree.getLinkVisualizer(father, me).setThickness(3.0f);
        familyTree.getLinkVisualizer(father, sibling).setThickness(3.0f);
        familyTree.getLinkVisualizer(mother, me).setThickness(3.0f);
        familyTree.getLinkVisualizer(mother, sibling).setThickness(3.0f);

        bridges.setDataStructure(familyTree);

        bridges.visualize();
    }
}
