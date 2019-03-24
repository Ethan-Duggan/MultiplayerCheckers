import javafx.scene.shape.Circle;

public class CheckerPiece extends Circle {
    private boolean playerPiece;  //true if the the piece is one of the player's pieces
    private boolean king = false;

    CheckerPiece(boolean playerPiece){
        this.playerPiece = playerPiece;
    }

    public boolean isPlayerPiece(){
        return(playerPiece);
    }

    public boolean isKing(){
        return(king);
    }

    public void makeKing(){
        king = true;
    }
}
