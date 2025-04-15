package com.davi_crawler.common.util;

public class ConstantUtil {
    //ID 값
    public static final Long DATA_LIST_LINK_ID = 1L;
    public static final Long DATA_LIST_SIDO_ID = 2L;
    public static final Long DATA_LIST_SIGUNGU_ID = 3L;
    public static final Long DATA_LIST_EUPMYEONDONG_ID = 4L;
    public static final Long DATA_LIST_LI_ID = 5L;
    public static final Long DATA_LIST_PUBLICDATAPORTAL_ID = 6L;
    public static final Long DATA_LIST_ROAD_ID = 7L;
    public static final Long DATA_LIST_SEADIST_ID = 8L;
    public static final Long DATA_LIST_HANG_ID = 9L;
    public static final Long DATA_LIST_BUOY_ID = 10L;
    public static final Long DATA_LIST_LSMDCONT_ID = 11L;
    public static final Long DATA_LIST_CCTV_ID = 12L;
    public static final Long DATA_LIST_CHILDZONE_ID = 13L;
    public static final Long DATA_LIST_TRAFFICLIGHT_ID = 14L;
    public static final Long DATA_LIST_CROSSWALK_ID = 15L;
    public static final Long DATA_LIST_SPEEDDUMP_ID = 16L;
    public static final Long DATA_LIST_RSE_ID = 17L;
    public static final Long DATA_LIST_FROST_ID = 19L;
    public static final Long DATA_LIST_FOG_ID = 20L;
    public static final Long DATA_LIST_BUSROUTELINK_ID = 21L;
    public static final Long DATA_LIST_BUSSEOULINFO_ID = 22L;
    public static final Long DATA_LIST_BUSROUTEPATH_ID = 23L;
    public static final Long DATA_LIST_CIVILDEFENSE_ID = 24L;
    public static final Long DATA_LIST_SEOUL113_ID = 25L;
    public static final Long DATA_LIST_GRID_ID = 26L;
    public static final Long DATA_LIST_CHIMSU_ID = 27L;


    //DB 각 테이블 주요키 시퀀스 명
    public static final String EUPMYEONDONG_SEQ_NAME = "DV_MOLIT_EUP_MYEON_DONG_EUP_MYEON_DONG_ID_SEQ";
    public static final String LI_SEQ_NAME = "DV_MOLIT_LI_LI_ID_SEQ";
    public static final String LINK_SEQ_NAME = "DV_MOLIT_LINK_LINK_ID_SEQ";
    public static final String SIDO_SEQ_NAME = "DV_MOLIT_SI_DO_SI_DO_ID_SEQ";
    public static final String SIGUNGU_SEQ_NAME = "DV_MOLIT_SI_GUN_GU_SI_GUN_GU_ID_SEQ";
    public static final String ROAD_SEQ_NAME = "DV_MOLIT_ROAD_ROAD_ID_SEQ";
    public static final String PDP_SEQ_NAME = "DV_PUBLIC_DATA_PORTAL_SEQ";
    public static final String PDPREQ_SEQ_NAME = "DV_PUBLIC_DATA_PORTAL_REQ_SEQ";
    public static final String PDPRES_SEQ_NAME = "DV_PUBLIC_DATA_PORTAL_RES_SEQ";
    public static final String PDPSERVICE_SEQ_NAME = "DV_PUBLIC_DATA_PORTAL_SERVICE_SEQ";
    public static final String SEADIST_SEQ_NAME = "DV_KHOA_SEA_DIST_SEA_DIST_ID_SEQ";
    public static final String HANG_SEQ_NAME = "DV_KHOA_HANG_HANG_ID_SEQ";
    public static final String BUOY_SEQ_NAME = "DV_KHOA_BUOY_BUOY_ID_SEQ";
    public static final String LSMDCONT_SEQ_NAME = "DV_LSMD_CONT_LSMD_CONT_ID_SEQ";
    public static final String CCTV_SEQ_NAME = "DV_TRAFFIC_CCTV_CCTV_ID_SEQ";
    public static final String CHILDZONE_SEQ_NAME = "DV_TRAFFIC_CHILD_ZONE_CHILD_ZOND_ID_SEQ";
    public static final String DATACOLLIST_SEQ_NAME = "DV_DATA_COL_LIST_COL_LIST_ID_SEQ";
    public static final String TRAFFICLIGHT_SEQ_NAME = "DV_TRAFFIC_TRAFFIC_LIGHT_TRAFFIC_LIGHT_ID_SEQ";
    public static final String CROSSWALK_SEQ_NAME = "DV_TRAFFIC_CROSSWALK_CROSSWALK_ID_SEQ";
    public static final String SPEEDDUMP_SEQ_NAME = "DV_TRAFFIC_SPEED_DUMP_SPEED_DUMP_ID_SEQ";
    public static final String RSE_SEQ_NAME = "DV_TRAFFIC_RSE_RSE_ID_SEQ";
    public static final String FROST_SEQ_NAME = "DV_TRAFFIC_FROST_FROST_ID_SEQ";
    public static final String FOG_SEQ_NAME = "DV_TRAFFIC_FOG_FOG_ID_SEQ";
    public static final String BUSROUTELINK_SEQ_NAME = "DV_TRAFFIC_BUS_ROUTE_LINK_BUS_ROUTE_LINK_ID_SEQ";
    public static final String BUSSEOULINFO_SEQ_NAME = "DV_TRAFFIC_BUS_SEOUL_INFO_BUS_SEOUL_INFO_ID_SEQ";
    public static final String BUSROUTEPATH_SEQ_NAME = "DV_TRAFFIC_BUS_ROUTE_PATH_BUS_ROUTE_PATH_ID_SEQ";
    public static final String CIVILDEFENSE_SEQ_NAME = "DV_DISASTER_CIVIL_DEFENSE_CIVIL_DEFENSE_ID_SEQ";
    public static final String SEOUL113_SEQ_NAME = "DV_DISASTER_SEOUL113_SEOUL113_ID_SEQ";
    public static final String GRID_SEQ_NAME = "DV_COMMON_GRID_GRID_ID_SEQ";
    public static final String CHIMSU_SEQ_NAME = "DV_DISASTER_CHIMSU_CHIMSU_ID_SEQ";

    //웹 실행 시 DATA_LIST 정보를 캐쉬에 담아둘 때 사용되는 키
    public static final String DATA_LIST_INIT_CACHE_KEY = "DATA_LIST_INIT_CACHE_KEY";

    //각 데이터별 페이지 파라미터 표기법
    public static final String PDP_PAGE_PREFIX = "currentPage=";
    public static final String MOLIT_PAGE_PREFIX = "page=";
    public static final String KHOA_PAGE_PREFIX = "pageNo=";
    //크롤링 파일 위치
    public static final String CRAWLING_FILE_PATH = "src/main/webapp/WEB-INF/file/";

    //각 사이트 서비스키
    public static final String PDP_SERVICE_KEY = "2HrkEW0EqTLtlnpp1KWE98SReBtg0ab8hqPSlRCp38J3%2Flx58aziYFgrnt5uOfm44A%2BJwa6aA9Jcb5sdccb2Bg%3D%3D";
    public static final String MOLIT_SERVICE_KEY = "7CCB2DBB-2659-35F6-9E16-D0D863374352";
}
