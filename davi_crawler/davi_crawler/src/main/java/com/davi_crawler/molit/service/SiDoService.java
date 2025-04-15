package com.davi_crawler.molit.service;

import com.davi_crawler.common.dto.DataListDto;
import com.davi_crawler.common.service.DataColListService;
import com.davi_crawler.common.service.DataListService;
import com.davi_crawler.common.service.SequenceService;
import com.davi_crawler.common.util.ApiUtil;
import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.common.util.ConvertEntityDtoListUtil;
import com.davi_crawler.molit.util.MolitUtil;
import com.davi_crawler.molit.dto.SiDoDto;
import com.davi_crawler.molit.entity.SiDoEntity;
import com.davi_crawler.molit.repository.SiDoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class SiDoService {
    private final DataListService dataListService;
    private final DataColListService dataColListService;
    private final SiDoRepository siDoRepository;
    private final SequenceService sequenceService;

    public void setSiDoDto(JSONObject root, int size) throws Exception{
        List<SiDoDto> siDoDtos = new ArrayList<>();

        for(int i=0; i<size; i++){
            JSONObject tmpRoot = MolitUtil.getGeoJsonSingle(root, i);

            if(tmpRoot == null){
                break;
            }

            SiDoDto siDoDto = new SiDoDto();
            siDoDto.setDataListId(ConstantUtil.DATA_LIST_SIDO_ID);
            siDoDto.setCtpEngNm(MolitUtil.getPropertiesName(tmpRoot, MolitUtil.SIDO_PREFIX, 0));
            siDoDto.setCtpKorNm(MolitUtil.getPropertiesName(tmpRoot, MolitUtil.SIDO_PREFIX, 1));
            siDoDto.setCtprvnCd(MolitUtil.getPropertiesCd(tmpRoot, MolitUtil.SIDO_PREFIX + "rvn"));
            siDoDto.setJsonData(tmpRoot.toJSONString());

            siDoDtos.add(siDoDto);
        }

        if(siDoDtos.size() != 0){
            insertAll(siDoDtos);
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> updateApiData() throws Exception{
        Map<String, Object> result = new HashMap<>();

        deleteAll();

        dataColListService.updateColData(ConstantUtil.DATA_LIST_SIDO_ID,
                dataListService.selectById(ConstantUtil.DATA_LIST_SIDO_ID).getTableNm());

        DataListDto dataListDto = dataListService.selectById(ConstantUtil.DATA_LIST_SIDO_ID);
        String url = dataListDto.getUrl();

        Map<String, String> params = MolitUtil.getMolitParams("LT_C_ADSIDO_INFO");
        Map<String,Object> apiData = ApiUtil.getApiData(url,null,params);

        if((Boolean) apiData.get("success") == false){
            throw new Exception((String) apiData.get("message"));
        }

        JSONObject root = MolitUtil.getRoot(apiData);
        int totalPage = MolitUtil.getTotalPage(root);
        int size = MolitUtil.getSize(root);

        setSiDoDto(root, size);
        log.debug("[INSERT 1] " + MolitUtil.getPage(root));

        for(int i=2; i <= totalPage; i++){
            params.put("page", String.valueOf(i));
            apiData = ApiUtil.getApiData(url,null,params);          //데이터 가져오기

            if((Boolean) apiData.get("success") == false){
                throw new Exception((String) apiData.get("message"));
            }

            root = MolitUtil.getRoot(apiData);            //새로운 jsonObject 값 가져오기

            setSiDoDto(root, size);                       //dto조합 후 insert

            log.debug("[INSERT " + i + "] " + MolitUtil.getPage(root));
        }

        result.put("success", true);
        result.put("message", "SIDO 데이터 업데이트 성공");

        return result;
    }

    public void insert(SiDoDto siDoDto) throws Exception{
        SiDoEntity siDoEntity = siDoDto.toEntity();
        siDoRepository.save(siDoEntity);
    }

    public void insertAll(List<SiDoDto> siDoDtos) throws Exception{
        List<SiDoEntity> siDoEntities = ConvertEntityDtoListUtil.toListSiDoEntity(siDoDtos);
        siDoRepository.saveAll(siDoEntities);
    }

    public void deleteAll() throws Exception{
        siDoRepository.deleteAll();
        sequenceService.initSeqNumZero(ConstantUtil.SIDO_SEQ_NAME);
    }
}
