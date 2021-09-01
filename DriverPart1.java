public class DriverPart1 {
    public static void main(String... strings) {
        ArrayList<Integer> arr = new ArrayList<>();
        ArrayList<Position<Integer, String>> posArray = new ArrayList<>();
        ArrayList<Position<Integer, String>> arrayForInsertion = new ArrayList<>();
        LinkedHeapPriorityQueue<Integer, String> heap = new LinkedHeapPriorityQueue<>();

        for (int i = 0; i < 4; i++)
            arr.add(i,i);

        int i = 0, j = 0, k = 0;
        while (i < arr.size()) {
            posArray.add(i,heap.createNode(arr.get(i), null, null, null, null));
            i++;
        }

        while (k < arr.size()) {
            heap.insert(posArray.get(k).getKey(), posArray.get(k).getValue());
            k++;
        }

        heap.heapify(posArray); // yukaridaki position arraylistlerindeki elemanlari ekledikten sonra çagirdim.

        arrayForInsertion.add(0,heap.createNode(89, null, null, null, null));
        arrayForInsertion.add(1,heap.createNode(-1, null, null, null, null));
        arrayForInsertion.add(2,heap.createNode(68, null, null, null, null));
        arrayForInsertion.add(3,heap.createNode(-35, null, null, null, null));
        arrayForInsertion.add(4,heap.createNode(-48, null, null, null, null));

        while (j < arrayForInsertion.size()) { //yukaridaki elemanlar yeni ekleniyor
            heap.insert(arrayForInsertion.get(j).getKey(), arrayForInsertion.get(j).getValue());
            j++;
        }

        heap.printPreOrder(heap.root);
        System.out.println();
        System.out.println("LAST NODE: " + "KEY-> " + heap.lastNode().getKey());
        System.out.println("Current min: " + heap.min().getKey());

        heap.removeMin();
        heap.printPreOrder(heap.root);
        System.out.println();
        System.out.println("Current min: " + heap.min().getKey());
        System.out.println("LAST NODE: " + "KEY-> " + heap.lastNode().getKey());

        heap.insert(77, "");
        heap.printPreOrder(heap.root);
        System.out.println();
        System.out.println("LAST NODE: " + "KEY-> " + heap.lastNode().getKey());
        System.out.println("DONE.");

    }
}
