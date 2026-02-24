package dsa_practice;

public class DynamicArray
{
    private int[] arr;
    private int size;
//    constructor creates initial array
    public DynamicArray(){
        arr = new int[2]; // start with size 2
        size = 0;
    }
    public void add(int value){
//        of array full , create bigger array
        if(size == arr.length){
            grow();
        }
        arr[size] = value;
        size++;
    }
//    grow method
    public void grow(){
        int []newArr = new int[arr.length * 2];
        for(int i = 0 ; i< size ; i++){
            newArr[i] = arr[i];
        }
        arr = newArr;
    }

//     display elements
    public void displayInfo(){
        for(int i= 0 ; i< size;i++){
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        DynamicArray da = new DynamicArray();
        da.add(10);
        da.add(20);
        da.add(30);

        da.displayInfo();
    }
}
