<script>
    import { redirect } from '@sveltejs/kit';
    import Cookies from 'js-cookie';

    let backendURL = import.meta.env.VITE_BACKEND_URL;
    async function postLogin(body){
        const response = await fetch(backendURL + '/api/v1/user-api/login', {
            method:'POST',
            credentials: 'include', //Credential옵션을 키면 백엔드에게 cookie의 헤더값을 담아서 던진다.
            body : body,
        });
        return response;
    }

    // async function getSessionId(){
    //     const response = await fetch(backendURL + '/api/v1/user-api/login', {
    //         method:'GET',
    //         credentials: 'include',
    //     });
    //     return await response;
    // }

    async function handleSubmit(event) {
        event.preventDefault();
        const formData = new FormData(event.target);

        // >>> 테스트만하고 삭제해도됌(RSA 암복호화임)
        // let passwd = formData.get('passwd');
        // console.log("TEST FOR ORGPASS",passwd);
        // var rsa = new RSAKey();
        // rsa.setPublic('86da4ee16c696a5eebe3b38010ccf2c888252f82b718eba3a5acface881cb0f01996ca71d95b1ef8a496c79ac89f92f1ca5f8253749537ec705e9c99ac4b7c0c765f7a634b1a711e6243f82502b629eef111409bdd694a6316ea8c1049fd80698e486f214f948457321dd068ddc1a56952a6bb9144f84fb240c0b56ad3967b05', '10001');
        // let encPasswd = rsa.encrypt(passwd);
        // console.log("TEST FOR ENCPASS", encPasswd);
        // const response = await fetch('http://localhost:8282/molit/road/update', {
        //     method:'POST',
        // });
        // return;
        // <<< 테스트만하고 삭제

        const body = {
            userId: formData.get('userId'),
            passwd: formData.get('passwd'),
        };
    
        const bodyFormData = new FormData();
        
        bodyFormData.append("userId", body.userId);
        bodyFormData.append("passwd", body.passwd);

        try {
            let response = await postLogin(bodyFormData);
            //response = await getSessionId();
            if (response.ok) {
                let resJson = await response.json();
                console.log(resJson);
                //로그인 실패
                if(resJson.errorCode == "001"){
                    alert("정보가 일치하지 않습니다.");
                }
                //로그인 성공
                else{
                    window.localStorage.setItem("accessToken", resJson.accessToken);
                    window.localStorage.setItem("grantType", resJson.grantType);
                    window.localStorage.setItem("refreshToken", resJson.refreshToken);

                    window.location.href="/home";                   
                }    
            }
        } catch (error) {
            console.log("error", error);
        // Handle network error
        }
    }
</script>

<div class="container-login">
    <div class="d-flow justify-content-center">
        <div class="d-flex justify-content-center">
            <div class="login-dev">
                <form class="login-form" on:submit={handleSubmit}>
                    <!-- <input type="hidden" name="credentials" value="import"> -->
                    <div class="p-5 input-form-div">
                        <div class="input-group mb-3">
                            <span class="input-group-text bg-dark"><i class="bi bi-person-plus-fill text-white"></i></span>
                            <input type="text" name="userId" id="login-id-input" class="form-control" placeholder="사용자 아이디" autocomplete="off">
                        </div>
                        <div class="input-group mb-3">
                            <span class="input-group-text bg-dark"><i class="bi bi-key-fill text-white"></i></span>
                            <input type="password" name="passwd" id="login-passwd-input" class="form-control" placeholder="비밀번호">
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" value="" id="flexCheckDefault">
                            <label class="form-check-label" for="flexCheckDefault">
                                내 정보 기억하기
                            </label>
                        </div>
                        <button class="btn btn-dark text-center mt-2" id="login-button" type="submit">
                            로그인
                        </button>
                        <p class="text-center mt-5">계정이 없으신가요?
                            <span class="text-dark login-text">
                                <a href="/join">회원가입</a>
                            </span>
                        </p>
                        <p class="text-center text-dark login-text">비밀번호를 잊어버리셨나요?</p>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<style>
    .container-login{
        width: 100%;
    }

    .login-text{
        font-weight: bold;
    }

    .input-group{
        width: 330px;
    }

    .btn{
        width: 330px;
    }

    @media only screen and (max-width: 576px){
        .container-login{
            width: 100%;
        }

        .login-text{
            font-weight: bold;
        }

        .input-group{
            width: 100%;
        }

        .btn{
            width: 100%;
        }
    }
</style>