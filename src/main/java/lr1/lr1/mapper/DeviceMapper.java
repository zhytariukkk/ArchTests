package lr1.lr1.mapper;

import lr1.lr1.exception.*;
import lr1.lr1.dto.DeviceDTO;
import lr1.lr1.model.Device;
import lr1.lr1.model.Room;
import lr1.lr1.repository.RoomRepository;
import org.springframework.stereotype.Component;

@Component
public class DeviceMapper {
    private final RoomRepository roomRepository;

    public DeviceMapper(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Device toEntity(DeviceDTO dto) {
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Кімната не знайдена"));

        return new Device(dto.getId(), dto.getName(), dto.getType(), dto.isStatus(), room);
    }

    public DeviceDTO toDTO(Device device) {
        return new DeviceDTO(device.getId(), device.getName(), device.getType(), device.isStatus(), device.getRoom().getId());
    }
}
