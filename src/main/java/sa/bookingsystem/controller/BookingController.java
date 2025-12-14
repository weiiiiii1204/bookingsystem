package sa.bookingsystem.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sa.bookingsystem.dto.BookingRequest;
import sa.bookingsystem.dto.RoomSearchResult;
import sa.bookingsystem.model.Reservation;
import sa.bookingsystem.service.BookingSystem;

@RestController
@RequestMapping("/api/booking")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingSystem bookingSystem;

    @GetMapping("/rooms")
    public RoomSearchResult searchRooms(
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut) {
        return bookingSystem.searchAvailableRooms(checkIn, checkOut);
    }


    @PostMapping("/reserve")
    public List<Reservation> createReservation(@RequestBody BookingRequest request) {
    return bookingSystem.createReservation(request);
}
}