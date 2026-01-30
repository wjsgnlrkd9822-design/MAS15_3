/* mypagePet.js - 반려견 관리 스크립트 */

/* 반려견 목록 불러오기 */
async function loadPetCards() {
    try {
        console.log("=== 반려견 목록 로드 ===");
        const response = await fetch('/api/pets/list', {
            headers: { 'Content-Type': 'application/json' }
        });
        
        console.log("API 응답 상태:", response.status);
        
        if (!response.ok) {
            const errorText = await response.text();
            console.error('반려견 API 응답 에러:', response.status, errorText);
            throw new Error('반려견 목록 조회 실패: ' + response.status);
        }
        
        const data = await response.json();
        console.log("반려견 데이터:", data);
        
        const container = document.getElementById('petCardsContainer');
        if (!container) {
            console.error("petCardsContainer 엘리먼트를 찾을 수 없습니다!");
            return;
        }
        
        container.innerHTML = '';
        
        if (data.success && data.pets && data.pets.length > 0) {
            console.log("반려견 수:", data.pets.length);
            data.pets.forEach(pet => {
                const genderIcon = pet.gender === '수컷' ? '♂' : '♀';
                const neuteredStatus = pet.neutered === '예' ? 'O' : 'X';
                
                const petCard = document.createElement('div');
                petCard.className = 'pet-card';
                petCard.innerHTML = `
                    <div class="pet-image-container">
                        <img src="/api/pets/image/${pet.no}" alt="${pet.name}" class="pet-image">
                    </div>
                    <div class="pet-info">
                        <span>이름: ${pet.name}</span>
                        <span>견종: ${pet.species} · 크기: ${pet.size} · ${pet.age}살</span>
                        <span>성별: ${genderIcon} · 중성화: ${neuteredStatus}</span>
                    </div>
                    <button class="btn btn-primary" onclick="viewPetDetail(${pet.no})">수정하기</button>
                `;
                container.appendChild(petCard);
            });
        } else {
            console.log("등록된 반려견이 없습니다.");
            container.innerHTML = '<p style="text-align: center; color: #999;">등록된 반려견이 없습니다.</p>';
        }
    } catch (error) {
        console.error('=== 반려견 로드 실패 ===:', error);
    }
}











/* 반려견 상세보기 - 모달 열기 및 데이터 로드 */
async function viewPetDetail(petNo) {
    console.log(`반려견 상세정보 조회 시작... petNo: ${petNo}`);
    
    const modalEl = document.getElementById('addPetModal');
    if (!modalEl) {
        console.error("addPetModal 엘리먼트를 찾을 수 없습니다!");
        return;
    }

    try {
        // API에서 반려견 상세 정보 조회
        console.log(` GET /api/pets/${petNo} 요청 중...`);
        const res = await fetch(`/api/pets/${petNo}`);
        
        console.log(`응답 상태: ${res.status} ${res.statusText}`);
        
        if (!res.ok) {
            const errorText = await res.text();
            console.error(`상세정보 조회 실패:`, {
                status: res.status,
                statusText: res.statusText,
                body: errorText
            });
            alert(`반려견 정보 불러오기 실패 (${res.status})\n${errorText}`);
            return;
        }

        const pet = await res.json();
        console.log("조회된 반려견 데이터:", pet);

        // hidden input에 petNo 설정
        const petNoInput = document.getElementById('petNo');
        if (petNoInput) petNoInput.value = petNo;

        // 폼 필드에 데이터 채우기
        const fields = {
            'petName': pet.name,
            'petSpecies': pet.species,
            'petSize': pet.size,
            'petAge': pet.age,
            'petWeight': pet.weight,
            'petGender': pet.gender,
            'petNeutered': pet.neutered,
            'petVaccination': pet.vaccination,
            'petEtc': pet.ect || ""
        };

        Object.entries(fields).forEach(([fieldId, value]) => {
            const el = document.getElementById(fieldId);
            if (el) el.value = value;
        });

        // 이미지 미리보기 설정
        const profilePreview = document.getElementById('profilePreview');
        if (profilePreview) {
            // Base64 이미지 문자열 사용
            if (pet.profileImgBase64) {
                profilePreview.src = pet.profileImgBase64;
                profilePreview.style.display = 'block';
                console.log("✓ 이미지 미리보기 설정 완료");
            } else if (pet.profileImg) {
                // byte array인 경우 처리
                if (Array.isArray(pet.profileImg)) {
                    const blob = new Blob([new Uint8Array(pet.profileImg)], { type: 'image/jpeg' });
                    const url = URL.createObjectURL(blob);
                    profilePreview.src = url;
                    profilePreview.style.display = 'block';
                }
            } else {
                profilePreview.style.display = 'none';
            }
        }

        // 모달 표시
        const modal = new bootstrap.Modal(modalEl);
        modal.show();
        console.log("반려견 상세정보 모달 표시 완료");

    } catch (err) {
        console.error("반려견 상세정보 조회 오류:", err);
        alert("반려견 정보 불러오기 실패: " + err.message);
    }
}







/* 반려견 폼 제출 처리 - 추가 및 수정 */
function setupAddPetForm() {
    console.log("=== setupAddPetForm 실행 ===");
    const form = document.getElementById("addPetForm");
    if (!form) {
        console.error("반려견 추가 폼을 찾을 수 없습니다.");
        return;
    }
    
    // 이전 리스너 제거 (중복 방지)
    form.removeEventListener("submit", handleFormSubmit);
    form.addEventListener("submit", handleFormSubmit);
    console.log("✓ 반려견 폼 제출 이벤트 리스너 등록 완료");
}

async function handleFormSubmit(e) {
    e.preventDefault();
    console.log("=== 반려견 폼 제출 시작 ===");

    const form = document.getElementById("addPetForm");
    const formData = new FormData(form);
    const petNo = document.getElementById('petNo')?.value;
    
    // FormData 로깅
    console.log("폼 데이터:");
    for (let [key, value] of formData.entries()) {
        if (value instanceof File) {
            console.log(`  ${key}: File(${value.name})`);
        } else {
            console.log(`  ${key}: ${value}`);
        }
    }

    const isUpdate = petNo && petNo.trim() !== '';
    console.log("모드:", isUpdate ? "수정" : "신규추가");

    try {
        const url = isUpdate ? "/api/pets/update" : "/api/pets/add";
        
        // CSRF 토큰 확인
        if (!csrfToken) {
            console.error("CSRF 토큰이 없습니다!");
            alert("보안 토큰이 없습니다. 페이지를 새로고침 해주세요.");
            return;
        }

        const headers = {};
        headers[csrfHeader] = csrfToken;

        console.log(`\n API 요청:`);
        console.log(`  URL: ${url}`);
        console.log(`  Method: POST`);
        console.log(`  CSRF Header: ${csrfHeader}`);
        
        const res = await fetch(url, {
            method: "POST",
            headers: headers,
            body: formData
        });

        console.log(` API 응답: ${res.status} ${res.statusText}`);

        if (!res.ok) {
            const text = await res.text();
            console.error("API 에러:", text);
            alert(`저장 실패: ${res.status} ${res.statusText}\n${text}`);
            return;
        }

        const data = await res.json();
        console.log("응답 데이터:", data);
        
        if (data.success) {
            const message = isUpdate ? "반려견 정보 수정 성공!" : "반려견 등록 성공!";
            alert(message);
            console.log("✓ " + message);
            
            // 폼 초기화
            form.reset();
            const petNoInput = document.getElementById('petNo');
            if (petNoInput) petNoInput.value = '';
            const profilePreview = document.getElementById('profilePreview');
            if (profilePreview) profilePreview.style.display = 'none';
            
            // 모달 닫기
            const modal = bootstrap.Modal.getInstance(document.getElementById('addPetModal'));
            if (modal) {
                modal.hide();
                console.log("✓ 모달 닫힘");
            }
            
            // 반려견 목록 새로고침
            console.log("목록 새로고침 중...");
            loadPetCards();
        } else {
            alert("저장 실패: " + data.message);
            console.error("저장 실패:", data.message);
        }

    } catch (err) {
        console.error("=== 폼 제출 중 에러 ===:", err);
        alert("서버 오류: " + err.message);
    }
}


/* DOMContentLoaded - 페이지 로드 시 초기화 */
document.addEventListener("DOMContentLoaded", async () => {
    console.log("=== mypagePet.js 초기화 ===");
    
    // 반려견 목록 로드
    loadPetCards();
    
    // 반려견 추가 폼 이벤트 리스너 설정
    setupAddPetForm();
    
    // 파일 입력 변경 이벤트 리스너
    const petProfile = document.getElementById('petProfile');
    if (petProfile) {
        petProfile.addEventListener('change', function(e) {
            const file = e.target.files[0];
            const profilePreview = document.getElementById('profilePreview');
            
            if (file && profilePreview) {
                const reader = new FileReader();
                reader.onload = function(event) {
                    profilePreview.src = event.target.result;
                    profilePreview.style.display = 'block';
                    console.log("이미지 미리보기 업데이트됨");
                };
                reader.readAsDataURL(file);
            }
        });
    }
    
    // 모달이 닫힐 때 폼 초기화
    const addPetModal = document.getElementById('addPetModal');
    if (addPetModal) {
        addPetModal.addEventListener('hidden.bs.modal', function() {
            const form = document.getElementById('addPetForm');
            if (form) {
                form.reset();
                const petNoInput = document.getElementById('petNo');
                if (petNoInput) petNoInput.value = '';
                const profilePreview = document.getElementById('profilePreview');
                if (profilePreview) profilePreview.style.display = 'none';
            }
        });
    }
});
