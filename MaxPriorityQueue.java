public class MaxPriorityQueue<K, V> implements PriorityQueueMax<K, V> {

    private class Node<K, V> implements Position<K, V> {
        protected K key;
        protected V value;// an element stored at this node
        private Node<K, V> parent; // a reference to the parent node (if any)
        private Node<K, V> left; // a reference to the left child (if any)
        private Node<K, V> right; // a reference to the right child (if any)
        private Node<K, V> successor;
        private Node<K, V> predecessor;


        public Node(K k, V v, Node<K, V> above, Node<K, V> leftChild, Node<K, V> rightChild) {
            key = k;
            value = v;
            parent = above;
            left = leftChild;
            right = rightChild;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public Node<K, V> getParent() {
            return parent;
        }

        public Node<K, V> getLeft() {
            return left;
        }

        public Node<K, V> getRight() {
            return right;
        }

        public Node<K, V> getSuccessor() { return successor;}

        public void setSuccessor(Node<K, V> node) { successor = node; }

        public Node<K, V> getPredecessor() { return predecessor;}

        public void setPredecessor(Node<K, V> node) { predecessor = node; }

        public void setKey(K k) {
            key = k;
        }

        public void setValue(V v) {
            value = v;
        }

        public void setParent(Node<K, V> parentNode) {
            parent = parentNode;
        }

        public void setLeft(Node<K, V> leftChild) {
            left = leftChild;
        }

        public void setRight(Node<K, V> rightChild) {
            right = rightChild;
        }
    }

    protected Node<K, V> root = null;
    protected Node<K, V> lastNode = null;
    private int size = 0, depth = 0;

    public MaxPriorityQueue() {}

    protected Node<K, V> createNode(K k, V v, Node<K, V> parent, Node<K, V> left, Node<K, V> right) {
        return new Node<K, V>(k, v, parent, left, right);
    }

    protected Node<K, V> validate(Position<K, V> p) throws IllegalArgumentException {
        if (!(p instanceof Node))
            throw new IllegalArgumentException("Not valid position type");
        Node<K, V> node = (Node<K, V>) p; // safe cast
        if (node.getParent() == node) // our convention for defunct node
            throw new IllegalArgumentException("p is no longer in the tree");
        return node;
    }

    public Position<K, V> addRoot(K k, V v) throws IllegalStateException {
        if (!isEmpty())
            throw new IllegalStateException("Root is not empty");
        root = createNode(k, v, null, null, null);
        size = 1;
        depth++;
        lastNode = root;
        System.out.println("Yeni eklendi: " + lastNode.getKey());
        return root;

    }

    public Position<K, V> addLeft(Position<K, V> p, K k, V v) throws IllegalArgumentException {
        Node<K, V> node= validate(p);
        Node<K, V> child = createNode(k, v, node, null, null);
        node.setLeft(child);
        size++;


        if(node.getSuccessor() != null) {
            node.getSuccessor().setPredecessor(null);
            node.setSuccessor(null);
        }

        if(lastNode != root) {
            lastNode.setSuccessor(child);
            child.setPredecessor(lastNode);


            Node<K, V> nextSuccessor = lastNode, copy = nextSuccessor;
                while (nextSuccessor.getPredecessor() != null && nextSuccessor.getPredecessor() != copy)
                    nextSuccessor = nextSuccessor.getPredecessor();
                child.setSuccessor(nextSuccessor);
            }

        lastNode =  child;
        System.out.println("Yeni eklendi: " + lastNode.getKey());
        return child;
    }

    public Position<K, V> addRight(Position<K, V> p, K k, V v) throws IllegalArgumentException {
        Node<K, V> node= validate(p);
        Node<K, V> child = createNode(k, v, node, null, null);
        node.setRight(child);
        size++;

        child.setSuccessor(lastNode.getSuccessor());
        node.getLeft().setSuccessor(child);
        child.setPredecessor(node.getLeft());

        if(size == (Math.pow(2,depth+1) -1)) depth++;

        lastNode = child;
        System.out.println("Yeni eklendi: " + lastNode.getKey());
        return child;
    }

    protected Position<K, V> parent(Position<K, V> p) {
        Node<K, V> node = validate(p);
        return node.getParent();
    }

    protected Position<K, V> left(Position<K, V> p) {
        Node<K, V> node = validate(p);
        return node.getLeft();
    }

    protected Position<K, V> right(Position<K, V> p) {
        Node<K, V> node = validate(p);
        return node.getRight();
    }

    protected Position<K, V> sibling(Position<K, V> p) {
        Node<K, V> node = validate(p);
        if(node.getParent().getRight() == node) return null;

        return node.getParent().getRight();
    }

    protected boolean hasLeft(Position<K, V> p) {
        Node<K, V> node = validate(p);
        if (null != node.getLeft())
            return true;
        return false;
    }

    protected boolean hasRight(Position<K, V> p) {
        Node<K, V> node = validate(p);
        if (null != node.getRight())
            return true;
        return false;
    }

    protected boolean checkKey(K key) throws IllegalArgumentException {
        try {
            return (this.compare(key, key) == 0); // see if key can be compared to itself

        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Incompatible key");
        }
    }

    public int compare(K k1, K k2) throws ClassCastException {
        return ((Comparable<K>) k1).compareTo(k2);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return null == root;
    }

    @Override
    public Position<K, V> insert(K key, V value) throws IllegalArgumentException {
        Node<K, V> temp = null;

        checkKey(key);

        if (this.isEmpty()) {
            this.addRoot(key, value);
            return root;
        } else {
            if (lastNode != root && lastNode.getParent().getLeft() == lastNode) { // eger yeni eklenecek node son eklenin sag kardesi ise
                temp = lastNode.getParent();

            } else {
                if (size == (Math.pow(2, depth) - 1)) { // eger yeni levela geciyorsak
                    Node<K, V> node = lastNode;
                    while (node.getPredecessor() != null) // yeni levela gecmek icin en sol leaf araniyor
                        node = node.getPredecessor();

                    temp = node;

                } else { // eger son eklenmis node sagda ise yukari cikip, yeni parent nodu bulup elimizdeki nodeu onun soluna ekleyecegiz
                    Node<K, V> node = lastNode;
                    while (temp == null) {
                        if (sibling(node.getParent()) == null){
                            node = node.getParent();
                        }else{
                            node = (Node<K, V>) sibling(node.getParent());
                            while (hasLeft(node))
                                node = node.getLeft();

                            temp = node;
                        }
                    }
                }
            }
        }

        if (!this.hasLeft(temp)) {
            Node<K, V> newest = (Node<K, V>) this.addLeft(temp, key, value);
            upheap(newest);
            return lastNode;
        } else if (!this.hasRight(temp)) {
            Node<K, V> newest = (Node<K, V>) this.addRight(temp, key, value);
            upheap(newest);
            return lastNode;
        }
        return null;

    }

    @Override
    public Position<K, V> max() {
        return root;
    }

    public Position<K, V> lastNode() { return lastNode;}

    protected void downheap(Position<K, V> p) {
        Node<K, V> nodeP;
        while (hasLeft(p)) { // continue to bottom (or break statement)
            nodeP = validate(p);
            Node<K, V> leftNode = nodeP.getLeft();
            Node<K, V> bigChildNode = leftNode; // although right may be bigger
            if (hasRight(p)) {
                Node<K, V> rightNode = nodeP.getRight();
                if (compare(leftNode.getKey(), rightNode.getKey()) < 0)
                    bigChildNode = rightNode; // right child is bigger
            }
            if (compare(bigChildNode.getKey(), p.getKey()) <= 0)
                break; // heap property has been restored
            swap(p, bigChildNode);
            p = bigChildNode; // continue at position of the child
        }
    }

    protected void upheap(Position<K, V> p) {
        Node<K, V> node = validate(p);
        Node<K, V> parentOfP;

        while (node != root) { // continue until reaching root (or break statement)
            parentOfP = node.getParent();
            if (compare(node.getKey(), parentOfP.getKey()) <= 0)
                break; // heap property verified
            swap(node, parentOfP);
            node = parentOfP; // continue from the parent's location
        }
    }
    
    protected void swap(Position<K, V> p1, Position<K, V> p2) {
        K temp = validate(p1).getKey();
        validate(p1).setKey(p2.getKey());
        validate(p2).setKey(temp);
    }

    @Override
    public Position<K, V> removeMax() {
        if (isEmpty())
            return null;

        Node<K, V> answer = root;
        swap(root,lastNode); // put maximum item at the end
        Node<K, V> toBeRemoved = validate(lastNode);
        Node<K, V> newLastNode = toBeRemoved.getParent();

        if (toBeRemoved.getParent().getLeft() == lastNode) {
            toBeRemoved.getParent().setLeft(null);
        } else {
            toBeRemoved.getParent().setRight(null);
        }

        toBeRemoved.getPredecessor().setSuccessor(null);
        toBeRemoved.setPredecessor(null);

        downheap(root); // then fix new root
        lastNode = newLastNode.getLeft() == null ? newLastNode : newLastNode.getLeft();
        lastNode.setSuccessor(toBeRemoved.getSuccessor());
        toBeRemoved.setParent(toBeRemoved); // defunct
        size--;
        if(size == (Math.pow(2,depth) -1)) depth--;

        return toBeRemoved;
    }

    protected void heapify(ArrayList<Position<K, V>> arr) {
        heapifyAux(arr, size() - 1);
    }

    protected ArrayList<Position<K, V>> heapifyAux(ArrayList<Position<K, V>> arr, int j) {
        if (j < 0 || j == size() - 1)
            return arr;
        downheap(arr.get(j));
        return heapifyAux(arr, j - 1);
    }

    public void printPreOrder(Position<K, V> p) {
        Node<K,V> pos = (Node<K,V>) p;

        System.out.println("P " + p.getKey());
        if(((Node<K, V>) p).getSuccessor() != null) System.out.println(p.getKey() +" references to " + ((Node<K, V>) p).getSuccessor().getKey());

        if(hasLeft(pos))
            printPreOrder(pos.getLeft());
        if(hasRight(pos))
            printPreOrder(pos.getRight());
    }

}
