package com.davi_server.user_data_list.repository;

import com.davi_server.user_data_list.dto.UserDataListDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDataListRepository {
    int insertUserDataList(UserDataListDto userDataListDto) throws Exception;
}
