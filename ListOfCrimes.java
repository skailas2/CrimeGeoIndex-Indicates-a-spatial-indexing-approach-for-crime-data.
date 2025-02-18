import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A singly linked list to store crime records and export them to a KML file.
 */
public class ListOfCrimes {
    /**
     * Inner class representing a node in the linked list.
     */
    private static class Node {
        String crimeData;
        Node next;

        public Node(String crimeData) {
            this.crimeData = crimeData;
            this.next = null;
        }
    }

    private Node head, tail;  // Track both head and tail for efficient appending

    /**
     * Constructor initializes an empty list.
     */
    public ListOfCrimes() {
        head = tail = null;
    }

    /**
     * Adds a crime record to the list in FIFO order (appending at the end).
     *
     * @param crimeData The crime data as a CSV string.
     */
    public void addCrime(String crimeData) {
        Node newNode = new Node(crimeData);
        if (head == null) {
            head = tail = newNode; // First node
        } else {
            tail.next = newNode; // Append to the end
            tail = newNode;
        }
    }

    /**
     * Prints all stored crimes to the console.
     */
    public void printCrimes() {
        Node current = head;
        int count = 0;
        while (current != null) {
            System.out.println(current.crimeData);
            current = current.next;
            count++;
        }
        System.out.println("Found " + count + " crimes.");
    }

    /**
     * Exports the stored crime records to a Google Earth KML file.
     *
     * @param filename The output KML file name.
     */
    public void toKML(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<kml xmlns=\"http://earth.google.com/kml/2.2\">\n");
            writer.write("<Document>\n");

            Node current = head;
            while (current != null) {
                String[] parts = current.crimeData.split(",");

                // Ensure the data contains at least 10 columns (based on your dataset)
                if (parts.length <= 8) {
                    System.err.println("Skipping invalid crime data: " + current.crimeData);
                    current = current.next;
                    continue;
                }

                try {
                    double lat = Double.parseDouble(parts[7]);  // Latitude
                    double lon = Double.parseDouble(parts[8]);  // Longitude

                    writer.write("  <Placemark>\n");
                    writer.write("    <name>" + escapeXML(parts[4]) + "</name>\n");  // Crime type
                    writer.write("    <description>" + escapeXML(parts[3]) + "</description>\n");  // Street
                    writer.write("    <Point>\n");
                    writer.write("      <coordinates>" + lon + "," + lat + ",0</coordinates>\n");
                    writer.write("    </Point>\n");
                    writer.write("  </Placemark>\n");

                } catch (NumberFormatException e) {
                    System.err.println("Skipping malformed coordinates: " + current.crimeData);
                }

                current = current.next;
            }

            writer.write("</Document>\n</kml>");
            System.out.println("✅ KML file successfully created: " + filename);

        } catch (IOException e) {
            System.err.println("❌ Error writing KML file: " + e.getMessage());
        }
    }

    /**
     * Escapes special XML characters in a string.
     *
     * @param text The text to escape.
     * @return The escaped string safe for XML.
     */
    private String escapeXML(String text) {
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
