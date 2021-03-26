package engines;

import java.util.Random;

import game.Board;
import game.Side;
import oldgui.Chess;

public class RandomEngine extends ChessEngine {


	private static final int BREAK_THRESHOLD = 16777216; // after this many moves, we think there is stalemate or checkmate
													// situation so stop the randomizer from making more moves

	public RandomEngine(Board board, Side side) {
		super(board, side);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void makeAMove() {
		 Random r = new Random();

		 int counter = 1;
		 
         while (!board.move(r.nextInt(8), r.nextInt(8), r.nextInt(8), r.nextInt(8))) {
        	 counter++;
         }
         
//         System.out.println("It took " + counter + " attempts for the computer to find a valid move.");

         
         

	}

}
