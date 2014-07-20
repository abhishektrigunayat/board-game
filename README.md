Implementation of a program to simulate a board game consisting of a 10 by 10 grid, and an avator.  

The player will interact with the game through the following set of commands:  
  
"PLACE <x> <y> <d>" - Place the avator at position x, y on the grid, facing direction d  
"TURN LEFT" - Tell the avator to make a left turn  
"TURN RIGHT" - Tell the avator to make a right run  
"FORWARD" - Tell the avator to take one step forward  
"STATUS" - Report the current position and direction of the avator  
  
The game should ignore commands that will cause the avator to move outside of the grid.  
  
Example program:  
  
PLACE 0, 0, NORTH  
FORWARD  
FORWARD  
TURN RIGHT  
FORWARD  
STATUS  
  
Example output:  
Avator is at (2, 1) facing EAST  
