package com.davi_crawler.molit.service;

import com.davi_crawler.common.dto.DataListDto;
import com.davi_crawler.common.service.DataColListService;
import com.davi_crawler.common.service.DataListService;
import com.davi_crawler.common.service.SequenceService;
import com.davi_crawler.common.util.ApiUtil;
import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.common.util.ConvertEntityDtoListUtil;
import com.davi_crawler.molit.util.MolitUtil;
import com.davi_crawler.molit.dto.SiGunGuDto;
import com.davi_crawler.molit.entity.SiGunGuEntity;
import com.davi_crawler.molit.repository.SiGunGuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class SiGunGuService {
    private final DataListService dataListService;
    private final DataColListService dataColListService;
    private final SiGunGuRepository siGunGuRepository;
    private final SequenceService sequenceService;

    public void setSiGunGuDto(JSONObject root, int size) throws Exception{
        List<SiGunGuDto> siGunGuDtos = new ArrayList<>();

        for(int i=0; i<size; i++){
            JSONObject tmpRoot = MolitUtil.getGeoJsonSingle(root, i);

            if(tmpRoot == null){
                break;
            }

            SiGunGuDto siGunGuDto = new SiGunGuDto();
            siGunGuDto.setDataListId(ConstantUtil.DATA_LIST_SIGUNGU_ID);
            siGunGuDto.setSigEngNm(MolitUtil.getPropertiesName(tmpRoot, MolitUtil.SIGUNGU_PREFIX, 0));
            siGunGuDto.setSigKorNm(MolitUtil.getPropertiesName(tmpRoot, MolitUtil.SIGUNGU_PREFIX, 1));
            siGunGuDto.setFullNm(MolitUtil.getPropertiesName(tmpRoot, MolitUtil.SIGUNGU_PREFIX, 2));
            siGunGuDto.setSigCd(MolitUtil.getPropertiesCd(tmpRoot, MolitUtil.SIGUNGU_PREFIX));
            siGunGuDto.setJsonData(tmpRoot.toJSONString());

            siGunGuDtos.add(siGunGuDto);
        }

        if(siGunGuDtos.size() != 0){
            insertAll(siGunGuDtos);
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> updateApiData() throws Exception {
        Map<String, Object> result = new HashMap<>();

        deleteAll();

        dataColListService.updateColData(ConstantUtil.DATA_LIST_SIGUNGU_ID,
                dataListService.selectById(ConstantUtil.DATA_LIST_SIGUNGU_ID).getTableNm());

        DataListDto dataListDto = dataListService.selectById(ConstantUtil.DATA_LIST_SIGUNGU_ID);
        String url = dataListDto.getUrl();

        Map<String, String> params = MolitUtil.getMolitParams("LT_C_ADSIGG_INFO");
        Map<String,Object> apiData = ApiUtil.getApiData(url,null,params);

        if((Boolean) apiData.get("success") == false){
            throw new Exception((String) apiData.get("message"));
        }

        JSONObject root = MolitUtil.getRoot(apiData);

        int totalPage = MolitUtil.getTotalPage(root);
        int size = MolitUtil.getSize(root);

        setSiGunGuDto(root, size);
        log.debug("[INSERT 1] " + MolitUtil.getPage(root));

        for(int i=2; i <= totalPage; i++){
            params.put("page", String.valueOf(i));
            apiData = ApiUtil.getApiData(url,null,params);          //데이터 가져오기

            if((Boolean) apiData.get("success") == false){
                throw new Exception((String) apiData.get("message"));
            }

            root = MolitUtil.getRoot(apiData);          //새로운 jsonObject 값 가져오기

            setSiGunGuDto(root, size);                  //dto조합 후 insert

            log.debug("[INSERT " + i + "] " + MolitUtil.getPage(root));
        }

        updateSIGUNGUCenterPosData();

        result.put("success", true);
        result.put("message", "SIGUNGU 데이터 업데이트 성공");

        return result;
    }

    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> updateSIGUNGUCenterPosData() throws Exception{
        Map<String, Object> result = new HashMap<>();
        BufferedReader br = null;

        try{
            String filePath = ConstantUtil.CRAWLING_FILE_PATH + "molit";
            String fileName = "sigungu_center_pos.csv";

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
                if(csvData.size() != 3){
                    int tmpCsvDataSize = 3 - csvData.size();

                    for(int j=0; j<tmpCsvDataSize; j++){
                        csvData.add(null);
                    }
                }

                String sggNm  = csvData.get(0).replaceAll("\"", "");
                String centerLng = csvData.get(1).replaceAll("\"", "");
                String centerLat = csvData.get(2).replaceAll("\"", "");

                SiGunGuDto siGunGuDto = siGunGuRepository.findByFullNmContainsAndSigKorNm("서울", sggNm).toDto();

                if(siGunGuDto == null){
                    continue;
                }

                siGunGuDto.setCenterLng(centerLng);
                siGunGuDto.setCenterLat(centerLat);
                log.debug(siGunGuDto);

                insert(siGunGuDto);
            }

            result.put("success", true);
            result.put("message", "시군구 중심 좌표 업데이트 성공");
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

    public void insert(SiGunGuDto siGunGuDto) throws Exception{
        SiGunGuEntity siGunGuEntity = siGunGuDto.toEntity();
        siGunGuRepository.save(siGunGuEntity);
    }

    public void insertAll(List<SiGunGuDto> siGunGuDtos) throws Exception{
        List<SiGunGuEntity> siGunGuEntities = ConvertEntityDtoListUtil.toListSiGunGuEntity(siGunGuDtos);
        siGunGuRepository.saveAll(siGunGuEntities);
    }

    public void deleteAll() throws Exception{
        siGunGuRepository.deleteAll();
        sequenceService.initSeqNumZero(ConstantUtil.SIGUNGU_SEQ_NAME);
    }
}
