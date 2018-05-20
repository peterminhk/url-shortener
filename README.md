# URL Shortener
단축 URL을 생성해주는 웹 애플리케이션.

## 기능
* URL 입력 폼에 URL을 입력하면 단축 URL을 생성한다. (예: https://en.wikipedia.org/wiki/URL_shortening => http://localhost/JZfOQNro)
* URL Shortening Key는 '0~1, A~Z, a~z' 문자를 조합하여 총 8자리로 생성한다.
* 동일한 URL에 대한 요청은 동일한 Shortening Key로 응답한다.
* 단축 URL로 접속하면 원래의 URL로 리다이렉트한다.

## 구조

## 빌드

### 준비사항
* Java 8 (required)
* Lombok IDE 플러그인 (optional)
  * [Lombok](https://projectlombok.org/)은 Getter, Setter, 생성자 등을 annotation을 통하여 자동 생성해주는 라이브러리이다. IDE에서는 플러그인을 설치하여야 자동 생성된 method를 인식할 수 있다. 
  * [IntelliJ 플러그인 설치 안내](https://projectlombok.org/setup/intellij)

## 실행
