/**
 Клас MyHeapMin створений для реалізації структури типу Heap, те кореневий елемент це
 найменший елемент, який знаходиться в Heap
 Клас має обмежений функціонал, та спроектований суто для вирішення конкретного завдання.
 */

public class MyHeapMin extends AbstractHeap {
    @Override
    protected void bubbleElement(int index) {
        if (index == 0) return;
        int parentIndex = (index - 1) / 2;
        if (array[parentIndex] >= array[index]) {
            int tmp = array[parentIndex];
            array[parentIndex] = array[index];
            array[index] = tmp;
            bubbleElement(parentIndex);
        }
    }

    @Override
    protected void popElement(int index) {
        if (index >= size) {
            throw new IllegalArgumentException("Index out of bounds");
        }

        if (2 * index + 1 < size) {
            int leftChildIndex = 2 * index + 1;
            int rightChildIndex = 2 * index + 2;

            if (rightChildIndex < size && array[rightChildIndex] < array[leftChildIndex]) {
                array[index] = array[rightChildIndex];
                popElement(rightChildIndex);
            } else {
                array[index] = array[leftChildIndex];
                popElement(leftChildIndex);
            }
        } else {
            array[index] = array[size - 1];
            size--;
            bubbleElement(index);
        }
    }
}
