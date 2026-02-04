async function loadReservationCards() {
    try {
        console.log("=== 예약 내역 카드 로드 ===");

        const res = await fetch('/api/reservations/my');
        const data = await res.json();

        const container = document.getElementById('reservationCardsContainer');
        if (!container) return;

        container.innerHTML = '';

        if (!data.success || !data.reservations || data.reservations.length === 0) {
            container.innerHTML =
                '<p class="text-center text-muted">예약 내역이 없습니다.</p>';
            return;
        }

        data.reservations.forEach(resv => {
            const statusBadge =
                resv.status === '예약완료'
                    ? '<span class="badge bg-success">예약 완료</span>'
                    : resv.status === '취소'
                        ? '<span class="badge bg-danger">취소됨</span>'
                        : '<span class="badge bg-secondary">' + resv.status + '</span>';

            const card = document.createElement('div');
            card.className = 'reservation-card';

            card.innerHTML = `
                <div class="reservation-header">
                    <span class="reservation-no">예약번호 #${resv.resNo}</span>
                    ${statusBadge}
                </div>

                <div class="reservation-body">
                    <div class="reservation-pet">
                        🐶 <strong>${resv.petName}</strong>
                    </div>

                    <div class="reservation-date">
                        <span>체크인</span>
                        <strong>${resv.checkin}</strong>
                    </div>

                    <div class="reservation-date">
                        <span>체크아웃</span>
                        <strong>${resv.checkout}</strong>
                    </div>
                </div>

                <div class="reservation-footer">
                    <button class="btn btn-outline-primary btn-sm"
                        onclick="openEditModal(${resv.resNo})"
                        ${resv.status !== '예약완료' ? 'disabled' : ''}>
                        수정
                    </button>
                </div>
            `;

            container.appendChild(card);
        });

    } catch (err) {
        console.error(err);
        alert('예약 내역을 불러오지 못했습니다.');
    }
}

document.addEventListener('DOMContentLoaded',() => {
    loadReservationCards();
});