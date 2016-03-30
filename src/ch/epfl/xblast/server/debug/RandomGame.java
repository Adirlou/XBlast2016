package ch.epfl.xblast.server.debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;

public class RandomGame {

    public static void main(String[] args) throws InterruptedException{
        RandomEventGenerator randomShit=new RandomEventGenerator(2016, 30, 100);
        //java -classpath jar/sq.jar:bin ch.epfl.xblast.server.debug.RandomGame
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
        players.add(new Player(PlayerID.PLAYER_1,3, new Cell(1, 1), 5, 5));
        players.add(new Player(PlayerID.PLAYER_2, 3, new Cell(13, 1), 5, 5));
        players.add(new Player(PlayerID.PLAYER_3, 3, new Cell(13, 11), 5, 5));
        players.add(new Player(PlayerID.PLAYER_4, 3, new Cell(1, 11), 5, 5));
        
        Set<PlayerID> set=new HashSet<>();
        set.add(PlayerID.PLAYER_4);
        
        Map<PlayerID, Optional<Direction>> map=new HashMap<>();
        map.put(PlayerID.PLAYER_1, Optional.of(Direction.E));

        GameState g = new GameState(board, players);
        for(int i=0;i<100;++i){
            GameStatePrinter.printGameState(g);
            Thread.sleep(100);
            //System.out.print("\u001b[2J");
            g=g.next(randomShit.randomSpeedChangeEvents(), randomShit.randomBombDropEvents());
            System.out.println();
        }
        
    }

}
