package sa.bookingsystem.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    private String roomID;
    private String type;
    private double price;
    private String status;

     public boolean checkAvailability(LocalDate start, LocalDate end) {
        return "AVAILABLE".equalsIgnoreCase(this.status);
    }

   
    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }
}