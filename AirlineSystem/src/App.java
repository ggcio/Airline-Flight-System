
public class App {
    public static void main(String[] args) throws Exception {
        AirlineSystem as = new AirlineSystem();

        // Load graph data from file
        as.loadGraphFromFile("graph.json");
        // Show the main menu
        as.mainMenu();
        // Save graph data to file before exiting
        as.saveGraphToFile("graph.json");
    }
}
