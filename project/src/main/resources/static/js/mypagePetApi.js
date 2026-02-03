/* mypagePet.api.js */

/* 반려견 목록 조회 */
async function apiFetchPetList() {
    const res = await fetch('/api/pets/list', {
        headers: { 'Content-Type': 'application/json' }
    });

    if (!res.ok) {
        throw new Error('반려견 목록 조회 실패');
    }

    return await res.json();
}

/* 반려견 단건 조회 */
async function apiFetchPetDetail(petNo) {
    const res = await fetch(`/api/pets/${petNo}`);

    if (!res.ok) {
        throw new Error('반려견 상세 조회 실패');
    }

    return await res.json();
}

/* 반려견 추가 */
async function apiAddPet(formData) {
    const res = await fetch('/api/pets/add', {
        method: 'POST',
        headers: { [csrfHeader]: csrfToken },
        body: formData
    });

    if (!res.ok) {
        throw new Error('반려견 등록 실패');
    }

    return await res.json();
}

/* 반려견 수정 */
async function apiUpdatePet(formData) {
    const res = await fetch('/api/pets/update', {
        method: 'POST',
        headers: { [csrfHeader]: csrfToken },
        body: formData
    });

    if (!res.ok) {
        throw new Error('반려견 수정 실패');
    }

    return await res.json();
}

/* 반려견 삭제 */
async function apiDeletePet(petNo) {
    const res = await fetch(`/api/pets/delete/${petNo}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
        }
    });

    if (!res.ok) {
        throw new Error('반려견 삭제 실패');
    }

    return await res.json();
}
