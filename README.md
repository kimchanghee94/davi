# DAVI
공공 데이터 포털 실시간 시각화 플랫폼

* DAVI_CRAWLER : Spring boot - JPA - Build Gradle
** 
* DAVI_SERVER : Spring boot - JPA - Build Gradle
* DAVI_WEBSOCKET : NodeJs
* DAVI_CLIENT : Svelte, Spring boot - JPA - Build Gradle

* Front : Svelte
* Backend : Spring boot
* Server : Apache, Tomcat, PostgreSQL
* Database : 로컬에 구축 (MySQLWorkBench 사용)
* IDE : intellij
* TextEditor : Visual Studio Code
* Framework : Spring MVC - Maven
* 주요 Library : 
  1. spring-security(auth-provider 사용, 각 post메서드 csrf 적용)
  2. transaction, aop, aspectjweaver(applicationContext에서 aop를 이용한 트랜잭션 처리)
  3. WebSocket기반 Stomp(with SockJS)
     * Apache와 Tomcat은 mod_jk기반 AJP프로토콜 사용
     * 웹소켓의 경우 AJP프로토콜에서 미지원으로 ProxyPass를 이용하여 Tomcat으로 접속
  4. Mybatis
  5. BootStrap
  6. ApacheTiles

* REST-API : 
  1. 공공데이터포털(전국 주차장)
  2. Kakao Map
  3. Kakao Mobilty 길찾기
  4. Kakao Pay
