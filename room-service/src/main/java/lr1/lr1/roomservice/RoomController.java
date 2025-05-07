package lr1.lr1.roomservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {


    public RoomController() {
    }

    @GetMapping("/{id}")
    public Room getRoom(@PathVariable("id") Long id) {
        return new Room(id, "Room#" + id);
    }
}
