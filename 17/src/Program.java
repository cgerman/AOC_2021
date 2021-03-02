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
      List<String> l = Files.readAllLines(Paths.get("D:\\src\\practicas\\AOC_2020\\17\\src\\problem.txt"));

      String result = partOne(l);
      System.out.println("Part one - solution: " + result);

      result = partTwo(l);
      System.out.println("Part two - solution: " + result);
   }

   private static String partOne(List<String> l) {
      char[][][] dimension = parseDimension1(l);
      //printDimension1(dimension);
      
      for (int i=1; i<=6; i++) {
         dimension = cycle1(dimension);
         //printDimension1(dimension);
      }
      int nActive = countActive1(dimension);
      return String.valueOf(nActive);
   }

   private static String partTwo(List<String> l) {
      char[][][][] dimension = parseDimension2(l);
      //printDimension2(dimension);

      for (int i=1; i<=6; i++) {
         dimension = cycle2(dimension);
         //printDimension2(dimension);
      }
      int nActive = countActive2(dimension);
      return String.valueOf(nActive);
   }

   private static char[][][] cycle1(char[][][] dimension) {
      char[][][] newDimension = new char[dimension.length][dimension[0].length][dimension[0][0].length];
      for (int i = 0; i < dimension.length; i++) {
         for (int j = 0; j < dimension[0].length; j++) {
            for (int k = 0; k < dimension[0][0].length; k++) {
               if (activeNextCycle1(i, j, k, dimension)) {
                  newDimension[i][j][k] = '#';
               } else {
                  newDimension[i][j][k] = '.';
               }
            }
         }
      }
      return newDimension;
   }

   private static char[][][][] cycle2(char[][][][] dimension) {
      char[][][][] newDimension = new char[dimension.length][dimension[0].length][dimension[0][0].length][dimension[0][0][0].length];
      for (int i = 0; i < dimension.length; i++) {
         for (int j = 0; j < dimension[0].length; j++) {
            for (int k = 0; k < dimension[0][0].length; k++) {
               for (int l = 0; l < dimension[0][0][0].length; l++) {
                  if (activeNextCycle2(i, j, k, l, dimension)) {
                     newDimension[i][j][k][l] = '#';
                  } else {
                     newDimension[i][j][k][l] = '.';
                  }
               }
            }
         }
      }
      return newDimension;
   }

   private static boolean activeNextCycle1(int x, int y, int z, char[][][] dimension) {
      int activeNeighbours = 0;
      for (int i = x - 1; i <= x + 1; i++) {
         if (i < 0 || i >= dimension.length) continue;
         for (int j = y - 1; j <= y + 1; j++) {
            if (j < 0 || j >= dimension[0].length) continue;
            for (int k = z - 1; k <= z + 1; k++) {
               if (k < 0 || k >= dimension[0][0].length) continue;
               if (i == x && j == y && k == z) continue;
               if (dimension[i][j][k] == '#') activeNeighbours++;
            }
         }
      }
      if (dimension[x][y][z] == '#') {
         return (activeNeighbours == 2 || activeNeighbours == 3);
      } else {
         return activeNeighbours == 3;
      }
   }

   private static boolean activeNextCycle2(int x, int y, int z, int w, char[][][][] dimension) {
      int activeNeighbours = 0;
      for (int i = x - 1; i <= x + 1; i++) {
         if (i < 0 || i >= dimension.length) continue;
         for (int j = y - 1; j <= y + 1; j++) {
            if (j < 0 || j >= dimension[0].length) continue;
            for (int k = z - 1; k <= z + 1; k++) {
               if (k < 0 || k >= dimension[0][0].length) continue;
               for (int l = w - 1; l <= w + 1; l++) {
                  if (l < 0 || l >= dimension[0][0][0].length) continue;
                  if (i == x && j == y && k == z && l == w) continue;
                  if (dimension[i][j][k][l] == '#') activeNeighbours++;
               }
            }
         }
      }
      if (dimension[x][y][z][w] == '#') {
         return (activeNeighbours == 2 || activeNeighbours == 3);
      } else {
         return activeNeighbours == 3;
      }
   }

   private static int countActive1(char[][][] dimension) {
      int nActive = 0;
      for (int i = 0; i < dimension.length; i++) {
         for (int j = 0; j < dimension[0].length; j++) {
            for (int k = 0; k < dimension[0][0].length; k++) {
              if (dimension[i][j][k] == '#') nActive++;
            }
         }
      }
      return nActive;
   }

   private static int countActive2(char[][][][] dimension) {
      int nActive = 0;
      for (int i = 0; i < dimension.length; i++) {
         for (int j = 0; j < dimension[0].length; j++) {
            for (int k = 0; k < dimension[0][0].length; k++) {
               for (int l = 0; l < dimension[0][0][0].length; l++) {
                  if (dimension[i][j][k][l] == '#') nActive++;
               }
            }
         }
      }
      return nActive;
   }

   private static char[][][] parseDimension1(List<String> l) {
      // Sabemos que la superficie especificada es cuadrada
      // Sabemos que en 6 ciclos, puede crecer como máximo 2*6 unidades (6 en cada lado)

      char[][][] dimension = new char[l.size() + 2 * 6][l.size() + 2 * 6][1 + 2 * 6];
      for (int i = 0; i < l.size() + 2 * 6; i++) {
         for (int j = 0; j < l.size() + 2 * 6; j++) {
            for (int k = 0; k < 1 + 2 * 6; k++) {
               dimension[i][j][k] = '.';
            }
         }
      }

      for (int j = 6; j < 6 + l.size(); j++) {
         String line = l.get(j - 6);
         for (int i = 6; i < 6 + line.length(); i++) {
            dimension[i][j][6] = line.charAt(i - 6);
         }
      }

      return dimension;
   }

   private static char[][][][] parseDimension2(List<String> lines) {
      // Sabemos que la superficie especificada es cuadrada
      // Sabemos que en 6 ciclos, puede crecer como máximo 2*6 unidades (6 en cada lado)

      char[][][][] dimension = new char[lines.size() + 2 * 6][lines.size() + 2 * 6][1 + 2 * 6][1 + 2 * 6];
      for (int i = 0; i < lines.size() + 2 * 6; i++) {
         for (int j = 0; j < lines.size() + 2 * 6; j++) {
            for (int k = 0; k < 1 + 2 * 6; k++) {
               for (int l = 0; l < 1 + 2 * 6; l++) {
                  dimension[i][j][k][l] = '.';
               }
            }
         }
      }

      for (int j = 6; j < 6 + lines.size(); j++) {
         String line = lines.get(j - 6);
         for (int i = 6; i < 6 + line.length(); i++) {
            dimension[i][j][6][6] = line.charAt(i - 6);
         }
      }

      return dimension;
   }

   private static void printDimension1(char[][][] dimension) {
      for (int k = 0; k < dimension[0][0].length; k++) {
         for (int j = 0; j < dimension[0].length; j++) {
            for (int i = 0; i < dimension.length; i++) {
               System.out.print(dimension[i][j][k]);
            }
            System.out.println();
         }
         System.out.println("\n");
      }
   }
}
