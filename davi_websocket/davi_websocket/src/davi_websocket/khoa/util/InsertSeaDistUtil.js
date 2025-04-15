const MathUtil = require('../../common/util/MathUtil');
const seaDistDbc = require('../dbc/InsertSeaDistDbc');

//실시간 배 이동 위치 데이터 만들기
exports.insertPos = async function (connection, resData){
    let result = {};

    try{
        let totalRanLngArr = [];
        let totalRanLatArr = [];

        let rows = await seaDistDbc.selectList();

        for(let i=0; i<rows.length; i++){
            let row = rows[i];

            let lng = [];
            let lat = [];
            lng = row['degree_lon_arr'].split(',').map(Number);
            lat = row['degree_lat_arr'].split(',').map(Number);

            let rowRanLngArr = [];
            let rowRanLatArr = [];

            for(let j=0; j<lng.length - 1; j++){
                let fx = lng[j], fy = lat[j];
                let sx = lng[j + 1], sy = lat[j + 1];

                let ranLngArr = [];
                let ranLatArr = [];

                //맨 초기 시작점 담기
                ranLngArr.push(fx);
                ranLatArr.push(fy);

                //점과 점사이 가운데 점 담기
                let ranDist = 0.003;        //점 간격 길이
                if(fx !== sx){
                    let totalSum = fx;

                    //lat값 구하기 위한 기울기와 상수
                    let m = MathUtil.round(((fy-sy)/(fx-sx)), 6);
                    let c = fy - (m*fx);

                    while(true){
                        let ranVal;

                        if(!(-1 <= m && m <= 1)){
                            ranVal = (Math.random() * (1.3 - 1) + 1) * ranDist;
                        }else{
                            ranVal = (Math.floor(Math.random()*(3 - 2) + 2)) * ranDist;
                        }
                        ranVal = MathUtil.round(ranVal, 6);

                        if(fx < sx){
                            totalSum += ranVal;
                            if(totalSum >= sx){
                                break;
                            }
                        }else {
                            totalSum -= ranVal;
                            if(totalSum <= sx){
                                break;
                            }
                        }

                        totalSum = MathUtil.round(totalSum, 6);
                        ranLngArr.push(totalSum);
                    }

                    for(let k = 1 ; k < ranLngArr.length; k++){
                        let ranLat;
                        ranLat = MathUtil.round((ranLngArr[k]*m) + c, 6);

                        //행선에서 오차를 주어 삐뚤삐뚤하게 움직이도록 데이터 조작
                        let errRange = 0.002;
                        let ranVal = Math.random()*(errRange - (-errRange)) + (-errRange);
                        ranVal = MathUtil.round(ranVal, 6);

                        //기울기가 1인 또는 -1의 또는 기울기가 가파른 경우는 최종 위아래 방향은 유지 되도록 한다.
                        if(m == 1 || m == -1 || !(-1 < m && m < 1)){
                            ranLngArr[k] = MathUtil.round(ranLngArr[k] + ranVal, 6);
                            ranLatArr.push(ranLat);
                        }
                        //기울기가 완만한 경우는 위 혹은 아래방향으로 삐뚤삐뚤하게하고 좌 또는 우의 방향은 유지 시켜준다.
                        else if(-1 < m && m < 1){
                            ranLatArr.push(MathUtil.round(ranLat + ranVal, 6));
                        }
                    }
                }else if (fx === sx){
                    //fx === sx인 경우에 대한 처리 변수
                    let totalSum = fy;

                    while(true){
                        let ranVal = (Math.floor(Math.random()*(3 - 2) + 2)) * ranDist;
                        ranVal = MathUtil.round(ranVal, 6);

                        if(fy < sy){
                            totalSum += ranVal;
                            if(totalSum >= sy){
                                break;
                            }
                        }else {
                            totalSum -= ranVal;
                            if(totalSum <= sy){
                                break;
                            }
                        }

                        totalSum = MathUtil.round(totalSum, 6);
                        ranLatArr.push(totalSum);
                    }

                    //lng좌표가 같아 기울기가 무한대인 경우 위 또는 아래 유지하고 경도만 좌우로 삐뚤삐뚤하게 한다.
                    for(let k=1; k<ranLatArr.length; k++){
                        //행선에서 오차를 주어 삐뚤삐뚤하게 움직이도록 데이터 조작
                        let errRange = 0.002;
                        let ranVal = Math.random()*(errRange - (-errRange)) + (-errRange);
                        ranVal = MathUtil.round(ranVal, 6);
                        ranLngArr.push(MathUtil.round(fx + ranVal, 6));
                    }
                }

                //최종 도착지점 담아주기
                if(j === lng.length - 2){
                    ranLngArr.push(sx);
                    ranLatArr.push(sy);
                }

                rowRanLngArr.push(ranLngArr);
                rowRanLatArr.push(ranLatArr);
            }

            totalRanLngArr.push(rowRanLngArr);
            totalRanLatArr.push(rowRanLatArr);
        }

        result["lng"] = totalRanLngArr;
        result["lat"] = totalRanLatArr;
        //모든 랜덤좌표 대입
        await seaDistDbc.deleteList();
        await seaDistDbc.insertList(rows, result);

        result["success"] = true;
        result["message"] = "Data insertion complete";
    }catch (err) {
        console.error(err);
        result["success"] = false;
        result["message"] = "Data insertion fail " + err.stack;
    }

    connection.sendUTF(JSON.stringify(result));
}