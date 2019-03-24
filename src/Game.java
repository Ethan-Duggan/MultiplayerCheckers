public class Game {
    CheckerPiece[][] board = new CheckerPiece[8][8];

    private void Game(boolean host){
        initializeBoard();
    }

    private void initializeBoard(){
        //initialize player's pieces at the bottom of the player's board
        for(int i = 0; i < 3; i++){
            for(int j = (i)%2; j < 8; j+=2){
                board[i][j] = new CheckerPiece(true);
            }
        }
        //initialize the enemy's pieces at the top of the player's board
        for(int i = 5; i < 8; i++){
            for(int j = (i)%2; j < 8; j+=2){
                board[i][j] = new CheckerPiece(false);
            }
        }
    }
}
