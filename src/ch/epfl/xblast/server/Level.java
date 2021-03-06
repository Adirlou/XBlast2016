package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.painter.BlockImage;
import ch.epfl.xblast.server.painter.BoardPainter;

/**
 * A level
 * 
 * @author Guillaume Michel (258066)
 * @author Adrien Vandenbroucque (258715)
 *
 */
public final class Level {
    private final BoardPainter boardPainter;
    private final GameState gamestate;
    
    /**
     * Variable that contains a "default level"
     */
    public static final Level DEFAULT_LEVEL=defaultLevel();
    
    /**
     * Constructs a level with the given parameters
     * 
     * @param boardPainter
     *      The board painter
     *      
     * @param gamestate
     *      The initial game state
     *      
     * @throws NullPointerException
     *      If the board painter or the game state is "null"
     */
    public Level(BoardPainter boardPainter, GameState gamestate) throws NullPointerException{
        this.boardPainter=Objects.requireNonNull(boardPainter);
        this.gamestate=Objects.requireNonNull(gamestate);
    }
    
    /**
     * Returns the board painter of this level
     * 
     * @return
     *      The board painter of this level
     */
    public BoardPainter boardPainter(){
        return boardPainter;
    }
    
    /**
     * Returns the game state of this level
     * 
     * @return
     *      The game state of this level
     */
    public GameState gameState(){
        return gamestate;
    }
    
    private static Level defaultLevel(){
        Map<Block, BlockImage> defaultPalette=new HashMap<>();
        defaultPalette.put(Block.FREE, BlockImage.IRON_FLOOR);
        for(int i=1;i<Block.values().length;i++)
            defaultPalette.put(Block.values()[i], BlockImage.values()[i+1]);
        
        Block __ = Block.FREE;
        Block XX = Block.INDESTRUCTIBLE_WALL;
        Block xx = Block.DESTRUCTIBLE_WALL;
        Board board = Board.ofQuadrantNWBlocksWalled(
          Arrays.asList(
            Arrays.asList(__, __, __, __, __, xx, __),
            Arrays.asList(__, XX, xx, XX, xx, XX, xx),
            Arrays.asList(__, xx, __, __, __, xx, __),
            Arrays.asList(xx, XX, __, XX, XX, XX, XX),
            Arrays.asList(__, xx, __, xx, __, __, __),
            Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
        
        List<Player> players = new ArrayList<>();
        players.add(new Player(PlayerID.PLAYER_1,3, new Cell(1, 1), 2, 3));
        players.add(new Player(PlayerID.PLAYER_2, 3, new Cell(13, 1), 2, 3));
        players.add(new Player(PlayerID.PLAYER_3, 3, new Cell(13, 11), 2, 3));
        players.add(new Player(PlayerID.PLAYER_4, 3, new Cell(1, 11), 2, 3));
        
        return new Level(new BoardPainter(defaultPalette, BlockImage.IRON_FLOOR_S), new GameState(board, players));
    }
}
