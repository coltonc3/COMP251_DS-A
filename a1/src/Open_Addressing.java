import java.io.*;
import java.util.*;

public class Open_Addressing {
     public int m; // number of SLOTS AVAILABLE
     public int A; // the default random number
     int w;
     int r;
     public int[] Table;

     protected Open_Addressing(int w, int seed, int A) {

         this.w = w;
         this.r = (int) (w-1)/2 +1;
         this.m = power2(r);
         if (A==-1){
            this.A = generateRandom((int) power2(w-1), (int) power2(w),seed);
         }
        else{
            this.A = A;
        }
         this.Table = new int[m];
         for (int i =0; i<m; i++) {
             Table[i] = -1;
         }
         
     }


     /** Calculate 2^w*/
     public static int power2(int w) {
         return (int) Math.pow(2, w);
     }


     public static int generateRandom(int min, int max, int seed) {     
         Random generator = new Random(); 
                 if(seed>=0){
                    generator.setSeed(seed);
                 }
         int i = generator.nextInt(max-min-1);
         return i+min+1;
     }


     /**Implements the hash function g(k) i.e. linear probing */
     public int probe(int key, int i) {
         return (hash(key) + i) % power2(this.r);
     }


     /* copy of chain() method from Chaining class, to be used in probe function */
     public int hash(int key) {
         return ((this.A * key) % power2(this.w)) >> (this.w - this.r);
     }
     
     
     /**Inserts key k into hash table. Returns the number of collisions encountered*/
     public int insertKey(int key){
         int probeCounter = 0;
         int index = probe(key, probeCounter);
         int collisions = 0;

         /* list of indices that have already been visited so we don't overcount collsions */
         ArrayList<Integer> visited = new ArrayList<>();

         while(Table[index] != -1 && collisions < Table.length) {
             probeCounter++;
             if (!visited.contains((index))) {
                 collisions++;
                 visited.add(index);
             }
             index = probe(key, probeCounter);
         }

         if(collisions < Table.length)
            Table[index] = key;

         return collisions;
     }


     /**Sequentially inserts a list of keys into the HashTable. Outputs total number of collisions */
     public int insertKeyArray (int[] keyArray){
         //TODO
         int collision = 0;
         for (int key: keyArray) {
             collision += insertKey(key);
         }
         return collision;
     }


     /**Removes key k from hash table. Returns the number of slots visited*/
     public int removeKey(int key){
         int visited = 0;
         int probeCounter = 0;
         int index = probe(key, probeCounter);

         /* increment probeCounter and continue running probe hash function until we find the key we're looking for */
         while (this.Table[index] != key && visited < Table.length) {
             visited++;
             probeCounter++;
             index = probe(key, probeCounter);
         }

         /* replace removed key with -1 */
         this.Table[index] = -1;
         return visited;
     }
}
