package lr1.lr1.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;  // Назва пристрою

    @Column(nullable = false)
    private String type;  // Тип пристрою (наприклад, освітлення, опалення)

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;  // Пристрій належить певній кімнаті

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
