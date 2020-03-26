# java-chess
체스 게임 구현을 위한 저장소

## 요구사항
* 체스판을 초기화한다.
    * 체스판에서 말의 위치 값은 가로 위치는 왼쪽부터 a ~ h이고, 세로는 아래부터 위로 1 ~ 8로 구현한다.
    * 체스판에서 각 진영은 검은색(대문자)과 흰색(소문자) 편으로 구분한다.
        * 검은색은 세로 7, 8줄에 위치한다.
        * 흰색은 세로 1, 2줄에 위치한다.
* 게임 명령어 도움말을 출력한다.
* 게임 명령어를 입력 받는다.
    * start: 게임 시작
    * end: 게임 종료
    * move source위치 target위치 - 예. move b2 b3
        * (예외) 위치가 존재하지 않을 때
        * (예외) 자신의 이동 범위가 아닌 위치를 입력했을 때
        * (예외) 이동 중에 다른 기물을 마주칠 때
        * (예외) 같은 편 기물 위치를 target으로 입력했을 때
        * (예외) source가 empty일 때
    * (예외) 명령어를 정확하지 입력하지 않았을 때
    * (예외) start를 하지 않고 나머지 명령어를 입력했을 때
    

### 체스 기물 규칙
 * 기물의 종류: King, Queen, Bishop, Knight, Rook, Pawn
 * 기물의 이동 규칙
     * King
         * 모든 방향(좌우/상하/대각선)이든 1칸만 이동할 수 있다.
     * Queen
         * 모든 방향으로 자유롭게 이동할 수 있다.
     * Bishop
         * 대각선으로 자유롭게 이동할 수 있다.
     * Knight
         * 상하좌우 중 하나를 선택하여 한 칸 이동 후 대각선으로 한 칸 더 이동할 수 있다.
         * 혹은, 대각선으로 한 칸 이동 후 상하좌우 중 한 칸으로 이동할 수 있다.
     * Rook
         * 상하좌우로 자유롭게 이동할 수 있다.
     * Pawn
         * 이동은 전진 1칸만 이동 가능하다.
             * 첫회 한정으로 2칸 이동 가능하다.
                * (예외) 전진의 경우, 목표 지점이 empty가 아니라면 이동할 수 없다.
         * 공격은 앞 대각선으로만 공격할 수 있다.
            * (예외) 앞 대각선으로 이동했을 때 empty인 경우 공격 및 이동할 수 없다.