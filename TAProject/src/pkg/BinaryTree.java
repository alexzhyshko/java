package pkg;
import java.util.ArrayList;
import java.util.LinkedList; 
import java.util.Queue; 
public class BinaryTree { 
       
    /* A binary tree node has key, pointer to  
    left child and a pointer to right child */
    Node root; 
    Node temp = root; 
    
    public BinaryTree() {
    	this.root = new Node("i");
    }
    
    public ArrayList<String> search(String string) {
    	ArrayList<String> result = new ArrayList<>();
    	Node temp = root;
    	string = string.toLowerCase();
    	inorder(temp, string, result);
    	return result;
    }
    /* Inorder traversal of a binary tree*/
    public void inorder(Node temp,String string,  ArrayList<String> result) 
    { 
        if (temp == null) 
            return; 
       
        inorder(temp.left, string, result); 
        if(temp.key.toLowerCase().contains(string)) {
        	result.add(temp.key);
        }
        inorder(temp.right, string, result); 
    } 
       
    /*function to insert element in binary tree */
    
    public void add(String string) {
    	Node temp = root;
    	insert(temp, string);
    }
    
    public void insert(Node temp, String key) 
    { 
        Queue<Node> q = new LinkedList<Node>(); 
        q.add(temp); 
       
        // Do level order traversal until we find 
        // an empty place.  
        while (!q.isEmpty()) { 
            temp = q.peek(); 
            q.remove(); 
       
            if (temp.left == null) { 
                temp.left = new Node(key); 
                break; 
            } else
                q.add(temp.left); 
       
            if (temp.right == null) { 
                temp.right = new Node(key); 
                break; 
            } else
                q.add(temp.right); 
        } 
    } 
}  