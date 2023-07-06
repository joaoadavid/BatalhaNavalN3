package board;

import java.util.Scanner;

public class Board {
    public static final int BOARD_SIZE = 10;

    private char[][] board;
    private boolean[][] attacked;

    public Board() {
        board = new char[BOARD_SIZE][BOARD_SIZE];
        attacked = new boolean[BOARD_SIZE][BOARD_SIZE];
    }

    public void initialize() {
        // Preenche o tabuleiro com espaços vazios
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = ' ';
                attacked[i][j] = false;
            }
        }


      // posicionamento de forma personalizada
        placeShip("P", 5);
        placeShip("N", 4);
        placeShip("C", 3);
        placeShip("S", 2);
    }

    private void placeShip(String shipType, int size) {
        System.out.println("Posicione o navio " + shipType + " (" + size + " posições)");

        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < size; i++) {
            boolean validPosition = false;
            while (!validPosition) {
                System.out.print("Digite a linha da posição " + (i + 1) + " (A-J): ");
                String rowInput = scanner.nextLine();
                int row = rowInput.charAt(0) - 'A';

                System.out.print("Digite a coluna da posição " + (i + 1) + " (1-10): ");
                int column = Integer.parseInt(scanner.nextLine()) - 1;

                if (isValidPosition(row, column) && isEmptyPosition(row, column)) {
                    board[row][column] = shipType.charAt(0);
                    validPosition = true;
                } else {
                    System.out.println("Posição inválida. Tente novamente.");
                }
            }
        }
    }

    public void printBoard() {
        System.out.println("Tabuleiro:");

        System.out.print("   ");
        for (int i = 1; i <= BOARD_SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print((char) ('A' + i) + "  ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void printOpponentBoard() {
        System.out.println("Tabuleiro do Oponente:");

        System.out.print("   ");
        for (int i = 1; i <= BOARD_SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print((char) ('A' + i) + "  ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (attacked[i][j]) {
                    if (board[i][j] == 'X' || board[i][j] == '-') {
                        System.out.print(board[i][j] + " ");
                    } else {
                        System.out.print("  ");
                    }
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public String simulateAttack(String attackCoordinates) {
        char rowChar = attackCoordinates.charAt(0);
        int row = rowChar - 'A';
        int column = Integer.parseInt(attackCoordinates.substring(1)) - 1;

        if (isValidPosition(row, column)) {
            if (board[row][column] != ' ') {
                char shipType = board[row][column];
                board[row][column] = 'X';
                return "Acertou um navio " + shipType + "!";
            } else {
                board[row][column] = '-';
                return "Ataque sem sucesso.";
            }
        } else {
            return "Coordenadas de ataque inválidas.";
        }
    }

    public void updateBoard(int row, int column, char result) {
        attacked[row][column] = true;
        if (result == 'A') {
            board[row][column] = 'X';
        } else {
            board[row][column] = '-';
        }
    }

    private boolean isValidPosition(int row, int column) {
        return row >= 0 && row < BOARD_SIZE && column >= 0 && column < BOARD_SIZE;
    }

    private boolean isEmptyPosition(int row, int column) {
        return board[row][column] == ' ';
    }
}
