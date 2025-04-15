package com.davi_crawler.traffic.service;

import com.davi_crawler.common.service.DataColListService;
import com.davi_crawler.common.service.DataListService;
import com.davi_crawler.common.service.SequenceService;
import com.davi_crawler.common.util.ApiUtil;
import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.common.util.ConvertEntityDtoListUtil;
import com.davi_crawler.traffic.dto.BusRoutePathDto;
import com.davi_crawler.traffic.dto.BusSeoulInfoDto;
import com.davi_crawler.traffic.entity.BusRoutePathEntity;
import com.davi_crawler.traffic.entity.BusSeoulInfoEntity;
import com.davi_crawler.traffic.repository.BusRoutePathRepository;
import com.davi_crawler.traffic.repository.BusSeoulInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class BusRoutePathService {
    private final DataListService dataListService;
    private final DataColListService dataColListService;
    private final BusRoutePathRepository busRoutePathRepository;
    private final BusSeoulInfoRepository busSeoulInfoRepository;
    private final SequenceService sequenceService;

    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> updateApiData() throws Exception{
        Map<String, Object> result = new HashMap<>();

        deleteAll();

        dataColListService.updateColData(ConstantUtil.DATA_LIST_BUSROUTEPATH_ID,
                dataListService.selectById(ConstantUtil.DATA_LIST_BUSROUTEPATH_ID).getTableNm());

        List<BusRoutePathDto> busRoutePathDtos = new ArrayList<>();

        List<BusSeoulInfoEntity> busSeoulInfoEntities = busSeoulInfoRepository.findByNodeOrder("1");
        List<BusSeoulInfoDto> busSeoulInfoDtos = ConvertEntityDtoListUtil.toListBusSeoulInfoDto(busSeoulInfoEntities);

        for(BusSeoulInfoDto busSeoulInfoDto : busSeoulInfoDtos){
            BusRoutePathDto busRoutePathDto = new BusRoutePathDto();
            busRoutePathDto.setDataListId(ConstantUtil.DATA_LIST_BUSROUTEPATH_ID);
            busRoutePathDto.setRouteId(busSeoulInfoDto.getRouteId());

            //bus route path url
            String url = dataListService.selectById(ConstantUtil.DATA_LIST_BUSROUTEPATH_ID).getUrl();

            //param 조합
            Map<String, String> params = new HashMap<>();
            params.put("serviceKey", ConstantUtil.PDP_SERVICE_KEY);
            params.put("busRouteId", busRoutePathDto.getRouteId());

            Map<String, Object> apiData = ApiUtil.getApiData(url, null, params);

            if((Boolean)apiData.get("success") == false){
                throw new Exception((String) apiData.get("message"));
            }

            //xml parsing
            Document document = Jsoup.parse((String)apiData.get("data"));
            if(!document.getElementsByTag("headerCd").text().equals("0")){
                continue;
            }

            Elements elements = document.getElementsByTag("itemList");
            JSONArray jsonArray = new JSONArray();

            for(Element element : elements){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("lat", element.getElementsByTag("gpsY").text());
                jsonObject.put("lng", element.getElementsByTag("gpsX").text());

                jsonArray.add(jsonObject);
            }

            busRoutePathDto.setJsonData(jsonArray.toJSONString());
            log.debug(busRoutePathDto);

            busRoutePathDtos.add(busRoutePathDto);
        }

        insertAll(busRoutePathDtos);

        result.put("success", true);
        result.put("message", "버스 라우트 경로 정보 업데이트 성공");

        return result;
    }

    public void insert(BusRoutePathDto busRoutePathDto) throws Exception{
        BusRoutePathEntity busRoutePathEntity = busRoutePathDto.toEntity();
        busRoutePathRepository.save(busRoutePathEntity);
    }

    public void insertAll(List<BusRoutePathDto> busRoutePathDtos) throws Exception{
        List<BusRoutePathEntity> busRoutePathEntities = ConvertEntityDtoListUtil.toListBusRoutePathEntity(busRoutePathDtos);
        busRoutePathRepository.saveAll(busRoutePathEntities);
    }

    public void deleteAll() throws Exception {
        busRoutePathRepository.deleteAll();
        sequenceService.initSeqNumZero(ConstantUtil.BUSROUTEPATH_SEQ_NAME);
    }
}
