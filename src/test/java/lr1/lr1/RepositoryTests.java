package lr1.lr1;

import lr1.lr1.model.Device;
import lr1.lr1.repository.DeviceRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RepositoryTests {

    @Autowired
    private DeviceRepository deviceRepository;

    @BeforeEach
    void setUp() {
        deviceRepository.save(new Device("Lamp", "Lighting", null));
        deviceRepository.save(new Device("Heater", "Heating", null));
    }

    @AfterEach
    void tearDown() {
        deviceRepository.deleteAll();
    }

    @Test
    void shouldFindAllDevices() {
        List<Device> devices = deviceRepository.findAll();
        assertThat(devices).hasSize(2);
    }

    @Test
    void shouldFindById() {
        Device device = deviceRepository.save(new Device("Thermostat", "Climate", null));
        Optional<Device> found = deviceRepository.findById(device.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Thermostat");
    }

    @Test
    void shouldUpdateDevice() {
        Device device = deviceRepository.findAll().get(0);
        device.setName("Updated Lamp");
        deviceRepository.save(device);

        Device updated = deviceRepository.findById(device.getId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo("Updated Lamp");
    }

    @Test
    void shouldDeleteDevice() {
        Device device = deviceRepository.findAll().get(0);
        deviceRepository.deleteById(device.getId());

        Optional<Device> found = deviceRepository.findById(device.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void shouldFindDevicesByType() {
        List<Device> devices = deviceRepository.findAll();
        List<Device> lightingDevices = devices.stream()
                .filter(d -> "Lighting".equals(d.getType()))
                .toList();

        assertThat(lightingDevices).hasSize(1);
        assertThat(lightingDevices.get(0).getName()).isEqualTo("Lamp");
    }

    @Test
    void shouldSaveNewDevice() {
        Device device = new Device("Fan", "Cooling", null);
        Device saved = deviceRepository.save(device);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Fan");
    }

    @Test
    void shouldExistById() {
        Device device = deviceRepository.findAll().get(0);
        boolean exists = deviceRepository.existsById(device.getId());

        assertThat(exists).isTrue();
    }

    @Test
    void shouldCountDevices() {
        long count = deviceRepository.count();
        assertThat(count).isEqualTo(2);
    }

    @Test
    void shouldDeleteAllDevices() {
        deviceRepository.deleteAll();
        List<Device> devices = deviceRepository.findAll();

        assertThat(devices).isEmpty();
    }

    @Test
    void shouldAssignIdAfterSave() {
        Device device = new Device("Speaker", "Audio", null);
        Device saved = deviceRepository.save(device);

        assertThat(saved.getId())
                .isNotNull()
                .isGreaterThan(0);
    }
}
