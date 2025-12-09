package sa.bookingsystem.controller;

import sa.bookingsystem.model.Customer;
import sa.bookingsystem.model.Reservation;
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

    // 修改: 回傳型態改為 RoomSearchResult
    @GetMapping("/rooms")
    public RoomSearchResult searchRooms(
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut) {
        return bookingSystem.searchAvailableRooms(checkIn, checkOut);
    }

    private static int CustomerIDCounter = 0;

    // 修改: 接收 roomIDs 並回傳 List<Reservation>
    @PostMapping("/reserve")
    public List<Reservation> createReservation(@RequestBody BookingRequest request) {
        String newCustomerID = String.valueOf(++CustomerIDCounter);
        Customer customer = new Customer(newCustomerID, request.getCustomerName(),
                request.getCustomerPhone(), request.getCustomerEmail());

        return bookingSystem.createReservation(
                request.getRoomIDs(),
                customer,
                request.getCheckIn(),
                request.getCheckOut(),
                request.getPaymentDetails()
        );
    }
}