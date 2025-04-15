export const csr = false;
export const ssr = true;

export function load({page}){
    return {
        props:{
            message:"Page loaded on the server"
        }
    }
}

/**
 *  @type {import('./$types').PageServerLoad}
 */
export async function testPageLoad(){
    console.log("SSR TEST PAGE");
}