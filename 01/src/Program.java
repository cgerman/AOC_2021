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
      List<String> l = Files.readAllLines(Paths.get("D:\\src\\practicas\\AOC_2020\\01\\src\\problem.txt"));
      if (l.size() < 2) {
         System.out.println("No hay suficientes datos");
         return;
      }
      
      String result = partOne(l);
      System.out.println("Part one - solution: " + result);
      result = partTwo(l);
      System.out.println("Part two - solution: " + result);
   }

   private static String partOne(List<String> l) {
      for (int i=0; i < l.size() - 1; i++) {
         String candidate = l.get(i);
         long lCandidate = Long.parseLong(candidate);
         for (int j=i+1; j < l.size(); j++) {
            long lPair = Long.parseLong(l.get(j));
            if (lCandidate + lPair == 2020) {
               return String.valueOf((lCandidate*lPair));
            }
         }
      }
      return "No hay solucion";
   }

   private static String partTwo(List<String> l) {
      for (int i=0; i < l.size() - 2; i++) {
         String firstNum = l.get(i);
         long lFirstNum = Long.parseLong(firstNum);
         for (int j=i+1; j < l.size() - 1; j++) {
            long lSecondNum = Long.parseLong(l.get(j));
            for (String s : l) {
               long lThirdNum = Long.parseLong(s);
               if (lFirstNum + lSecondNum + lThirdNum == 2020) {
                  return String.valueOf((lFirstNum * lSecondNum * lThirdNum));
               }
            }
         }
      }
      return "No hay solucion";
   }
   
}
