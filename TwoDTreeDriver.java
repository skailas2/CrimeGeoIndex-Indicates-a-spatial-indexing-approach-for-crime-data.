import java.util.Scanner;
public class TwoDTreeDriver {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TwoDTree tree = new TwoDTree("src/CrimeLatLonXY.csv");

        System.out.println("Crime file loaded into 2D tree.");

        int choice;
        do {
            System.out.println("\nWhat would you like to do?");
            System.out.println("1: Inorder");
            System.out.println("2: Preorder");
            System.out.println("3: Level Order");
            System.out.println("4: Postorder");
            System.out.println("5: Reverse Level Order");
            System.out.println("6: Search for points within rectangle");
            System.out.println("7: Search for nearest neighbor");
            System.out.println("8: Quit");
            System.out.print("> ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> tree.inOrderPrint();
                case 2 -> tree.preOrderPrint();
                case 3 -> tree.levelOrderPrint();
                case 4 -> tree.postOrderPrint();
                case 5 -> tree.reverseLevelOrderPrint();
                case 6 -> {
                    // Range Search Input
                    System.out.println("Enter a rectangle bottom left (X1, Y1) and top right (X2, Y2) as four doubles each separated by a space:");
                    double x1 = scanner.nextDouble();
                    double y1 = scanner.nextDouble();
                    double x2 = scanner.nextDouble();
                    double y2 = scanner.nextDouble();

                    System.out.println("Searching for points within (" + x1 + "," + y1 + ") and (" + x2 + "," + y2 + ")");
                    ListOfCrimes crimes = tree.findPointsInRange(x1, y1, x2, y2);
                    crimes.printCrimes();

                    // Perform a spatial query
                    //ListOfCrimes crimesInArea = tree.findPointsInRange(10.0, 10.0, 50.0, 50.0);
                    // Export found crimes to a KML file
                    //crimes.toKML("src/CrimeData.kml");
                    System.out.println("Google Earth KML file generated.");

                    //System.out.println("The crime data has been written to PGHCrimes.kml. It is viewable in Google Earth Pro.");
                    crimes.toKML("PGHCrimes.kml");
                }
                case 7 -> {
                    // Nearest Neighbor Search Input
                    System.out.println("Enter a point to find the nearest crime (X Y):");
                    double x = scanner.nextDouble();
                    double y = scanner.nextDouble();

                    Neighbor nearest = tree.nearestNeighbor(x, y);
                    System.out.println("Looked at " + tree.getNodesExamined() + " nodes in tree. Found the nearest crime at:");
                    System.out.println(nearest.nearestNode.crimeData);
                }
                case 8 -> System.out.println("Thank you for exploring Pittsburgh crimes in the 1990’s.");
                default -> System.out.println("Invalid option. Please enter a number between 1 and 8.");
            }
        } while (choice != 8);

        scanner.close();
    }
}
