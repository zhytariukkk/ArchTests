package lr1.lr1.service;

import lr1.lr1.model.Room;
import lr1.lr1.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElseThrow();
    }

    public Room createRoom(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Room must not be null");
        }
        return roomRepository.save(room);
    }

    public Room updateRoom(Long id, Room room) {
        // якщо room == null — впаде NullPointerException, як у тестах
        room.setId(id);
        return roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        if (!roomRepository.existsById(id)) {
            throw new EmptyResultDataAccessException("Room not found", 1);
        }
        roomRepository.deleteById(id);
    }
}
