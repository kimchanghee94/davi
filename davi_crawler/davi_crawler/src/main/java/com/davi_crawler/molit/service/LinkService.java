package com.davi_crawler.molit.service;

import com.davi_crawler.common.dto.DataListDto;
import com.davi_crawler.common.service.DataColListService;
import com.davi_crawler.common.service.DataListService;
import com.davi_crawler.common.service.SequenceService;
import com.davi_crawler.common.util.ApiUtil;
import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.common.util.ConvertEntityDtoListUtil;
import com.davi_crawler.molit.util.MolitUtil;
import com.davi_crawler.molit.dto.LinkDto;
import com.davi_crawler.molit.entity.LinkEntity;
import com.davi_crawler.molit.repository.LinkRepository;
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
public class LinkService {
    private final DataListService dataListService;
    private final DataColListService dataColListService;
    private final LinkRepository linkRepository;
    private final SequenceService sequenceService;

    public void setLinkDto(JSONObject root, int size) throws Exception{
        List<LinkDto> linkDtos = new ArrayList<>();

        for(int i=0; i<size; i++){
            JSONObject tmpRoot = MolitUtil.getGeoJsonSingle(root, i);

            if(tmpRoot == null){
                break;
            }

            LinkDto linkDto = new LinkDto();
            linkDto.setDataListId(ConstantUtil.DATA_LIST_LINK_ID);
            linkDto.setRoadName((String) MolitUtil.getProperties(tmpRoot).get("road_name"));
            linkDto.setMaxSpd((String) MolitUtil.getProperties(tmpRoot).get("max_spd"));
            linkDto.setRdRankH((String) MolitUtil.getProperties(tmpRoot).get("rd_rank_h"));
            linkDto.setRdTypeH((String) MolitUtil.getProperties(tmpRoot).get("rd_type_h"));
            linkDto.setRestW((String) MolitUtil.getProperties(tmpRoot).get("rest_w"));
            linkDto.setRestH((String) MolitUtil.getProperties(tmpRoot).get("rest_h"));
            linkDto.setRemark((String) MolitUtil.getProperties(tmpRoot).get("remark"));
            linkDto.setPropLinkId((String) MolitUtil.getProperties(tmpRoot).get("link_id"));
            linkDto.setJsonData(tmpRoot.toJSONString());

            linkDtos.add(linkDto);
        }

        if(linkDtos.size() != 0){
            insertAll(linkDtos);
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> updateApiData() throws Exception{
        Map<String, Object> result = new HashMap<>();

        deleteAll();

        dataColListService.updateColData(ConstantUtil.DATA_LIST_LINK_ID,
                dataListService.selectById(ConstantUtil.DATA_LIST_LINK_ID).getTableNm());

        DataListDto dataListDto = dataListService.selectById(ConstantUtil.DATA_LIST_LINK_ID);
        String url = dataListDto.getUrl();

        Map<String, String> params = MolitUtil.getMolitParams("LT_L_MOCTLINK");
        Map<String,Object> apiData = ApiUtil.getApiData(url,null,params);

        if((Boolean) apiData.get("success") == false){
            throw new Exception((String) apiData.get("message"));
        }

        JSONObject root = MolitUtil.getRoot(apiData);
        int totalPage = MolitUtil.getTotalPage(root);
        int size = MolitUtil.getSize(root);

        setLinkDto(root, size);
        log.debug("[INSERT 1] " + MolitUtil.getPage(root));

        for(int i=2; i <= totalPage; i++){
            params.put("page", String.valueOf(i));
            apiData = ApiUtil.getApiData(url,null,params);          //데이터 가져오기

            if((Boolean) apiData.get("success") == false){
                throw new Exception((String) apiData.get("message"));
            }

            root = MolitUtil.getRoot(apiData);              //새로운 jsonObject 값 가져오기

            setLinkDto(root, size);                         //dto조합 후 insert

            log.debug("[INSERT " + i + "] " + MolitUtil.getPage(root));
        }

        result.put("success", true);
        result.put("message", "LINK 데이터 업데이트 성공");

        return result;
    }

    public void insert(LinkDto linkDto) throws Exception{
        LinkEntity linkEntity = linkDto.toEntity();
        linkRepository.save(linkEntity);
    }

    public void insertAll(List<LinkDto> linkDtos) throws Exception{
        List<LinkEntity> linkEntities = ConvertEntityDtoListUtil.toListLinkEntity(linkDtos);
        linkRepository.saveAll(linkEntities);
    }

    public void deleteAll() throws Exception{
        linkRepository.deleteAll();
        sequenceService.initSeqNumZero(ConstantUtil.LINK_SEQ_NAME);
    }
}
