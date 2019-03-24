import javafx.scene.shape.Rectangle;

public class BoardTile extends Rectangle {
    CheckerPiece checkerPiece;
    boolean playable;

    BoardTile(boolean playable){
        this.playable = playable;
        //the size of the tiles can be changed, 40 is just a placeholder
        this.setHeight(40);
        this.setWidth(40);
    }
    public void setCheckerPiece(CheckerPiece checkerPiece){
        this.checkerPiece = checkerPiece;
    }
}
