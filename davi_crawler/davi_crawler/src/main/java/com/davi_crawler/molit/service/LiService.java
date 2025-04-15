package com.davi_crawler.molit.service;

import com.davi_crawler.common.dto.DataListDto;
import com.davi_crawler.common.service.DataColListService;
import com.davi_crawler.common.service.DataListService;
import com.davi_crawler.common.service.SequenceService;
import com.davi_crawler.common.util.ApiUtil;
import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.common.util.ConvertEntityDtoListUtil;
import com.davi_crawler.molit.util.MolitUtil;
import com.davi_crawler.molit.dto.LiDto;
import com.davi_crawler.molit.entity.LiEntity;
import com.davi_crawler.molit.repository.LiRepository;
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
public class LiService {
    private final DataListService dataListService;
    private final DataColListService dataColListService;
    private final LiRepository liRepository;
    private final SequenceService sequenceService;

    public void setLiDto(JSONObject root, int size) throws Exception{
        List<LiDto> liDtos = new ArrayList<>();

        for (int i=0; i<size; i++){
            JSONObject tmpRoot = MolitUtil.getGeoJsonSingle(root, i);

            if(tmpRoot == null){
                break;
            }

            LiDto liDto = new LiDto();
            liDto.setDataListId(ConstantUtil.DATA_LIST_LI_ID);
            liDto.setLiEngNm(MolitUtil.getPropertiesName(tmpRoot, MolitUtil.LI_PREFIX, 0));
            liDto.setLiKorNm(MolitUtil.getPropertiesName(tmpRoot, MolitUtil.LI_PREFIX, 1));
            liDto.setFullNm(MolitUtil.getPropertiesName(tmpRoot, MolitUtil.LI_PREFIX, 2));
            liDto.setLiCd(MolitUtil.getPropertiesCd(tmpRoot, MolitUtil.LI_PREFIX));
            liDto.setJsonData(tmpRoot.toJSONString());

            liDtos.add(liDto);
        }
        if(liDtos.size() != 0){
            insertAll(liDtos);
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> updateApiData() throws Exception{
        Map<String, Object> result = new HashMap<>();

        deleteAll();

        dataColListService.updateColData(ConstantUtil.DATA_LIST_LI_ID,
                dataListService.selectById(ConstantUtil.DATA_LIST_LI_ID).getTableNm());

        DataListDto dataListDto = dataListService.selectById(ConstantUtil.DATA_LIST_LI_ID);
        String url = dataListDto.getUrl();

        Map<String, String> params = MolitUtil.getMolitParams("LT_C_ADRI_INFO");
        Map<String,Object> apiData = ApiUtil.getApiData(url,null,params);

        if((Boolean) apiData.get("success") == false){
            throw new Exception((String) apiData.get("message"));
        }

        JSONObject root = MolitUtil.getRoot(apiData);
        int totalPage = MolitUtil.getTotalPage(root);
        int size = MolitUtil.getSize(root);

        setLiDto(root, size);
        log.debug("[INSERT 1] " + MolitUtil.getPage(root));

        for(int i=2; i <= totalPage; i++){
            params.put("page", String.valueOf(i));
            apiData = ApiUtil.getApiData(url,null,params);          //데이터 가져오기

            if((Boolean) apiData.get("success") == false){
                throw new Exception((String) apiData.get("message"));
            }

            root = MolitUtil.getRoot(apiData);          //새로운 jsonObject 값 가져오기

            setLiDto(root, size);                       //dto조합 후 insert

            log.debug("[INSERT " + i + "] " + MolitUtil.getPage(root));
        }

        result.put("success", true);
        result.put("message", "LI 데이터 업데이트 성공");

        return result;
    }

    public void insert(LiDto liDto) throws Exception{
        LiEntity liEntity = liDto.toEntity();
        liRepository.save(liEntity);
    }

    public void insertAll(List<LiDto> liDtos) throws Exception{
        List<LiEntity> liEntities = ConvertEntityDtoListUtil.toListLiEntity(liDtos);
        liRepository.saveAll(liEntities);
    }

    public void deleteAll() throws Exception{
        liRepository.deleteAll();
        sequenceService.initSeqNumZero(ConstantUtil.LI_SEQ_NAME);
    }
}
