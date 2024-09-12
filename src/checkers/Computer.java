package checkers;
import java.util.ArrayList;
public class Computer{
	private Move bestMove;
	public Computer(){

	}
	private Board hello = new Board();
	
	public Move getBestMove(){
		return bestMove;
	}
	
	public int minimax(int depth, Data board, boolean  isMax, int h, int alpha, int beta)
	{
		// Terminating condition. i.e leaf node is reached
		if (depth == h)
			return getEval(board);

		// If current move is maximizer, find the maximum attainable
		// value
		Data x = board.clone();
		int ans;
		Move bestMove = null;
		if (isMax) {
			//return the maximum of all elgal moves
			ArrayList<Move> moves = hello.getLegalMoves(board);
			ans = Integer.MIN_VALUE;
			for(Move m: moves) {
				hello.doMovee(m, board);
				if(board.getDouble().size() != 0) isMax = !isMax;
				int temp = minimax(depth+1, board, !isMax, h, alpha, beta);
				board=x.clone();
				if(temp > ans){
					ans=temp;
					if(depth == 0) bestMove = m;
				} 
				if(ans > alpha) {
					alpha = ans;
				}
				if(beta <= alpha) break;				
			}
		}

		// Else (If current move is Minimizer), find the minimum
		// attainable value
		else{
			ArrayList<Move> moves = hello.getLegalMoves(board);
			ans = Integer.MAX_VALUE;
			for(Move m: moves) {
				hello.doMovee(m, board);
				if(board.getDouble().size() != 0) isMax = !isMax;
				int temp = minimax(depth+1, board, !isMax, h, alpha, beta);
				board=x.clone();
				if(temp < ans){
					ans=temp;
					if(depth == 0) bestMove = m;
				} 
				if(ans < beta) {
					beta = ans;
				}
				if(beta <= alpha) break;
			}
		}
		if(depth == 0) this.bestMove = bestMove;
		return ans;
	}


	public int getEval(Data board){
		int[][] y = board.getArray();
		int turn = board.getTurn();
		int pieceTotals = 0;
		int closerToKing=0;
		double kingb=0;
		double kingw=0;
		double king=0;
		int x=0;

		for(int r = 0; r < y.length; r++){
			for(int c = 0; c < y[0].length; c++){

				switch(y[r][c]){
				case Board.EMPTY:
					x = x -1;
					break;

				case Board.BLACK:
					closerToKing -= r;
					pieceTotals -= 3;
					break;

				case Board.WHITE:
					closerToKing += 7-r;
					pieceTotals += 3;
					break;

				case Board.WHITE_KING:

					kingb += Math.abs(r-3.5);
					pieceTotals += 5;
					break;

				case Board.BLACK_KING:
					kingw += Math.abs(r-3.5);
					pieceTotals -= 5;
					break;


				}
				x=x+1;
			}
		}
		king=kingw-kingb;
		if(pieceTotals<0){
			x=x*-1;
		}
		return pieceTotals*10000000 + closerToKing*100000 + x*1000 + (int)king*10 + (int)(Math.random()*10);


	}




}