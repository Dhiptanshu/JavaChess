# JavaChess
Semester 3 Java Project - terminal-based chess game.

Pre-requisites: 
1. Installed JDK (Java Development Kit) with proper path setup. If you haven't already, go to https://www.oracle.com/in/java/technologies/downloads/ and download the JDK version of your choice.
2. If you have installation difficulty, you can follow this guide https://www.geeksforgeeks.org/download-and-install-java-development-kit-jdk-on-windows-mac-and-linux/.

Steps to run the game:
1. Save the file chessgame.java on your pc.
2. Go to your terminal and ensure you are in the same directory as the one where you saved *chessgame.java*. For example, if you have stored chessgame.java on downloads, type - **cd downloads** on your terminal.
3. javac chessgame.java to compile.
4. java chessgame.java to run.

That was easy. Now, how do you play the game? First, you should know all the chess rules and how the pieces move. 
If you don't know how to play chess, this is a great guide - https://www.chess.com/learn-how-to-play-chess.
If you already know, let's move forward.
1. You run the game as stated in the above steps and enter the names of the players. The data will be stored in a file *chess_game_results.txt* which will be stored in the same directory/folder as *chessgame.java*.
2. The white player starts. You move the piece by entering the starting and ending coordinates. For example, a valid move for white is *e7 e5*. Then a valid move for black is *e2 e4*.
3. Your main motive is to defend your king and kill the other player's king. After a player successfully does that, the game ends and the results are stored in *chess_game_results.txt*.
