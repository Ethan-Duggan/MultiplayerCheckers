public class MoveToSpace extends MoveComponent{
    private BoardPos destination;

    MoveToSpace(BoardPos destination){
        this.destination = destination;
    }

    public BoardPos getDestination(){return destination; }
}
