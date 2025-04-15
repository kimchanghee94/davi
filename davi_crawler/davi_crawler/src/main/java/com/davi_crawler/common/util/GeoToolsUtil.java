package com.davi_crawler.common.util;

import lombok.extern.log4j.Log4j2;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import java.io.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Log4j2
public class GeoToolsUtil {
    public static void convertShpToGeoJson(String filePath, String fileName) throws Exception{
        DataStore dataStore = null;
        try{
            File shapefile = new File(filePath, fileName);
            File geoJsonFile = new File(filePath, fileName.substring(0, fileName.indexOf('.')) + ".geojson");

            // Create a data store for the shapefile
            Map<String, Object> params = new HashMap<>();
            params.put("url", shapefile.toURI().toString());
            params.put("charset", "euc-kr");
            dataStore = DataStoreFinder.getDataStore(params);

            // Get the feature source and collection
            String[] typeNames = dataStore.getTypeNames();
            SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeNames[0]);

            // featureCollection 접근 & geojson 인코딩
            SimpleFeatureCollection featureCollection = featureSource.getFeatures();

            // 원본 좌표계
            CoordinateReferenceSystem sourceCrs = featureSource.getSchema().getCoordinateReferenceSystem();
            // 변환 좌표계
            CoordinateReferenceSystem targetCrs = CRS.decode("EPSG:4326");
            MathTransform transform = CRS.findMathTransform(sourceCrs, targetCrs, true);

            // Create a new SimpleFeatureCollection with transformed geometries
            DefaultFeatureCollection transformedCollection = new DefaultFeatureCollection();
            try (SimpleFeatureIterator features = featureCollection.features()) {
                while (features.hasNext()) {
                    SimpleFeature feature = features.next();
                    Geometry geometry = (Geometry) feature.getDefaultGeometry();
                    Geometry transformedGeometry = JTS.transform(geometry, transform);
                    Coordinate[] coordinates = transformedGeometry.getCoordinates();

                    //x좌표랑 y좌표 순서 바꿔주기
                    for(Coordinate coordinate : coordinates){
                        Double tmp = Math.round(coordinate.y * 10000000) / 10000000.0;
                        coordinate.y = Math.round(coordinate.x * 10000000) / 10000000.0;
                        coordinate.x = tmp;
                    }

                    SimpleFeatureType featureType = feature.getFeatureType();
                    SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
                    typeBuilder.init(featureType);
                    typeBuilder.setCRS(targetCrs);
                    SimpleFeatureType transformedFeatureType = typeBuilder.buildFeatureType();
                    SimpleFeature transformedFeature = SimpleFeatureBuilder.build(transformedFeatureType, feature.getAttributes(), feature.getID());
                    transformedFeature.setDefaultGeometry(transformedGeometry);
                    transformedCollection.add(transformedFeature);
                }
            }

            //소수점 4자리까지 나오는 현상 GeometryJson생성자 초기화를 통해 6자리까지 출력가능하도록 기능 설정
            FeatureJSON featureJSON = new FeatureJSON(new GeometryJSON(6));

            //json 출력
            try (FileOutputStream output = new FileOutputStream(geoJsonFile)) {
                featureJSON.writeFeatureCollection(transformedCollection, output);
            }

            dataStore.dispose();
            log.debug(geoJsonFile + " UPLOAD SUCCESS");
        }finally {
            if(dataStore != null){
               dataStore.dispose();
            }
        }
    }
}
