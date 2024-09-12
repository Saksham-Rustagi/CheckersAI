package checkers.copy;

import java.util.Scanner;


class Main {
  public static void main(String[] args) {
    
	  
	  
	  
	Scanner scanner = new Scanner(System.in);
    Board board = new Board();
    board.setUp();
    while(board.getState()==0){
      board.print();
      board.getMove(scanner);     
    }
    if(board.getState()==board.WHITE){ System.out.println("White won"); }
    else{ System.out.println("Black won"); }
    

  }
}