package com.davi_server.molit.controller;

import com.davi_server.common.util.ConstantUtil;
import com.davi_server.molit.dto.LinkDto;
import com.davi_server.molit.service.LinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/molit/link/*")
public class LinkController {
    private final LinkService linkService;

    //http://localhost:7272/molit/link/one/link-id
    @RequestMapping(value="/one/link-id", method=RequestMethod.POST)
    public Map<String, Object> getOneByPropLinkId(@RequestBody List<LinkDto> linkDtos){
        log.debug("Get Link One By Prop Link Id " + linkDtos);
        return linkService.selectByPropLinkId(linkDtos);
    }

    //http://localhost:7272/molit/link/list
    @RequestMapping(value="/list", method=RequestMethod.POST)
    public Map<String, Object> getList(){
        log.debug("Get Link List");
        return linkService.selectList();
    }

    //http://localhost:7272/molit/link/sample-list
    @RequestMapping(value="/sample-list", method=RequestMethod.POST)
    public Map<String, Object> getSampleList(){
        log.debug("Get Link Sample List");
        return linkService.selectSampleList(ConstantUtil.CACHE_LINK_KEY);
    }
}
