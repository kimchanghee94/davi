package com.davi_crawler.molit.util;

import com.davi_crawler.common.util.ConstantUtil;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class MolitUtil {

    //API Response 응답 데이터의 Property 구간 키값 접두사
    public static final String EUPMYEONDONG_PREFIX = "emd";
    public static final String LI_PREFIX = "li";
    public static final String SIDO_PREFIX = "ctp";
    public static final String SIGUNGU_PREFIX = "sig";

    /**
     * 행정구역 MOLIT API 형태에서 공통 파라미터 반환
     * @param dataType 행정구역 포털 API에서 받아올 api 데이터 타입 (리, 읍면동, 시군구 ...)
     * @return 공통 파라미터 반환
     */
    public static Map<String, String> getMolitParams(String dataType){
        Map<String, String> params = new HashMap<>();
        params.put("key", ConstantUtil.MOLIT_SERVICE_KEY);
        params.put("domain", "localhost");
        params.put("service", "data");
        params.put("version", "2.0");
        params.put("request", "getfeature");
        params.put("format", "json");
        params.put("size", "1000");
        params.put("page", "1");
        params.put("geometry", "true");
        params.put("attribute", "true");
        params.put("crs", "EPSG:4326");
        params.put("geomfilter", "BOX(124.5,33.0,132.0,38.9)");
        params.put("data", dataType);

        return params;
    }

    /**
     * json데이터 용량이 클수록 json parser 시간복잡도가 매우 상승함. 따라서 한페이지에 한번 호출
     * @param data molit 응답 json 데이터
     * @return 최초 Parser를 통해 root Object값을 가져온다.
     */
    public static JSONObject getRoot(Map<String, Object> data){
        try{
            JSONParser jsonParser = new JSONParser();
            return (JSONObject)jsonParser.parse((String)data.get("data"));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param jsonObject molit 응답 json 데이터
     * @return 페이지 정보 반환
     */
    public static JSONObject getPage(JSONObject jsonObject){
        try{
            JSONObject response = (JSONObject)jsonObject.get("response");
            return (JSONObject) response.get("page");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param jsonObject molit 응답 json 데이터
     * @return size에 따른 전체페이지 개수 반환
     */
    public static int getTotalPage(JSONObject jsonObject){
        try{
            JSONObject page = getPage(jsonObject);
            String totalStr = (String) page.get("total");

            int totalPage = Integer.parseInt(totalStr);
            return totalPage;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @param jsonObject molit 응답 json 데이터
     * @return 한번에 받아올 size 반환
     */
    public static int getSize(JSONObject jsonObject){
        try{
            JSONObject page = getPage(jsonObject);
            String sizeStr = (String) page.get("size");

            int size = Integer.parseInt(sizeStr);
            return size;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @param jsonObject molit 응답 json 데이터
     * @return JSONObject result 객체 반환
     */
    public static JSONObject getResult(JSONObject jsonObject){
        try{
            JSONObject response = (JSONObject)jsonObject.get("response");
            //molit 응답 json 초기데이터일 경우
            if(response != null){
                return (JSONObject) response.get("result");
            }
            //getGeoJsonSingle 메서드를 거치고 난 json데이터일 경우
            else{
                return jsonObject;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param jsonObject molit 응답 json 데이터
     * @return JSONArray형태로 반환
     */
    public static JSONArray getFeatures(JSONObject jsonObject){
        try{
            JSONObject result = getResult(jsonObject);
            JSONObject featureCollection = (JSONObject) result.get("featureCollection");
            return (JSONArray) featureCollection.get("features");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param jsonObject molit 응답 json 데이터
     * @return JSONObject 형태의 바디부 반환
     */
    public static JSONObject getFeature(JSONObject jsonObject){
        try{
            JSONArray jsonArray = getFeatures(jsonObject);
            return (JSONObject) jsonArray.get(0);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param jsonObject molit 응답 json 데이터
     * @return JSONArray 형태의 좌표값들을 반환
     */
    public static JSONArray getCoordinates(JSONObject jsonObject){
        try{
            JSONObject feature = getFeature(jsonObject);
            JSONObject geometry = (JSONObject) feature.get("geometry");
            JSONArray coordinates = (JSONArray) geometry.get("coordinates");
            JSONArray jsonArray1 = (JSONArray) coordinates.get(0);
            JSONArray jsonArray2 = (JSONArray) jsonArray1.get(0);

            return  jsonArray2;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param jsonObject molit 응답 json 데이터
     * @return JSONObject형태의 properties 값들을 반환
     */
    public static JSONObject getProperties(JSONObject jsonObject){
        try{
            JSONObject Feature = getFeature(jsonObject);
            return (JSONObject) Feature.get("properties");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param jsonObject molit 응답 json 데이터
     * @param prefixKey 시도, 시군구, 읍면동, 리 마다의 접두사 구간
     * @param type 0을 넘길 경우 영어명 반환, 1을 넘길 경우 한국명 반환, 2를 넘길 경우 풀네임
     * @return 영어명 혹은 한국명으로 반환
     */
    public static String getPropertiesName(JSONObject jsonObject, String prefixKey, int type){
        try{
            if(type == 0){
                return (String) getProperties(jsonObject).get(prefixKey + "_eng_nm");
            }else if(type == 1){
                return (String) getProperties(jsonObject).get(prefixKey + "_kor_nm");
            }else if(type == 2){
                return (String) getProperties(jsonObject).get("full_nm");
            }else {
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param jsonObject molit 응답 json 데이터
     * @param cdKey 행정구역 코드 키 값
     * @return 행정구역코드값을 반환
     */
    public static String getPropertiesCd(JSONObject jsonObject, String cdKey){
        try{
            return (String) getProperties(jsonObject).get(cdKey + "_cd");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param jsonObject molit 응답 json 데이터
     * @param idx features json array 내부 데이터 중 하나의 참조할 인덱스
     * @return features json array를 하나의 geoJson형태로 반환
     */
    public static JSONObject getGeoJsonSingle(JSONObject jsonObject, int idx){
        try{
            JSONObject result = getResult(jsonObject);

            //깊은복사를 통해 주소값이 다른 데이터를 반환시켜야한다.
            //Parser로 복사하여 진행했을 때 결국 시간복잡도가 매우 상승한다.
            //따라서 변동된 구간에 대해서만 데이터를 만들어 반환하는 것이 효율적
            JSONObject root = new JSONObject();
            JSONObject featureCollection = new JSONObject();

            featureCollection.put("type", "FeatureCollection");
            featureCollection.put("bbox", ((JSONObject)result.get("featureCollection")).get("bbox"));

            JSONArray features = new JSONArray();
            features.add(getFeatures(result).get(idx));
            featureCollection.put("features", features);

            root.put("featureCollection", featureCollection);

            return root;
        }catch (IndexOutOfBoundsException e){       //jsonArray 끝이상일 경우 종료
            return null;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
