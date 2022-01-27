import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class Board  {
	//iterator
	private LinkedList<Board> stack;
	private int n;//size of the tile
	private  int[][] tiles ;
	private  Board twin ;
	private  int[][] goal ;
	private int x_zero_pos;//position of 0
	private int y_zero_pos;
	//declaration of the neigboor of board 
	/*private Board move_right;
	private Board move_left;
	private Board move_up;
	private Board move_down;*/
	//
	private Board parent;
	//private int num_moves;
	private int manhattan;
	private int hamming ;
	//private int Manhattan_priority_func;
	// create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
    	n = tiles.length;
    	if(n < 2 || n >= 128)
    		throw new IllegalArgumentException("n must be in range 2 ≤ n < 128");
    	this.tiles = new int[n][n];  
    	/////////////initialisation of the goal state////////
    	goal = new int[n][n];
    	//twin  = new int[n][n];
    	int count =  1 ;
		for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
            	this.tiles[i][j] = tiles[i][j];
            	this.goal[i][j] = count;	
                count++;
                if(i== n-1 && j==n-1) goal[i][j]=0;
                if(tiles[i][j]  == 0) {
                	x_zero_pos = i;
                	y_zero_pos = j;
               }
            }           
        }
		//////////////////
		stack = new LinkedList<>();
		this.parent = null;
		//this.num_moves = 0;
		this.manhattan = this.manhattan(); 
		this.hamming = this.hamming();
		//this.Manhattan_priority_func = this.num_moves + this.manhattan ;
    }
                                           
    // string representation of this board
    public String toString() {
    	StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
    	return n;
    }

    // number of tiles out of place
    public int hamming() {
    	int ham = 0;
    	for(int i=0 ; i < n ; i++) {
    		for(int j=0 ; j< n ; j++) {
    			if(i == n-1 && j == n-1)  break;
    				if(goal[i][j] != tiles[i][j]) ham++;
    		}
    	}
    	return ham;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
    	int sum = 0;
    	for(int i = 0 ; i < n ;i++) {
    		for(int j=0 ; j < n ; j++) {
    			int index = tiles[i][j];
    			if(index !=0) {
    				int i1 = (index-1)/n;
        			int j1 = (index-1)%n;
        			int ss = Math.abs(i1 -i)+Math.abs(j1-j);
        			sum +=ss;
    			}    			    			
    		}
    	}
    	return sum;
    }

    // is this board the goal board?
    public boolean isGoal() {
    	int[][] e1 = this.tiles;    
    	return Arrays.deepEquals(e1,this.goal);
    }

    // does this board equal y?
    public boolean equals(Object y) {	   	
    	if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        int[][] e1 = this.tiles;
        int[][] e2 = that.tiles; 
    	return Arrays.deepEquals(e1,e2);
    }

    // all neighboring boards
    public Iterable<Board> neighbors(){
    	//x_zero_pos;
    	//y_zero_pos;
    	//move the white space to the right
    	int[][] t1 = new int[n][n];    		   				
		for(int a=0; a <n;a++) {
    		for(int b = 0;b<n ; b++) {
    			t1[a][b] = this.tiles[a][b];
    			//updating zero index
    			 if(this.tiles[a][b]  == 0) {
                 	this.x_zero_pos = a;
                 	this.y_zero_pos = b;
                }
    		}
    	}
    	if(y_zero_pos + 1 < n)
    	{    		
    		
    		//this.move_right = new Board(t1); 
    		
    		int tmp  = t1[x_zero_pos][y_zero_pos+1];
    		t1[x_zero_pos][y_zero_pos+1]  = t1[x_zero_pos][y_zero_pos];
    		t1[x_zero_pos][y_zero_pos] = tmp ;
    		//this.move_right.tiles = t1;
    		//this.move_right.parent = this;
    		//this.move_right.num_moves = this.num_moves+1;
    		//this.move_right.Manhattan_priority_func  =  this.move_right.num_moves+this.move_right.manhattan();    		
    			stack.add(new Board(t1));    		
    	}
    	//move the white space to the left
		int[][] t2 =  new int[n][n]; 
		for(int a=0; a <n;a++) {
    		for(int b = 0;b<n ; b++) {
    			t2[a][b] = this.tiles[a][b];
    			//updating zero index
	   			 if(this.tiles[a][b]  == 0) {
	                	this.x_zero_pos = a;
	                	this.y_zero_pos = b;
	               }
    		}
    	}
    	if(y_zero_pos - 1 >= 0)
    	{    		
    		//this.move_left = new Board(t2);
    		int tmp  = t2[x_zero_pos][y_zero_pos - 1];
    		t2[x_zero_pos][y_zero_pos - 1]  = t2[x_zero_pos][y_zero_pos];
    		t2[x_zero_pos][y_zero_pos] = tmp ;
    		//this.move_left.tiles = t2;
    		//this.move_left.parent = this;
    		//this.move_left.num_moves = this.num_moves+1;
    		//this.move_left.Manhattan_priority_func  =  this.move_left.num_moves+this.move_left.manhattan();
    			stack.add(new Board(t2));    		
    	}
    	//move the white space up
		int[][] t3 = new int[n][n];
		for(int a=0; a <n;a++) {
    		for(int b = 0;b<n ; b++) {
    			t3[a][b] = this.tiles[a][b];
    			//updating zero index
	   			 if(this.tiles[a][b]  == 0) {
	                	this.x_zero_pos = a;
	                	this.y_zero_pos = b;
	               }
    		}
    	}
    	if(x_zero_pos - 1 >= 0)
    	{    		
    		//this.move_up = new Board(t3);
    		int tmp  = t3[x_zero_pos-1][y_zero_pos];
    		t3[x_zero_pos-1][y_zero_pos]  = t3[x_zero_pos][y_zero_pos];
    		t3[x_zero_pos][y_zero_pos] = tmp ;
    		//this.move_up.tiles = t3;
    		//this.move_up.parent = this;
    		//this.move_up.num_moves = this.num_moves+1;
    		//this.move_up.Manhattan_priority_func  =  this.move_up.num_moves+this.move_up.manhattan();
    			stack.add(new Board(t3));    		
    	}
    	//move the white space down
		int[][] t4 = new int[n][n];
		for(int a=0; a <n;a++) {
    		for(int b = 0;b<n ; b++) {
    			t4[a][b] = this.tiles[a][b];
    			//updating zero index
	   			 if(this.tiles[a][b]  == 0) {
	                	this.x_zero_pos = a;
	                	this.y_zero_pos = b;
	             }
    		}
    	}
    	if(x_zero_pos + 1 < n)
    	{    		
    		//this.move_down = new Board(t4);
    		int tmp  = t4[x_zero_pos+1][y_zero_pos];
    		t4[x_zero_pos + 1][y_zero_pos]  = t4[x_zero_pos][y_zero_pos];
    		t4[x_zero_pos][y_zero_pos] = tmp ;
    		//this.move_down.tiles = t4;
    		//this.move_down.parent = this;
    		//this.move_down.num_moves = this.num_moves+1;
    		//this.move_down.Manhattan_priority_func  =  this.move_down.num_moves+this.move_down.manhattan();
    		stack.add(new Board(t4));    		
    	}
		return stack;
    	
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
    	if(this.twin != null) return this.twin;
    	    	
    	int[][] e1 = new int[n][n];
    	for(int a=0; a <n;a++) {
    		for(int b = 0;b<n ; b++) {
    			e1[a][b] = this.tiles[a][b];
    		}
    	}
    	
		int tmp1 = 0;
		int i = 0;
		if(i == this.x_zero_pos) i++;
		tmp1 = e1[i][0];
		e1[i][0] = e1[i][1];
		e1[i][1] = tmp1;
    	
    	this.twin = new Board(e1);    	
    	this.twin.parent = this.parent;
    	//this.twin.num_moves=this.num_moves;
    	//this.twin.Manhattan_priority_func= this.twin.num_moves +this.twin.manhattan();
		return this.twin; 
		
    }

	public static void main(String[] args) {
		int n =3;
		int[][] goal = new int[n][n];
		int[][] tiles = {
				{8,1,3},{4,0,2},{7,6,5}
		};
		Board board = new Board(tiles);
		
		int count =  1 ;
		for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
            	goal[i][j] = count;	
                count++;
                if(i== n-1 && j==n-1) goal[i][j]=0;
            }                       
        }

		System.out.println("the goal state");
		for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
            	System.out.print(goal[i][j]+"\t"); 
            }                       
            System.out.println();
        }
		System.out.println("the initial state");
		for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
            	System.out.print(tiles[i][j]+"\t"); 
            }                       
            System.out.println();
        }
		
		System.out.println("the Hamming distance = "+ board.hamming());
		System.out.println("the Manhattan distance = "+ board.manhattan());
		System.out.println("is the goal = "+ board.isGoal());
		System.out.println("is equal = "+ board.equals(goal));
		Board tw = board.twin();
		int[][] e1 =tw.tiles;
		System.out.println("testing twin methode");
		for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
            	System.out.print(e1[i][j]+"\t"); 
            }                       
            System.out.println();
        }
		System.out.println();
		System.out.println("*****testing nighbors****");
		System.out.println("diplay the original board");
		for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
            	System.out.print(board.tiles[i][j]+"\t"); 
            }                       
            System.out.println();
        }
		System.out.println("0 index i = "+board.x_zero_pos + " 0 index j = " + board.y_zero_pos);
		System.out.println("display all neighbors");
		Iterable<Board> pointer = board.neighbors();
		for (Board board2 : pointer) {
			if(board2 != null) {
				int[][] diplay = board2.tiles;
				for (int i = 0; i < n; i++) {
		            for (int j = 0; j < n; j++) {
		            	System.out.print(diplay[i][j]+"\t"); 
		            }                       
		            System.out.println();
		        }
			}
			System.out.println("+++++++++++++++++++++++++++++++");
		}
	}

}
