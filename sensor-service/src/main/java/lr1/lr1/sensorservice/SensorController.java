package lr1.lr1.sensorservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/sensorservices")
public class SensorController {


    @GetMapping("/{id}")
    public Sensor getSensor(@PathVariable("id") Long id) {
        return new Sensor(id, "Sensor#" + id, "DefaultType");
    }
}
