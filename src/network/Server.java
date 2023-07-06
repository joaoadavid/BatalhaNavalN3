package network;

import board.Board;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) throws IOException {
        int port = 12345;

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Porta " + port + " aberta!");

        Socket clientSocket = serverSocket.accept();
        System.out.println("Cliente " + clientSocket.getInetAddress().getHostAddress() + " adicionado.");

        Scanner clientInput = new Scanner(clientSocket.getInputStream());
        PrintStream output = new PrintStream(clientSocket.getOutputStream());
        Scanner consoleInput = new Scanner(System.in);

        Board board = new Board();
        board.initialize();
        board.printBoard();
        board.printOpponentBoard();

        boolean gameRunning = true;
        while (gameRunning) {
            output.println("YOUR_TURN");

            String clientMessage = clientInput.nextLine();
            System.out.println("Ataque recebido do cliente: " + clientMessage);

            String resultMessage = board.simulateAttack(clientMessage);
            output.println(resultMessage);

            if (resultMessage.equals("VOCÊ VENCEU!") || resultMessage.equals("VOCÊ PERDEU!")) {
                gameRunning = false;
            } else {
                int row = clientMessage.charAt(0) - 'A';
                int column = Integer.parseInt(clientMessage.substring(1)) - 1;
                board.updateBoard(row, column, resultMessage.charAt(0));
                board.printBoard();
                board.printOpponentBoard();

                String serverAttack = makeServerAttack(consoleInput);
                System.out.println("Ataque realizado pelo servidor: " + serverAttack);

                String serverResult = board.simulateAttack(serverAttack);
                output.println(serverResult);

                if (serverResult.equals("VOCÊ VENCEU!") || serverResult.equals("VOCÊ PERDEU!")) {
                    gameRunning = false;
                } else {
                    row = serverAttack.charAt(0) - 'A';
                    column = Integer.parseInt(serverAttack.substring(1)) - 1;
                    board.updateBoard(row, column, serverResult.charAt(0));
                    board.printBoard();
                    board.printOpponentBoard();
                }
            }
        }

        serverSocket.close();
    }

    private static String makeServerAttack(Scanner consoleInput) {
        System.out.print("Digite a linha do ataque do servidor (A-J): ");
        String attackRow = consoleInput.nextLine();
        System.out.print("Digite a coluna do ataque do servidor (1-10): ");
        int attackColumn = Integer.parseInt(consoleInput.nextLine()) - 1;

        return attackRow + Integer.toString(attackColumn);
    }
}
