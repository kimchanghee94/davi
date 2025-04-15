package com.davi_server.common.controller;

import com.davi_server.common.dto.JoinDto;
import com.davi_server.common.service.JoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/common/join/*")
public class JoinController {
    private final JoinService joinService;

    //http://localhost:7272/common/join/related
    @RequestMapping(value="/related", method = RequestMethod.POST)
    public Map<String, Object> getRelatedData(@RequestBody JoinDto joinDto){
        log.debug("Get Related Data By " + joinDto);
        return joinService.selectRelatedList(joinDto);
    }
}
