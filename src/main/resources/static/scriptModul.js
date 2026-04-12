const searchForm = document.getElementById("searchForm");
const goHomeBtn = document.getElementById("goHomeBtn");
const listCard = document.createElement("table");
const cardIdElement = document.getElementById("cardIdElement");
const userElement = document.getElementById("userElement");
const readerElement = document.getElementById("readerElement");
const createdElement = document.getElementById("createdElement");
const listBorrowBook = document.querySelector(".listBorrowBook");
const noteContent = document.getElementById("noteContent");
const totalAmountInput = document.getElementById("totalAmountInput");
const goBackBtn = document.getElementById("goBack");
const acptEditCard = document.getElementById("acpt");
const cancelCreateCard = document.getElementById("cancelCreateCard");
const acptCreateCard = document.getElementById("acptCreateCard");
const addBorowBookBtn = document.getElementById("addBorowBook");
const bookInCreateCard = document.querySelector(".bookInCreateCard");
const bookSearch = document.querySelector(".bookSearch");
const searchBookForm = document.getElementById("searchBookForm");
const searchBookInput = document.getElementById("searchBookInput");
const readerIdInput = document.getElementById("readerIdInput");
const userLabel = document.getElementById("userLabel");

function initListCard() {
    listCard.classList.add("listCard");

    let titleRow = document.createElement("tr");
    titleRow.classList.add("header");

    let th1 = document.createElement("th");
    th1.innerText = "ID";
    let th2 = document.createElement("th");
    th2.innerText = "Độc giả";
    let th3 = document.createElement("th");
    th3.innerText = "Người tạo";
    let th4 = document.createElement("th");
    th4.innerText = "Ngày tạo";

    titleRow.appendChild(th1);
    titleRow.appendChild(th2);
    titleRow.appendChild(th3);
    titleRow.appendChild(th4);

    listCard.appendChild(titleRow);
}

goHomeBtn.addEventListener("click", function () {
    bookType = "IT";
    window.location.href = "/home";
});

searchForm.addEventListener("submit", async function (e) {
    e.preventDefault();

    let target = searchInput.value.trim();

    if (target === "") return;

    let response = await fetch("/api/book/searchBookType?type=" + bookType + "&name=" + target);
    let books = await response.json();

    if (books.length === 0) {
        alert("Không có kết quả phù hợp")
        return;
    }

    mainView.innerHTML = "";

    for (let i = 0; i < books.length; i++) {
        addBookToUI(books[i]);
    }
});

function addCardToUI(card) {
    let row = document.createElement("tr");
    row.classList.add("row");
    row.style.fontWeight = "none";
    row.dataset.userId = card.userId;
    row.dataset.readerId = card.readerId;
    row.dataset.borrowCardId = card.borrowCardId;

    let cardId = document.createElement("td");
    cardId.innerText = card.borrowCardId;

    let reader = document.createElement("td");
    reader.innerText = card.readerName;

    let user = document.createElement("td");
    user.innerText = card.userName;

    let createdAt = document.createElement("td");
    createdAt.innerText = card.createdAt;

    row.appendChild(cardId);
    row.appendChild(reader);
    row.appendChild(user);
    row.appendChild(createdAt);

    row.addEventListener("click", async function () {
        let rowBook = document.querySelectorAll(".rowBook");
        rowBook.forEach(book => book.remove());

        loadDetailCard(row);
        mainView.style.display = "none";
        cardDiv.style.display = "flex";
    });

    listCard.appendChild(row);
}

manageBorrowCardBtn.addEventListener("click", async function () {
    itBooks.style.backgroundColor = "";
    novels.style.backgroundColor = "";
    comicBooks.style.backgroundColor = "";
    scienceBooks.style.backgroundColor = "";
    literatureBooks.style.backgroundColor = "";
    manageBorrowCardBtn.style.backgroundColor = "#A9A9A9";

    viewingBook = false;
    searchInput.style.display = "none";
    cardDiv.style.display = "none";
    createCardDiv.style.display = "none";

    mainView.innerHTML = "";
    mainView.style.display = "flex";
    mainView.style.flexDirection = "column";
    listCard.innerHTML = "";

    let rowBook = document.querySelectorAll(".rowBook");
    rowBook.forEach(book => book.remove());

    let createCardBtn = document.createElement("p");
    createCardBtn.classList.add("createCardBtn");
    createCardBtn.innerText = "Tạo phiếu mượn";
    createCardBtn.addEventListener("click", function () {
        mainView.style.display = "none";
        createCardDiv.style.display = "flex";
    });
    mainView.appendChild(createCardBtn);

    let searchInputCard = document.createElement("input");
    searchInputCard.classList.add("searchInputCard");
    searchInputCard.placeholder = "Tìm kiếm độc giả...";

    let buttonSearch = document.createElement("button");
    buttonSearch.type = "submit";
    buttonSearch.hidden = true;

    let searchCardForm = document.createElement("form");
    searchCardForm.classList.add("searchCardForm");
    searchCardForm.appendChild(searchInputCard);
    searchCardForm.appendChild(buttonSearch);
    mainView.appendChild(searchCardForm);

    initListCard();

    let cards = await fetch("/api/borrowCard/getAllCard").then(res => res.json());
    for (let i = 0; i < cards.length; i++) {
        addCardToUI(cards[i]);
    }

    mainView.appendChild(listCard);
});

function addBorrowBook(book) {
    let row = document.createElement("tr");
    row.classList.add("rowBook");

    let bookIdElement = document.createElement("td");
    bookIdElement.innerText = book.bookId;

    let bookNameElement = document.createElement("td");
    bookNameElement.innerText = book.bookName;

    let expireElement = document.createElement("td");
    expireElement.innerText = book.expire;

    let returnDateElement = document.createElement("td");
    returnDateElement.innerText = book.returnDate;

    let statusElement = document.createElement("td");
    if (book.status === "borrowing") statusElement.innerText = "Chưa trả";
    else statusElement.innerText = "Đã trả";

    let actionElement = document.createElement("td");
    actionElement.classList.add("actionReturn");
    if (book.status === "borrowing") actionElement.innerText = "Trả";
    else actionElement.innerText = "";

    row.appendChild(bookIdElement);
    row.appendChild(bookNameElement);
    row.appendChild(expireElement);
    row.appendChild(returnDateElement);
    row.appendChild(statusElement);
    row.appendChild(actionElement);

    listBorrowBook.appendChild(row);
}

async function loadDetailCard(row) {
    let cardDetail = await fetch("/api/borrowCard/getDetailCard?cardId=" + row.dataset.borrowCardId).then(res => res.json());

    cardIdElement.innerText = "ID: " + cardDetail.borrowCardId;
    userElement.innerText = "Người tạo: " + cardDetail.userName + " (ID: " + cardDetail.userId + ")";
    readerElement.innerText = "Độc giả: " + cardDetail.readerName + " (ID: " + cardDetail.readerId + ")";
    createdElement.innerText = "Ngày tạo: " + cardDetail.createdAt;

    let books = cardDetail.books;
    for (let i = 0; i < books.length; i++) {
        addBorrowBook(books[i]);
    }
}

goBackBtn.addEventListener("click", function () {
    cardDiv.style.display = "none";
    mainView.style.display = "flex";
});

cancelCreateCard.addEventListener("click", function () {
    createCardDiv.style.display = "none";
    mainView.style.display = "flex";
});

function addRow() {
    let row = document.createElement("tr");
    row.classList.add("rowBook");

    let bookIdElement = document.createElement("td");
    bookIdElement.contentEditable = true;

    let bookNameElement = document.createElement("td");
    bookNameElement.contentEditable = true;

    let expireElement = document.createElement("td");
    expireElement.contentEditable = true;

    row.appendChild(bookIdElement);
    row.appendChild(bookNameElement);
    row.appendChild(expireElement);

    bookInCreateCard.appendChild(row);
}

addBorowBookBtn.addEventListener("click", function () {
    let qtyBook = document.querySelectorAll(".rowBook");
    if (qtyBook.length > 4) {
        alert("Chỉ được mượn tối đa 5 quyển sách cùng lúc");
        return;
    }

    addRow();
});

function addBookSearch(book) {
    let row = document.createElement("tr");
    row.classList.add("rowBookSearch");

    let bookIdElement = document.createElement("td");
    bookIdElement.innerText = book.bookId;

    let bookNameElement = document.createElement("td");
    bookNameElement.innerText = book.name;

    let qtyElement = document.createElement("td");
    qtyElement.innerText = book.quantity;

    row.appendChild(bookIdElement);
    row.appendChild(bookNameElement);
    row.appendChild(qtyElement);

    bookSearch.appendChild(row);
}

searchBookForm.addEventListener("submit", async function (e) {
    e.preventDefault();

    let oldResult = document.querySelectorAll(".rowBookSearch");
    oldResult.forEach(book => book.remove());

    let target = searchBookInput.value.trim();
    if (target === "") return;

    let books = await fetch("/api/book/searchBook?name=" + target).then(res => res.json());
    for (let i = 0; i < books.length; i++) addBookSearch(books[i]);
});

acptCreateCard.addEventListener("click", async function () {
    let readerId = readerIdInput.value.trim();
    if (readerId === "") {
        alert("Vui lòng nhập mã độc giả");
        return;
    }

    let rowBooks = document.querySelectorAll(".rowBook");
    if (rowBooks.length === 0) {
        alert("Vui lòng chọn sách mượn");
        return;
    }

    let response = await fetch("/api/reader/checkReaderInfor?readerId=" + readerId).then(res => res.text());
    if (response === "false") {
        alert("Thông tin độc giả không tồn tại, vui lòng thêm độc giả");
        return;
    }

    let qtyBorrowing = await fetch("/api/reader/checkBorrowingBook?readerId=" + readerId).then(res => res.text());
    if (Number(qtyBorrowing) + rowBooks.length > 5) {
        alert("Độc giả đang mượn " + qtyBorrowing + " quyển sách, chỉ được mượn thêm " + (5 - Number(qtyBorrowing)) + " quyển sách");
        return;
    }

    response = await fetch("/api/reader/createBorrowCard", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            userId: userLabel.dataset.userId,
            readerId: readerId,
            totalAmount: "0",
            note: ""
        })
    }).then(res => res.text());
    let borrowCardId = Number(response);

    let borrowBooks = [];
    for (let i=0; i<rowBooks.length; i++) {
        borrowBooks.push({
            borrowCardId: borrowCardId,
            bookId: (rowBooks[i].cells)[0].innerText,
            expire: (rowBooks[i].cells)[2].innerText
        });
    }

    await fetch("/api/reader/addDetailCard", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            cardDetails: borrowBooks
        })
    });

    resetCreateCard();

    alert("Tạo phiếu mượn thành công");
});

function resetCreateCard() {
    readerIdInput.value = "";
    searchBookInput.value = "";

    let borrowBooks = document.querySelectorAll(".rowBook");
    borrowBooks.forEach(book => book.remove());
}