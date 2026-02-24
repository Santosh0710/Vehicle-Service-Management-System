package dsa_practice;
class MyStack{
    Node Top;
    void push(int data){
        Node newNode = new Node(data);
        newNode .next = Top;
        Top = newNode;
    }
    void pop(){
        if(Top == null){
            System.out.println("Stack Empty");
            return;
        }
        System.out.println("Popped : "+ Top.data);
        Top = Top.next;
    }
    void display(){
        Node temp = Top;
        System.out.println("Stack ---");
        while(temp!=null){
            System.out.print(temp.data + " ");
            temp = temp.next;
        }
        System.out.println();
    }
}
class MyQueue{
    Node front, rear;
    void enqueue(int data){
        Node newNode = new Node(data);
        if(rear == null){
            front = rear = newNode;
        }
        rear.next = newNode;
        rear = newNode;
    }
    void dequeue(){
        if(front == null){
            System.out.println("Queue is empty");
            return;
        }
        System.out.println("Dequeued : "+ front.data);
        front = front.next;
        if(front == null){
            rear = null;
        }
    }
    void display(){
        Node temp = front;
        System.out.println("Queue: ");
        while(temp!=null){
            System.out.print(front.data + " ");
            temp = temp.next;
        }
        System.out.println();
    }
}

public class stackAndQueue
{
    public static void main(String[] args) {
//        STACK DEMO
        MyStack stack = new MyStack();
        stack.push(10);
        stack.push(20);
        stack.push(30);
        stack.display();
        stack.pop();
        stack.display();

//        Queue Demo
        MyQueue queue = new MyQueue();
        queue.enqueue(100);
        queue.enqueue(200);
        queue.enqueue(300);
        queue.display();
        queue.dequeue();
        queue.display();
    }
}
