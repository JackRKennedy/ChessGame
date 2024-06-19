package com.chess.ui;

public class Main {
    public static void main(String[] args) {
        String newBoardFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        /*
        For testing purposes, this is complex position to load through fen
        String complexMiddleGamePosition = "r1bq1rk1/pp2bppp/2n1pn2/3p4/2pP4/2N1PN2/PP3PPP/R1BQKB1R w KQ - 2 8";
         */

        new ChessBoard(newBoardFen);
    }
}