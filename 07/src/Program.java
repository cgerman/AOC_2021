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
      List<String> l = Files.readAllLines(Paths.get("D:\\src\\practicas\\AOC_2020\\07\\src\\problem.txt"));

      List<BagRule> bagRules = parseBagRules(l);

      String result = partOne(bagRules);
      System.out.println("Part one - solution: " + result);
      result = partTwo(bagRules);
      System.out.println("Part two - solution: " + result);
   }

   private static String partOne(List<BagRule> bagRules) {
      Set<String> testLabels = getContainers(bagRules, "shiny gold");
      Set<String> result = partOneStep(bagRules, testLabels);
      result.remove("shiny gold");
      return String.valueOf(result.size());
   }

   private static Set<String> partOneStep(List<BagRule> bagRules, Set<String> testLabels) {
      Set<String> containerLabels = new HashSet<>();
      for (String label : testLabels) {
         Set<String> c2 = getContainers(bagRules, label);
         containerLabels.addAll(c2);
      }
      containerLabels.removeAll(testLabels);

      if (containerLabels.size() == 0) return testLabels;
      
      Set<String> result = partOneStep(bagRules, containerLabels);
      result.addAll(testLabels);
      return result;
   }

   private static Set<String> getContainers(List<BagRule> bagRules, String label) {
      Set<String> containers = new HashSet<>();
      for (BagRule br : bagRules) {
         if (br.contains(label)) containers.add(br.bagLabel);
      }
      return containers;
   }

   private static String partTwo(List<BagRule> bagRules) {
      long counter = partTwoStep(bagRules, "shiny gold");      
      return String.valueOf(counter);
   }

   private static long partTwoStep (List<BagRule> bagRules, String label) {
      long counter = 0;
      Map<String, Integer> contained = getContained(bagRules, label);
      for (String containedLabel : contained.keySet()) {
         int n = contained.get(containedLabel);
         counter += n;
         counter += n * partTwoStep(bagRules, containedLabel);
      }
      return counter;
   }
   
   private static Map<String, Integer> getContained(List<BagRule> bagRules, String label) {
      for (BagRule br : bagRules) {
         if (br.bagLabel.equals(label)) return br.contains;
      }
      return new HashMap<>();
   }

   private static List<BagRule> parseBagRules(List<String> l) {
      List<BagRule> bagRules = new ArrayList<>();

      for (String line : l) {
         String[] parts = line.split(" bags contain ");
         BagRule br = new BagRule(parts[0].trim());
         String containedLabels = parts[1];
         parts = containedLabels.split(", ");
         for (String part : parts) {
            String[] words = part.split(" ");
            int number;
            try {
               number = Integer.parseInt(words[0]);
               String label = "";
               for (int i = 1; i < words.length - 1; i++) {
                  label += words[i];
                  if (i + 1 < words.length - 1) label += " ";
               }
               br.add(number, label);
            } catch (NumberFormatException e) {
               //e.printStackTrace();
            }
         }
         bagRules.add(br);
      }
      return bagRules;
   }

   private static class BagRule {
      protected String bagLabel;
      protected Map<String, Integer> contains;

      public BagRule(String bagLabel) {
         this.bagLabel = bagLabel;
         contains = new HashMap<>();
      }

      public void add(int n, String label) {
         contains.put(label, n);
      }

      @Override
      public String toString() {
         String result = bagLabel + " ==> ";
         for (String label : contains.keySet()) {
            result += label + "(" + contains.get(label) + ") ";
         }
         return result;
      }

      public boolean contains(String label) {
         return contains.containsKey(label);
      }
   }
}
