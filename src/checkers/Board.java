package checkers;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class Board extends JComponent implements MouseListener{
	public static final int EMPTY = 0,WHITE = 1, WHITE_KING = 2,BLACK = 3, BLACK_KING = 4;

	private Data board = new Data(new int[8][8], WHITE);
	private int gameState;

	private int selectedRow = -1;
	private int selectedCol = -1;
	private ArrayList<Move> selectedLegalMoves;

	private ArrayList<Data> history = new ArrayList<Data>();

	private JLabel message;
	private JButton reset;
	private JButton undo;

	//Constructs board object
	public Board(){
		this.gameState = 0;
		setBackground(Color.BLACK);
		addMouseListener(this);
		setLayout(null);

		message = new JLabel("Red to Move");
		message.setFont(new Font("Georgia", Font.BOLD, 14));
		message.setForeground(new Color(20, 20, 25));
		message.setBounds(0,0,200,200);

		reset = new JButton("New Game");
		reset.setBounds(0,200,120,50);
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setUp();
				repaint();
			} }); 

		undo = new JButton("Undo");
		undo.setBounds(0, 300, 120, 50);
		undo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				undoMove();
				repaint();
			} }); 

		add(reset);
		add(message);
		add(undo);
		setUp();
    
	}



	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int squarex, squarey, squaresize = getHeight()/8;

		for(int r = 0; r < 8; r++) {
			for(int c = 0; c < 8; c++) {
				squarex = (getWidth()-getHeight())/2 + c * squaresize;
				squarey = r * squaresize;
				if(r == selectedRow && c == selectedCol) {
					g2.setColor(new Color(200,200,0));
				}
				else if((r+c)%2==0) {
					g2.setColor(Color.WHITE);
				}
				else {
					g2.setColor(new Color(200,150,100));
				}
				ArrayList<Move> moves = getLegalMoves(board); 
				for(int i = 0; i < moves.size(); i++){
					if(moves.contains(new Move(selectedRow,selectedCol,r,c))){
						g2.setColor(new Color(150,100,50));
					}
				}
				g2.fillRect(squarex,squarey,squaresize,squaresize);

				switch(board.getArray()[r][c]) {
				case WHITE_KING:
					g2.setColor(Color.RED);
					g2.fillOval(squarex,squarey,squaresize,squaresize);
					g2.setColor(Color.YELLOW);
					g2.fillRect(squarex + squaresize/4, squarey + squaresize/4,squaresize/2,squaresize/2);
					break;
				case WHITE:
					g2.setColor(Color.RED);
					g2.fillOval(squarex,squarey,squaresize,squaresize);
					break;
				case BLACK_KING:
					g2.setColor(Color.BLACK);
					g2.fillOval(squarex,squarey,squaresize,squaresize);
					g2.setColor(Color.YELLOW);
					g2.fillRect(squarex + squaresize/4, squarey + squaresize/4,squaresize/2,squaresize/2);
					break;
				case BLACK:
					g2.setColor(Color.BLACK);
					g2.fillOval(squarex,squarey,squaresize,squaresize);
					break;
				case EMPTY: break;
				}
			}
		}
	}

	//Sets up the board to starting state. 
	public void setUp(){
		board.setTurn(WHITE);
		for(int row = 0; row < 3; row++){
			for(int col = 0; col < 8; col++){
				if((row + col)%2 == 1){
					board.setInd(row,col,BLACK);
				} 
			}
		}
		for(int row = 7; row > 4; row--){
			for(int col = 0; col < 8; col++){
				if((row + col) %2 == 1){
					board.setInd(row,col,WHITE);
				} 
			}
		}
		for(int row = 3; row <= 4; row++){
			for(int col = 0; col < 8 ; col++){
				board.setInd(row,col,EMPTY);
			}
		}
		history.clear();
		history.add(board.clone());
	}


	//Gets game state value
	public int getState(){
		return gameState;
	}

	//Checks state and calculates
	public void checkState(Data board){

		ArrayList<Move> moves = getLegalMoves(board);
		if(moves.size() == 0) {
			gameState = 4-board.getTurn();
			switch(gameState){
			case WHITE:
				message.setText("Red Wins!");
				break;
			case BLACK:
				message.setText("Black Wins!");
				break;
			}
		}else {
			gameState = 0;
		}
	}

	/*
	 * @param Gets move to check
	 * Checks if move is legal. If it is then make it
	 */
	public void moveTester(Move move){

		ArrayList<Move> moves = getLegalMoves(board);

		if(moves.contains(move)){
			doMove(move);
		}
		else{}
	}


	//Makes move onto board regardless of if it is legal
	public void doMove(Move move) {
		System.out.println("hi");
		repaint();
		board.setInd(move.getrn(),move.getcn(),board.getArray()[move.getr()][move.getc()]);
		board.setInd(move.getr(),move.getc(),EMPTY);

		//Checks if becomes a king
		if(move.getrn()==7 && board.getArray()[move.getrn()][move.getcn()] == BLACK) {
			board.setInd(move.getrn(),move.getcn(),BLACK_KING);
		}
		if(move.getrn()==0 && board.getArray()[move.getrn()][move.getcn()] == WHITE) {
			board.setInd(move.getrn(),move.getcn(),WHITE_KING);
		}

		//Checks if it was a capture
		if(move.isJump()){
			board.setInd((move.getr()+move.getrn())/2,(move.getc()+move.getcn())/2,EMPTY);

			//Checks for Double Jump
			board.setDouble(getLegalCapturesFrom(board,move.getrn(),move.getcn()));
			if(board.getDouble().size()!=0){
				board.setTurn(4-board.getTurn()); 
				message.setText("Take Another Jump");
			}
			else{board.setDouble(new ArrayList<Move>());}
		}

		//Switch turn and check if game over
		board.setTurn(4-board.getTurn());
		message.setText(((board.getTurn()==WHITE)?"Red":"Black") + " to move!");
		checkState(board);
		history.add(board.clone());
		if(board.getTurn() == BLACK) {
			repaint();
			Computer computer = new Computer();
		    System.out.println(computer.minimax(0, board.clone(), false, 12, Integer.MIN_VALUE, Integer.MAX_VALUE));
		    doMove(computer.getBestMove());
		}
		/*else if(board.getTurn() == WHITE) {
			Computer computer = new Computer();
		    System.out.println(computer.minimax(0, board.clone(), true, 9));
		    doMove(computer.getBestMove());
		}*/
	}
  public void doMovee(Move move, Data board) {
		board.setInd(move.getrn(),move.getcn(),board.getArray()[move.getr()][move.getc()]);
		board.setInd(move.getr(),move.getc(),EMPTY);

		//Checks if becomes a king
		if(move.getrn()==7 && board.getArray()[move.getrn()][move.getcn()] == BLACK) {
			board.setInd(move.getrn(),move.getcn(),BLACK_KING);
		}
		if(move.getrn()==0 && board.getArray()[move.getrn()][move.getcn()] == WHITE) {
			board.setInd(move.getrn(),move.getcn(),WHITE_KING);
		}

		//Checks if it was a capture
		if(move.isJump()){
			board.setInd((move.getr()+move.getrn())/2,(move.getc()+move.getcn())/2,EMPTY);

			//Checks for Double Jump
			board.setDouble(getLegalCapturesFrom(board,move.getrn(),move.getcn()));
			if(board.getDouble().size()!=0){
				board.setTurn(4-board.getTurn()); 
			}
			else{board.setDouble(new ArrayList<Move>());}
		}

		//Switch turn and check if game over
		board.setTurn(4-board.getTurn());
		//message.setText(((board.getTurn()==WHITE)?"Red":"Black") + " to move!");
		//checkState(board);
		//history.add(board.clone());
	}
  

	//Undos a certain move: Not done
  
	public void undoMove() {
		if(history.size() > 1) {
			board.setArray(Data.copy(history.get(history.size() - 2).getArray()));
			board.setTurn(history.get(history.size()-2).getTurn());
			board.setDouble(history.get(history.size()-2).getDouble());
			history.remove(history.size()-1);
		}
    
	}

	//Debug Method; prints board
	public void printBoard(int[][] board) {
		for(int i = 0; i<8; i++) {for(int j = 0; j < 8; j ++) {System.out.print(board[i][j] + " ");}System.out.println();}
    System.out.println();
	}

	public ArrayList<Move> getLegalMoves(Data board){
		ArrayList<Move> moves;
		if(board.getDouble().size()!=0) {
			return board.getDouble();
		}
		moves = availCaptures(board);
		if(moves.size()==0){
			moves = availMoves(board);
		} 
		return moves;
	}

	/*
	  @param x is the x position of checker being checked.
	  @param y is the y position of checker being checked.
	  Function returns list of available moves that are captures for a checker.
	 */
	public ArrayList<Move> getLegalCapturesFrom(Data board, int r, int c){
		ArrayList<Move> legalCaptures = new ArrayList<Move>();

		if(board.getArray()[r][c] == board.getTurn() || board.getArray()[r][c] == board.getTurn()+1){
			int checker = 0;
			try{
				checker = board.getArray()[r][c];
			}catch(ArrayIndexOutOfBoundsException e){}

			//Checks for a checker that is the right color
			if(!(board.getTurn()==checker || board.getTurn()+1 == checker)){return legalCaptures;}
			int oppColor = 4-board.getTurn();

			//Checks different squares for the different colors
			switch(checker){
			case WHITE_KING:
			case BLACK_KING:
			case WHITE:
				for(int i=2; i>=-2; i=i-4){
					try{
						if(board.getArray()[r-2][c+i] == EMPTY && (board.getArray()[r-1][c+i/2] == oppColor || board.getArray()[r-1][c+i/2] == oppColor+1)){
							legalCaptures.add(new Move(r,c,r-2,c+i));                        
						}
					}catch(ArrayIndexOutOfBoundsException e){}
				}
				if(board.getArray()[r][c]==WHITE){break;}
			case BLACK:
				for(int i=2; i>=-2; i=i-4){
					try{
						if(board.getArray()[r+2][c+i] == EMPTY && (board.getArray()[r+1][c+i/2] == oppColor || board.getArray()[r+1][c+i/2] == oppColor+1)){
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
	
	
	public ArrayList<Move> availCaptures(Data board){
		ArrayList<Move> availCaptures = new ArrayList<Move>();
		int checker = 0,c,r;

		//Loops though every square on the board
		for(int j = 0; j<64; j++){
			c = j / 8;
			r = j % 8;
			availCaptures.addAll(getLegalCapturesFrom(board,r,c));
		}
		return availCaptures;
	}

	/*
  @param x is the x position of checker being checked.
  @param y is the y position of checker being checked.
  Function returns list of available moves that are not captures for a checker.
	 */
	public ArrayList<Move> availMoves(Data board){
		ArrayList<Move> aMoves = new ArrayList<Move>();
		int checker = 0, c, r;
		for(int j=0;j<64;j++){
			c = j % 8;
			r = j / 8;

			try{
				checker = board.getArray()[r][c];
			}catch(ArrayIndexOutOfBoundsException e){}
			if(!(board.getTurn()==checker || board.getTurn()+1 == checker)){continue;}
			int oppColor = 4-board.getTurn();
			switch(checker){
			case WHITE_KING:
			case BLACK_KING:
			case WHITE:
				for(int i=1; i>=-1; i=i-2){
					try{
						if(board.getArray()[r-1][c+i]==EMPTY){
							aMoves.add(new Move(r,c,r-1,c+i));                        
						}
					}catch(ArrayIndexOutOfBoundsException e){}
				}
				if(board.getArray()[r][c]==WHITE){break;}
			case BLACK:
				for(int i=1; i>=-1; i=i-2){
					try{
						if(board.getArray()[r+1][c+i]==EMPTY){
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

	private void doClickSquare(int r, int c) {
		if(board.getArray()[r][c] == board.getTurn() || board.getArray()[r][c] == board.getTurn() + 1) {
			selectedRow = r;
			selectedCol = c;      
		}
		else if(board.getArray()[r][c] == EMPTY){
			moveTester(new Move(selectedRow,selectedCol,r,c));
			selectedRow = -1;
			selectedCol = -1;

		}
		repaint();
	}

	//Mouse pressed events
	public void mousePressed(MouseEvent evt) {

		int col = (evt.getX()-(getWidth()-getHeight())/2) / (getHeight()/8);
		int row = evt.getY() / (getHeight()/8);
		if (col >= 0 && col < 8 && row >= 0 && row < 8) {
			doClickSquare(row,col);
		}

	}
	public void mouseReleased(MouseEvent evt) { }
	public void mouseClicked(MouseEvent evt) {
    
  }
	public void mouseEntered(MouseEvent evt) { }
	public void mouseExited(MouseEvent evt) { }
} 