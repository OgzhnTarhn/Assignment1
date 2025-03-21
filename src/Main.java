import java.util.*;

public class Main {
    // Atın (knight) yapabileceği 8 farklı hareket (satrançtaki L şeklindeki hareketler)
    static int[] dRow = {-2, -1, 1, 2, 2, 1, -1, -2};
    static int[] dCol = {1, 2, 2, 1, -1, -2, -2, -1};

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Girdi olarak verilen board stringini oku.
        // Örneğin: "55...T.T...T..KG.T...T...T." 
        // İlk iki karakter tahtanın satır ve sütun sayısını temsil eder.
        String input = sc.next();

        // İlk iki karakterden satır ve sütun sayısını alıyoruz.
        int rows = Character.getNumericValue(input.charAt(0));
        int cols = Character.getNumericValue(input.charAt(1));

        // Tahtayı temsil eden 2D char dizisini oluşturuyoruz.
        char[][] board = new char[rows][cols];

        // Board stringinin geri kalan kısmını alıyoruz.
        String boardStr = input.substring(2);
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = boardStr.charAt(index++);
            }
        }

        // Başlangıç noktasını (at 'K') ve altını ('G') buluyoruz.
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

        // BFS (Breadth-First Search) için gerekli diziler ve yapılandırmalar.
        boolean[][] visited = new boolean[rows][cols];
        // Her hücreye gelene kadar kaç adım atıldığı bilgisini tutar.
        int[][] distance = new int[rows][cols];
        // Önceki hücre bilgisini saklamak için (yolun geri izlenebilmesi için)
        int[][][] prev = new int[rows][cols][2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                prev[i][j][0] = -1;
                prev[i][j][1] = -1;
            }
        }

        // BFS için kuyruk oluşturuyoruz.
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;
        distance[startRow][startCol] = 0;

        boolean found = false;
        // BFS algoritması: kuyruk boşalana kadar devam et.
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int curRow = cell[0];
            int curCol = cell[1];

            // Altına ulaştıysak, aramayı sonlandır.
            if (curRow == goldRow && curCol == goldCol) {
                found = true;
                break;
            }

            // Atın 8 farklı hareketini dene.
            for (int k = 0; k < 8; k++) {
                int newRow = curRow + dRow[k];
                int newCol = curCol + dCol[k];
                // Yeni koordinatların tahtanın sınırları içinde ve ağaç ('T') içermediğinden emin ol.
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols
                        && board[newRow][newCol] != 'T' && !visited[newRow][newCol]) {
                    visited[newRow][newCol] = true;
                    distance[newRow][newCol] = distance[curRow][curCol] + 1;
                    // Önceki hücre bilgisini kaydet (yol izleme için)
                    prev[newRow][newCol][0] = curRow;
                    prev[newRow][newCol][1] = curCol;
                    queue.add(new int[]{newRow, newCol});
                }
            }
        }

        // Sonuçları ekrana yazdır.
        if (!found) {
            System.out.println("No path to the gold");
        } else {
            // Bulunan yolun geri izlenmesi (gold'dan başlayıp başlangıca kadar)
            ArrayList<String> path = new ArrayList<>();
            int r = goldRow, c = goldCol;
            while (r != -1 && c != -1) {
                // Hücre adını 1-indexli formatta oluştur: c<satır>,<sütun>
                path.add("c" + (r + 1) + "," + (c + 1));
                int pr = prev[r][c][0];
                int pc = prev[r][c][1];
                r = pr;
                c = pc;
            }
            // Geri izlenen yol ters sırada olduğundan, doğru sıraya çevirmek için ters çeviriyoruz.
            Collections.reverse(path);
            // Yol adımlarını " -> " ile birleştirerek yazdırıyoruz.
            System.out.println(String.join(" -> ", path));
        }

        sc.close();
    }
}
