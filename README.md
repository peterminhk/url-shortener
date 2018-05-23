# URL Shortener

## 소개
URL Shortener는 단축 URL을 생성해주는 웹 애플리케이션이다. 

주요 기능은 다음과 같다:
* URL 입력 폼에 URL을 입력하면 단축 URL을 생성한다. (예: https://en.wikipedia.org/wiki/URL_shortening => http://localhost/JZfOQNro)
* URL Shortening Key는 숫자와 영문 대소문자를 조합하여 총 8자리로 생성한다.
* 동일한 URL에 대한 요청은 동일한 Shortening Key로 응답한다.
* 단축 URL로 접속하면 원래의 URL로 리다이렉트한다.

기반 기술 및 오픈소스:
* [Spring Boot](https://projects.spring.io/spring-boot/)
* [MySQL](https://www.mysql.com/)
* [H2](http://www.h2database.com/) (테스트 전용)
* [Hibernate](http://hibernate.org/)
* [Lombok](https://projectlombok.org/)

## 구조

### Shortening Key 생성
Shortening Key는 '0~1, A~Z, a~z' 문자를 한 자릿수로 하는 62진수를 이용한다. Key는 총 8자리로 이루어지므로 총 62<sup>8</sup> (=218,340,105,584,896)가지 조합의 key를 생성할 수 있다.

따라서 key를 생성할 때는 `1`부터 `218340105584896`까지의 양의 정수를 `seed`로 사용한다. 이때 중복 key 발생을 방지하고 고른 분포의 문자 조합으로 key를 생성하기 위하여 다음과 같은 수식을 사용하여 key를 생성한다.
```
max_count = 218340105584896
key = (seed * leap + shift) mod max_count

shortening_key = Base62.encode(key)
```

`leap`은 연속적으로 key를 생성할 때 각 key 값을 분산시키기 위한 상수이며 `max_count` 값과 서로소인 값 중 상당히 큰 값 하나를 임의로 택한다.
```
leap = 213340105584895
```

`shift`는 key 값 예측을 어렵게 하기 위한 상수이며 `max_count` 값보다 작은 양의 정수 중 하나를 임의로 택한다.
```
shift = 217
```

예를 들어 `seed`가 1일 경우 shortening key 생성 과정은 다음과 같다.
```
key(1) = (1 * 213340105584895 + 217) mod 218340105584896
       = 4999999999784

shoretning_key(1) = Base62.encode(4999999999784)
                  = yZyH7Y9A
```

`seed`를 2, 3처럼 순차적으로 증가시키면서 key를 생성하여도 생성된 key의 문자 조합이 고르게 분포되는 것을 확인할 수 있다.
```
shoretening_key(2) = x9wYF6Ep
shoretening_key(3) = vjupMeKU
```

웹 애플리케이션에서 실제로 생성하는 key의 순서는 이것과는 다른데 이것은 key를 미리 여러벌 생성한 뒤 뒤섞는 단계가 추가되어 있기 때문이며 자세한 내용은 다음 절에서 설명한다.

### Shortening Key Queue
URL Shortener는 사용자가 URL 단축을 요청할 때 key를 생성하는 것이 아니라 미리 여러 개의 key를 queue에 넣어두었다가 사용자 요청 시 하나씩 꺼내는 방식을 취하는데 주요 이유는 다음과 같다.

먼저 여러 개의 key를 미리 생성해 둠으로 인하여 동시에 대량의 URL 생성 요청이 쏟아지는 상황에서도 단축 URL 생성에 소요되는 시간을 줄일 수 있다.

또 key를 미리 생성하면 key 목록을 뒤섞은 뒤 queue에 넣는 방식으로 무작위 순서를 간편하게 구현할 수 있다. 218340105584896가지나 되는 대량의 전체 key 조합을 모두 뒤섞는 것보다 필요한 수량 만큼만 미리 생성하여 섞는 방식으로 충분히 예측하기 힘든 URL 조합을 만들어 낼 수 있다.

미리 생성할 key의 수량에 대한 설정은 `application.yml`에서 한다.
```yml
custom:
  shortening-key-queue:
    # Key를 저장할 queue의 크기
    capacity: 1000
    # kqyQueue에 추가로 key를 채워 넣을 경계점. kqyQueue의 크기가 이 값 미만이 되면 key를 추가로 채워넣는다.
    fill-threshold: 100
    # keyQueue에서 key를 꺼낼 때 기다릴 시간 (단위: ms)
    timeout: 500
```


## 시스템 환경
* [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html) 이상 (필수)
* [MySQL](https://dev.mysql.com/downloads/mysql/) (필수)
* [Maven](http://maven.apache.org/) (선택)
  * 빌드할 때만 필요하다.
  * Maven이 설치되어 있지 않을 경우 프로젝트 홈의 `mvnw` 실행 파일을 대신 이용할 수 있다. 예를 들어 `mvn package` 명령은 `./mvnw package` 명령으로 대체할 수 있다.
* [Lombok](https://projectlombok.org/) IDE 플러그인 (선택)
  * Lombok은 Getter, Setter, 생성자 등을 annotation을 통하여 자동 생성해주는 라이브러리이다. IDE에서는 플러그인을 설치하여야 자동 생성된 method를 인식할 수 있다. 
  * [IntelliJ 플러그인 설치 안내](https://projectlombok.org/setup/intellij)


## 빌드
프로젝트 홈 디렉토리에서 Maven 빌드를 실행하면 `target` 디렉토리에 `url-shortener-{version}.jar` 파일이 생성된다.
```
mvn package
```

빌드 시 `prod` 프로파일 옵션을 추가하면 운영 환경 용으로 빌드할 수 있다.
```
mvn package -P prod
```

프로파일은 `dev`와 `prod` 두 가지가 존재하고 입력하지 않을 경우 `dev`가 기본 프로파일로 사용된다. 프로파일 별 환경 설정은 `src/main/resources/application.yml`에서 정의할 수 있다.


## 실행
`java -jar` 명령을 사용하면 앞선 빌드 과정에서 생성한 `jar` 파일을 실행할 수 있다.
```
java -jar target/url-shortener-1.0.0.jar
```

Spring Boot Maven 플러그인을 이용하면 `jar` 파일을 미리 생성하지 않고 빌드와 실행을 동시에 할 수도 있다.
```
mvn spring-boot:run [-P prod]
```

웹 애플리케이션이 실행되면 [http://localhost:8080](http://localhost:8080) 주소로 접속할 수 있다.
