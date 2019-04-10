import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;

public class Main extends Application {
    static Stage mainStage; //set to reference "stage" in the start function to avoid having to pass around reference to the main stage
    static Scene mainMenu; //make main scene global so it can be navigated back to from any part in the application

    static final int stageWidth = 640;
    static final int stageHeight = 480;

    static BoardTile[][] board = new BoardTile[8][8];

    static ServerSocket ss;
    static Socket socket;
    static String IPaddress;
    static DataInputStream in;
    static DataOutputStream out;

    public static void main(String[] args) { launch(); }

    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage; //mainStage is a class attribute, used so I can reference stage globally without passing references as arguments

        //initialize IPaddress
        try {
            IPaddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        //UI: menus and stuff, implement all menu functionality including initializing games with the hostGame() and joinGame() methods
        VBox mainMenuPane = new VBox(); mainMenuPane.setSpacing(20); mainMenuPane.setAlignment(Pos.CENTER);
        mainMenu = new Scene(mainMenuPane, stageWidth, stageHeight); //mainMenu scene is an attribute, globally available
        stage.setScene(mainMenu);

        //make the menu buttons
        Button hostButton = new Button("Host Match");
        Button joinButton = new Button("Join Match");
        Button exitButton = new Button("Exit Checkers");
        mainMenuPane.getChildren().addAll(hostButton,joinButton,exitButton);

        //set the event Handlers for the main menu buttons
        hostButton.setOnAction(e -> { hostGame(); });
        joinButton.setOnAction(e -> { joinGame(); });
        exitButton.setOnAction(e -> { Platform.exit(); });

        stage.show();
    }

    private static void hostGame() {
        //NETWORK: wait for player to connect and assign socket and I/O streams, do in separate thread

        //initialize server socket and begin waiting for client connection
        try {
            ss = new ServerSocket(6000);
            Thread waitForConnection = new Thread(new ConnectToClient());
            waitForConnection.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //UI: display IPaddress and screen waiting for opponent to connect
        VBox preGamePane = new VBox(); preGamePane.setSpacing(20); preGamePane.setAlignment(Pos.CENTER); //set up pane
        Scene preGameScene = new Scene(preGamePane, stageWidth, stageHeight); //set up scene
        //Platform.runLater(() -> {mainStage.setScene(preGameScene);} ); //set current scene, use runlater since this is all happening in a seperate thread created by the event handler for the "Host Game" button in the main method
        mainStage.setScene(preGameScene);

        Text IPaddressDisplay = new Text(IPaddress);
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            mainStage.setScene(mainMenu);
            try {
                ss.close(); //closing the ss will cause any thread stuck on the ss.accept() method to throw an exception and continue past that method, this will effectively end the waitForConnectionThread
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        mainStage.setScene(preGameScene);
        preGamePane.getChildren().addAll(IPaddressDisplay, backButton);
    }

    private static void joinGame() {
        //UI: have user enter an IP address to connect to
        VBox preGamePane = new VBox(); preGamePane.setSpacing(20); preGamePane.setAlignment(Pos.CENTER); //set up pane
        Scene preGameScene = new Scene(preGamePane, stageWidth, stageHeight); //set up scene
        mainStage.setScene(preGameScene);
        //create IPaddressField TextField
        TextField IPaddressField = new TextField(); IPaddressField.setPromptText("Enter Host's IP Adress here...");
        //create joinButton
        Button joinButton = new Button("Join");
        joinButton.setOnAction(e -> {
            try {
                //create socket and connect to server
                socket = new Socket(IPaddressField.getText(), 6000);
                System.out.println("Client: connection successful");

                //initialize IO streams
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());



                //start game
                //runGame(false);

            } catch (IOException e1) {
                e1.printStackTrace();
            } finally{
                System.out.println("hmm");
            }
        });
            //set IPaddressField to call the joinButton handler class defined directly above here when the user presses enter in the "IPaddressField" TextField
        IPaddressField.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER){
                joinButton.fire();
            }
        });
        //create backButton
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> { mainStage.setScene(mainMenu); });
        //put nodes in panes
        HBox enterAddressAndJoinPane = new HBox();  //used to hold the text field to enter the IP address as well as the join button so that they appear next to each other, this just looks prettier
        enterAddressAndJoinPane.getChildren().addAll(IPaddressField,joinButton);
        preGamePane.getChildren().addAll(enterAddressAndJoinPane, backButton);

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
        BoardPos piecePos = new BoardPos(0,0);

        while (move == null) {
            BoardPos destination;
            //UI: Player clicks on piece to move and then where they wish to move it
            //***move = getMove(piecePos, destination);
        }

        boolean gameOver = executeMove(move, piecePos);

        //NETWORK: send move and piece position to client
        /*try {
            out.writeObject(move);
            out.writeObject(piecePos);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        return gameOver;
    }

    private static boolean executeOpponentTurn() {

        //NETWORK: receive move from client, set move and piecePos variables - DONE
        /*try {
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
        }*/
        return false;
    }

    private static Move getMove(BoardPos startPos, BoardPos endPos) {
        //BACKEND: determine if the move is valid, if it is then return array of moveComponents
        //otherwise return null
        return new Move();
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
        return false;
    }

    private static void movePiece(BoardPos startPos, BoardPos endPos){
        board[endPos.row][endPos.col].checkerPiece = board[endPos.row][endPos.col].checkerPiece;
        board[endPos.row][endPos.col].checkerPiece = null;
    }

    static class ConnectToClient implements Runnable{

        @Override
        public void run() {
            try {
                //create socket to communicate with client
                socket = ss.accept();

                //initialize IO streams
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                System.out.println("Host: connection successful");

                //start game
                //runGame(true);

            } catch (SocketException e) {
                System.out.println("Waiting for client socket connection cancelled");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
