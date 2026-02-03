/* mypagePet.ui.js - 반려견 화면(UI) 전용 */

/* =========================
   반려견 카드 목록 렌더링
========================= */
async function loadPetCards() {
    try {
        console.log("=== 반려견 목록 로드(UI) ===");

        const data = await apiFetchPetList();
        const container = document.getElementById('petCardsContainer');

        if (!container) {
            console.error("petCardsContainer 없음");
            return;
        }

        container.innerHTML = '';

        if (!data.success || !data.pets || data.pets.length === 0) {
            container.innerHTML =
                '<p class="text-center text-muted">등록된 반려견이 없습니다.</p>';
            return;
        }

        data.pets.forEach(pet => {
            const genderIcon = pet.gender === '수컷' ? '♂' : '♀';
            const neuteredStatus = pet.neutered === '예' ? 'O' : 'X';

            const card = document.createElement('div');
            card.className = 'pet-card';
            card.innerHTML = `
                <div class="pet-image-container">
                    <img src="/api/pets/image/${pet.no}" class="pet-image">
                </div>
                <div class="pet-info">
                    <span>이름: ${pet.name}</span>
                    <span>견종: ${pet.species} · 크기: ${pet.size} · ${pet.age}살</span>
                    <span>성별: ${genderIcon} · 중성화: ${neuteredStatus}</span>
                </div>
                <button class="btn btn-primary"
                    onclick="openEditPetModal(${pet.no})">
                    수정하기
                </button>
            `;
            container.appendChild(card);
        });

    } catch (err) {
        console.error(err);
        alert("반려견 목록을 불러오지 못했습니다.");
    }
}

/* =========================
   수정 모달 열기
========================= */
async function openEditPetModal(petNo) {
    try {
        console.log("=== 반려견 상세 로드(UI) ===", petNo);

        const pet = await apiFetchPetDetail(petNo);

        document.getElementById('petNo').value = petNo;
        document.getElementById('petName').value = pet.name;
        document.getElementById('petSpecies').value = pet.species;
        document.getElementById('petSize').value = pet.size;
        document.getElementById('petAge').value = pet.age;
        document.getElementById('petWeight').value = pet.weight;
        document.getElementById('petGender').value = pet.gender;
        document.getElementById('petNeutered').value = pet.neutered;
        document.getElementById('petVaccination').value = pet.vaccination;
        document.getElementById('petEtc').value = pet.ect || '';

        // 이미지 미리보기
        const preview = document.getElementById('profilePreview');
        if (pet.profileImgBase64) {
            preview.src = pet.profileImgBase64;
            preview.style.display = 'block';
        } else {
            preview.style.display = 'none';
        }

        new bootstrap.Modal(
            document.getElementById('addPetModal')
        ).show();

    } catch (err) {
        console.error(err);
        alert("반려견 정보를 불러오지 못했습니다.");
    }
}

/* =========================
   폼 제출 (추가 / 수정)
========================= */
async function handlePetFormSubmit(e) {
    e.preventDefault();

    const form = document.getElementById('addPetForm');
    const formData = new FormData(form);
    const petNo = document.getElementById('petNo').value;

    try {
        if (petNo) {
            await apiUpdatePet(formData);
            alert("반려견 정보가 수정되었습니다.");
        } else {
            await apiAddPet(formData);
            alert("반려견이 등록되었습니다.");
        }

        closePetModal();
        loadPetCards();

    } catch (err) {
        console.error(err);
        alert(err.message);
    }
}

/* =========================
   반려견 삭제
========================= */
async function deletePetUI() {
    const petNo = document.getElementById('petNo').value;
    if (!petNo) return;

    if (!confirm("정말로 삭제하시겠습니까?")) return;

    try {
        await apiDeletePet(petNo);
        alert("반려견이 삭제되었습니다.");

        closePetModal();
        loadPetCards();

    } catch (err) {
        console.error(err);
        alert(err.message);
    }
}

/* =========================
   모달 닫기 + 초기화
========================= */
function closePetModal() {
    const modalEl = document.getElementById('addPetModal');
    const modal = bootstrap.Modal.getInstance(modalEl);

    if (modal) modal.hide();

    const form = document.getElementById('addPetForm');
    if (form) form.reset();

    document.getElementById('petNo').value = '';
    document.getElementById('profilePreview').style.display = 'none';
}

/* =========================
   이미지 미리보기
========================= */
function setupImagePreview() {
    const input = document.getElementById('petProfile');
    const preview = document.getElementById('profilePreview');

    if (!input || !preview) return;

    input.addEventListener('change', e => {
        const file = e.target.files[0];
        if (!file) return;

        const reader = new FileReader();
        reader.onload = ev => {
            preview.src = ev.target.result;
            preview.style.display = 'block';
        };
        reader.readAsDataURL(file);
    });
}

/* =========================
   초기화
========================= */
document.addEventListener('DOMContentLoaded', () => {
    console.log("=== mypagePet UI 초기화 ===");

    loadPetCards();
    setupImagePreview();

    document
        .getElementById('addPetForm')
        .addEventListener('submit', handlePetFormSubmit);
});
