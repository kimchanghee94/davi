package com.davi_server.user_data_list.service;

import com.davi_server.user_data_list.dto.UserDataListDto;
import com.davi_server.user_data_list.repository.UserDataListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserDataListService {
    UserDataListRepository userDataListRepository;

    public Map<String, Object> saveUserDataList(Map<String, Object> paramDto){
        Map<String, Object> result = new HashMap<>();

        try{
//            userDataListRepository.insertUserDataList(paramDto)
            result.put("success", true);
            result.put("message", false);
        }catch (Exception e){
            result.put("success", false);
            result.put("message", "사용자 데이터 리스트 저장 실패");
        }

        return result;
    }

    public Map<String, Object> getUserDataList(UserDataListDto userDataListDto){
        Map<String, Object> result = new HashMap<>();

        try{
            //로직 개발

            result.put("success", true);
            result.put("message", "사용자 데이터 리스트 불러오기 성공");
        }catch (Exception e){
            result.put("success", false);
            result.put("message", "사용자 데이터 리스트 불러오기 실패");
        }
        return result;
    }

    public Map<String, Object> getUserDataListByMetaType(UserDataListDto userDataListDto){
        Map<String, Object> result = new HashMap<>();

        try{
            //로직 개발

            result.put("success", true);
            result.put("message", "사용자 데이터 리스트 불러오기 성공");
        }catch (Exception e){
            result.put("success", false);
            result.put("message", "사용자 데이터 리스트 불러오기 실패");
        }
        return result;
    }
}
