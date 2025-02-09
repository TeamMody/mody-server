# Mody

> 패션 추천 ~~ “Mody“

## 프로젝트 소개

- 
- 

## Member

<div align=center>

|                                   Backend                                   |                                Backend                                |
| :--------------------------------------------------------------------------: |:---------------------------------------------------------------------:|
|                     [](https://github.com)                     |                        [](https://github.com/)                        |

</div>

## 개발 기간



## How to use

```sh
curl -X POST http://localhost/api/v1/user/signup \
-H "Content-Type: application/json" \
-d '{"user_code":"admin","password":"admin","user_name":"admin","user_email":"admin","department":"admin","semester":1,"major":"admin","phone_number":"010-1234-5678"}'
```

터미널 혹은 CMD 창에 다음 명령어를 통해서 admin 계정에 대해 회원가입을 진행합니다.

### client

```bash
yarn install
yarn start
```

클라이언트 디렉토리에서 위와 같은 명령어를 통해 실행합니다.

## 개발 환경

- OS: MacOS, Ubuntu
- Code Editor: Visual Studio Code, IntelliJ, PyCharm
- Language: Html, CSS, JavaScript, TypeScript, Python
- Collaboration Tool: Notion, Github, Slack
  <br>

## Tech Stack

<div align=center>

### ✔️Back-end

<img src="https://img.shields.io/badge/ubuntu-E95420?style=for-the-badge&logo=ubuntu&logoColor=white">
<img src="https://img.shields.io/badge/python-3776AB?style=for-the-badge&logo=python&logoColor=white">
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=OpenJDK&logoColor=white">
<img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white">
<img src="https://img.shields.io/badge/spring%20boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/spring%20security-6DB33F?style=for-the-badge&logo=SPRING%20SECURITY&logoColor=white">
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/amazon%20aws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white">
<img src="https://img.shields.io/badge/terraform-844FBA?style=for-the-badge&logo=terraform&logoColor=white">
<img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=for-the-badge&logo=GitHub Actions&logoColor=white">
<img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">

### ✔️Frond-end

  <img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white">
  <img src="https://img.shields.io/badge/css3-1572B6?style=for-the-badge&logo=css3&logoColor=white">
     <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black">
 <img src="https://img.shields.io/badge/TypeScript-007ACC?style=for-the-badge&logo=typescript&logoColor=white">
  <img src="https://img.shields.io/badge/Next.js-000000?style=for-the-badge&logo=Next.js&logoColor=white">

</div>

## 인프라 아키텍쳐

- 3 Tier Architecture로 AWS 인프라 환경구성을 설정했습니다.
- 소프트웨어 시스템을 사용자의 인터페이스를 처리하고 클라이언트와 상호작용하는 계층, 비즈니스 로직을 처리하고 애플리케이션의 핵심 기능을 실행하는 계층, 데이터베이스 및 데이터 저장소와 상호 작용하여 데이터를 저장하고 검색하는 세 가지 주요 계층으로 나눴습니다.
- 이를 통해 각 계층을 독립적으로 개발 및 유지보수할 수 있으므로 코드의 모듈화가 쉽고, 비즈니스 로직은 비즈니스 계층 중점적으로 구현되므로, 해당 로직을 다른 클라이언트나 애플리케이션에서 재사용할 수 있었습니다.

![infra](./document/image/infra.png)

## 시스템 구성도

- Github의 actions 기능을 활용하여 개발자가 release 브랜치로 feature 프로젝트를 merge하는 순간 트리거가 발동되어 프로젝트의 버전을 업데이트 할 수 있도록 구성했습니다.
- Github actions에서 build한 docker image들은 docker hub로 push 됩니다. 이후 actions에서 서버 측으로 진입을 하게 되고, 해당 서버에서 docker hub에 있는 이미지들을 pull 받아 현재 실행중인 어플리케이션을 다운하고 새로 pull 받은 이미지들을 어플리케이션으로 up 합니다.
  ![systme](./document/image/system.png)

## 협업 규칙

### Github 협업 규칙

Github 협업 규칙은 아래와 같습니다.

1. 전체적인 협업 flow는 Github flow를 따름.
2. Fork한 저장소를 각자 local로 가져와 수정.
3. 수정한 코드는 add -> commit -> push 후, upstream에 Pull Request를 수행.
4. main branch로부터 dev branch, prod branch를 구성.
5. 추가되는 기능에 대해서는 feature branch를 생성하여 각 기능별 branch를 구성.
6. Pull Request 시 Code Review 이후 Merge 진행.
7. Commit 규칙은 아래와 같이 진행했습니다.

   | 커밋 타입 | 설명                                                           |
      | --------- | -------------------------------------------------------------- |
   | Feat      | 새로운 기능 추가                                               |
   | Fix       | 버그 수정                                                      |
   | Docs      | 문서 수정                                                      |
   | Style     | 코드 formatting, 세미콜론 누락 등 코드 자체의 변경이 없는 경우 |
   | Chore     | 패키지 매니저 수정 및 기타 수정                                |
   | Design    | CSS 등 사용자 UI 변경                                          |

### Issue 활용

![issue](./document/image/issue.png)

- Github 레포지토리의 Issue탭에 Todo인 상황 혹은 In progress에 대한 상황을 작성하고 공유했습니다. 해당 Issue 번호로 각자의 로컬 레포지토리에 브랜치를 생성하여 Pull Request 시에 해당 Issue를 언급하여 공유했습니다. 해당 전략을 사용하여 Merge Conflict의 발생 가능성을 줄였습니다.

### PR 활용

![issue](./document/image/PR.png)

- 다음과 같이 개발 이후 특정 프로젝트에 대한 변경사항을 제안하고, 팀원과 이를 검토 및 논의한 후, 최종적으로 해당 변경사항을 반영할 수 있도록 했습니다.
- 다른 개발자들은 해당 Pull Request를 검토하고, 필요한 경우 피드백을 제공할 수 있었습니다.
- 검토 후, Pull Request가 승인되면 변경 사항이 메인 프로젝트로 병합되도록 했습니다. 반면, 추가적인 수정이 필요한 경우 개발자는 피드백을 반영하여 수정하고, 수정된 변경사항을 다시 push 했습니다.

## 구현결과

<div align=center>
<img src="./document/image/login.png">
<p>로그인 페이지</p>
<img src="./document/image/main.png">
<p>메인 페이지</p>
<img src="./document/image/info.png">
<p>학적 조회 페이지</p>
<img src="./document/image/grade.png">
<p>성적 조회 페이지</p>
<img src="./document/image/enroll.png">
<p>수강 신청 페이지</p>
<img src="./document/image/confirm.png">
<p>수강 조회 페이지</p>
<img src="./document/image/course.png">
<p>학습 관리 페이지</p>
</div>

## 기대효과

기대효과는 아래와 같습니다.

1. 페이지 하나에 학생들이 사용하는 대부분의 기능이 통합 되어있어 사용자의 편의성 증진
2. PC, 모바일 환경을 모두 고려한 개선을 통해 사용자의 편의성 증진
3. 프로젝트 수행을 통한 팀원들의 웹 시스템 구조 이해 상승
4. 협업 툴 (Github, Notion, Slack)을 활용하여 팀원들의 커뮤니케이션 능력 향상
5. 오픈소스를 통해 개발하는 과정에 대한 이해 상승

## License

