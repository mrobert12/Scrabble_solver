# Project 3 Scrabble

This project was for a design of large programs course.
The solver finds the highest point move given a board, a set of tiles and a dictionary.<br/>
The program uses a Trie as a dictionary structure for looking up words. It takes a set of
tiles, from scrabble or wirds with friends or some other game, a dictionary and a game board.

The algorithms used for solving the board were adaped from the Paper shared in 
class: https://www.cs.cmu.edu/afs/cs/academic/class/15451-s06/www/lectures/scrabble.pdf
and from a youtube video:https://www.youtube.com/watch?v=9cytoYiF9uY which follwed the
same paper but coding in python.

# Usage
The program takes a command line argument that is the dictionary of words to be
read in to the trie. It reads in boards from the System.in and prints the 
solutions to System.out. The type of tiles that the game takes can be changed
by changing the file that it is reading these values from in the Solver class.

# Author

Michel Robert<br/>
Microbe580@gmail.com

# To-do



#Known Bugs
No known bugs at the time