// CSRF 토큰 (전역에서 한 번만 선언)
const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

let myInfo = null;

/* 마이 페이지 전체 초기화 */
document.addEventListener("DOMContentLoaded", async () => {
    console.log("=== mypage.js DOMContentLoaded 실행 ===");
    try {
        // 사용자 정보 조회
        const response = await fetch('/api/users/select', {
            headers: { 'Content-Type': 'application/json' }
        });

        console.log("사용자 정보 API 응답 상태:", response.status);

        if (!response.ok) {
            const errorText = await response.text();
            console.error('사용자 정보 API 에러:', response.status, errorText);
            throw new Error('정보 조회 실패');
        }

        const data = await response.json();
        myInfo = data;

        console.log("로그인 사용자 정보:", data);

        // 화면에 사용자 정보 표시
        const usernameEl = document.getElementById('username');
        const nameEl = document.getElementById('name');
        const emailEl = document.getElementById('email');
        const phoneEl = document.getElementById('phone');
        const birthEl = document.getElementById('birth');
        const addressEl = document.getElementById('address');
        const detailAddressEl = document.getElementById('detailAddress');

        if (usernameEl) usernameEl.textContent = data.username;
        if (nameEl) nameEl.textContent = data.name;
        if (emailEl) emailEl.textContent = data.email;
        if (phoneEl) phoneEl.textContent = data.phone;
        if (birthEl) birthEl.textContent = data.birth;
        if (addressEl) addressEl.textContent = data.address;
        if (detailAddressEl) detailAddressEl.textContent = data.detailAddress;

        console.log("사용자 정보 화면 업데이트 완료");
    } catch (error) {
        console.error('=== 사용자 정보 로드 실패 ===:', error);
        alert('정보를 불러오는 중 오류 발생. 로그인 상태를 확인하세요.');
    }
});

/* 모달 열릴 때 input 자동 세팅 */
document.addEventListener('show.bs.modal', function (e) {
    if (e.target.id === 'editModal') {
        if (myInfo) {
            document.getElementById('editUsername').value = myInfo.username;
            document.getElementById('editName').value = myInfo.name;
            document.getElementById('editEmail').value = myInfo.email;
            document.getElementById('editPhone').value = myInfo.phone;
            document.getElementById('editBirth').value = myInfo.birth;
            document.getElementById('editAddress').value = myInfo.address;
            document.getElementById('editDetailAddress').value = myInfo.detailAddress;
        }
    }
}, true);

/* 수정 버튼 클릭 → 비동기 업데이트 */
async function updateMyInfo() {
    const body = {
        username: myInfo.username,
        email: document.getElementById('editEmail').value,
        phone: document.getElementById('editPhone').value,
        birth: document.getElementById('editBirth').value,
        address: document.getElementById('editAddress').value,
        detailAddress: document.getElementById('editDetailAddress').value
    };

    try {
        // CSRF 토큰 가져오기
        const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

        const res = await fetch('/api/users/update', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify(body)
        });

        if (!res.ok) {
            alert('수정 실패');
            return;
        }

        // 화면 즉시 반영
        myInfo = {
            ...myInfo,
            email: body.email,
            phone: body.phone,
            birth: body.birth,
            address: body.address,
            detailAddress: body.detailAddress
        };

        document.getElementById('email').textContent = body.email;
        document.getElementById('phone').textContent = body.phone;
        document.getElementById('birth').textContent = body.birth;
        document.getElementById('address').textContent = body.address;
        document.getElementById('detailAddress').textContent = body.detailAddress;
        

        // 모달 닫기
        const modal = bootstrap.Modal.getInstance(document.getElementById('editModal'));
        if (modal) {
            modal.hide();
        }

        alert('정보가 수정되었습니다.');
    } catch (error) {
        console.error('수정 중 오류 발생:', error);
        alert('수정 중 오류가 발생했습니다.');
    }
}

/* 회원 탈퇴 처리 */
async function deleteMyAccount() {
    if (!confirm('정말로 회원 탈퇴 하시겠습니까? 모든 데이터가 삭제됩니다.')) return;

    try {
        const res = await fetch('/api/users/delete', {
            method: 'DELETE',
            headers: {
                [csrfHeader]: csrfToken
            }
        });

        if (!res.ok) {
            const text = await res.text();
            console.error('회원 탈퇴 실패:', res.status, text);
            alert('회원 탈퇴 실패: ' + res.status);
            return;
        }

        const data = await res.json();
        if (data.success) {
            alert(data.message || '회원 탈퇴가 완료되었습니다.');
            window.location.href = '/';
        } else {
            alert('회원 탈퇴 실패: ' + (data.message || '알 수 없음'));
        }
    } catch (err) {
        console.error('회원 탈퇴 중 오류:', err);
        alert('서버 오류가 발생했습니다.');
    }
}

/* 전화번호 자동 하이푼 */
const phoneInput = document.getElementById('editPhone');

phoneInput.addEventListener('input', function(e) {
    let number = e.target.value.replace(/\D/g, ''); // 숫자만 추출
    if(number.length > 3 && number.length <= 7){
        number = number.replace(/(\d{3})(\d+)/, '$1-$2');
    } else if(number.length > 7){
        number = number.replace(/(\d{3})(\d{4})(\d+)/, '$1-$2-$3');
    }
    e.target.value = number;
});

/* 주소 api 버튼 기능  */

function searchAddress() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 도로명 주소를 input에 넣음
            document.getElementById('editAddress').value = data.roadAddress;
        }
    }).open();
}