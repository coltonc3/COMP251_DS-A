import java.io.*;
import java.util.*;


/****************************
*
* COMP251 template file
*
* Assignment 2, Question 1
*
*****************************/

/* NO COLLABORATORS */
public class DisjointSets {

    private int[] par;
    private int[] rank;
    
    /* contructor: creates a partition of n elements. */
    /* Each element is in a separate disjoint set */
    DisjointSets(int n) {
        if (n>0) {
            par = new int[n];
            rank = new int[n];
            for (int i=0; i<this.par.length; i++) {
                par[i] = i;
                rank[i] = 0;
            }
        }
    }
    
    public String toString(){
        int pari,countsets=0;
        String output = "";
        String[] setstrings = new String[this.par.length];
        /* build string for each set */
        for (int i=0; i<this.par.length; i++) {
            pari = find(i);
            if (setstrings[pari]==null) {
                setstrings[pari] = String.valueOf(i);
                countsets+=1;
            } else {
                setstrings[pari] += "," + i;
            }
        }
        /* print strings */
        output = countsets + " set(s):\n";
        for (int i=0; i<this.par.length; i++) {
            if (setstrings[i] != null) {
                output += i + " : " + setstrings[i] + "\n";
            }
        }
        return output;
    }
    
    /* find representative of element i and do path compression along the way */
    public int find(int i) {
        if (par[i] == i) {
            return i;
        }
        else {
            par[i] = find(par[i]); // path compression - make all nodes on the find path direct children of the root
            return par[i];
        }
    }

    /* merge sets containing elements i and j by merging the set with the smaller rank into the set with the larger rank
    * if both ranks are the same, merge set containing i into st containing j
    * @return the representative of the new merged set
    * */
    public int union(int i, int j) {
        int repI = find(i); // representative of set containing i
        int repJ = find(j); // representative of set containing j
        if (rank[i] <= rank[j]) {
            par[repI] = repJ;
            if (rank[i] == rank[j]) {
                rank[j]++;
            }
            return repJ;
        }
        else {
            par[repJ] = repI;
            return repI;
        }
    }
    
    public static void main(String[] args) {
        
        DisjointSets myset = new DisjointSets(6);
        System.out.println(myset);
        System.out.println("-> Union 2 and 3");
        myset.union(2,3);
        System.out.println(myset);
        System.out.println("-> Union 2 and 3");
        myset.union(2,3);
        System.out.println(myset);
        System.out.println("-> Union 2 and 1");
        myset.union(2,1);
        System.out.println(myset);
        System.out.println("-> Union 4 and 5");
        myset.union(4,5);
        System.out.println(myset);
        System.out.println("-> Union 3 and 1");
        myset.union(3,1);
        System.out.println(myset);
        System.out.println("-> Union 2 and 4");
        myset.union(2,4);
        System.out.println(myset);
        
    }

}
