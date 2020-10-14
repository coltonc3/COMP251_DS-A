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

         /* continuously call hash function until we find an open slot in the Table,
          * stop when # of collisions is equal to Table length
          */
         while(this.Table[index] != -1 && collisions < this.Table.length) {
             probeCounter++;

             /* this is to avoid double-counting visited slots for counting collisions */
             if (!visited.contains((index))) {
                 collisions++;
                 visited.add(index);
             }
             index = probe(key, probeCounter);
         }

         /* only change Table if there's an available slot for our key */
         if(collisions < this.Table.length)
            this.Table[index] = key;

         return collisions;
     }


     /**Sequentially inserts a list of keys into the HashTable. Outputs total number of collisions */
     public int insertKeyArray (int[] keyArray){
         int collision = 0;
         for (int key: keyArray) {
             collision += insertKey(key);
         }
         return collision;
     }


     /**Removes key k from hash table. Returns the number of slots visited*/
     public int removeKey(int key){
         int visited = 0; // number of collisions encountered before finding the key for removal
         int probeCounter = 0;
         int index = probe(key, probeCounter);

         /* increment probeCounter and continue running probe hash function until we find the key we're looking for */
         while (this.Table[index] != key && visited < this.Table.length) {
             visited++;
             probeCounter++;
             index = probe(key, probeCounter);
         }

         /* replace key with -1 if # of collisions is less than Table length */
         if (visited<this.Table.length)
            this.Table[index] = -1;
         return visited;
     }
}
