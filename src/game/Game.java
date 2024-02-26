package game;

import game.board.Board;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Game {

    private final Board board;

    private final Player player1;
    private final Player player2;

    private Player currentTurn;

    public Game(Frame frame) {

        frame.setLayout(new BorderLayout());

        // Create the board
        JPanel boardPanel = new JPanel(new BorderLayout());
        this.board = new Board(this);
        boardPanel.add(this.board, BorderLayout.CENTER);

        // Create the save button
        JButton saveButton = new JButton("Save Game");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));

                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
                String currentDate = LocalDateTime.now().format(formatter);

                fileChooser.setSelectedFile(new java.io.File(currentDate + ".csv"));

                int option = fileChooser.showSaveDialog(null);

                if (option == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    Game.this.board.save(file);
                }
            }
        });

        // Create the load button
        JButton loadButton = new JButton("Load Game");

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));

                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));

                int option = fileChooser.showOpenDialog(null);

                if (option == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    Game.this.board.load(file);
                }
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(new Color(230, 214, 185));

        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.NORTH);

        frame.setLocationRelativeTo(null);
        frame.pack();

        // Create the players
        this.player1 = new Player("Player 1", Color.WHITE);
        this.player2 = new Player("Player 2", Color.BLACK);

        // Set the current turn to player 1 (White)
        this.currentTurn = this.player1;
    }

    public Color getTurn() {
        return this.currentTurn.getColor();
    }

    public void swapTurn() {
        this.currentTurn = this.currentTurn == this.player1 ? this.player2 : this.player1;
    }

    public void endGame(String title, String description) {
        JOptionPane.showMessageDialog(null, description, title, JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

}
