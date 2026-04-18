/**
 * Xử lý sự kiện nhấn nút "Trả" trong trang quản lý phiếu mượn
 */

let pendingReturns = {};
const FINE_PER_DAY = 2000;
const totalAmountElement = document.getElementById("totalAmount");

function formateDate(date) {
    let tmp = date.split("T");
    let tmp2 = tmp[0].split("-");
    return `${tmp2[2]}/${tmp2[1]}/${tmp2[0]} ${tmp[1]}`;
}

async function loadDetailCardForReturn(cardId) {
    let rowBook = document.querySelectorAll(".rowBook");
    rowBook.forEach(book => book.remove());
    currentCardId = cardId;
    pendingReturns = {};

    let cardDetail = await fetch("/api/borrowCard/getDetailCard?cardId=" + cardId).then(res => res.json());

    cardIdElement.innerText = "ID: " + cardDetail.borrowCardId;
    userElement.innerText = "Người tạo: " + cardDetail.userName + " (ID: " + cardDetail.userId + ")";
    readerElement.innerText = "Độc giả: " + cardDetail.readerName + " (ID: " + cardDetail.readerId + ")";
    createdElement.innerText = "Ngày tạo: " + cardDetail.createdAt;
    totalAmountElement.innerText = cardDetail.totalAmount + ' đồng';

    let books = cardDetail.books;
    for (let i = 0; i < books.length; i++) {
        addBorrowBook(books[i]);
    }

    mainView.style.display = "none";
    cardDiv.style.display = "flex";
    updateTotalFine();
}

document.addEventListener("click", async function (e) {
    if (e.target.tagName !== "BUTTON" || !e.target.classList.contains("returnBtn")) return;

    const returnState = e.target.dataset.returnState;

    if (returnState === "pending") {
        cancelIndividualReturn(e.target);
    } else {
        handleReturnBook(e.target);
    }
});

function calculateFine(expireDateStr) {
    if (!expireDateStr || expireDateStr.trim() === "") {
        return 0;
    }

    let expireDate;
    const originalStr = expireDateStr.trim();
    const dateOnly = originalStr.split(" ")[0];

    if (dateOnly.includes("/")) {
        const parts = dateOnly.split("/");
        expireDate = new Date(parts[2], parts[1] - 1, parts[0]);
    } else if (dateOnly.includes("-")) {
        expireDate = new Date(dateOnly);
    } else {
        return 0;
    }

    const today = new Date();
    today.setHours(0, 0, 0, 0);
    expireDate.setHours(0, 0, 0, 0);

    const timeDiff = today - expireDate;
    const daysDiff = Math.ceil(timeDiff / (1000 * 60 * 60 * 24));

    if (daysDiff <= 0) {
        return 0;
    }

    return daysDiff * FINE_PER_DAY;
}

function updateTotalFine() {
    let totalFine = 0;

    Object.keys(pendingReturns).forEach(returnKey => {
        totalFine += pendingReturns[returnKey].fine;
    });

    // Tạo hoặc lấy div hiển thị tiền phạt lần này
    let pendingFineDiv = document.getElementById("pendingFineDiv");
    if (!pendingFineDiv) {
        pendingFineDiv = document.createElement("div");
        pendingFineDiv.id = "pendingFineDiv";

        const label = document.createElement("label");
        label.innerHTML = '<strong>Tiền phạt lần này: </strong>';

        const fineValue = document.createElement("span");
        fineValue.id = "totalFineValue";

        pendingFineDiv.appendChild(label);
        pendingFineDiv.appendChild(fineValue);
        totalAmountElement.parentElement.parentElement.insertBefore(pendingFineDiv, totalAmountElement.parentElement.nextSibling);
    }

    if (totalFine > 0) {
        pendingFineDiv.style.display = "block";
        document.getElementById("totalFineValue").innerHTML = "<strong>" + totalFine + " đồng</strong>";
    } else {
        pendingFineDiv.style.display = "none";
    }
}

function handleReturnBook(button) {
    const row = button.closest("tr");
    const bookId = row.cells[0].innerText.trim();
    const expireDate = row.cells[1].innerText.trim();

    const borrowCardId = currentCardId;
    const fine = calculateFine(expireDate);
    const returnKey = `${borrowCardId}_${bookId}`;

    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const seconds = String(now.getSeconds()).padStart(2, '0');

    const returnDate = `${day}/${month}/${year} ${hours}:${minutes}:${seconds}`;

    pendingReturns[returnKey] = {
        borrowCardId: parseInt(borrowCardId),
        bookId: parseInt(bookId),
        fine: fine,
        row: row,
        button: button,
        returnDate: returnDate,
        originalReturnDateCell: row.cells[3].innerText
    };

    button.innerText = "Chờ xác nhận";
    button.dataset.returnState = "pending";
    button.dataset.returnKey = returnKey;
    row.cells[3].innerText = returnDate;

    updateTotalFine();
}

function cancelIndividualReturn(button) {
    const row = button.closest("tr");
    if (!row) return;

    const returnKey = button.dataset.returnKey;
    if (!returnKey || !pendingReturns[returnKey]) return;

    const item = pendingReturns[returnKey];

    button.innerText = "Trả";
    delete button.dataset.returnState;
    delete button.dataset.returnKey;
    row.cells[3].innerText = item.originalReturnDateCell;

    delete pendingReturns[returnKey];
    updateTotalFine();
}

async function confirmAllReturns() {
    if (Object.keys(pendingReturns).length === 0) {
        alert("Không có sách chờ xác nhận");
        return;
    }

    let totalFineConfirmed = 0;

    try {

        for (const returnKey in pendingReturns) {
            const item = pendingReturns[returnKey];
            const parts = item.returnDate.split(" ");
            const dateParts = parts[0].split("/");
            const formattedDate = `${dateParts[2]}-${dateParts[1]}-${dateParts[0]} ${parts[1]}`;

            const response = await fetch("/api/borrowCard/returnBook", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    borrowCardId: item.borrowCardId,
                    bookId: item.bookId,
                    returnDate: formattedDate,
                    fine: item.fine
                })
            });

            const result = await response.json();

            if (result.success) {
                item.row.cells[3].innerText = item.returnDate;
                item.row.cells[4].innerText = "Đã trả";
                item.button.remove();
                item.row.style.opacity = "0.6";
                item.row.style.backgroundColor = "";
                totalFineConfirmed += item.fine;
                delete pendingReturns[returnKey];
            } else {
                alert("Lỗi trả sách " + item.bookId + ": " + result.message);
            }
        }

        alert("Xác nhận trả sách hoàn tất!");
        loadDetailCardForReturn(currentCardId);

    } catch (error) {
        console.error("Lỗi:", error);
        alert("Có lỗi xảy ra: " + error.message);
    }
}

function cancelAllReturns() {
    Object.keys(pendingReturns).forEach(returnKey => {
        const item = pendingReturns[returnKey];
        item.button.style.backgroundColor = "#4CAF50";
        item.button.innerText = "Trả";
        delete item.button.dataset.returnState;
        delete item.button.dataset.returnKey;
        item.row.style.backgroundColor = "";
        item.row.cells[3].innerText = item.originalReturnDateCell;
    });

    pendingReturns = {};
    updateTotalFine();

    alert("Đã huỷ tất cả");
}

// Gán hàm confirmAllReturns cho nút xác nhận
document.addEventListener("DOMContentLoaded", function () {
    const acptBtn = document.getElementById("acpt");
    if (acptBtn) {
        acptBtn.addEventListener("click", async function () {
            await confirmAllReturns();
        });
    }

    if (goBackBtn) {
        goBackBtn.addEventListener("click", function () {
            resetCreateCard();
            console.log("hello");
            cardDiv.style.display = "none";
            mainView.style.display = "flex";
            pendingReturns = {};
            updateTotalFine();
        });
    }
});