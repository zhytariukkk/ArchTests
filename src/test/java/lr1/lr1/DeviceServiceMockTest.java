package lr1.lr1;

import lr1.lr1.model.Device;
import lr1.lr1.repository.DeviceRepository;
import lr1.lr1.service.DeviceService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceServiceMockTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceService deviceService;

    private Device sampleDevice;

    @BeforeEach
    void setUp() {
        sampleDevice = new Device();
        sampleDevice.setId(1L);
        sampleDevice.setName("TestDevice");
    }

    @AfterEach
    void tearDown() {
        clearInvocations(deviceRepository);
    }

    @Test
    @DisplayName("getDeviceById: повертає пристрій, якщо він є")
    void getDeviceById_shouldReturnDevice_whenFound() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(sampleDevice));

        Device result = deviceService.getDeviceById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("TestDevice");
        verify(deviceRepository).findById(1L);
    }

    @Test
    @DisplayName("getDeviceById: кидає NoSuchElementException, якщо пристрій не знайдено")
    void getDeviceById_shouldThrow_whenNotFound() {
        when(deviceRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deviceService.getDeviceById(2L))
                .isInstanceOf(NoSuchElementException.class);
        verify(deviceRepository).findById(2L);
    }

    @Test
    @DisplayName("getDeviceById: кидає RuntimeException, якщо репозиторій падає")
    void getDeviceById_shouldPropagateRepoException() {
        when(deviceRepository.findById(1L)).thenThrow(new RuntimeException("DB down"));

        assertThatThrownBy(() -> deviceService.getDeviceById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("DB down");
        verify(deviceRepository).findById(1L);
    }

    @Test
    @DisplayName("getDeviceById: повідомлення виключення 'No value present' при відсутності значення")
    void getDeviceById_exceptionMessageCorrect() {
        when(deviceRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deviceService.getDeviceById(2L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No value present");
        verify(deviceRepository).findById(2L);
    }

    @Test
    @DisplayName("getDeviceById: делегує пошук репозиторію з правильним ID")
    void getDeviceById_shouldDelegateToRepository() {
        when(deviceRepository.findById(7L)).thenReturn(Optional.of(sampleDevice));

        deviceService.getDeviceById(7L);

        verify(deviceRepository).findById(7L);
    }

    @Test
    @DisplayName("createDevice: зберігає і повертає пристрій")
    void createDevice_shouldSaveAndReturnDevice() {
        when(deviceRepository.save(sampleDevice)).thenReturn(sampleDevice);

        Device result = DeviceService.createDevice(sampleDevice);

        assertThat(result).isSameAs(sampleDevice);
        verify(deviceRepository).save(sampleDevice);
    }

    @Test
    @DisplayName("createDevice: кидає RuntimeException, якщо save() провалюється")
    void createDevice_shouldPropagateException_whenSaveFails() {
        when(deviceRepository.save(sampleDevice)).thenThrow(new RuntimeException("DB error"));

        assertThatThrownBy(() -> DeviceService.createDevice(sampleDevice))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("DB error");
        verify(deviceRepository).save(sampleDevice);
    }

    @Test
    @DisplayName("createDevice: кидає NullPointerException при null-пристрої")
    void createDevice_nullInput() {
        when(deviceRepository.save(null)).thenThrow(new NullPointerException("Device is null"));

        assertThatThrownBy(() -> DeviceService.createDevice(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Device is null");
        verify(deviceRepository).save(null);
    }

    @Test
    @DisplayName("createDevice: multiple invocations створюють два різні пристрої")
    void createDevice_multipleInvocations() {
        Device d1 = new Device();
        d1.setName("Device-One");
        d1.setType("T1");
        Device d2 = new Device();
        d2.setName("Device-Two");
        d2.setType("T2");

        when(deviceRepository.save(d1)).thenReturn(d1);
        when(deviceRepository.save(d2)).thenReturn(d2);

        DeviceService.createDevice(d1);
        DeviceService.createDevice(d2);

        verify(deviceRepository, times(1)).save(d1);
        verify(deviceRepository, times(1)).save(d2);
    }


    @Test
    @DisplayName("createDevice: не викликає findById")
    void createDevice_noFindByIdCalls() {
        when(deviceRepository.save(sampleDevice)).thenReturn(sampleDevice);

        DeviceService.createDevice(sampleDevice);

        verify(deviceRepository).save(sampleDevice);
        verify(deviceRepository, never()).findById(any());
    }


}
