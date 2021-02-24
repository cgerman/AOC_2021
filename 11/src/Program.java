import java.io.IOException;
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
      List<String> l = Files.readAllLines(Paths.get("D:\\src\\practicas\\AOC_2020\\11\\src\\problem.txt"));

      // Construimos una matriz de asientos
      Seat[][] seats = new Seat[l.size()][l.get(0).length()];
      for (int i = 0; i < l.size(); i++) {
         String line = l.get(i);
         for (int j = 0; j < line.length(); j++) {
            seats[i][j] = new Seat(line.charAt(j));
         }
      }

      String result = partOne(seats);
      System.out.println("Part one - solution: " + result);
      result = partTwo(seats);
      System.out.println("Part two - solution: " + result);
   }

   private static String partOne(Seat[][] seats) throws IOException {
      int nRound = 0;
      while (true) {
         nRound++;
         Seat[][] newSeats = new Seat[seats.length][seats[0].length];

         boolean unchanged = true;
         for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
               if (seats[i][j].isFree() && nOccupiedNeighbours(seats, i, j) == 0) {
                  newSeats[i][j] = new Seat('#');
                  unchanged = false;
               } else if (seats[i][j].isOccupied() && nOccupiedNeighbours(seats, i, j) >= 4) {
                  newSeats[i][j] = new Seat('L');
                  unchanged = false;
               } else {
                  newSeats[i][j] = new Seat(seats[i][j].state);
               }
            }
         }

         if (unchanged) break;

         //print(newSeats);

         seats = newSeats;
      }
      return String.valueOf(nOccupied(seats));
   }

   private static String partTwo(Seat[][] seats) {
      int nRound = 0;
      while (true) {
         nRound++;
         Seat[][] newSeats = new Seat[seats.length][seats[0].length];

         boolean unchanged = true;
         for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
               if (seats[i][j].isFree() && nOccupiedNeighbours2(seats, i, j) == 0) {
                  newSeats[i][j] = new Seat('#');
                  unchanged = false;
               } else if (seats[i][j].isOccupied() && nOccupiedNeighbours2(seats, i, j) >= 5) {
                  newSeats[i][j] = new Seat('L');
                  unchanged = false;
               } else {
                  newSeats[i][j] = new Seat(seats[i][j].state);
               }
            }
         }

         if (unchanged) break;

         //print(newSeats);

         seats = newSeats;
      }
      return String.valueOf(nOccupied(seats));
   }

   private static int nOccupiedNeighbours(Seat[][] seats, int x, int y) {
      int nOccupied = 0;
      for (int i = x - 1; i <= x + 1; i++) {
         if (i < 0 || i > seats.length - 1) continue;
         for (int j = y - 1; j <= y + 1; j++) {
            if (j < 0 || j > seats[0].length - 1) continue;
            if (i == x && j == y) continue;

            if (seats[i][j].isOccupied()) nOccupied++;
         }
      }
      return nOccupied;
   }

   private static int nOccupiedNeighbours2(Seat[][] seats, int x, int y) {
      int nOccupied = 0;

      nOccupied += isNeighbourOccupied(seats, x, y, 0, 1);
      nOccupied += isNeighbourOccupied(seats, x, y, 0, -1);

      nOccupied += isNeighbourOccupied(seats, x, y, 1, 0);
      nOccupied += isNeighbourOccupied(seats, x, y, 1, 1);
      nOccupied += isNeighbourOccupied(seats, x, y, 1, -1);

      nOccupied += isNeighbourOccupied(seats, x, y, -1, 0);
      nOccupied += isNeighbourOccupied(seats, x, y, -1, 1);
      nOccupied += isNeighbourOccupied(seats, x, y, -1, -1);

      return nOccupied;
   }

   private static int isNeighbourOccupied(Seat[][] seats, int x, int y, int inc_x, int inc_y) {
      for (int i = x + inc_x, j = y + inc_y; i >= 0 && i < seats.length && j >= 0 && j < seats[i].length; i += inc_x, j += inc_y) {
         if (seats[i][j].isOccupied()) return 1;
         if (seats[i][j].isFree()) return 0;
      }
      return 0;
   }

   private static int nOccupied(Seat[][] seats) {
      int nOccupied = 0;
      for (int i = 0; i < seats.length; i++) {
         for (int j = 0; j < seats[i].length; j++) {
            if (seats[i][j].isOccupied()) nOccupied++;
         }
      }
      return nOccupied;
   }

   private static void print(Seat[][] seats) {
      for (int i = 0; i < seats.length; i++) {
         for (int j = 0; j < seats[i].length; j++) {
            System.out.print(seats[i][j].state);
         }
         System.out.println();
      }
      System.out.println();
   }
   //////////////////

   private static class Seat {
      char state;

      public Seat(char state) {
         this.state = state;
      }

      public boolean isOccupied() {
         return state == '#';
      }

      public boolean isFree() {
         return state == 'L';
      }
   }

}
