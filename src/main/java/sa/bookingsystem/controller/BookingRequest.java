package sa.bookingsystem.controller;

import sa.bookingsystem.model.Customer;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class BookingRequest {

    private List<String> roomIDs;
    private Customer customer;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String paymentDetails;
}