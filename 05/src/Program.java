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
      List<String> l = Files.readAllLines(Paths.get("D:\\src\\practicas\\AOC_2020\\05\\src\\problem.txt"));

      String result = partOne(l);
      System.out.println("Part one - solution: " + result);
      result = partTwo(l);
      System.out.println("Part two - solution: " + result);
   }

   private static String partOne(List<String> l) {
      long highestSeatId = 0;
      for (String boardingPass : l) {
         long seatId = getSeatId(boardingPass);
         if (seatId > highestSeatId) highestSeatId = seatId;
      }
      return String.valueOf(highestSeatId);
   }

   private static String partTwo(List<String> l) {
      int[] seatIds = new int[1024];
      for (int i=0; i<1024; i++) seatIds[i] = -1;
      for (String boardingPass : l) {
         int seatId = getSeatId(boardingPass);
         seatIds[seatId] = 1;
      }
      for (int i=1; i<1023; i++) {
         if (seatIds[i-1] == 1 && seatIds[i] == -1 && seatIds[i+1] == 1) {
            return String.valueOf(i);
         }
      }
      return "no hay asientos vacíos";
   }

   private static int getSeatId(String boardingPass) {
      String rowSpec = boardingPass.substring(0,7);
      String colSpec = boardingPass.substring(7,10);
      int row = getPoint(rowSpec, 'F');
      int col = getPoint(colSpec, 'L');
      return row * 8 + col;
   }
   
   private static int getPoint(String spec, char low) {
      int exp = spec.length();
      int min = 0;
      int max = (int) Math.pow(2, exp) - 1;
      return getPointRecursive(spec, min, max, low);
   }
   
   private static int getPointRecursive(String spec, int min, int max, char low) {
      char c = spec.charAt(0);
      if (spec.length() == 1) {
         return c == low ? min : max;
      }
      String newSpec = spec.substring(1);
      int newExp = newSpec.length();
      int newMin, newMax;
      if (c == low) {
         newMin = min;
         newMax = (int) (max - Math.pow(2, newExp));
      } else {
         newMin = (int) (min + Math.pow(2, newExp));
         newMax = max;
      }
      return getPointRecursive(newSpec, newMin, newMax, low);
   }
}
