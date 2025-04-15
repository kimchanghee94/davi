export const csr = true;
export const ssr = true;
import {refreshTokenCheck} from "./token.js";


/** @type {import('./$types').PageServerLoad} */
export async function load({fetch}){
    let response = await refreshTokenCheck({fetch});
    console.log("refreshTokenFlag Check : " + response);
    return {
        refreshTokenFlag : response
    };
}