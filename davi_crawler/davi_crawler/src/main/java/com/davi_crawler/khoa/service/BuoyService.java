package com.davi_crawler.khoa.service;

import com.davi_crawler.common.service.DataColListService;
import com.davi_crawler.common.service.DataListService;
import com.davi_crawler.common.service.SequenceService;
import com.davi_crawler.common.util.ApiUtil;
import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.common.util.ConvertEntityDtoListUtil;
import com.davi_crawler.khoa.dto.BuoyDto;
import com.davi_crawler.khoa.entity.BuoyEntity;
import com.davi_crawler.khoa.repository.BuoyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
public class BuoyService {
    private final DataListService dataListService;
    private final BuoyRepository buoyRepository;
    private final SequenceService sequenceService;

    private final DataColListService dataColListService;
    
    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> updateApiData() throws Exception{
        Map<String, Object> result = new HashMap<>();

        deleteAll();

        dataColListService.updateColData(ConstantUtil.DATA_LIST_BUOY_ID,
                dataListService.selectById(ConstantUtil.DATA_LIST_BUOY_ID).getTableNm());

        //등대표 url
        String url = dataListService.selectById(ConstantUtil.DATA_LIST_BUOY_ID).getUrl();

        int pageNo = 1;

        //param 조합
        Map<String, String> params = new HashMap<>();
        params.put("ServiceKey", ConstantUtil.PDP_SERVICE_KEY);
        params.put("numOfRows", "1000");
        params.put("pageNo", String.valueOf(pageNo));

        while (true){
            Map<String, Object> apiData = ApiUtil.getApiData(url,null,params);

            if((Boolean) apiData.get("success") == false){
                throw new Exception((String) apiData.get("message"));
            }

            //xml parsing
            Document document = Jsoup.parse((String)apiData.get("data"));
            if(!document.getElementsByTag("resultCode").text().equals("00")){
                break;
            }

            Elements elements = document.getElementsByTag("item");

            List<BuoyDto> buoyDtos = new ArrayList<>();

            //item별 dto뽑기
            for(Element element : elements){
                BuoyDto buoyDto = new BuoyDto();
                buoyDto.setDataListId(ConstantUtil.DATA_LIST_BUOY_ID);
                buoyDto.setBlfrNo(element.getElementsByTag("blfrno").text());
                buoyDto.setBuoyKr(element.getElementsByTag("buoykr").text());
                buoyDto.setBuoyEn(element.getElementsByTag("buoyen").text());
                buoyDto.setBuoyNm(element.getElementsByTag("buoynm").text());
                buoyDto.setSeaNm(element.getElementsByTag("seanm").text());
                buoyDto.setLgtProperty(element.getElementsByTag("lgt_property").text());
                buoyDto.setRemark(element.getElementsByTag("remark").text());

                //좌표넣기
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("x", element.getElementsByTag("wgs84east").text());
                jsonObject.put("y", element.getElementsByTag("wgs84north").text());
                buoyDto.setLngLat(jsonObject.toJSONString());
                log.debug(buoyDto);

                buoyDtos.add(buoyDto);
            }

            insertAll(buoyDtos);

            pageNo++;
            params.put("pageNo", String.valueOf(pageNo));
        }

        result.put("success", true);
        result.put("message", "등대표 업데이트 성공");

        return result;
    }

    public void insert(BuoyDto buoyDto) throws Exception{
        BuoyEntity buoyEntity = buoyDto.toEntity();
        buoyRepository.save(buoyEntity);
    }

    public void insertAll(List<BuoyDto> buoyDtos) throws Exception{
        List<BuoyEntity> buoyEntities = ConvertEntityDtoListUtil.toListBuoyEntity(buoyDtos);
        buoyRepository.saveAll(buoyEntities);
    }

    public void deleteAll() throws Exception{
        buoyRepository.deleteAll();
        sequenceService.initSeqNumZero(ConstantUtil.BUOY_SEQ_NAME);
    }
}
