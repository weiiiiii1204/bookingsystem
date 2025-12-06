package sa.bookingsystem.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    private String reservationID;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalAmount;
    private String status;    
    
    private Room room;
    private Customer customer;
    private Payment payment;

  
    public void saveDetails(String reservationID, Room room, Customer customer, LocalDate checkInDate, LocalDate checkOutDate, double totalAmount) {
        this.reservationID = reservationID;
        this.room = room;
        this.customer = customer;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalAmount = totalAmount;
        this.status = "COMFIRMED";
    }


    public void updatePaymentStatus(String status) {
        this.status = status;
    }
}