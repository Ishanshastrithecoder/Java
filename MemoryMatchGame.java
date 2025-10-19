import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class MemoryMatchGame extends JFrame implements ActionListener {

    private final int SIZE = 4; // 4x4 grid = 8 pairs
    private final JButton[] buttons = new JButton[SIZE * SIZE];
    private final Integer[] cardValues = new Integer[SIZE * SIZE];

    private JButton firstButton = null;
    private JButton secondButton = null;
    private int firstValue, secondValue;
    private boolean checking = false;
    private int moves = 0;
    private int matchedPairs = 0;
    private JLabel statusLabel;

    public MemoryMatchGame() {
        setTitle("🧠 Memory Match Game");
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        // Top panel (status)
        statusLabel = new JLabel("Moves: 0", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(statusLabel, BorderLayout.NORTH);

        // Grid panel
        JPanel gridPanel = new JPanel(new GridLayout(SIZE, SIZE, 5, 5));
        gridPanel.setBackground(new Color(50, 50, 50));
        add(gridPanel, BorderLayout.CENTER);

        // Prepare pairs (0–7 duplicated)
        int k = 0;
        for (int i = 0; i < SIZE * SIZE / 2; i++) {
            cardValues[k++] = i;
            cardValues[k++] = i;
        }

        // Shuffle safely
        java.util.List<Integer> list = Arrays.asList(cardValues);
        Collections.shuffle(list);

        // Create buttons
        for (int i = 0; i < buttons.length; i++) {
            JButton btn = new JButton("");
            btn.setFont(new Font("Arial", Font.BOLD, 26));
            btn.setBackground(Color.LIGHT_GRAY);
            btn.setFocusPainted(false);
            btn.addActionListener(this);
            buttons[i] = btn;
            gridPanel.add(btn);
        }

        getContentPane().setBackground(Color.DARK_GRAY);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (checking) return; // prevent clicking while checking

        JButton clicked = (JButton) e.getSource();
        int index = Arrays.asList(buttons).indexOf(clicked);

        if (index == -1 || !clicked.isEnabled()) return;

        clicked.setText(String.valueOf(cardValues[index]));
        clicked.setBackground(Color.WHITE);

        if (firstButton == null) {
            firstButton = clicked;
            firstValue = cardValues[index];
        } else if (secondButton == null && clicked != firstButton) {
            secondButton = clicked;
            secondValue = cardValues[index];
            moves++;
            statusLabel.setText("Moves: " + moves);
            checking = true;

            javax.swing.Timer timer = new javax.swing.Timer(700, evt -> {
                if (firstValue == secondValue) {
                    firstButton.setEnabled(false);
                    secondButton.setEnabled(false);
                    firstButton.setBackground(Color.GREEN);
                    secondButton.setBackground(Color.GREEN);
                    matchedPairs++;

                    if (matchedPairs == (SIZE * SIZE) / 2) {
                        JOptionPane.showMessageDialog(this,
                                "🎉 You won in " + moves + " moves!",
                                "Game Over", JOptionPane.INFORMATION_MESSAGE);
                        resetGame();
                    }
                } else {
                    firstButton.setText("");
                    secondButton.setText("");
                    firstButton.setBackground(Color.LIGHT_GRAY);
                    secondButton.setBackground(Color.LIGHT_GRAY);
                }
                firstButton = null;
                secondButton = null;
                checking = false;
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void resetGame() {
        java.util.List<Integer> list = Arrays.asList(cardValues);
        Collections.shuffle(list);

        for (JButton b : buttons) {
            b.setEnabled(true);
            b.setText("");
            b.setBackground(Color.LIGHT_GRAY);
        }

        matchedPairs = 0;
        moves = 0;
        statusLabel.setText("Moves: 0");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MemoryMatchGame::new);
    }
}
