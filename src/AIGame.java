import java.util.*;

public class AIGame implements Game 
{
	public boolean gameOver;
	public int winner; //0 for no winner, 1 for player 1, 2 for player 2

	private Board board;
	private Stack<Move> moveStack;

	public AIGame()
	{
		gameOver = false;
		winner = 0;
		moveStack = new Stack<Move>();
		board = new Board(new IntVector2(11, 11), 6);
	}

	public boolean isGameOver() 
	{
		GamePiece[] player1Pieces = board.getPlayerPieces(1);
		GamePiece[] player2Pieces = board.getPlayerPieces(2);

		for (int i = 0; i < player1Pieces.length; i++)
		{
			if (player1Pieces[i].coordinates != player2Pieces[0].getStartingCell().coords
			 && player1Pieces[i].coordinates != player2Pieces[1].getStartingCell().coords
			 && player1Pieces[i].coordinates != player2Pieces[2].getStartingCell().coords)
			{
				break;
			}
			if (i == player1Pieces.length-1)
			{
				winner = 1;
				gameOver = true;
				return true;
			}
		}
		for (int i = 0; i < player2Pieces.length; i++)
		{
			if (player2Pieces[i].coordinates != player1Pieces[0].getStartingCell().coords
			 && player2Pieces[i].coordinates != player1Pieces[1].getStartingCell().coords
			 && player2Pieces[i].coordinates != player1Pieces[2].getStartingCell().coords)
			{
				return false;
			}
		}
		winner = 2;
		gameOver = true;
		return true;
	}

	public double gameValue(int player) 
	{
		if (!gameOver || winner == 0) 
		{
			GamePiece[] pieces = board.getPlayerPieces(player);
			int value = 0;
			for (int i = 0; i < pieces.length; i++)
			{	
				int distanceToClosestGoal = BFS(pieces[i]);
				if (distanceToClosestGoal == 0)
				{
					value += 1.0/(double)pieces.length;
				}
				value += (1.0/(double)distanceToClosestGoal)/(double)pieces.length;
			}
				
			return value;
		}
		if (winner == player) return 1.0;
		else return -1.0;
	}

	public Move[] getPossibleMoves(int player) 
	{
		GamePiece[] pieces = board.getPlayerPieces(player);
		ArrayList<Move> moves = new ArrayList<Move>();

		for (int i = 0; i < pieces.length; i++)
		{
			BoardCellEdge[] edges = pieces[i].getCurrentCell().getEdges();
			for (int j = 0; j < edges.length; j++)
			{
				BoardCell currentCell = edges[j].cell;
				if (edges[j] instanceof BoardWall || edges[j].adjCell.piece != null) {}
				else
				{
					while (currentCell.getEdges()[j] instanceof BoardPassage && currentCell.getEdges()[j].adjCell.piece == null)
					{
						currentCell = currentCell.getEdges()[j].adjCell;
					}
					moves.add(new Move(currentCell.coords, pieces[i]));
				}
			}
		}
		return moves.toArray(new Move[moves.size()]);
	}
	
	private BoardCell[] getPossibleMovesFromCell(BoardCell cell)
	{
		BoardCellEdge[] edges = cell.getEdges();
		ArrayList<BoardCell> destinationCells = new ArrayList<BoardCell>();
		
		for (int j = 0; j < edges.length; j++)
		{
			BoardCell currentCell = edges[j].cell;
			if (edges[j] instanceof BoardWall || edges[j].adjCell.piece != null) {}
			else
			{
				while (currentCell.getEdges()[j] instanceof BoardPassage && currentCell.getEdges()[j].adjCell.piece == null)
				{
					currentCell = currentCell.getEdges()[j].adjCell;
				}
				destinationCells.add(currentCell);
			}
		}
		return destinationCells.toArray(new BoardCell[destinationCells.size()]);
	}

	public void makeMove(Move move, int player) 
	{
		board.getCell(move.piece.getCurrentCell().coords).piece = null;
		board.getCell(move.destination).piece = move.piece;

		move.piece.setCurrentCell(board.getCell(move.destination));

		moveStack.push(move);
	}

	public void undoMove() 
	{
		if (moveStack.isEmpty()) { return; }
		if (gameOver) gameOver = false;
		winner = 0;
		Move move = moveStack.pop();

		move.piece.getCurrentCell().piece = null;
		if (moveStack.empty())
		{
			move.piece.getStartingCell().piece = move.piece;
			move.piece.setCurrentCell(move.piece.getStartingCell());
		}
		else 
		{
			board.getCell(moveStack.peek().destination).piece = move.piece;
			move.piece.setCurrentCell(board.getCell(moveStack.peek().destination));
		}
	}
	
	public Board getBoard()
	{
		return board;
	}
	
	private int BFS(GamePiece piece)
	{
		BoardCell currentCell = board.getCell(piece.coordinates);
		GamePiece[] opponentPieces = board.getPlayerPieces((piece.playerNumber%2)+1);
		for (int index = 0; index < opponentPieces.length; index++)
		{
			if (currentCell == opponentPieces[index].getStartingCell())
				return 0;
		}
		
		HashMap hashmap = new HashMap();
		Queue<BoardCell> q = new LinkedList<BoardCell>();
		
		q.add(currentCell);
		hashmap.put(currentCell, null);
		int time = 0;
		
		while (!q.isEmpty())
		{
			time++;
			currentCell = q.poll();
			BoardCell[] movableCells = getPossibleMovesFromCell(currentCell);
			for (int i = 0; i < movableCells.length; i++)
			{
				if (!hashmap.containsKey(movableCells[i]))
				{
					
					for (int j = 0; j < opponentPieces.length; j++)
					{
						if (movableCells[i] == opponentPieces[j].getStartingCell())
							return time;
					}
					q.add(movableCells[i]);
					hashmap.put(movableCells[i], null);
				}
			}
		}
		return Integer.MAX_VALUE;
	}
}
