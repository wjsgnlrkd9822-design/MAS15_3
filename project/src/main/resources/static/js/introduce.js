// 슬라이더 기능
document.addEventListener('DOMContentLoaded', function() {
    const facilityItems = document.querySelectorAll('.facility-item');
    
    facilityItems.forEach(item => {
        const sliderTrack = item.querySelector('.slider-track');
        const slides = item.querySelectorAll('.slide');
        const prevBtn = item.querySelector('.prev-btn');
        const nextBtn = item.querySelector('.next-btn');
        const sliderContainer = item.querySelector('.slider-container');
        
        let currentIndex = 0;
        const totalSlides = slides.length;
        
        // 슬라이드 1개만 있으면 버튼 숨김
        if (totalSlides <= 1) {
            prevBtn.style.display = 'none';
            nextBtn.style.display = 'none';
            return;
        }
        
        // 무한 슬라이드를 위한 복제
        const firstClone = slides[0].cloneNode(true);
        const lastClone = slides[totalSlides - 1].cloneNode(true);
        
        sliderTrack.appendChild(firstClone);
        sliderTrack.insertBefore(lastClone, slides[0]);
        
        let currentPosition = -100; // 복제된 마지막 슬라이드를 고려한 초기 위치
        sliderTrack.style.transform = `translateX(${currentPosition}%)`;
        
        // 드래그 변수
        let isDragging = false;
        let startPos = 0;
        let currentTranslate = 0;
        let prevTranslate = currentPosition;
        let animationID = 0;
        
        function setSliderPosition() {
            sliderTrack.style.transform = `translateX(${currentTranslate}%)`;
        }
        
        function animation() {
            setSliderPosition();
            if (isDragging) requestAnimationFrame(animation);
        }
        
        function updateSlider(smooth = true) {
            if (smooth) {
                sliderTrack.style.transition = 'transform 0.4s ease';
            } else {
                sliderTrack.style.transition = 'none';
            }
            currentPosition = -(currentIndex + 1) * 100;
            sliderTrack.style.transform = `translateX(${currentPosition}%)`;
            prevTranslate = currentPosition;
            currentTranslate = currentPosition;
        }
        
        // 다음 슬라이드
        function nextSlide() {
            currentIndex++;
            updateSlider(true);
            
            // 마지막 슬라이드 복제본에 도달하면
            setTimeout(() => {
                if (currentIndex >= totalSlides) {
                    currentIndex = 0;
                    updateSlider(false);
                }
            }, 400);
        }
        
        // 이전 슬라이드
        function prevSlide() {
            currentIndex--;
            updateSlider(true);
            
            // 첫 슬라이드 복제본에 도달하면
            setTimeout(() => {
                if (currentIndex < 0) {
                    currentIndex = totalSlides - 1;
                    updateSlider(false);
                }
            }, 400);
        }
        
        // 버튼 이벤트
        nextBtn.addEventListener('click', (e) => {
            e.preventDefault();
            if (isDragging) return;
            nextSlide();
        });
        
        prevBtn.addEventListener('click', (e) => {
            e.preventDefault();
            if (isDragging) return;
            prevSlide();
        });
        
        // 마우스 드래그 시작
        function dragStart(e) {
            if (e.type === 'touchstart') {
                startPos = e.touches[0].clientX;
            } else {
                startPos = e.clientX;
                e.preventDefault();
            }
            
            isDragging = true;
            animationID = requestAnimationFrame(animation);
            sliderContainer.style.cursor = 'grabbing';
        }
        
        // 드래그 중
        function dragMove(e) {
            if (!isDragging) return;
            
            const currentPosition = e.type === 'touchmove' 
                ? e.touches[0].clientX 
                : e.clientX;
            
            const diff = currentPosition - startPos;
            const movePercent = (diff / sliderContainer.offsetWidth) * 100;
            
            currentTranslate = prevTranslate + movePercent;
            
            if (e.type === 'mousemove') {
                e.preventDefault();
            }
        }
        
        // 드래그 종료
        function dragEnd(e) {
            if (!isDragging) return;
            
            isDragging = false;
            cancelAnimationFrame(animationID);
            sliderContainer.style.cursor = 'grab';
            
            const movedBy = currentTranslate - prevTranslate;
            
            // 30% 이상 이동했을 때만 슬라이드 변경
            if (movedBy < -15) {
                nextSlide();
            } else if (movedBy > 15) {
                prevSlide();
            } else {
                // 원위치
                updateSlider(true);
            }
        }
        
        // 마우스 이벤트
        sliderContainer.addEventListener('mousedown', dragStart);
        sliderContainer.addEventListener('mousemove', dragMove);
        sliderContainer.addEventListener('mouseup', dragEnd);
        sliderContainer.addEventListener('mouseleave', dragEnd);
        
        // 터치 이벤트
        sliderContainer.addEventListener('touchstart', dragStart, { passive: true });
        sliderContainer.addEventListener('touchmove', dragMove, { passive: false });
        sliderContainer.addEventListener('touchend', dragEnd);
        
        // 이미지 드래그 방지
        const allSlides = sliderTrack.querySelectorAll('.slide');
        allSlides.forEach(slide => {
            const img = slide.querySelector('img');
            if (img) {
                img.addEventListener('dragstart', (e) => {
                    e.preventDefault();
                });
            }
        });
        
        // 초기 위치 설정
        updateSlider(false);
    });
});