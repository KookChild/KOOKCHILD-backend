
# Git Convention
### 커밋 메세지

- feat: : 새로운 기능 추가/수정/삭제
- refactor : 버그를 수정하거나 기능 추가가 없는 단순 코드 변경
- fix: 버그, 오류 해결
- test: : 테스트 코드
- chore: 빌드 업무 수정, settings.py 수정 등
- docs: Readme를 비롯한 문서 변경시
- init: initial commit을 할 시
- build: 라이브러리 추가 등
- !HOTFIX: 치명적 버그를 급하게 수정할 때

### 네이밍 규칙

- class: Pascal ex) MyClass, PersonInfo
- Variable: Camel ex) userName, totalCount
- Function: Camel ex) calculateTotal, getUserData
- Constant : Pascal + Snake pascal ex) MAX_SIZE, DEFAULT_TIMEOUT


### 🚷 KookChild 서버 규칙

1. 각자 branch에서 `dev` 브랜치에 PR을 날려요.
2. PR올리면 카톡으로 알려주고, 그 날 자정까지 확인하고 코드 리뷰를 후 승인 뒤에 `merge`해요.
3. DB 관련된 `model` 코드 수정은 한명이 해요. 모델 수정이 필요하면 공유해요.
4. 커밋 메세지를 잘 지켜요.
5. 주석 및 네이밍 규칙도 잘 지켜요.
6. 한 일과, 해야할 일을 솔직하게 공유해요.

