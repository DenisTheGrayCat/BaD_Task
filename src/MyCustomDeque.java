import java.util.StringJoiner;

public class MyCustomDeque {
    private Node head;
    private Node tail;
    private int size;

    public MyCustomDeque() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public void addToTail(int element) {
        Node node = new Node(element);
        if (this.tail != null) {
            this.tail.setTail(node);
            node.setHead(this.tail);
        } else {
            this.head = node;
        }
        this.tail = node;
        this.size++;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public Node getHead() {
        return head;
    }

    public void setHead(Node head) {
        this.head = head;
    }

    public Node getTail() {
        return tail;
    }

    public void setTail(Node tail) {
        this.tail = tail;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void reassignQueue(MyCustomDeque newDeque) {
        this.head = newDeque.getHead();
        this.tail = newDeque.getTail();
        this.size = newDeque.getSize();
        newDeque.setHead(null);
        newDeque.setTail(null);
        newDeque.setSize(0);
    }

    @Override
    public String toString() {
        StringJoiner result = new StringJoiner(", ");
        Node currentNode = this.head;
        while (currentNode != null) {
            result.add(Integer.toString(currentNode.getValue()));
            currentNode = currentNode.getTail();
        }
        return result.toString();
    }


    public class Node {
        private Node head = null;
        private Node tail = null;
        private int value = 0;

        public Node(int data) {
            this.value = data;
        }

        public Node getHead() {
            return head;
        }

        public Node getTail() {
            return tail;
        }

        public int getValue() {
            return value;
        }

        public void setHead(Node head) {
            this.head = head;
        }

        public void setTail(Node tail) {
            this.tail = tail;
        }
    }

}
