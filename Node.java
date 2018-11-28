//Represents a Node of a tree

/**
 * Since we have an unknown number of children in Fibonacci heaps,
 * we have to arrange the children of a node in a linked list. 
 * So, we need at most two pointers to the siblings of every node. 
 * This results in a linear double-linked list. 
 * Now, we need another pointer to any node of the children list and to the parent of every node. 
 * All in all, there are 5 members
 * Furthermore, we define a rank(order) for every node, which says how many children a node has.
 * 
 * Worst case is O(1)
 * @return the number of elements on the priority queue
 */
public class Node {
    // count of keyword stored in this Node
    int count;
    // Order of the tree rooted by this Node			
    int order;
    // Siblings of this Node	
    Node rightSibling, leftSibling;
    // Child of this Node	
    Node child, parent;
    // keyword
    String key;

    // Constructor initializing key and count members
    public Node (String key, int count) {
        this.count = count;
        this.key = key;
        this.order = 0;
        this.rightSibling = null;
        this.leftSibling = null;
        this.child = null;
        this.parent = null;
    }

    public Node () {}

    /**
     *
     * UpdateCount
     * Updates the count of the current node 
     * This operation is done in time O(1).
     */
    public void updateCount(int count) {
        this.count = this.count + count;
    }

    /**
     *
     * AddChild
     * Adds a child node to the current node by inserting it into the children list and linking it. 
     * This operation is done in time O(1).
     */
    public boolean addChild(Node node) {
		if(this.child != null) {
            this.child.addSibling(node);
        } else {
            this.child = node;
            node.parent = this;
            this.order = 1;
        }

        return true;
    }

    /**
     * AddSibling
     * Adds a node into the child list the current node belongs to. 
     * This is done in time O(1) too.
     */
    public boolean addSibling(Node node) {
        Node temp = rightMostSibling();
        if(temp == null)
            return false;

        temp.rightSibling = node;
        node.leftSibling = temp;
        
        node.rightSibling = null;
        node.parent = this.parent;

        if(this.parent != null)
            this.parent.order++;

        return true;
    }

    /**
     * Remove
     * Removes the node from the sibling list and refreshes the affected pointers. 
     * This is also done in time O(1).
     * 
     */
    public boolean cascadingCut() {
		if(parent != null)
        {
            this.parent.order--;
            if(this.leftSibling != null)
                this.parent.child = this.leftSibling;
            else if(this.rightSibling != null)
                this.parent.child = this.rightSibling;
            else
                this.parent.child = null;
        }    
        
        if(this.leftSibling != null)
            this.leftSibling.rightSibling = this.rightSibling;
        if(this.rightSibling != null)
            this.rightSibling.leftSibling = this.leftSibling;
        
        this.leftSibling = null;
        this.rightSibling = null;
        this.parent = null;

        return true;
    }

    /**
     * RightMostSibling
     * Traverse the heap to get the right most node 
     * This is also done in time O(n).
     * 
     * @return Node which is the right most sibling of current node
     */
    public Node rightMostSibling(){
        
        if(this == null)
            return null;

        Node temp = this;
        while(temp.rightSibling != null)
            temp = temp.rightSibling;
        return temp;
    }

    /**
     * LeftMostSibling
     * Traverse the heap to get the left most node 
     * This is also done in time O(n).
     * 
     * @return Node which is the left most sibling of current node
     */
    public Node leftMostSibling(){
        
        if(this == null)
            return null;

        Node temp = this;
        while(temp.leftSibling != null)
            temp = temp.leftSibling;
        return temp;
    }
   
    public String toString() 
    { 
        return this.key + " - " + Integer.toString(this.count);
    }
    
}
