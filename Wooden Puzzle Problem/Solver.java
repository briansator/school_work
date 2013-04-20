//solver class
/*
 * CS 560 "Wooden Puzzle Problem"
 *
 * Brian Sator
 * Johnathan Palace
 * Patrick Impey
 * Alexandria Munoz
 *
 */

import java.util.*;


// Class to produce the solution to the Wooden Box Problem
public class Solver {   
        private HashSet<Board> memoConfig;
        private LinkedList<Board> remember;
        private Board finalAns;
        private Board starterBoard;
        private ArrayList<Integer[]> movesPath;
        
        /**
         * Constructor
         * @param initial integer array representing the starting board.
         * @param goal integer array representing the goal configuration.
         */
        public Solver(int[] initial,int[] goal){
                starterBoard = new Board(initial,0);
                finalAns = new Board(goal,1);
                remember = new LinkedList<Board>();
                memoConfig = new HashSet<Board>();
                
                this.solve(starterBoard);
                
        }
        
        /**
         * trackMoves returns the sequence of movesPath taken to get to the goal configuration
         * @param currentBoard board to track the moves of
         * @return Array of integers representing the moves to reach the board
         */
        private static ArrayList<Integer[]> trackMoves(Board currentBoard){
                ArrayList<Integer[]> bMoveList = new ArrayList<Integer[]>();
                while(currentBoard!=null){
                        if(currentBoard.getMove()!=null){
                                bMoveList.add(currentBoard.getMove());                      
                                currentBoard = currentBoard.getParent();
                        }else{
                                break;
                        }
                }
                //we have to reverse our list at the end in order to present
                //the moves from start to finish to the user
                Collections.reverse(bMoveList);
                return bMoveList;
        }
        
        /**
         * printBoardMoves prints the moves taken to get to the final configuration 
         * @param movesPath an array of integers used to print the path
         */
        public void printBoardMoves(ArrayList<Integer[]> movesPath){
        		//set up integer to track total number of moves
        		int nummovesPath = 0;
        		//iterate through the moves path in order to spit out the order of moves to follow
                for(Iterator<Integer[]> i=movesPath.iterator();i.hasNext();){
                        Integer[] current = i.next();
                        if(current!=null){
                                System.out.print("Move piece at coordinates ("+current[0]+","+current[1]+") one unit ");                        
                                if((current[2]-current[0])==1)
                                	System.out.println("down");
                                else if((current[2]-current[0])==-1)
                                	System.out.println("up");
                                else if((current[3]-current[1])==1)
                                	System.out.println("right");
                                else if((current[3]-current[1])==-1)
                                	System.out.println("left");
                                	
                        }
                        nummovesPath++;
                }
                //print out the total number of moves after printing out the order of moves
                System.out.println("The total number of movesPath is "+nummovesPath);
        }
        
        /**
         * Get moves method
         * @return movesPath gives the path of moves
         */
        public ArrayList<Integer[]> getmovesPath(){
                return movesPath;
        }
        
        /**
         * Solver function
         * 
         * @param currBoard is the initial board configuration to solve from
         */
        public void solve(Board currBoard){
                remember.addFirst(currBoard);
                while(!remember.isEmpty()){
                		//here we test to see if the the current board is the answer
                		//if success we exit the solver and print the moves
                        if(currBoard.ansFound(finalAns)){
                                movesPath = trackMoves(currBoard);
                                printBoardMoves(movesPath);
                                return;
                        }
                        //all the potential moves that a given block can make
                        for(int i = 0;i<currBoard.getBlocks().size();i++){
                        		Board left      = currBoard.move(i, 0, -1);
                        		Board right		= currBoard.move(i, 0, 1);
                        		Board up        = currBoard.move(i, 1, 0);
                                Board down      = currBoard.move(i, -1, 0);

                                //we then test each of the potential moves to see if
                                //we already have that configuration, if not we save it
                                if(left!=null && !memoConfig.contains(left)){
                                    left.setParent(currBoard);
                                    memoConfig.add(left);
                                    remember.addFirst(left);
                                }
                                if(right!=null && !memoConfig.contains(right)){
                                    right.setParent(currBoard);
                                    memoConfig.add(right);
                                    remember.addFirst(right);
                                }
                                if(up!=null && !memoConfig.contains(up)){
                                    up.setParent(currBoard);
                                    memoConfig.add(up);
                                    remember.addFirst(up);
                                }
                                if(down!=null && !memoConfig.contains(down)){
                                    down.setParent(currBoard);
                                    memoConfig.add(down);
                                    remember.addFirst(down);
                                }
                                
                                
                                                       
                        }
                        
                        //change the current board for a new check with the next loop iteration
                        currBoard = remember.pollFirst();
                }               
                System.exit(1);
                return;
        }
        
        /**
         * main class for the solver program with initial configuration
         * 
         */
        public static void main(String[] args) {
        		//set up initial configuration as well as the goal configuration
        		//each collection of 4 integers represents one block following the pattern
        		//height,width,row position of the upper left of the block, column position
        		//of the upper left of the block
                int[] initialConfiguration = {1,2,0,0,1,2,0,2,1,2,3,0,1,2,3,2,2,1,1,2,1,1,0,4,1,1,1,3,1,1,2,3,1,1,3,4,2,2,1,0};
                int[] goalConfiguration ={2,2,1,3};
                
                new Solver(initialConfiguration, goalConfiguration);
                       
                        
        }
        
}