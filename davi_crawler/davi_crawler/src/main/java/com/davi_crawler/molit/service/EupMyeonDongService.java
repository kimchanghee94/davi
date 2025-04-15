package com.davi_crawler.molit.service;

import com.davi_crawler.common.dto.DataListDto;
import com.davi_crawler.common.service.DataColListService;
import com.davi_crawler.common.service.DataListService;
import com.davi_crawler.common.service.SequenceService;
import com.davi_crawler.common.util.ApiUtil;
import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.common.util.ConvertEntityDtoListUtil;
import com.davi_crawler.molit.util.MolitUtil;
import com.davi_crawler.molit.dto.EupMyeonDongDto;
import com.davi_crawler.molit.entity.EupMyeonDongEntity;
import com.davi_crawler.molit.repository.EupMyeonDongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class EupMyeonDongService {
    private final DataListService dataListService;
    private final DataColListService dataColListService;
    private final EupMyeonDongRepository eupMyeonDongRepository;
    private final SequenceService sequenceService;

    public void setEupMyeonDongDto(JSONObject root, int size) throws Exception{
        List<EupMyeonDongDto> eupMyeonDongDtos = new ArrayList<>();

        for (int i=0; i<size; i++) {
            JSONObject tmpRoot = MolitUtil.getGeoJsonSingle(root, i);

            if(tmpRoot == null){
                break;
            }

            EupMyeonDongDto eupMyeonDongDto = new EupMyeonDongDto();
            eupMyeonDongDto.setDataListId(ConstantUtil.DATA_LIST_EUPMYEONDONG_ID);
            eupMyeonDongDto.setEmdEngNm(MolitUtil.getPropertiesName(tmpRoot, MolitUtil.EUPMYEONDONG_PREFIX, 0));
            eupMyeonDongDto.setEmdKorNm(MolitUtil.getPropertiesName(tmpRoot, MolitUtil.EUPMYEONDONG_PREFIX, 1));
            eupMyeonDongDto.setFullNm(MolitUtil.getPropertiesName(tmpRoot, MolitUtil.EUPMYEONDONG_PREFIX, 2));
            eupMyeonDongDto.setEmdCd(MolitUtil.getPropertiesCd(tmpRoot, MolitUtil.EUPMYEONDONG_PREFIX));
            eupMyeonDongDto.setJsonData(tmpRoot.toJSONString());

            eupMyeonDongDtos.add(eupMyeonDongDto);
        }

        if(eupMyeonDongDtos.size() != 0){
            insertAll(eupMyeonDongDtos);
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> updateApiData() throws Exception{
        Map<String, Object> result = new HashMap<>();

        deleteAll();

        dataColListService.updateColData(ConstantUtil.DATA_LIST_EUPMYEONDONG_ID,
                dataListService.selectById(ConstantUtil.DATA_LIST_EUPMYEONDONG_ID).getTableNm());

        DataListDto dataListDto = dataListService.selectById(ConstantUtil.DATA_LIST_EUPMYEONDONG_ID);
        String url = dataListDto.getUrl();

        Map<String, String> params = MolitUtil.getMolitParams("LT_C_ADEMD_INFO");
        Map<String,Object> apiData = ApiUtil.getApiData(url,null,params);

        if((Boolean) apiData.get("success") == false){
            throw new Exception((String) apiData.get("message"));
        }

        JSONObject root = MolitUtil.getRoot(apiData);
        int totalPage = MolitUtil.getTotalPage(root);
        int size = MolitUtil.getSize(root);

        setEupMyeonDongDto(root, size);
        log.debug("[INSERT 1] " + MolitUtil.getPage(root));

        for(int i=2; i <= totalPage; i++){
            params.put("page", String.valueOf(i));
            apiData = ApiUtil.getApiData(url,null,params);          //데이터 가져오기

            if((Boolean) apiData.get("success") == false){
                throw new Exception((String) apiData.get("message"));
            }

            root = MolitUtil.getRoot(apiData);          //새로운 jsonObject 값 가져오기

            setEupMyeonDongDto(root, size);             //dto조합 후 insert

            log.debug("[INSERT " + i + "] " + MolitUtil.getPage(root));
        }

        //법정동코드 및 행정도코드 삽입
        updateHdongBdongApiData();
        updateEMDCenterPosData();

        result.put("success", true);
        result.put("message", "EUPMYEONDONG 데이터 업데이트 성공");

        return result;
    }

    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> updateHdongBdongApiData() throws Exception{
        Map<String, Object> result = new HashMap<>();
        FileInputStream fis = null;
        try{
            String filePath = ConstantUtil.CRAWLING_FILE_PATH + "molit";
            String fileName = "hdong-bdong.xlsx";

            fis = new FileInputStream(new File(filePath, fileName));
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);

            //총 행의 수
            int rLen = sheet.getPhysicalNumberOfRows();
            for(int rIdx = 0; rIdx < rLen; rIdx++){
                XSSFRow row = sheet.getRow(rIdx);
                if(row != null){
                    int cLen = row.getLastCellNum();
                    if(cLen != 7) continue;

                    List<String> cellVal = new ArrayList<>();
                    for(int cIdx = 0; cIdx < cLen; cIdx++){
                        XSSFCell cell = row.getCell(cIdx);
                        String value = "";

                        if(cell == null){
                            value = "null";
                        }else{
                            value = cell.getStringCellValue();
                        }

                        cellVal.add(value);
                    }

                    if(!cellVal.get(1).equals("null") && !cellVal.get(5).equals("null")){
                        if(cellVal.get(5).charAt(cellVal.get(5).length() - 1) == '시'
                        || cellVal.get(5).charAt(cellVal.get(5).length() - 1) == '도'
                        || cellVal.get(5).charAt(cellVal.get(5).length() - 1) == '구'
                        || cellVal.get(5).charAt(cellVal.get(5).length() - 1) == '군'
                        || cellVal.get(5).charAt(cellVal.get(5).length() - 1) == '리'){
                            continue;
                        }

                        String fullNm = cellVal.get(1).replaceAll(" ", "%") + "%";
                        if(!cellVal.get(2).equals("null")){
                            fullNm += (cellVal.get(2).replaceAll(" ", "%") + "%");
                        }
                        fullNm += (cellVal.get(5).replaceAll(" ", "%") + "%");

                        List<EupMyeonDongEntity> eupMyeonDongEntities = eupMyeonDongRepository.findAllByFullNmLike(fullNm);
                        for(int i=0; i<eupMyeonDongEntities.size(); i++){
                            EupMyeonDongDto eupMyeonDongDto = eupMyeonDongEntities.get(i).toDto();
                            eupMyeonDongDto.setHdongCd(cellVal.get(0));
                            eupMyeonDongDto.setBdongCd(cellVal.get(4));
                            insert(eupMyeonDongDto);
                        }
                    }
                }
            }

            result.put("success", true);
            result.put("message", "hdong, bdong 데이터 삽입 성공");
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

    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> updateEMDCenterPosData() throws Exception{
        Map<String, Object> result = new HashMap<>();
        BufferedReader br = null;

        try{
            String filePath = ConstantUtil.CRAWLING_FILE_PATH + "molit";
            String fileName = "emd_center_pos.csv";

            br = new BufferedReader(new FileReader(new File(filePath, fileName)));
            String line = "";

            List<List<String>> csvDatas = new ArrayList<>();

            while((line = br.readLine()) != null){
                String lineArr[] = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                List<String> aLine = Arrays.asList(lineArr);
                csvDatas.add(aLine);
            }

            for(int i=1; i<csvDatas.size(); i++){
                List<String> csvData = new ArrayList<>(csvDatas.get(i));

                //칼럼 데이터가 개수를 만족하지 않는 로우일 경우
                if(csvData.size() != 8){
                    int tmpCsvDataSize = 8 - csvData.size();

                    for(int j=0; j<tmpCsvDataSize; j++){
                        csvData.add(null);
                    }
                }

                String emdCd = csvData.get(1).replaceAll("\"", "");
                String centerLng = csvData.get(6).replaceAll("\"", "");
                String centerLat = csvData.get(7).replaceAll("\"", "");

                EupMyeonDongDto eupMyeonDongDto = eupMyeonDongRepository.findByEmdCd(emdCd).toDto();

                if(eupMyeonDongDto == null){
                    continue;
                }

                eupMyeonDongDto.setCenterLng(centerLng);
                eupMyeonDongDto.setCenterLat(centerLat);
                log.debug(eupMyeonDongDto);

                insert(eupMyeonDongDto);
            }

            result.put("success", true);
            result.put("message", "읍면동 중심 좌표 업데이트 성공");
        }finally {
            try{
                if(br != null){
                    br.close();
                }
            }catch (IOException ie){
                ie.printStackTrace();
            }
        }

        return result;
    }

    public void insert(EupMyeonDongDto eupMyeonDongDto) throws Exception{
        EupMyeonDongEntity eupMyeonDongEntity = eupMyeonDongDto.toEntity();
        eupMyeonDongRepository.save(eupMyeonDongEntity);
    }

    public void insertAll(List<EupMyeonDongDto> eupMyeonDongDtos) throws Exception{
        List<EupMyeonDongEntity> eupMyeonDongEntities = ConvertEntityDtoListUtil.toListEupMyeonDongEntity(eupMyeonDongDtos);
        eupMyeonDongRepository.saveAll(eupMyeonDongEntities);
    }

    public void deleteAll() throws Exception{
        eupMyeonDongRepository.deleteAll();
        sequenceService.initSeqNumZero(ConstantUtil.EUPMYEONDONG_SEQ_NAME);
    }
}
