<script>
    import {browser} from "$app/environment";

    let backendURL = import.meta.env.VITE_BACKEND_URL;
    export let refreshTokenFlag = true;
    console.log("refreshTokenFlag : " + refreshTokenFlag);

    async function logout(){
        let formData = new FormData();
        let grantType = window.localStorage.getItem("grantType");
        let accessToken = window.localStorage.getItem("accessToken");
            
        if(browser 
            && grantType !== null
            && accessToken !== null){
            formData.append("grantType", grantType);
            formData.append("accessToken", accessToken);
        }

        const res = await fetch(backendURL + '/api/v1/user-api/logout',{
            method: 'POST',
            body : formData,
        });

        let resJson = await res.json();
        console.log(resJson);

        if(resJson.header.statusCode == "00" || resJson.header.statusCode == "01"){
            console.log("Logout Success!!");
            console.log(resJson.header.msg);
            //로그아웃 성공 후에 토큰을 모두 지워준다.
            window.localStorage.removeItem("grantType");
            window.localStorage.removeItem("accessToken");
            window.localStorage.removeItem("refreshToken");
            window.location.href="/login";
        }
    }
</script>

<!-- navbar -->
<nav class="navbar navbar-default navbar-expand-sm navbar-dark bg-dark">
    <a href="/home" class="navbar-brand">Kenny Company</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="collapsibleNavbar">
        <ul class="nav navbar-nav mr-auto">
            <li class="nav-item"><a href="/home" class="nav-link">회사소개</a></li>
            <li class="nav-item"><a href="/notice" class="nav-link">공지사항</a></li>
            <li class="nav-item"><a href="/roadmap" class="nav-link">오시는길</a></li>
            <li class="nav-item"><a href="/profit" class="nav-link">매출액</a></li>
            <li class="nav-item"><a href="/analytics" class="nav-link">분석</a></li>
            <li class="nav-item"><a href="/seamap" class="nav-link">해양맵</a></li>
            <li class="nav-item"><a href="/seamap2" class="nav-link">해양맵2</a></li>
            <li class="nav-item"><a href="/trafficmap" class="nav-link">자율주행</a></li>
            <li class="nav-item"><a href="/mulyuu" class="nav-link">물류</a></li>
        </ul>
        <ul class="nav navbar-nav my-2 my-lg-0">
            <li class="nav-item dropdown mr-sm-4">
                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                접속하기</a>
                <div class="dropdown-menu dropdown-menu-left" aria-labelledby="navbarDropdown">
                    {#if !refreshTokenFlag}
                    <a class="dropdown-item" href="/login">로그인</a>
                    <a class="dropdown-item" href="/join">회원가입</a>
                    {:else}
                    <a class="dropdown-item" href="/home" on:click={logout}>로그아웃</a>
                    {/if}
                </div>
            </li>    
        </ul>
    </div>
</nav>

<style>
    .dropdown-menu{
        min-width:50px;
    }
</style>