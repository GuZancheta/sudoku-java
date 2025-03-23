package br.com.gustavoz;

import br.com.gustavoz.model.Board;
import br.com.gustavoz.model.Space;
import br.com.gustavoz.util.BoardTemplate;

import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SuppressWarnings("java:S106")
public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static Board board;

    private static final int BOARD_SIZE = 9;

    @SuppressWarnings("java:S2189")
    public static void main(String[] args) {
        final var positions = Stream.of(args)
                .collect(Collectors.toMap(
                        k -> k.split(";")[0],
                        v -> v.split(";")[1]
                ));

        var option = -1;
        while (true) {
            System.out.println("Select one of the options below:");
            System.out.println("1 - Start a new game");
            System.out.println("2 - Enter a new number");
            System.out.println("3 - Remove a number");
            System.out.println("4 - View current game");
            System.out.println("5 - Check game status");
            System.out.println("6 - Clear game");
            System.out.println("7 - Finish the game");
            System.out.println("8 - Exit");

            option = scanner.nextInt();

            switch (option) {
                case 1 -> startGame(positions);
                case 2 -> inputNumber();
                case 3 -> removeNumber();
                case 4 -> showCurrentGame();
                case 5 -> showGameStatus();
                case 6 -> clearGame();
                case 7 -> finishGame();
                case 8 -> System.exit(0);
                case 9 -> inputAllNumbers(positions); // Only for tests
                default -> System.out.println("Invalid option, select one option of menu");
            }
        }
    }

    private static void startGame(Map<String, String> positions) {
        if (board != null) {
            System.out.println("The game has already started");
            return;
        }

        board = new Board(IntStream.range(0, BOARD_SIZE)
                .mapToObj(i -> IntStream.range(0, BOARD_SIZE)
                        .mapToObj(j -> {
                            String[] config = positions.get("%s,%s".formatted(i, j)).split(",");
                            return new Space(Integer.parseInt(config[0]), Boolean.parseBoolean(config[1]));
                        }).toList())
                .toList());

        System.out.println("Game started");
    }

    private static void inputNumber() {
        if (validationGameHasNotStarted()) return;

        System.out.println("Enter the row, column and number: ");
        var row = runUntilGetValidNumber(0, 8);
        var column = runUntilGetValidNumber(0, 8);
        var number = runUntilGetValidNumber(1, 9);

        if (!board.changeValue(row, column, number)) {
            System.out.println("Invalid position, try again, the position " + row + "," + column + " is fixed");
            return;
        }
        System.out.println("Number entered");
    }

    private static void removeNumber() {
        if (validationGameHasNotStarted()) return;

        System.out.println("Enter the row, column and number: ");
        var row = runUntilGetValidNumber(0, 8);
        var column = runUntilGetValidNumber(0, 8);

        if (!board.clearSpace(row, column)) {
            System.out.println("Invalid position, try again, the position " + row + "," + column + " is fixed");
            return;
        }
        System.out.println("Number removed");
    }

    private static void showCurrentGame() {
        if (validationGameHasNotStarted()) return;

        var args = new Object[81];
        var argPos = 0;

        for (var i = 0; i < BOARD_SIZE; i++) {
            for (var j = 0; j < BOARD_SIZE; j++) {
                args[argPos++] = " " + (board.getSpaceList().get(i).get(j).getActual() != null ? board.getSpaceList().get(i).get(j).getActual() : " ");
            }
        }

        System.out.println("Current game:");
        System.out.printf(BoardTemplate.BOARD_TEMPLATE, args);
    }

    private static void showGameStatus() {
        System.out.println("Game status: " + board.getGameStatus().getValue());
        if (board.hasErrors()) {
            System.out.println("Game has errors");
        } else {
            System.out.println("Game has no errors");
        }
    }

    private static void clearGame() {
        if (validationGameHasNotStarted()) return;

        System.out.println("Can you really clear the game?");
        System.out.println("1 - Yes");
        System.out.println("2 - No");
        var option = scanner.nextInt();
        if (option != 1) {
            System.out.println("Game not cleared");
            return;
        }

        board.reset();
        System.out.println("Game cleared");
    }

    private static void finishGame() {
        if (validationGameHasNotStarted()) return;

        if (board.gameIsComplete()) {
            System.out.println("Game already finished");
            showCurrentGame();
        } else if (board.hasErrors()) {
            System.out.println("Game has errors");
            showCurrentGame();
        } else {
            System.out.println("Game finished");
            showCurrentGame();
        }
    }

    private static void inputAllNumbers(Map<String, String> positions) {
        if (validationGameHasNotStarted()) return;

        for (var i = 0; i < BOARD_SIZE; i++) {
            for (var j = 0; j < BOARD_SIZE; j++) {
                String[] config = positions.get("%s,%s".formatted(i, j)).split(",");
                var number = Integer.parseInt(config[0]);

                if (config[1].equals("false")) {
                    board.changeValue(i, j, number);
                }
            }
        }
    }

    private static boolean validationGameHasNotStarted() {
        if (board == null) {
            System.out.println("The game hasn't started yet");
            return true;
        }
        return false;
    }

    private static int runUntilGetValidNumber(final int min, final int max) {
        var currentNumber = scanner.nextInt();
        while (currentNumber < min || currentNumber > max) {
            System.out.println("Invalid number, try again, enter a number between " + min + " and " + max);
            currentNumber = scanner.nextInt();
        }
        return currentNumber;
    }

}