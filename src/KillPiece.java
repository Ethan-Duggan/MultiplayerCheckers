public class KillPiece extends MoveComponent{
    private BoardPos destination;

    KillPiece(BoardPos destination){
        this.destination = destination;
    }

    public BoardPos getDestination(){return destination; }
}
