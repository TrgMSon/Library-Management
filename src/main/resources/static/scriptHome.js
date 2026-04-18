const mainView = document.querySelector(".main-view2");
const itBooks = document.getElementById("itBook");
const novels = document.getElementById("novel");
const comicBooks = document.getElementById("comicBook");
const scienceBooks = document.getElementById("scienceBook");
const literatureBooks = document.getElementById("literatureBook");
const viewDetailBtns = document.querySelectorAll(".viewDetailBtn");
const logoutBtn = document.getElementById("logoutBtn");
const manageBorrowCardBtn = document.getElementById("manageBorrowCard");
const searchInput = document.getElementById("searchInput");
const cardDiv = document.querySelector(".cardDiv");
const createCardDiv = document.querySelector(".createCardDiv");
const userLabel = document.getElementById("userLabel");
const manageEmployee = document.getElementById("manageEmployee");
const overView = document.getElementById("overView");
const inforDiv = document.querySelector(".infor-div");
const addBookBtn = document.getElementById("addBookBtn");
const waiting = document.getElementById("waiting");

waiting.classList.add("hide");
inforDiv.style.display = "none";

if (userLabel.dataset.role === "user") {
    manageEmployee.style.display = "none";
    overView.style.display = "none";
    addBookBtn.style.display = "none";
}

cardDiv.style.display = "none";
createCardDiv.style.display = "none";

let bookType = "IT";
let viewingBook = true;

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

    let viewDetailBtn = document.createElement("button");
    viewDetailBtn.classList.add("viewDetailBtn");
    viewDetailBtn.innerText = "Xem chi tiết";
    viewDetailBtn.dataset.bookId = book.bookId;

    viewBook.appendChild(img);
    viewBook.appendChild(name);
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
    manageBorrowCardBtn.style.backgroundColor = "";

    viewingBook = true;
    searchInput.style.display = "block";
    cardDiv.style.display = "none";
    createCardDiv.style.display = "none";

    mainView.innerHTML = "";
    mainView.style.display = "grid";
    mainView.style.flexDirection = "";

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
    manageBorrowCardBtn.style.backgroundColor = "";

    viewingBook = true;
    searchInput.style.display = "block";
    cardDiv.style.display = "none";
    createCardDiv.style.display = "none";

    mainView.innerHTML = "";
    mainView.style.display = "grid";
    mainView.style.flexDirection = "";

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
    manageBorrowCardBtn.style.backgroundColor = "";

    viewingBook = true;
    searchInput.style.display = "block";
    cardDiv.style.display = "none";
    createCardDiv.style.display = "none";

    mainView.innerHTML = "";
    mainView.style.display = "grid";
    mainView.style.flexDirection = "";

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
    manageBorrowCardBtn.style.backgroundColor = "";

    viewingBook = true;
    searchInput.style.display = "block";
    cardDiv.style.display = "none";
    createCardDiv.style.display = "none";

    mainView.innerHTML = "";
    mainView.style.display = "grid";
    mainView.style.flexDirection = "";

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
    manageBorrowCardBtn.style.backgroundColor = "";

    viewingBook = true;
    searchInput.style.display = "block";
    cardDiv.style.display = "none";
    createCardDiv.style.display = "none";

    mainView.innerHTML = "";
    mainView.style.display = "grid";
    mainView.style.flexDirection = "";

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
    window.location.href = "/logout";
});

addBookBtn.addEventListener("click", function () {
    inforDiv.style.display = "flex";
    addBookBtn.style.textDecoration = "underline";
    addBookBtn.style.color = "#c45500";
});

inforDiv.addEventListener("click", async function (e) {
    if (e.target.classList.contains("acptBtn")) {
        waiting.classList.remove("hide");

        let nameInput = document.getElementById("bookName");
        let authorInput = document.getElementById("author");
        let descInput = document.getElementById("desc");
        let publishInput = document.getElementById("publish");
        let typeInput = document.getElementById("type");
        let qtyInput = document.getElementById("qty");
        let imgInput = document.getElementById("imgInput");

        let name = nameInput.value.trim();
        let author = authorInput.value.trim();
        let description = descInput.value.trim();
        let publish = publishInput.value.trim();
        let type = typeInput.value.trim();
        let quantity = qtyInput.value.trim();
        let files = imgInput.files;

        if (name === "") {
            nameInput.focus();
            alert("Vui lòng nhập đủ thông tin");
            waiting.classList.add("hide");
            return;
        }

        if (author === "") {
            authorInput.focus();
            alert("Vui lòng nhập đủ thông tin");
            waiting.classList.add("hide");
            return;
        }

        if (publish === "") {
            publishInput.focus();
            alert("Vui lòng nhập đủ thông tin");
            waiting.classList.add("hide");
            return;
        }

        if (description === "") {
            descInput.focus();
            alert("Vui lòng nhập đủ thông tin");
            waiting.classList.add("hide");
            return;
        }

        if (type === "") {
            typeInput.focus();
            alert("Vui lòng nhập đủ thông tin");
            waiting.classList.add("hide");
            return;
        }

        if (quantity === "") {
            qtyInput.focus();
            alert("Vui lòng nhập đủ thông tin");
            waiting.classList.add("hide");
            return;
        }

        if (files.length === 0) {
            alert("Vui lòng nhập đủ thông tin");
            waiting.classList.add("hide");
            return;
        }
        else {
            let formData = new FormData();
            let image = files[0];
            if (image.size > 20 * 1024 * 1024) {
                alert("Ảnh gửi lên quá 20MB, vui lòng thử lại");
                return;
            }

            formData.append("image", image);
            let response = await fetch("/api/book/upload-image", {
                method: "POST",
                body: formData
            });

            let urlImg = await response.text();

            await fetch("/api/book/addBook", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    bookId: e.target.dataset.bookId,
                    name: name,
                    author: author,
                    description: description,
                    publish: publish,
                    type: type,
                    quantity: quantity,
                    urlImg: urlImg
                })
            });
        }

        alert("Cập nhật thành công");
        waiting.classList.add("hide");

        inforDiv.style.display = "none";
        addBookBtn.style.textDecoration = "";
        addBookBtn.style.color = "";
        window.location.reload();
    }

    else if (e.target.classList.contains("closeBtn")) {
        inforDiv.style.display = "none";
        addBookBtn.style.textDecoration = "";
        addBookBtn.style.color = "";
    }
});