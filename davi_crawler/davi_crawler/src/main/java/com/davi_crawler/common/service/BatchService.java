package com.davi_crawler.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class BatchService {
    private final Job job;
    private final JobLauncher jobLauncher;

    public Map<String, Object> startJob(Map<String, Object> paramDto){
        Map<String, Object> result = new HashMap<>();
        Map<String, JobParameter> parameters = new HashMap<>();

        String serviceType = (String) paramDto.get("serviceType");

        parameters.put("timeStamp", new JobParameter(System.currentTimeMillis()));
        parameters.put("serviceType", new JobParameter(serviceType));

        try{
            //jobLauncer에 주입되는 메서드 내부에 비동기식 설정이 되어있다.
            JobExecution jobExecution = jobLauncher.run(job, new JobParameters(parameters));

            result.put("success", true);
            result.put("message", serviceType + " Batch 실행 결과 성공");
            result.put("status", jobExecution.getStatus());

        }catch (Exception e){
            e.printStackTrace();
            result.put("message", "Batch 실행 결과 실패");
        }

        return result;
    }
}
