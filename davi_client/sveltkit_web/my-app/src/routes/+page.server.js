import { redirect } from "@sveltejs/kit";

/** @type {import('./$type').PageServerLoad} */
export function load(){
    throw redirect(301,'/home');
}