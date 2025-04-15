package com.davi_crawler.molit.service;

import com.davi_crawler.common.dto.DataListDto;
import com.davi_crawler.common.service.DataColListService;
import com.davi_crawler.common.service.DataListService;
import com.davi_crawler.common.service.SequenceService;
import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.molit.dto.RoadDto;
import com.davi_crawler.molit.entity.RoadEntity;
import com.davi_crawler.molit.repository.RoadRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.json.simple.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class RoadService {
    //ROAD LOGIN PAGE, LOGIN ACTION URL
    private final String ROAD_LOGIN_PAGE_URL = "https://www.nsdi.go.kr/lxportal/?menuno=2971";
    private final String ROAD_LOGIN_ACTION_URL = "https://www.nsdi.go.kr/lxportal/j_spring_security_check";
    private final RoadRepository roadRepository;
    private final DataListService dataListService;
    private final DataColListService dataColListService;
    private final SequenceService sequenceService;
    private ObjectMapper objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

    //파일 직접 수동으로 다운로드 받아서 읽어들이기 => dbf파일 전용
    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> updateApiData() throws Exception{
        Map<String, Object> result = new HashMap<>();
        FileInputStream fis = null;

        try{
            deleteAll();

            dataColListService.updateColData(ConstantUtil.DATA_LIST_ROAD_ID,
                    dataListService.selectById(ConstantUtil.DATA_LIST_ROAD_ID).getTableNm());

            String filePath = ConstantUtil.CRAWLING_FILE_PATH + "molit/road";
            String fileName = ".dbf";

            for(int page = 1; page<16; page++){
                String tmpFileName = page + fileName;

                fis = new FileInputStream(new File(filePath, tmpFileName));
                //인코딩 설정
                DbaseFileReader dbfReader = new DbaseFileReader(fis.getChannel(), true, Charset.forName("EUC-KR"));

                //속성의 필드 사이즈
                int colSize = dbfReader.getHeader().getNumFields();

                //필드 배열 생성
                String[] headers = new String[colSize];

                for(int i=0; i<colSize; i++) {
                    headers[i] = dbfReader.getHeader().getFieldName(i);
                }

                while(dbfReader.hasNext()){
                    Object[] values = dbfReader.readEntry();
                    JSONObject jsonObject = new JSONObject();

                    for(int i=0; i<colSize; i++){
                        if(values[i] != null){
                            jsonObject.put(headers[i].toLowerCase(), values[i].toString());
                        }else{
                            jsonObject.put(headers[i].toLowerCase(), "");
                        }
                    }
                    jsonObject.put("data_list_id", ConstantUtil.DATA_LIST_ROAD_ID);
                    JsonNode jsonNode = objectMapper.readTree(jsonObject.toJSONString());
                    RoadDto roadDto = objectMapper.treeToValue(jsonNode, RoadDto.class);
                    insert(roadDto);
                }

                dbfReader.close();
                fis.close();
            }

            result.put("success", true);
            result.put("message", "ROAD 데이터 업데이트 성공");
        }finally {
            try{
                if(fis != null){
                    fis.close();
                }
            }catch (IOException ie){
                ie.printStackTrace();
            }
        }

        return result;
    }

    public void insert(RoadDto roadDto) throws Exception{
        RoadEntity roadEntity = roadDto.toEntity();
        roadRepository.save(roadEntity);
    }

    public void deleteAll() throws Exception{
        roadRepository.deleteAll();
        sequenceService.initSeqNumZero(ConstantUtil.ROAD_SEQ_NAME);
    }

    //파일 직접 수동으로 다운로드 받아서 읽어들이기
    public Map<String, Object> updateApiData3(){
        Map<String, Object> result = new HashMap<>();
        try{
            String filePath = ConstantUtil.CRAWLING_FILE_PATH;
            String fileName = "1.dbf";

            FileInputStream file = new FileInputStream(new File(filePath, fileName));

            XSSFWorkbook wb = new XSSFWorkbook(file);
            XSSFSheet sheet = wb.getSheetAt(0);

            //모든 행(row)들을 조회
            Iterator<Row> rowIterator = sheet.iterator();
            int i=0;
            while(rowIterator.hasNext()){
                Row row = rowIterator.next();

                //각각의 행에 존재하는 모든 열(cell)을 순회
                Iterator<Cell> cellIterator = row.cellIterator();

                while(cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    System.out.print(cell.getStringCellValue());
                }
                System.out.println();
                i++;
                if(i==5){
                    break;
                }
            }

            result.put("success", true);
            result.put("message", "ROAD 데이터 업데이트 성공");
            file.close();
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "ROAD 데이터 업데이트 실패");
        }        
        
        return result;
    }

    //Jsoup으로 로그인하고 파일 링크 요청해서 다운받아 읽어들이기
    public Map<String, Object> updateApiData2(){
        Map<String, Object> result = new HashMap<>();

        try{
            // >>> 다운로드 테스트 중
            Map<String, String> testCookie = new HashMap<>();
            String testId = "zGYTvSHAAeENbMt2sQbmB5zt.lxportal21";
            testCookie.put("JSESSIONID", testId);
            testCookie.put("SSO", testId);
            downloadFile("http://www.nsdi.go.kr/lxdownload/download.do?DS_SQ=12902&FILE_SQ=21168", testCookie);

            if(testCookie.size() > 0){
                return null;
            }
            // <<< 다운로드 테스트 중

            //다운로드 링크 받아오기
            List<String> downLoadUrl = getDownloadLink();
            log.debug("Download URL : " + downLoadUrl);

            //로그인 하고 쿠키값 받아오기
            Map<String,String> cookies = getLoginCookies();
            log.debug("Get Cookies : " + cookies);

            Connection.Response resp = Jsoup.connect(downLoadUrl.get(0))
                    .method(Connection.Method.GET)
                    .cookies(cookies)
                    .execute();
            log.debug(resp);



//            Document document =
//                    Jsoup.connect("http://www.nsdi.go.kr/lxdownload/download.do?DS_SQ=12902&FILE_SQ=21168")
//                    .cookies(cookies).get();
//            log.debug(document);

            result.put("success", true);
            result.put("message", "ROAD 데이터 업데이트 성공");
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "ROAD 데이터 업데이트 실패");
        }

        return result;
    }

    private List<String> getDownloadLink() throws Exception{
        DataListDto dataListDto = dataListService.selectById(ConstantUtil.DATA_LIST_ROAD_ID);
        String url = dataListDto.getUrl();

        Document document = Jsoup.connect(url).timeout(5000*8).get();
        Elements elements = document.select("#data_table_resource > tbody > tr ul li button");

        List<String> downLoadUrl = new ArrayList<>();
        for(Element elem : elements){
            downLoadUrl.add(elem.attr("data-module-url"));
        }

        return downLoadUrl;
    }

    private Map<String, String> getLoginCookies() throws Exception{
        // input 태그 변수들 뽑아오기
        Document loginPage = Jsoup.connect(ROAD_LOGIN_PAGE_URL).get();

        Element loginForm = loginPage.selectFirst("form[name=frm]");
        Elements formInputs = loginForm.select("input");

        Map<String, String> body = new HashMap<>();

        for (Element input : formInputs) {
            String inputName = input.attr("name");

            if(inputName == null || inputName.length() == 0){
                continue;
            }

            if(inputName.equals("j_username")){
                body.put(inputName, "아이디 입력할 것");
            }else if(inputName.equals("j_password")){
                body.put(inputName, "인코딩 따로 필요없는 그냥 패스워드 입력할 것");
            }else{
                String inputValue = input.val();
                body.put(inputName, inputValue);
            }
        }

        // 로그인 action url에 전송하여 세션값 받아오기
        Connection.Response loginResponse = Jsoup.connect(ROAD_LOGIN_ACTION_URL)
                .data(body)
                .method(Connection.Method.POST)
                .execute();
        log.debug(loginResponse.cookies());

        return loginResponse.cookies();
    }

    private void downloadFile(String fileUrl, Map<String, String> cookies) throws Exception {
        String outputDir = "E:\\roadExcel";
        InputStream is = null;
        FileOutputStream fos = null;

        log.debug(Jsoup.connect(fileUrl).cookies(cookies).get());

        try{
            //>>>test
            fileUrl = "http://www.nsdi.go.kr/lxdownload/dext5handler.do";
//            fileUrl = "http://www.nsdi.go.kr/lxdownload/download.do?DS_SQ=12902&FILE_SQ=21168";

            Map<String, String> body = new HashMap<>();
            body.put("DS_SQ", "12902");
            body.put("FILE_SQ", "21169");
//            body.put("FILE_ORT_NM", "Z_KAIS_TL_SPRD_MANAGE_서울.zip");
            body.put("customValue", "%257B%2522DS_SQ%2522%253A%252212902%2522%252C%2522FILE_SQ%2522%253A%252221168%2522%257D");
            body.put("d00", "UlpEQXhER1J2ZDI1c2IyRmtVbVZ4ZFdWemRBdGtNVEFNWHd0a01qVU1DMlF5Tmd4YVgwdEJTVk5mVkV4ZlUxQlNSRjlOUVU1QlIwVmY3SVNjN0pxNExucHBjQXRrTURjTU16UXdNVVEyTVRRdFJqaERRUzFHTVRNMExVVkZNVEl0TWtSRE1FUTNNMEkxTWpBekMyUTFNUXd3Q3c9PQ%3D%3D");
            Map<String, String> tmpCook = new HashMap<>();
            tmpCook.put("JSESSIONID", "A9947A308AA440232A15DECDADB10377.lxdownload21");
            tmpCook.put("SSO", "zGYTvSHAAeENbMt2sQbmB5zt.lxportal21");
//            tmpCook.put("_ga_X4PRX19WH3", "GS1.1.1683705884.2.0.1683705884.0.0.0");
//            tmpCook.put("_gid", "GA1.3.216628122.1688458506");
//            tmpCook.put("dq_cookie", "%EA%B5%90%ED%86%B5%EB%A7%81%ED%81%AC^%EB%8F%84%EB%A1%9C^%ED%96%89%EC%A0%95%EA%B2%BD%EA%B3%84^%ED%96%89%EC%A0%95%EA%B5%AC%EC%97%AD^%ED%96%89%EC%A0%95%EA%B5%AC%EC%97%AD%20%EC%8B%9C%EB%8F%84");
//            tmpCook.put("popup_cookie_193", "1");
//            tmpCook.put("_ga_NLTXLHCKNC", "GS1.1.1688613203.23.1.1688614617.0.0.0");
//            tmpCook.put("_EXEN", "1");
//            tmpCook.put("_gat_gtag_UA_126541033_1", "1");
//            tmpCook.put("_ga_060B8F9M9Y", "GS1.1.1688621639.20.1.1688621640.0.0.0");
//            tmpCook.put("_ga_NLTXLHCKNC", "GS1.1.1688621809.24.1.1688621810.0.0.0");
//            tmpCook.put("_ga", "GA1.1.375098872.1683702390");
            Connection.Response response = Jsoup.connect(fileUrl)
//                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
//                    .header("Accept-Encoding", "gzip, deflate")
//                    .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
//                    .header("Connection", "keep-alive")
//                    .header("Content-Length", "320")
//                    .header("Cache-Control", "no-cache")
//                    .header("Content-Type", "application/x-www-form-urlencoded")
//                    .header("Host", "www.nsdi.go.kr")
//                    .header("Origin", "http://www.nsdi.go.kr")
//                    .header("Pragma", "no-cache")
//                    .header("Referer", "http://www.nsdi.go.kr/lxdownload/download.do?DS_SQ=12902&FILE_SQ=21168")
//                    .header("Upgrade-Insecure-Requests", "1")
//                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")
                    .data(body)
                    .cookies(tmpCook)
                    .method(Connection.Method.POST)
                    .execute();
            log.debug("========================================================");
            log.debug(response.parse());
//            log.debug(response.bodyStream());
            log.debug(response.statusCode());
            log.debug("========================================================");
            //<<<test



            URL url = new URL(fileUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestProperty("Cookie", "JSESSIONID=" + cookies.get("JSESSIONID") + ";SSO=" + cookies.get("SSO"));
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.setRequestProperty("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Content-Length", "320");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Host", "www.nsdi.go.kr");
            conn.setRequestProperty("Origin", "http://www.nsdi.go.kr");
            conn.setRequestProperty("Pragma", "no-cache");
            conn.setRequestProperty("Referer", "http://www.nsdi.go.kr/lxdownload/download.do?DS_SQ=12902&FILE_SQ=21168");
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
            conn.setRequestProperty("Cookie", "JSESSIONID=A9947A308AA440232A15DECDADB10377.lxdownload21;" +
                    " _ga_X4PRX19WH3=GS1.1.1683705884.2.0.1683705884.0.0.0;" +
                    " _gid=GA1.3.216628122.1688458506;" +
                    " dq_cookie=%EA%B5%90%ED%86%B5%EB%A7%81%ED%81%AC^%EB%8F%84%EB%A1%9C^%ED%96%89%EC%A0%95%EA%B2%BD%EA%B3%84^%ED%96%89%EC%A0%95%EA%B5%AC%EC%97%AD^%ED%96%89%EC%A0%95%EA%B5%AC%EC%97%AD%20%EC%8B%9C%EB%8F%84;" +
                    " popup_cookie_193=1;" +
                    " SSO=RcAidPk4xCkDv8liknMaoOZQ.lxportal21;" +
                    " _ga_NLTXLHCKNC=GS1.1.1688613203.23.1.1688614617.0.0.0;" +
                    " _EXEN=1;" +
                    " _gat_gtag_UA_126541033_1=1;" +
                    " _ga_060B8F9M9Y=GS1.1.1688621639.20.1.1688621640.0.0.0;" +
                    " _ga=GA1.1.375098872.1683702390");

            int responseCode = conn.getResponseCode();
            log.debug(responseCode);

            if(responseCode == HttpURLConnection.HTTP_OK){
                String fileName = "";
                String disposition = conn.getHeaderField("Content-Disposition");
                String contentType = conn.getContentType();

                if(disposition !=  null){
                    String target = "filename=";
                    int index = disposition.indexOf(target);
                    if(index != -1){
                        fileName = disposition.substring(index + target.length() + 1);
                    }
                }else{
                    fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
                }

                // >>> test
                fileName = "test.txt";
                // <<< test

                log.fatal("Content-type = " + contentType);
                log.fatal("Content-Disposition = " + disposition);
                log.fatal("fileName = " + fileName);

                is = conn.getInputStream();
                fos = new FileOutputStream(new File(outputDir, fileName));

                final int BUFFER_SIZE = 4096;
                int bytesRead;
                byte[] buffer = new byte[BUFFER_SIZE];
                while((bytesRead = is.read(buffer)) != -1){
                    fos.write(buffer, 0, bytesRead);
                }
                fos.close();
                is.close();
                log.debug("FILE DOWNLOAD");
            }
            else if(responseCode == HttpURLConnection.HTTP_MOVED_TEMP){
                throw new Exception("로그인 세션 ID가 유효하지 않습니다.");
            }
            conn.disconnect();
        }catch (Exception e){
            e.printStackTrace();
            try{
                if( is != null){
                    is.close();
                }
                if( fos != null){
                    fos.close();
                }
            }catch (IOException ie){
                ie.printStackTrace();
            }
        }
    }
}