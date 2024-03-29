import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;

public class NFA {
    private char[] re;
    private Digraph G;
    private int M;

    public NFA(String regexp) {
        M = regexp.length();
        re = regexp.toCharArray();
        G = buildEpsilonTransitionDigraph();
    }
    public boolean recognizes(String txt) {
        Bag<Integer> pc = new Bag<Integer>();
        DirectedDFS dfs = new DirectedDFS(G, 0);
        for (int v = 0; v < G.V(); v++)
            if (dfs.marked(v)) pc.add(v);
        for (int i = 0; i < txt.length(); i++) {
            Bag<Integer> match = new Bag<Integer>();
            for (int v : pc) {
                if (v == M) continue;
                if ((re[v] == txt.charAt(i)) || re[v] == '.')
                    match.add(v+1);
            }

            dfs = new DirectedDFS(G, match);
            pc = new Bag<Integer>();
            for (int v = 0; v < G.V(); v++)
                if (dfs.marked(v)) pc.add(v);
        }
        for (int v : pc)
            if (v == M) return true;
        return false;
    }
    public Digraph buildEpsilonTransitionDigraph() {
        Digraph G = new Digraph(M+1);
        Stack<Integer> ops = new Stack<Integer>();
        for (int i = 0; i < M; i++) {
            int lp = i;
            if (re[i] == '(' || re[i] == '|') ops.push(i);
            else if (re[i] == ')') {
                int or = ops.pop();
                if (re[or] == '|') {
                    lp = ops.pop();
                    G.addEdge(lp, or+1);
                    G.addEdge(or, i);
                } else lp = or;
            }
            if (i < M-1 && re[i+1] == '*') {
                G.addEdge(lp, i+1);
                G.addEdge(i+1, lp);
            }
            if (re[i] == '(' || re[i] == '*' || re[i] == ')')
                G.addEdge(i, i+1);
        }
        return G;
    }

    public static void main(String[] args) {
        try {
            File file = new File("src/InputNFA.txt");
            Scanner in = new Scanner(file);
            String re = in.next();
            String text = in.next();
            NFA nfa = new NFA(re);
            FileWriter fw = new FileWriter("src/OutputNFA.txt");
            PrintWriter pw = new PrintWriter(fw);
            if (nfa.recognizes(text)) pw.println("True");
            else pw.println("False");
            pw.close();
            fw.close();
            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
