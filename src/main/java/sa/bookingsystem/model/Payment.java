package sa.bookingsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    private String paymentID;
    private double amount;
    private String method;
    private boolean isSuccessful;

    private static int PaymentIDCounter = 0;


    public void processPayment(double amount, String paymentDetails) {
        this.amount = amount;
        this.method = "Credit Card";
        //假設隨機50%的機率會付款失敗
        this.isSuccessful = (amount > 0);
        if (Math.random() < 0.5) {
            this.isSuccessful = false;
        }
        if (this.isSuccessful) {
            this.paymentID = String.valueOf(++PaymentIDCounter);
        }
    }
}