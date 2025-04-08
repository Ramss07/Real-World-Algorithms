import bridges.connect.Bridges;
import bridges.base.BSTElement;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;

public class BinarySearchTree {

    public static BSTElement<Integer, String> insert(BSTElement<Integer, String> node, int key) {
        if (node == null)
            return new BSTElement<Integer, String>(key, String.valueOf(key));
        if (key < node.getKey())
            node.setLeft(insert(node.getLeft(), key));
        else
            node.setRight(insert(node.getRight(), key));
        return node;
    }

    public static void rangeSearch(BSTElement<Integer, String> node, int low, int high) {
        if (node == null) return;
        int key = node.getKey();
        if (key > low)
            rangeSearch(node.getLeft(), low, high);
        if (key >= low && key <= high) {
            node.setColor("red");
            node.setSize(30);
            node.setShape("circle");
        }
        if (key < high)
            rangeSearch(node.getRight(), low, high);
    }

    public static void main(String[] args) throws Exception {
        Bridges bridges = new Bridges(23, "RamleyHirn", "575356762377");
        bridges.setTitle("Binary Search Tree Range Search");
        bridges.setDescription("Highlighting nodes in the range 80-120");

        Random rand = new Random();
        Set<Integer> keys = new HashSet<>();
        BSTElement<Integer, String> root = null;

        while (keys.size() < 100) {
            int key = rand.nextInt(201);
            if (keys.add(key)) {
                root = insert(root, key);
            }
        }

        rangeSearch(root, 80, 120);

        bridges.setDataStructure(root);
        bridges.visualize();
    }
}
