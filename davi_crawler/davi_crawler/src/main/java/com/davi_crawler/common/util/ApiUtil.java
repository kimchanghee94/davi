package com.davi_crawler.common.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class ApiUtil {

    private static String mapToUrlParam(Map<String, String> params) {
        StringBuffer paramData = new StringBuffer();

        if(params == null){
            return "";
        }

        for (Map.Entry<String, String> param : params.entrySet()) {
            if (paramData.length() != 0) {
                paramData.append('&');
            }
            paramData.append(param.getKey());
            paramData.append('=');
            paramData.append(param.getValue());
        }
        return "?" + paramData.toString();
    }

    /**
     * url 파라미터를 넘기면 GET형태로 api 응답데이터를 담아 반환한다.
     * @param url 전송할 url 문자열
     * @param httpHeaders 헤더에 담을 값
     * @param params url 파라미터
     * @return 응답데이터를 body에 담고 header에 응답 성공여부를 담아 반환
     */
    public static Map<String, Object> getApiData(String url, HttpHeaders httpHeaders, Map<String, String> params){
        Map<String, Object> result = new HashMap<>();

        try{
            //uri전용으로 인코딩해서 보내지 않을 경우 에러가 발생하는 서비스 존재
            URI uri = new URI(url + mapToUrlParam(params));

            //응답데이터 한글깨짐 문제 해결
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

            //헤더 세팅
            HttpEntity request = new HttpEntity(httpHeaders);

            ResponseEntity<String> res = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    request,
                    String.class);

            result.put("success", true);
            result.put("message", "API를 호출하는데 성공하였습니다.");
            result.put("data", res.getBody());
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "API를 호출하는데 실패하였습니다.");
        }

        return result;
    }

    /**
     * url 파라미터를 넘기면 POST형태로 api 응답데이터를 담아 반환한다.
     * @param url 전송할 url 문자열
     * @param httpHeaders 전송할 헤더
     * @param body MultiValueMap 타입으로 전송할 바디
     * @return 응답데이터를 body에 담고 header에 응답 성공여부를 담아 반환
     */
    public static Map<String, Object> postApiData(String url, HttpHeaders httpHeaders, MultiValueMap<String, String> body){
        Map<String, Object> result = new HashMap<>();

        try{
            HttpEntity<MultiValueMap<String,String>> requestEntity = new HttpEntity<>(body, httpHeaders);
            ResponseEntity<String> res = new RestTemplate().postForEntity(url, requestEntity, String.class);

            result.put("success", true);
            result.put("message", "API를 호출하는데 성공하였습니다.");
            result.put("data", res.getBody());
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "API를 호출하는데 실패하였습니다.");
        }

        return result;
    }

    /**
     * url 파라미터를 넘기면 POST형태로 api 응답데이터를 담아 반환한다.
     * @param url 전송할 url 문자열
     * @param httpHeaders 전송할 헤더
     * @param body String 타입으로 전송할 바디
     * @return 응답데이터를 body에 담고 header에 응답 성공여부를 담아 반환
     */
    public static Map<String, Object> postApiData(String url, HttpHeaders httpHeaders, String body){
        Map<String, Object> result = new HashMap<>();

        try{
            HttpEntity<String> requestEntity = new HttpEntity<>(body, httpHeaders);
            ResponseEntity<String> res = new RestTemplate().postForEntity(url, requestEntity, String.class);

            result.put("success", true);
            result.put("message", "API를 호출하는데 성공하였습니다.");
            result.put("data", res.getBody());
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "API를 호출하는데 실패하였습니다.");
        }

        return result;
    }

    /**
     * @param url API url 주소
     * @param nextPageIdx 다음 읽어들일 페이지
     * @return 다음 읽어들일 페이지 주소를 반환
     */
    public static String getNextPageUrl(String url, String pageStr, int nextPageIdx) throws Exception{
        return url.replace(pageStr + (nextPageIdx - 1), pageStr + nextPageIdx);
    }
}
