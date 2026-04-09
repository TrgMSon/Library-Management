const mainView = document.querySelector(".main-view2");
const itBooks = document.getElementById("itBook");
const novels = document.getElementById("novel");
const comicBooks = document.getElementById("comicBook");
const scienceBooks = document.getElementById("scienceBook");
const literatureBooks = document.getElementById("literatureBook");
const viewDetailBtns = document.querySelectorAll(".viewDetailBtn");
const logoutBtn = document.getElementById("logoutBtn");

let bookType = "IT";

itBooks.style.backgroundColor = "#A9A9A9";
comicBooks.style.backgroundColor = "";
scienceBooks.style.backgroundColor = "";
literatureBooks.style.backgroundColor = "";
novels.style.backgroundColor = "";

function addBookToUI(book) {
    let viewBook = document.createElement("div");
    viewBook.classList.add("viewBook");

    let img = document.createElement("img");
    img.src = book.urlImg;

    let name = document.createElement("p");
    name.innerText = book.name;

    let addBtn = document.createElement("button");
    addBtn.classList.add("addToCart")
    addBtn.innerText = "Thêm vào giỏ hàng";
    addBtn.dataset.bookId = book.bookId;

    let viewDetailBtn = document.createElement("button");
    viewDetailBtn.classList.add("viewDetailBtn");
    viewDetailBtn.innerText = "Xem chi tiết";
    viewDetailBtn.dataset.bookId = book.bookId;

    viewBook.appendChild(img);
    viewBook.appendChild(name);
    viewBook.appendChild(addBtn);
    viewBook.appendChild(viewDetailBtn);

    mainView.appendChild(viewBook);
}

itBooks.addEventListener("click", async function () {
    bookType = "IT";

    itBooks.style.backgroundColor = "#A9A9A9";
    novels.style.backgroundColor = "";
    comicBooks.style.backgroundColor = "";
    scienceBooks.style.backgroundColor = "";
    literatureBooks.style.backgroundColor = "";

    mainView.innerHTML = "";

    let response = await fetch("/api/book/getBookType?type=IT");
    let books = await response.json();
    for (let i = 0; i < books.length; i++) {
        addBookToUI(books[i]);
    }
});

novels.addEventListener("click", async function () {
    bookType = "Novel";

    itBooks.style.backgroundColor = "";
    novels.style.backgroundColor = "#A9A9A9";
    comicBooks.style.backgroundColor = "";
    scienceBooks.style.backgroundColor = "";
    literatureBooks.style.backgroundColor = "";

    mainView.innerHTML = "";

    let response = await fetch("/api/book/getBookType?type=Novel");
    let books = await response.json();
    for (let i = 0; i < books.length; i++) {
        addBookToUI(books[i]);
    }
});

comicBooks.addEventListener("click", async function () {
    bookType = "comic";

    itBooks.style.backgroundColor = "";
    novels.style.backgroundColor = "";
    comicBooks.style.backgroundColor = "#A9A9A9";
    scienceBooks.style.backgroundColor = "";
    literatureBooks.style.backgroundColor = "";

    mainView.innerHTML = "";

    let response = await fetch("/api/book/getBookType?type=comic");
    let books = await response.json();
    for (let i = 0; i < books.length; i++) {
        addBookToUI(books[i]);
    }
});

scienceBooks.addEventListener("click", async function () {
    bookType = "Science";

    itBooks.style.backgroundColor = "";
    novels.style.backgroundColor = "";
    comicBooks.style.backgroundColor = "";
    scienceBooks.style.backgroundColor = "#A9A9A9";
    literatureBooks.style.backgroundColor = "";

    mainView.innerHTML = "";

    let response = await fetch("/api/book/getBookType?type=Science");
    let books = await response.json();
    for (let i = 0; i < books.length; i++) {
        addBookToUI(books[i]);
    }
});

literatureBooks.addEventListener("click", async function () {
    bookType = "literature";

    itBooks.style.backgroundColor = "";
    novels.style.backgroundColor = "";
    comicBooks.style.backgroundColor = "";
    scienceBooks.style.backgroundColor = "";
    literatureBooks.style.backgroundColor = "#A9A9A9";

    mainView.innerHTML = "";

    let response = await fetch("/api/book/getBookType?type=literature");
    let books = await response.json();
    for (let i = 0; i < books.length; i++) {
        addBookToUI(books[i]);
    }
});

mainView.addEventListener("click", async function (e) {
    if (e.target.classList.contains("viewDetailBtn")) {
        let bookId = e.target.dataset.bookId;
        window.location.href = "/viewDetail?bookId=" + bookId;
    }
});

logoutBtn.addEventListener("click", async function (e) {
    e.preventDefault();

    await fetch("/api/deleteCart", {
        method: "POST"
    });
    cartId = null;

    window.location.href = "/logout";
});