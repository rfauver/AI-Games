
public class Main
{
	public static void main(String[] args) 
	{
		AIGame AIG = new AIGame();
		Player p1 = new AlphaBetaPlayer(1, 6);
		Player p2 = new AlphaBetaPlayer(2, 6);
		
//		GamePiece testPiece = AIG.getBoard().getPlayerPieces(1)[0];
//		System.out.println(testPiece.coordinates.x + "  " + testPiece.coordinates.z);
//		
//		BoardCell[] testGoal = new BoardCell[1];
//		testGoal[0] = AIG.getBoard().getCell(new IntVector2(8,9));
//		System.out.println(AIG.BFS(testPiece, testGoal)[0]);
		
		while(!AIG.gameOver)
		{
			p1.makeMove(AIG);
			AIG.gameOver = AIG.isGameOver();
			System.out.println(AIG.getBoard().toString());
			if (AIG.gameOver) break;
			p2.makeMove(AIG);
			AIG.gameOver = AIG.isGameOver();
			System.out.println(AIG.getBoard().toString());
		}
	}
}
