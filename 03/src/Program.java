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
      List<String> l = Files.readAllLines(Paths.get("D:\\src\\practicas\\AOC_2020\\03\\src\\problem.txt"));

      char[][] matrix = new char[l.size()][];
      for (int i=0; i< l.size(); i++) {
         String line = l.get(i);
         matrix[i] = line.toCharArray();
      }

      String result = partOne(matrix);
      System.out.println("Part one - solution: " + result);
      result = partTwo(matrix);
      System.out.println("Part two - solution: " + result);
   }

   private static String partOne(char[][] matrix) {
      return String.valueOf(nTrees(matrix, 1, 3));
   }

   private static String partTwo(char[][] matrix) {
      long a = nTrees(matrix, 1, 1);
      long b = nTrees(matrix, 1, 3);
      long c = nTrees(matrix, 1, 5);
      long d = nTrees(matrix, 1, 7);
      long e = nTrees(matrix, 2, 1);
      return String.valueOf(a*b*c*d*e);
   }
   
   private static long nTrees(char[][] matrix, int downSlope, int rightSlope) {
      long counter = 0;
      for (int x=0, y=0; y < matrix.length; y+=downSlope, x= (x+rightSlope) % 31) {
         if (matrix[y][x] == '#') counter++;
      }
      return counter;
   }
   
}
