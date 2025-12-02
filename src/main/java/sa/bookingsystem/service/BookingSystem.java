package sa.bookingsystem.service;

import sa.bookingsystem.model.*;
import sa.bookingsystem.repository.MockDataStore;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BookingSystem {

    public List<Room> searchAvailableRooms(LocalDate start, LocalDate end) {
        List<Room> result = new ArrayList<>();
        for (Room room : MockDataStore.rooms) {
            if ("AVAILABLE".equals(room.getStatus())) {
                result.add(room);
            }
        }
        return result;
    }

    public double calculateTotalAmount(Room room, LocalDate start, LocalDate end) {
        long days = ChronoUnit.DAYS.between(start, end);
        if (days <= 0) days = 1;
        return room.getPrice() * days;
    }

    public Reservation createReservation(String roomId, Customer customer, LocalDate checkIn, LocalDate checkOut) {
        Room targetRoom = MockDataStore.rooms.stream()
                .filter(r -> r.getRoomId().equals(roomId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("找不到房間 ID: " + roomId));

        double totalAmount = calculateTotalAmount(targetRoom, checkIn, checkOut);

        Reservation reservation = new Reservation();
        reservation.setReservationId(UUID.randomUUID().toString());
        reservation.setRoom(targetRoom);
        reservation.setCustomer(customer);
        reservation.setCheckInDate(checkIn);
        reservation.setCheckOutDate(checkOut);
        reservation.setTotalAmount(totalAmount);
        reservation.setStatus("CONFIRMED");
        
        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setAmount(totalAmount);
        payment.setMethod("CREDIT_CARD");
        payment.setSuccessful(true);
        reservation.setPayment(payment);

        targetRoom.setStatus("BOOKED");
        MockDataStore.reservations.add(reservation);

        sendConfirmation(reservation.getReservationId());
        return reservation;
    }

    public void sendConfirmation(String reservationId) {
        System.out.println(">>> 訂單確認信已發送，訂單編號: " + reservationId);
    }
}