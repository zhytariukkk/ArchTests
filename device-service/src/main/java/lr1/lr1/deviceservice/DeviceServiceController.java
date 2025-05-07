package lr1.lr1.deviceservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lr1.lr1.roomservice.Room;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/devices")
class DeviceController {

    private final RestTemplate rest;
    @Autowired
    public DeviceController(RestTemplate rest) {
        this.rest = rest;
    }

    @GetMapping("/{id}")
    public Device getDevice(@PathVariable("id") Long id) {
        return new Device(id, "Device#" + id);
    }

    @GetMapping("/{id}/room")
    public Room getDeviceRoom(@PathVariable Long id) {
        return rest.getForObject("http://localhost:8082/api/rooms/" + id, Room.class);
    }
}
