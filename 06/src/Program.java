import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
      List<String> l = Files.readAllLines(Paths.get("D:\\src\\practicas\\AOC_2020\\06\\src\\problem.txt"));

      String result = partOne(l);
      System.out.println("Part one - solution: " + result);
      result = partTwo(l);
      System.out.println("Part two - solution: " + result);
   }

   private static String partOne(List<String> l) {
      List<Group> yesAnswersByGroups = readYesAnswersByGroups(l);
      long yesCounter = 0;
      for (Group group : yesAnswersByGroups) {
         yesCounter += group.size();
      }
      return String.valueOf(yesCounter);
   }

   private static String partTwo(List<String> l) {
      List<Group> yesAnswersByGroups = readYesAnswersByGroups(l);
      long yesCounter = 0;
      for (Group group : yesAnswersByGroups) {
         for (String c : group.keySet()) {
            String charCounter = group.get(c);
            if (Integer.parseInt(charCounter) == group.nPersonas) yesCounter++; 
         }
      }
      return String.valueOf(yesCounter);
   }

   private static List<Group> readYesAnswersByGroups(List<String> l) {
      List<Group> yesAnswersByGroups = new ArrayList<>();
      Group currentGroup = new Group();
      yesAnswersByGroups.add(currentGroup);
      for (String line : l) {
         if (line.isEmpty()) {
            currentGroup = new Group();
            yesAnswersByGroups.add(currentGroup);
         } else {
            currentGroup.nPersonas++;
            for (int j = 0; j < line.length(); j++) {
               currentGroup.count(line.charAt(j));
            }
         }
      }
      return yesAnswersByGroups;
   }

   private static class Group extends HashMap<String, String> {
      int nPersonas = 0;
      
      private void count(char c) {
         String counter = get(String.valueOf(c));
         if (counter == null) {
            put(String.valueOf(c), "1");
         } else {
            put(String.valueOf(c), String.valueOf(Integer.parseInt(counter) + 1));
         }
      }
   }
}
