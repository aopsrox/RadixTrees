/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package radixtree;


public class Node {
   
    static int MAX_CHILDREN = 4;   // A child could be one of A, C, G, or T.
    boolean endOfDNA; // Indicates if this node marks the end of a DNA segment
    int countOfDNA;   // Maintains the count of DNA segments
        
    // Every node can have at most 4 children. They could be either
    // a single character or a larger string consisting of A, C, G, and T.
    // Node[0] = "A..."; 
    // Node[1] = "C..."; 
    // Node[2] = "G..."; and 
    // Node[3] = "T..."
    //
    Node[] childNode = new Node[MAX_CHILDREN];
    String prefixCode;
    Node(){
        endOfDNA = false;
        countOfDNA = 0;
        for (int i = 0; i < 4; i++)
            childNode[i] = null;
    }
}
