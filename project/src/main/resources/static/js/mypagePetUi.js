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
   추가 모달 열기
========================= */
async function openAddPetModal() {
    const modal = new bootstrap.Modal(
        document.getElementById('addPetModal')
    );

    const form = document.getElementById('addPetForm');
    form.reset();

    const fileInput = document.getElementById('addPetProfile');
    fileInput.value = '';

    const preview = document.getElementById('addProfilePreview');
    preview.src = '';
    preview.style.display = 'none';

    modal.show();
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
        document.getElementById('petEtc').value = pet.etc || '';

        // 이미지 미리보기
        const preview = document.getElementById('profilePreview');
        if (pet.profileImgBase64) {
            preview.src = pet.profileImgBase64;
            preview.style.display = 'block';
        } else {
            preview.style.display = 'none';
        }

        new bootstrap.Modal(
            document.getElementById('editPetModal')
        ).show();

    } catch (err) {
        console.error(err);
        alert("반려견 정보를 불러오지 못했습니다.");
    }
}

/* =========================
   폼 제출 (추가)
========================= */
async function handlePetFormSubmit(e) {
    e.preventDefault();

    const form = document.getElementById('addPetForm');
    const formData = new FormData(form);

    try {
        await apiAddPet(formData);
        alert("반려견이 등록되었습니다.");
        closePetModal();
        loadPetCards();
    } catch (err) {
        console.error(err);
        alert(err.message);
    }
}

/* =========================
   폼 제출 (수정)
========================= */
async function handlePetEditSubmit(e) {
    e.preventDefault();

    const form = document.getElementById('editPetForm');
    const formData = new FormData(form);
    const petNo = document.getElementById('petNo').value;

    try {
        await apiUpdatePet(formData);
        alert("반려견 정보가 수정되었습니다.");
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
    const addModalEl = document.getElementById('addPetModal');
    const addModal = bootstrap.Modal.getInstance(addModalEl);

    const editModalEl = document.getElementById('editPetModal');
    const editModal = bootstrap.Modal.getInstance(editModalEl);

    if (addModal) addModal.hide();
    if (editModal) editModal.hide();

    const addForm = document.getElementById('addPetForm');
    if (addForm) addForm.reset();

    const editForm = document.getElementById('editPetForm');
    if (editForm) editForm.reset();

    const addPreview = document.getElementById('addProfilePreview');
    if (addPreview) {
        addPreview.src = '';
        addPreview.style.display = 'none';
    }

    const editPreview = document.getElementById('profilePreview');
    if (editPreview) {
        editPreview.src = '';
        editPreview.style.display = 'none';
    }

    document.getElementById('petNo').value = '';
    document.getElementById('addPetProfile').value = '';
    document.getElementById('petProfile').value = '';
}

document.getElementById('addPetModal')
  .addEventListener('hidden.bs.modal', () => {
    const form = document.getElementById('addPetForm');
    form.reset();

    const img = document.getElementById('addProfilePreview');
    img.src = '';
    img.style.display = 'none';

    document.getElementById('addPetProfile').value = '';
});

/* =========================
   이미지 미리보기
========================= */
async function setupImagePreview() {
    const inputEdit = document.getElementById('petProfile');
    const previewEdit = document.getElementById('profilePreview');

    const inputAdd = document.getElementById('addPetProfile');
    const previewAdd = document.getElementById('addProfilePreview');

    if (inputEdit && previewEdit) {
        inputEdit.addEventListener('change', e => {
            const file = e.target.files[0];
            if (!file) return;

            const reader = new FileReader();
            reader.onload = ev => {
                previewEdit.src = ev.target.result;
                previewEdit.style.display = 'block';
            };
            reader.readAsDataURL(file);
        });
    }

    if (inputAdd && previewAdd) {
        inputAdd.addEventListener('change', e => {
            const file = e.target.files[0];
            if (!file) return;

            const reader = new FileReader();
            reader.onload = ev => {
                previewAdd.src = ev.target.result;
                previewAdd.style.display = 'block';
            };
            reader.readAsDataURL(file);
        });
    }
}


/* =========================
   초기화
========================= */
document.addEventListener('DOMContentLoaded', () => {
    console.log("=== mypagePet UI 초기화 ===");

    loadPetCards();
    setupImagePreview();

    document.getElementById('addPetForm').addEventListener('submit', handlePetFormSubmit);
    document.getElementById('editPetForm').addEventListener('submit', handlePetEditSubmit);
});
