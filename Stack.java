public class Stack<T> {
    private static class Node<T> {
        T data;
        Node<T> next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> top;

    public Stack() {
        top = null;
    }

    public void push(T item) {
        Node<T> newNode = new Node<>(item);
        newNode.next = top;
        top = newNode;
    }

    public T pop() {
        if (top == null) return null;
        T item = top.data;
        top = top.next;
        return item;
    }

    public boolean isEmpty() {
        return top == null;
    }
}
