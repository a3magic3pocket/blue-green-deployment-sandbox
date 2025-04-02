# blue-green-deployment-sandbox
blue-green 배포를 도커 컨테이너를 활용하여 소규모로 구현한다.



### 가정
- 모든 명령어는 blue-green_manifest 디렉토리 내에서 실행합니다.
- blue-green 이미지 빌드 시 사용하는 웹 서버 애플리케이션은   
  ./blue-green_manifest/spring-0.0.1-SNAPSHOT.jar 이다.

### 명령
- ```bash
  # blue 이미지 빌드
  docker build -t server-blue:0.0.1 -f ./Dockerfiler-server .

  # green 이미지 빌드
  docker build -t server-green:0.0.1 -f ./Dockerfile-server .

  # 도커 컨테이너 생성
  docker compose up -d

  # 블루 버전으로 트래픽 분배
  docker compose -f ./docker-compose.yml exec loadbalancer sh -c "
    mv /etc/nginx/conf.d/default.conf /etc/nginx/conf.d/default.conf.bak 
    rm -rf /etc/nginx/conf.d/*.conf
    ln -s /etc/nginx/sites-available/loadbalancer-nginx-blue.conf /etc/nginx/conf.d/
    nginx -t
    nginx -s reload
  "
  # 블루버전으로 트래픽 분배되는지 확인
  # 브라우저에서 http://localhost 로 접속 후 blue라는 문자열이 표시되는지 확인

  # 그린 버전으로 트래픽 분배
  docker compose -f ./docker-compose.yml exec loadbalancer sh -c "
    mv /etc/nginx/conf.d/default.conf /etc/nginx/conf.d/default.conf.bak 
    rm -rf /etc/nginx/conf.d/*.conf
    ln -s /etc/nginx/sites-available/loadbalancer-nginx-green.conf /etc/nginx/conf.d/
    nginx -t
    nginx -s reload
  "
  # 그린버전으로 트래픽 분배되는지 확인
  # 브라우저에서 http://localhost 로 접속 후 green라는 문자열이 표시되는지 확인

  # 블루 새로운 버전인 0.0.2 버전 빌드
  docker build -t server-blue:0.0.2 -f ./Dockerfiler-server .

  # docker-compose.yml 에서 각 서버의 블루 버전을 0.0.1 -> 0.0.2로 변경
  ""yml
  ...
    server1-blue:
    image: server-blue:0.0.2
  ...
    server2-blue:
    image: server-blue:0.0.2
  ""

  # server1-blue에 변경사항 적용
  docker-compose up -d --no-deps --force-recreate server1-blue

  # server1-blue에 버전 변경 확인
  #(이미지가 server-blue:0.0.2로 나와야 함)
  docker ps -a  | grep server1-blue

  # server2-blue에 변경사항 적용
  docker-compose up -d --no-deps --force-recreate server2-blue
  
  # server2-blue에 버전 변경 확인
  #(이미지가 server-blue:0.0.2로 나와야 함)
  docker ps -a  | grep server2-blue

  # 블루 버전으로 트래픽 분배
  docker compose -f ./docker-compose.yml exec loadbalancer sh -c "
    mv /etc/nginx/conf.d/default.conf /etc/nginx/conf.d/default.conf.bak 
    rm -rf /etc/nginx/conf.d/*.conf
    ln -s /etc/nginx/sites-available/loadbalancer-nginx-blue.conf /etc/nginx/conf.d/
    nginx -t
    nginx -s reload
  "
  # 블루버전으로 트래픽 분배되는지 확인
  # 브라우저에서 http://localhost 로 접속 후 blue라는 문자열이 표시되는지 확인
  ```


    