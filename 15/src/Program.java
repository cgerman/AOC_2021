import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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
      List<String> l = Files.readAllLines(Paths.get("D:\\src\\practicas\\AOC_2020\\15\\src\\problem.txt"));

      String result = partOne(l);
      System.out.println("Part one - solution: " + result);
      result = partTwo(l);
      System.out.println("Part two - solution: " + result);
   }

   private static String partOne(List<String> l) {
      String[] startingNumbers = l.get(0).split(",");
      List<Long> turnos = new ArrayList<>();
      for (String num : startingNumbers) turnos.add(new Long(num));

      Long numberSpoken = Long.valueOf(startingNumbers[startingNumbers.length-1]);
      for (long i = startingNumbers.length; i < 2020; i++) {
         numberSpoken = nextNumber(turnos); 
         turnos.add(numberSpoken);
      }
      return numberSpoken.toString();
   }

   private static String partTwo(List<String> l) {
      String[] startingNumbers = l.get(0).split(",");

      Map<Long, Turnos> spokenNumbers = new HashMap<>();
      for (int i=0;i<startingNumbers.length; i++) {
         Turnos t = new Turnos(i);
         spokenNumbers.put(new Long(startingNumbers[i]), t);
      }
      
      Long numberSpoken = Long.valueOf(startingNumbers[startingNumbers.length-1]);
      for (long i = startingNumbers.length; i < 30000000; i++) {
         numberSpoken = nextNumber2(numberSpoken, spokenNumbers);
         if (i % 1000000 == 0) System.out.print(i + ": "+ numberSpoken + "\r");

         Turnos t = spokenNumbers.get(numberSpoken);
         if (t == null) {
            t = new Turnos(i);
            spokenNumbers.put(numberSpoken, t);
         } else {
            t.newTurn(i);
         }
      }
      return numberSpoken.toString();
   }

   private static Long nextNumber(List<Long> turnos) {
      Long lastNumber = turnos.get(turnos.size() - 1);
      for (int count = 1, i = turnos.size() - 2; i >= 0; i--, count++) {
         Long l = turnos.get(i);
         if (l.equals(lastNumber)) return (long) count;
      }
      return 0L;
   }

   private static Long nextNumber2(Long lastNumber, Map<Long, Turnos> spokenNumbers) {
      Turnos t = spokenNumbers.get(lastNumber);
      if (!t.atLeastTwo()) return 0L;
      
      return t.last-t.prev;
   }
   
   private static class Turnos {
      Long last;
      Long prev;
      
      public Turnos(long last) {
         this.last = last;
         this.prev = null;
      }
      
      public boolean atLeastTwo() {
         return prev != null;
      }
      
      public void newTurn(long t) {
         this.prev = this.last;
         this.last = t;
      }
   }

}
