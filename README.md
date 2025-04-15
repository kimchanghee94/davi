# DAVI
공공 데이터 포털 실시간 시각화 플랫폼

* DAVI_CRAWLER : Spring boot - JPA - Build Gradle
* * 일시 주기로 공공데이터 포털의 시군구, 읍면동 경계처리와 항만, 재난, 교통 데이터 수집 역할 (JSoup처리)
 
* DAVI_SERVER : Spring boot - JPA - Build Gradle
* * DAVI_CLIENT에게 DAVI_CRAWLER에서 저장된 데이터들을 제공하는 역할
 
* DAVI_WEBSOCKET : NodeJs
* * DAVI_CLIENT에게 실시간으로 선박의 위치, 배달 차량의 위치를 제공하는 역할
 
* DAVI_CLIENT : Svelte, Spring boot - JPA - Build Gradle - Redis
* * 사용자에게 화면을 출력하고 JWT 토큰방식으로 인증 처리 및 세션 유지 역할

* Server : Apache, Tomcat, PostgreSQL
* Database : 로컬에 구축 (MySQLWorkBench 사용)
* IDE : intellij
* TextEditor : Visual Studio Code

