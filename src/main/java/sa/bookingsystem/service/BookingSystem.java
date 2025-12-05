package sa.bookingsystem.service;

import sa.bookingsystem.model.*;
import sa.bookingsystem.repository.MockDataStore;
import sa.bookingsystem.controller.RoomSearchResult;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingSystem {

    /**
     * 功能: 搜尋並分類房間 (可用 vs 已預訂)
     */
    public RoomSearchResult searchAvailableRooms(LocalDate start, LocalDate end) {
        // 1. 找出所有可用房間
        List<Room> available = MockDataStore.rooms.stream()
                .filter(room -> "AVAILABLE".equalsIgnoreCase(room.getStatus()))
                .collect(Collectors.toList());

        // 2. 找出所有已預訂房間
        List<Room> booked = MockDataStore.rooms.stream()
                .filter(room -> !"AVAILABLE".equalsIgnoreCase(room.getStatus()))
                .collect(Collectors.toList());

        return new RoomSearchResult(available, booked);
    }

    /**
     * 功能: 計算金額
     */
    public double calculateTotalAmount(Room room, LocalDate start, LocalDate end) {
        long days = ChronoUnit.DAYS.between(start, end);
        if (days <= 0) days = 1;
        return room.getPrice() * days;
    }

    /**
     * 功能: 批次建立訂單
     */
    public List<Reservation> createReservation(List<String> roomIds, Customer customer, LocalDate checkIn, LocalDate checkOut) {
        List<Reservation> createdReservations = new ArrayList<>();
        List<Room> selectedRooms = new ArrayList<>();
        double grandTotalAmount = 0.0;

        // 1. 驗證所有房間並計算總金額
        for (String roomId : roomIds) {
            Room targetRoom = MockDataStore.rooms.stream()
                    .filter(r -> r.getRoomID().equals(roomId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("找不到房間 ID: " + roomId));

            selectedRooms.add(targetRoom);
            grandTotalAmount += calculateTotalAmount(targetRoom, checkIn, checkOut);
        }

        // 2. 處理付款 (一次付清)
        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.processPayment(grandTotalAmount, "CREDIT_CARD");
        
        if (!payment.returnResult()) {
            throw new RuntimeException("付款失敗");
        }

        // 3. 逐一建立訂單
        for (Room room : selectedRooms) {
            Reservation reservation = new Reservation();
            String resId = UUID.randomUUID().toString();
            double roomAmount = calculateTotalAmount(room, checkIn, checkOut);

            reservation.saveDetails(resId, room, customer, checkIn, checkOut, roomAmount);
            reservation.setPayment(payment); // 共用同一個付款紀錄
            reservation.updatePaymentStatus("CONFIRMED");

            room.updateStatus("BOOKED"); // 更新庫存
            
            MockDataStore.reservations.add(reservation);
            
            sendConfirmation(resId);
            createdReservations.add(reservation);
        }

        return createdReservations;
    }

    public void sendConfirmation(String reservationId) {
        System.out.println(">>> System: 訂單確認信已發送，訂單編號: " + reservationId);
    }
}