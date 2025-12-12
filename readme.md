# 飯店訂房系統 (Hotel Booking System)

## 專案簡介
這是一個基於 Spring Boot 開發的飯店預訂系統原型，提供使用者根據自己所提出的日期區間來預訂房間。
。
## 🛠 技術堆疊

後端框架: Spring Boot 3.5.8

語言: Java 21

建置工具: Maven

前端介面: HTML5, Bootstrap 5, Vanilla JavaScript (Fetch API)

工具庫: Lombok (簡化程式碼)

## 🚀 快速開始 

### 1. 環境需求

在開始之前，請確保你的電腦已安裝：

**Java JDK 17** 或更高版本。

**IDE: 推薦使用 VS Code**(需安裝 Extension Pack for Java)。

**Git** (用於版本控制)。

### 2. 下載專案
開啟終端機 (Terminal)，執行以下指令將專案 Clone 至本地：
```bash
git clone https://github.com/weiiiiii1204/bookingsystem.git

cd bookingsystem
```

### 3. 開發環境設定 (VS Code)

開啟 VS Code，選擇 File -> Open Folder，開啟 bookingsystem 資料夾。

等待 VS Code 右下角載入 Java 專案。

### 4. 啟動專案

找到 src/main/java/sa/bookingsystem/BookingsystemApplication.java。

點擊程式碼上方的 **Run | Debug**按鈕。

當 Terminal 出現 Tomcat started on port 8080，代表啟動成功。

### 5. 開啟前端頁面

啟動後，打開瀏覽器訪問：
👉 http://localhost:8080/index.html

## 📂 專案結構 (Project Structure)
```bash
src/main/java/sa/bookingsystem/
├── controller/               # API 接口層 (接收前端 Fetch 請求)
│   └── BookingController.java
├── dto/                      # 資料傳輸物件 (API request/response 格式)
│   ├── BookingRequest.java
│   └── RoomSearchResult.java
├── model/                    # 資料模型 (Room, Reservation, Customer, Payment)
│   ├── Room.java            
│   ├── Reservation.java
│   ├── Customer.java
│   └── Payment.java
├── service/                  # 業務邏輯層 (處理複雜運算、判斷)
│   └── BookingSystem.java
└── BookingsystemApplication.java  # 程式進入點

src/main/resources/
├── static/                   # 靜態資源 (圖片/HTML/CSS/JS)
│   ├── images
│   ├── index.html
│   ├── style.css
│   └── script.js             
└── application.properties    # 設定檔 
```

## 📡 API 文件 (API Documentation)

本系統提供 RESTful API 供前端呼叫。

### 1. 查詢空房 (Search Rooms)

URL: GET /api/booking/rooms

描述: 根據日期區間搜尋可用的房間。

參數:

checkIn: 入住日期 (YYYY-MM-DD)

checkOut: 退房日期 (YYYY-MM-DD)

範例: /api/booking/rooms?checkIn=2023-12-01&checkOut=2023-12-05

### 2. 預訂房間 (Reserve Room)

URL: POST /api/booking/reserve

描述: 建立新的訂單。
```bash
Body (JSON):

{
  "roomId": "R101",
  "customer": {
    "name": "測試員",
    "phone": "0912345678",
    "email": "test@example.com"
  },
  "checkIn": "2023-12-01",
  "checkOut": "2023-12-05"
}
```

## 🤝 協作指南 (Contribution Guide)

為了讓團隊合作順利，請遵守以下規則：

資料重置注意：

因為是 In-Memory 架構，每次重新啟動後端，所有新訂單都會消失，只會保留 MockDataStore.java 裡面寫死的假資料。這是正常現象。

分工與修改：

後端組 (A/B): 修改 Java 程式碼後，必須點擊紅色的 Stop 按鈕並重新 Run，程式碼才會生效。

前端組 (C/D): 修改 index.html 後，只需重新整理瀏覽器即可 (若沒變更，請嘗試 Ctrl+F5 強制重新整理)。

Lombok 錯誤排除：

如果你發現 getRoomId() 或 setPrice() 報錯紅字，代表你的 IDE 沒有啟用 Annotation Processing。

VS Code: 只要安裝 "Extension Pack for Java" 通常會自動處理。

IntelliJ: 設定 -> Build, Execution, Deployment -> Compiler -> Annotation Processors -> 勾選 Enable annotation processing。

## 🐛 常見問題排除 (Troubleshooting)

Port 8080 already in use:

原因：你可能已經開啟了一個執行視窗，卻又嘗試開第二個。

解法：去 Terminal 把舊的 Process 殺掉，或是關閉所有 Java 視窗重開。

找不到 sa.bookingsystem 套件:

解法：請確認你的資料夾結構是否正確，必須是 src/main/java/sa/bookingsystem/...。

