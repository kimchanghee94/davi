/**
 * 자릿수에 해당하는 값을 반올림하여 반환
 * @param val 반올림 할 대상 값
 * @param pos 몇번째 자리에서 반올림할지 정한다
 * @return 반올림 된 값을 반환
 */
exports.round = function round(val, pos){
    try{
        let posCnt = 1;
        for(let i=0; i<pos; i++){
            posCnt*=10;
        }

        return Math.round((val) * posCnt)/posCnt;
    } catch (err){
        return err.stack;
    }
}
