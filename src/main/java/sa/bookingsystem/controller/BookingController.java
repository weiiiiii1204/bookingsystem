package sa.bookingsystem.controller;

import sa.bookingsystem.model.Reservation;
import sa.bookingsystem.model.Room;
import sa.bookingsystem.service.BookingSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/booking")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingSystem bookingSystem;

    @GetMapping("/rooms")
    public List<Room> searchRooms(
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut) {
        return bookingSystem.searchAvailableRooms(checkIn, checkOut);
    }

    @PostMapping("/reserve")
    public Reservation createReservation(@RequestBody BookingRequest request) {
        return bookingSystem.createReservation(
                request.getRoomId(),
                request.getCustomer(),
                request.getCheckIn(),
                request.getCheckOut()
        );
    }
}