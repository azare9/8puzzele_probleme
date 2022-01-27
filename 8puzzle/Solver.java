import java.util.LinkedList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

	private SearchNode searchNode;
	private SearchNode twin ;
	private LinkedList<Board> stack;
	// find a solution to the initial board (using the A* algorithm)
	public Solver(Board initial) {
	
		
	if(initial == null)	throw new IllegalArgumentException();
    	// #move = previous.#move+1
    	this.searchNode = new SearchNode(initial,0, null);
    	this.twin =new SearchNode(searchNode.board.twin(),searchNode.num_moves,searchNode.previous);    	
    	
    	MinPQ<SearchNode> min1 = new MinPQ<SearchNode>();
    	MinPQ<SearchNode> min2 = new MinPQ<SearchNode>();
    	
    	min1.insert(this.searchNode);
    	SearchNode node1 = min1.delMin();
    	
    	min2.insert(this.twin);
    	SearchNode node2 = min2.delMin();
    	
    	if(node1.manhattan == 0 || node2.manhattan ==0 ) return;//if this is the goal node
    	
    	while(node1.manhattan != 0 && node2.manhattan !=0 ) {
    		
    		//add all neighbors of the search node to MinPQ    		
    		Iterable<Board> neighbors_Snode = node1.board.neighbors();
    		Iterable<Board> neighbors_Twinnode = node2.board.neighbors();
    		
    		for (Board board : neighbors_Snode) {
    			//before adding to the stack we check if we have duplicate , if this board equal his grand parent
    			if(node1.previous != null) {
        			Board grandpa = node1.previous.board;        			
        			if(!board.equals(grandpa)) {
        				SearchNode searchnode = new SearchNode(board,node1.num_moves+1,node1);
            			min1.insert(searchnode);
        			}
        		}else {
        			SearchNode searchnode = new SearchNode(board,node1.num_moves+1,node1);
        			min1.insert(searchnode);
        		}
    			    			
			}
    		
    		for (Board board : neighbors_Twinnode) {
    			//before adding to the stack we check if we have duplicate , if this board equal his grand parent
    			if(node2.previous != null) {
        			Board grandpa2 = node2.previous.board;        			
        			if(!board.equals(grandpa2)) {
        				SearchNode twinsearchnode = new SearchNode(board,node2.num_moves+1,node2);
            			min2.insert(twinsearchnode);
        			}
        		}else {
        			SearchNode twinsearchnode = new SearchNode(board,node2.num_moves+1,node2);
        			min2.insert(twinsearchnode);
        		}
    			
			}
    		//dequeuing from the MinPQ 
    		//we have node already dequeued from the MinPQ , and we use it as parent node to  any dequeued node  
    		node1 =  min1.delMin();
    		node2 =  min2.delMin();
    	}
    	this.searchNode = node1;
    	this.twin = node2;
    	this.stack = new LinkedList<>();    
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
    	if(this.twin.board.manhattan() == 0) return false;
    	return true;
    }

    // min number of moves to solve initial board
    public int moves() {  
    	if (!this.isSolvable()) return -1;  	
    	return this.searchNode.num_moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution(){
    	if(!this.isSolvable()) return null;
    	while(this.searchNode != null) {
    		this.stack.addFirst(this.searchNode.board);
    		this.searchNode = this.searchNode.previous; 
    	}
    	return this.stack;
    }
    private class SearchNode implements Comparable<SearchNode>{
    	
    	private SearchNode previous;
    	private Board board;
    	private int manhattan;
    	private int num_moves;
    	private int Manhattan_priority_func;
    	
		public SearchNode(Board board,int num_move,SearchNode prev) {
			this.board = board;
			this.previous = prev;
			this.manhattan = this.board.manhattan();
			this.num_moves= num_move;
			this.Manhattan_priority_func = this.num_moves + this.manhattan ; 
		}


		@Override
		public int compareTo(SearchNode o) {
			if(this.Manhattan_priority_func < o.Manhattan_priority_func) return -1;
			else if(this.Manhattan_priority_func > o.Manhattan_priority_func) return 1;
			else return 0;
		}
    	
    }


	public static void main(String[] args) {		
		// create initial board from file
	    In in = new In(args[0]);
	    int n = in.readInt();
	    int[][] tiles = new int[n][n];
	    for (int i = 0; i < n; i++)
	        for (int j = 0; j < n; j++)
	            tiles[i][j] = in.readInt();
	    Board initial = new Board(tiles);

	    // solve the puzzle
	    Solver solver = new Solver(initial);

	    // print solution to standard output
	    if (!solver.isSolvable())
	        StdOut.println("No solution possible");
	    else {
	        StdOut.println("Minimum number of moves = " + solver.moves());
	        for (Board board : solver.solution())
	            StdOut.println(board);
	    }
	}

}
