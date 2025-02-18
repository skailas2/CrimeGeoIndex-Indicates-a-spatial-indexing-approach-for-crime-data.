import java.io.*;
import java.util.*;
/**
 * A 2D Tree implementation for storing and querying crime records based on spatial coordinates.
 */
public class TwoDTree {
    /**
     * Represents a node in the 2D tree with X, Y coordinates and crime data.
     */
    static class TreeNode {
        double x, y;
        String crimeData;
        TreeNode left, right;

        public TreeNode(double x, double y, String crimeData) {
            this.x = x;
            this.y = y;
            this.crimeData = crimeData;
            this.left = null;
            this.right = null;
        }
    }

    private TreeNode root;
    private int size;

    /**
     * Constructs a 2D tree and loads crime data from a file.
     *
     * @param crimeDataLocation The file path of the crime data CSV.
     */
    public TwoDTree(String crimeDataLocation) {
        root = null;
        size = 0;
        loadCrimeData(crimeDataLocation);
    }

    /**
     * Loads crime data from the CSV file and inserts records into the tree.
     *
     * @param crimeDataLocation The file path of the crime data CSV.
     */
    private void loadCrimeData(String crimeDataLocation) {
        try (BufferedReader br = new BufferedReader(new FileReader(crimeDataLocation))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                insert(x, y, line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Inserts a new crime record into the 2D tree.
     *
     * @param x         X-coordinate.
     * @param y         Y-coordinate.
     * @param crimeData Crime data associated with the coordinates.
     */
    public void insert(double x, double y, String crimeData) {
        root = insertRecursive(root, x, y, crimeData, 0);
        size++;
    }

    /**
     * Recursively inserts a new node into the tree based on the depth level.
     *
     * @param node      The current node in recursion.
     * @param x         X-coordinate.
     * @param y         Y-coordinate.
     * @param crimeData Crime data associated with the coordinates.
     * @param depth     Depth of the tree (used for alternating between x and y comparison).
     * @return The modified node.
     */
    private TreeNode insertRecursive(TreeNode node, double x, double y, String crimeData, int depth) {
        if (node == null) {
            return new TreeNode(x, y, crimeData);
        }
        boolean compareX = (depth % 2 == 0);
        if (compareX) {
            if (x < node.x)
                node.left = insertRecursive(node.left, x, y, crimeData, depth + 1);
            else
                node.right = insertRecursive(node.right, x, y, crimeData, depth + 1);
        } else {
            if (y < node.y)
                node.left = insertRecursive(node.left, x, y, crimeData, depth + 1);
            else
                node.right = insertRecursive(node.right, x, y, crimeData, depth + 1);
        }
        return node;
    }

    /**
     * Prints the tree in pre-order traversal.
     */
    public void preOrderPrint() {
        preOrderPrintRecursive(root);
    }
//Time Complexity is O(N)
    public void inOrderPrint() {
        inOrderPrintRecursive(root);
    }
//Time Complexity is O(n)
    private void inOrderPrintRecursive(TreeNode node) {
        if (node == null) return;
        inOrderPrintRecursive(node.left);
        System.out.println(node.crimeData);
        inOrderPrintRecursive(node.right);
    }
    public void postOrderPrint() {
        postOrderPrintRecursive(root);
    }
    private void postOrderPrintRecursive(TreeNode node) {
        if (node == null) return;
        postOrderPrintRecursive(node.left);
        postOrderPrintRecursive(node.right);
        System.out.println(node.crimeData);
    }


    private void preOrderPrintRecursive(TreeNode node) {
        if (node == null) return;
        System.out.println(node.crimeData);
        preOrderPrintRecursive(node.left);
        preOrderPrintRecursive(node.right);
    }

    private int nodesExamined;

    public int getNodesExamined() {
        return nodesExamined;
    }

    /**
     * Finds the nearest neighbor to the given (x1, y1) coordinates.
     *
     * @param x1 X-coordinate of query point.
     * @param y1 Y-coordinate of query point.
     * @return The nearest neighbor as a Neighbor object.
     */
    public Neighbor nearestNeighbor(double x1, double y1) {
        nodesExamined = 0;
        return nearestNeighbor(root, x1, y1, 0, new Neighbor(null, Double.MAX_VALUE));
    }

    private Neighbor nearestNeighbor(TreeNode node, double x1, double y1, int depth, Neighbor best) {
        if (node == null) return best;

        nodesExamined++; // Track number of examined nodes

        double distance = Math.sqrt(Math.pow(node.x - x1, 2) + Math.pow(node.y - y1, 2));
        if (distance < best.distance) {
            best = new Neighbor(node, distance);
        }

        boolean compareX = (depth % 2 == 0);
        TreeNode first, second;
        if (compareX) {
            first = (x1 < node.x) ? node.left : node.right;
            second = (x1 < node.x) ? node.right : node.left;
        } else {
            first = (y1 < node.y) ? node.left : node.right;
            second = (y1 < node.y) ? node.right : node.left;
        }

        best = nearestNeighbor(first, x1, y1, depth + 1, best);
        if ((compareX && Math.abs(x1 - node.x) < best.distance) || (!compareX && Math.abs(y1 - node.y) < best.distance)) {
            best = nearestNeighbor(second, x1, y1, depth + 1, best);
        }

        return best;
    }

    /**
     * Finds all points within a given rectangular range.
     *
     * @param x1 Lower x-bound.
     * @param y1 Lower y-bound.
     * @param x2 Upper x-bound.
     * @param y2 Upper y-bound.
     * @return A list of crimes within the specified range.
     */
    public ListOfCrimes findPointsInRange(double x1, double y1, double x2, double y2) {
        ListOfCrimes crimesInRange = new ListOfCrimes();
        rangeSearch(root, x1, y1, x2, y2, 0, crimesInRange);
        return crimesInRange;
    }


    private void rangeSearch(TreeNode node, double x1, double y1, double x2, double y2, int depth, ListOfCrimes result) {
        if (node == null) return;

        // If the node is within range, add it to the result list
        if (node.x >= x1 && node.x <= x2 && node.y >= y1 && node.y <= y2) {
            result.addCrime(node.crimeData);
        }

        boolean compareX = (depth % 2 == 0);

        // Recursively search both left and right subtrees if necessary
        if (compareX) {
            if (x1 <= node.x) rangeSearch(node.left, x1, y1, x2, y2, depth + 1, result);
            if (x2 >= node.x) rangeSearch(node.right, x1, y1, x2, y2, depth + 1, result);
        } else {
            if (y1 <= node.y) rangeSearch(node.left, x1, y1, x2, y2, depth + 1, result);
            if (y2 >= node.y) rangeSearch(node.right, x1, y1, x2, y2, depth + 1, result);
        }
    }


    /**
     * Prints the tree in level-order traversal.
     * Time Complexity is o(n)
     */
    public void levelOrderPrint() {
        if (root == null) return;
        Queue<TreeNode> queue = new Queue<>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.dequeue();
            System.out.println(node.crimeData);
            if (node.left != null) queue.enqueue(node.left);
            if (node.right != null) queue.enqueue(node.right);
        }
    }

    /**
     * Prints the tree in reverse level-order traversal.
     * Time Complexity is O(n)
     */
    public void reverseLevelOrderPrint() {
        if (root == null) return;
        Queue<TreeNode> queue = new Queue<>();
        Stack<TreeNode> stack = new Stack<>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.dequeue();
            stack.push(node);
            if (node.right != null) queue.enqueue(node.right);
            if (node.left != null) queue.enqueue(node.left);
        }
        while (!stack.isEmpty()) {
            System.out.println(stack.pop().crimeData);
        }
    }
}