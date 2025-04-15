<script>
let backendURL = import.meta.env.VITE_BACKEND_URL;
let str_space = /\s/;

//회원가입 버튼 flag체크
let joinFlag = false;

let userIdFlag = false;
let userNameFlag = false;
let userPasswdFlag = false;
let userPasswd2Flag = false;
let userPhoneFlag = false;


//사용자 ID 조건분기 처리
let userId = "";
let userIdError = "";

function idCheck(){
    userId = userId.replace(str_space, "");

    if(0 < userId.length && userId.length < 7){
        userIdError = "길이가 총 7자 이상이여야 됩니다.";
        userIdFlag = false;
    }else if(userId.length == 0){
        userIdError = "";
        userIdFlag = false;
    }
    else{
        async function doGet(){
            const res = await fetch(backendURL+'/api/v1/user-api/userid?id=' + userId);
            const resJson = await res.json();
        
            if(resJson.header.statusCode === "00"){
                userIdFlag = false;
                userIdError = "이미 사용중인 아이디입니다."
                joinCheck();
            }else if(resJson.header.statusCode === "01"){
                userIdFlag = true;
                userIdError = "사용 가능한 아이디입니다."
                joinCheck();
            }
        }
        
        doGet();

        // async function doPost(){
        //     const res = await fetch('http://localhost:5052/userIdDupCheck',{
        //         method: 'POST',
        //         body : JSON.stringify({userId}),
        //         headers: {
        //             'content-type' : 'application/json'
        //         }
        //     });

        //     let response = await res.json();
            
        //     if(response.header.statusCode == "00"){
        //         userIdFlag = true;
        //         joinCheck();
        //     }
        // }

        // doPost().then((resJson) => {
        //     console.log(resJson);    
        // });
        // console.log('out1');
    }
}

//사용자 이름 조건분기 처리
let userName = "";
let userNameError = "";

function userNameCheck(){
    userName = userName.replace(str_space, "");

    if(userName.length == 0){
        userNameError = "이름이 비어있습니다.";
        userNameFlag = false;
    }else{
        userNameError = "";
        userNameFlag = true;
    }
    joinCheck();
}

//사용자 비밀번호 조건분기 처리
let userPasswd = "";
let userPasswdError = "";

function userPasswdCheck(){
    userPasswd = userPasswd.replace(str_space, "");

    let regexPw = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,}$/;

    if(!regexPw.test(userPasswd) && userPasswd.length > 0){
        userPasswdError = "영문, 숫자, 특수문자 조합 8자리 이상으로 되야합니다.";
        userPasswdFlag = false;
    }else{
        userPasswdError = "";
        userPasswdFlag = true;
    }

    if(userPasswd != userPasswd2 && userPasswd2.length != 0){
        userPasswd2Error = "비밀번호가 같지 않습니다.";
        userPasswd2Flag = false;
    }else{
        userPasswd2Error = "";
        userPasswd2Flag = true;
    }
    joinCheck();
}

//사용자 비밀번호 확인 조건분기 처리
let userPasswd2 = "";
let userPasswd2Error = "";

function userPasswd2Check(){
    userPasswd2 = userPasswd2.replace(str_space, "");

    if(userPasswd != userPasswd2 && userPasswd2.length != 0){
        userPasswd2Error = "비밀번호가 같지 않습니다.";
        userPasswd2Flag = false;
    }else{
        userPasswd2Error = "";
        userPasswd2Flag = true;
    }
    joinCheck();
}

//사용자 핸드폰번호 조건분기 처리
let userPhone = "";
let userPhoneError = "";

function userPhoneCheck(){
    userPhone = userPhone.replace(/[^0-9]/g, '').replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3").replace(/(\-{1,2})$/g, "");
    
    if(userPhone.length == 0){
        userPhoneError = "핸드폰 번호가 비어있습니다."
        userPhoneFlag = false;
    }else if(userPhone.length != 13 || !userPhone.includes('-')){
        userPhoneError = "핸드폰 번호 길이가 틀립니다."
        userPhoneFlag = false;
    }else{
        userPhoneError = "";
        userPhoneFlag = true;
    }
    joinCheck();
}

//회원가입 가능 유무 체크
function joinCheck(){
    if(userIdFlag && userNameFlag && userPasswdFlag && userPasswd2Flag && userPhoneFlag){
        joinFlag = true;
    }else{
        joinFlag = false;
    }
}

</script>

<div class="container-join">
  <div class="">
    <div class="rounded d-flex remove-attr justify-content-center">
      <div>
        <form action="{backendURL}/api/v1/user-api/join" method="POST">
          <div class="p-5">
            <div class="input-group">
              <span class="input-group-text bg-dark"><i class="bi bi-emoji-smile-fill text-white"/></span>
              <input type="text" name="userId" class="form-control" placeholder="사용자 아이디" bind:value={userId} on:keyup={idCheck} autocomplete="off"/>
            </div>
            <div class="error">{userIdError}</div>
            <div class="input-group">
              <span class="input-group-text bg-dark"><i class="bi bi-person-plus-fill text-white"/></span>
              <input type="text" name="name" class="form-control" placeholder="이름" bind:value={userName} on:keyup={userNameCheck} autocomplete="off"/>
            </div>
            <div class="error">{userNameError}</div>
            <div class="input-group"><span class="input-group-text bg-dark"><i class="bi bi-key text-white" /></span>
              <input type="password" name="passwd" class="form-control" placeholder="비밀번호" bind:value={userPasswd} on:keyup={userPasswdCheck} autocomplete="off"/>
            </div>
            <div class="error">{userPasswdError}</div>
            <div class="input-group"><span class="input-group-text bg-dark"><i class="bi bi-key-fill text-white"/></span>
              <input type="password" name="passwd2" class="form-control" placeholder="비밀번호 확인" bind:value={userPasswd2} on:keyup={userPasswd2Check} autocomplete="off"/>
            </div>
            <div class="error">{userPasswd2Error}</div>
            <div class="input-group"><span class="input-group-text bg-dark"><i class="bi bi-telephone-fill text-white" /></span>
              <input type="text" name="phone" class="form-control" placeholder="핸드폰 번호" bind:value={userPhone} on:keyup={userPhoneCheck} autocomplete="off"/>
            </div>
            <div class="error">{userPhoneError}</div>
            <button class="btn btn-dark text-center mt-2" type="submit" disabled={!joinFlag}>생성완료</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>

<style>
    .container-join{
    width : 100%;
}

.input-group-text{
    height:38px;
}

.input-group{
    display: flex;
    width: 330px;
    height: 38px;
}

.btn{
    width: 330px;
}

.error{
    font-size: 12px;
    height: 30px;
    color: red;
    font-weight: 700;
    margin-left: 45px;
}

@media only screen and (max-width: 576px){
    .container-join{
        width: 100%;
        padding: 0;
    }

    .btn{
        width: 100%;
    }

    .input-group{
        display: flex;
        width: 100%;
        height: 38px;
    }

    .remove-attr{
        width: 100%;
    }
}
</style>