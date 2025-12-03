package sa.bookingsystem.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    private String reservationId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalAmount;
    private String status;    
    
    private Room room;
    private Customer customer;
    private Payment payment;

  
    public void saveDetails(String id, Room room, Customer customer, LocalDate checkIn, LocalDate checkOut, double amount) {
        this.reservationId = id;
        this.room = room;
        this.customer = customer;
        this.checkInDate = checkIn;
        this.checkOutDate = checkOut;
        this.totalAmount = amount;
        this.status = "PENDING";
    }


    public void updatePaymentStatus(String status) {
        this.status = status;
    }
}