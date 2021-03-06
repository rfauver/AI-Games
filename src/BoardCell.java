
public class BoardCell 
{
	public IntVector2 coords;
	public GamePiece piece;

	private Board board;
	private BoardCellEdge[] edges;

	public BoardCell(IntVector2 coordinates, Board b)
	{
		coords = coordinates;
		board = b;
		piece = null;
		edges = new BoardCellEdge[Direction.values().length];
	}
	
	public void createEdges(String walls)
	{
		boolean[] wall = new boolean[4];

		int index = 0;
		while (walls.charAt(index) != ';')
		{
			switch (walls.charAt(index))
			{
			case 'N':
				wall[0] = true;
				break;
			case 'E':
				wall[1] = true;
				break;
			case 'S':
				wall[2] = true;
				break;
			case 'W':
				wall[3] = true;
				break;
			default:
				break;
			}
			index++;
		}

		for (int i = 0; i < edges.length; i++)
		{
			if (wall[i]) //is wall
			{
				edges[i] = new BoardWall(this, adjacent(Direction.toDirection(i)), Direction.toDirection(i));
			}
			else //is passage
			{
				edges[i] = new BoardPassage(this, adjacent(Direction.toDirection(i)), Direction.toDirection(i));
			}
		}	
	}
	
	public BoardCell adjacent(Direction dir)
	{
		switch (dir)
		{
		case NORTH:
			return board.getCell(new IntVector2(coords.x - 1, coords.z));
		case EAST:
			return board.getCell(new IntVector2(coords.x, coords.z + 1));
		case SOUTH: 
			return board.getCell(new IntVector2(coords.x + 1, coords.z));
		case WEST:
			return board.getCell(new IntVector2(coords.x, coords.z - 1));
		default:
			return null;
		} 
	}
	
	public BoardCellEdge[] getEdges()
	{
		return edges;
	}
	
	public BoardCellEdge getEdge(Direction direction)
	{
		return edges[direction.toInt()];
	}
	
	public boolean isGoal(int playerNumber)
	{
		GamePiece[] opponentPieces = board.getPlayerPieces((playerNumber%2)+1);
		for (int i = 0; i < opponentPieces.length; i++)
		{
			if (this == opponentPieces[i].getStartingCell())
				return true;
		}
		return false;
	}
}
