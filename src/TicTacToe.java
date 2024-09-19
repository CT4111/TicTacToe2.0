import javax.swing.*;

public class TicTacToe {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.add(new MainBoard());
        frame.setVisible(true);
    }
}