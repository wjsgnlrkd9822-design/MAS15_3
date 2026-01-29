const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');


/* 마이 페이지 내정보 불러오기 */
document.addEventListener("DOMContentLoaded", async () => {
try {
    const response = await fetch('/api/users/select', {
    headers: { 'Content-Type' : 'application/json' }
    });

    if (!response.ok) throw new Error('정보 조회 실패');

    const data = await response.json();
    document.getElementById('username').textContent = data.username;
    document.getElementById('name').textContent = data.name;
    document.getElementById('email').textContent = data.email;
    document.getElementById('phone').textContent = data.phone;
    document.getElementById('birth').textContent = data.birth;
    document.getElementById('address').textContent = data.address;
} catch (error) {
    console.error('Error fetching user info:', error);
    alert('정보를 불러오는 중 오류 발생.');
}
});




/* 마이 페이지 내정보 수정하기*/
let myInfo = null;

/* 페이지 로딩 시 내 정보 조회 */
document.addEventListener("DOMContentLoaded", async () => {
  const res = await fetch('/api/users/select');
  myInfo = await res.json();

  user.textContent = myInfo.username;
  name.textContent = myInfo.name;
  email.textContent = myInfo.email;
  phone.textContent = myInfo.phone;
  birth.textContent = myInfo.birth;
  address.textContent = myInfo.address;
});

/* 모달 열릴 때 input 자동 세팅 */
document.getElementById('editModal')
  .addEventListener('show.bs.modal', () => {

    editUsername.value = myInfo.username;
    editName.value = myInfo.name;
    editEmail.value = myInfo.email;
    editPhone.value = myInfo.phone;
    editBirth.value = myInfo.birth;
    editAddress.value = myInfo.address;
});

/* 수정 버튼 클릭 → 비동기 업데이트 */
async function updateMyInfo() {

  const body = {
    email: editEmail.value,
    phone: editPhone.value,
    birth: editBirth.value,
    address: editAddress.value


  };

  const res = await fetch('/api/users/update', {
    method: 'PUT',
    headers: { 
        'Content-Type': 'application/json' 
        , [csrfHeader]: csrfToken               // CSRF 토큰 헤더 추가 이거 없으면 403 에러 남 
    },
    body: JSON.stringify(body)
  });

  if (!res.ok) {
    alert('수정 실패');
    return;
  }

  // 화면 즉시 반영
    myInfo = body;
    myInfo.username = editUsername.value;
    myInfo.name = editName.value;
    myInfo.email = editEmail.value;
    myInfo.phone = editPhone.value;
    myInfo.birth = editBirth.value;
    myInfo.address = editAddress.value;

  // 모달 닫기
  bootstrap.Modal.getInstance(
    document.getElementById('editModal')
  ).hide();

  alert('정보가 수정되었습니다.');

  location.reload();
}