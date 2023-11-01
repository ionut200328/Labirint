import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GenerateGraphFromMatrix {
    private static ArrayList<ArrayList<Integer>> matrix, listaAdiacenta;
      private static String filename = "matrix.txt";
    private static ArrayList<Integer> iesiriInaccessible;
    public static ArrayList<ArrayList<Integer>> bfs(int s, ArrayList<ArrayList<Integer>> listaAdiacenta, ArrayList<ArrayList<Integer>> matrix) {
        int n = listaAdiacenta.size();
        boolean[] visited = new boolean[n];
        int[] distance = new int[n];
        int[] parent = new int[n];

        Queue<Integer> queue = new LinkedList<>();

        for (int i = 0; i < n; i++) {
            visited[i] = false;
            distance[i] = Integer.MAX_VALUE;
            parent[i] = -1;
        }

        visited[s] = true;
        distance[s] = 0;
        queue.add(s);

        while (!queue.isEmpty()) {
            int v = queue.poll();
            for (int u : listaAdiacenta.get(v)) {
                if (!visited[u]) {
                    visited[u] = true;
                    distance[u] = distance[v] + 1;
                    parent[u] = v;
                    queue.add(u);
                }
            }
        }

        ArrayList<ArrayList<Integer>> parcurgeri = new ArrayList<ArrayList<Integer>>();

        // Print all paths from source to exits
        for (int i = 0; i < n; i++) {
            if (isExit(i, matrix)) { // You need to implement this method
                System.out.print("Path from " + (s+1) + " to " + (i+1) + ": ");
                parcurgeri.add(printPath(s, i, parent)); // You need to implement this method
                System.out.println();
            }
        }
        return parcurgeri;
    }
    public static ArrayList<Integer> printPath(int s, int v, int[] parent) {
        ArrayList<Integer> path = new ArrayList<Integer>();
        if (v == s) {
            path.add(s);
            System.out.print((s+1) + " ");
        } else if (parent[v] == -1) {
            System.out.println("No path from " + (s+1) + " to " + (v+1));
            iesiriInaccessible.add(v);
        } else {
            path.addAll(printPath(s, parent[v], parent));
            path.add(v);
            System.out.print((v+1) + " ");
        }
        return path;

    }
    public static boolean isExit(int node, ArrayList<ArrayList<Integer>> matrix) {
        int rows = matrix.size();
        int cols = matrix.get(0).size();
        int row = node / cols;
        int col = node % cols;

        // Check if the node is on the edge and has a value of 1
        if ((row == 0 || row == rows - 1 || col == 0 || col == cols - 1) && matrix.get(row).get(col) == 1) {
            return true;
        }

        return false;
    }
    public static ArrayList<ArrayList<Integer>> getMatrix()
    {
        matrix = readMatrix(filename);
        return matrix;
    }
    public static ArrayList<ArrayList<Integer>> getListaAdiacenta()
    {
        return listaAdiacenta;
    }
    public static ArrayList<Integer> getIesiriInaccessible()
    {
        return iesiriInaccessible;
    }
    //read from file matrix.txt
    private static ArrayList<ArrayList<Integer>> readMatrix(String filename) {
        ArrayList<ArrayList<Integer>> matrix = new ArrayList<ArrayList<Integer>>();
        try {
            List<String>allLines = Files.readAllLines(Paths.get(filename));
            for(String line: allLines) {
                ArrayList<Integer> row = new ArrayList<Integer>();
                for(char c: line.toCharArray()) {
                    row.add(Character.getNumericValue(c));
                }
                matrix.add(row);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return matrix;
    }
    public static ArrayList<ArrayList<Integer>> Parcurgeri(int sursa) {
        listaAdiacenta = new ArrayList<ArrayList<Integer>>();

        for(int i=0; i<matrix.size()*matrix.get(0).size(); i++) {
            listaAdiacenta.add(new ArrayList<Integer>());
        }

        for(int i=0; i<matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).size(); j++) {
                int nr_casuta=i*matrix.get(i).size()+j;
                if(matrix.get(i).get(j)==1) {
                    if (j-1>=0 && matrix.get(i).get(j-1)==1) {
                        listaAdiacenta.get(nr_casuta).add(nr_casuta-1);
                    }
                    if (j+1<matrix.get(i).size() && matrix.get(i).get(j+1)==1) {
                        listaAdiacenta.get(nr_casuta).add(nr_casuta+1);
                    }
                    if (i-1>=0 && matrix.get(i-1).get(j)==1) {
                        listaAdiacenta.get(nr_casuta).add(nr_casuta-matrix.get(i).size());
                    }
                    if (i+1<matrix.size() && matrix.get(i+1).get(j)==1) {
                        listaAdiacenta.get(nr_casuta).add(nr_casuta+matrix.get(i).size());
                    }
                }
            }
        }
        //print listaAdiacenta
        /*for(int i=0; i<listaAdiacenta.size(); i++) {
            System.out.print(i + ": ");
            for(int j=0; j<listaAdiacenta.get(i).size(); j++) {
                System.out.print(listaAdiacenta.get(i).get(j) + " ");
            }
            System.out.println();
        }*/

        ArrayList<ArrayList<Integer>> parcurgeri = new ArrayList<ArrayList<Integer>>();
        iesiriInaccessible = new ArrayList<Integer>();

        /*for (int i = 0; i < listaAdiacenta.size(); i++) {
            if (matrix.get(i / matrix.get(0).size()).get(i % matrix.get(0).size()) == 1) {
                parcurgeri.addAll(bfs(i, listaAdiacenta, matrix));
            }
        }*/

        parcurgeri.addAll(bfs(sursa, listaAdiacenta, matrix));
        return parcurgeri;
    }

    public static int pickRandomNode() {
        int rows = matrix.size();
        int cols = matrix.get(0).size();
        int node = (int) (Math.random() * (rows * cols));
        int row = node / cols;
        int col = node % cols;

        // Check if the node is on the edge and has a value of 1
        while(matrix.get(row).get(col) != 1) {
            node = (int) (Math.random() * (rows * cols));
            row = node / cols;
            col = node % cols;
        }


        return node;
    }
}
