import java.nio.file.Files;
import java.nio.file.Paths;
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
      List<String> l = Files.readAllLines(Paths.get("D:\\src\\practicas\\AOC_2020\\14\\src\\problem.txt"));

      String result = partOne(l);
      System.out.println("Part one - solution: " + result);
      result = partTwo(l);
      System.out.println("Part two - solution: " + result);
   }

   private static String partOne(List<String> l) {
      Map<Long, Value> memory = new HashMap<>();
      String currentMask = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
      for (String line : l) {
         String newMask = execute(line, memory, currentMask);
         if (newMask != null) currentMask = newMask;
      }

      // now sum the values in memory
      long total = 0;
      for (Long address : memory.keySet()) {
         Value value = memory.get(address);
         long lValue = value.lMaskedValue;
         total += lValue;
      }

      return String.valueOf(total);
   }

   private static String partTwo(List<String> l) {
      Map<Long, Value> memory = new HashMap<>();
      String currentMask = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
      for (String line : l) {
         String newMask = execute2(line, memory, currentMask);
         if (newMask != null) currentMask = newMask;
      }

      // now sum the values in memory
      long total = 0;
      for (Long address : memory.keySet()) {
         Value value = memory.get(address);
         long lValue = value.originalValue;
         total += lValue;
      }

      return String.valueOf(total);
   }

   private static String execute(String instruction, Map<Long, Value> memory, String currentMask) {
      String[] parts = instruction.split(" = ");
      if (parts[0].equals("mask")) return parts[1];

      // Aquí tenemos una asignación de memoria
      String decimalValue = parts[1];
      String address = parts[0].substring(4, parts[0].indexOf(']'));
      memory.put(new Long(address), new Value(Long.parseLong(decimalValue), currentMask));

      return null;
   }

   private static String execute2(String instruction, Map<Long, Value> memory, String currentMask) {
      String[] parts = instruction.split(" = ");
      if (parts[0].equals("mask")) return parts[1];

      // Aquí tenemos una asignación de memoria
      String decimalValue = parts[1];
      String initialDecimalAddress = parts[0].substring(4, parts[0].indexOf(']'));

      // Obtenemos todas las direcciones afectadas aplicando la mascara sobre initialAddress,
      // Y sobre todas ellas escribimos el valor especificado (convertido por la misma máscara).
      String sBinaryAddress = Long.toBinaryString(Long.parseLong(initialDecimalAddress));
      String initialAddress = String.format("%36s", sBinaryAddress).replace(' ', '0');
      String maskedAddress = getMaskedAddress(initialAddress, currentMask);

      // Materializamos la maskedAddress en todas sus posibles physicalAddresses, y 
      // hacemos la asignación del valor en todas ellas
      assign(maskedAddress, 0, memory, new Value(Long.parseLong(decimalValue), currentMask));

      //memory.put(new Long(initialDecimalAddress), new Value(Long.parseLong(decimalValue), currentMask));

      return null;
   }

   private static String getMaskedAddress(String address, String mask) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < 36; i++) {
         char maskBit = mask.charAt(i);
         if (maskBit == '0') sb.append(address.charAt(i));
         else if (maskBit == '1') sb.append("1");
         else sb.append("X");
      }
      return sb.toString();
   }

   // Recursiva
   private static void assign(String maskedAddress, int from, Map<Long, Value> memory, Value v) {
      int i = from;
      while (i < maskedAddress.length() && maskedAddress.charAt(i) != 'X') {
         i++;
      }
      if (i == maskedAddress.length()) {
         memory.put(Long.parseLong(maskedAddress, 2), v);
         return;
      }

      // En este punto sabemos que maskedAddress.charAt(i)=='X'
      String newMaskedAddress = maskedAddress.substring(0, i) + '0';
      if (i + 1 < maskedAddress.length()) newMaskedAddress += maskedAddress.substring(i + 1);
      assign(newMaskedAddress, i, memory, v);

      newMaskedAddress = maskedAddress.substring(0, i) + '1';
      if (i + 1 < maskedAddress.length()) newMaskedAddress += maskedAddress.substring(i + 1);
      assign(newMaskedAddress, i, memory, v);
   }

   /////////////////////////////////

   private static class Value {
      long originalValue;
      String mask;
      String sMaskedValue;
      long lMaskedValue;

      public Value(long originalValue, String mask) {
         this.originalValue = originalValue;
         this.mask = mask;
         applyMask();
      }

      private void applyMask() {
         StringBuilder sb = new StringBuilder();
         String sBinaryNumber = Long.toBinaryString(originalValue);
         sBinaryNumber = String.format("%36s", sBinaryNumber).replace(' ', '0');
         for (int i = 0; i < 36; i++) {
            char maskBit = mask.charAt(i);
            if (maskBit == '0') sb.append("0");
            else if (maskBit == '1') sb.append("1");
            else sb.append(sBinaryNumber.charAt(i));
         }
         sMaskedValue = sb.toString();
         lMaskedValue = Long.parseLong(sMaskedValue, 2);
      }
   }

}
