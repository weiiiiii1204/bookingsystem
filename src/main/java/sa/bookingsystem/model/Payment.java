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
        
        // 模擬驗證與扣款：金額 > 0 即視為成功
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