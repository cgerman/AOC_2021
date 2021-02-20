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
      List<String> l = Files.readAllLines(Paths.get("D:\\src\\practicas\\AOC_2020\\04\\src\\problem.txt"));

      List<Map<String, String>> passports = readPassports(l);

      String result = partOne(passports);
      System.out.println("Part one - solution: " + result);
      result = partTwo(passports);
      System.out.println("Part two - solution: " + result);
   }

   private static String partOne(List<Map<String, String>> passports) {
      long counter = 0;
      for (Map<String, String> passport : passports) {
         if (hasMandatoryFields(passport)) counter++;
      }
      return String.valueOf(counter);
   }

   private static String partTwo(List<Map<String, String>> passports) {
      long counter = 0;
      for (Map<String, String> passport : passports) {
         if (hasMandatoryFields(passport) && hasValidFields(passport)) counter++;
      }
      return String.valueOf(counter);
   }

   private static boolean hasMandatoryFields(Map<String, String> passport) {
      if (passport.size() < 7) return false;

      return passport.containsKey("byr") &&
               passport.containsKey("iyr") &&
               passport.containsKey("eyr") &&
               passport.containsKey("hgt") &&
               passport.containsKey("hcl") &&
               passport.containsKey("ecl") &&
               passport.containsKey("pid");
   }

   private static boolean hasValidFields(Map<String, String> passport) {
      return valid_byr(passport.get("byr")) &&
               valid_iyr(passport.get("iyr")) &&
               valid_eyr(passport.get("eyr")) &&
               valid_hgt(passport.get("hgt")) &&
               valid_hcl(passport.get("hcl")) &&
               valid_ecl(passport.get("ecl")) &&
               valid_pid(passport.get("pid"));
   }

   private static boolean valid_byr(String byr) {
      try {
         int year = Integer.parseInt(byr);
         return year >= 1920 && year <= 2002;
      } catch (Exception e) {
         return false;
      }
   }

   private static boolean valid_iyr(String iyr) {
      try {
         int year = Integer.parseInt(iyr);
         return year >= 2010 && year <= 2020;
      } catch (Exception e) {
         return false;
      }
   }

   private static boolean valid_eyr(String eyr) {
      try {
         int year = Integer.parseInt(eyr);
         return year >= 2020 && year <= 2030;
      } catch (Exception e) {
         return false;
      }
   }

   private static boolean valid_hgt(String hgt) {
      try {
         if (hgt.length()<3) return false;
         String units = hgt.substring(hgt.length() - 2);
         if (!units.equals("in") && !units.equals("cm")) return false;
         String sNumber = hgt.substring(0, hgt.length() - 2);
         int n = Integer.parseInt(sNumber);
         if (units.equals("cm")) return n >= 150 && n <= 193; 
         else return n >= 59 && n <= 76;
      } catch (Exception e) {
         return false;
      }
   }

   private static boolean valid_hcl(String hcl) {
      try {
         if (hcl.length()!=7) return false;
         if (hcl.charAt(0) != '#') return false;
         hcl = hcl.substring(1);
         Integer.parseInt(hcl, 16);
         return true;
      } catch (Exception e) {
         return false;
      }
   }

   private static boolean valid_ecl(String ecl) {
     return ecl.equals("amb") || ecl.equals("blu") || ecl.equals("brn") ||
              ecl.equals("gry") || ecl.equals("grn") || ecl.equals("hzl") || ecl.equals("oth");
   }

   private static boolean valid_pid(String pid) {
      if (pid.length()!=9) return false;
      boolean valid = true;
      for (int i=0; i<pid.length(); i++) {
         char c = pid.charAt(i);
         valid = valid && Character.isDigit(c);
      }
      return valid;
   }

   private static List<Map<String, String>> readPassports(List<String> l) {
      List<Map<String, String>> passports = new ArrayList<>();

      Map<String, String> currentPassport = new HashMap<>();
      passports.add(currentPassport);

      for (String line : l) {
         if (line.isEmpty()) { // change of passport 
            currentPassport = new HashMap<>();
            passports.add(currentPassport);
         } else {
            String[] parts = line.split(" ");
            for (String part : parts) {
               String[] props = part.split(":");
               String fieldName = props[0];
               String fieldValue = props[1];
               currentPassport.put(fieldName, fieldValue);
            }
         }
      }
      return passports;
   }
}
