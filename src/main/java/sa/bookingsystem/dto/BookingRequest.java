package sa.bookingsystem.dto;


import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class BookingRequest {

    private List<String> roomIDs;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String paymentDetails;
}