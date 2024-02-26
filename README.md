# Chess

#### Description:

This is an implementation of the Chess game, made in Java,
enhanced with a graphical user interface (GUI) built using
the Swing framework. The project provides a visually and interactive
platform for playing chess, allowing users to enjoy the game
with friends. It features a load/save game function and checks for
valid moves, as well as checks, checkmates, and stalemates.

#### Implementation:

The game is implemented by setting a 2D array of 64 squares, each
of which is an object of the Square class. Each square has a color
and a piece. The piece is an object of the Piece class, which has
a color, a type, and a boolean indicating whether it has moved.
Each piece type is a subclass of the Piece class, and has its own
move method. That allows checking for valid moves to be done per 
piece type. The game is played by clicking on a piece and then
clicking on the square to which the piece should be moved.

Each type of piece has a function called isValidMove, which checks
whether this type of piece would be allowed to move from a given
square to another given square. This function is used in 2 ways:

1. It's iterated over all squares to highlight possible move locations for the selected piece.
2. It is used to check whether a move is valid before the move is made.

It can also be used to check for checks, checkmates, and stalemates after
a move is made. This is done by checking whether the king of the player
who just made a move is in a valid move for another piece.

#### How to run:

To run the game, run the Chess.java file. The game can be played
by clicking on a piece and then clicking on the square to which
the piece should be moved. The game can be saved by clicking on
the "Save" button, and loaded by clicking on the "Load" button.

#### How the game is saved:

The game is saved by writing the board to a csv file, with a name of
your choice. The csv file contains the following information:

The first line tells which player's turn it is. The following lines start
with "PIECE" and then contain the piece's location (x and y), type and color.

#### Explanation of files:

- Chess.java: The main file, which contains the main function.
- Board.java: The class representing the board.
- Game.java: The class representing the game.
- Player.java: The class representing a player.
- Square.java: The class representing a square on the board.
- Piece.java: The class representing a piece on the board.
- Pawn.java: The class representing a pawn piece.
- Rook.java: The class representing a rook piece.
- Knight.java: The class representing a knight piece.
- Bishop.java: The class representing a bishop piece.
- Queen.java: The class representing a queen piece.
- King.java: The class representing a king piece.
- IllegalMoveException.java: The class representing an illegal move exception.