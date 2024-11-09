// 게시글로 이동
document.querySelectorAll('.mypage-protect-list').forEach(function(element) {
    element.addEventListener('click', function() {
        const protectRequestNo = element.querySelector('.protectRequestNo').innerText;
        window.location.href = '/mypage/updateProtectRequest?protectRequestNo=' + protectRequestNo; // 상세 페이지로 이동
    });
});
