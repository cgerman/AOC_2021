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
      List<String> l = Files.readAllLines(Paths.get("D:\\src\\practicas\\AOC_2020\\18\\src\\problem.txt"));

      String result = partOne(l);
      System.out.println("Part one - solution: " + result);

      result = partTwo(l);
      System.out.println("Part two - solution: " + result);
   }

   private static String partOne(List<String> l) throws Exception {
      long sum = 0L;
      for (String line : l) {
         Node n = parse1(line);
         long calc = n.calculate();
         System.out.println(line + " = " + calc);
         sum += calc;
      }
      return String.valueOf(sum);
   }

   private static String partTwo(List<String> l) throws Exception {
      long sum = 0L;
      for (String line : l) {
         Node n = parse2(line);
         long calc = n.calculate();
         System.out.println(line + " = " + calc);
         sum += calc;
      }
      return String.valueOf(sum);
   }

   private static Node parse1(String line) {
      // Vamos siempre de derecha a izquierda
      int pos = line.length() - 1;
      char c = line.charAt(pos);

      Node n = new Node();
      if (Character.isDigit(c)) {
         int j;
         for (j = pos; j >= 0 && Character.isDigit(line.charAt(j));) j--;
         String vRight = line.substring(j + 1, pos + 1);
         n.right = new TerminalNode(vRight);

         pos = j - 1; // quitamos el espacio en blanco

         n.value = String.valueOf(line.charAt(pos));

         pos = pos - 2; // saltamos el operador y el espacio en blanco

         if (Character.isDigit(line.charAt(pos))) {
            for (j = pos; j >= 0 && Character.isDigit(line.charAt(j));) j--;
            if (j == -1) {
               String vLeft = line.substring(0, pos + 1);
               n.left = new TerminalNode(vLeft);
               return n;
            }
         }

         String newLine = line.substring(0, pos + 1);
         n.left = parse1(newLine);
      } else {
         // Es un paréntesis cerrado
         pos--; // lo saltamos
         int nInnerParenthesis = 0;
         int j;
         for (j = pos; j >= 0 && (line.charAt(j) != '(' || nInnerParenthesis > 0); j--) {
            if (line.charAt(j) == ')') nInnerParenthesis++;
            else if (line.charAt(j) == '(') nInnerParenthesis--;
         }
         if (j == 0) {
            String term = line.substring(1, pos + 1);
            return parse1(term);
         }

         String rightTerm = line.substring(j + 1, pos + 1);
         n.right = parse1(rightTerm);

         pos = j - 2; //saltamos el paréntesis abierto y el espacio

         n.value = String.valueOf(line.charAt(pos));

         pos = pos - 2; // saltamos el operador y el espacio en blanco

         if (Character.isDigit(line.charAt(pos))) {
            for (j = pos; j >= 0 && Character.isDigit(line.charAt(j));) j--;
            if (j == -1) {
               String vLeft = line.substring(0, pos + 1);
               n.left = new TerminalNode(vLeft);
               return n;
            }
         }

         String newLine = line.substring(0, pos + 1);
         n.left = parse1(newLine);
      }
      return n;
   }

   private static Node parse2(String line) {
      // Vamos a poner paréntesis en todas las sumas,
      // y luego pasamos el parsing de parse1()

      int pos = line.indexOf('+');
      if (pos != -1) {
         do {
            // En la posicion 'pos' tenemos un signo '+'

            // Buscamos hacia la izquierda hasta encontrar el principio del primer sumando y antemponemos un paréntesis abierto
            int i = pos - 2;
            if (Character.isDigit(line.charAt(i))) {
               int j;
               for (j = i; j >= 0 && Character.isDigit(line.charAt(j)); ) j--;
               if (j == 0) {
                  line = "(" + line;
               } else {
                  line = line.substring(0, j + 1) + "(" + line.substring(j + 1);
               }
               pos++;
            } else {
               // Es un paréntesis cerrado
               i--; // lo saltamos
               int nInnerParenthesis = 0;
               int j;
               for (j = i; j >= 0 && (line.charAt(j) != '(' || nInnerParenthesis > 0); j--) {
                  if (line.charAt(j) == ')') nInnerParenthesis++;
                  else if (line.charAt(j) == '(') nInnerParenthesis--;
               }
               if (j == 0) {
                  line = "(" + line;
               } else {
                  line = line.substring(0, j + 1) + "(" + line.substring(j + 1);
               }
               pos++;
            }

            // Ahora buscamos hacia la derecha hasta encontrar el final del segundo sumando 
            // y post-ponemos un paréntesis cerrado
            i = pos + 2;
            if (Character.isDigit(line.charAt(i))) {
               int j;
               for (j = i; j < line.length() && Character.isDigit(line.charAt(j)); ) j++;
               if (j == line.length()) {
                  line = line + ')';
               } else {
                  line = line.substring(0, j) + ")" + line.substring(j);
               }
            } else {
               // Es un paréntesis abierto
               i++; // lo saltamos
               int nInnerParenthesis = 0;
               int j;
               for (j = i; j < line.length() && (line.charAt(j) != ')' || nInnerParenthesis > 0); j++) {
                  if (line.charAt(j) == '(') nInnerParenthesis++;
                  else if (line.charAt(j) == ')') nInnerParenthesis--;
               }
               if (j == line.length()) {
                  line = line + ')';
               } else {
                  line = line.substring(0, j) + ")" + line.substring(j);
               }
            }

            pos = line.indexOf('+', pos + 1);
         } while (pos != -1);
      }
      
      return parse1(line);
   }

   ///////////////////////////////

   private static class Node {
      String value;
      Node left;
      Node right;

      public Node() {
         value = null;
         right = null;
         left = null;
      }

      public long calculate() throws Exception {
         long result;
         long v1 = left.calculate();
         long v2 = right.calculate();
         switch (value.charAt(0)) {
            case '+':
               result = v1 + v2;
               break;
            case '*':
               result = v1 * v2;
               break;
            default:
               throw new Exception("Unexpected operator: " + value);
         }
         return result;
      }
   }

   private static class TerminalNode extends Node {
      public TerminalNode(String value) {
         super();
         this.value = value;
      }

      @Override
      public long calculate() {
         return Long.parseLong(value);
      }
   }

}
