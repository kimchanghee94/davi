// layout.js에서 csr=false로 막았는데 여기서 한번더 false를 해주지 않으면 prefetching이 되어버리는 걸까...
// app.html기본 옵션에 prefetching되도록 허용이 되어있었댜!
// export const csr = false;


// import { browser } from "$app/environment";
// /** @type {import('./$types').PageServerLoad} */
// export async function load(){
//     let accessToken;
//     console.log(browser);
//     if(window.localStorage.getItem("accessToken") !== null){
//         accessToken = localStorage.getItem("accessToken");
//         console.log(accessToken);
//     }else{
//         console.log("Not Access Token");
//     }

//     console.log("Server Side Rendering");
//     const res = await fetch(import.meta.env.VITE_BACKEND_URL + '/dbtest',{
//         method: 'GET',
//         credentials: 'include',
//         headers:{
//            'Authorization': accessToken
//         },     
//     });
    
//     const resJson = await res.json();
//     console.log("["+resJson.header.statusCode+"]", resJson.header.msg);
//     return resJson;
// }