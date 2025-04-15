package com.davi_server.user_data_list.controller;

import com.davi_server.user_data_list.dto.UserDataListDto;
import com.davi_server.user_data_list.service.UserDataListService;
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
@RequestMapping("/user-data-list/*")
public class UserDataListController {
    private final UserDataListService userDataListService;

    //http://localhost:7272/user-data-list/save
    @RequestMapping(value="/save", method = RequestMethod.POST)
    public Map<String, Object> saveUserDataList(@RequestBody Map<String, Object> paramDto){
        log.debug("Save User Data List By " + paramDto);
        return userDataListService.saveUserDataList(paramDto);
    }

    //http://localhost:7272/user-data-list/list
    @RequestMapping(value="/list", method = RequestMethod.POST)
    public Map<String, Object> getUserDataList(@RequestBody UserDataListDto userDataListDto){
        log.debug("Get User Data List By " + userDataListDto);
        return userDataListService.getUserDataList(userDataListDto);
    }
}
