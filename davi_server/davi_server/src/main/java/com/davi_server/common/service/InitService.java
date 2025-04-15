package com.davi_server.common.service;

import com.davi_server.common.util.ConstantUtil;
import com.davi_server.khoa.service.BuoyService;
import com.davi_server.khoa.service.HangService;
import com.davi_server.khoa.service.LsmdContService;
import com.davi_server.khoa.service.SeaDistService;
import com.davi_server.molit.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class InitService {
    private final CacheManager cacheManager;

    private final EupMyeonDongService eupMyeonDongService;
    private final LiService liService;
    private final LinkService linkService;
    private final RoadService roadService;
    private final SiDoService siDoService;
    private final SiGunGuService siGunGuService;
    private final SeaDistService seaDistService;
    private final BuoyService buoyService;
    private final HangService hangService;
    private final LsmdContService lsmdContService;

    //서비스가 시작되고 캐쉬에 대용량 데이터들을 미리 저장해둔다.
    @PostConstruct
    public void getMolitDataAll(){
        Map<String, Object> eupMyeonDongDtos = eupMyeonDongService.selectSampleList(ConstantUtil.CACHE_EUPMYEONDONG_KEY);
        Map<String, Object> liDtos = liService.selectSampleList(ConstantUtil.CACHE_LI_KEY);
        Map<String, Object> linkDtos = linkService.selectSampleList(ConstantUtil.CACHE_LINK_KEY);
        Map<String, Object> roadDtos = roadService.selectSampleList(ConstantUtil.CACHE_ROAD_KEY);
        Map<String, Object> siDoDtos = siDoService.selectSampleList(ConstantUtil.CACHE_SIDO_KEY);
        Map<String, Object> siGunGuDtos = siGunGuService.selectSampleList(ConstantUtil.CACHE_SIGUNGU_KEY);
        Map<String, Object> seaDistDtos = seaDistService.selectSampleList(ConstantUtil.CACHE_SEADIST_KEY);
        Map<String, Object> buoyDtos = buoyService.selectSampleList(ConstantUtil.CACHE_BUOY_KEY);
        Map<String, Object> hangDtos = hangService.selectSampleList(ConstantUtil.CACHE_HANG_KEY);
        Map<String, Object> lsmdContDtos = lsmdContService.selectSampleList(ConstantUtil.CACHE_LSMDCONT_KEY);

        cacheManager.getCache(ConstantUtil.CACHE_MOLIT_VALUE).putIfAbsent(ConstantUtil.CACHE_EUPMYEONDONG_KEY,  eupMyeonDongDtos);
        cacheManager.getCache(ConstantUtil.CACHE_MOLIT_VALUE).putIfAbsent(ConstantUtil.CACHE_LI_KEY, liDtos);
        cacheManager.getCache(ConstantUtil.CACHE_MOLIT_VALUE).putIfAbsent(ConstantUtil.CACHE_LINK_KEY, linkDtos);
        cacheManager.getCache(ConstantUtil.CACHE_MOLIT_VALUE).putIfAbsent(ConstantUtil.CACHE_ROAD_KEY, roadDtos);
        cacheManager.getCache(ConstantUtil.CACHE_MOLIT_VALUE).putIfAbsent(ConstantUtil.CACHE_SIDO_KEY, siDoDtos);
        cacheManager.getCache(ConstantUtil.CACHE_MOLIT_VALUE).putIfAbsent(ConstantUtil.CACHE_SIGUNGU_KEY, siGunGuDtos);
        cacheManager.getCache(ConstantUtil.CACHE_KHOA_VALUE).putIfAbsent(ConstantUtil.CACHE_SEADIST_KEY, seaDistDtos);
        cacheManager.getCache(ConstantUtil.CACHE_KHOA_VALUE).putIfAbsent(ConstantUtil.CACHE_BUOY_KEY, buoyDtos);
        cacheManager.getCache(ConstantUtil.CACHE_KHOA_VALUE).putIfAbsent(ConstantUtil.CACHE_HANG_KEY, hangDtos);
        cacheManager.getCache(ConstantUtil.CACHE_KHOA_VALUE).putIfAbsent(ConstantUtil.CACHE_LSMDCONT_KEY, lsmdContDtos);
    }

    public void reGetMolitDataAll(){
        Map<String, Object> eupMyeonDongDtos = eupMyeonDongService.reSelectSampleList(ConstantUtil.CACHE_EUPMYEONDONG_KEY);
        Map<String, Object> liDtos = liService.reSelectSampleList(ConstantUtil.CACHE_LI_KEY);
        Map<String, Object> linkDtos = linkService.reSelectSampleList(ConstantUtil.CACHE_LINK_KEY);
        Map<String, Object> roadDtos = roadService.reSelectSampleList(ConstantUtil.CACHE_ROAD_KEY);
        Map<String, Object> siDoDtos = siDoService.reSelectSampleList(ConstantUtil.CACHE_SIDO_KEY);
        Map<String, Object> siGunGuDtos = siGunGuService.reSelectSampleList(ConstantUtil.CACHE_SIGUNGU_KEY);
        Map<String, Object> seaDistDtos = seaDistService.selectSampleList(ConstantUtil.CACHE_SEADIST_KEY);
        Map<String, Object> buoyDtos = buoyService.selectSampleList(ConstantUtil.CACHE_BUOY_KEY);
        Map<String, Object> hangDtos = hangService.selectSampleList(ConstantUtil.CACHE_HANG_KEY);
        Map<String, Object> lsmdContDtos = lsmdContService.selectSampleList(ConstantUtil.CACHE_LSMDCONT_KEY);
    }
}
