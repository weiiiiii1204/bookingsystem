package sa.bookingsystem.model;

import lombok.Data;
import java.time.LocalDate;
@Data
public class Reservation {
    private String reservationId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalAmount;
    private String status;    
    
    private Room room;
    private Customer customer;
    private Payment payment;
}