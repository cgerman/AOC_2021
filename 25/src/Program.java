import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<String> l = Files.readAllLines(Paths.get("D:\\src\\practicas\\AOC_2020\\25\\src\\problem.txt"));

        String result = partOne(l);
        System.out.println("Part one - solution: " + result);

        result = partTwo(l);
        System.out.println("Part two - solution: " + result);
    }

    private static String partOne(List<String> l) throws Exception {
        long cardPublicKey = Long.parseLong(l.get(0));
        long doorPublicKey = Long.parseLong(l.get(1));
        long cardLoopSize = guessLoopSize2(cardPublicKey);
        System.out.println("Card's loopSize: " + cardLoopSize);
        long doorLoopSize = guessLoopSize2(doorPublicKey);
        System.out.println("Door's loopSize: " + doorLoopSize);
        long encKey1 = transform(cardPublicKey, doorLoopSize);
        long encKey2 = transform(doorPublicKey, cardLoopSize);
        assert encKey1 == encKey2;
        return String.valueOf(encKey1);
    }

    private static String partTwo(List<String> l) throws Exception {
        return "";
    }

    private static long guessLoopSize(long encKey) {
        long loopSize = 1;
        while (true) {
            long candidateKey = transform(7L, loopSize);
            if (candidateKey == encKey) return loopSize;
            loopSize++;
            if (loopSize % 1000 == 0) System.out.print("\rTrying loopSize=" + loopSize);
        }
    }

    private static long transform(long subject, long loopSize) {
        long value = 1L;
        for (int i = 0; i < loopSize; i++) {
            value = value * subject;
            value = value % 20201227;
        }
        return value;
    }

    private static long guessLoopSize2(long encKey) {
        long loopSize = 1L;
        long candidateKey = 1L;
        while (true) {
            candidateKey = transform2(7L, candidateKey);
            if (candidateKey == encKey) return loopSize;
            loopSize++;
        }
    }

    private static long transform2(long subject, long startingValue) {
        long value = startingValue;
        value = value * subject;
        value = value % 20201227;
        return value;
    }
}
