import java.io.*;
import java.util.*;


public class main {

    Chaining c = new Chaining(5, 0, -1);
    Open_Addressing oa = new Open_Addressing(5, 0, -1);
    int[] keyArray = {1,2,3,4,5};
    String p = "passed";
    String f = "failed";

    public static void main(String[] args) {
        main m = new main();
        m.chainTest();
        m.chainingInsertKeyTest();
        m.probeTest();
        m.openAddressingInsertKeyTest();
        m.openAddressingRemoveKeyTest1();
        m.openAddressingRemoveKeyTest2();
    }

    public void chainTest() {
        int hash = c.chain(1);
        String result = hash == 4 ? p : f;
        System.out.println("chain test: " + result);
    }

    public void chainingInsertKeyTest(){
        c.insertKeyArray(keyArray);
        int collisions = c.insertKey(6);
        String result = collisions == 1 ? p : f;
        System.out.println("chaining insert key test: " + result);
    }

    public void probeTest(){
        int hash = oa.probe(1, 0);
        String result = hash == 4 ? p : f;
        System.out.println("probe test: " + result);
    }

    public void openAddressingInsertKeyTest(){
        oa.insertKeyArray(keyArray);
        int collisions = oa.insertKey(6);
        String result = collisions == 1 ? p : f;
        System.out.println("open addressing insert key test: " + result);
    }

    /* remove a present key */
    public void openAddressingRemoveKeyTest1(){
        int collisions = oa.removeKey(4);
        String result = collisions == 0 ? p : f;
        System.out.println("open addressing remove existing key test: " + result);
    }

    /* remove a key that does not exist */
    public void openAddressingRemoveKeyTest2(){
        int collisions = oa.removeKey(12);
        String result = collisions == oa.Table.length ? p : f;
        System.out.println("open addressing remove nonexisting key test: " + result);
    }
}