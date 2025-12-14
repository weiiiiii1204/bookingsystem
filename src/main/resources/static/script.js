const API_URL = 'http://localhost:8080/api/booking';
const $ = (sel) => document.querySelector(sel); 
const showMsg = (icon, title, text, btnText = '確定') => 
    Swal.fire({ icon, title, text, confirmButtonColor: '#222', confirmButtonText: btnText });

let state = { checkIn: null, checkOut: null, nights: 0, roomGroups: {}, selections: {} };
const bookingModal = new bootstrap.Modal($('#bookingModal'));

flatpickr("#dateRange", { mode: "range", minDate: "today", dateFormat: "Y-m-d", showMonths: 1 });

//搜尋與資料處理
$('#searchForm').addEventListener('submit', function(e) {
    e.preventDefault();
    const dateStr = $('#dateRange').value;
    if (!dateStr.includes(" to ")) return showMsg('warning', '日期未選擇', '請選擇有效的入住與退房日期範圍');

    const [start, end] = dateStr.split(" to ");
    Object.assign(state, { checkIn: start, checkOut: end, nights: Math.max(1, Math.ceil((new Date(end) - new Date(start)) / 86400000)) });

    fetch(`${API_URL}/rooms?checkIn=${start}&checkOut=${end}`)
        .then(res => res.json())
        .then(({ availableRooms = [], soldOutRooms = [] }) => {
            state.roomGroups = {}; state.selections = {};
            const process = (list, isSoldOut) => list.forEach(r => {
                if (!state.roomGroups[r.type]) state.roomGroups[r.type] = { price: r.price, rooms: [], isSoldOut };
                state.roomGroups[r.type].rooms.push(r);
            });
            process(availableRooms, false);
            process(soldOutRooms, true);
            updateUI();
        })
        .catch(err => { console.error(err); showMsg('error', '連線錯誤', '系統暫時無法回應，請稍後再試。'); });
});

function updateUI() {
    const container = $('#room-list-container');
    const types = Object.keys(state.roomGroups);
    if (!types.length) return container.innerHTML = '<div class="col-12 text-center py-5">No rooms available.</div>';

    container.innerHTML = types.map(type => {
        const g = state.roomGroups[type];
        const count = g.rooms.length;
        const isSold = g.isSoldOut || count === 0;
        const img = {'標準單人房':'single.jpg','標準雙人房':'double.jpg','標準四人房':'family.png','總統套房':'fancy.jpg'}[type] || '1.jpg';
        
        let opts = isSold ? '<option>Sold Out</option>' : `<option value="0">選擇數量</option>` + 
                   Array.from({length: count}, (_, i) => `<option value="${i+1}">${i+1}</option>`).join('');

        return `
        <div class="col-lg-4 col-md-6"><div class="room-card ${isSold ? 'sold-out' : ''}">
            <div class="room-img-wrapper"><img src="/images/${img}" class="room-img" alt="${type}"></div>
            <div class="room-content">
                <div class="d-flex justify-content-between align-items-baseline mb-2">
                    <h5 class="room-title">${type}</h5><span class="room-price-tag">NT$${g.price}</span>
                </div>
                <ul class="amenities-list"><li><i class="bi bi-wifi"></i> Wifi</li><li><i class="bi bi-cup-hot"></i> 早餐</li></ul>
                <div class="mt-3"><label class="small text-muted mb-1 fw-bold">剩餘數量 : ${count}</label>
                    <select class="room-quantity-select" ${isSold?'disabled':''} onchange="updateSelection('${type}', this.value)">${opts}</select>
                </div>
            </div>
        </div></div>`;
    }).join('');
    
    $('#room-section').style.display = 'block';
    $('#room-section').scrollIntoView({ behavior: 'smooth' });
    updateCheckoutBar();
}

// 互動邏輯
window.updateSelection = (type, count) => {
    (parseInt(count) > 0) ? state.selections[type] = parseInt(count) : delete state.selections[type];
    updateCheckoutBar();
};

function updateCheckoutBar() {
    let count = 0, total = 0;
    Object.entries(state.selections).forEach(([t, q]) => {
        if(state.roomGroups[t]) { count += q; total += state.roomGroups[t].price * q * state.nights; }
    });
    
    const bar = $('#checkout-bar');
    bar.style.display = count > 0 ? 'flex' : 'none';
    $('#bar-count').innerText = count;
    $('#bar-total').innerText = `NT$${total}`;
}

//  Modal 與步驟 
window.openBookingModal = () => {
    $('#modal-dates').innerText = `${state.checkIn} — ${state.checkOut} (共 ${state.nights} 晚)`;
    let total = 0, html = '';
    
    Object.entries(state.selections).forEach(([t, q]) => {
        const sub = state.roomGroups[t].price * q * state.nights;
        total += sub;
        html += `<div class="d-flex justify-content-between border-bottom py-2"><span>${t} x ${q}</span><span>NT$${sub}</span></div>`;
    });

    $('#modal-room-list').innerHTML = html;
    $('#modal-price').innerText = `NT$${total}`;
    
    // 重置 Modal 狀態
    goToStep(1);
    $('#payment-error').style.display = 'none';
    $('#payment-processing').style.display = 'none';
    const btn = $('#btn-submit'); btn.disabled = false; btn.innerText = '送出付款資訊';
    bookingModal.show();
};

window.closeModal = () => bookingModal.hide();
window.goToStep1 = () => goToStep(1);
window.goToStep2 = () => goToStep(2);

function goToStep(step) {
    if (step === 2) {
        if (['customerName', 'customerPhone', 'customerEmail'].some(id => !$(`#${id}`).value)) 
            return showMsg('warning', '資料不完整', '請填寫所有旅客資料');
    }
    $('#step-1-content').style.display = step === 1 ? 'block' : 'none';
    $('#step-2-content').style.display = step === 2 ? 'block' : 'none';
    $('#step-indicator-1').classList.toggle('active', step === 1);
    $('#step-indicator-2').classList.toggle('active', step === 2);
}

//信用卡格式
$('#cardNumber').oninput = e => e.target.value = e.target.value.replace(/\D/g, '').replace(/(.{4})/g, '$1 ').trim();
$('#cardExpiry').oninput = e => {
    let v = e.target.value.replace(/\D/g, '');
    if (v.length >= 2) v = v.slice(0, 2) + '/' + v.slice(2);
    e.target.value = v;
};
$('#cardCvv').oninput = e => e.target.value = e.target.value.replace(/\D/g, '');

// 送出訂單
$('#bookingForm').onsubmit = (e) => {
    e.preventDefault();
    const [num, exp, cvv] = ['#cardNumber', '#cardExpiry', '#cardCvv'].map(id => $(id).value.trim());
    if (!num || !exp || !cvv) return showMsg('warning', '付款資訊不完整', '請填寫完整的信用卡號、到期日與安全碼');

    const ui = { bar: $('#payment-processing'), inner: $('#progress-bar-inner'), text: $('#process-text'), percent: $('#process-percent'), btn: $('#btn-submit') };
    ui.bar.style.display = 'block'; $('#payment-error').style.display = 'none'; ui.btn.disabled = true;

    let width = 0;
    const timer = setInterval(() => {
        width++;
        
        ui.text.innerText = width > 98 ? '驗證完成' : '驗證資料中...';
        ui.inner.style.width = width + '%';
        ui.percent.innerText = width + '%';

        if (width >= 100) {
            clearInterval(timer);
            executeBooking(num, ui);
        }
    }, 30);
};

function executeBooking(cardNum, ui) {
    const roomIDs = Object.entries(state.selections).flatMap(([t, q]) => state.roomGroups[t].rooms.slice(0, q).map(r => r.roomID));
    
    fetch(`${API_URL}/reserve`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            roomIDs, checkIn: state.checkIn, checkOut: state.checkOut, paymentDetails: cardNum,
            customerName: $('#customerName').value, customerPhone: $('#customerPhone').value, customerEmail: $('#customerEmail').value
        })
    })
    .then(res => res.ok ? res.text() : res.text().then(t => { throw new Error(JSON.parse(t).message || t) }))
    .then(() => setTimeout(() => showMsg('success', '預訂成功！', '確認信已發送至您的信箱。').then(r => r.isConfirmed && location.reload()), 1500))
    .catch(err => {
        console.error("Booking Error:", err);
        setTimeout(() => {
            ui.bar.style.display = 'none'; ui.btn.disabled = false; ui.btn.innerText = '再次送出付款資訊';
            showMsg('error', '付款失敗', '付款資訊有誤，請重新輸入付款資訊', '重新輸入');
        }, 300);
    });
}