package sa.bookingsystem.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    private String paymentID;
    private double amount;
    private String method;
    private boolean isSuccessful;

    private static int idCounter = 0;


    public void processPayment(double amount, String paymentDetails) {

        this.paymentID = String.valueOf(++idCounter);
        this.amount = amount;
        
        //假設隨機50%的機率會失敗
        if (Math.random() < 0.5) {
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