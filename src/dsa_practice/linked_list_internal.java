package dsa_practice;
class Node{
    int data;
    Node next;
    public Node(int data){
        this.data = data;
        this.next = null;
    }
}
class MyLinkedList{
    Node head;
//    Add new node at the end
    void add(int data){
        Node newNode = new Node( data);
        if(head == null){
            head = newNode;
            return;
        }
        Node temp = head;
        while(temp.next !=null){
            temp = temp.next;
        }
        temp.next = newNode;
    }

    void display(){
        Node temp = head;
        while(temp != null){
            System.out.print(temp.data + "-->");
            temp = temp.next;
        }
        System.out.println("null");
    }
}
public class linked_list_internal
{
    public static void main(String[] args) {
        MyLinkedList list = new MyLinkedList();
        list.add(10);
        list.add(20);
        list.add(30);
        list.display();
    }
}
