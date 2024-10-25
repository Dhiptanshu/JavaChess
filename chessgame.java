import java.util.Scanner;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;

public class ChessGame {
    private Piece[][] board = new Piece[8][8];
    private boolean isWhiteTurn = true;
    private String whitePlayer, blackPlayer;
    private static final String FILE_NAME = "chess_game_results.txt";

    public ChessGame() {
        tossForPieces();
        initializeBoard();
        storePlayersToFile();
    }

    private void tossForPieces() {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        System.out.print("Enter Player 1 name: ");
        String player1 = scanner.nextLine();
        System.out.print("Enter Player 2 name: ");
        String player2 = scanner.nextLine();

        System.out.println("Tossing a coin to assign pieces...");

        int tossResult = random.nextInt(2);

        if (tossResult == 0) {
            whitePlayer = player1;
            blackPlayer = player2;
            System.out.println(player1 + " wins the toss and plays with White.");
        } else {
            whitePlayer = player2;
            blackPlayer = player1;
            System.out.println(player2 + " wins the toss and plays with White.");
        }
    }

    private void storePlayersToFile() {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write("White: " + whitePlayer + "\n");
            writer.write("Black: " + blackPlayer + "\n");
            writer.write("-----------\n");
            System.out.println("Player information saved to file: " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("An error occurred while saving the player info.");
        }
    }

    private void initializeBoard() {
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn(true);  
            board[6][i] = new Pawn(false); 
        }

        board[0][0] = new Rook(true);
        board[0][7] = new Rook(true);
        board[7][0] = new Rook(false);
        board[7][7] = new Rook(false);

        board[0][1] = new Knight(true);
        board[0][6] = new Knight(true);
        board[7][1] = new Knight(false);
        board[7][6] = new Knight(false);

        board[0][2] = new Bishop(true);
        board[0][5] = new Bishop(true);
        board[7][2] = new Bishop(false);
        board[7][5] = new Bishop(false);

        board[0][3] = new Queen(true);
        board[7][3] = new Queen(false);

        board[0][4] = new King(true);
        board[7][4] = new King(false);
    }

    public void printBoard() {
        System.out.println("  a  b  c  d  e  f  g  h");
        for (int i = 0; i < 8; i++) {
            System.out.print(8 - i + " ");
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == null) {
                    System.out.print("-- ");
                } else {
                    System.out.print(board[i][j].getSymbol() + " ");
                }
            }
            System.out.println(8 - i);
        }
        System.out.println("  a  b  c  d  e  f  g  h");
    }

    public boolean movePiece(int startX, int startY, int endX, int endY) {
        Piece piece = board[startX][startY];
        if (piece == null) {
            System.out.println("No piece at starting position!");
            return false;
        }
    
        if (piece.isWhite() != isWhiteTurn) {
            System.out.println("Not your turn!");
            return false;
        }
    
        if (!piece.isValidMove(startX, startY, endX, endY, board)) {
            System.out.println("Invalid move!");
            return false;
        }
    
        Piece targetPiece = board[endX][endY];
        if (targetPiece != null && targetPiece.isWhite() == piece.isWhite()) {
            System.out.println("Invalid move! You cannot move onto your own piece.");
            return false;
        }
    
        board[endX][endY] = piece;
        board[startX][startY] = null;
    
        if (!isKingAlive()) {
            String loserName = !piece.isWhite() ? whitePlayer : blackPlayer;
            String winnerName = !piece.isWhite() ? blackPlayer : whitePlayer;
            System.out.println("Congratulations!! " + winnerName + " wins!");
            storeGameResult(winnerName, loserName); 
            System.exit(0); 
        }
    
        isWhiteTurn = !isWhiteTurn; 
        return true;
    }
    
    private boolean isKingAlive() {
        boolean whiteKingAlive = false;
        boolean blackKingAlive = false;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece currentPiece = board[i][j];
                if (currentPiece instanceof King) {
                    if (currentPiece.isWhite()) {
                        whiteKingAlive = true;
                    } else {
                        blackKingAlive = true;
                    }
                }
            }
        }

        return whiteKingAlive && blackKingAlive;
    }

    private void storeGameResult(String winner, String loser) {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write("Winner: " + winner + "\n");
            writer.write("Loser: " + loser + "\n");
            writer.write("-----------\n");
        } catch (IOException e) {
            System.out.println("An error occurred while saving the game result.");
        }
    }

    public static void main(String[] args) {
        ChessGame game = new ChessGame();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            game.printBoard();
            System.out.println((game.isWhiteTurn ? game.whitePlayer : game.blackPlayer) + "'s move");
            System.out.print("Enter move (e.g., e2 e4): ");
            String move = scanner.nextLine();

            int startX = 8 - (move.charAt(1) - '0'); 
            int startY = move.charAt(0) - 'a';
            int endX = 8 - (move.charAt(4) - '0');
            int endY = move.charAt(3) - 'a';

            game.movePiece(startX, startY, endX, endY);
        }
    }
}

abstract class Piece {
    private boolean isWhite;

    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public abstract boolean isValidMove(int startX, int startY, int endX, int endY, Piece[][] board);
    public abstract String getSymbol();
}

class Pawn extends Piece {
    public Pawn(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Piece[][] board) {
        int direction = isWhite() ? 1 : -1; 

        if (startX + direction == endX && startY == endY && board[endX][endY] == null) {
            return true;
        }

        if (startX == (isWhite() ? 1 : 6) && startX + (2 * direction) == endX && startY == endY && board[endX][endY] == null) {
            return board[startX + direction][startY] == null; 
        }

        if (startX + direction == endX && Math.abs(startY - endY) == 1 &&
            board[endX][endY] != null && board[endX][endY].isWhite() != this.isWhite()) {
            return true;
        }

        return false;
    }

    @Override
    public String getSymbol() {
        return isWhite() ? "WP" : "BP"; 
    }
}

class Rook extends Piece {
    public Rook(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Piece[][] board) {
        if (startX == endX) { 
            int step = (endY - startY > 0) ? 1 : -1;
            for (int y = startY + step; y != endY; y += step) {
                if (board[startX][y] != null) {
                    return false; 
                }
            }
            return true;
        } else if (startY == endY) { 
            int step = (endX - startX > 0) ? 1 : -1;
            for (int x = startX + step; x != endX; x += step) {
                if (board[x][startY] != null) {
                    return false; 
                }
            }
            return true;
        }
        return false; 
    }

    @Override
    public String getSymbol() {
        return isWhite() ? "WR" : "BR"; 
    }
}

class Knight extends Piece {
    public Knight(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Piece[][] board) {
        int dx = Math.abs(startX - endX);
        int dy = Math.abs(startY - endY);
        return (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
    }

    @Override
    public String getSymbol() {
        return isWhite() ? "WN" : "BN"; 
    }
}

class Bishop extends Piece {
    public Bishop(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Piece[][] board) {
        int dx = Math.abs(startX - endX);
        int dy = Math.abs(startY - endY);
        if (dx == dy) { 
            int stepX = (endX - startX > 0) ? 1 : -1;
            int stepY = (endY - startY > 0) ? 1 : -1;
            for (int i = 1; i < dx; i++) {
                if (board[startX + i * stepX][startY + i * stepY] != null) {
                    return false; 
                }
            }
            return true;
        }
        return false; 
    }

    @Override
    public String getSymbol() {
        return isWhite() ? "WB" : "BB"; 
    }
}

class Queen extends Piece {
    public Queen(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Piece[][] board) {
        Rook rook = new Rook(isWhite());
        Bishop bishop = new Bishop(isWhite());

        return rook.isValidMove(startX, startY, endX, endY, board) || bishop.isValidMove(startX, startY, endX, endY, board);
    }

    @Override
    public String getSymbol() {
        return isWhite() ? "WQ" : "BQ"; 
    }
}

class King extends Piece {
    public King(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Piece[][] board) {
        int dx = Math.abs(startX - endX);
        int dy = Math.abs(startY - endY);
        return (dx <= 1 && dy <= 1);
    }

    @Override
    public String getSymbol() {
        return isWhite() ? "WK" : "BK"; 
    }
}
