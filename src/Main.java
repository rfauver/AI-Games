
public class Main
{
	public static void main(String[] args) 
	{

		ReinforcementLearnerPlayer p1 = new ReinforcementLearnerPlayer(1);
		ReinforcementLearnerPlayer p2 = new ReinforcementLearnerPlayer(2);
		int moves = 0;
		int p1Wins = 0;
		int p2Wins = 0;
		int p1WinMoves = 0;
		int p2WinMoves = 0;
		
		for (int i = 0; i < 50; i++)
		{
			AIGame AIG = new AIGame();
			moves = 0;
			while(!AIG.gameOver)
			{
				p1.makeMove(AIG);
				moves++;
				AIG.gameOver = AIG.isGameOver();
//				System.out.println(AIG.getBoard().toString());
				if (AIG.gameOver) break;
				p2.makeMove(AIG);
				AIG.gameOver = AIG.isGameOver();
//				System.out.println(AIG.getBoard().toString());
			}
			if (AIG.winner == 1)
			{
				p1Wins++;
				p1WinMoves += moves;
			}
			else
			{
				p2Wins++;
				p2WinMoves += moves;
			}
//			System.out.println("Player " + AIG.winner + " won in " + moves + " moves");
		}
		p1.exportWeights();
		p2.exportWeights();
		System.out.println("Player 1 won " + p1Wins + " times with an average of " + ((double)p1WinMoves/(double)p1Wins) + " moves per win");
		System.out.println("Player 2 won " + p2Wins + " times with an average of " + ((double)p2WinMoves/(double)p2Wins) + " moves per win");
	}
}
