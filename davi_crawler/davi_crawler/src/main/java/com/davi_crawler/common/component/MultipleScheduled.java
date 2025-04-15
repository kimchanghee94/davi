package com.davi_crawler.common.component;

import com.davi_crawler.common.service.BatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
@RequiredArgsConstructor
public class MultipleScheduled {
    private final BatchService batchService;
    private static final Map<String, ThreadPoolTaskScheduler> scheduledMap = new HashMap<>();
    private String cron = "*/10 * * * * *";
    public Map<String, Object> result = new HashMap<>();
    public Map<String, Object> stopResult = new HashMap<>();


    public void setCron(String cron){
        this.cron = cron;
    }

    public Map<String, Object> startScheduler(String serviceType){
        try{
            ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
            scheduler.initialize();

            //이미 등록되어있는 경우
            if(scheduledMap.get(serviceType) != null){
                stopScheduler(serviceType);
            }
            scheduledMap.put(serviceType, scheduler);
            scheduler.schedule(getRunnable(serviceType), getTrigger());
            result.put("success", true);
            result.put("message", serviceType + " 배치 주기 설정 완료");
        }catch (Exception e){
            result.put("success", false);
            result.put("message", serviceType + " 배치 주기 설정 실패");
        }

        return result;
    }

    public Map<String, Object> stopScheduler(String serviceType){
        ThreadPoolTaskScheduler scheduler = scheduledMap.get(serviceType);
        //이미 등록되어있으나 실행 중이지 아닐 경우
        if(scheduler != null){
            scheduledMap.remove(serviceType);
            scheduler.shutdown();

            stopResult.put("success", true);
            stopResult.put("message", serviceType + " 배치 주기 종료 완료");
        }else{
            stopResult.put("success", false);
            stopResult.put("message", serviceType + "이(가) 스케줄러에 등록되있지 않습니다.");
        }
        return stopResult;
    }

    private Runnable getRunnable(String serviceType){
        Map<String, Object> paramDto = new HashMap<>();
        return () -> {
            paramDto.put("serviceType", serviceType);
            result = batchService.startJob(paramDto);
        };
    }

    private Trigger getTrigger(){
        return new CronTrigger(this.cron);
    }
}
