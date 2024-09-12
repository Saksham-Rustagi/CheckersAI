package checkers.copy;

import java.util.ArrayList;
import java.util.Scanner;

import checkers.copy.Move;


public class Board{
	public static final int EMPTY = 0,WHITE = 1, WHITE_KING = 2,BLACK = 3, BLACK_KING = 4;

	private int[][] board = new int[8][8];
	private int gameState;
	private int turn;
	private ArrayList<Move> doublej = new ArrayList<Move>();
	private ArrayList<Move> moveHistory = new ArrayList<Move>();
	public Board(){
		this.gameState = 0;
		this.turn = WHITE;
	}

	//Sets up the board to starting state. 
	public void setUp(){
		for(int row = 0; row < 3; row++){
			for(int col = 0; col < 8; col++){
				if((row + col)%2 == 1){
					board[row][col] = BLACK;
				} 
			}
		}
		for(int row = 7; row > 4; row--){
			for(int col = 0; col < 8; col++){
				if((row + col) %2 == 1){
					board[row][col] = WHITE;
				} 
			}
		}
	}

	// Gets the turn value.
	public int getTurn(){
		return turn;
	}

	//Gets game state value
	public int getState(){
		return gameState;
	}

	//Checks state and calculates
	public int checkState(){

		ArrayList<Move> moves = getLegalMoves(turn);
		if(moves.size() == 0) {
			return 4-turn;
		}else {
			return 0;
		}
	}
	/*
@param scan is the input the person gives in console
Takes users moves
	 */
	public void getMove(Scanner scan){
		if(turn==WHITE){System.out.println("White to move:");}
		if(turn==BLACK){System.out.println("Black to move: ");}
		System.out.println("Row of piece");
		int rold = scan.nextInt()-1;
		System.out.println("Column of piece");
		int cold = scan.nextInt()-1;
		System.out.println("Row of where to move");
		int rnew = scan.nextInt()-1;
		System.out.println("Column of where to move");
		int cnew = scan.nextInt()-1;
		moveTester(new Move(rold,cold,rnew,cnew));

	}

	/*
	 */
	public void moveTester(Move move){

		ArrayList<Move> moves = getLegalMoves(turn);

		if(moves.contains(move)){
			doMove(move);
		}
		else{System.out.println("Error in input. Try again");}
	}

	
	//Makes move onto board regardless of if it is legal
	public void doMove(Move move) {
		moveHistory.add(move);
		board[move.getrn()][move.getcn()] = turn;
		board[move.getr()][move.getc()] = EMPTY;

		//Checks if it was a capture
		if(move.isJump()){
			board[(move.getr()+move.getrn())/2][(move.getc()+move.getcn())/2] = EMPTY;

			//Checks for Double Jump
			doublej = getLegalCapturesFrom(turn,move.getrn(),move.getcn());
			if(doublej.size()!=0){
				turn = 4 - turn; 
				System.out.println("Take Another Jump");
			}
			else{doublej = new ArrayList<Move>();}
		}

		//Checks if becomes a king
		if(move.getrn()==7 && board[move.getrn()][move.getcn()] == BLACK) {
			board[move.getrn()][move.getcn()] = BLACK_KING;
		}
		if(move.getrn()==0 && board[move.getrn()][move.getcn()] == WHITE) {
			board[move.getrn()][move.getcn()] = WHITE_KING;
		}
		
		//Switch turn and check if game over
		turn = 4 - turn;
		gameState = checkState();
		System.out.println("Move Completed");
	}
	
	//Undos a certain move: Not done
	public void undoMove() {
		Move move = moveHistory.get(moveHistory.size()-1);
		moveHistory.remove(moveHistory.size()-1);
		board[move.getrn()][move.getcn()] = EMPTY;
		board[move.getr()][move.getc()] = 4 - turn;
		if(move.isJump()) {
			board[(move.getr()+move.getrn())/2][(move.getc()+move.getcn())/2] = turn;
		}
		if(move.getrn()==7 && board[move.getrn()][move.getcn()] == BLACK) {
			board[move.getrn()][move.getcn()] = BLACK_KING;
		}
		if(move.getrn()==0 && board[move.getrn()][move.getcn()] == WHITE) {
			board[move.getrn()][move.getcn()] = WHITE_KING;
		}
		
		turn = 4-turn;
	}

	//Prints the board as Text
	public void print(){
		System.out.println("  1 2 3 4 5 6 7 8");
		for(int rw=0;rw<8;rw++){
			System.out.print(rw+1);
			for(int cm=0;cm<8;cm++){
				switch(board[rw][cm]){
				case EMPTY:
					System.out.printf("%2s", "  ");
					break;
				case WHITE:
					System.out.printf("%2s",WHITE);
					break;
				case WHITE_KING:
					System.out.printf("%2s",WHITE_KING);
					break;
				case BLACK:
					System.out.printf("%2s",BLACK);
					break;
				case BLACK_KING:
					System.out.printf("%2s",BLACK_KING);
					break;
				}
			}
			System.out.println("");
		}
	}

	public ArrayList<Move> getLegalMoves(int player){
		ArrayList<Move> moves;
		if(doublej.size()!=0) {
			return doublej;
		}
		moves = availCaptures();
		if(moves.size()==0){
			moves = availMoves();
		} 
		return moves;
	}
	/*
  @param x is the x position of checker being checked.
  @param y is the y position of checker being checked.
  Function returns list of available moves that are not captures for a checker.
 33 */
	public ArrayList<Move> getLegalCapturesFrom(int player, int r, int c){
		ArrayList<Move> legalCaptures = new ArrayList<Move>();

		if(board[r][c] == player){
			int checker = 0;
			try{
				checker = board[r][c];
			}catch(ArrayIndexOutOfBoundsException e){}

			//Checks for a checker that is the right color
			if(!(turn==checker || turn+1 == checker)){return legalCaptures;}
			int oppColor = 4-turn;

			//Checks different squares for the different colors
			switch(checker){
			case WHITE_KING:
			case BLACK_KING:
			case WHITE:
				for(int i=2; i>=-2; i=i-4){
					try{
						if(board[r-2][c+i] == EMPTY && board[r-1][c+i/2] == oppColor){
							legalCaptures.add(new Move(r,c,r-2,c+i));                        
						}
					}catch(ArrayIndexOutOfBoundsException e){}
				}
				if(board[r][c]==WHITE){break;}
			case BLACK:
				for(int i=2; i>=-2; i=i-4){
					try{
						if(board[r+2][c+i] == EMPTY && board[r+1][c+i/2] == oppColor){
							legalCaptures.add(new Move(r,c,r+2,c+i));
						}
					}catch(ArrayIndexOutOfBoundsException e){}
				}
				break;
			case EMPTY:
				break;
			}

			return legalCaptures;
		}
		else{
			return legalCaptures;
		}
	}
	public ArrayList<Move> availCaptures(){
		ArrayList<Move> availCaptures = new ArrayList<Move>();
		int checker = 0,c,r;

		//Loops though every square on the board
		for(int j = 0; j<64; j++){
			c = j / 8;
			r = j % 8;
			availCaptures.addAll(getLegalCapturesFrom(turn,r,c));
			System.out.println(availCaptures);
		}
		return availCaptures;
	}

	/*
  @param x is the x position of checker being checked.
  @param y is the y position of checker being checked.
  Function returns list of available moves that are not captures for a checker.
	 */
	public ArrayList<Move> availMoves(){
		ArrayList<Move> aMoves = new ArrayList<Move>();
		int checker = 0, c, r;
		for(int j=0;j<64;j++){
			c = j % 8;
			r = j / 8;

			try{
				checker = board[r][c];
			}catch(ArrayIndexOutOfBoundsException e){}
			if(!(turn==checker || turn+1 == checker)){continue;}
			int oppColor = 4-turn;
			switch(checker){
			case WHITE_KING:
			case BLACK_KING:
			case WHITE:
				for(int i=1; i>=-1; i=i-2){
					try{
						if(board[r-1][c+i]==EMPTY){
							aMoves.add(new Move(r,c,r-1,c+i));                        
						}
					}catch(ArrayIndexOutOfBoundsException e){}
				}
				if(board[r][c]==WHITE){break;}
			case BLACK:
				for(int i=1; i>=-1; i=i-2){
					try{
						if(board[r+1][c+i]==EMPTY){
							aMoves.add(new Move(r,c,r+1,c+i));                         
						}
					}catch(ArrayIndexOutOfBoundsException e){}
				}
				break;
			case EMPTY:
				break;
			}
		}
		return aMoves;
	}
} 