// export default function fetchWithAuthorization(url, options){
//     const apiKey = 'IDJe8lxnwi50baG6wWjnJTzAKy6TpU1JXS1TE4t7Bryl2t2qxkB3bU5CUajIQCd5';
//
//     options.headers = {
//         'Authorization': `Bearer ${apiKey}`,
//         'Content-Type': 'application/json',
//         credentials: 'include',
//         'Access-Control-Allow-Origin': '*',   // 모든 사이트를 허가
//         'Access-Control-Allow-Methods': 'GET, POST, PUT',
//         'Access-Control-Allow-Headers' : '*'
//     }
//     return fetch(url, options);
// }