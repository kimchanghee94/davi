import { open } from 'sqlite';
import sqlite3 from 'sqlite3';
import path from 'path';

let database_TMP = null;

/**
 *
 * @returns {Promise<import('sqlite').Database>}
 */
export async function getDatabase(){
    if(!database_TMP){
        database_TMP = await open({
            filename : path.join(process.cwd(), 'database_TMP.db'),
            driver: sqlite3.Database
        });
    }

    return database_TMP;
}

export async function createUser(){
    const db = await getDatabase();
    const result = await db.run(
        `INSERT INTO users (username, password) VALUES (?,?)`,
        "kimchanghee",
        "1234"
    );
    return result.lastID;
}