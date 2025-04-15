package com.davi_crawler.config.batch;

import com.davi_crawler.common.service.GridService;
import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.disaster.entity.Seoul113Entity;
import com.davi_crawler.disaster.service.ChimsuService;
import com.davi_crawler.disaster.service.CivilDefenseService;
import com.davi_crawler.disaster.service.Seoul113Service;
import com.davi_crawler.khoa.service.BuoyService;
import com.davi_crawler.khoa.service.HangService;
import com.davi_crawler.khoa.service.LsmdContService;
import com.davi_crawler.khoa.service.SeaDistService;
import com.davi_crawler.molit.service.*;
import com.davi_crawler.public_data_portal.service.PublicDataPortalService;
import com.davi_crawler.traffic.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Log4j2
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final PublicDataPortalService publicDataPortalService;
    private final SiDoService siDoService;
    private final SiGunGuService siGunGuService;
    private final EupMyeonDongService eupMyeonDongService;
    private final LiService liService;
    private final LinkService linkService;
    private final RoadService roadService;
    private final SeaDistService seaDistService;
    private final HangService hangService;
    private final BuoyService buoyService;
    private final LsmdContService lsmdContService;
    private final CctvService cctvService;
    private final ChildZoneService childZoneService;
    private final CrosswalkService crosswalkService;
    private final TrafficLightService trafficLightService;
    private final SpeedDumpService speedDumpService;
    private final RseService rseService;
    private final FrostService frostService;
    private final FogService fogService;
    private final BusRouteLinkService busRouteLinkService;
    private final BusSeoulInfoService busSeoulInfoService;
    private final BusRoutePathService busRoutePathService;
    private final CivilDefenseService civilDefenseService;
    private final Seoul113Service seoul113Service;
    private final GridService gridService;
    private final ChimsuService chimsuService;

    @Bean
    public Job daviJob(){
        return jobBuilderFactory.get("daviJob")
                .start(step())
                .build();
    }

    @JobScope
    @Bean
    public Step step(){
        return stepBuilderFactory.get("step")
                .tasklet(tasklet())
                .build();
    }

    @StepScope
    @Bean
    public Tasklet tasklet(){
        return ((contribution, chunkContext) -> {
            final JobExecution jobExecution = chunkContext.getStepContext().getStepExecution().getJobExecution();

            String serviceTypeStr = jobExecution.getJobParameters().getString("serviceType");
            Long serviceType = Long.parseLong(serviceTypeStr);

            if(serviceType == null){
                return RepeatStatus.FINISHED;
            }

            if(serviceType.equals(ConstantUtil.DATA_LIST_PUBLICDATAPORTAL_ID)){
                publicDataPortalService.updateApiData();
            }else if(serviceType.equals(ConstantUtil.DATA_LIST_SIDO_ID)){
                siDoService.updateApiData();
            }else if(serviceType.equals(ConstantUtil.DATA_LIST_SIGUNGU_ID)){
                siGunGuService.updateApiData();
            }else if(serviceType.equals(ConstantUtil.DATA_LIST_EUPMYEONDONG_ID)){
                eupMyeonDongService.updateApiData();
            }else if(serviceType.equals(ConstantUtil.DATA_LIST_LI_ID)){
                liService.updateApiData();
            }else if(serviceType.equals(ConstantUtil.DATA_LIST_LINK_ID)){
                linkService.updateApiData();
            }else if(serviceType.equals(ConstantUtil.DATA_LIST_ROAD_ID)){
                roadService.updateApiData();
            }else if(serviceType.equals(ConstantUtil.DATA_LIST_SEADIST_ID)){
                seaDistService.updateApiData();
            }else if(serviceType.equals(ConstantUtil.DATA_LIST_HANG_ID)){
                hangService.updateApiData();
            }else if(serviceType.equals(ConstantUtil.DATA_LIST_BUOY_ID)){
                buoyService.updateApiData();
            }else if(serviceType.equals(ConstantUtil.DATA_LIST_LSMDCONT_ID)){
                lsmdContService.updateApiData();
            }else if(serviceType.equals(ConstantUtil.DATA_LIST_CCTV_ID)){
                cctvService.updateApiData();
            }else if(serviceType.equals(ConstantUtil.DATA_LIST_CHILDZONE_ID)){
                childZoneService.updateApiData();
            }else if(serviceType.equals(ConstantUtil.DATA_LIST_CROSSWALK_ID)){
                crosswalkService.updateApiData();
            }else if(serviceType.equals(ConstantUtil.DATA_LIST_TRAFFICLIGHT_ID)){
                trafficLightService.updateApiData();
            }else if(serviceType.equals(ConstantUtil.DATA_LIST_SPEEDDUMP_ID)){
                speedDumpService.updateApiData();
            }else if(serviceType.equals(ConstantUtil.DATA_LIST_RSE_ID)){
                rseService.updateApiData();
            }else if(serviceType.equals(ConstantUtil.DATA_LIST_FROST_ID)){
                frostService.updateApiData();
            }else if(serviceType.equals(ConstantUtil.DATA_LIST_FOG_ID)){
                fogService.updateApiData();
            }else if(serviceType.equals(ConstantUtil.DATA_LIST_BUSROUTELINK_ID)){
                busRouteLinkService.updateApiData();
            }else if(serviceType.equals(ConstantUtil.DATA_LIST_BUSSEOULINFO_ID)){
                busSeoulInfoService.updateApiData();
            }else if(serviceType.equals(ConstantUtil.DATA_LIST_BUSROUTEPATH_ID)){
                busRoutePathService.updateApiData();
            }else if(serviceType.equals(ConstantUtil.DATA_LIST_CIVILDEFENSE_ID)){
                civilDefenseService.updateApiData();
            }else if(serviceType.equals(ConstantUtil.DATA_LIST_SEOUL113_ID)){
                seoul113Service.updateApiData();
            }else if(serviceType.equals(ConstantUtil.DATA_LIST_GRID_ID)){
                gridService.updateApiData();
            }else if(serviceType.equals(ConstantUtil.DATA_LIST_CHIMSU_ID)){
                chimsuService.updateApiData();
            }

            return RepeatStatus.FINISHED;
        });
    }
}
