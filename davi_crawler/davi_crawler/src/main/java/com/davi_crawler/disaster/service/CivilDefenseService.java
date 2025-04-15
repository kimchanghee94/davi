package com.davi_crawler.disaster.service;

import com.davi_crawler.common.service.DataColListService;
import com.davi_crawler.common.service.DataListService;
import com.davi_crawler.common.service.SequenceService;
import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.common.util.ConvertEntityDtoListUtil;
import com.davi_crawler.disaster.dto.CivilDefenseDto;
import com.davi_crawler.disaster.entity.CivilDefenseEntity;
import com.davi_crawler.disaster.repository.CivilDefenseRepository;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class CivilDefenseService {
    private final DataListService dataListService;
    private final DataColListService dataColListService;
    private final SequenceService sequenceService;
    private final CivilDefenseRepository civilDefenseRepository;

    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> updateApiData() throws Exception{
        Map<String, Object> result = new HashMap<>();
        FileInputStream fis = null;

        try{
            deleteAll();

            dataColListService.updateColData(ConstantUtil.DATA_LIST_CIVILDEFENSE_ID,
                    dataListService.selectById(ConstantUtil.DATA_LIST_CIVILDEFENSE_ID).getTableNm());

            String filePath = ConstantUtil.CRAWLING_FILE_PATH + "disaster";
            String fileName = "civil_defense.xlsx";

            fis = new FileInputStream(new File(filePath, fileName));
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);

            List<CivilDefenseDto> civilDefenseDtos = new ArrayList<>();

            //총 행의 수
            int rLen = sheet.getPhysicalNumberOfRows();
            for(int rIdx = 1; rIdx < rLen; rIdx++){
                XSSFRow  row = sheet.getRow(rIdx);

                if(row != null){
                    int cLen = row.getLastCellNum();
                    if(cLen != 26) continue;

                    List<String> cellVal = new ArrayList<>();

                    for(int cIdx = 0; cIdx < cLen; cIdx++){
                        XSSFCell cell = row.getCell(cIdx);
                        String value = "";

                        if(cell == null){
                            value = "null";
                        }else{
                            value = cell.getStringCellValue();
                        }

                        cellVal.add(value);
                    }

                    CivilDefenseDto civilDefenseDto = new CivilDefenseDto(
                            null, ConstantUtil.DATA_LIST_CIVILDEFENSE_ID,
                            cellVal.get(1), cellVal.get(2), cellVal.get(4),
                            cellVal.get(5), cellVal.get(6), cellVal.get(7),
                            cellVal.get(8), cellVal.get(9), cellVal.get(10),
                            cellVal.get(11), cellVal.get(12), cellVal.get(22),
                            cellVal.get(23)
                    );

                    civilDefenseDtos.add(civilDefenseDto);
                    log.debug(civilDefenseDto);
                }
            }

            insertAll(civilDefenseDtos);

            result.put("success", true);
            result.put("message", "민방위 정보 업데이트 성공");
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

    public void insertAll(List<CivilDefenseDto> civilDefenseDtos) throws Exception{
        List< CivilDefenseEntity> civilDefenseEntities = ConvertEntityDtoListUtil.toListCivilDefenseEntity(civilDefenseDtos);
        civilDefenseRepository.saveAll(civilDefenseEntities);
    }

    public void deleteAll() throws Exception{
        civilDefenseRepository.deleteAll();
        sequenceService.initSeqNumZero(ConstantUtil.CIVILDEFENSE_SEQ_NAME);
    }
}
