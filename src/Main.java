import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;

public class Main {
    static BoardTile[][] board = new BoardTile[8][8];

    static ServerSocket ss;
    static String IPaddress;
    static ObjectOutputStream out;
    static ObjectInputStream in;

    public static void main(String[] args) {
        ss = new ServerSocket(8000);
        IPaddress = InetAddress.getLocalHost().getHostAddress();
        //UI: menus and stuff, implement all menu functionality including initializing games
    }

    private static void hostGame() {
        //UI: display IPaddress and screen waiting for opponent to connect
        //NETWORK: wait for player to connect and assign socket and I/O streams
        runGame(true);
    }

    private static void joinGame() {
        //UI: have user enter an IP address to connect
        //NETWORK: assign socket and I/O streams
        runGame(false);
    }

    private static void runGame(boolean hosting) {
        initializeBoard();
        boolean gameOver = false;
        if (hosting) {
            executePlayerTurn();
        } //host goes first
        while (!gameOver) {
            gameOver = executeOpponentTurn();
            if (gameOver) {
                break;
            }
            gameOver = executePlayerTurn();
        }
    }

    static boolean executePlayerTurn() {
        Move move = null;
        BoardPos piecePos;
        while (move == null) {
            //UI: Player clicks on piece to move and then where they wish to move it
            move = getMove(piecePos, destination);
        }
        boolean gameOver = executeMove(move, piecePos);
        //NETWORK: send move and piece position to client
        return gameOver;
    }

    private static boolean executeOpponentTurn() {
        //NETWORK: receive move from client, set move and piecePos variables
        gameOver = executeMove(move, piecePos) //executing the opponent’s move
    }

    private static Move getMove(BoardPos startPos, BoardPos endPos) {
        //BACKEND: determine if the move is valid, if it is then return array of moveComponents
        //otherwise return null
    }

    private static void initializeBoard() {
    }

    private static boolean executeMove(Move move, BoardPos piecePos) {
        //UI: animate the move (the piece at piecePos is the piece that is being moved)
        //BACKEND: update board array, determine if the game is over and whether win or loss
        //UI: if game is over, display win/lose screen
        //BACKEND return true if game is over, false otherwise
    }

}
