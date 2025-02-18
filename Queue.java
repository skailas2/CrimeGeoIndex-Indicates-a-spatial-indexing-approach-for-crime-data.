public class Queue<T> {
    private static class Node<T> {
        T data;
        Node<T> next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> front, rear;

    public Queue() {
        front = rear = null;
    }

    public void enqueue(T item) {
        Node<T> newNode = new Node<>(item);
        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
    }

    public T dequeue() {
        if (front == null) return null;
        T item = front.data;
        front = front.next;
        if (front == null) rear = null;
        return item;
    }

    public boolean isEmpty() {
        return front == null;
    }
}
