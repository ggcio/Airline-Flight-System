import java.util.*;

public class Graph {
    private Map<String, Vertex> airports = new HashMap<>(); // Map of airports with their codes as keys
    private Map<String, List<Edge>> adjacencyList = new HashMap<>(); // Adjacency list

    public Graph() {
    } // Empty constructor

    public Graph(List<Vertex> airports, List<Edge> routes) {
        for (Vertex airport : airports) {
            this.airports.put(airport.code, airport);
            adjacencyList.put(airport.code, new ArrayList<>());
        }

        for (Edge route : routes) {
            addRoute(route);
        }
    }

    public boolean addAirport(Vertex airport) {
        if (!airports.containsKey(airport.code)) {
            airports.put(airport.code, airport);
            adjacencyList.put(airport.code, new ArrayList<>());
            return true;    // Airport added successfully
        }
        return false; // Airport already exists
    }

    public boolean removeAirport(String airportCode) {
        if (airports.containsKey(airportCode)) {
            // Remove the airport from the airports map and adjacency list
            airports.remove(airportCode);
            adjacencyList.remove(airportCode);

            // Remove all the edges that have this airport as the destination
            for (List<Edge> edges : adjacencyList.values()) {
                edges.removeIf(e -> e.destinationCode.equals(airportCode));
            }
            return true;    // Airport removed successfully
        }
        return false; // Airport not found
    }

    /** 
    *   this function must have print statements to display the detailed reason
    *   why the route is not added (e.g. route already exists, one or both airports
    *   do not exist, etc.) 
    **/
    public boolean addRoute(Edge route) {
        if (route.startingCode.equals(route.destinationCode)) { // Filter out self-loop routes
            System.out.println("Cannot add a route from " + route.startingCode + " airport to itself!");
            return false; // Cannot add a route from an airport to itself
        }

        // Route that enters here is not a self-loop
        else if (airports.containsKey(route.startingCode) && airports.containsKey(route.destinationCode)) {
            // Must check if the adjacency list for the starting airport is not null
            // If it's null, directly use adjacencyList.get(route.startingCode) will cause NPE
            if (adjacencyList.get(route.startingCode) != null) {
                for (Edge r : adjacencyList.get(route.startingCode)) {
                    // Check if the route already exists in the adjacency list
                    if (route.equals(r)) {
                        System.out.println(
                                "\nRoute already exists from " + route.startingCode + " to " + route.destinationCode);
                        return false; // Route already exists
                    }
                }
            }
            // Route reaches here means the route does not exist, so we can add it
            adjacencyList.get(route.startingCode).add(route);
            System.out.println("\nRoute successfully added: " + route.toString() + ".");
            return true; // Route added successfully
        }

        else { // Route that enters here does not have both airports existing in the graph
            System.out.println(
                    "\nOne or both airports do not exist: " + route.startingCode + " / " + route.destinationCode);
            return false; // One or both airports do not exist
        }
    }

    public boolean removeRoute(String from, String to) {
        List<Edge> routes = adjacencyList.get(from);
        if (routes != null) {
            for (Edge route : routes) {
                if (route.destinationCode.equals(to)) {
                    routes.remove(route);
                    return true; // Route removed successfully
                }
            }
        }
        return false; // Route not found
    }

    public void displayGraph() {
        System.out.println("\nAirline Routes Graph:");
        System.out.println(
                "===========================================================================================================================");
        System.out.printf("%-3s | %-45s | %-45s | %-10s | %-10s%n", "No.", "Starting Airport", "Destination Airport", "Cost (RM)",
                "Time (h)");
        System.out.println(
                "---------------------------------------------------------------------------------------------------------------------------");
        
        int count = 0;
        for (String airportCode : adjacencyList.keySet()) {
            Vertex airport = airports.get(airportCode);

            for (Edge route : adjacencyList.get(airportCode)) {
                Vertex destinationAirport = airports.get(route.destinationCode);
                count++;

                System.out.printf("%-3d | %-39s (%3s) | %-39s (%3s) | %-10.2f | %-10.2f%n", count,
                        airport.name, airport.code, destinationAirport.name, destinationAirport.code, route.cost,
                        route.time);
            }
        }
    }

    public List<Vertex> bfsReachable(String startCode) {
        // return null = startCode does not exist (got output in this function)
        // return empty list = startCode is not reachable from any other airport (no
        // output in this function)
        // return non-empty list = there are reachable airports from startCode (no
        // output in this function)
        if (!airports.containsKey(startCode)) {
            System.out.println("Airport with code " + startCode + " does not exist.");
            return null;
        }

        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        queue.offer(startCode);
        visited.add(startCode);

        while (!queue.isEmpty()) {
            String current = queue.poll();

            for (Edge edge : adjacencyList.getOrDefault(current, new ArrayList<>())) {
                if (!visited.contains(edge.destinationCode)) {
                    visited.add(edge.destinationCode);
                    queue.offer(edge.destinationCode);
                }
            }
        }

        visited.remove(startCode); // Remove starting node (only want reachable nodes)
        List<Vertex> reachableAirports = new ArrayList<>();

        // Convert visited codes to Vertex objects
        for (String code : visited) {
            reachableAirports.add(airports.get(code));
        }
        return reachableAirports;
    }

    public List<Edge> bfsShortestPath(String startCode, String destinationCode) {
        // return null = startCode or destinationCode does not exist (got output in this
        // function)
        // return null = startCode and destinationCode are the same airport (got output
        // in this function)
        // return empty list = destinationCode is unreachable from startCode (no output
        // in this function)
        // return non-empty list = the shortest path from startCode to destinationCode
        // (no output in this function)

        if (!airports.containsKey(startCode) || !airports.containsKey(destinationCode)) {
            System.out.println("One or both airports do not exist: " + startCode + " / " + destinationCode);
            return null; // One or both airports do not exist
        }
        if (startCode.equals(destinationCode)) {
            System.out.println("Start and destination airports should not be the same: " + startCode);
            return null; // Start and destination airports are the same
        }

        Queue<String> queue = new LinkedList<>();
        // key = child, value = parent
        Map<String, String> parents = new HashMap<>();
        Set<String> visited = new HashSet<>();

        queue.offer(startCode);
        visited.add(startCode);
        parents.put(startCode, null);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            // current node is destination, return the path
            if (current.equals(destinationCode)) {
                List<Edge> path = new ArrayList<>();
                // Backtrack to get the path
                // until we reach the starting airport (airport with null parent)
                for (String node = destinationCode; node != null; node = parents.get(node)) {
                    String parentNode = parents.get(node);
                    if (parentNode != null) {
                        for (Edge edge : adjacencyList.get(parentNode)) {
                            if (edge.destinationCode.equals(node)) {
                                path.add(edge);
                                break;
                            }
                        }
                    }
                }
                // reverse the path to get the correct order
                Collections.reverse(path);
                return path;
            }

            // Add children to queue and mark as visited
            for (Edge edge : adjacencyList.getOrDefault(current, new ArrayList<>())) {
                if (!visited.contains(edge.destinationCode)) {
                    visited.add(edge.destinationCode);
                    parents.put(edge.destinationCode, current);
                    queue.offer(edge.destinationCode);
                }
            }
        }
        return Collections.emptyList(); // Destination unreachable
    }

    public List<Edge> dijkstra(String start, String end, boolean useCost) {
        // return null = start or end does not exist (got output in this function)
        // return null = start and end are the same airport (got output in this
        // function)
        // return empty list = end is unreachable from start (no output in this
        // function)
        // return non-empty list = the path is the optimal path from start to end (no
        // output in this function)
        if (!airports.containsKey(start) || !airports.containsKey(end)) {
            System.out.println("One or both airports do not exist: " + start + " / " + end);
            return null;
        }
        if (start.equals(end)) {
            System.out.println("Start and destination airports should not be the same: " + start);
            return null;
        }

        // Distance from start to each airport (key = destination airport, value = cost
        // or time)
        Map<String, Double> dist = new HashMap<>();
        // Previous airport in optimal path (key = current airport, value = previous
        // airport)
        Map<String, String> prev = new HashMap<>();
        // Edge used to reach each airport (key = current airport, value = edge)
        Map<String, Edge> prevEdge = new HashMap<>();

        for (String code : airports.keySet()) {
            dist.put(code, Double.POSITIVE_INFINITY); // initialisng all distance to infinity
            prev.put(code, null);
            prevEdge.put(code, null);
        }
        dist.put(start, 0.0);

        // Priority queue that stores airport codes (sorted by distance)
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        pq.add(start);

        while (!pq.isEmpty()) {
            String current = pq.poll();

            if (current.equals(end))
                break; // Found destination, exit loop

            for (Edge edge : adjacencyList.getOrDefault(current, new ArrayList<>())) {
                double weight = useCost ? edge.cost : edge.time;
                double alt = dist.get(current) + weight; // Calculate new distance from start to destination

                // If new distance from start to destination is shorter than previously known
                // distance, then update
                if (alt < dist.get(edge.destinationCode)) {
                    dist.put(edge.destinationCode, alt); // Update shortest distance
                    prev.put(edge.destinationCode, current); // Record best way to reach destination is via current
                                                             // airport
                    prevEdge.put(edge.destinationCode, edge); // Record the edge used to reach destination
                    pq.remove(edge.destinationCode); // Remove old entry (if any)
                    pq.add(edge.destinationCode); // Add new entry with updated priority
                }
            }
        }

        // Reconstruct path
        List<Edge> path = new ArrayList<>();
        String node = end;
        while (prev.get(node) != null) {
            Edge edge = prevEdge.get(node); // edge = best edge used to reach node
            if (edge != null) {
                path.add(edge);
                node = prev.get(node); // Move to parent node
            } else
                break;
        }
        Collections.reverse(path); // Reverse the path to get the correct order
        return path;
    }

    public Map<String, Vertex> getAirports() {
        return airports;
    }

    public Map<String, List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }
}
