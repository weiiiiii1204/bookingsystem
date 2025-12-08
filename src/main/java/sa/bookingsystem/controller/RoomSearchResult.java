package sa.bookingsystem.controller;

import sa.bookingsystem.model.Room;
import lombok.Data;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
public class RoomSearchResult {
    private List<Room> availableRooms;
    private List<Room> soldOutRooms;
}