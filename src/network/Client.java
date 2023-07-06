package network;

import board.Board;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        String serverIP = "127.0.0.1";
        int serverPort = 12345;

        Socket clientSocket = new Socket(serverIP, serverPort);
        System.out.println("conectado!");

        Scanner consoleInput = new Scanner(System.in);
        Scanner serverInput = new Scanner(clientSocket.getInputStream());
        PrintStream output = new PrintStream(clientSocket.getOutputStream());

        Board board = new Board();
        board.initialize();
        board.printBoard();

        boolean gameRunning = true;
        while (gameRunning) {
            System.out.println("Aguardando seu turno de ataque...");

            String serverMessage = serverInput.nextLine();
            if (serverMessage.equals("YOUR_TURN")) {
                System.out.println("É o seu turno de ataque!");

                board.printOpponentBoard(); // Exibe o tabuleiro do oponente

                System.out.print("Digite a linha de ataque (A-J): ");
                String attackRow = consoleInput.nextLine();
                System.out.print("Digite a coluna de ataque (1-10): ");
                int attackColumn = Integer.parseInt(consoleInput.nextLine()) - 1;

                output.println(attackRow + attackColumn);

                serverMessage = serverInput.nextLine();
                System.out.println(serverMessage);

                if (serverMessage.equals("VOCÊ VENCEU!") || serverMessage.equals("VOCÊ PERDEU!")) {
                    gameRunning = false;
                } else {
                    int row = attackRow.charAt(0) - 'A';
                    int column = attackColumn;
                    board.updateBoard(row, column, serverMessage.charAt(0));
                    board.printBoard();
                }
            } else if (serverMessage.equals("WAITING_TURN")) {
                System.out.println("Aguarde o seu turno de ataque...");
            }
        }

        clientSocket.close();
    }
}
