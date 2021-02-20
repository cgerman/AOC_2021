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
      List<String> l = Files.readAllLines(Paths.get("D:\\src\\practicas\\AOC_2020\\02\\src\\problem.txt"));
      if (l.size() < 1) {
         System.out.println("No hay suficientes datos");
         return;
      }
      
      String result = partOne(l);
      System.out.println("Part one - solution: " + result);
      result = partTwo(l);
      System.out.println("Part two - solution: " + result);
   }

   private static String partOne(List<String> l) {
      long counter = 0;
      for (String line : l) {
         String[] parts = line.split(":");
         if (parts.length != 2) {
            return "Una de las líneas tiene formato incorrecto: " + line;
         }
         String policy = parts[0];
         String password = parts[1].substring(1); // Empieza por espacio
         if (compliant(password, policy)) counter++;
      }
      return String.valueOf(counter);
   }

   private static String partTwo(List<String> l) {
      long counter = 0;
      for (String line : l) {
         String[] parts = line.split(":");
         if (parts.length != 2) {
            return "Una de las líneas tiene formato incorrecto: " + line;
         }
         String policy = parts[0];
         String password = parts[1].substring(1); // Empieza por espacio
         if (compliant2(password, policy)) counter++;
      }
      return String.valueOf(counter);
   }
   
   private static boolean compliant(String pwd, String policy) {
      String[] parts = policy.split(" ");
      String times = parts[0];
      String character = parts[1];
      parts = times.split("-");
      int minOccurrences = Integer.parseInt(parts[0]);
      int maxOccurrences = Integer.parseInt(parts[1]);
      return compliant(pwd, minOccurrences, maxOccurrences, character.charAt(0));
   }
   
   private static boolean compliant (String pwd, int minOccurrences, int maxOccurrences, char c) {
      int counter = 0;
      for (int i=0; i< pwd.length(); i++){
         char nthChar = pwd.charAt(i);
         if (nthChar== c) counter++;
      }
      return counter >=minOccurrences && counter <= maxOccurrences;
   }

   private static boolean compliant2(String pwd, String policy) {
      String[] parts = policy.split(" ");
      String positions = parts[0];
      String character = parts[1];
      parts = positions.split("-");
      int optionA = Integer.parseInt(parts[0]);
      int optionB = Integer.parseInt(parts[1]);
      return compliant2(pwd, optionA-1, optionB-1, character.charAt(0));
   }

   private static boolean compliant2 (String pwd, int posA, int posB, char c) {
      int counter = 0;
      if (pwd.charAt(posA) == c) counter++;
      if (pwd.charAt(posB) == c) counter++;
      return counter == 1;
   }

}
