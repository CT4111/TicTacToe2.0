import javax.swing.*;
import java.awt.*;

public class MainBoard extends JPanel implements BoardClickListener {
    private SmallBoard[][] boards = new SmallBoard[3][3];
    public int[][] boardState = new int[3][3];
    private boolean playerX = true; // Track the current player

    public MainBoard() {
        setLayout(new GridLayout(3, 3));
        initializeBoards();
    }

    private void initializeBoards() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boards[i][j] = new SmallBoard(i, j, this);
                add(boards[i][j]);
            }
        }
    }

    @Override
    public void onBoardClick(int smallBoardRow, int smallBoardCol, int buttonRow, int buttonCol) {
        boardState[smallBoardRow][smallBoardCol] = boards[smallBoardRow][smallBoardCol].getGameState();
        System.out.println(boardState[0][0] + " " + boardState[0][1] + " " + boardState[0][2]);
        System.out.println(boardState[1][0] + " " + boardState[1][1] + " " + boardState[1][2]);
        System.out.println(boardState[2][0] + " " + boardState[2][1] + " " + boardState[2][2]);

        if (boardState[buttonRow][buttonCol] == 0) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    setAllBoardsEnabled(false, i, j);
                }
            }
            setAllBoardsEnabled(true, buttonRow, buttonCol);
        } else {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    setAllBoardsEnabled(true, i, j);
                }
            }
        }

        playerX = !playerX; // Switch the player after each move
        checkForOverallWinOrTie();
    }

    public boolean isPlayerX() {
        return playerX;
    }

    public void setAllBoardsEnabled(boolean enabled, int i, int j) {
        boards[i][j].setEnabled(enabled);
    }

    private void checkForOverallWinOrTie() {
        // Check rows and columns for a winner
        for (int i = 0; i < 3; i++) {
            if ((boardState[i][0] == boardState[i][1] && boardState[i][1] == boardState[i][2] && boardState[i][0] != 0) ||
                    (boardState[0][i] == boardState[1][i] && boardState[1][i] == boardState[2][i] && boardState[0][i] != 0)) {
                showWinMessage(boardState[i][0] == 1 ? "O" : "X");
                resetGame();
                return;
            }
        }

        // Check diagonals for a winner
        if ((boardState[0][0] == boardState[1][1] && boardState[1][1] == boardState[2][2] && boardState[0][0] != 0) ||
                (boardState[0][2] == boardState[1][1] && boardState[1][1] == boardState[2][0] && boardState[0][2] != 0)) {
            showWinMessage(boardState[1][1] == 1 ? "O" : "X");
            resetGame();
            return;
        }

        // Check for a tie
        boolean tie = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (boardState[i][j] == 0) {
                    tie = false;
                    break;
                }
            }
        }

        if (tie) {
            JOptionPane.showMessageDialog(this, "It's a tie!");
            resetGame();
        }
    }

    private void showWinMessage(String winner) {
        JOptionPane.showMessageDialog(this, "We have a winner: " + winner + "!");
    }

    private void resetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boards[i][j].resetBoard();
                boardState[i][j] = 0;
            }
        }
        playerX = true;
    }
}