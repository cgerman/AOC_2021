import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
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
      List<String> l = Files.readAllLines(Paths.get("D:\\src\\practicas\\AOC_2020\\10\\src\\problem.txt"));
      int[] tmp = new int[l.size()];
      for (int i = 0; i < l.size(); i++) tmp[i] = Integer.parseInt(l.get(i));
      Arrays.sort(tmp);
      int[] jolts = new int[l.size() + 1];
      System.arraycopy(tmp, 0, jolts, 0, l.size());
      jolts[l.size()] = jolts[l.size() - 1] + 3;

      String result = partOne(jolts);
      System.out.println("Part one - solution: " + result);
      result = partTwo(jolts);
      System.out.println("Part two - solution: " + result);
   }

   private static String partOne(int[] jolts) {
      int nDiff1 = 0;
      int nDiff3 = 0;
      int lastJoltage = 0;
      for (int jolt : jolts) {
         int diff = jolt - lastJoltage;
         if (diff == 1) nDiff1++;
         if (diff == 3) nDiff3++;
         if (diff > 3) return "La diferencia es mayor de 3 jolts!";
         lastJoltage = jolt;
      }
      return String.valueOf(nDiff1 * nDiff3);
   }

   private static String partTwo(int[] tmp) {
      // Primero anteponemos un elemento del array con joltage=0
      Adapter[] jolts = new Adapter[tmp.length + 1];
      for (int i = 0; i < tmp.length; i++) jolts[i + 1] = new Adapter(tmp[i]);
      jolts[0] = new Adapter(0);
      long nArrangements = countArrangements(jolts, 0);
      return String.valueOf(nArrangements);
   }

   private static long countArrangements(Adapter[] jolts, int from) {
      if (jolts[from].nArrangementsFromHere != -1) return jolts[from].nArrangementsFromHere; 

      if (from == jolts.length - 1) {
         jolts[from].nArrangementsFromHere = 1;
         return 1;
      }
      if (from == jolts.length - 2) {
         jolts[from].nArrangementsFromHere = 1;
         return 1;
      }

      int initialJoltage = jolts[from].joltage;
      int maxJoltage = initialJoltage + 3;

      if (from == jolts.length - 3) {
         int finalJoltage = jolts[jolts.length - 1].joltage;
         if (finalJoltage <= maxJoltage) {
            jolts[from].nArrangementsFromHere = 2;
            return 2;
         }
         else {
            jolts[from].nArrangementsFromHere = 1;
            return 1;
         }
      }

      long nArrangements = 0;
      for (int i = from + 1; i <= from + 3; i++) {
         int finalJoltage = jolts[i].joltage;
         if (finalJoltage <= maxJoltage) {
            nArrangements += countArrangements(jolts, i);
         }
      }
      jolts[from].nArrangementsFromHere = nArrangements;
      return nArrangements;
   }
   
   //////////////////////
   
   private static class Adapter {
      int joltage;
      long nArrangementsFromHere;
      public Adapter (int joltage) {
         this.joltage = joltage;
         this.nArrangementsFromHere = -1;
      }
   }

}
