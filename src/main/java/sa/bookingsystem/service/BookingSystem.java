package sa.bookingsystem.service;

import sa.bookingsystem.model.*;
import sa.bookingsystem.controller.RoomSearchResult;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


@Service
public class BookingSystem {


    private List<Room> allRooms = new ArrayList<>();
    private List<Reservation> allReservations = new ArrayList<>();

    @PostConstruct
    public void initData() {
        // 初始化假資料
        allRooms.add(new Room("101", "標準單人房", 2000, "AVAILABLE"));
        allRooms.add(new Room("102", "豪華雙人房", 3500, "AVAILABLE"));
        allRooms.add(new Room("103", "海景四人房", 5000, "AVAILABLE"));
        allRooms.add(new Room("201", "總統套房", 8800, "AVAILABLE"));
        allRooms.add(new Room("202", "經濟雙人房", 2500, "BOOKED")); // 測試用
    }
    private static int reservationIDCounter = 0;

    public RoomSearchResult searchAvailableRooms(LocalDate start, LocalDate end) {

        List<Room> available = new ArrayList<>();
        for (Room room : allRooms) {
            if (room.checkAvailability(start, end)) {
                available.add(room);
            }
        }

        List<Room> booked = new ArrayList<>();
        for (Room room : allRooms) {
            if (!room.checkAvailability(start, end)) {
                booked.add(room);
            }
        }

        return new RoomSearchResult(available, booked);
    }

    public double calculateTotalAmount(Room room, LocalDate start, LocalDate end) {
        long days = ChronoUnit.DAYS.between(start, end);
        return room.getPrice() * days;
    }

  
    public List<Reservation> createReservation(List<String> roomIDs, Customer customer, LocalDate start, LocalDate end,String paymentDetails) {
        List<Reservation> createdReservations = new ArrayList<>();
        List<Room> targetRooms = new ArrayList<>();
        double grandTotalAmount = 0.0;

        // 第一步：驗證所有房間是否存在且可用，並計算總金額
        for (String roomID : roomIDs) {
            Room foundRoom = null;
            for (Room room : allRooms) {
                if (room.getRoomID().equals(roomID)) {
                    foundRoom = room;
                    break;
                }
            }

            if (foundRoom == null) {
                throw new RuntimeException("找不到房間 ID: " + roomID);
            }
            

            if (!"AVAILABLE".equals(foundRoom.getStatus())) {
                 throw new RuntimeException("房間 " + roomID + " 已被預訂");
            }

            targetRooms.add(foundRoom);
            grandTotalAmount += calculateTotalAmount(foundRoom,start,end);
        }

        // 2. 處理付款 (一次付清)
        Payment payment = new Payment();

        payment.processPayment(grandTotalAmount,paymentDetails);
        
        if (!payment.returnResult()) {
            throw new RuntimeException("付款失敗");
        }

        // 3. 逐一建立訂單
        for (Room room : targetRooms) {
            Reservation reservation = new Reservation();
            String reservationID = String.valueOf(++reservationIDCounter);
            double totalAmount = calculateTotalAmount(room, start, end);

            reservation.saveDetails(reservationID, room, customer, start, end, totalAmount);
            reservation.setPayment(payment); // 共用同一個付款紀錄
            reservation.updatePaymentStatus("CONFIRMED");

            room.updateStatus("BOOKED"); // 更新庫存
            
            allReservations.add(reservation);
            
            sendConfirmation(reservationID);
            createdReservations.add(reservation);
        }

        return createdReservations;
    }

    public void sendConfirmation(String reservationID) {
        System.out.println(">>> System: 訂單確認信已發送，訂單編號: " + reservationID);
    }
}