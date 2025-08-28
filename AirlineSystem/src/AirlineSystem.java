import java.util.*;

public class AirlineSystem {
    private Graph graph = new Graph();

    public void mainMenu() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("===========  Airline Flight System Menu  ===========");
            System.out.println("1. View Airports and Routes");
            System.out.println("2. Add / Remove Airport");
            System.out.println("3. Add / Remove Route");
            System.out.println("4. View Reachable Airports (using BFS)");
            System.out.println("5. Find Least Transits (using BFS)");
            System.out.println("6. Find Shortest Path by Cost/Time (using Dijkstra)");
            System.out.println("7. Exit & Save Graph");
            System.out.println("----------------------------------------------------");
            System.out.print("Choose an option (1-7): ");
            String choice = sc.next();
            sc.nextLine();

            switch (choice) {
                case "1" -> { // View airports and routes
                    cls();
                    graph.displayGraph();
                    pressEnterToContinue();
                    break;
                }
                case "2" -> { // Add / remove airport
                    addOrRemoveAirportOption(sc);
                    cls();
                    break;
                }
                case "3" -> { // Add / remove route
                    addOrRemoveRouteOption(sc);
                    cls();
                    break;
                }
                case "4" -> { // View reachable airports (BFS)
                    viewReachableAirportsOption(sc);
                    cls();
                    break;
                }
                case "5" -> { // Find least transits (BFS)
                    findLeastTransitsOption(sc);
                    cls();
                    break;
                }
                case "6" -> { // Find shortest path by cost/time (Dijkstra)
                    findShortestPathOption(sc);
                    cls();
                    break;
                }
                case "7" -> { // Exit system
                    System.out.println("Graph is saved to file.");
                    System.out.println("Exiting system. Goodbye!");
                    return; // Only way to exit the system
                }
                default -> {
                    System.out.println("Invalid option. Try again.");
                    pressEnterToContinue();
                    break;
                }
            }
        }
    }

    public void loadGraphFromFile(String filename) {
        GraphIO.loadGraph(graph, filename);
    }

    public void saveGraphToFile(String filename) {
        GraphIO.saveGraph(graph, filename);
    }

    private void addOrRemoveAirportOption(Scanner sc) {
        while (true) {
            cls();
            System.out.println("=======  Add / Remove Airport =======");
            System.out.println("1. Add Airport");
            System.out.println("2. Remove Airport");
            System.out.println("3. Back to Main Menu");
            System.out.println("---------------------------------------");
            System.out.print("Choose an option (1-3): ");
            String subChoice = sc.next();
            sc.nextLine();

            switch (subChoice) {
                case "1" -> { // Add airport
                    cls();
                    System.out.println("=====  Add Airport  =====");
                    System.out.println("-------------------------");

                    System.out.print("Enter airport name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter airport IATA code (3 letters): ");
                    String code = sc.nextLine();
                    System.out.print("Enter the state where the airport is located: ");
                    String state = sc.nextLine();

                    // Validate the airport code
                    if (isValidAirportCode(code)) {
                        code = code.trim().toUpperCase();
                        Vertex airport = new Vertex(name, code, state);
                        boolean added = graph.addAirport(airport);
                        if (added) {
                            System.out.println(
                                    "\nAirport added successfully: " + airport.toString() + ".");
                        } else {
                            Vertex existingAirport = graph.getAirports().get(code);
                            System.out
                                    .println("\nAirport with code " + code + " already exists. Cannot add duplicate.");
                            System.out
                                    .println("\nExisting airport: " + existingAirport.toString() + ".");
                        }
                    } else {
                        System.out.println(
                                "\nInvalid airport code, must be 3 letters and alphabetic characters only. Please try again.");
                    }
                    pressEnterToContinue();
                    break;
                }
                case "2" -> { // Remove airport
                    cls();
                    System.out.println("=====  Remove Airport  =====");
                    System.out.println("----------------------------");

                    System.out.print("Enter airport IATA code (3 letters): ");
                    String code = sc.nextLine();

                    // Validate the airport code
                    if (isValidAirportCode(code)) {
                        code = code.trim().toUpperCase();
                        boolean removed = graph.removeAirport(code);
                        if (removed) {
                            Vertex removedAirport = graph.getAirports().get(code);
                            System.out.println(
                                    "\nAirport removed successfully: " + removedAirport.toString() + ".");
                        } else {
                            System.out.println("\nAirport with code " + code + " does not exist. Cannot remove.");
                        }
                    } else {
                        System.out.println(
                                "\nInvalid airport code, must be 3 letters and alphabetic characters only. Please try again.");
                    }
                    pressEnterToContinue();
                    break;
                }
                case "3" -> { // Back to main menu
                    return; // Only way to exit the loop
                }
                default -> {
                    System.out.println("Invalid option. Try again.");
                    pressEnterToContinue();
                    break;
                }
            }
        }
    }

    private void addOrRemoveRouteOption(Scanner sc) {
        while (true) {
            cls();
            System.out.println("=======  Add / Remove Route =======");
            System.out.println("1. Add Route");
            System.out.println("2. Remove Route");
            System.out.println("3. Back to Main Menu");
            System.out.println("-------------------------------------");
            System.out.print("Choose an option (1-3): ");
            String subChoice = sc.next();
            sc.nextLine();

            switch (subChoice) {
                case "1" -> { // Add route
                    cls();
                    System.out.println("=====  Add Route  =====");
                    System.out.println("-----------------------");

                    String from = "";
                    while (true) { // Loop until valid starting airport code
                        System.out.print("Enter starting airport IATA code (3 letters): ");
                        from = sc.nextLine();
                        if (isValidAirportCode(from)) {
                            from = from.trim().toUpperCase();
                            break;
                        } else {
                            System.out.println(
                                    "Invalid airport code, must be 3 letters and alphabetic characters only. Please try again.");
                        }
                    }

                    String to = "";
                    while (true) { // Loop until valid destination airport code
                        System.out.print("Enter destination airport IATA code (3 letters): ");
                        to = sc.nextLine();
                        if (isValidAirportCode(to)) {
                            to = to.trim().toUpperCase();
                            break;
                        } else {
                            System.out.println(
                                    "Invalid airport code, must be 3 letters and alphabetic characters only. Please try again.");
                        }
                    }

                    double cost = -1;
                    while (cost <= 0) { // Loop until valid positive cost
                        System.out.print("Enter the cost of the flight (RM): ");
                        try {
                            cost = sc.nextDouble();
                            sc.nextLine(); // Consume the remaining newline
                            if (cost <= 0) {
                                System.out.println("Invalid input. Please enter a positive number for cost.");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input. Please enter a positive number for cost.");
                            sc.nextLine(); // Clear the invalid input
                        }
                    }

                    double time = -1;
                    while (time <= 0) { // Loop until valid positive time
                        System.out.print("Enter the time of the flight (hours): ");
                        try {
                            time = sc.nextDouble();
                            if (time <= 0) {
                                System.out.println("Invalid input. Please enter a positive number for time.");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input. Please enter a positive number for time.");
                            sc.nextLine(); // Clear the invalid input
                        }
                    }

                    Edge route = new Edge(from, to, cost, time);
                    graph.addRoute(route);

                    pressEnterToContinue();
                    break; // Exit the loop after successful addition

                }
                case "2" -> { // Remove route
                    cls();
                    System.out.println("=====  Remove Route  =====");
                    System.out.println("--------------------------");

                    System.out.print("Enter starting airport IATA code (3 letters): ");
                    String from = sc.nextLine();
                    System.out.print("Enter destination airport IATA code (3 letters): ");
                    String to = sc.nextLine();

                    // Validate the airport codes
                    if (isValidAirportCode(from) && isValidAirportCode(to)) {
                        from = from.trim().toUpperCase();
                        to = to.trim().toUpperCase();

                        boolean removed = graph.removeRoute(from, to);
                        if (removed) {
                            for (Edge edge : graph.getAdjacencyList().get(from)) {
                                if (edge.destinationCode.equals(to)) {
                                    System.out.println("\nRoute removed successfully: " + edge.toString() + ".");
                                    break;
                                }
                            }
                        } else {
                            System.out
                                    .println("\nRoute from " + from + " to " + to + " does not exist. Cannot remove.");
                        }
                    } else {
                        System.out.println(
                                "\nInvalid airport code, must be 3 letters and alphabetic characters only. Please try again.");
                    }
                    pressEnterToContinue();
                    break;
                }
                case "3" -> { // Back to main menu
                    return; // Only way to exit the loop
                }
                default -> {
                    System.out.println("Invalid option. Try again.");
                    pressEnterToContinue();
                    break;
                }
            }
        }
    }

    private void viewReachableAirportsOption(Scanner sc) {
        while (true) {
            cls();
            System.out.println("=====  View Reachable Airports =====");
            System.out.println("1. Enter Airport Code");
            System.out.println("2. Back to Main Menu");
            System.out.println("--------------------------------------");
            System.out.print("Choose an option (1-2): ");
            String subChoice = sc.next();
            sc.nextLine();

            switch (subChoice) {
                case "1" -> { // View reachable airports
                    cls();
                    System.out.print("Enter starting airport IATA code (3 letters): ");
                    String start = sc.next();
                    sc.nextLine();

                    // Check if the start code is a 3-letter code
                    if (isValidAirportCode(start)) {
                        start = start.trim().toUpperCase(); // Convert to uppercase
                        List<Vertex> reachable = graph.bfsReachable(start);
                        if (reachable == null) {
                            // This is to just filter out the null return value
                            // The bfsReachable method already prints the reason
                        } else if (reachable.isEmpty()) {
                            System.out.println("\nNo reachable airports from " + start);
                        } else {
                            System.out.println("\nReachable airports from " + start + ": ");
                            System.out.println(
                                    "======================================================================================");
                            System.out.printf("%-3s | %-45s | %-12s | %-15s%n", "No.", "Airport Name",
                                    "Airport Code", "Airport State");
                            System.out.println(
                                    "--------------------------------------------------------------------------------------");
                            for (int i = 0; i < reachable.size(); i++) {
                                Vertex airport = reachable.get(i);
                                System.out.printf("%-3d | %-45s | %-12s | %-15s%n", i + 1, airport.name,
                                        airport.code, airport.state);
                            }

                        }
                    } else {
                        System.out.println(
                                "Invalid airport code, must be 3-letters and alphabetic characters only. Please try again.");
                    }
                    pressEnterToContinue();
                    break;
                }
                case "2" -> { // Back to main menu
                    return; // Only way to exit the loop
                }
                default -> {
                    System.out.println("Invalid option. Try again.");
                    pressEnterToContinue();
                    break;
                }
            }
        }
    }

    private void findLeastTransitsOption(Scanner sc) {
        while (true) {
            cls();
            System.out.println("=======  Find Least Transits =======");
            System.out.println("1. Enter Airport Code");
            System.out.println("2. Back to Main Menu");
            System.out.println("--------------------------------------");
            System.out.print("Choose an option (1-2): ");
            String subChoice = sc.next();
            sc.nextLine();

            switch (subChoice) {
                case "1" -> { // Find least transits
                    cls();
                    String start = "";
                    while (true) { // Loop until valid starting airport code
                        System.out.print("Enter starting airport IATA code (3 letters): ");
                        start = sc.nextLine().trim().toUpperCase();
                        // Validate the start airport code
                        if (!isValidAirportCode(start)) {
                            System.out.println(
                                    "Invalid start airport code, must be 3 letters and alphabetic characters only. Please try again.");
                        } else {
                            break; // Exit the loop if the code is valid
                        }
                    }

                    String end = "";
                    while (true) { // Loop until valid destination airport code
                        System.out.print("Enter destination airport IATA code (3 letters): ");
                        end = sc.nextLine().trim().toUpperCase();
                        // Validate the end airport code
                        if (!isValidAirportCode(end)) {
                            System.out.println(
                                    "Invalid end airport code, must be 3 letters and alphabetic characters only. Please try again.");
                        } else {
                            break; // Exit the loop if the code is valid
                        }
                    }

                    List<Edge> leastTransitsPath = graph.bfsShortestPath(start, end);
                    if (leastTransitsPath == null) {
                        // This is to just filter out the null return value
                        // The bfsShortestPath method already prints the reason
                    } else if (leastTransitsPath.isEmpty()) {
                        System.out.println("\nNo path found from " + start + " to " + end + ".");
                    } else {
                        System.out.println("\nLeast transits path from " + start + " to " + end + ": ");
                        System.out.println(
                                "===============================================================================================================================");
                        System.out.printf("%-3s | %-45s | %-45s | %-10s | %-10s%n", "No.", "From", "To",
                                "Cost (RM)", "Time (hrs)");
                        System.out.println(
                                "-------------------------------------------------------------------------------------------------------------------------------");
                        double totalCost = 0;
                        double totalTime = 0;
                        for (int i = 0; i < leastTransitsPath.size(); i++) {
                            Edge edge = leastTransitsPath.get(i);
                            Vertex from = graph.getAirports().get(edge.startingCode);
                            Vertex to = graph.getAirports().get(edge.destinationCode);
                            totalCost += edge.cost;
                            totalTime += edge.time;
                            System.out.printf("%-3d | %-39s (%3s) | %-39s (%3s) | %-10.2f | %-10.2f%n", i + 1,
                                    from.name, from.code, to.name, to.code, edge.cost, edge.time);
                        }
                        System.out.println(
                                "-------------------------------------------------------------------------------------------------------------------------------");
                        // no of transits = no of edges - 1
                        System.out.printf("Number of Transits: %s times %n", leastTransitsPath.size() - 1);
                        System.out.printf("Total Cost: RM %.2f%n", totalCost);
                        System.out.printf("Total Time: %.2f hours%n", totalTime);
                    }
                    pressEnterToContinue();
                    break;
                }
                case "2" -> { // Back to main menu
                    return; // Only way to exit the loop
                }
                default -> {
                    System.out.println("Invalid option. Try again.");
                    pressEnterToContinue();
                    break;
                }
            }
        }
    }

    private void findShortestPathOption(Scanner sc) {
        while (true) {
            cls();
            System.out.println("========  Find Shortest Path ========");
            System.out.println("1. Find Shortest Path by Cost/Time");
            System.out.println("2. Back to Main Menu");
            System.out.println("---------------------------------------");
            System.out.print("Choose an option (1-2): ");
            String subChoice = sc.next();
            sc.nextLine();

            switch (subChoice) {
                case "1" -> { // Find shortest path by cost/time
                    cls();
                    boolean useCost = true;

                    boolean isValidCostOption = false;
                    while (!isValidCostOption) { // Ask for cost/time option
                        System.out.println("====  Find Shortest Path Method  ====");
                        System.out.println("1. By Cost (in RM)");
                        System.out.println("2. By Time (in hours)");
                        System.out.println("3. Back to Previous Menu");
                        System.out.println("---------------------------------------");
                        System.out.print("Choose an option (1-2): ");
                        String costOption = sc.next();
                        sc.nextLine();

                        if (costOption.equals("1")) { // By cost
                            useCost = true;
                            isValidCostOption = true;
                            pressEnterToContinue();
                        } else if (costOption.equals("2")) { // By time
                            useCost = false;
                            isValidCostOption = true;
                            pressEnterToContinue();
                        } else if (costOption.equals("3")) { // Back to previous menu
                            break; // Exit the loop
                        } else {
                            System.out.println("Invalid option. Try again.");
                            pressEnterToContinue();
                        }

                    }

                    if (!isValidCostOption) {
                        break; // Go back to previous menu
                    }

                    String start = null;
                    while (true) { // Loop until valid starting airport code
                        System.out.print("Enter starting airport IATA code (3 letters): ");
                        start = sc.nextLine().trim().toUpperCase();

                        if (isValidAirportCode(start)) {
                            break; // Exit the loop if the code is valid
                        } else {
                            System.out.println(
                                    "Invalid start airport code, must be 3 letters and alphabetic characters only. Please try again.");
                        }
                    }

                    String end = null;
                    while (true) { // Loop until valid destination airport code
                        System.out.print("Enter destination airport IATA code (3 letters): ");
                        end = sc.nextLine().trim().toUpperCase();

                        if (isValidAirportCode(end)) {
                            break; // Exit the loop if the code is valid
                        } else {
                            System.out.println(
                                    "Invalid end airport code, must be 3 letters and alphabetic characters only. Please try again.");
                        }
                    }

                    List<Edge> shortestPath = graph.dijkstra(start, end, useCost);
                    if (shortestPath == null) {
                        // This is to just filter out the null return value
                        // The dijkstra method already prints the reason
                    } else if (shortestPath.isEmpty()) {
                        System.out.println("\nNo path found from " + start + " to " + end + ".");
                    } else {
                        System.out.println("\nShortest path from " + start + " to " + end + ": ");
                        System.out.println(
                                "===============================================================================================================================");
                        System.out.printf("%-3s | %-45s | %-45s | %-10s | %-10s%n", "No.", "From", "To",
                                "Cost (RM)", "Time (hrs)");
                        System.out.println(
                                "-------------------------------------------------------------------------------------------------------------------------------");
                        double totalCost = 0;
                        double totalTime = 0;
                        for (int i = 0; i < shortestPath.size(); i++) {
                            Edge edge = shortestPath.get(i);
                            Vertex from = graph.getAirports().get(edge.startingCode);
                            Vertex to = graph.getAirports().get(edge.destinationCode);
                            totalCost += edge.cost;
                            totalTime += edge.time;
                            System.out.printf("%-3d | %-39s (%3s) | %-39s (%3s) | %-10.2f | %-10.2f%n", i + 1,
                                    from.name, from.code, to.name, to.code, edge.cost, edge.time);
                        }
                        System.out.println(
                                "-------------------------------------------------------------------------------------------------------------------------------");
                        System.out.printf("Number of Transits: %s times %n", shortestPath.size() - 1);
                        System.out.printf("Total Cost: RM %.2f%n", totalCost);
                        System.out.printf("Total Time: %.2f hours%n", totalTime);
                    }
                    pressEnterToContinue();
                    break;
                }
                case "2" -> { // Back to main menu
                    return; // Only way to exit the loop
                }
                default -> {
                    System.out.println("Invalid option. Try again.");
                    pressEnterToContinue();
                    break;
                }
            }
        }
    }

    private void pressEnterToContinue() {
        System.out.print("\nPress enter to continue.......");
        @SuppressWarnings("resource")
        Scanner sc = new Scanner(System.in);
        sc.nextLine(); // This will consume the rest of the line, dumping any extra input
        cls();
        return;
    }

    private void cls() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private boolean isValidAirportCode(String code) {
        // Check if the code is a 3-letter code
        return code.trim().matches("^[a-zA-Z]{3}$");
    }

    @SuppressWarnings("unused")
    private Graph initializeSampleData() {
        // Initialize 10 Malaysian airports
        List<Vertex> airports = new ArrayList<>();
        airports.add(new Vertex("Kuala Lumpur International Airport", "KUL", "Selangor"));
        airports.add(new Vertex("Penang International Airport", "PEN", "Penang"));
        airports.add(new Vertex("Langkawi International Airport", "LGK", "Kedah"));
        airports.add(new Vertex("Kota Kinabalu International Airport", "BKI", "Sabah"));
        airports.add(new Vertex("Kuching International Airport", "KCH", "Sarawak"));
        airports.add(new Vertex("Johor Bahru Senai Airport", "JHB", "Johor"));
        airports.add(new Vertex("Sultan Ismail Petra Airport", "KBR", "Kelantan"));
        airports.add(new Vertex("Sultan Mahmud Airport", "TGG", "Terengganu"));
        airports.add(new Vertex("Miri Airport", "MYY", "Sarawak"));
        airports.add(new Vertex("Ipoh Sultan Azlan Shah Airport", "IPH", "Perak"));

        // Add realistic domestic routes with cost (RM) and time (hours)
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge("KUL", "PEN", 150, 1.5));
        edges.add(new Edge("KUL", "LGK", 120, 1.25));
        edges.add(new Edge("KUL", "JHB", 80, 1));
        edges.add(new Edge("KUL", "KCH", 280, 2.5));
        edges.add(new Edge("PEN", "LGK", 90, 1));
        edges.add(new Edge("PEN", "KBR", 110, 1.75));
        edges.add(new Edge("BKI", "KCH", 200, 2));
        edges.add(new Edge("BKI", "MYY", 150, 1.5));
        edges.add(new Edge("JHB", "KBR", 130, 2));
        edges.add(new Edge("TGG", "KBR", 70, 1));
        edges.add(new Edge("TGG", "KUL", 160, 1.75));
        edges.add(new Edge("IPH", "KUL", 100, 1.25));
        edges.add(new Edge("IPH", "PEN", 85, 1));
        edges.add(new Edge("MYY", "KCH", 90, 1));
        edges.add(new Edge("LGK", "KCH", 320, 3));
        edges.add(new Edge("JHB", "PEN", 140, 1.5));

        Graph graph = new Graph(airports, edges);
        return graph;
    }
}
