package sa.bookingsystem.repository;

import sa.bookingsystem.model.Room;
import sa.bookingsystem.model.Reservation;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class MockDataStore {
    
    public static List<Room> rooms = new ArrayList<>();
    public static List<Reservation> reservations = new ArrayList<>();

    @PostConstruct
    public void initData() {
        rooms.add(new Room("101", "標準單人房", 2000, "AVAILABLE"));
        rooms.add(new Room("102", "豪華雙人房", 3500, "AVAILABLE"));
        rooms.add(new Room("103", "海景四人房", 5000, "AVAILABLE"));
        rooms.add(new Room("201", "總統套房", 8800, "AVAILABLE"));
        rooms.add(new Room("202", "經濟雙人房", 2500, "BOOKED")); // 故意設一間被訂走的，測試用
    }
}