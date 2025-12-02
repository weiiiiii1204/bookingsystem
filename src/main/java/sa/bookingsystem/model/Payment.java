package sa.bookingsystem.model;

import lombok.Data;

@Data
public class Payment {
    private String paymentId;
    private double amount;
    private String method;
    private boolean isSuccessful;
}
