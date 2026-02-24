package dsa_practice;

public class BST
{
   TreeNode root;
   TreeNode insert(TreeNode node , int data)
   {
       if(node == null){
           return new TreeNode(data);
       }
       if(data < node.data){
           node.left = insert(node.left , data);
       }
       else{
           node.right = insert(node.right,data);
       }
       return node;
   }
   void insertValue(int data)
   {
       root = insert(root , data);
   }
   boolean search(TreeNode node , int key)
   {
       if(node == null){
           return false;
       }
       if(node.data == key){
           return true;
       }
       if(key< node.data){
           return search(node.left , key);
       }
       else
       {
           return search(node.right , key);
       }
   }
   TreeNode findMin(TreeNode node)
   {
       while(node.left!=null)
       {
           node = node.left;
       }
       return node;
   }
   TreeNode delete(TreeNode node , int key)
   {
//       if node is empty
       if(node == null)
       {
           return null;
       }

       if(key < node.data)
       {
           node.left = delete(node.left , key);
       }
       else if(key > node.data)
       {
           node.right = delete(node.right , key);
       }
       else
       {
//           node found
//           case 1 : no child
           if(node.left == null && node.right == null)
           {
               return null;
           }
//           case 2 : one child
           if(node.left == null)
           {
               return node.right;
           }
           if(node.right == null)
           {
               return node.left;
           }
//           Two Children
           TreeNode successor = findMin(node.right);
           node.data = successor.data;
           node.right = delete(node.right , successor.data);
       }
       return node;
   }
    void inOrder(TreeNode node)
    {
        if(node == null) return;
        inOrder(node.left);
        System.out.print(node.data + " ");
        inOrder(node.right);
    }

    public static void main(String[] args)
    {
        BST tree = new BST();
//      Inserting the value in the tree
        tree.insertValue(50);
        tree.insertValue(30);
        tree.insertValue(70);
        tree.insertValue(20);
        tree.insertValue(40);
        tree.insertValue(60);
        tree.insertValue(80);
//        searching the key in tree
        System.out.println("Search 60 : " + tree.search(tree.root , 60));
        System.out.println("Search 25: " + tree.search(tree.root , 25));
        //traversing the tree
        System.out.println("Inorder Traversal");
        tree.inOrder(tree.root);

        System.out.println();

//       deleting the key from the tree
        tree.root = tree.delete(tree.root , 20); // leaf node
//        tree.root = tree.delete(tree. root , 30); // one child
//        tree.root = tree.delete(tree.root, 50); // two children

//        again traversing the tree
        System.out.println("Again traversing the tree");
        tree.inOrder(tree.root);

        System.out.println();

    }
}
