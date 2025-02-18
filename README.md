# mody

> "모드(Mode)"와 "버디(Buddy)"의 결합으로 만들어진 이 이름은, 당신의 **라이프스타일과 개성을 반영하는 최적의 '모드'를 발견하도록 돕는 친구**를 뜻합니다.
> 고객의 체형, 취향, 라이프스타일 데이터를 기반으로 맞춤형 스타일링 경험을 제공합니다.
> 단순한 추천을 넘어, 고객 스스로 스타일링 능력을 기를 수 있도록 돕는 서비스입니다.

<div align=center>
  
![image](https://github.com/user-attachments/assets/3863789b-de5a-45e5-8a63-a72f58d02cdf)
</div>

## 프로젝트 기능
1. 종합 패션 스타일 컨설팅
   * 체형 분석
   * 스타일 추천
   * 패션 아이템 추천
   * 브랜드 추천
2. 데일리 패션 추천
   * 날씨 기반 추천
   * 일정 및 라이프스타일 반영 추천
3. 부가 기능
   * 스타일링 팁 제공
   * 쇼핑 연계 기능
   * 저장 및 비교 기능
   * 스타일 커뮤니티
4. 기능 기대 효과
   * 시간 절약: 고객이 스타일 고민에 소모하던 시간을 획기적으로 단축
   * 개성 표현: 획일적인 스타일에서 벗어나, 개인의 개성과 매력을 돋보이게 함
   * 편리한 쇼핑: 추천과 쇼핑을 한 번에 해결하여 구매 실패율을 낮춤
   * 지속적인 성장: 고객 스스로 스타일링 실력을 키워 패션 근육을 강화

## Member

|                 박동규                   |                   김재헌                   |                  서상효                   |                   김성욱                     |                    최윤서                   |                                                                                     
|:---------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------:|
| <img width="173" alt="image" src="https://github.com/user-attachments/assets/488fec06-d15c-444b-a7ac-9b6f0c633839" /> | <img width="172" alt="image" src="https://github.com/user-attachments/assets/021ca13c-4e3c-4db8-aebb-5f920c3a6338" /> | <img width="164" alt="image" src="https://github.com/user-attachments/assets/6422ab4d-c39f-43e0-bb8c-e065dd2f4635" /> | <img width="164" alt="image" src="https://github.com/user-attachments/assets/180c4941-1370-482e-a626-21324fcf1ef9" /> | <img width="213" alt="image" src="https://github.com/user-attachments/assets/6ae5daf0-5ca5-4581-82c4-1cbc3eb78336" /> |
|                       [dong99u](https://github.com/dong99u)                        |                  [jher235](https://github.com/jher235)                   |                     [seoshinehyo](https://github.com/seoshinehyo)                     |                     [so3659](https://github.com/so3659)                     |                     [yunseo02](https://github.com/yunseo02)                     |

## Tech Stack
<div align=center>
  
|                                                         기술 스택                                                         |   버전   |
|:---------------------------------------------------------------------------------------------------------------------:|:------:|
|        <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=OpenJDK&logoColor=white">          |   21   |
| ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white) | 3.3.5  |
|          ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)           | 8.0.39 |

<img src="https://img.shields.io/badge/ubuntu-E95420?style=for-the-badge&logo=ubuntu&logoColor=white">
<img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white">
<img src="https://img.shields.io/badge/spring%20security-6DB33F?style=for-the-badge&logo=SPRING%20SECURITY&logoColor=white">
<img src="https://img.shields.io/badge/amazon%20aws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white">
<img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=for-the-badge&logo=GitHub Actions&logoColor=white">
<img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">

</div>

## 아키텍처 다이어그램
![image](https://github.com/user-attachments/assets/25a84d99-a57e-42c9-afcf-1f9cb9adc9da)


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
   | feature   | 새로운 기능 구현                                               |
   | fix       | 수정                                                          |
   | refactor  | 리팩토링                                                       |
   | docs      | 문서 수정                                                      |
   | style     | 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우                 |
   | chore     | 패키지 구조 수정, code와 무관한 부분들 (.gitignore, build.gradle 같은) 수정   |
   | hotfix    | hotfix                                                        |
   | remove    | 패키지나 클래스 삭제                                            |
   | test      | 테스트 코드, 리펙토링 테스트 코드 추가                           |
   | rename    | 패키지나 클래스명 수정                                          |
   | comment   | 주석 추가                                                      |

### Issue 활용
![image](https://github.com/user-attachments/assets/3c16e50a-9c5c-4457-8f0d-13b0f9e73788)

- Github 레포지토리의 Issue탭에 Todo인 상황 혹은 In progress에 대한 상황을 작성하고 공유했습니다. 해당 Issue 번호로 각자의 로컬 레포지토리에 브랜치를 생성하여 Pull Request 시에 해당 Issue를 언급하여 공유했습니다. 해당 전략을 사용하여 Merge Conflict의 발생 가능성을 줄였습니다.

### PR 활용
![image](https://github.com/user-attachments/assets/02656e24-d5d9-4e5b-a16b-925fd674cc53)

- 다음과 같이 개발 이후 특정 프로젝트에 대한 변경사항을 제안하고, 팀원과 이를 검토 및 논의한 후, 최종적으로 해당 변경사항을 반영할 수 있도록 했습니다.
- 다른 개발자들은 해당 Pull Request를 검토하고, 필요한 경우 피드백을 제공할 수 있었습니다.
- 검토 후, Pull Request가 승인되면 변경 사항이 메인 프로젝트로 병합되도록 했습니다. 반면, 추가적인 수정이 필요한 경우 개발자는 피드백을 반영하여 수정하고, 수정된 변경사항을 다시 push 했습니다.

## 구현결과

<div align=center>

<h3>로그인 / 회원가입</h3>

![image](https://github.com/user-attachments/assets/867c85a4-761c-4ef6-842c-23b4d066fce7)
![image](https://github.com/user-attachments/assets/b1d49b5a-6e70-4a71-98b1-16f041456048)
![image](https://github.com/user-attachments/assets/2aee9521-a4ed-4e05-b8f4-5f2e5c437f09)
![image](https://github.com/user-attachments/assets/3b477bcb-c034-4107-b66a-eea815e6a21b)
![image](https://github.com/user-attachments/assets/cb493564-0c7b-4f1b-bc95-8c39e280b2a7)


<h3>체형 분석</h3>

![image](https://github.com/user-attachments/assets/6c206542-aafc-4856-a67e-e041c97a6d84)
![image](https://github.com/user-attachments/assets/e4c5b166-cbc3-4bf9-b148-91c62c2422b8)

<h3>스타일 추천</h3>

![image](https://github.com/user-attachments/assets/0e743011-7340-43a0-b272-7f9f280ea888)
<p>패션 아이템 추천</p>

![image](https://github.com/user-attachments/assets/7043c532-cf06-4031-9391-63b359b2790f)

<h3>마이페이지</h3>
<img width="986" alt="Image" src="https://github.com/user-attachments/assets/bdd28c2d-bd05-4e38-b8fd-c1b25a443d2e" />
<img width="1192" alt="Image" src="https://github.com/user-attachments/assets/e248157a-cee7-4d5f-8bde-ccf882a2a3e6" />
<img width="583" alt="Image" src="https://github.com/user-attachments/assets/a2d362ca-4994-415c-a67c-59d45797045a" />
<img width="785" alt="Image" src="https://github.com/user-attachments/assets/28089422-9155-4d36-a9d2-449c818eb985" />

<h3>비슷한 체형 게시글</h3>
<img width="994" alt="Image" src="https://github.com/user-attachments/assets/7e08d1a3-5ae5-4799-b740-17dc50ea40de" />
<img width="486" alt="Image" src="https://github.com/user-attachments/assets/59560e91-9ef8-42b8-a057-28fbfa49ac03" />


</div>

## Contact

* 박동규 : qkrehdrb0813@gmail.com
* 김재헌 : king09044@naver.com
* 서상효 : springssh0504@naver.com
* 김성욱 : so3659@naver.com
* 최윤서 : chldbstj021902@gmail.com
