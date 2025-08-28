public class Vertex {
    public String name;     // Name of the airport
    public String code;     // Unique code of the airport
    public String state;    // State where the airport is located

    public Vertex(String name, String code, String state) {
        this.name = name;
        this.code = code;
        this.state = state;
    }

    @Override
    public String toString() {
        return String.format(name + " (" + code + ") in " + state);
    }
}
