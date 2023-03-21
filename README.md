# 블로그 검색 서비스

## 다운로드 링크

https://github.com/stir084/blog-search-service/releases/download/v1.0.0/blog-search-service.jar

## API 명세서(Spring Rest Docs)

http://localhost:8080/docs/dev-guide.html

## 기술 스택
- Java 17
- Sping Boot 3.0.2
- H2 (1.4.199 testdb 사용)
- JPA
- Junit 5
- WebFlux - **WebClient를 통한 API 호출을 위해 사용했습니다.**  
- Ehcache 3(with jaxb library) - **인기 검색어 조회로 인한 DB 과부하를 막기 위해 사용했습니다.**  
- Spring Rest Docs - **API 명세서를 위해 사용했습니다.**

## 목차  
  
  
1. [카카오 API 이외에 새로운 검색 소스가 추가될 수 있음을 고려하기(Naver API 추가)](#카카오-API-이외에-새로운-검색-소스가-추가될-수-있음을-고려하기naver-api-추가)  
2. [에러 처리(Exception Handling)](#에러-처리exception-handling)  
3. [멀티 모듈 구성 및 모듈간 의존성 제약](#멀티-모듈-구성-및-모듈간-의존성-제약)  
4. [트래픽이 많고 저장되어 있는 데이터가 많음을 염두에 둔 구현](#트래픽이-많고-저장되어-있는-데이터가-많음을-염두에-둔-구현)  
5. [동시성 이슈가 발생할 수 있는 부분을 염두에 둔 구현](#동시성-이슈가-발생할-수-있는-부분을-염두에-둔-구현)  

  

## 카카오 API 이외에 새로운 검색 소스가 추가될 수 있음을 고려하기(Naver API 추가)
→ **Interface를 구현하여 새로운 검색 소스가 늘어나더라도 연결이 쉽도록 구현했습니다.**  
   
  
![image](https://user-images.githubusercontent.com/47946124/226191457-cad0f990-badd-479b-85e6-e981e245df40.png)  


해당 Interface를 상속받는 구현체에 각각의 검색 API를 구현하여 언제든지 API 소스 대체, 장애 시 그 다음 API를 연결이 가능합니다.  
그로인해 API 서버에 장애가 발생하더라도 유연하게 대처가 가능하도록 설계 했습니다.  
  
![image](https://user-images.githubusercontent.com/47946124/226195282-c287d649-a311-494d-94a8-71f90bfb3d1c.png)  

카카오 블로그 검색 API에 장애가 발생한 경우 네이버 블로그 검색 API를 통한 데이터 제공을 위해 위와 같이 구현했습니다.  
API 서버에 장애가 발생한 경우 일반적으로 500 Error가 발생했다고 가정했습니다.(물론 503 Error도 있지만)  
500 Error 발생 시 예외를 잡아서 통과 시키는 방식으로 구현하고 두번째 SearchProvider 구현체를 통해 API 데이터를 제공하는 형태로 구현했습니다.  

## 에러 처리(Exception Handling)

→ **외부 API 예외를 그대로 구현**  
   
  
![image](https://user-images.githubusercontent.com/47946124/226191928-ec3c4186-b44d-4b51-bcc6-3d89b94d7fa3.png)  
   
  
기본적인 REST API 예외는 @ControllerAdvice로 구현했습니다.  
WebClient를 통한 외부 API에서 발생한 예외 또한 사용자가 볼 수 있도록 위와 같이 처리했습니다.

## 멀티 모듈 구성 및 모듈간 의존성 제약
  
  
![image](https://user-images.githubusercontent.com/47946124/226533158-2d3bc1c4-83bf-43fc-9a54-5fbf12d4f402.png)

**module-client - (controller)**  

클라이언트 웹 접근에 대한 용도로 모듈화 했으며 현재 재직중인 금융 회사에서도 EAI를 사용한 모듈 분리를 필수적으로 사용하고 있습니다.  
대부분의 금융권에서는 자금이 움직이는 상황에 Client가 접근하는 Web영역과 Core에 대한 모듈 분리는 필수라고 생각해서 위와 같이 분리했습니다.

**module-core (service, repository, domain, exception)**  

필수 도메인과 비즈니스로직이 여기에 포함됩니다.  

**module-external-api (외부 API 연결)**  

외부 API를 모듈화 했으며 해당 API를 언제 어느 프로젝트에서도 갖다 쓸 수 있도록 모듈화 했습니다.  

**module-common (dto, Utility, Constant)**  

각 모듈의 프로젝트마다 갖다 쓸 수 있는 부분은 common으로 모듈화 했습니다.  
  

## 트래픽이 많고 저장되어 있는 데이터가 많음을 염두에 둔 구현
   
  
→ **Ehcache를 사용해 부하 개선**  
   
  
![image](https://user-images.githubusercontent.com/47946124/226192129-ae41d97b-3bc0-4ede-a805-b53aab905be4.png)  
인기 검색어 목록은 트래픽이 많고 저장되어 있는 데이터가 굉장히 많기 때문에 실시간 반응보다 캐시를 이용하는 것이 DB 부하를 줄일 수 있어서 Ehcache를 사용했습니다.  
Ehcache는 적용하기가 쉽고 백단의 DB가 죽어도 캐시된 시간만큼은 데이터를 보장 받는다는 장점이 있습니다.  
30초마다 캐시가 삭제되도록 구현했습니다.  

상위 인기 검색어를 확인하기 위해 임시 Dummy 데이터를 삽입하여 확인했습니다.  
![image](https://user-images.githubusercontent.com/47946124/226527768-4d8ee5b1-1446-425c-8f53-c71dcc802ff1.png)


## 동시성 이슈가 발생할 수 있는 부분을 염두에 둔 구현
   
  
![image](https://user-images.githubusercontent.com/47946124/226192557-98898e24-8514-4a97-b40f-74d1a21406a5.png)  

검색 횟수의 동시성 이슈가 발생할 여지로 인해 Pessimistic Lock을 사용했습니다.  
하지만 이미 이 부분에 있어서는 MYSQL의 MVCC 기능도 적용된다고 생각하는데 이유는 아래와 같습니다.

- [MVCC가 적용된다고 생각하는 이유](https://github.com/stir084/blog-search-service/wiki/MVCC%EA%B0%80-%EC%A0%81%EC%9A%A9%EB%90%9C%EB%8B%A4%EA%B3%A0-%EC%83%9D%EA%B0%81%ED%95%98%EB%8A%94-%EC%9D%B4%EC%9C%A0)




   
  

