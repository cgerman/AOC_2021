import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Project     : Advent of Code
 * Headline    : Practice programming.
 *
 * (C) 2021 Carlos German Romero
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * For more information, see see <http://www.gnu.org/licenses/>
 */

public class Program {
   public static void main(String[] args) throws Exception {
      List<String> l = Files.readAllLines(Paths.get("D:\\src\\practicas\\AOC_2020\\09\\src\\problem.txt"));

      String result = partOne(l);
      System.out.println("Part one - solution: " + result);
      result = partTwo(l);
      System.out.println("Part two - solution: " + result);
   }

   private static String partOne(List<String> l) {
      int first = 0;
      int preamble = 25;
      for (int i = first + preamble; i < l.size(); i++, first++) {
         String sNum = l.get(i);
         if (!validSum(l, Long.parseLong(sNum), first, preamble)) return sNum;
      }
      return "All numbers are valid sums of two of the " + preamble + " previous ones";
   }

   private static String partTwo(List<String> l) {
      String sInvalidNum = partOne(l);
      long invalidNum = Long.parseLong(sInvalidNum);

      for (int i = 0; i < l.size(); i++) {
         long encWeakness = buscaSumasConsecutivos(l, i, invalidNum);
         if (encWeakness > 0) return String.valueOf(encWeakness);
      }
      return "No encontramos la debilidad en XMAS";
   }

   private static long buscaSumasConsecutivos(List<String> l, int first, long invalidNum) {
      long suma = Long.parseLong(l.get(first));
      long min = suma;
      long max = suma;
      int i = first + 1;
      while (suma < invalidNum) {
         long n = Long.parseLong(l.get(i));
         if (n < min) min = n;
         if (n > max) max = n;
         suma = suma + n;
         i++;
      }
      if (suma == invalidNum) return min + max;
      else return 0;
   }

   private static boolean validSum(List<String> l, long num, int first, int preamble) {
      for (int i = first; i < first + preamble - 1; i++) {
         long ni = Long.parseLong(l.get(i));
         for (int j = i + 1; j < first + preamble; j++) {
            long nj = Long.parseLong(l.get(j));
            if (ni + nj == num) return true;
         }
      }
      return false;
   }
}
