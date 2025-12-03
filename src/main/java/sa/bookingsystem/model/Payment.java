package sa.bookingsystem.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    private String paymentId;
    private double amount;
    private String method;
    private boolean isSuccessful;

    public void processPayment(double amount, String paymentDetails) {
        this.amount = amount;
        this.method = "CREDIT_CARD";
        
        //假設隨機30%的機率會失敗
        if (Math.random() < 0.3) {
            this.isSuccessful = false;
            return; 
        }
   
        if (amount > 0) {
            this.isSuccessful = true;
        } else {
            this.isSuccessful = false;
        }
    }

    public boolean returnResult() {
        return this.isSuccessful;
    }
}