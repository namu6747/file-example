/**
 * 공통 함수
 * 2022-03-10
 */

/**
 * 숫자만 입력 받기
 */
const fnOnlyNumber = (obj) => {
    let regExp = new RegExp('^[0-9]*$');

    if (regExp.test(obj.value)==false) {
        obj.value = obj.value.replace(/[^0-9]/gi, '');
        return false;
    }
}

/**
 * 목록 checkbox 전체 선택/해제
 */
const fnCheckboxAll = () => {
    let chkAll = document.getElementById('chkAll');     //전체 선택 checkbox
    let checkboxes = document.getElementsByName('chk'); //row checkbox
    checkboxes.forEach((checkbox) => {
        checkbox.checked = chkAll.checked;
    });
}

//기간 검색
function fnChangePeriod(start, end) {
    document.getElementById("period").value = start.format('YYYY-MM-DD') + ' ~ ' + end.format('YYYY-MM-DD');
    document.getElementById("startDate").value = start.format('YYYY-MM-DD');
    document.getElementById("endDate").value = end.format('YYYY-MM-DD');
}

function fnKeepSession() {
    let h = (moment().format('HH'));
    let d = (moment().day());

    if ((h > 7 && h < 20) && (d > 0 && d < 6)) {
        fetch("/keep",{
            method: "GET"
        }).then(function(res){
            return res.text();
        });
    }
}

//쿠키 추가
function setCookie(key, value, obj) {
    let todayDate = new Date();
    //24 * 60 * 60 * 1000 하루
    todayDate.setTime(todayDate.getTime() + (obj * 1000));
    document.cookie = key + "=" + escape(value) + "; path=/; expires=" + todayDate.toGMTString() + ";";
}

//쿠키 조회
function getCookie(key){
    key = new RegExp(key + '=([^;]*)');
    return key.test(document.cookie) ? unescape(RegExp.$1) : '';
}

//검색 기간 문자열
function getPeriodStr() {
    let period = document.getElementById("period").value;
    return period.replace(' ~ ', '_');
}

//검색어 입력
function fnEnterKey(event, searchMethod){
    let key = event.key || event.keyCode;

    //검색어가 없는 경우 검색어 초기화 버튼 숨김
    showHideSearchReset();

    //엔터키 입력
    if ((key === 'Enter' && !event.shiftKey) || (key === 13 && key !== 16)) {
        event.preventDefault();
        //페이지별 검색 실행 함수
        searchMethod();
        return false;
    }
}

//검색어 초기화 버튼 실행
function fnSearchReset(searchMethod){
    //검색어 공백 처리
    document.getElementById('searchValue').value = "";
    //페이지별 검색 실행 함수
    searchMethod();
    //검색 초기화 버튼 표시/숨김
    showHideSearchReset();
    return false;
}

//검색어 초기화 버튼 표시/숨김 - 검색어가 공백이 아닐 경우
function showHideSearchReset(){
    if(document.getElementById('searchValue').value == ""){
        document.querySelector('.btn-reset').style.display = "none";
    }else{
        document.querySelector('.btn-reset').style.display = "";
    }
}

//로딩바 열고 닫기
function callLoadingView() {
    //로딩 화면 height, width
    let loadingViewHeight = window.document.body.clientHeight;
    let loadingViewWidth  = window.document.body.clientWidth;

    //로딩 화면 요소 생성
    let loadingView = "<div id='loadingView' style='position:absolute; z-index:10000; background-color:#000000;"
             + "display:none; left:0; top:0;'></div>";
    let loadingImg = "<img src='/images/loading.gif' style='position:absolute; top:50%; left:50%;'/>";

    $('body').append(loadingView)

    //로딩 화면 요소에 height, width 세팅 및 불투명도 조절
    $('#loadingView').css({
            'width' : loadingViewWidth,
            'height': loadingViewHeight,
            'opacity' : '0.3'
    });

    //로딩 화면 보이기
    $('#loadingView').show();

    //로딩 로고 보이기
    $('#loadingView').append(loadingImg);
    $('#loadingView').show();
}
function closeLoadingView() {
    $('#loadingView, #loadingImg').remove();
}

//입력 증명서 명이 영문인지 체크
function iniNameEngChk(iniName, iniCode){
    if(iniCode.startsWith(2)){
        return "(영문)" + iniName;
    } else {
        return "(국문)" + iniName;
    }
}

//서버에서 생성해준 CSRF 토큰 get 함수
function getCookie(str) {
    const value = document.cookie.match('(^|;) ?' + str + '=([^;]*)(;|$)');
    return value ? value[2] : null;
}
function getCsrfToken() {
    let token = getCookie("XSRF-TOKEN");
    return token;
}

