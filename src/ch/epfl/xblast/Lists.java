package ch.epfl.xblast;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
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
        List<T> copy = new ArrayList<>(l);
        List<List<T>> list = new ArrayList<>();
        int s1 = copy.size();
        if (s1<2){
            list.add(copy);
            return list;
        }
        if (s1==2){
            list.add(new ArrayList<>(copy));
            Collections.reverse(copy);
            list.add(new ArrayList<>(copy));
            return list;
        }
        List<List<T>> l1 = new ArrayList<>();
        l1.addAll(permutations(copy.subList(1, s1)));
        List<T> temp;
        T el = copy.get(0);
        for (List<T> t:l1){
            for (int i=0;i<s1;++i){
                temp= new ArrayList<>(t);
                temp.add(i,el);
                list.add(temp);
            }
        }
        return list;
    }
    
    public static <T> List<List<T>> permutationsBis(List<T> l){
        ArrayList<T> copy = new ArrayList<T>(l);
        ArrayDeque<T> el = new ArrayDeque<T>();
        ArrayList<T> temp = new ArrayList<>();
        List<List<T>> list = new ArrayList<>();
        if (l.size()<2) {
            list.add(l);
            return list;
        }
        while (copy.size()>2){
            el.addFirst(copy.get(0));
            copy.remove(0);
        }
        temp.addAll(copy); //size==2
        list.add(new ArrayList<>(temp));
        temp.clear();
        Collections.reverse(copy);
        temp.addAll(copy);
        list.add(new ArrayList<>(temp));
        while (!el.isEmpty()){
            list=recursionLists(list,el.getFirst());
            el.removeFirst();
        }
        return list;
    }
    
    private static <T> List<List<T>> recursionLists(List<List<T>> l,T el){
        ArrayList<ArrayDeque<T>> copy = new ArrayList<>();
        ArrayDeque<T> t;
        for (int i=0;i<l.size();++i){
            t=new ArrayDeque<>(l.get(i));
            copy.add(t);
        }
        ArrayList<List<T>> list = new ArrayList<>();
        for (int i=0;i<l.size();++i){
            copy.get(i).addFirst(el);
            for (int j=0;j<copy.get(i).size();++j){
                list.add(new ArrayList<>(copy.get(i)));
                copy.get(i).addFirst(copy.get(i).getLast());
                copy.get(i).removeLast();
            }
        }
        return list;
        
    }
}
