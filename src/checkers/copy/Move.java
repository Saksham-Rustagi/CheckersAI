package checkers.copy;

class Move{
	private int r;
	private int c;
	private int rn;
	private int cn;

	public Move(int r, int c, int rn, int cn){
		this.r = r;
		this.c = c;
		this.rn = rn;
		this.cn = cn;
	}
	public int getr(){
		return r;
	}
	public int getc(){
		return c;
	}
	public int getrn(){
		return rn;
	}
	public int getcn(){
		return cn;
	} 
	
	public boolean isJump() {
		return Math.abs(r-rn) == 2;
	}
	public String toString() {
		return "[" + r + ", " + c + ", " + rn + ", " + cn + "]";
	}
	
	//@Override
	public boolean equals(Object a) {
		boolean retVal = false;
        if (a instanceof Move){
            Move ptr =  (Move) a;
            retVal = ptr.r == this.r && ptr.c == this.c && ptr.rn == this.rn && ptr.cn == this.cn;
        }
        return retVal;
	}
}