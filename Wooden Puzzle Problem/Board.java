
//Block class
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


/**
 * Board class. 
 *
 *
 */
public class Board {
        private static final int emptySpace = -1;

        private ArrayList<Block> boardPieces;
        private Integer[] lastPieceMoved;
        private Board parentBoard = null;
        private int rows = 4, columns = 5;
        private int[][] boardRepresentation;

        /**
         * Board class constructor.
         * 
         * @param boardConfig gives a board configuration
         * @param configVal tells if it is the start board or goal board
         */
        public Board(int[] boardConfig,int configVal) {
                boardPieces = new ArrayList<Block>();
                
                //Grab board configuration
                getBoardBlocks(boardConfig, configVal);
                //set our representation of the current board for testing equals to
                boardRepresentation = new int[rows][columns];
                //make the call to update our board with new information
                updateBoard();
        }
        
        
        
        /**
         * Copy an already instantiated board.
         * @param copy Board object to be copied.
         */
        public Board(Board toCopy) {
                this.rows = toCopy.rows;
                this.columns = toCopy.columns;
                boardRepresentation = new int[rows][columns];
                
                //we need to pull out the individual pieces of a board configuration
                boardPieces = new ArrayList<Block>(toCopy.boardPieces.size());
                for (Iterator<Block> i = toCopy.boardPieces.iterator(); i.hasNext();) {
                        Block currBlock = i.next();
                        boardPieces.add(new Block(currBlock.blockID, currBlock.length,
                                        currBlock.width, currBlock.row, currBlock.column));
                }
                
                //then we save those pieces and their placement in our "copy"
                for (int i = 0; i < toCopy.rows; i++) {
                        for (int j = 0; j < toCopy.columns; j++) {
                                boardRepresentation[i][j] = toCopy.boardRepresentation[i][j];
                        }
                }
        }
        
        
        
        //this is necessary to use a hashset
        /**
         * Returns the hash code of the board.
         * 
         */
        public int hashCode() {
                String myTemp = "";
                String[] beenSorted = sortBlocks(boardPieces);
                for (int i = 0; i < beenSorted.length; i++) {
                        myTemp += beenSorted[i];
                }
                return myTemp.hashCode();
        }
        
        //in order to create a hash representation of a configuration
        //we need to sort the Blocks in order to ensure we create a unique hash value
        /**
         * Returns a string with sorted blocks.
         * @param sortMe ArrayList of Blocks to be sorted
         *
         */
        public String[] sortBlocks(ArrayList<Block> toSort) {
                String[] beenSorted = new String[toSort.size()];
                for (int i = 0; i < toSort.size(); i++) {
                        beenSorted[i] = Integer.toString(toSort.get(i).getLength()) + " "
                                        + Integer.toString(toSort.get(i).getWidth()) + " "
                                        + Integer.toString(toSort.get(i).getRow()) + " "
                                        + Integer.toString(toSort.get(i).getCol());
                }

                for (int p = 1; p < beenSorted.length; p++) {
                        String tmp = beenSorted[p];
                        int j = p;
                        for (; j > 0 && tmp.compareTo(beenSorted[j - 1]) < 0; j--) {
                                beenSorted[j] = beenSorted[j - 1];
                        }
                        beenSorted[j] = tmp;
                }
                return beenSorted;
        }
        
        /**
         * 
         * @return boardPieces, all of the pieces of a given Board.
         */
        public ArrayList<Block> getBlocks() {
                return boardPieces;
        }

        /**
         *
         * @return lastPieceMoved, the last piece to be moved in a given Board.
         */
        public Integer[] getMove() {
                return lastPieceMoved;
        }
        
        /**
         * 
         * Set's the blocks parentBoard to parent.
         * @param parent the board to set as the new parent.
         */
        public void setParent(Board parent) {
                parentBoard = parent;
        }
        
        /**
         * 
         * @return parentBoard of a given Board.
         */
        public Board getParent() {
                return parentBoard;
        }

        /**
         * Updates our matrix according to the blocks 
         */
        private void updateBoard() {
                
                //Resets the board
                for (int r = 0; r < rows; r++) {
                        for (int c = 0; c < columns; c++) {
                                boardRepresentation[r][c] = emptySpace;
                        }
                }
                //we then iterate through the block pieces and reconstruct the
                //board in order to ensure that we have updated it completely
                for (Iterator<Block> i = boardPieces.iterator(); i.hasNext();) {
                        Block current = i.next();
                        int startRow = current.getRow();
                        int startCol = current.getCol();
                        int length = current.getLength() + startRow;
                        int width = current.getWidth() + startCol;
                        for (int r = startRow; r < length; r++) {
                                for (int c = startCol; c < width; c++) {
                                        boardRepresentation[r][c] = current.blockID;
                                }
                        }
                }
        }
        
        
        /**
         * ansFound performs a check to seed if the configuration is the goal configuration
         * @param test answer configuration to test against
         * @return True if the answer has been found
         */
        public boolean ansFound(Board test) {
        		//get the blocks from the board to test against
                for (Iterator<Block> i = test.boardPieces.iterator(); i.hasNext();) {
                        Block block = i.next();
                        int id = boardRepresentation[block.row][block.column];
                        if (id == -1) {
                        		//not our answer
                                return false;
                        }
                        //test to see if our blocks match
                        Block matching = boardPieces.get(id);
                        if (!matching.equals(block)) {
                        		//not our answer
                                return false;
                        }
                }
                //the answer was found
                return true;
        }

        /**
         * 
         * @param initialConfig the board to get the blocks from.
         * @param select is the type of board we are checking, initial or goal.
         *           
         */
        private void getBoardBlocks(int[] initialConfig,int select) {
        	//set up a variable that we will increment in order to provide unique ID's
        	//for each block in a board
            int numID = 0;
            //initial configuration
            if(select == 0){
            	for(int i = 0; i < 40; i+=4) {
                    boardPieces.add(new Block(numID, initialConfig[i], initialConfig[i+1], initialConfig[i+2], initialConfig[i+3]));
                    numID++;
                }
            }
            //goal configuration
            else if(select == 1){
                 boardPieces.add(new Block(numID, initialConfig[0], initialConfig[1], initialConfig[2], initialConfig[3]));
            }
        }
        
        /**
         * Getter
         * @return the row value for the board.
         */
        public int getRows() {
                return rows;
        }

        /**
         * Getter
         * @return the column value for the board.
         */
        public int getcolumns() {
                return columns;
        }

        /**
         * Getter
         * @param index the id of the block we are trying to grab.
         * @return returns the block.
         */
        public Block getBlock(int index) {
                if (index < boardPieces.size()) {
                        return boardPieces.get(index);
                } else {
                        return null;
                }
        }

        /**
         * move method for moving blocks on the board.
         * 
         * @param blockIndex tells the function which block to move
         * @param rows value of rows to move.
         * @param columns value of columns to move.
         * @return the new board with a moved block.
         */
        public Board move(int blockIndex, int rows, int columns) {
                Board returnBoard = new Board(this);
                Block block = returnBoard.getBlock(blockIndex);
                int row = block.getRow();
                int column = block.getCol();
                int length = block.getLength();
                int width = block.getWidth();
                int newRow = row + rows;
                int newCol = column + columns;

                

                if (!returnBoard.checkArea(block.blockID, newRow, newCol, length, width)) {
                        return null;
                } else {
                        
                       
                        //we need to remember the information for the last
                		//piece moved, in order to print the moves in the end
                        
                        returnBoard.lastPieceMoved = new Integer[4];
                        returnBoard.lastPieceMoved[0] = block.row;
                        returnBoard.lastPieceMoved[1] = block.column;
                        returnBoard.lastPieceMoved[2] = newRow;
                        returnBoard.lastPieceMoved[3] = newCol;

                        block.row = newRow;
                        block.column = newCol;
                        returnBoard.updateBoard();
                }
                return returnBoard;
        }

        /**
         * To see if our board is equal to another board
         * @param test the configuration to check against
         */
        public boolean equals(Object test) {
                Board otherBoard = (Board) test;
                //we iterate through, going through each column first and then
                //moving down to the next row and going through the columns
                for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < columns; j++) {
                                if (otherBoard.boardRepresentation[i][j] != -1
                                                && boardRepresentation[i][j] == -1) {
                                		//not equal
                                        return false;
                                }

                                if (boardRepresentation[i][j] != -1
                                                && otherBoard.boardRepresentation[i][j] == -1) {
                                		//not equal
                                        return false;
                                }
                        }
                }
                //equal!
                return true;
        }

        /**
         * Check to see if a space is open.
         * 
         * @param row to start at.
         * @param col to start at.
         * @param numRows length to check in the configuration
         * @param numcolumns width to check in the configuration
         * @return if the area is empty
         */
        private boolean checkArea(int id, int row, int col, int rowsMove, int colsMove) {
                int endRow = row + rowsMove;
                int endCol = col + colsMove;
                //check to make sure move is possible within the confines of the board
                if (endRow > rows || endCol > columns || row < 0 || col < 0) {
                        return false;
                }
                for (int j = col; j < endCol; j++) {
                        for (int i = row; i < endRow; i++) {
                                if (boardRepresentation[i][j] != emptySpace && boardRepresentation[i][j] != id) {
                                        return false;
                                }
                        }
                }

                return true;
        }

        
        /*
        *We need an inner block class to hold our block sizes
        *as well as the location of a block.  This block class
        *stores all of this information as well as providing us
        *with the appropriate getter methods as well as an equals
        *method
        */
        
        /**
         * Block class to hold a board's blocks
         */
        public class Block {
                private int length, width, row, column;
                private int blockID;

                //Block class constructor
                public Block(int id, int length, int width, int row, int column) {
                        this.length = length;
                        this.width = width;
                        this.row = row;
                        this.column = column;
                        this.blockID = id;
                }
                

                
                /**
                 * Getter
                 * @return length
                 */
                public int getLength() {
                        return length;
                }

                /**
                 * Getter
                 * @return width
                 */
                public int getWidth() {
                        return width;
                }

                /**
                 * Getter
                 * @return row of the upper left hand part of the block
                 */
                public int getRow() {
                        return row;
                }
                
                /**
                 * Getter
                 * @return column of the upper left hand part of the block
                 */
                public int getCol() {
                        return column;
                }

                /**
                 * Equals test for blocks
                 * @param test block to compare this block to.
                 * 
                 * @return  if the two blocks are equal
                 */
                public boolean equals(Object test) {
                        Block other = (Block) test;
                        return other.row == row && other.column == column
                                        && other.length == length && other.width == width;
                }
        }
        //End of Block class
        
        

        

 
}
