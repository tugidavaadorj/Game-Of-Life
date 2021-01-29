# Game-Of-Life

The Conway Game of Life is a simulation that is based off a set of rule. Initially, the games start off with Alive Cells on the graph. Whether these Alive Cells stay alive or not are dependent on its neighbors. While this seems like a trivial exercise at first, the Game of Life has very cool patterns and pulsars that emerge. There have been numerous studies done and this automation can actually be used to solve problems. 

For this version, I made it very simple. The user has the option to enter in specific points on the inital graph by using the x and y coordinates on a text file. Or, there is a random mode, where the user can input a specific probablility of each square initially being alive. My next goal is to create this on a front-end website and add more user-friendly features. 


The Rules (copied from Wikipedia): 
1. Any live cell with fewer than two live neighbours dies, as if by underpopulation.
2. Any live cell with two or three live neighbours lives on to the next generation.
3. Any live cell with more than three live neighbours dies, as if by overpopulation.
4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
