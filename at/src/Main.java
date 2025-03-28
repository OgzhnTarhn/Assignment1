import java.util.*;

public class Main {
    static int[] dRow = {-2, -1, 1, 2, 2, 1, -1, -2};
    static int[] dCol = {1, 2, 2, 1, -1, -2, -2, -1};


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.next();

        int rows = Character.getNumericValue(input.charAt(0));
        int cols = Character.getNumericValue(input.charAt(1));

        char[][] board = new char[rows][cols];
        String boardStr = input.substring(2);
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = boardStr.charAt(index++);
            }
        }

        int startRow = -1, startCol = -1;
        int goldRow = -1, goldCol = -1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == 'K') {
                    startRow = i;
                    startCol = j;
                } else if (board[i][j] == 'G') {
                    goldRow = i;
                    goldCol = j;
                }
            }
        }

        Node result = bfs(board, startRow, startCol, goldRow, goldCol);
        if (result == null) {
            System.out.println("No path to the gold");
        } else {
            System.out.println(printPath(result, " -> "));
        }
        sc.close();
    }

    /**
     BFS algoritma mantığını kullanarak bir method yazdım.
     Her düğümde yol bilgisini saklamak yerine parent pointer kullandım.Böylece yol bilgisini daha verimli tuttum.
     */
    public static Node bfs(char[][] board, int startRow, int startCol, int goldRow, int goldCol) {
        int rows = board.length;
        int cols = board[0].length;
        boolean[][] visited = new boolean[rows][cols];

        Queue<Node> queue = new LinkedList<>();
        Node startNode = new Node(startRow, startCol, null);
        queue.add(startNode);
        visited[startRow][startCol] = true;

        while (!queue.isEmpty()) {
            Node current = queue.poll(); //Kuyruktan çıkar
            //Gold'a ulaşmışsak return edip BFS sonlanıyor
            if (current.row == goldRow && current.col == goldCol) {
                return current;
            }
            //Atın 8 farklı hareketi deneniyor ve kuruğa ekleme yapılıyor
            for (int k = 0; k < 8; k++) {
                int newRow = current.row + dRow[k];
                int newCol = current.col + dCol[k];
                //Bu row ve column sınırlar içinde,ağaç içermiyor ve ziyaret edilmemişse ziyaret et ve kuyruğa ekle
                if (isValid(board, newRow, newCol, visited)) {
                    visited[newRow][newCol] = true;
                    queue.add(new Node(newRow, newCol, current));
                }
            }
        }
        return null;
    }

    //Verilen hücre koordinatları sınırlar içinde mi engel içeriyor mu ziyaret edilmiş mi kontrol ediyor.
    public static boolean isValid(char[][] board, int row, int col, boolean[][] visited) {
        int rows = board.length;
        int cols = board[0].length;
        return row >= 0 && row < rows && col >= 0 && col < cols && board[row][col] != 'T' && !visited[row][col];
    }

    //Gold düğümünden başlayarak, parent zinciri aracılığıyla yol bilgisini oluşturur.
    //Her düğümün konum bilgisi addFirst() metodu ile eklenir,böylece yol başlangıçtan hedefe doğru sıralanıyor.
    public static String printPath(Node target, String delimiter) {
        LinkedList<String> pathList = new LinkedList<>();
        Node current = target;
        while (current != null) {
            pathList.addFirst("c" + (current.row + 1) + "," + (current.col + 1));//reverse yapmaya gerek yok
            current = current.parent;
        }
        //Pathlist linkedlistteki her bir string elemanına erişip belirlenen delimeter ile tek bir string haline getiriyoruz
        StringBuilder sb = new StringBuilder();
        Iterator<String> iter = pathList.iterator();
        while (iter.hasNext()) {
            sb.append(iter.next());
            if (iter.hasNext()) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }
}
