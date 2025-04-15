package com.davi_crawler.traffic.service;

import com.davi_crawler.common.service.DataColListService;
import com.davi_crawler.common.service.DataListService;
import com.davi_crawler.common.service.SequenceService;
import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.common.util.ConvertEntityDtoListUtil;
import com.davi_crawler.traffic.dto.SpeedDumpDto;
import com.davi_crawler.traffic.entity.SpeedDumpEntity;
import com.davi_crawler.traffic.repository.SpeedDumpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class SpeedDumpService {
    private final DataListService dataListService;
    private final DataColListService dataColListService;
    private final SpeedDumpRepository speedDumpRepository;
    private final SequenceService sequenceService;

    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> updateApiData() throws Exception{
        Map<String, Object> result = new HashMap<>();
        FileInputStream fis = null;

        try{
            deleteAll();

            dataColListService.updateColData(ConstantUtil.DATA_LIST_SPEEDDUMP_ID,
                    dataListService.selectById(ConstantUtil.DATA_LIST_SPEEDDUMP_ID).getTableNm());

            String filePath = ConstantUtil.CRAWLING_FILE_PATH + "traffic";
            String fileName = "speed-dump.xlsx";

            fis = new FileInputStream(new File(filePath, fileName));
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);

            List<SpeedDumpDto> speedDumpDtos = new ArrayList<>();
            int rLen = sheet.getPhysicalNumberOfRows();

            for(int rIdx = 1; rIdx < rLen; rIdx++){
                XSSFRow row = sheet.getRow(rIdx);
                if(row != null){
                    int cLen = row.getLastCellNum();

                    List<String> cellVal = new ArrayList<>();

                    for(int cIdx = 1; cIdx < cLen; cIdx++){
                        XSSFCell cell = row.getCell(cIdx);
                        String value = "";

                        if(cell == null){
                            value = "null";
                        }else{
                            value = cell.getStringCellValue();
                        }

                        cellVal.add(value);
                    }

                    log.debug(cellVal);

                    speedDumpDtos.add(new SpeedDumpDto(null, ConstantUtil.DATA_LIST_SPEEDDUMP_ID,
                            cellVal.get(0), cellVal.get(1), cellVal.get(2), cellVal.get(3), cellVal.get(4), cellVal.get(5),
                            cellVal.get(6), cellVal.get(7), cellVal.get(8), cellVal.get(9), cellVal.get(10), cellVal.get(11),
                            cellVal.get(12), cellVal.get(13), cellVal.get(14), cellVal.get(15), cellVal.get(16), cellVal.get(17),
                            cellVal.get(18), cellVal.get(19), cellVal.get(20), cellVal.get(21)));
                }

            }

            insertAll(speedDumpDtos);

            result.put("success", true);
            result.put("message", "과속방지턱 정보 업데이트 성공");
        }finally {
            try{
                if(fis != null){
                    fis.close();
                }
            }catch (IOException ie){
                ie.printStackTrace();
            }
        }

        return result;
    }

    public void insert(SpeedDumpDto speedDumpDto) throws Exception{
        SpeedDumpEntity speedDumpEntity = speedDumpDto.toEntity();
        speedDumpRepository.save(speedDumpEntity);
    }

    public void insertAll(List<SpeedDumpDto> speedDumpDtos) throws Exception{
        List<SpeedDumpEntity> speedDumpEntities = ConvertEntityDtoListUtil.toListSpeedDumpEntity(speedDumpDtos);
        speedDumpRepository.saveAll(speedDumpEntities);
    }

    public void deleteAll() throws Exception{
        speedDumpRepository.deleteAll();
        sequenceService.initSeqNumZero(ConstantUtil.SPEEDDUMP_SEQ_NAME);
    }
}
