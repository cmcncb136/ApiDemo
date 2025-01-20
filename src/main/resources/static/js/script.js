const searchBoxContainer = document.querySelector(".search-box-container");
const searchBox = document.querySelector("#search-box");
const contextBox = document.getElementById("context-box");
const loadingContainer = document.getElementById("loading-container"); // 로딩 컨테이너
const loadingText = document.getElementById("loading-text"); // 로딩 메시지 텍스트

// 입력한 메시지를 처리하는 함수
searchBox.addEventListener("keypress", function (event) {
    if (event.key === "Enter" && searchBox.value.trim() !== "") {
        const userMessage = searchBox.value.trim(); // 사용자가 입력한 메시지

        addMessage(userMessage, 'user'); // 사용자 메시지 화면에 추가
        searchBox.value = ''; // 입력창 초기화

        // 작업 내용을 동적으로 설정
        const taskDescription = getTaskDescription(userMessage);
        showLoading(taskDescription); // 로딩 애니메이션 및 메시지 표시

        // 서버로 메시지를 전송하고 응답 받기
        fetch("http://localhost:8080/agent?query=" + userMessage, {  // 실제 서버 URL로 수정
            method: "GET",
            headers: {
                "Content-Type": "text/plain", // 서버로 전송하는 데이터 형식은 텍스트
            },
        })
            .then(response => response.text())  // 서버에서 텍스트 형식으로 응답 받기
            .then(data => {
                // 서버에서 받은 텍스트 응답을 봇 메시지로 화면에 추가
                addMessage(data, 'bot');
            })
            .catch(error => {
                console.error("Error:", error);
                addMessage("서버와의 연결에 문제가 발생했습니다. 다시 시도해주세요.", 'bot');
            })
            .finally(() => {
                hideLoading(); // 로딩 애니메이션 숨기기
            });
    }
});

// 메시지를 추가하는 함수
function addMessage(message, sender) {
    const messageElement = document.createElement("div");
    messageElement.classList.add("message", sender);
    messageElement.innerHTML = message; // HTML을 포함한 메시지 추가

    contextBox.appendChild(messageElement); // 메시지를 contextBox에 추가
    contextBox.scrollTop = contextBox.scrollHeight; // 스크롤을 가장 아래로 이동
}

// 로딩 애니메이션과 메시지를 보이게 하는 함수
function showLoading(taskDescription) {
    loadingText.textContent = taskDescription; // 로딩 메시지 설정
    loadingContainer.style.display = "flex"; // 로딩 컨테이너 표시
}

// 로딩 애니메이션과 메시지를 숨기는 함수
function hideLoading() {
    loadingContainer.style.display = "none";
}

// 사용자의 메시지에 따라 작업 내용을 반환하는 함수
function getTaskDescription(userMessage) {
    if (userMessage.includes("번역")) {
        return "번역 작업을 수행 중입니다...";
    } else if (userMessage.includes("네이버")) {
        return "네이버 API에서 데이터를 가져오는 중입니다...";
    } else if (userMessage.includes("GPT")) {
        return "GPT 결과를 생성하는 중입니다...";
    } else {
        return "요청을 처리 중입니다...";
    }
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
