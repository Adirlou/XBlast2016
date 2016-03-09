package ch.epfl.xblast.server;

import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.Player.LifeState.State;

/**
 * A player
 * 
 * @author Guillaume Michel
 * @author Adrien Vandenbroucque
 *
 */
public final class Player {
    private final PlayerID id;
    private final Sq<LifeState> lifeStates;
    private final Sq<DirectedPosition> directedPos;
    private final int maxBombs;
    private final int bombRange;
    
    public Player(PlayerID id, Sq<LifeState> lifeStates, Sq<DirectedPosition> directedPos, int maxBombs, int bombRange) throws IllegalArgumentException, NullPointerException{
        this.id=Objects.requireNonNull(id);
        this.lifeStates=Objects.requireNonNull(lifeStates);
        this.directedPos=Objects.requireNonNull(directedPos);
        this.maxBombs=ArgumentChecker.requireNonNegative(maxBombs);
        this.bombRange=ArgumentChecker.requireNonNegative(bombRange);
    }
    
    public Player(PlayerID id, int lives, Cell position, int maxBombs, int bombRange){
        
    }
    
    public PlayerID id(){
        return id;
    }
    
    public Sq<LifeState> lifeStates(){
        return lifeStates;
    }
    
    public LifeState lifeState(){
        return lifeStates.head();
    }
    
    public Sq<LifeState> statesForNextLife(){
       return Sq.repeat(Ticks.PLAYER_DYING_TICKS, new LifeState(lives(), State.DYING)).concat(createLifeStateSequence(lives()-1));
    }
    
    private static Sq<LifeState> createLifeStateSequence(int lives){
        if(lives==0){
            return Sq.constant(new LifeState(0, State.DEAD));
        }else{
            return (Sq.repeat(Ticks.PLAYER_INVULNERABLE_TICKS, new LifeState(lives, State.INVULNERABLE))).concat(Sq.constant(new LifeState(lives, State.VULNERABLE)));
        }
    }
    
    public int lives(){
        return lifeState().lives();
    }
    
    public boolean isAlive(){
        return lives()>0;
    }
    
    public Sq<DirectedPosition> directedPositions(){
        return directedPos;
    }
    
    public SubCell position(){
        return directedPositions().head().position();
    }
    
    public Direction direction(){
        return directedPositions().head().direction();
    }
    
    public int maxBombs(){
        return maxBombs;
    }
    
    public Player withMaxBombs(int newMaxBombs){
        return new Player(id, lifeStates, directedPos, newMaxBombs, bombRange);
    }
    
    public int bombRange(){
        return bombRange;
    }
    
    public Player witBombRange(int newBombRange){
        return new Player(id, lifeStates, directedPos, maxBombs, newBombRange);
    }
    
    public Bomb newBomb(){
        return new Bomb(id, position().containingCell(), Ticks.BOMB_FUSE_TICKS, bombRange);
    }

    /**
     * A life state (represents the couple (number of lives, state) of the player)
     */
    public final static class LifeState{
        private final int lives;
        private final State state;
        
        /**
         * Constructs the couple (number of lives, state) with the given values
         * 
         * @param lives
         *      The number of lives of the player
         *      
         * @param state
         *      The state of the player
         *      
         * @throws IllegalArgumentException
         *      If the number of lives is strictly negative
         *      
         * @throws NullPointerException
         *      If the state is null
         */
        public LifeState(int lives, State state) throws IllegalArgumentException, NullPointerException{
            this.lives=ArgumentChecker.requireNonNegative(lives);
            this.state=Objects.requireNonNull(state);
        }
        
        /**
         * Returns the number of lives of this life state
         * 
         * @return
         *      The number of lives of this couple
         */
        public int lives(){
            return lives;
        }
        
        /**
         * Returns the state of this life state
         * 
         * @return
         *      The state of the couple
         */
        public State state(){
            return state;
        }
        
        /**
         * Determines if the player can move, and returns the appropriate boolean
         * 
         * @return
         *      <b>True</b> if the player can move, <b>false</b> otherwise
         */
        public boolean canMove(){
            return (state==State.INVULNERABLE || state==State.VULNERABLE);
        }
        
        /**
         * A state
         */
        public enum State{
            INVULNERABLE, VULNERABLE, DYING, DEAD;
        }
    }
    
    /**
     * A directed position (represents the pair (position, direction))
     */
    public final static class DirectedPosition{
        private final SubCell position;
        private final Direction direction;
        
        /**
         * Constructs a directed position with the given position and the given direction
         * 
         * @param position
         *      The position
         *      
         * @param direction
         *      The direction
         *      
         * @throws NullPointerException
         *      If one argument or the other is null
         */
        public DirectedPosition(SubCell position, Direction direction) throws NullPointerException{
            this.position=Objects.requireNonNull(position);
            this.direction=Objects.requireNonNull(direction);
        }
        
        /**
         * Returns the position of this directed position
         * 
         * @return
         *      The position
         */
        public SubCell position(){
            return position;
        }
        
        /**
         * Returns a directed position with the given position (and conserves its direction)
         * 
         * @param newPosition
         *      The new position
         *      
         * @return
         *      The directed position with the given position (and conserves the same direction)
         */
        public DirectedPosition withPosition(SubCell newPosition){
            return new DirectedPosition(newPosition, direction);
        }
        
        /**
         * Returns the direction of this directed position
         * 
         * @return
         *      The direction of this directed position
         */
        public Direction direction(){
            return direction;
        }
        
        /**
         * Returns a directed position with the given direction (and conserves its position)
         * 
         * @param newDirection
         *      The new direction
         *      
         * @return
         *      The directed position with the given direction (and conserves the same position)
         */
        public DirectedPosition withDirection(Direction newDirection){
            return new DirectedPosition(position, newDirection);
        }
        
        /**
         * Returns an infinite sequence composed only by the given directed position (represents a player stopped in this position)
         * 
         * @param p
         *      The directed position the player is stopped at
         *      
         * @return
         *      The infinite sequence composed only by the given directed position
         * 
         */
        public static Sq<DirectedPosition> stopped(DirectedPosition p){
            return Sq.constant(p);
        }
        
        /**
         * Returns an infinite sequence of directed positions (to represent a player moving in a direction he looks at)
         * 
         * @param p
         *      The current directed position of the player
         *      
         * @return
         *      The infinite sequence of directed positions representing the moving player
         */
        public static Sq<DirectedPosition> moving(DirectedPosition p){
            return Sq.iterate(p, d -> d.withPosition(d.position.neighbor(d.direction)));
        }
    }
}
