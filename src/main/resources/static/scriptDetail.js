const goBackBtn = document.getElementById("goBackBtn");
const editBtn = document.getElementById("editBtn");
const deleteBtn = document.getElementById("deleteBtn");
const userIdLoginP = document.getElementById("userIdLoginP");
const mainView = document.querySelector(".main-view-detail-book");
const inforDiv = document.querySelector(".infor-div");
const goHomeBtn = document.getElementById("goHomeBtn");

goHomeBtn.style.cursor = "default";
inforDiv.style.display = "none";

if (userIdLoginP.dataset.role === "user") {
    editBtn.style.display = "none";
    deleteBtn.style.display = "none";
}

goBackBtn.addEventListener("click", function () {
    window.history.back();
});

deleteBtn.addEventListener("click", async function () {
    let acptDelete = confirm("Bạn có chắc chắn muốn xóa đầu sách này?");
    if (acptDelete) {
        let response = await fetch("/api/book/deleteBook", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                bookId: deleteBtn.dataset.bookId
            })
        }).then(res => res.text());

        if (response === "false") {
            alert("Đầu sách đang có trong phiếu mượn, không thể xóa");
            return;
        }

        alert("Xóa đầu sách thành công");
    }
});

function loadEditBookForm(book) {
    let name = document.getElementById("bookName");
    name.value = book.name;

    let author = document.getElementById("author");
    author.value = book.author;

    let description = document.getElementById("desc");
    description.value = book.description;

    let publish = document.getElementById("publish");
    publish.value = book.publish;

    let type = document.getElementById("type");
    type.value = book.type;

    let quantity = document.getElementById("qty");
    quantity.value = book.quantity;

    let imgInput = document.getElementById("imgInput");
    imgInput.dataset.urlImg = book.urlImg;
}

editBtn.addEventListener("click", async function () {
    let bookData = await fetch("/api/book/searchBookId?bookId=" + editBtn.dataset.bookId).then(res => res.json());
    inforDiv.style.display = "flex";
    loadEditBookForm(bookData);
});

inforDiv.addEventListener("click", async function (e) {
    if (e.target.classList.contains("acptBtn")) {
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
            return;
        }

        if (author === "") {
            authorInput.focus();
            alert("Vui lòng nhập đủ thông tin");
            return;
        }

        if (publish === "") {
            publishInput.focus();
            alert("Vui lòng nhập đủ thông tin");
            return;
        }

        if (description === "") {
            descInput.focus();
            alert("Vui lòng nhập đủ thông tin");
            return;
        }

        if (type === "") {
            typeInput.focus();
            alert("Vui lòng nhập đủ thông tin");
            return;
        }

        if (quantity === "") {
            qtyInput.focus();
            alert("Vui lòng nhập đủ thông tin");
            return;
        }

        if (files.length === 0) {
            await fetch("/api/book/updateBook", {
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
                    urlImg: imgInput.dataset.urlImg
                })
            });
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

            await fetch("/api/book/updateBook", {
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

        inforDiv.style.display = "none";
        window.location.reload();
    }

    else if (e.target.classList.contains("closeBtn")) {
        inforDiv.style.display = "none";
    }
});