const goBackBtn = document.getElementById("goBackBtn");

goBackBtn.addEventListener("click", function (event) {
    event.preventDefault();
    window.history.back();
});