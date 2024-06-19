package com.chess.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ChessBoard extends JFrame {
    private JPanel board;
    private final JButton[][] squares = new JButton[8][8];
    private boolean isWhitePerspective = true; // Default to white perspective

    public ChessBoard(String fen) {
        // Create a window for color selection
        JFrame selectionFrame = new JFrame("Select Color");
        selectionFrame.setSize(300, 100);
        selectionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        selectionFrame.setLayout(new GridLayout(1, 2));

        JButton whiteButton = new JButton("Play as White");
        JButton blackButton = new JButton("Play as Black");

        whiteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isWhitePerspective = true;
                selectionFrame.dispose();
                initializeMainFrame(fen);
            }
        });

        blackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isWhitePerspective = false;
                selectionFrame.dispose();
                initializeMainFrame(fen);
            }
        });

        selectionFrame.add(whiteButton);
        selectionFrame.add(blackButton);
        selectionFrame.setVisible(true);
    }

    /**
     * Initializes the main chess frame based on the FEN string and user's color selection.
     *
     * @param fen The FEN string representing the chess position.
     */
    private void initializeMainFrame(String fen) {
        // Initialize the main frame
        JFrame frame = new JFrame("Pretty Shit Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        // Initialize the chessboard panel with a grid layout
        board = new JPanel();
        board.setLayout(new GridLayout(8, 8));

        // Set up the empty chessboard
        initializeBoard();

        // Add the board to the frame and make it visible
        frame.add(board);
        frame.setVisible(true);

        // Load the initial chess position using FEN notation
        loadPositionFromFEN(fen);
    }

    /**
     * Initializes the chessboard with alternating colors and adds buttons to each square.
     */
    private void initializeBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton button = new JButton();
                Color originalColor;

                originalColor = ((row + col) % 2 == 0) ? new Color(240, 240, 220) : new Color(119, 154, 88); // bottom right square should always be the light colour

                button.setBackground(originalColor);
                button.setOpaque(true);

                // Set original border and add mouse hover effects
                Border originalBorder = button.getBorder();
                Color finalOriginalColor = originalColor;
                button.addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent evt) {
                        // Highlight square on mouse enter
                        Color brighterColor = finalOriginalColor.brighter();
                        button.setBackground(brighterColor);

                        // Create a compound border for the hover effect
                        Border line = new LineBorder(brighterColor, 2);
                        Border margin = new EmptyBorder(5, 15, 5, 15);
                        Border compound = new CompoundBorder(line, margin);
                        button.setBorder(compound);
                    }

                    public void mouseExited(MouseEvent evt) {
                        // Revert to original color and border on mouse exit
                        button.setBackground(finalOriginalColor);
                        button.setBorder(originalBorder);
                    }
                });

                button.setBorderPainted(false);
                button.setFocusPainted(false);

                // Add button to the grid
                squares[row][col] = button;
                board.add(button);
            }
        }
    }

    /**
     * Loads the chess position from a FEN string and places pieces on the board.
     *
     * @param fen The FEN string representing the chess position.
     */
    private void loadPositionFromFEN(String fen) {
        String[] parts = fen.split(" ");
        String boardPart = parts[0];
        String[] rows = boardPart.split("/");

        if (isWhitePerspective) {
            for (int row = 0; row < 8; row++) {
                String rowString = rows[row];
                int col = 0;

                for (char symbol : rowString.toCharArray()) {
                    if (Character.isDigit(symbol)) {
                        int emptySquares = Character.getNumericValue(symbol);
                        col += emptySquares; // Skip empty squares
                    } else {
                        String pieceName = getPieceName(symbol);
                        if (pieceName != null) {
                            squares[row][col].setIcon(getResizedIcon("src/com/chess/ui/pieces/" + pieceName, squares[row][col].getWidth(), squares[row][col].getHeight()));
                        }
                        col++;
                    }
                }
            }
        } else {
            for (int row = 7; row >= 0; row--) {
                String rowString = rows[7 - row];
                int col = 7;

                for (char symbol : rowString.toCharArray()) {
                    if (Character.isDigit(symbol)) {
                        int emptySquares = Character.getNumericValue(symbol);
                        col -= emptySquares; // Skip empty squares
                    } else {
                        String pieceName = getPieceName(symbol);
                        if (pieceName != null) {
                            squares[row][col].setIcon(getResizedIcon("src/com/chess/ui/pieces/" + pieceName, squares[row][col].getWidth(), squares[row][col].getHeight()));
                        }
                        col--;
                    }
                }
            }
        }
    }

    /**
     * Maps FEN piece symbols to corresponding image file names.
     *
     * @param symbol The FEN character representing a chess piece.
     * @return The filename of the image representing the piece.
     */
    private String getPieceName(char symbol) {
        switch (symbol) {
            case 'r': return "blackRook.png";
            case 'n': return "blackKnight.png";
            case 'b': return "blackBishop.png";
            case 'q': return "blackQueen.png";
            case 'k': return "blackKing.png";
            case 'p': return "blackPawn.png";
            case 'R': return "whiteRook.png";
            case 'N': return "whiteKnight.png";
            case 'B': return "whiteBishop.png";
            case 'Q': return "whiteQueen.png";
            case 'K': return "whiteKing.png";
            case 'P': return "whitePawn.png";
            default: return null;
        }
    }

    /**
     * Resizes the piece icon to fit within the button.
     *
     * @param imagePath The path to the image file.
     * @param width The desired width of the icon.
     * @param height The desired height of the icon.
     * @return The resized ImageIcon.
     */
    private ImageIcon getResizedIcon(String imagePath, int width, int height) {
        try {
            BufferedImage originalImage = ImageIO.read(new File(imagePath));
            Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImage);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
