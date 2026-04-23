const searchForm = document.getElementById("searchForm");
const goHomeBtn = document.getElementById("goHomeBtn");
const listCard = document.createElement("table");
const cardIdElement = document.getElementById("cardIdElement");
const userElement = document.getElementById("userElement");
const readerElement = document.getElementById("readerElement");
const createdElement = document.getElementById("createdElement");
const listBorrowBook = document.querySelector(".listBorrowBook");
const noteContent = document.getElementById("noteContent");
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
const totalAmountElement = document.getElementById("totalAmount");

let currentCardId = null;

function formateDate(date) {
    let tmp = date.split("T");
    let tmp2 = tmp[0].split("-");
    return `${tmp2[2]}/${tmp2[1]}/${tmp2[0]} ${tmp[1]}`;
}

function formatTotal(total) {
    let ans = "";
    let arr = total.split('');
    arr.reverse();
    for (let i = 0; i < arr.length; i++) {
        ans = arr[i] + ans;
        if ((i + 1) % 3 === 0 && i > 0 && (i + 1) < arr.length) ans = '.' + ans;
    }
    return ans;
}

function initListCard() {
    listCard.classList.add("listCard");

    let titleRow = document.createElement("tr");
    titleRow.classList.add("header");

    let th1 = document.createElement("th");
    th1.innerText = "ID phiếu";
    let th5 = document.createElement("th");
    th5.innerText = "ID độc giả";
    let th2 = document.createElement("th");
    th2.innerText = "Độc giả";
    let th3 = document.createElement("th");
    th3.innerText = "Người tạo";
    let th4 = document.createElement("th");
    th4.innerText = "Ngày tạo";

    titleRow.appendChild(th1);
    titleRow.appendChild(th5);
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

    let readerId = document.createElement("td");
    readerId.innerText = card.readerId;

    let readerName = document.createElement("td");
    readerName.innerText = card.readerName;

    let user = document.createElement("td");
    user.innerText = card.userName;

    let createdAt = document.createElement("td");
    createdAt.innerText = formateDate(card.createdAt);

    row.appendChild(cardId);
    row.appendChild(readerId);
    row.appendChild(readerName);
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

function initOptionCard() {
    let buttonDiv = document.createElement("div");
    buttonDiv.style.display = "flex";

    let allBtn = document.createElement("input");
    allBtn.id = "allBtn";
    allBtn.name = "option";
    allBtn.type = "radio";
    allBtn.value = "all";
    allBtn.checked = true;
    let lbAll = document.createElement("label");
    lbAll.htmlFor = "allBtn";
    lbAll.innerText = "Tất cả";

    let borrowingBtn = document.createElement("input");
    borrowingBtn.id = "borrowingBtn";
    borrowingBtn.name = "option";
    borrowingBtn.type = "radio";
    borrowingBtn.value = "borrowing";
    let lbborrowing = document.createElement("label");
    lbborrowing.htmlFor = "borrowingBtn";
    lbborrowing.innerText = "Đang mượn";

    let returnedBtn = document.createElement("input");
    returnedBtn.id = "returnedBtn";
    returnedBtn.name = "option";
    returnedBtn.type = "radio";
    returnedBtn.value = "returned";
    let lbreturned = document.createElement("label");
    lbreturned.htmlFor = "returnedBtn";
    lbreturned.innerText = "Đã trả";

    buttonDiv.appendChild(allBtn);
    buttonDiv.appendChild(lbAll);
    buttonDiv.appendChild(borrowingBtn);
    buttonDiv.appendChild(lbborrowing);
    buttonDiv.appendChild(returnedBtn);
    buttonDiv.appendChild(lbreturned);

    return buttonDiv;
}

async function searchCard(target, option) {
    let cards = await fetch("/api/borrowCard/searchCard?target=" + target + "&option=" + option).then(res => res.json());

    if (cards.length === 0) {
        alert("Không có kết quả phù hợp");
        return;
    }

    let borrowBooks = document.querySelectorAll(".row");
    borrowBooks.forEach(book => book.remove());

    for (let i = 0; i < cards.length; i++) {
        addCardToUI(cards[i]);
    }
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
    if (userLabel.dataset.role === "user") mainView.appendChild(createCardBtn);

    let buttonDiv = initOptionCard();
    mainView.appendChild(buttonDiv);

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
    searchCardForm.addEventListener("submit", async function (e) {
        e.preventDefault();
        let option = document.querySelector('input[name="option"]:checked')?.value;
        let target = document.querySelector(".searchInputCard").value.trim();
        if (target === "") {
            alert("Vui lòng nhập mã độc giả để tìm kiếm");
            return;
        }

        await searchCard(target, option);
    });
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
    expireElement.innerText = formateDate(book.expire);

    let returnDateElement = document.createElement("td");
    if (book.returnDate != null) returnDateElement.innerText = formateDate(book.returnDate);
    else returnDateElement.innerText = book.returnDate || "";

    let statusElement = document.createElement("td");
    if (book.status === "borrowing") statusElement.innerText = "Chưa trả";
    else statusElement.innerText = "Đã trả";

    let actionElement = document.createElement("td");
    if (book.status === "borrowing" && userLabel.dataset.role === "user") {
        let returnBtn = document.createElement("button");
        returnBtn.type = "button";
        returnBtn.classList.add("returnBtn");
        returnBtn.innerText = "Trả";
        actionElement.appendChild(returnBtn);
    }

    row.appendChild(bookIdElement);
    row.appendChild(bookNameElement);
    row.appendChild(expireElement);
    row.appendChild(returnDateElement);
    row.appendChild(statusElement);
    row.appendChild(actionElement);

    listBorrowBook.appendChild(row);
}

async function loadDetailCard(row) {
    currentCardId = row.dataset.borrowCardId;

    let cardDetail = await fetch("/api/borrowCard/getDetailCard?cardId=" + row.dataset.borrowCardId).then(res => res.json());

    cardIdElement.innerText = "ID: " + cardDetail.borrowCardId;
    userElement.innerText = "Người tạo: " + cardDetail.userName + " (ID: " + cardDetail.userId + ")";
    readerElement.innerText = "Độc giả: " + cardDetail.readerName + " (ID: " + cardDetail.readerId + ")";
    createdElement.innerText = "Ngày tạo: " + formateDate(cardDetail.createdAt);

    let books = cardDetail.books;
    for (let i = 0; i < books.length; i++) {
        addBorrowBook(books[i]);
    }

    totalAmountElement.innerText = formatTotal(cardDetail.totalAmount + "") + " đồng";
}

cancelCreateCard.addEventListener("click", function () {
    resetCreateCard();
    cardDiv.style.display = "none";
    mainView.style.display = "flex";
    createCardDiv.style.display = "none";
    mainView.style.display = "flex";
});

function addRow() {
    let row = document.createElement("tr");
    row.classList.add("rowBook");

    let bookIdElement = document.createElement("td");
    bookIdElement.contentEditable = true;

    let expireElement = document.createElement("td");
    let expireInput = document.createElement("input");
    expireInput.type = "date";
    expireElement.appendChild(expireInput);

    row.appendChild(bookIdElement);
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

    let books = await fetch("/api/book/searchBookName?name=" + target).then(res => res.json());
    for (let i = 0; i < books.length; i++) addBookSearch(books[i]);
});

function markItem(bookIds) {
    let rowBooks = document.querySelectorAll(".rowBook");
    rowBooks.forEach(book => {
        if (bookIds.includes((book.cells)[0].innerText) || bookIds.includes("invalidBook-" + (book.cells)[0].innerText)) book.style.backgroundColor = "#A9A9A9";
    });
}

acptCreateCard.addEventListener("click", async function () {
    let readerId = readerIdInput.value.trim();
    if (readerId === "") {
        alert("Vui lòng nhập mã độc giả");
        return;
    }

    let isError = false;
    let rowBooks = document.querySelectorAll(".rowBook");
    for (book of rowBooks) {
        if ((book.cells)[0].innerText.trim() === "" && (book.cells)[1].querySelector("input").value === "") {
            book.remove();
            continue;
        }

        if ((book.cells)[1].querySelector("input").value === "") {
            alert("Vui lòng nhập đủ thông tin sách mượn");
            isError = true;
            return;
        }

        if ((book.cells)[0].innerText.trim() === "") {
            alert("Vui lòng nhập đủ thông tin sách mượn");
            isError = true;
            return;
        }
    }
    if (isError) return;

    rowBooks = document.querySelectorAll(".rowBook");
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
    for (let i = 0; i < rowBooks.length; i++) {
        borrowBooks.push({
            borrowCardId: borrowCardId,
            bookId: (rowBooks[i].cells)[0].innerText,
            expire: (rowBooks[i].cells)[1].querySelector("input").value
        });
    }

    response = await fetch("/api/reader/addDetailCard", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            cardDetails: borrowBooks
        })
    }).then(res => res.json());

    if (response.length > 0) {
        console.log(response);
        if (response.includes("invalidBook")) {
            markItem(response);
            alert("Thông tin đầu sách không tồn tại, vui lòng thử lại");
        }
        else {
            markItem(response);
            alert("Số lượng đầu sách trong kho không đủ");
        }

        await fetch("/api/borrowCard/deleteCard", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(borrowCardId)
        });

        return;
    }

    resetCreateCard();

    alert("Tạo phiếu mượn thành công");
});

function resetCreateCard() {
    readerIdInput.value = "";
    searchBookInput.value = "";

    let borrowBooks = document.querySelectorAll(".rowBook");
    if (borrowBooks != null) borrowBooks.forEach(book => book.remove());

    let oldResult = document.querySelectorAll(".rowBookSearch");
    if (oldResult != null) oldResult.forEach(book => book.remove());
}