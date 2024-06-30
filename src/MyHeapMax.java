/**
 Клас MyHeapMax створений для реалізації структури типу Heap, те кореневий елемент це
 найбільший елемент, який знаходиться в Heap
 Клас має обмежений функціонал, та спроектований суто для вирішення конкретного завдання.
 */

public class MyHeapMax extends AbstractHeap {
    @Override
    protected void bubbleElement(int index) {
        if (index == 0) return;
        int parentIndex = (index - 1) / 2;
        if (array[parentIndex] <= array[index]) {
            int tmp = array[parentIndex];
            array[parentIndex] = array[index];
            array[index] = tmp;
            bubbleElement(parentIndex);
        }
    }

    @Override
    protected void popElement(int index) {
        if (index * 2 + 1 < size) {
            int leftChildIndex = index * 2 + 1;
            int rightChildIndex = index * 2 + 2;
            if (rightChildIndex < size && array[leftChildIndex] > array[rightChildIndex]) {
                array[index] = array[leftChildIndex];
                popElement(leftChildIndex);
            } else {
                array[index] = array[rightChildIndex];
                popElement(rightChildIndex);
            }
        } else {
            array[index] = array[size - 1];
            size--;
            bubbleElement(index);
        }
    }
}
