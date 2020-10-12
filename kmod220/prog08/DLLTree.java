package prog08;
import java.util.*;

import prog07.BST.Node;

public class DLLTree <K extends Comparable<K>, V>
  extends AbstractMap<K, V> {

  private class Node <K extends Comparable<K>, V>
    implements Map.Entry<K, V> {
    K key;
    V value;
  
    Node left, right;
    int size = 1; // size of subtree with this node as root

    Node previous, next;
    
    Node (K key, V value) {
      this.key = key;
      this.value = value;
    }

    public K getKey () { return key; }
    public V getValue () { return value; }
    public V setValue (V newValue) {
      V oldValue = value;
      value = newValue;
      return oldValue;
    }
  }
  
  private Node theRoot, tail, head;

  public int size () { 
    if (theRoot == null)
      return 0;
    return theRoot.size;
  }

  /**
   * Find the node with the given key.
   * @param key The key to be found.
   * @return The node with that key.
   */
  private Node<K, V> find (K key, Node<K,V> root) {
    // EXERCISE
    // return null; // not correct
	  
	  if(root == null)
	  {
		  return null;
	  }
	  int compare = key.compareTo(root.key);
	  if(compare == 0)
	  {
		  return root;
	  }
	  else if(compare < 0)
	  {
		  return find(key, root.left);
	  }
	  else
	  {
		  return find(key, root.right);
	  }

  }    

  public boolean containsKey (Object key) {
    return find((K) key, theRoot) != null;
  }
  
  public V get (Object key) {
    Node<K, V> node = find((K) key, theRoot);
    if (node != null)
      return node.value;
    return null;
  }
  
  public boolean isEmpty () { return theRoot == null; }

  /**
   * Add node between previous and next in the DLL.
   */
  private void addDLL (Node<K,V> node, Node<K,V> previous, Node<K,V> next) {
    // EXERCISE
	  node.next = next;
	  node.previous = previous;
	  
	  if(previous == null)
	  {
		  head = node;
	  }
	  else
	  {
		  previous.next = node;
	  }
	  
	  if(next == null)
	  {
		  tail = node;
	  }
	  else
	  {
		  next.previous = node;
	  }

  }

  /**
   * Add key,value pair to tree rooted at root.
   * Return root of modified tree.
   */
  private Node<K,V> add (K key, V value, Node<K,V> root) {
    // EXERCISE
	  if(root == null)
	  {
		  return new Node<K, V>(key, value);
	  }
	  int compare = key.compareTo(root.key);
	  if(compare < 0)
	  {
		  root.left = add(key, value, root.left);
		  if(root.left.next == null)
		  {
			  addDLL(root.left, root.previous, root);
		  }
	  }
	  else
	  {
		  root.right = add(key, value, root.right);
		  if(root.right.previous == null)
		  {
			  addDLL(root.right, root, root.next);
		  }
	  }
	  
	root.size++;
	
	if(size(root.right) > size(root)*2/3 || size(root.left) > size(root)*2/3 )
	{
		return rebalance(getMinimum(root), size(root));
		
	}
    return root;
  }
  
  /**
   * Return the size of the tree rooted at root, using root.size if it
   * is not an empty tree.
   */
  private int size (Node<K,V> root) {
    if (root == null)
      return 0;
    return root.size;
  }

  /**
   * Return the root of a balanced tree made from the nodes in a DLL
   * with head head and size elements.
   */
  private Node<K,V> rebalance (Node<K,V> head, int size) {
    // EXERCISE
//calls rebalanceLeft and rebalance
//calls itself recursively on the right half (root.right)
//root.next
	
	  Node<K, V> root;


	  if(size == 1)
	  {
		  head.left = null;
		  head.right = null;
		  head.size = 1;
		  return head;
	  }
	  root = rebalanceLeft(head, size-(size/2));
	  root.right = rebalance(root.next, size/2);  
		  
	  root.size = size(root.left) + size(root.right) + 1;

    return root;
  }

  /**
   * Return the left half of a balanced tree made from the nodes in a
   * DLL with head head and size elements: the root and left subtree
   * are correct, but the right subtree is empty.
   */
  private Node<K,V> rebalanceLeft (Node<K,V> head, int size) {
    // EXERCISE
//call rebalanceLeft twice
	  
	  Node<K, V> left;
	  Node<K, V> root;

	  if(size == 1)
	  {
		  head.left = null;
		  head.right = null;
		  head.size = 1;
		  return head;
	  }
		  left = rebalanceLeft(head, size/2);
		  root =  rebalanceLeft(left.next, size-(size/2));
		  left.right = root.left;
		  left.size = size(root.left) + size(left.left) + 1;
		  root.left = left;
		  root.size = size(root.right) + size(left) + 1;


    return root;
  }

  public V put (K key, V value) {
    // EXERCISE
	  Node<K, V> node = find(key, theRoot);
	  if(node != null)
	  {
		 //V save = node.getValue();
		  return node.setValue(value);
	  }
	  else
	  {
		  theRoot = add(key, value, theRoot);
	  }
	  
	  if(theRoot.previous == null)
	  {
		  head = theRoot;
	  }
	  if(theRoot.next == null)
	  {
		  tail = theRoot;
	  }

    return null;

  }      
  
  public V remove (Object keyAsObject) {
    // EXERCISE
    K key = (K) keyAsObject;
    //return null; // not correct
    
    Node<K, V> node = find(key, theRoot);
    if(node != null)
    {
    	V value = node.getValue();
    	theRoot = remove(key, theRoot);
    	return value;
    }
    return null;

  }

  /**
   * Remove the node with key from the tree with root.  Return the
   * root of the resulting tree.
   */
  private Node<K,V> remove (K key, Node<K,V> root) {
    // EXERCISE

	  int compare = key.compareTo(root.key);
	  if(compare == 0)
	    {
	    	return removeRoot(root);
	    }
	    if(compare < 0) 
	    {
	    	root.left = remove(key, root.left);
	    }
	    else
	    {
	    	root.right = remove(key, root.right);
	    }
	    
	  root.size--;
	  if(size(root.right) > size(root)*2/3 || size(root.left) > size(root)*2/3 )
		{
			return rebalance(getMinimum(root), size(root));
		}
    return root;
  }

  /**
   * Remove node from a DLL.
   */
  private void removeDLL (Node<K,V> node) {
    // EXERCISE
	  
	  Node<K, V> previous = node.previous;
	  Node<K, V> next = node.next;
	  
		  if(previous == null)
		  {
			  head = next;
		  }
		  else
		  {
			  previous.next = next;
		  }
		  
		  if(next == null)
		  {
			  tail = previous;
		  }
		  else
		  {
			  next.previous = previous;
		  }
  }

  /**
   * Remove root of tree rooted at root.
   * Return root of a BST of the remaining nodes.
   */
  private Node<K,V> removeRoot (Node<K,V> root) {
    // IMPLEMENT using getMinimum and removeMinimum
    // Node returned by getMinimum becomes the new root.
   // return null; // not correct

	  removeDLL(root);
	  
	  if(root.left == null)
	  {
		  return root.right;
	  }
	  if(root.right == null)
	  {
		  return root.left;
	  }
	 
	  Node newRoot = getMinimum(root.right);
	  root.right = removeMinimum(root.right);
	  newRoot.right = root.right;
	  newRoot.left = root.left;
	  root = newRoot;

    return root;

  }


  Node getMinimum(Node root)
  {
	  if(root.left == null)
	  {
		  return root;
	  }
	  
	  return getMinimum(root.left);
  }
  
  Node removeMinimum(Node root)
  {
	  if(root.left == null)
	  {
		  return root.right;
	  }
	  root.left = removeMinimum(root.left);
	  return root;
  }


  protected class Iter implements Iterator<Map.Entry<K, V>> {
    Node<K, V> next;
    
    Iter () {
      next = head;
    }
    
    public boolean hasNext () { return next != null; }
    
    public Map.Entry<K, V> next () {
      Map.Entry<K, V> ret = next;
      next = next.next;
      return ret;
    }
    
    public void remove () {
      throw new UnsupportedOperationException();
    }
  }
  
  protected class Setter extends AbstractSet<Map.Entry<K, V>> {
    public Iterator<Map.Entry<K, V>> iterator () {
      return new Iter();
    }
    
    public int size () { return DLLTree.this.size(); }
  }
  
  public Set<Map.Entry<K, V>> entrySet () { return new Setter(); }
  
  public String toString () {
    return toString(theRoot, 0);
  }
  
  private String toString (Node root, int indent) {
    if (root == null)
      return "";
    String ret = toString(root.right, indent + 2);
    for (int i = 0; i < indent; i++)
      ret = ret + "  ";
    ret = ret + root.key + ":" + root.value + ":" + root.size + "\n";
    ret = ret + toString(root.left, indent + 2);
    return ret;
  }

  public String listString () {
    if (head == null)
      return "";
    String s = head.key + ":" + head.value;
    for (Node<K,V> node = head.next; node != null; node = node.next)
      s = s + " " + node.key + ":" + node.value;
    return s;
  }

  public static void main (String[] args) {
    DLLTree<Character, Integer> tree = new DLLTree<Character, Integer>();
    String s = "abcdefghijklmnopqrstuvwxyz";
    
    for (int i = 0; i < s.length(); i++) {
      tree.put(s.charAt(i), i);
      System.out.print(tree);
      System.out.println("-------------");
    }

    System.out.println("list: " + tree.listString());
    System.out.println("-------------");

    for (int i = 0; i < s.length(); i++) {
      tree.remove(s.charAt(i));
      System.out.print(tree);
      System.out.println("-------------");
    }

    System.out.println("list: " + tree.listString());
  }
}
