import java.util.*;
public class Node {
    int row, col;
    Node parent; // Bu düğümün parentını tutuyor

    Node(int row, int col, Node parent) {
        this.row = row;
        this.col = col;
        this.parent = parent;
    }
}