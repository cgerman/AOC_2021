import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
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

public class ProgramFuncionaPeroLento {
   public static void main(String[] args) throws Exception {
      List<String> l = Files.readAllLines(Paths.get("D:\\src\\practicas\\AOC_2020\\13\\src\\problem.txt"));

      String result = partOne(l);
      System.out.println("Part one - solution: " + result);
      result = partTwo(l);
      System.out.println("Part two - solution: " + result);
   }

   private static String partOne(List<String> l) {
      long timestamp = Long.parseLong(l.get(0));
      List<Long> busIds = getBusIds(l.get(1));

      long earliestDepartureTime = Long.MAX_VALUE;
      long earliestBusId = 0;
      long waitingTime = 0;
      for (Long busId : busIds) {
         long mod = timestamp % busId;
         long wait = busId - mod; // timestamp - mod + busId - timestamp
         long nextDepartureTime = timestamp + wait;  // timestamp - mod + busId
         if (nextDepartureTime < earliestDepartureTime) {
            earliestBusId = busId;
            earliestDepartureTime = nextDepartureTime;
            waitingTime = wait;
         }
      }
      return String.valueOf(earliestBusId * waitingTime);
   }

   private static String partTwo(List<String> l) {
      List<Bus> busses = getBusses(l.get(1));
      // Ordenamos la lista. Queremos el bus con id mayor al principio.
      // Su id (su periodo) es el que nos marcará los saltos en la búsqueda del timestamp
      busses.sort(new Comparator<Bus>() {
         @Override
         public int compare(Bus o1, Bus o2) {
            return Long.compare(o2.id, o1.id);
         }
      });

      long ts_candidate;
      long nSaltos = 1;
      while (true) {
         ts_candidate = busses.get(0).id * nSaltos - busses.get(0).offset;
         if (validTimestamp(ts_candidate, busses)) break;
         nSaltos++;
      }
      return String.valueOf(ts_candidate);
   }
   
  private static List<Long> getBusIds(String line) {
      List<Long> l = new ArrayList<>();
      String[] parts = line.split(",");
      for (String part : parts) {
         if (part.charAt(0) != 'x') l.add(Long.parseLong(part));
      }
      return l;
   }

   private static List<Bus> getBusses(String line) {
      List<Bus> l = new ArrayList<>();
      String[] parts = line.split(",");
      int offset = 0;
      for (String part : parts) {
         if (part.charAt(0) != 'x') l.add(new Bus(Long.parseLong(part), offset));
         offset++;
      }
      return l;
   }

   private static boolean validTimestamp(long ts_candidate, List<Bus> busses) {
      for (int i = 1; i < busses.size(); i++) {
         Bus bus = busses.get(i);
         if ((ts_candidate + bus.offset) % bus.id != 0) return false;
      }
      return true;
   }

   //////////////////////////

   private static class Bus {
      long id;
      long offset;

      public Bus(long id, long offset) {
         this.id = id;
         this.offset = offset;
      }
   }
}
