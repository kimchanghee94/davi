package com.davi_crawler.khoa.service;

import com.davi_crawler.common.service.DataColListService;
import com.davi_crawler.common.service.DataListService;
import com.davi_crawler.common.service.SequenceService;
import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.common.util.ConvertEntityDtoListUtil;
import com.davi_crawler.khoa.dto.LsmdContDto;
import com.davi_crawler.khoa.entity.LsmdContEntity;
import com.davi_crawler.khoa.repository.LsmdContRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.geojson.GeoJsonWriter;
import org.locationtech.proj4j.*;
import org.postgis.PGgeometry;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@Service
@RequiredArgsConstructor
public class LsmdContService {
    private final LsmdContRepository lsmdContRepository;
    private final SequenceService sequenceService;
    private final DataListService dataListService;
    private final DataColListService dataColListService;


    //geometryText에서 좌표형태만 출력
    private List<String> extractCoordinates(String geometryText) throws Exception{
        List<String> coordinates = new ArrayList<>();
        Pattern pattern = Pattern.compile("(-?\\d+\\.\\d+) (-?\\d+\\.\\d+)");
        Matcher matcher = pattern.matcher(geometryText);

        while(matcher.find()){
            String coordinate = matcher.group();
            coordinates.add(coordinate);
        }

        return coordinates;
    }

    //EPSG5174에서 WGS84로 코드 변환
    private String convertEPSG5174ToWgs84GeoJson(String geometryText) throws Exception{
        List<String> extractCoord = extractCoordinates(geometryText);
        for(String coord : extractCoord){
            StringTokenizer st = new StringTokenizer(coord, " ");
            List<Double> lngLat = new ArrayList<>();

            while(st.hasMoreTokens()){
                lngLat.add(Double.parseDouble(st.nextToken()));
            }

            //출력한 코드를 가지고 WGS84 형태로 변환 시작
            CRSFactory factory = new CRSFactory();
            CoordinateReferenceSystem epsg5174 = factory.createFromParameters("EPSG:5174",
                    "+proj=tmerc +lat_0=38 +lon_0=127.0028902777778 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43");
            CoordinateReferenceSystem epsg4326 = factory.createFromParameters("EPSG:4326",
                    "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs");
            CoordinateTransformFactory ctFactory  = new CoordinateTransformFactory();
            CoordinateTransform epsg5174Toepsg4326 = ctFactory.createTransform(epsg5174, epsg4326);

            ProjCoordinate beforeCoord = new ProjCoordinate(lngLat.get(0), lngLat.get(1));
            ProjCoordinate afterCoord = new ProjCoordinate();

            epsg5174Toepsg4326.transform(beforeCoord, afterCoord);

            //문자열 replace를 통해 변환 좌표 적용
            String orgLng = String.valueOf(lngLat.get(0));
            String orgLat = String.valueOf(lngLat.get(1));
            String afterLng = String.valueOf(afterCoord.x);
            String afterLat = String.valueOf(afterCoord.y);

            geometryText = geometryText.replaceAll(orgLng, afterLng);
            geometryText = geometryText.replaceAll(orgLat, afterLat);

        }

        //geometryText를 geoJson형태로 출력하기
        WKTReader wktReader = new WKTReader();
        Geometry geometry = wktReader.read(geometryText);

        GeoJsonWriter geoJsonWriter = new GeoJsonWriter();
        String geoJson = geoJsonWriter.write(geometry);

        return geoJson;
    }

    //hex geometry형태를 text형태로 출력
    private String convertToGeometryText(String hexGeometry) throws Exception{
        PGgeometry pGgeometry = new PGgeometry(hexGeometry);
        String geometryText = pGgeometry.getValue();

        //geometry타입만 출력한다.
        String geometryText2 = null;
        StringTokenizer st = new StringTokenizer(geometryText, ";");
        while(st.hasMoreTokens()){
            geometryText2 = st.nextToken();
        }

        return geometryText2;
    }

    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> updateApiData() throws Exception{
        Map<String, Object> result = new HashMap<>();
        BufferedReader br = null;

        try{
            deleteAll();

            dataColListService.updateColData(ConstantUtil.DATA_LIST_LSMDCONT_ID,
                    dataListService.selectById(ConstantUtil.DATA_LIST_LSMDCONT_ID).getTableNm());

            String filePath = ConstantUtil.CRAWLING_FILE_PATH + "khoa";
            String fileName = "항만모형.csv";

            br = new BufferedReader(new FileReader(new File(filePath, fileName)));
            String line = "";

            List<List<String>> lsmdContDatas = new ArrayList<>();

            while((line = br.readLine()) != null){
                //한 칼럼 데이터 안에 표시된 콤마처리로 split 되지 않도록 정규식을 사용한다.
                String[] lineArr = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                List<String> aLine = Arrays.asList(lineArr);
                lsmdContDatas.add(aLine);
            }

            List<LsmdContDto> lsmdContDtos = new ArrayList<>();

            for(int i=1; i < lsmdContDatas.size(); i++){
                List<String> lsmdContData = lsmdContDatas.get(i);

                LsmdContDto lsmdContDto = new LsmdContDto();
                lsmdContDto.setDataListId(ConstantUtil.DATA_LIST_LSMDCONT_ID);
                lsmdContDto.setObjectid(lsmdContData.get(0).replaceAll("\"", ""));
                lsmdContDto.setMnum(lsmdContData.get(1).replaceAll("\"", ""));
                lsmdContDto.setSggOid(lsmdContData.get(2).replaceAll("\"", ""));
                lsmdContDto.setColAdmSectCd(lsmdContData.get(3).replaceAll("\"", ""));
                lsmdContDto.setNtfdate(lsmdContData.get(4).replaceAll("\"", ""));
                lsmdContDto.setAlias(lsmdContData.get(5).replaceAll("\"", ""));
                lsmdContDto.setRemark(lsmdContData.get(6).replaceAll("\"", ""));

                //hex binary타입을 geometry 텍스트 출력
                String geometryText = convertToGeometryText(lsmdContData.get(7).replaceAll("\"", ""));
                //EPSG5174 -> Wgs84로 변환 후 geoJson으로 출력
                String geoJson = convertEPSG5174ToWgs84GeoJson(geometryText);
                lsmdContDto.setShape(geoJson);

                log.debug(lsmdContDto);
                lsmdContDtos.add(lsmdContDto);
            }

            insertAll(lsmdContDtos);

            result.put("success", true);
            result.put("message", "항만모형 데이터 업데이트 성공");
        }finally {
            try{
                if(br != null){
                    br.close();
                }
            }catch (IOException ie){
                ie.printStackTrace();
            }
        }

        return result;
    }

    public void insert(LsmdContDto lsmdContDto) throws Exception{
        LsmdContEntity lsmdContEntity = lsmdContDto.toEntity();
        lsmdContRepository.save(lsmdContEntity);
    }

    public void insertAll(List<LsmdContDto> lsmdContDtos) throws Exception{
        List<LsmdContEntity> lsmdContEntities = ConvertEntityDtoListUtil.toListLsmdContEntity(lsmdContDtos);
        lsmdContRepository.saveAll(lsmdContEntities);
    }

    public void deleteAll() throws Exception{
        lsmdContRepository.deleteAll();
        sequenceService.initSeqNumZero(ConstantUtil.LSMDCONT_SEQ_NAME);
    }
}
