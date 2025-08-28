public class Edge {
    public String startingCode;         // Code of the starting airport
    public String destinationCode;      // Code of the destination airport
    public double cost;                 // Price of the flight route (in RM)
    public double time;                 // Flight time of the route (in hours)

    public Edge(String startingCode, String destinationCode, double cost, double time) {
        this.startingCode = startingCode;
        this.destinationCode = destinationCode;
        this.cost = cost;
        this.time = time;
    }

    @Override
    public boolean equals(Object obj) {
        // Check if the object is an instance of Edge
        if ((obj instanceof Edge)) {
            Edge edge = (Edge) obj;
            // Check if both starting and destination codes are the same
            return this.startingCode.equals(edge.startingCode) && this.destinationCode.equals(edge.destinationCode);
        };
        return false;   // Return false if the object is not an Edge
    }

    @Override
    public String toString() {
        return String.format("From %s to %s | Cost: RM%.2f | Time: %.2f hours", startingCode, destinationCode, cost, time);
    }
}