import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class Main {
    static BoardTile[][] board = new BoardTile[8][8];

    static ServerSocket ss;
    static String IPaddress;
    static ObjectInputStream in;
    static ObjectOutputStream out;


    public static void main(String[] args) {
        //initialize server socket
        try {
            ss = new ServerSocket(8000);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //initialize IPaddress
        try {
            IPaddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        //UI: menus and stuff, implement all menu functionality including initializing games with the hostGame() and joinGame() methods
    }

    private static void hostGame() {
        //UI: display IPaddress and screen waiting for opponent to connect

        //NETWORK: wait for player to connect and assign socket and I/O streams - DONE
        try {
            //create socket to communicate with client
            Socket socket = ss.accept();

            //initialize IO streams
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

            //start game
            runGame(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void joinGame() {
        String hostIPaddress;

        //UI: have user enter an IP address to connect to, store IP address in the String variable "hostIPaddress"

        //NETWORK: assign socket and I/O streams - DONE
        try {
            //create socket and connect to server
            Socket socket = new Socket(hostIPaddress, 8000);

            //initialize IO streams
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

            //start game
            runGame(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void runGame(boolean hosting) {

        initializeBoard();

        boolean gameOver = false;

        if (hosting) {executePlayerTurn(); } //host goes first, so if the player is hosting their first turn is executed here
        //execute turns until game is over
        while (!gameOver) {
            gameOver = executeOpponentTurn();
            if (gameOver) {break; }
            gameOver = executePlayerTurn();
        }
    }

    static boolean executePlayerTurn() {

        Move move = null;
        BoardPos piecePos;

        while (move == null) {
            BoardPos destination;
            //UI: Player clicks on piece to move and then where they wish to move it
            move = getMove(piecePos, destination);
        }

        boolean gameOver = executeMove(move, piecePos);

        //NETWORK: send move and piece position to client
        try {
            out.writeObject(move);
            out.writeObject(piecePos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gameOver;
    }

    private static boolean executeOpponentTurn() {

        //NETWORK: receive move from client, set move and piecePos variables - DONE
        try {
            //read the opponent's move data from the input stream
            Move move = (Move) in.readObject();
            BoardPos piecePos = (BoardPos) in.readObject();

            //execute the opponent’s move and return the gameOver value
            boolean gameOver = executeMove(move, piecePos);
            return gameOver;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static Move getMove(BoardPos startPos, BoardPos endPos) {
        //BACKEND: determine if the move is valid, if it is then return array of moveComponents
        //otherwise return null
    }

    private static void initializeBoard() {
        //initialize board
        for(int i = 0; i < 8; i+=2){
            for(int j = 0; j < 8; j+=2) {
                board[i][j] = new BoardTile(false);
                board[i][j+1] = new BoardTile(true);
                board[i+1][j+1] = new BoardTile(false);
                board[i+1][j] = new BoardTile(true);
            }
        }

        //initialize checker pieces
        for(int i = 0; i < 3; i++){
            for(int j = (i)%2; j < 8; j+=2){
                board[i][j].setCheckerPiece(new CheckerPiece(true));
            }
        }
    }

    private static boolean executeMove(Move move, BoardPos piecePos) {
        //UI: animate the move (the piece at piecePos is the piece that is being moved)
        //BACKEND: update board array, determine if the game is over and whether win or loss
        //UI: if game is over, display win/lose screen
        //BACKEND return true if game is over, false otherwise
    }

    private static void movePiece(BoardPos startPos, BoardPos endPos){
        board[endPos.row][endPos.col].checkerPiece = board[endPos.row][endPos.col].checkerPiece;
        board[endPos.row][endPos.col].checkerPiece = null;
    }
}
