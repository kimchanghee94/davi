import { browser } from "$app/environment";
import  getRefreshToken from '../token.js';

export const csr = true;

//해당 메서드는 전체적인 UI는 SSR로 그려왔고 공지사항에 띄울 데이터에 대해서는 CSR로 처리해야된다....
//그래야 백엔드에게 사용자의 세션을 전달할 수가 있다.
/** @type {import('./$types').PageServerLoad} */
export async function load({fetch}){
    let accessToken = "";
    let backendURL = import.meta.env.VITE_BACKEND_URL;
    
    if(browser 
        && window.localStorage.getItem("grantType") !== null
        && window.localStorage.getItem("accessToken") !== null){
        accessToken = window.localStorage.getItem("grantType") + " ";
        accessToken += window.localStorage.getItem("accessToken");
    }

    console.log("Server Side Rendering OR Client Side Rendering");

    const res = await fetch(backendURL + '/dbtest',{
        method: 'GET',
        //credentials: 'include',
        headers:{
            'Authorization': accessToken
        },
        
    });
    let resJson;
    if(res.ok){
        resJson = await res.json();
        console.log(resJson);
        
        //Expired Jwt Token
        if(resJson.errorCode === '002'){
            console.log(resJson.message);
            let jwtTokenResult = await getRefreshToken({fetch});

            if(jwtTokenResult !== null && jwtTokenResult.header.statusCode == "00"){
                console.log("REFRESH??? " + jwtTokenResult.header.msg);

                if(browser 
                    && window.localStorage.getItem("grantType") !== null
                    && window.localStorage.getItem("accessToken") !== null){
                    accessToken = window.localStorage.getItem("grantType") + " ";
                    accessToken += window.localStorage.getItem("accessToken");
                }
            
                console.log("Server Side Rendering OR Client Side Rendering");
                console.log("AFTER ACCESS-TOKEN:",accessToken);
            
                let refreshAfterRes = await fetch(backendURL + '/dbtest',{
                    method: 'GET',
                    //credentials: 'include',
                    headers:{
                        'Authorization': accessToken
                    },
                });

                resJson = await refreshAfterRes.json();
                console.log(resJson);
            }
            //Refresh Token도 만기 상태라 AccessToken재발급을 못받는 경우
            else{

            }
        }
        //로그인 하지 않은 상태
        else if(resJson.errorCode === '001'){
            alert("로그인 이후 이용 가능합니다.");
            return null;
        }        
        else{
            console.log("["+resJson.header.statusCode+"]", resJson.header.msg);  
        }
    }else{

    }
    return resJson;
}