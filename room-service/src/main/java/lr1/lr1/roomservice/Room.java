package lr1.lr1.roomservice;

public class Room {
    private Long id;
    private String name;

    private String location;

    public Room(Long id, String name) {
        this.id = id;
        this.name = name;
        this.location = "Unknown";  // або будь-яке дефолтне значення
    }

    public Room(Long id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }
    public Long getId() { return id;}
    public void setId(Long id) { this.id = id;}
    public String getName() { return name;}
    public void setName(String name) { this.name = name;}

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}
