const searchBoxContainer = document.querySelector(".search-box-container");
const searchBox = document.querySelector("#search-box");
const contextBox = document.getElementById("context-box");

// 입력한 메시지를 처리하는 함수
searchBox.addEventListener("keypress", function(event) {
    if (event.key === "Enter" && searchBox.value.trim() !== "") {
        const userMessage = searchBox.value.trim(); // 사용자가 입력한 메시지

        addMessage(userMessage, 'user'); // 사용자 메시지 화면에 추가
        searchBox.value = ''; // 입력창 초기화

        // 서버로 메시지를 전송하고 응답 받기
        fetch("https://yourserver.com/api/chat", {
            method: "POST",
            headers: {
                "Content-Type": "text/plain", // String 형식으로 전송
            },
            body: userMessage, // 사용자 메시지 서버로 전송
        })
            .then(response => {
                // 임의의 데이터를 반환 (예: "서버 응답: <사용자 입력>")
                return new Promise((resolve) => {
                    setTimeout(() => {
                        resolve("임의의 봇 응답: <a href='https://www.naver.com' target='_blank'>네이버로 이동하기</a>");
                    }, 500); // 500ms 후에 응답 처리
                });
            })
            .then(data => {
                // 서버에서 받은 응답을 봇 메시지로 화면에 추가
                addMessage(data, 'bot', true);
            })
            .catch(error => {
                console.error("Error:", error);
                addMessage("서버와의 연결에 문제가 발생했습니다. 다시 시도해주세요.", 'bot');
            });
    }
});

// 메시지를 추가하는 함수
function addMessage(message, sender, isHTML = false) {
    const messageElement = document.createElement("div");
    messageElement.classList.add("message", sender);

    if (isHTML) {
        messageElement.innerHTML = message; // HTML을 포함한 메시지 추가
    } else {
        messageElement.textContent = message; // 텍스트만 추가
    }

    contextBox.appendChild(messageElement); // 메시지를 contextBox에 추가
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