import java.util.StringJoiner;

public abstract class AbstractHeap {
    protected int[] array;
    protected int size;
    protected int maxTreeLevel;

    public AbstractHeap() {
        this.array = new int[0];
        this.size = 0;
        this.maxTreeLevel = 0;
    }

    public void addElement(int element) {
        if (array.length <= size) {
            int[] newArray = new int[(int) (array.length + (Math.pow(2, maxTreeLevel)))];
            System.arraycopy(array, 0, newArray, 0, array.length);
            array = newArray;
            maxTreeLevel++;
        }
        size++;
        array[size - 1] = element;
        bubbleElement(size - 1);
    }

    protected abstract void bubbleElement(int index);

    protected abstract void popElement(int index);

    @Override
    public String toString() {
        StringJoiner stringJoiner = new StringJoiner(", ");
        for (int i = 0; i < size; i++) {
            stringJoiner.add(Integer.toString(array[i]));
        }
        return stringJoiner.toString();
    }

    public int getSize() {
        return size;
    }

    public int getHead() {
        int result = array[0];
        popElement(0);
        return result;
    }
}
