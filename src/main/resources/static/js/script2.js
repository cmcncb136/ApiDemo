const searchBoxContainer = document.querySelector(".search-box-container");
const searchBox = document.querySelector("#search-box");
const contextBoxContainer = document.querySelector("#context-box");
const buttonContainer = document.querySelector("#button-container");
const buttons = document.querySelectorAll(".round-button");


for (let i = 0; i < 5; i++){
    buttons[i].addEventListener('click', x => { keywordSearch(
        buttons[0].textContent)})

}

// 페이지 로드 시 바로 서버에서 데이터를 받아와서 버튼 텍스트를 변경
window.addEventListener("DOMContentLoaded", () => {
    fetchSearchResults(); // 페이지 로드 후 서버에 데이터 요청
});

// 서버에서 검색 결과를 받아오는 함수
function fetchSearchResults() {
    fetch("http://localhost:8080/search?languageCode=" + "en", { // 여기에 서버 URL을 입력
        method: 'GET',
        headers: {
            "Content-Type": "text/plain"
        }
    })
        .then(response => response.json())  // 응답을 JSON 형식으로 변환
        .then(data => {
            updateButtonLabels(data);  // 받은 데이터로 버튼 텍스트 업데이트
            //showContextBox(data);      // 받은 데이터로 contextBox 업데이트
        })
        .catch(error => {
            console.error("Error fetching search results:", error);
        });
}

// 서버에서 받은 데이터로 버튼의 텍스트를 변경하는 함수
function updateButtonLabels(dataFromServer) {
    // 첫 번째부터 다섯 번째까지의 버튼 텍스트를 서버에서 받은 데이터로 설정
    for (let i = 0; i < 5 && i < dataFromServer.length; i++) {
        const button = buttons[i];
        button.textContent = dataFromServer[i]; // 각 데이터 항목의 title을 버튼 텍스트로 설정
    }
}

// 검색창에 포커스가 가면 화면이 변경되도록 처리
searchBox.addEventListener("focus", () => {
    searchBoxContainer.classList.add("moved");
});

// Enter 키 입력 시 서버에 요청 보내고, 결과 받아서 화면에 뿌리기
searchBox.addEventListener("keydown", function (event) {
    if (event.key === "Enter") {
        const searchTerm = searchBox.value.trim();
        /*showContextBox();*/
        if (searchTerm) {
            fetchSearchResults(searchTerm);
        }
    }
});

// 서버에서 검색 결과를 받아오는 함수
function keywordSearch(query) {
    fetch("http://localhost:8080/search/data?keyword=" + query, { // 여기 바꿔야 함.
        method: 'GET', // POST 요청 사용
        headers: {
            "Content-Type": "text/plain"
        },
    })
        .then(response => showSearchResults(response))  // 응답을 JSON 형식으로 변환)
        .catch(error => {
            console.error("Error fetching search results:", error);
        });
}

/*// 서버에서 받은 데이터 (예시)
const dataFromServer =
    [{
        "title" : "가족과 함께하는 제주도 여행",
        "keywords" : ["제주도", "가족 여행", "렌터카", "휘발유 가격", "서귀포", "휘게호텔", "온수 수영장", "헬로우키티 랜드", "화조원", "미쁜제과", "트릭아이 미술관", "응급실"],
        "content" : "이번 제주도 여행은 가족 모두에게 소중한 경험이었습니다. 우리는 제주OK렌터카에서 차량을 대여하여 편리하게 이동했습니다. 하지만 여행 중 휘발유 가격이 상승한 점이 아쉬웠습니다. 숙소로는 서귀포에 위치한 휘게호텔을 선택했는데, 옥상에 있는 온수 수영장이 특히 좋았습니다. 아이들과 함께 헬로우키티 랜드와 화조원에서 즐거운 시간을 보냈고, 가족 모두가 즐길 수 있는 다양한 액티비티가 있었습니다. 또한, 미쁜제과에서 맛있는 베이커리를 즐겼고, 트릭아이 미술관에서 재미있는 사진을 많이 찍어 가족의 소중한 추억을 만들었습니다. 하지만 여행 중 아이가 아파서 응급실에 가는 어려움도 겪었습니다. 그럼에도 불구하고 많은 즐거운 기억이 깃든 여행이었습니다.",
        "highlight" : ["제주OK렌터카", "휘발유 가격 상승", "휘게호텔 옥상 온수 수영장", "헬로우키티 랜드 방문", "화조원", "미쁜제과 베이커리", "트릭아이 미술관에서 사진 촬영", "응급실 방문"],
        "isAd" : false
    }, {
        "title" : "아이와 함께하는 제주도 화산 탐험 여행",
        "keywords" : ["제주도", "화산 지형", "아이 교육", "시청각 자료", "여행 경험"],
        "content" : "알파맘님은 아이의 세 번째 제주도 여행을 특별하게 만들기 위해 화산 지형의 형성과 관련된 다양한 시청각 자료를 준비했습니다. 전문가 자료와 함께 흥미로운 유튜브 동영상을 공유하여 아이들이 제주도의 독특한 지질을 학습할 수 있는 기회를 제공합니다. 비행기 여행 중 해당 자료에 관심이 있는 분들은 댓글을 남기면 자동으로 자료를 전달받을 수 있습니다.",
        "highlight" : ["전문 자료 제공", "교육적인 요소 포함", "시청각 자료 활용", "YTN 및 제주도교육청 자료"],
        "isAd" : false
    }, {
        "title" : "아이와 함께하는 제주도 여행 추천지: 제주민속촌과 제주홀릭뮤지엄",
        "keywords" : ["제주도", "아이 여행", "관광지", "제주민속촌", "제주홀릭뮤지엄", "문화 체험"],
        "content" : "제주도에서 아이와 함께할 수 있는 최고의 관광지로 제주민속촌과 제주홀릭뮤지엄을 추천합니다. 제주민속촌은 09:30~18:00 운영되며 전통 문화와 건축 양식을 체험할 수 있는 공간으로, 서귀포에 위치해 다른 관광지와의 연계도 용이합니다. 입장요금은 성인 15,000원, 청소년 12,000원, 소인은 11,000원입니다. 한편 제주홀릭뮤지엄은 5개의 전시관으로 구성되어 있어 다양한 체험과 정보 제공이 가능하며, 운영시간은 09:30~18:00(관람 마감 19:00)로 가족 단위 방문객들에게 특히 인기가 많습니다. 이곳은 입장요금이 성인 11,000원, 청소년 10,000원, 소인은 8,000원으로 경제적입니다. 두 곳 모두 아이와 함께 방문하기에 적합하여 겨울철 실내 활동으로 추천됩니다.",
        "highlight" : ["제주민속촌: 전통 문화 체험", "제주홀릭뮤지엄: 다양한 체험과 정보 제공", "가족 단위 방문객에게 인기"],
        "isAd" : false
    }, {
        "title": "12월 제주도 여행 추천지",
        "keywords": ["제주도", "여행 추천", "동백꽃", "귤체험", "겨울 여행"],
        "content": "안녕하세요, 여행인플루언서 사라하보입니다. 12월 제주도 여행 추천지 몇 곳을 소개할게요. 제주동백수목원에서는 아름다운 동백꽃을 감상할 수 있으며, 1100고지 휴게소에서는 눈꽃을 즐길 수 있습니다. 겨울에는 감귤 수확 체험도 가능하고, 용머리해안에서는 화산 활동으로 형성된 아름다운 경치를 즐길 수 있습니다. 이 코스들이 가족여행에 도움이 되길 바라며, 즐거운 제주도 여행 되세요!",
        "highlight": ["제주동백수목원", "1100고지 휴게소", "감귤체험", "용머리해안"],
        "isAd": false
    }];*/

// contextBox에 리스트 항목들을 추가하는 함수
function showContextBox(dataFromServer) {
    // 기존 항목들 초기화
    contextBoxContainer.innerHTML = "";

    // 서버에서 받은 데이터를 하나씩 처리하여 div에 넣기
    dataFromServer.forEach(item => {
        const contextItem = document.createElement("div");
        contextItem.classList.add("context-box-item");

        // 각 아이템의 title을 출력 (content나 다른 속성도 가능)
        contextItem.innerHTML = `
            <h2>${item.title}</h2>
            <p>${item.content}</p>
        `;

        contextBoxContainer.appendChild(contextItem);
    });
}

// 받은 검색 결과로 contextBox 업데이트하는 함수
function showSearchResults(dataFromServer) {
    // 기존 항목들 초기화
    contextBoxContainer.innerHTML = "";

    dataFromServer = dataFromServer.text();
    console.log("data : " + dataFromServer)

    // 서버에서 받은 데이터를 하나씩 처리하여 div에 넣기
    dataFromServer.forEach(item => {
        const contextItem = document.createElement("div");
        contextItem.classList.add("context-box-item");

        // 각 아이템의 title을 출력 (content나 다른 속성도 가능)
        contextItem.innerHTML = `
            <h2>${item.title}</h2>
            <p>${item.content}</p>
            <p><strong>Keywords:</strong> ${item.keywords.join(", ")}</p>
            <p><strong>Highlights:</strong> ${item.highlight.join(", ")}</p>
        `;

        contextBoxContainer.appendChild(contextItem);
    });
}
