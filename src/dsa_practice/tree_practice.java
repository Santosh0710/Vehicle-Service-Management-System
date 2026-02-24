package dsa_practice;



class TreeNode{
    int data;
    TreeNode left , right;
    TreeNode(int data){
        this.data = data;
        left = right = null;
    }
}
class BinaryTree{
//    Build Tree
    TreeNode root;
    void insertRoot(int data){

        root = new TreeNode(data);
    }
    void insertLeft(TreeNode parent , int data)
    {
        parent.left = new TreeNode(data);
    }
    void insertRight(TreeNode parent , int data)
    {

        parent.right = new TreeNode(data);

    }

    // Traverse Tree
    void inOrder(TreeNode node){
        if(node == null) return;
        inOrder(node.left);
        System.out.print(node.data + " ");
        inOrder(node.right);
    }
    void preOrder(TreeNode node){
        if(node == null) return;
        System.out.print(node.data + " ");
        preOrder(node.left);
        preOrder(node.right);
    }
    void postOrder(TreeNode node){
        if(node == null) return;
        postOrder(node.left);
        postOrder(node.right);
        System.out.print(node.data + " ");
    }
}
public class tree_practice {
    public static void main(String[] args) {
        BinaryTree tree = new BinaryTree();
        tree.insertRoot(50);

        tree.insertLeft(tree.root , 30);
        tree.insertRight(tree.root,70);

        tree.insertLeft(tree.root.left, 20);
        tree.insertRight(tree.root.left , 40);

        tree.insertLeft(tree.root.right, 60);
        tree.insertRight(tree.root.right, 80);

        System.out.println("Binary Tree Created Successfully");

        System.out.println("Inorder Traversal");
        tree.inOrder(tree.root);
        System.out.println();
        System.out.println("preorder Traversal");
        tree.preOrder(tree.root);
        System.out.println();
        System.out.println("Postorder Traversal");
        tree.postOrder(tree.root);
    }
}
