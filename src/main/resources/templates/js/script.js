const searchBoxContainer = document.querySelector(".search-box-container");
const searchBox = document.querySelector("#search-box");
const contextBox = document.getElementById("context-box");

// 입력한 메시지를 처리하는 함수
searchBox.addEventListener("keypress", function(event) {
    if (event.key === "Enter" && searchBox.value.trim() !== "") {
        addMessage(searchBox.value, 'user');
        searchBox.value = ''; // 입력창 초기화
        // 가상의 봇 메시지 추가
        setTimeout(() => {
            addMessage("이건 봇의 응답입니다.", 'bot');
        }, 500);
    }
});

// 메시지를 추가하는 함수
function addMessage(message, sender) {
    const messageElement = document.createElement("div");
    messageElement.classList.add("message", sender);
    messageElement.textContent = message;
    contextBox.appendChild(messageElement);
    contextBox.scrollTop = contextBox.scrollHeight; // 스크롤을 가장 아래로 이동
}

// 검색창에 포커스가 가면 화면이 변경되도록 처리
searchBox.addEventListener("focus", () => {
    searchBoxContainer.classList.add("moved");
    showContextBox();
});

// context-box 보이게 하는 함수
function showContextBox() {
    contextBox.style.display = "flex";
}