import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Project     : Advent of Code
 * Headline    : Practice programming.
 * <p>
 * (C) 2021 Carlos German Romero
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * For more information, see see <http://www.gnu.org/licenses/>
 */

public class Program {
    public static void main(String[] args) throws Exception {
        List<String> l = Files.readAllLines(Paths.get("D:\\src\\practicas\\AOC_2020\\22\\src\\problem.txt"));

        String result = partOne(l);
        System.out.println("Part one - solution: " + result);

        result = partTwo(l);
        System.out.println("Part two - solution: " + result);
    }

    private static String partOne(List<String> l) throws Exception {
        LinkedList<Integer> playerOneDeck = new LinkedList<>();
        LinkedList<Integer> playerTwoDeck = new LinkedList<>();
        parseProblemInput(l, playerOneDeck, playerTwoDeck);

        playCombat(playerOneDeck, playerTwoDeck);

        LinkedList<Integer> winnerDeck = (playerOneDeck.size() == 0 ? playerTwoDeck : playerOneDeck);
        long winningDeckScore = score(winnerDeck);
        return String.valueOf(winningDeckScore);
    }

    private static String partTwo(List<String> l) throws Exception {
        LinkedList<Integer> playerOneDeck = new LinkedList<>();
        LinkedList<Integer> playerTwoDeck = new LinkedList<>();
        parseProblemInput(l, playerOneDeck, playerTwoDeck);

        playRecursiveCombat(playerOneDeck, playerTwoDeck, new ArrayList<>(), new ArrayList<>());

        LinkedList<Integer> winnerDeck = (playerOneDeck.size() == 0 ? playerTwoDeck : playerOneDeck);
        long winningDeckScore = score(winnerDeck);
        return String.valueOf(winningDeckScore);
    }

    private static void parseProblemInput(List<String> l, LinkedList<Integer> playerOneDeck, LinkedList<Integer> playerTwoDeck) {
        int i = 1;
        while (!l.get(i).isEmpty()) {
            Integer card = new Integer(l.get(i));
            playerOneDeck.addLast(card);
            i++;
        }
        i += 2;
        while (i < l.size()) {
            Integer card = new Integer(l.get(i));
            playerTwoDeck.addLast(card);
            i++;
        }
    }

    private static boolean endOfGame(LinkedList<Integer> playerOneDeck, LinkedList<Integer> playerTwoDeck) {
        return playerOneDeck.size() == 0 || playerTwoDeck.size() == 0;
    }

    private static void playCombat(LinkedList<Integer> playerOneDeck, LinkedList<Integer> playerTwoDeck) {
        do {
            roundCombat(playerOneDeck, playerTwoDeck);
        } while (!endOfGame(playerOneDeck, playerTwoDeck));
    }

    private static void roundCombat(LinkedList<Integer> playerOneDeck, LinkedList<Integer> playerTwoDeck) {
        Integer card1 = playerOneDeck.removeFirst();
        Integer card2 = playerTwoDeck.removeFirst();

        Integer winnerCard, looserCard;
        LinkedList<Integer> winningDeck;
        if (card1 > card2) {
            winningDeck = playerOneDeck;
            winnerCard = card1;
            looserCard = card2;
        } else {
            winningDeck = playerTwoDeck;
            winnerCard = card2;
            looserCard = card1;
        }
        winningDeck.addLast(winnerCard);
        winningDeck.addLast(looserCard);
    }

    private static int playRecursiveCombat(LinkedList<Integer> playerOneDeck, LinkedList<Integer> playerTwoDeck, List<LinkedList<Integer>> memoryOne, List<LinkedList<Integer>> memoryTwo) {
        do {
            // Primero verificamos que no estamos en un bucle infinito.
            // Si así fuera, el juego lo gana el jugador 1.
            for (LinkedList<Integer> deck1 : memoryOne) {
                if (deck1.equals(playerOneDeck)) {
                    for (LinkedList<Integer> deck2 : memoryTwo) {
                        if (deck2.equals(playerTwoDeck)) {
                            return 1;
                        }
                    }
                }
            }
            memoryOne.add(new LinkedList<>(playerOneDeck));
            memoryTwo.add((new LinkedList<>(playerTwoDeck)));

            Integer card1 = playerOneDeck.getFirst();
            Integer card2 = playerTwoDeck.getFirst();
            int roundWinner = roundRecursiveCombat(playerOneDeck, playerTwoDeck);
            if (roundWinner == 1) {
                playerOneDeck.addLast(card1);
                playerOneDeck.addLast(card2);
            } else {
                playerTwoDeck.addLast(card2);
                playerTwoDeck.addLast(card1);
            }

        } while (!endOfGame(playerOneDeck, playerTwoDeck));

        if (playerOneDeck.size() == 0) return 2;
        else return 1;
    }

    private static int roundRecursiveCombat(LinkedList<Integer> playerOneDeck, LinkedList<Integer> playerTwoDeck) {
        Integer card1 = playerOneDeck.removeFirst();
        Integer card2 = playerTwoDeck.removeFirst();
        if (card1 > playerOneDeck.size() || card2 > playerTwoDeck.size()) {
            if (card1 > card2) return 1;
            else return 2;
        }

        // El ganador de la ronda será el que gane un sub-juego
        // Preparamos los mazos para ese nuevo sub-juego
        LinkedList<Integer> newPlayerOneDeck = new LinkedList<>();
        for (int i = 0; i < card1; i++) newPlayerOneDeck.add(playerOneDeck.get(i));
        LinkedList<Integer> newPlayerTwoDeck = new LinkedList<>();
        for (int i = 0; i < card2; i++) newPlayerTwoDeck.add(playerTwoDeck.get(i));
        return playRecursiveCombat(newPlayerOneDeck, newPlayerTwoDeck, new ArrayList<>(), new ArrayList<>());
    }

    private static long score(LinkedList<Integer> deck) {
        long total = 0;
        int i = 1;
        Iterator<Integer> iter = deck.descendingIterator();
        while (iter.hasNext()) {
            Integer card = iter.next();
            total += card * i++;
        }
        return total;
    }
}
