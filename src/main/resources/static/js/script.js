const url = "/stream"; // Spring Boot 스트리밍 엔드포인트

// DOM 요소 가져오기
const searchBoxContainer = document.querySelector(".search-box-container");
const searchBox = document.querySelector("#search-box");
const contextBox = document.getElementById("context-box");
const loadingText = document.querySelector(".loading-container p");

// 검색창에 포커스가 가면 화면이 변경되도록 처리
searchBox.addEventListener("focus", () => {
    searchBoxContainer.classList.add("moved"); // 화면 변경 클래스 추가
    showContextBox();
});

// context-box 보이게 하는 함수
function showContextBox() {
    contextBox.style.display = "flex";
}

// 입력한 메시지를 처리하는 함수
searchBox.addEventListener("keypress", function (event) {
    if (event.key === "Enter" && searchBox.value.trim() !== "") {
        const userMessage = searchBox.value.trim(); // 사용자가 입력한 메시지

        addMessage(userMessage, "user"); // 사용자 메시지 화면에 추가
        searchBox.value = ""; // 입력창 초기화

        showLoading("데이터를 가져오는 중입니다..."); // 로딩 애니메이션 표시

        // 서버로 메시지를 전송하고 응답 받기
        fetch("http://localhost:8080/agent?query=" + userMessage, {
            method: "GET",
            headers: {
                "Content-Type": "text/plain", // 서버로 전송하는 데이터 형식은 텍스트
            },
        })
            .then((response) => response.text()) // 서버에서 텍스트 형식으로 응답 받기
            .then((data) => {
                // 서버에서 받은 텍스트 응답을 봇 메시지로 화면에 추가
                addMessage(data, "bot");
            })
            .catch((error) => {
                console.error("Error:", error);
                addMessage(
                    "서버와의 연결에 문제가 발생했습니다. 다시 시도해주세요.",
                    "bot"
                );
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

// 로딩 애니메이션 보이기
function showLoading(statusMessage) {
    loadingText.textContent = statusMessage; // 로딩 메시지 업데이트
    loadingContainer.style.display = "flex"; // 로딩 컨테이너 보이기
}

// 로딩 애니메이션 숨기기
function hideLoading() {
    loadingContainer.style.display = "none"; // 로딩 컨테이너 숨기기
}

// Fetch API로 스트리밍 데이터 수신
fetch(url)
    .then((response) => {
        if (!response.body) {
            throw new Error("ReadableStream is not supported in this browser.");
        }
        const reader = response.body.getReader();
        const decoder = new TextDecoder("utf-8");

        function updateLoadingText(stage, customMessage = null) {
            // 단계에 맞는 텍스트 업데이트
            const stages = {
                "1": "네이버 검색 중...",
                "2": "데이터 요약 중...",
                "3": "데이터 번역 중...",
                "4": "데이터 취합 중...",
            };

            if (stage === "0" && customMessage) {
                loadingText.textContent = customMessage;
            } else if (stages[stage]) {
                loadingText.textContent = stages[stage];
            } else {
                loadingText.textContent = "단계를 식별할 수 없습니다.";
            }
        }

        function readStream() {
            return reader.read().then(({ done, value }) => {
                if (done) {
                    console.log("Stream complete.");
                    loadingText.textContent = "완료!";
                    return;
                }
                // 수신된 데이터를 처리
                const data = decoder.decode(value, { stream: true }).trim();
                console.log("Received:", data);

                // 0인 경우 메시지 분리
                if (data.startsWith("0:")) {
                    const customMessage = data.split(":")[1].trim();
                    updateLoadingText("0", customMessage);
                } else {
                    updateLoadingText(data);
                }
                return readStream();
            });
        }

        return readStream();
    })
    .catch((error) => {
        console.error("Error:", error);
        loadingText.textContent = "오류 발생!";
    });
