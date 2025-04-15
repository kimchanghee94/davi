import { openDB } from "idb";

const DB_NAME = "davi";
export const STORE_NAME = "join-data"

export async function openDatabase(){
    return await openDB(DB_NAME, 1, {
        upgrade(db){
            db.createObjectStore(STORE_NAME);
        },
    });
}