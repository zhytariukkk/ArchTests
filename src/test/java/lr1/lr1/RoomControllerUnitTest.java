// src/test/java/lr1/lr1/RoomControllerUnitTest.java
package lr1.lr1;

import lr1.lr1.controller.RoomController;
import lr1.lr1.model.Room;
import lr1.lr1.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class RoomControllerUnitTest {

    @Mock
    RoomService roomService;

    @InjectMocks
    RoomController controller;

    private Room room;

    @BeforeEach
    void setUp() {
        room = new Room();
        room.setId(1L);
        room.setName("TestRoom");
    }

    @Test
    void getRoom_returnsOk() {
        given(roomService.getRoomById(1L)).willReturn(room);

        ResponseEntity<Room> resp = controller.getRoom(1L);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isEqualTo(room);
    }

    @Test
    void createRoom_returnsCreated() {
        given(roomService.createRoom(room)).willReturn(room);

        ResponseEntity<Room> resp = controller.createRoom(room);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(resp.getBody()).isEqualTo(room);
    }

    @Test
    void updateRoom_returnsOk() {
        given(roomService.updateRoom(1L, room)).willReturn(room);

        ResponseEntity<Room> resp = controller.updateRoom(1L, room);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isEqualTo(room);
    }

    @Test
    void deleteRoom_returnsNoContent() {
        willDoNothing().given(roomService).deleteRoom(1L);

        ResponseEntity<Void> resp = controller.deleteRoom(1L);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(resp.getBody()).isNull();
    }
}
