import java.util.Iterator;
import java.util.HashMap;
import java.util.Deque;
import java.util.ArrayDeque;


/**
 * FibonnacciMaxPQ
 * A Fibonacci heap.
 */
public class FibonnacciMaxPQ {
    // Maximum Node of list	of roots			
    private Node maxRoot;
    Node[] rootListByRank;
    
    /**
	 * Initializes a priority queue with given nodes
	 * Worst case is O(n)
     * 
	 */
	public FibonnacciMaxPQ() {
        this.maxRoot = null;
    }
    
    /**
     * Insert a node in the Fibonnacci heap
     * Inserts a node into the root list 
     * and checks whether its value is lower than the currently lowest node 
     * and changes the access pointer if necessary.
	 * Worst case is O(1)
     * 
     * @param node to be inserted in the FibonnacciMaxPQ
     * @return true if successful
     * 
	 * 
	 */
    public boolean insertNode(Node node) {

        if(node == null)
            return false;

        if(this.maxRoot == null)
            this.maxRoot = node;
        else {
            this.maxRoot.addSibling(node);
            if(this.maxRoot.count < node.count)
                this.maxRoot = node;
        }
        return true;
    }

    /**
     * Increase the value of key for the given node in Fibonnacci heap
     * Then the node is removed from its sibling list and is inserted into the root list. 
     * At least, it is checked whether the access pointer needs to be changed. 
     *  
     * @param delta the amount to increase the value 
     * @param vertex Node
     * @return true if successful
     * 
     **/ 
    public void increaseKey(int delta, Node node) {

        node.count = node.count + delta;

        // The node has a parent
        if(node.parent != null) 
        {
            // Remove node and add to root list
            node.cascadingCut();
            this.maxRoot.addSibling(node);        
        }
        // Check if key is smaller than the key of maxRoot
        if(node.count > this.maxRoot.count)
            this.maxRoot = node;
        // cascadingCut(y);

    }

    // 
    /**
     * Removes the Node with maximum count from the Fibonnacci heap
     * 
     * @return maxRoot
     */
    public Node removeMax(int numOfLines) {
        Node temp, nextTemp;
        if (this.maxRoot == null) {
            return null;
        }
        
        if (this.maxRoot.child != null && this.maxRoot.child.leftMostSibling() != null) {
            temp = this.maxRoot.child.leftMostSibling();
            nextTemp = null;

            // Adding Children to root list        
            while(temp != null)
            {
                nextTemp = temp.rightSibling; // Save next Sibling
                temp.cascadingCut();
                this.maxRoot.addSibling(temp);
                temp = nextTemp;
            }
        }
        // Select the left-most sibling of maxRoot
        temp = this.maxRoot.leftMostSibling();

        // Remove maxRoot and set it to any sibling, if there exists one
        if(temp == this.maxRoot)
        {
            if(this.maxRoot.rightSibling != null)
                temp = this.maxRoot.rightSibling;
            else
            {
                // Heap is obviously empty
                Node out = this.maxRoot;
                this.maxRoot.cascadingCut();
                this.maxRoot = null;
                return out;
            }
        }
        Node out = maxRoot;
        this.maxRoot.cascadingCut();
        this.maxRoot = temp;

        this.rootListByRank = new Node[numOfLines];
        
        while(temp != null)
        {
            // Check if key of current vertex is smaller than the key of maxRoot
            if(temp.count > this.maxRoot.count)
            {
                this.maxRoot = temp;
            }

            nextTemp = temp.rightSibling;        
            merge(temp);
            temp = nextTemp;
        }

        return out;    

    }

    /*************************************
	* General helper functions for manipulating circular lists
	************************************/

    /**
     * 
     * @param root
     * @return true if successful
     * 
     * Checks if two nodes in the root list have the same rank. 
     * If yes, the node with the higher key is moved into the children list of the other node.
     */
    public boolean merge(Node root) {
        // Insert Vertex into root list
        if(this.rootListByRank[root.order] == null)
        {
            this.rootListByRank[root.order] = root;
            return false;
        }
        else
        {
            // Link the two roots
            Node linkVertex = this.rootListByRank[root.order];
            this.rootListByRank[root.order] = null;
            
            if(root.count > linkVertex.count || root == this.maxRoot) {
                linkVertex.cascadingCut();
                root.addChild(linkVertex);
                if(this.rootListByRank[root.order] != null)
                    merge(root);
                else
                    this.rootListByRank[root.order] = root;
            }
            else {
                root.cascadingCut();
                linkVertex.addChild(root);
                if(this.rootListByRank[linkVertex.order] != null)
                    merge(linkVertex);
                else
                    this.rootListByRank[linkVertex.order] = linkVertex;
            }
            return true;
        }
    }

    
    /**
     * Creates a String representation of this Fibonacci heap.
     *
     * @return String of this.
     */
    @Override
    public String toString() {
        if (this.maxRoot == null) {
            return "FibonacciHeap=[]";
        }

        // create a new stack and put root on it
        Deque<Node> stack = new ArrayDeque<>();
        stack.push(this.maxRoot);

        StringBuilder buf = new StringBuilder(512);
        buf.append("FibonacciHeap=[");
        
        // do a simple breadth-first traversal on the tree
        while (!stack.isEmpty()) {
            Node currentNode = stack.pop();
            buf.append(currentNode);
            buf.append(", ");

            if (currentNode.child != null) {
                stack.push(currentNode.child);
            }

            Node start = currentNode;
            currentNode = currentNode.rightSibling;

            while (currentNode != start && currentNode != null) {
                buf.append(currentNode);
                buf.append(", ");

                if (currentNode != null && currentNode.child != null) {
                    stack.push(currentNode.child);
                }

                currentNode = currentNode.rightSibling;
            }
        }

        buf.append(']');

        return buf.toString();
    }

    


}

