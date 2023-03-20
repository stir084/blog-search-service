# 블로그 검색 서비스

## 다운로드 링크

https://github.com/stir084/blog-search-engine/raw/master/blog-search-engine.jar

## API 명세서

http://localhost:8080/docs/dev-guide.html

## 기술 스택
- Java 17
- Sping Boot 
- H2
- JPA
- Junit5
- WebFlux - **WebClient를 통한 API 호출을 위해 사용했습니다.**  
- Ehcache - **인기 검색어 조회로 인한 DB 과부하를 막기 위해 사용했습니다.**  


## 카카오 API 이외에 새로운 검색 소스가 추가될 수 있음을 고려하기
→ **SearchProvider Interface를 구현하여 새로운 검색 소스가 늘어나더라도 연결이 쉽도록 구현했습니다.**  
   
  
![image](https://user-images.githubusercontent.com/47946124/226191457-cad0f990-badd-479b-85e6-e981e245df40.png)  



## 에러 처리(Exception Handling)

→ **외부 API 예외를 그대로 구현**  
   
  
![image](https://user-images.githubusercontent.com/47946124/226191928-ec3c4186-b44d-4b51-bcc6-3d89b94d7fa3.png)  
   
  
기본적인 REST API 예외는 @ControllerAdvice로 구현하고 WebClient를 통해 외부 API에서 발생한 예외를 그대로 받아서 처리하는 BlogSearchEngineResponseException 클래스 파일을 생성해서 사용했습니다.


## 트래픽이 많고, 저장되어 있는 데이터가 많음을 염두에 둔 구현  
   
  
→ **Ehcache를 사용해 부하 개선**  
   
  
![image](https://user-images.githubusercontent.com/47946124/226192129-ae41d97b-3bc0-4ede-a805-b53aab905be4.png)  
인기 검색어 목록은 트래픽이 많고 저장되어 있는 데이터가 굉장히 많기 때문에 실시간 반응보다 캐시를 이용하는 것이 DB 부하를 줄일 수 있어서 Ehcache를 사용했습니다.  
Ehcache는 적용하기가 쉽고 백단의 DB가 죽어도 캐시된 시간만큼은 데이터를 보장 받는다는 장점이 있습니다. 30초마다 캐시가 삭제되도록 구현했습니다.  

상위 인기 검색어를 확인하기 위해 임시 Dummy 데이터를 삽입하여 확인했습니다.  
![image](https://user-images.githubusercontent.com/47946124/226196005-185c993d-b5d4-4795-bf5b-a67c32ccc37e.png)

## 동시성 이슈가 발생할 수 있는 부분을 염두에 둔 구현
   
  
![image](https://user-images.githubusercontent.com/47946124/226192557-98898e24-8514-4a97-b40f-74d1a21406a5.png)  

검색 횟수의 동시성 이슈가 발생할 여지로 인해 Pessimistic Lock을 사용했습니다.  
하지만 이미 이 부분에 있어서는 MYSQL의 MVCC 기능으로 인해 동시성 이슈가 처리가 될걸로 예상합니다.  

- [MVCC가 적용된다고 생각하는 이유](https://github.com/stir084/blog-search-engine/wiki/MVCC%EA%B0%80-%EC%A0%81%EC%9A%A9%EB%90%9C%EB%8B%A4%EA%B3%A0-%EC%83%9D%EA%B0%81%ED%95%98%EB%8A%94-%EC%9D%B4%EC%9C%A0)



## 카카오 블로그 검색 API에 장애가 발생한 경우, 네이버 블로그 검색 API를 통해 데이터 제공
   
  
![image](https://user-images.githubusercontent.com/47946124/226195282-c287d649-a311-494d-94a8-71f90bfb3d1c.png)

카카오 블로그 검색 API에 장애가 발생한 경우 일반적으로 500 Error가 발생했다고 가정합니다.  
500 Error 발생 시 예외를 잡아서 던지지 않고 for문을 이용해 SearchProvider를 구현한 API를 반복적으로 돌아서 그 다음의 API를 통해 데이터를 제공하는 형태로 구현했습니다.