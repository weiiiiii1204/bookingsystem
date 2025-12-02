package sa.bookingsystem.controller;

import sa.bookingsystem.model.Customer;
import lombok.Data;
import java.time.LocalDate;

@Data
public class BookingRequest {
    private String roomId;
    private Customer customer;
    private LocalDate checkIn;
    private LocalDate checkOut;
}