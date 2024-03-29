public class Huffman {
    private static final int R = 256;

    private Huffman() { }

    private static class Node implements Comparable<Node> {
        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch    = ch;
            this.freq  = freq;
            this.left  = left;
            this.right = right;
        }
        private boolean isLeaf() { return (left == null) && (right == null); }
        public int compareTo(Node that) { return this.freq - that.freq; }
    }

    public static void expand() {
        Node root = readTrie();
        int N = BinaryStdIn.readInt();
        for (int i = 0; i < N; i++) {
            Node x = root;
            while (!x.isLeaf()) {
                if (!BinaryStdIn.readBoolean())
                    x = x.left;
                else
                    x = x.right;
            }
            BinaryStdOut.write(x.ch, 8);
        }
        BinaryStdOut.close();
    }

    private static void writeTrie(Node x) {
        if (x.isLeaf()) {
            BinaryStdOut.write(true);
            BinaryStdOut.write(x.ch, 8);
            return;
        }
        BinaryStdOut.write(false);
        writeTrie(x.left);
        writeTrie(x.right);
    }

    private static Node readTrie() {
        if (BinaryStdIn.readBoolean())
            return new Node(BinaryStdIn.readChar(), -1, null, null);
        else {
            Node x = readTrie();
            Node y = readTrie();
            return new Node('\0', 0, x, y);
        }
    }

    private static Node buildTrie(int[] freq) {
        MinPQ<Node> pq = new MinPQ<Node>();
        for (char i = 0; i < R; i++)
            if (freq[i] > 0)
                pq.insert(new Node(i, freq[i], null, null));

        while (pq.size() > 1) {
            Node x  = pq.delMin();
            Node y = pq.delMin();
            Node parent = new Node('\0', x.freq + y.freq, x, y);
            pq.insert(parent);
        }
        return pq.delMin();
    }

    public static void compress() {
        String s = BinaryStdIn.readString();
        char[] input = s.toCharArray();
        int[] freq = new int[R];
        for (int i = 0; i < input.length; i++)
            freq[input[i]]++;
        Node root = buildTrie(freq);
        String[] st = new String[R];
        buildCode(st, root, "");
        writeTrie(root);
        BinaryStdOut.write(input.length);
        for (int i = 0; i < input.length; i++) {
            String code = st[input[i]];
            for (int j = 0; j < code.length(); j++) {
                if (code.charAt(j) == '0') {
                    BinaryStdOut.write(false);
                }
                else if (code.charAt(j) == '1') {
                    BinaryStdOut.write(true);
                }
                else throw new IllegalStateException("Illegal state");
            }
        }
        BinaryStdOut.close();
    }

    public static void printOriginalByteSize() {
        String s = BinaryStdIn.readString();
        char[] input = s.toCharArray();
        BinaryStdOut.write(input.length);
        BinaryStdOut.close();
    }

    private static void buildCode(String[] st, Node x, String s) {
        if (!x.isLeaf()) {
            buildCode(st, x.left,  s + '0');
            buildCode(st, x.right, s + '1');
        }
        else {
            st[x.ch] = s;
        }
    }

    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else printOriginalByteSize();


    }
}
