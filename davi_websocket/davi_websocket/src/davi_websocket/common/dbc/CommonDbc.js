const {Pool, Client} = require('pg');
const { dbconfig } = require('../../../../config');

//클라이언트 방식
exports.dbClient = new Client(dbconfig);
exports.dbClient.connect();


//풀 방식
exports.dbPool = new Pool(dbconfig);
