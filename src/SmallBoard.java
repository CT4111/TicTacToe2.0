import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SmallBoard extends JPanel implements ActionListener {
    private JButton[][] buttons = new JButton[3][3];
    private boolean isWon = false;
    private int row, col;
    private BoardClickListener listener;

    public SmallBoard(int row, int col, BoardClickListener listener) {
        this.row = row;
        this.col = col;
        this.listener = listener;
        setLayout(new GridLayout(3, 3));
        initializeButtons();
    }

    private void initializeButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 20));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].addActionListener(this);
                add(buttons[i][j]);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isWon) return; // Prevent further actions if the board is already won
        JButton buttonClicked = (JButton) e.getSource();
        if (buttonClicked.getText().equals("")) {
            boolean playerX = ((MainBoard) listener).isPlayerX(); // Get the current player from MainBoard
            buttonClicked.setText(playerX ? "X" : "O");
            checkForWin();
            checkForTie();
            notifyMainBoard(buttonClicked);
        }
    }

    private void notifyMainBoard(JButton buttonClicked) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j] == buttonClicked) {
                    listener.onBoardClick(row, col, i, j);
                    return;
                }
            }
        }
    }

    private void checkForWin() {
        // Check rows and columns
        for (int i = 0; i < 3; i++) {
            if (checkLine(buttons[i][0], buttons[i][1], buttons[i][2])) {
                showWinMessage(buttons[i][0].getText());
                return;
            }
            if (checkLine(buttons[0][i], buttons[1][i], buttons[2][i])) {
                showWinMessage(buttons[0][i].getText());
                return;
            }
        }
        // Check diagonals
        if (checkLine(buttons[0][0], buttons[1][1], buttons[2][2])) {
            showWinMessage(buttons[0][0].getText());
            return;
        }
        if (checkLine(buttons[0][2], buttons[1][1], buttons[2][0])) {
            showWinMessage(buttons[0][2].getText());
        }
    }

    private void checkForTie() {
        boolean tie = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    tie = false;
                    break;
                }
            }
        }
        if (tie) {
            JOptionPane.showMessageDialog(this, "It's a tie!");
            resetBoard();
        }
    }

    private boolean checkLine(JButton b1, JButton b2, JButton b3) {
        return !b1.getText().equals("") &&
                b1.getText().equals(b2.getText()) &&
                b2.getText().equals(b3.getText());
    }

    private void showWinMessage(String winner) {
        JOptionPane.showMessageDialog(this, "We have a winner: " + winner + "!");
        setWinnerSymbol(winner);
    }

    private void setWinnerSymbol(String winner) {
        removeAll();
        JLabel label = new JLabel(winner, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 200)); // Increase font size to fill the space
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        setLayout(new BorderLayout());
        add(label, BorderLayout.CENTER);
        revalidate();
        repaint();
        isWon = true;
        System.out.println("Winner: " + winner);
    }

    public void resetBoard() {
        removeAll();
        setLayout(new GridLayout(3, 3));
        initializeButtons();
        revalidate();
        repaint();
        isWon = false;
    }

    public void setEnabled(boolean enabled) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(enabled);
            }
        }
    }

    public int getGameState() {
        if (isWon) {
            return ((MainBoard) listener).isPlayerX() ? 2 : 1; // If playerX is false, X has won; otherwise, O has won
        }
        return 0; // Game is ongoing
    }
}