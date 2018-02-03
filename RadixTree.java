/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//
// This is the implementation for MIT PRIMES 2018 Problem 6
//

package radixtree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class RadixTree {

    static class Node {
   
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

    static Node root; 
     
    // Only allowed characters in our radix tree are 'A', 'C', 'G' and 'T',
    // which corresponds to the 4 DNA bases
    //
    static String dnaBases = "ACGT";
    static int MAX_CHILDREN = 4;
        
   /**
    * Returns the length of the common prefix between the nucleotide string
    * and the prefix code in treeNode. Nucleotide is never an empty string.
    * Examples:
    * nucleotide = AAA; prefixCode = AAA; We return 3
    * nucleotide = AAA; prefixCode = AA;  We return 2
    * nucleotide = AAA; prefixCode = AAAA; We return 3
    * nucleotide = AAA; prefixCode = null; We return 0
    * @param nucleotide: A string consisting of letters, A, C, G or T.
    * @param treeNode:   A radix tree node of type Node defined in Node.java
    * @return            Length of the common prefix
    */
    static int commonPrefixLength(String nucleotide, Node treeNode)
    {
        int dnaLen = nucleotide.length();
        int prefixLen;
        
        if (treeNode == null || treeNode.prefixCode == null)
            return 0;
        else
            prefixLen = treeNode.prefixCode.length();
       
        for (int i = 0; i < dnaLen; i++)
            if (i == prefixLen || 
                    nucleotide.charAt(i) != treeNode.prefixCode.charAt(i))
                return i;
       
        // nucleotide completely matches with the prefixCode
        return dnaLen;
       
    }
   
   
    /**
     * Check if the incoming nucleotide consists only of bases A, C, G or T.
     * If not, we should not add it to our radix tree. 
     * @param nucleotide A DNA segment, assumed to contain only A, C, G or T.
     * @return true if nucleotide only consists of bases A, C, G or T; else
     *         returns false.
     */
    static boolean invalidBaseFound(String nucleotide)
    {
        int dnaLen = nucleotide.length();
        
        if (dnaLen == 0)  // Do not add an empty string to tree
            return true;
        
        for (int i = 0; i < dnaLen; i++)
            if (dnaBases.indexOf(nucleotide.charAt(i)) == -1)
            {
                System.out.println("Invalid base " +  
                                nucleotide.charAt(i) + " found in input!");
                System.out.println("Only letters 'A', 'C', 'G', and 'T' are found in DNA");
                return true;
            }
        return false;
    }
    
    
    /**
     * Return the length of the prefix code corresponding to node. If node is
     * null or prefix code is an empty string, return 0
     * @param node  A node in radix tree
     * @return The length of the prefix code of the node
     */
    static int prefixCodeLength(Node node)
    {
        if (node != null && node.prefixCode != null)
            return node.prefixCode.length();
        else
            return 0;
    }
    
    
    /**
     * Insert a DNA segment into the radix tree specified by root if it is
     * already not there
     * @param nucleotide A string denoting a DNA segment made up of only 
     *                   characters A, C, G or T.
     * @param root       The root node of the radix tree. We assume that root
     *                   has been already created and is valid. Root is always
     *                   a dummy node.
     */
    static void insertDNA(String nucleotide, Node root)
    {
        int  index;           // Index where nucleotide should be added
        int  dnaBasesMatched; // No of nucleotide dnaBases matched between the 
                              // nucleotide and the prefix code at a given node
        Node currentNode;
        int  dnaLen = nucleotide.length();
        int  prefixLen = 0;
        
        // Check whether nucleotide is made up of only A, C, G, or T
        // Else don't add it to tree; return immediately
        if (invalidBaseFound(nucleotide))
            return;
        
        // Find the designated index based on the first char of nucleotide.
        // Each node has 4 children, so index could be one of 0, 1, 2, or 3.
        // nucleotide[0] = 'A', => index = 0; nucleotide[0] = 'C', => index = 1;
        // nucleotide[0] = 'G', => index = 2; nucleotide[0] = 'T', => index = 3;
        //
        index = dnaBases.indexOf(nucleotide.charAt(0));
        
        // Child node where this nucleotide should be added
        currentNode = root.childNode[index];
        
        // Get the length of the prefix code of currentNode
        prefixLen = prefixCodeLength(currentNode);
            
        // Find how many letters are matched between nucleotide and node
        dnaBasesMatched = commonPrefixLength(nucleotide, currentNode);
        
        // If there is no common prefix between the incoming nucleotide and the
        // prefixCode of the current node, create a new node and add it here.
        if (dnaBasesMatched == 0)
        {
            // Create an end of word node and attach it to the child at index
            Node tmpNode = new Node();
            tmpNode.prefixCode = nucleotide;
            tmpNode.endOfDNA = true;
            tmpNode.countOfDNA++;
            
            root.childNode[index] = tmpNode;
        }
        // There are 2 cases to consider here: (1) nucleotide exactly matches
        // with the prefixCode of the current node (2) nucleotide is a prefix
        // of the prefixCode of the current node
        else if (dnaBasesMatched == dnaLen)
        {
            // If the incoming nucleotide completely matches with the prefixCode 
            // of the current node, then no need to add it. Just mark the node 
            // as endOfDNA.
            if (dnaLen == prefixLen)
            {
                currentNode.endOfDNA = true;
                currentNode.countOfDNA++;
            }
            // Nucleotide is a prefix of the prefixCode of the current node. 
            // eg: nucleotide is AAA and the prefixCode = AAAGC. In this case
            // we need to split the current node into two, one containing AAA
            // and its child node containing GC.
            else if (prefixLen > dnaBasesMatched)
            {               
                // Find the unmatched suffix of the prefixCode of current node
                // This will become the prefix code of the new child node
                String suffix = 
                            currentNode.prefixCode.substring(dnaBasesMatched);
               
                // Create a new child node
                Node newNode = new Node();
                
                // Move the children of the current node to be the children
                // of the new node. Also reset the child nodes of currentNode
                for (int i=0; i < MAX_CHILDREN; i++)
                {
                    newNode.childNode[i] = currentNode.childNode[i];
                    currentNode.childNode[i] = null;
                }

                newNode.prefixCode = suffix;
                newNode.countOfDNA = currentNode.countOfDNA;
                
                // If currentNode was an end of word, make sure that
                // the newNode carries this information
                if (currentNode.endOfDNA == true)
                    newNode.endOfDNA = true;
                
                // Find the index of the current node where the new node should 
                // be added
                index = dnaBases.indexOf(suffix.charAt(0));
                
                // Attach newNode at the index of the currentNode
                currentNode.childNode[index] = newNode;
                
                // Trim the matched prefixCode at current node to nucleotide
                currentNode.prefixCode = nucleotide;
                
                // Since the incoming nucleotide is now the prefixCode of the
                // currentNode, mark this as end of word
                currentNode.endOfDNA = true;
                currentNode.countOfDNA = 1;
            }
        }
        // Only part of the incoming nucleotide matches with the prefixCode of
        // the current node. For eg: nucleotide is AAGC and prefixCode = AA. In
        // this case, we try to match the remaining letters at the designated
        // child node (recursively).
        else if (dnaBasesMatched < dnaLen) 
        { 
            if (dnaBasesMatched < prefixLen)
            // Eg: nucleotide is AAACC and the prefixCode = AAAGC. In this case
            // we need to split the current node into two, one containing AAA
            // and its child node containing GC. Then create a new node for CC
            // and setup the links
            {
                // Find the unmatched suffix of the prefixCode of the current
                // node. 
                String suffix2 = 
                            currentNode.prefixCode.substring(dnaBasesMatched);
                
                Node newNode = new Node();
                
                // Move the children of the current node to be the children
                // of the new node. Also reset the child nodes of currentNode
                for (int i=0; i < MAX_CHILDREN; i++)
                {
                    newNode.childNode[i] = currentNode.childNode[i];
                    currentNode.childNode[i] = null;
                }
                
                newNode.prefixCode = suffix2;
                newNode.countOfDNA++;
                // If currentNode was an end of word, make sure that
                // the newNode carries this information
                if (currentNode.endOfDNA == true)
                {
                    newNode.endOfDNA = true;
                    newNode.countOfDNA = currentNode.countOfDNA;
                }
                
                // Find the index of the current node where the new node should 
                // be added
                index = dnaBases.indexOf(suffix2.charAt(0));
                
                currentNode.childNode[index] = newNode;
                
                // Trim the matched prefixCode at current node
                currentNode.prefixCode = 
                            nucleotide.substring(0, dnaBasesMatched);
                
                // currentNode is no longer an end of word
                currentNode.endOfDNA = false;
                currentNode.countOfDNA = 0;
            }
            
            // Now, find the unmatched suffix of the incoming nucleotide. 
            String suffix = nucleotide.substring(dnaBasesMatched);
            
            // Insert nucleotide suffix recursively.
            insertDNA(suffix, currentNode); 
        }
    }
    
    
    /**
     * Prints a radix tree. Each node is printed on a separate line. This
     * method can be used to validate if a newly constructed tree has all
     * required nodes. 
     * @param tree The radix tree to be printed
     * @param dnaSegment A DNA segment from root to a leaf.
     */
    static void printRadixTree(Node tree, String dnaSegment)
    {
        if (tree == null)   // Nothing to print, so return
            return;
        
        // If we have reached the end of a word, print it
        if (tree.endOfDNA == true)
            System.out.println(dnaSegment);
        
        // Print children recursively
        for (int i = 0; i < MAX_CHILDREN; i++)
        {
            String currentSegment = null;
             
            if (tree.childNode[i] != null)
                currentSegment = dnaSegment + tree.childNode[i].prefixCode;
            
            printRadixTree(tree.childNode[i], currentSegment);
        }
    }
    
    
    /**
     * Print the radix tree rooted at tree. We use matching parenthesis to
     * indicate the codes corresponding to a node as well as a level in the
     * tree. Using the parenthesis, one could re-construct the entire tree on 
     * paper. Each node is printed with its formatted prefix.
     * @param tree A radix tree
     * @param dnaSegment A DNA segment, starting from the root node until we 
     *                   reach a leaf
     * @param level The level a node belongs to. Root is considered 0.
     */
    static void printNodes(Node tree, String dnaSegment, int level)
    {
        if (tree == null)   // Nothing to print, so return
            return;
        
        // If we have reached the end of a word, print it
        if (tree.endOfDNA == true)
        {
            int lenOfPrefix = dnaSegment.length() 
                                    - tree.prefixCode.length() - 2;
            if (lenOfPrefix == 0)
                System.out.println("Level " + level + ": " + dnaSegment);
            else
                System.out.println("Level " + level + ": " + 
                                dnaSegment.substring(1, lenOfPrefix+1) + 
                                "(" + tree.prefixCode + ")" ); 
        }
        
        level++;    // Go to next level of the tree
        
        // Print children recursively
        for (int i = 0; i < MAX_CHILDREN; i++)
        {
            String currentSegment = null;
             
            if (tree.childNode[i] != null)
            {
                currentSegment = dnaSegment + tree.childNode[i].prefixCode ;
                printNodes(tree.childNode[i], "(" + currentSegment + ")", 
                                                                        level);
            }
        }
    }
    
    
    /**
     * Returns the count of nodes in the entire radix tree. Root node is
     * not included in the count. Therefore, the caller should pass 
     * totalSoFar = 1, in order to include the root node in the final count.
     * @param tree: A radix tree
     * @param totalSoFar: totalSoFar denotes the count of nodes in the tree
     *      above.
     * @return Total count of nodes in the tree 
     */
    static int countNodes(Node tree, int totalSoFar)
    {
        if (tree == null)
            return 0;
        
        // If this node does not have any children, then only count this node
        if (!hasAChild(tree))  
            return 1;
        
        // Recursively add the count of all child nodes
        for (int i = 0; i < MAX_CHILDREN; i++)
        {
            int currentTotal = 0;
             
            if (tree.childNode[i] != null) 
            {
                currentTotal++;
                totalSoFar = totalSoFar + countNodes(tree.childNode[i], 
                                                currentTotal);
            }
        }
        
        return totalSoFar;  
    }
    
    
    
    /**
     * Returns the count of DNA strings in the entire radix tree. The count
     * of strings is equal to the number of end of words in the tree, which
     * is tracked by the boolean value, endOfDNA, in the tree.
     * @param tree: A radix tree
     * @param totalSoFar: totalSoFar denotes the count of strings the tree
     * @return Total count of DNA strings in the tree.
     */
    static int countStrings(Node tree, int totalSoFar)
    {
        if (tree == null)
            return 0;
        
        // Recursively add the count of all child nodes
        for (int i = 0; i < MAX_CHILDREN; i++)
        {
            int currentTotal = 0;
             
            if (tree.childNode[i] != null && tree.childNode[i].endOfDNA == true)
                currentTotal = currentTotal + tree.childNode[i].countOfDNA;
            
            totalSoFar = totalSoFar + countStrings(tree.childNode[i], 
                                                            currentTotal);
        }
        
        return totalSoFar;
    }
    
    
    /**
     * Returns the count of unique words in the entire radix tree. The count
     * of strings is equal to the number of end of words in the tree, which
     * is tracked by the boolean value, endOfWord, in the tree.
     * @param tree: A radix tree
     * @param totalSoFar: totalSoFar denotes the count of strings the tree
     * @return Total count of unique words in the tree.
     */
    static int countUniqueStrings(Node tree, int totalSoFar)
    {
        if (tree == null)
            return 0;
        
        // Recursively add the count of all child nodes
        for (int i = 0; i < MAX_CHILDREN; i++)
        {
            int currentTotal = 0;
             
            if (tree.childNode[i] != null && 
                        tree.childNode[i].endOfDNA == true)
                currentTotal++;
            
            totalSoFar = totalSoFar + countUniqueStrings(tree.childNode[i], 
                                                            currentTotal);
        }
        
        return totalSoFar;
    }
    
    
    /**
     * Collects all the string in a radix tree and returns them as a list of
     * strings.
     * @param tree: A radix tree
     * @param dnaSegment: A DNA segment leading up to its leaf node
     * @param dnaStrings: Intermediate list of string used to collect all the
     *                    strings in the tree
     * @return A list of strings found in the tree.
     */
    static List<String> gatherStrings(Node tree, String dnaSegment, 
                                                    List<String> dnaStrings)
    {
       
        if (tree == null)
            return dnaStrings;
        
        if (tree.endOfDNA == true)  // If this is a complete word, then add it
            dnaStrings.add(dnaSegment);
      
        // Recursively add the strings in the child nodes
        for (int i = 0; i < MAX_CHILDREN; i++)
        {
            String currentSegment = null;
             
            if (tree.childNode[i] != null)
            {
                currentSegment = dnaSegment + tree.childNode[i].prefixCode;
                gatherStrings(tree.childNode[i], currentSegment, dnaStrings);
            }
        }
        
        return dnaStrings;
    }
    
    
    
    /**
     * Finds a DNA string in a radix tree.
     * @param nucleotide: A DNA segment to be searched
     * @param tree: A radix tree consisting of valid DNA segments
     * @return Returns true if the specified DNA string is found in the tree;
     *         else returns false.
     */
    static boolean findDNA(String nucleotide, Node tree)
    {
        // If tree is empty or the search string consists of invalid letters
        // no need to search.
        if (tree == null || invalidBaseFound(nucleotide))
            return false;
       
        // Find the common prefix length between the nucleotide and the code
        // pointed to by the current node
        int prefixLen = commonPrefixLength(nucleotide, tree);
          
        if (prefixLen == 0)    // This may be root node, so check its children
        {
            int baseIndex = dnaBases.indexOf(nucleotide.charAt(prefixLen));
             
            // Search the tree rooted at childNode[baseIndex]
            return findDNA(nucleotide.substring(prefixLen), 
                                                    tree.childNode[baseIndex]);
        }
        else if (prefixLen == tree.prefixCode.length()) 
        // Eg: nucleotide = AGCC% and tree.prefixCode = AGCC
        {
            if (prefixLen == nucleotide.length())
            // Eg: nucleotide = AGCC and tree.prefixCode = AGCC
            { 
                if (tree.endOfDNA == true)
                    return true;      // Entire nucleotide matches with prefix 
                else
                    return false;     // But this node is not an end of word
            }
            // Now we are dealing with the case, nucleotide length > prefixLen
            // Eg: nucleotide = AGCCTAACG and tree.prefixCode = AGCC
            else
            {
                // Eg: nucleotide = AGCCTAACG and tree.prefixCode = AGCC
                // We need to split the nucleotide into AGCC and TAACG and
                // look for TAACG in the child tree 
                String suffix = nucleotide.substring(prefixLen);
                
                // Find the index of child node where suffix can be found
                int index = dnaBases.indexOf(suffix.charAt(0));
               
                // Search for suffix recursively in the child node
                return findDNA(suffix, tree.childNode[index]);
            }
        }
        else if (prefixLen < tree.prefixCode.length())
            return false;         // Unless the entire prefixCode matches, the 
                                  // word is not found in tree
        return false;
    }
    
    
    /**
     * This is a helper method, which finds whether a given node
     * has a child.
     * @param tree: A radix tree
     * @return Returns true if node "tree" has a child; else returns false
     */
    static boolean hasAChild(Node tree)
    {
        if (tree == null)
            return false;
        
        for (int i = 0; i < MAX_CHILDREN; i++)
            if (tree.childNode[i] != null)
                return true;
        
        return false; 
    }
    
    
    /**
     * This is a helper method, which deletes a node, including its children
     * permanently.
     * @param tree : A node to be deleted from a radix tree
     */
    static void deleteNode(Node tree)
    {
        tree.prefixCode = "";
        tree.endOfDNA = false;
        tree.countOfDNA = 0;
        
        // Also deletes its children
        for (int i=0; i < MAX_CHILDREN; i++)
        {
            if (tree.childNode[i] != null)
            {
                tree.childNode[i].prefixCode = null;
                tree.childNode[i].endOfDNA = false;
                tree.childNode[i].countOfDNA = 0;
                tree.childNode[i] = null;
            }
        }
        tree = null;
        System.gc();
    }
    
    
    /**
     * Deletes a DNA string in a radix tree.
     * @param nucleotide: A DNA segment to be deleted
     * @param tree: A radix tree consisting of valid DNA segments
     * @return Returns true if the specified DNA string is found and deleted;
     *         else returns false.
     */
    static boolean deleteDNA(String nucleotide, Node tree)
    {
        // If this is an emptry tree or the prefixCode is not present, or the
        // string to be deleted is an invalid DNA string, return immediately
        if (tree == null || tree.prefixCode == null 
                || invalidBaseFound(nucleotide))
            return false;
       
        int prefixLen = commonPrefixLength(nucleotide, tree);
        
        if (prefixLen == 0)  // This may be root node, so check its children
        {
            int baseIndex = dnaBases.indexOf(nucleotide.charAt(prefixLen));
        
            // Look for nucleotide in the tree rooted at the child node
            return deleteDNA(nucleotide.substring(prefixLen), 
                                            tree.childNode[baseIndex]);
        }
        else if (prefixLen == tree.prefixCode.length()) 
        // Eg: nucleotide = AGCC% and tree.prefixCode = AGCC
        {
            if (prefixLen == nucleotide.length())
            // Eg: nucleotide = AGCC and tree.prefixCode = AGCC
            {
                if (hasAChild(tree))  // Does this node have at least one child?
                {
                    // We cannot remove this node, but decrement word count
                    tree.countOfDNA--;
                    
                    // If no more end of words represented by this node,
                    // indicate it
                    if (tree.countOfDNA == 0)
                        tree.endOfDNA = false;                  
                }
                else  // We can safely remove this node
                    deleteNode(tree);
               
                return true;   // Entire nucleotide matches with prefix 
            }
            // Now we are dealing with the case, nucleotide length > prefixLen
            // Eg: nucleotide = AGCCTAACG and tree.prefixCode = AGCC
            else
            {
                // Eg: nucleotide = AGCCTAACG and tree.prefixCode = AGCC
                // We need to split the nucleotide into AGCC and TAACG and
                // look for TAACG in the child tree 
                String suffix = nucleotide.substring(prefixLen);
                
                // Find the index of child node where suffix can be found
                int index = dnaBases.indexOf(suffix.charAt(0));
           
                // Recursively for suffix in the tree rooted at 
                // childNode[index] and delete the leaf node
                return deleteDNA(suffix, tree.childNode[index]);
            }
        }
        else if (prefixLen < tree.prefixCode.length())
            return false;         // Unless the entire prefixCode matches, the 
                                  // word is not found in tree
        return false;
    }
    
    
    
    /**
     * Generates a random DNA string of length between 10 and 100. The string
     * contains only valid bases of A, C, G and T.
     * @return A valid DNA string
     */
    static String generateRandomDNA()
    {
        int MAXLEN = 100;       // Maximum length of DNA string
        int MINLEN = 10;        // Minimum length of DNA string
        int dnaLen = 0;
        String dnaString = "";
        
        // Make sure DNA string is atleast 10 char long
        while (dnaLen < MINLEN)
            dnaLen = (int) Math.ceil(Math.random()*MAXLEN);
        
        for (int i = 0; i < dnaLen; i++)
        {
            int index = (int) Math.ceil(Math.random()*MAX_CHILDREN) - 1;
            char base = dnaBases.charAt(index);
            dnaString = dnaString + base;
        }   
        return dnaString;
    }
    
    
    /**
     * Called from the user menu, this option allows the user to insert a 
     * single word into the radix tree.
     * 
     * @param myTree : A radix tree
     */
    public static void insertAWord(Node myTree)
    {
        String newWord;
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter a word to insert: ");
        newWord = scanner.nextLine();
        while (invalidBaseFound(newWord))
        {
            System.out.println("Word " + newWord + " contains an invalid character. Please enter a valid word.");
            newWord = scanner.nextLine();
        }
        System.out.println("     *********************************************");
        System.out.println("     ********     Insert Successful!     *********");
        System.out.println("     *********************************************");
        System.out.println("     ** Number of unique words in tree BEFORE = " + countUniqueStrings(myTree, 0));
        System.out.println("     ** Number of total words in tree BEFORE  = " + countStrings(myTree, 0));
        System.out.println("     *********************************************"); 
        insertDNA(newWord, myTree);
        System.out.println("     *********************************************");
        System.out.println("     ** Number of unique words in tree AFTER = " + countUniqueStrings(myTree, 0));
        System.out.println("     ** Number of total words in tree AFTER  = " + countStrings(myTree, 0));
        System.out.println("     *********************************************"); 
        System.out.println();
    }

    
    /**
     * This methods inserts a number of randomly generated DNA segments
     * into the tree. Any integer number can be specified by the user
     * 
     * @param myTree 
     */
    
    public static void insertRandomDNAs(Node myTree)
    {
        int choice;
        Scanner input = new Scanner(System.in);

        System.out.println("Enter number of random DNA segments to be inserted:");
        
        choice = input.nextInt();
          
        for (int i = 0; i < choice; i++)
        {
            String dnaStr = generateRandomDNA();
            insertDNA(dnaStr, myTree); 
        }
    }
    
    
    /**
     * Called from the user menu, this option allows the user to search for
     * a word in the tree.
     * If the word contains an invalid character, an error message is printed.
     * It in turn calls the method, findWord() to search for the word in the
     * tree. An appropriate message is printed to indicate whether the word
     * was found in the tree or not.
     * 
     * @param myTree : A radix tree
     */
    public static void findAWord(Node myTree)
    {
        String newWord;
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter a word to search: ");
        newWord = scanner.nextLine();
        while (invalidBaseFound(newWord))
        {
            System.out.println("Word " + newWord + " contains an invalid character. Please enter a valid word.");
            newWord = scanner.nextLine();
        }
        if (findDNA(newWord, myTree) == true)
            System.out.println("Word, " + newWord + " was found in the tree!");
        else
            System.out.println("Word, " + newWord + " was NOT found in the tree!");
        System.out.println();
    }
    
    
    /**
     * Called from the user menu, this option allows the user to delete
     * a word from the tree.
     * If the word contains an invalid character, an error message is printed.
     * It in turn calls the method, deleteWord() to delete the word from the
     * tree. An appropriate message is printed to indicate whether the word
     * was deleted or not.
     * 
     * @param myTree
     */
    public static void deleteAWord(Node myTree)
    {
        String newWord;
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter a word to delete: ");
        newWord = scanner.nextLine();
        while (invalidBaseFound(newWord))
        {
            System.out.println("Word " + newWord + " contains an invalid character. Please enter a valid word.");
            newWord = scanner.nextLine();
        }
        int noOfWords = countStrings(myTree, 0);
        int noOfUniqueWords = countUniqueStrings(myTree, 0);
        
        if (deleteDNA(newWord, myTree) == true)
        {
            System.out.println("     *********************************************");
            System.out.println("     ********     Delete Successful!     *********");
            System.out.println("     *********************************************");
            System.out.println("     ** Number of unique words in tree BEFORE = " + noOfUniqueWords);
            System.out.println("     ** Number of total words in tree BEFORE  = " + noOfWords);
            System.out.println("     *********************************************"); 
            System.out.println("     *********************************************");
            System.out.println("     ** Number of unique words in tree AFTER = " + countUniqueStrings(myTree, 0));
            System.out.println("     ** Number of total words in tree AFTER  = " + countStrings(myTree, 0));
            System.out.println("     *********************************************"); 
            System.out.println();
        }
        else
        {
            System.out.println("Word, " + newWord + " was NOT found in the tree!");
            System.out.println();
        }
    }
    
    
    /**
     * This method prints all the words found the given tree sequentially.
     * It also prints the number of unique words and the total number of words.
     * 
     * @param myTree : A radix tree
     */
    public static void printWordsInTree(Node myTree)
    {
        printRadixTree(myTree, "");
        System.out.println("     *********************************************");
        System.out.println("     ** Number of unique words in the tree = " + countUniqueStrings(myTree, 0));
        System.out.println("     ** Number of total words in the tree  = " + countStrings(myTree, 0));
        System.out.println("     *********************************************"); 
        System.out.println();
    }
    
    
    /**
     * This method prints all the words and their prefixes along with the 
     * nodes. It prints the prefixes using matching parentheses, which allows
     * users to reconstruct the tree using paper and pencil.
     * 
     * @param myTree : A radix tree.
     */
    public static void printPrefixTree(Node myTree)
    {
        printNodes(myTree, "", 0);
        System.out.println("     *********************************************");
        System.out.println("     ** Number of unique words in the tree = " + countUniqueStrings(myTree, 0));
        System.out.println("     ** Number of total words in the tree  = " + countStrings(myTree, 0));
        System.out.println("     ** Number of nodes in the tree        = " + countNodes(myTree, 0));
        System.out.println("     *********************************************"); 
        System.out.println();
    }
    
    
    /**
     * This method prints all the words in the tree in alphabetical order.
     * 
     * @param myTree 
     */
    public static void printAlphabeticalStrings(Node myTree)
    {
        List<String> myList = new ArrayList<String>();
      
        myList = gatherStrings(myTree, "", myList);
        Collections.sort(myList); 
      
        //
        // myList is now in alphabetical order
        //
        for (int i = 0; i < myList.size(); i++)
            System.out.println(myList.get(i)); 
        
        System.out.println("     *********************************************");
        System.out.println("     ** Number of unique words in the tree = " + countUniqueStrings(myTree, 0));
        System.out.println("     ** Number of total words in the tree  = " + countStrings(myTree, 0));
        System.out.println("     ** Number of nodes in the tree        = " + countNodes(myTree, 0));
        System.out.println("     *********************************************"); 
    }
    
    /**
     * This method provides a list of options for the user, which allows
     * various methods to be executed.
     * 
     * @return 
     */
    public static int userMenu() {

        int choice;
        Scanner input = new Scanner(System.in);

        System.out.println("Please select an option:");
        System.out.println("0 \t Insert randmly generated DNAs");
        System.out.println("1 \t Insert a DNA segment");
        System.out.println("2 \t Search for a DNA segment");
        System.out.println("3 \t Delete a DNA segment");
        System.out.println("4 \t Print DNA segments in tree");
        System.out.println("5 \t Print prefix tree");
        System.out.println("6 \t Print number of DNA segments in tree");
        System.out.println("7 \t Print number of nodes in tree");
        System.out.println("8 \t Print DNA segments in alphabetical order");
        System.out.println("9 \t Quit");

        choice = input.nextInt();
        return choice;    
    }
    
    

    /**
     * Main method, which invokes the user menu to call
     * for various actions
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        int option = 0;
        
        // Create the root node. Root node is a dummy node, which does not have
        // any data, other than links to its children
        //
        Node myTree = new Node();
        myTree.prefixCode = "";
        
        // Get option from user and execute it
        
        while (option != 9)
        {
            option = userMenu();
            
            switch (option) 
            {
                case 0:
                    insertRandomDNAs(myTree);
                    break;
                case 1:
                    insertAWord(myTree);
                    break;
                case 2:
                    findAWord(myTree);
                    break;
                case 3:
                    deleteAWord(myTree);
                    break;
                case 4:
                    printWordsInTree(myTree);
                    break;
                case 5:
                    printPrefixTree(myTree);
                    break;
                case 6:
                    System.out.println("     *********************************************");
                    System.out.println("     ** Number of total words in the tree  = " + countStrings(myTree, 0));
                    System.out.println("     *********************************************");
                    break;
                case 7:
                    System.out.println("     *********************************************");
                    System.out.println("     ** Number of nodes in the tree        = " + countNodes(myTree, 0));
                    System.out.println("     *********************************************");
                    break;
                case 8:
                    printAlphabeticalStrings(myTree);
                    break;
                case 9:
                    break;
                default:
                    System.out.println("Invalid choice. Please try again!");
            }
        }
    } 
}
     
   
      