package sa.bookingsystem.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    private String customerId;
    private String name;
    private String phone;
    private String email;

    public void provideDetails(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
}