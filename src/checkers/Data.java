package checkers;

	import java.util.ArrayList;
	
	class Data{
		private int turn;
		private int[][] board;
		private ArrayList<Move> doubleJump;
	
		public Data(int[][] x, int e){
			board=x;
			turn=e;
			doubleJump = new ArrayList<Move>();
		}
	
		public Data(int[][] x, int e, ArrayList<Move> f){
			board=x;
			turn=e;
			doubleJump = f;
		}
	
		public int[][] getArray(){
			return board;
		}
		public int getTurn(){
			return turn;
		}
		public void setTurn(int x){
			turn = x;
		}
		public void setArray(int[][] x){
			board = x;
		}
		public void setInd(int r, int c, int piece){
			board[r][c]= piece;
		}
	
		public ArrayList<Move> getDouble() {
			return doubleJump;
		}
		public void setDouble(ArrayList<Move> dj) {
			doubleJump = dj;
		}
		public static int[][] copy(int[][] x){
			int[][] y = new int[x.length][x[0].length];
			for(int i = 0; i < x.length; i++){
				for(int e = 0; e < x[0].length; e++){
					y[i][e]=x[i][e];
				}
			}
			return y;
		}
		
		public Data clone(){
      
			return new Data(copy(board),turn,doubleJump);
		}
	}
