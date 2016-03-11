package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Lists
 * 
 * @author Guillaume Michel (258066)
 * @author Adrien Vandenbroucque (258715)
 *
 */
public final class Lists {
    
    private Lists(){}
    
    /**
     * Returns a symmetric version of a given list
     * 
     * @param l
     *      The list from which we compute the symmetric version
     *      
     * @return
     *      The symmetric version of the given list
     * 
     * @throws IllegalArgumentException
     *      If the given list is empty
     */
    public static <T> List<T> mirrored(List<T> l) throws IllegalArgumentException{
        if(l.isEmpty()){
            throw new IllegalArgumentException();
        }else{
            List<T> subList=new ArrayList<T>(l.subList(0, l.size()-1));
            Collections.reverse(subList);
            l.addAll(subList);
            return l;
        }
    }
    
    public static <T> List<List<T>> permutations(List<T> l){
        List<List<T>> l1 = new ArrayList<List<T>>();
        if (l.isEmpty()) {
            l1.add(l);
            return l1;
        }
        return null;
    }
    
    public static <T> List<List<T>> recursivePermutations(List<T> l){
        ArrayList<T> copy = new ArrayList<T>(l);

        return null;
    }
}
