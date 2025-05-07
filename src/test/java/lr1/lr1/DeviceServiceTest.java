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
class DeviceServiceTest {

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

    @Test
    @DisplayName("updateDevice: встановлює ID та повертає оновлений пристрій")
    void updateDevice_shouldSetIdAndReturnUpdatedDevice() {
        Device toUpdate = new Device();
        toUpdate.setName("NewName");
        Device saved = new Device();
        saved.setId(1L);
        saved.setName("NewName");

        when(deviceRepository.save(any(Device.class))).thenReturn(saved);

        Device result = deviceService.updateDevice(1L, toUpdate);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("NewName");
        ArgumentCaptor<Device> captor = ArgumentCaptor.forClass(Device.class);
        verify(deviceRepository).save(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("updateDevice: кидає RuntimeException, якщо save() провалюється")
    void updateDevice_shouldPropagateException_whenSaveFails() {
        Device anyDevice = new Device();
        when(deviceRepository.save(any(Device.class))).thenThrow(new RuntimeException("Update failed"));

        assertThatThrownBy(() -> deviceService.updateDevice(5L, anyDevice))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Update failed");
        verify(deviceRepository).save(any(Device.class));
    }

    @Test
    @DisplayName("updateDevice: встановлює null ID, коли передано null")
    void updateDevice_nullIdSetsNull() {
        Device toUpdate = new Device();
        toUpdate.setName("Name");
        when(deviceRepository.save(any(Device.class))).thenAnswer(inv -> inv.getArgument(0));

        Device result = deviceService.updateDevice(null, toUpdate);

        assertThat(result.getId()).isNull();
        assertThat(result.getName()).isEqualTo("Name");
        verify(deviceRepository).save(toUpdate);
    }

    @Test
    @DisplayName("updateDevice: переписує початковий ID пристрою")
    void updateDevice_overwritesExistingId() {
        Device toUpdate = new Device();
        toUpdate.setId(5L);
        toUpdate.setName("Name");
        when(deviceRepository.save(any(Device.class))).thenAnswer(inv -> inv.getArgument(0));

        Device result = deviceService.updateDevice(10L, toUpdate);

        assertThat(result.getId()).isEqualTo(10L);
        verify(deviceRepository).save(toUpdate);
    }

    @Test
    @DisplayName("updateDevice: кидає NullPointerException, якщо device null")
    void updateDevice_nullDeviceThrows() {
        assertThatThrownBy(() -> deviceService.updateDevice(1L, null))
                .isInstanceOf(NullPointerException.class);
        verify(deviceRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateDevice: не викликає deleteById або findById")
    void updateDevice_noUnrelatedRepoCalls() {
        Device d = new Device();
        when(deviceRepository.save(any())).thenReturn(d);

        deviceService.updateDevice(1L, d);

        verify(deviceRepository).save(any());
        verify(deviceRepository, never()).deleteById(any());
        verify(deviceRepository, never()).findById(any());
    }

    @Test
    @DisplayName("deleteDevice: викликає deleteById з правильним ID")
    void deleteDevice_shouldCallDeleteById() {
        doNothing().when(deviceRepository).deleteById(3L);

        deviceService.deleteDevice(3L);

        verify(deviceRepository).deleteById(3L);
    }

    @Test
    @DisplayName("deleteDevice: кидає RuntimeException, якщо deleteById() провалюється")
    void deleteDevice_shouldPropagateException_whenDeleteFails() {
        doThrow(new RuntimeException("Delete error")).when(deviceRepository).deleteById(4L);

        assertThatThrownBy(() -> deviceService.deleteDevice(4L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Delete error");
        verify(deviceRepository).deleteById(4L);
    }

    @Test
    @DisplayName("deleteDevice: кидає IllegalArgumentException при null-ID")
    void deleteDevice_nullIdThrows() {
        doThrow(new IllegalArgumentException("ID is null")).when(deviceRepository).deleteById(null);

        assertThatThrownBy(() -> deviceService.deleteDevice(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ID is null");
        verify(deviceRepository).deleteById(null);
    }

    @Test
    @DisplayName("deleteDevice: не викликає save або findById")
    void deleteDevice_noUnrelatedRepoCalls() {
        doNothing().when(deviceRepository).deleteById(2L);

        deviceService.deleteDevice(2L);

        verify(deviceRepository).deleteById(2L);
        verify(deviceRepository, never()).save(any());
        verify(deviceRepository, never()).findById(any());
    }
}
