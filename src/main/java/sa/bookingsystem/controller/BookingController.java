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

    // 修改: 接收 roomIds 並回傳 List<Reservation>
    @PostMapping("/reserve")
    public List<Reservation> createReservation(@RequestBody BookingRequest request) {
        // 直接呼叫 Service，完全沒有 Customer 的行為方法呼叫
        Customer customer = request.getCustomer();
        
        customer.provideDetails(customer.getName(), customer.getPhone(), customer.getEmail());

        return bookingSystem.createReservation(
                request.getRoomIds(),
                request.getCustomer(),
                request.getCheckIn(),
                request.getCheckOut()
        );
    }
}