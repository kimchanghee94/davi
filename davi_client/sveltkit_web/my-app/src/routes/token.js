import {browser} from "$app/environment"

export async function refreshTokenCheck({fetch}){
    if(typeof window === "undefined") {
        return;
    }

    let backendURL = import.meta.env.VITE_BACKEND_URL;
    let formData = new FormData();
    let refreshToken = window.localStorage.getItem("refreshToken");

    if(browser && refreshToken !== null){
        formData.append("refreshToken", refreshToken);
    }else{
        //발급받은 Refresh Token이 없는 경우
        return false;
    }

    const response = await fetch(backendURL + '/api/v1/user-api/refresh-token-check', {
        method:'POST',
        body : formData   
    });
    console.log(response);
    if(response.ok){
        let resJson = await response.json();
        console.log("token js : " + resJson);
        //Refresh Token이 유효함
        if(resJson.header.statusCode === '00'){
            return true;
        }
        //Refresh Token이 유효하지 않음
        else if(resJson.header.statusCode === '01'){
            return false;
        }
    }else{
        return false;
    }
}

export default async function getRefreshToken({fetch}){
    let backendURL = import.meta.env.VITE_BACKEND_URL;
    let formData = new FormData();
    let grantType = window.localStorage.getItem("grantType");
    let accessToken = window.localStorage.getItem("accessToken");
    let refreshToken = window.localStorage.getItem("refreshToken");

    if(browser 
        && grantType !== null
        && accessToken !== null
        && refreshToken !== null){
        
        formData.append("grantType", grantType);
        formData.append("accessToken", accessToken);
        formData.append("refreshToken", refreshToken);
    }

    const response = await fetch(backendURL + '/api/v1/user-api/refresh-token', {
        method:'POST',
        body : formData,        
    });

    if(response.ok){
        let resJson = await response.json();
        if(resJson.header.statusCode == "00"){
            console.log("SUCCESS!");
            window.localStorage.setItem("grantType", resJson.body.grantType);
            window.localStorage.setItem("accessToken", resJson.body.accessToken);
            window.localStorage.setItem("refreshToken", resJson.body.refreshToken);
        }
        else{
            window.localStorage.removeItem("grantType");
            window.localStorage.removeItem("accessToken");
            window.localStorage.removeItem("refreshToken");
        }
        return resJson;
    }else{
        return null;
    } 
}