package sa.bookingsystem.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import sa.bookingsystem.dto.RoomSearchResult;
import sa.bookingsystem.model.Customer;
import sa.bookingsystem.model.Payment;
import sa.bookingsystem.model.Reservation;
import sa.bookingsystem.model.Room;

@Service
public class BookingSystem {
    private final List<Room> allRooms = new ArrayList<>();
    private final List<Reservation> allReservations = new ArrayList<>();
    private static int reservationIDCounter = 0;
    private static int customerIDCounter = 0;
    @PostConstruct
    public void initData() {
        // 初始化假資料
        allRooms.add(new Room("101", "標準單人房", 2000));
        allRooms.add(new Room("102", "標準雙人房", 4000));
        allRooms.add(new Room("103", "標準單人房", 2000));
        allRooms.add(new Room("104", "標準雙人房", 4000));
        allRooms.add(new Room("201", "標準四人房", 7500));
        allRooms.add(new Room("202", "標準四人房", 7500));
        allRooms.add(new Room("203", "標準四人房", 7500));
        allRooms.add(new Room("301", "總統套房", 10000));
        allRooms.add(new Room("302", "總統套房", 10000));
        allRooms.add(new Room("303", "總統套房", 10000));
    }

    public RoomSearchResult searchAvailableRooms(LocalDate start, LocalDate end) {

        List<Room> available = new ArrayList<>();
        
        // 1. 遍歷每一間房間
        for (Room room : allRooms) {
            boolean isConflict = false;
            for (Reservation res : allReservations) {
                // 如果訂單是這間房，且時間重疊
                if (res.getRoom().getRoomID().equals(room.getRoomID())) {
                    if (start.isBefore(res.getCheckOutDate()) && end.isAfter(res.getCheckInDate())) {
                        isConflict = true; 
                        break;
                    }
                }
            }

            if (!isConflict) {
                available.add(room);
            }
        }
        List<Room> soldOutRepresentatives = new ArrayList<>();
        Set<String> availableTypes = new HashSet<>();
        for (Room r : available) availableTypes.add(r.getType());

        Set<String> allTypes = new HashSet<>();
        for (Room r : allRooms) allTypes.add(r.getType());

        for (String type : allTypes) {
            if (!availableTypes.contains(type)) {
                for (Room r : allRooms) {
                    if (r.getType().equals(type)) {
                        Room representative = new Room("SOLD_OUT", r.getType(), r.getPrice());
                        soldOutRepresentatives.add(representative);
                        break; 
                    }
                }
            }
        }

        return new RoomSearchResult(available,soldOutRepresentatives);
    }

    public double calculateTotalAmount(Room room, LocalDate start, LocalDate end) {
        long days = ChronoUnit.DAYS.between(start, end);
        return room.getPrice() * days;
    }
  
    public List<Reservation> createReservation(List<String> roomIDs, Customer customer, LocalDate start, LocalDate end,String paymentDetails) {
        customer.setCustomerID(String.valueOf(++customerIDCounter));
        List<Reservation> createdReservations = new ArrayList<>();
        List<Room> targetRooms = new ArrayList<>();

        Map<Room, Double> roomPrices = new HashMap<>();

        double grandTotalAmount = 0.0;
        // 1. 驗證房間可用性並計算總金額
        for (String roomID : roomIDs) {
            Room foundRoom = null;
            for (Room room : allRooms) {
                if (room.getRoomID().equals(roomID)) {
                    foundRoom = room;
                    break;
                }
            }
            if (foundRoom == null) throw new RuntimeException("找不到房間 ID: " + roomID);

            boolean isConflict = false;
            for (Reservation res : allReservations) {
                if (res.getRoom().getRoomID().equals(roomID)) {
                    if (start.isBefore(res.getCheckOutDate()) && end.isAfter(res.getCheckInDate())) {
                        isConflict = true;
                        break;
                    }
                }
            }
            if (isConflict) throw new RuntimeException("房間 " + roomID + " 在此時段已被預訂");
            
            targetRooms.add(foundRoom);
            double singleRoomTotal = calculateTotalAmount(foundRoom, start, end);
            roomPrices.put(foundRoom, singleRoomTotal);
            grandTotalAmount += singleRoomTotal;
        }

        // 2. 處理付款
        Payment payment = new Payment();

        payment.processPayment(grandTotalAmount,paymentDetails);
        
        if (!payment.isSuccessful()) {
            throw new RuntimeException("付款失敗，請重新輸入付款資訊");
        }
        List<String> confirmedIDs = new ArrayList<>();

        // 3. 逐一建立訂單
        for (Room room : targetRooms) {
            String reservationID = String.valueOf(++reservationIDCounter);
            double totalAmount = roomPrices.get(room);
            Reservation reservation = new Reservation(
                reservationID,
                start,
                end,
                totalAmount,
                "CONFIRMED",
                room,
                customer,
                payment
            );
            
            allReservations.add(reservation);
            confirmedIDs.add(reservationID);
            createdReservations.add(reservation);
            System.out.println("【系統日誌】已存入訂單: " + reservation);
        }
        sendConfirmation(confirmedIDs);
        return createdReservations;
    }

    public void sendConfirmation(List<String> reservationIDs) {
        System.out.println("您的訂房已確認！包含以下訂單: " + reservationIDs);
    }
}