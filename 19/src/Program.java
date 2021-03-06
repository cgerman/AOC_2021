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
      List<String> l = Files.readAllLines(Paths.get("D:\\src\\practicas\\AOC_2020\\19\\src\\problem.txt"));

      String result = partOne(l);
      System.out.println("Part one - solution: " + result);

      result = partTwo(l);
      System.out.println("Part two - solution: " + result);
   }

   private static String partOne(List<String> l) throws Exception {
      Map<Integer, Rule> reglas = new HashMap<>();
      List<String> mensajes = new ArrayList<>();
      parseProblemInput(l, reglas, mensajes);

      Rule rZero = reglas.get(0);
      int total = 0;
      for (int j = 0; j < mensajes.size(); j++) {
         String msg = mensajes.get(j);
         int i = rZero.match(reglas, msg);
         if (i == -1) continue;
         if (i < msg.length()) continue;
         total++;
      }
      return String.valueOf(total);
   }

   private static String partTwo(List<String> l) throws Exception {
      Map<Integer, Rule> reglas = new HashMap<>();
      List<String> mensajes = new ArrayList<>();
      parseProblemInput(l, reglas, mensajes);

      // La regla 0 es la concatenación de la 8 y la 11
      // La nueva regla 8 viene a ser "una o más repeticiones de la 42"
      // la nueva regla 11 viene a ser "una o más repeticiones de la 42, seguidas por el mismo número de repeticiones de la 31
      // Por tanto, los mensajes válidos serán aquellos que empiezan por n expresiones de la 42, 
      // seguidos por m expresiones de la 31, con n>m y m>=1

      List<StringBuilder> expressions42 = new ArrayList<>();
      Rule r42 = reglas.get(42);
      r42.express(reglas, expressions42);
      List<String> flatExp42 = new ArrayList<>(expressions42.size());
      expressions42.forEach(stringBuilder -> flatExp42.add(stringBuilder.toString()));

      List<StringBuilder> expressions31 = new ArrayList<>();
      Rule r31 = reglas.get(31);
      r31.express(reglas, expressions31);
      List<String> flatExp31 = new ArrayList<>(expressions31.size());
      expressions31.forEach(stringBuilder -> flatExp31.add(stringBuilder.toString()));

      // Sabemos que los mensajes válidos serán aquellos que empiezan por n expresiones de la 42, 
      // seguidos por m expresiones de la 31, con n>m y m>=1
      int nMensajesValidos = 0;
      List<Mensaje> mensajesTratados = new ArrayList<>();
      for (String msg : mensajes) {
         Mensaje m = new Mensaje(msg);
         mensajesTratados.add(m);
         m.count31(flatExp31);
         m.count42(flatExp42);
         if (m.n31 > 0 && m.n42 > m.n31 && m.textWithout31andWithout42.length() == 0) nMensajesValidos++;
      }

      return String.valueOf(nMensajesValidos);
   }

   private static void parseProblemInput(List<String> l, Map<Integer, Rule> reglas, List<String> mensajes) {
      int i = 0;
      // Primero parseamos las reglas
      for (; i < l.size() && !l.get(i).isEmpty(); i++) {
         String[] parts = l.get(i).split(": ");
         int ruleId = Integer.parseInt(parts[0]);
         int begin = parts[1].indexOf("\"");
         if (begin >= 0) {
            // Regla terminal
            begin++;
            int end = parts[1].indexOf("\"", begin);
            String literal = parts[1].substring(begin, end);
            RuleTerminal rt = new RuleTerminal(ruleId, literal);
            reglas.put(ruleId, rt);
         } else {
            begin = parts[1].indexOf(" | ");
            if (begin < 0) {
               // Regla secuencia
               String[] secuencia = parts[1].split(" ");
               RuleSecuencia rs = new RuleSecuencia(ruleId);
               rs.secuencia = secuencia;
               reglas.put(ruleId, rs);
            } else {
               // regla alternativa
               String[] alternativas = parts[1].split(" \\| ");
               String[] secuencia1 = alternativas[0].split(" ");
               String[] secuencia2 = alternativas[1].split(" ");
               RuleAlternativa ra = new RuleAlternativa(ruleId);
               ra.secuencia1 = secuencia1;
               ra.secuencia2 = secuencia2;
               reglas.put(ruleId, ra);
            }
         }
      }
      i++;  // Saltamos la línea vacía de separación entre reglas y mensajes

      mensajes.addAll(l.subList(i, l.size()));
   }

   //////////////////////

   private static abstract class Rule {
      Integer id;

      public Rule(Integer id) {
         this.id = id;
      }

      // Devuelve la posición de la primera letra de text que no está incluida en el "match".
      // Devuelve -1 si no hay "match"
      public abstract int match(Map<Integer, Rule> reglas, String text);

      public abstract void express(Map<Integer, Rule> reglas, List<StringBuilder> expressions);

      @Override
      public String toString() {
         return id + ": ";
      }

   }

   private static class RuleTerminal extends Rule {
      String c;

      public RuleTerminal(Integer id, String c) {
         super(id);
         this.c = c;
      }

      @Override
      public int match(Map<Integer, Rule> reglas, String text) {
         return text.startsWith(c) ? c.length() : -1;
      }

      @Override
      public void express(Map<Integer, Rule> reglas, List<StringBuilder> expressions) {
         if (expressions.size() == 0) expressions.add(new StringBuilder(c));
         else {
            for (StringBuilder exp : expressions) {
               exp.append(c);
            }
         }
      }

      @Override
      public String toString() {
         return super.toString() + "\"" + c + "\"";
      }
   }

   private static class RuleSecuencia extends Rule {
      String[] secuencia;

      public RuleSecuencia(Integer id) {
         super(id);
      }

      @Override
      public int match(Map<Integer, Rule> reglas, String text) {
         int offset = 0;
         for (String sId : secuencia) {
            int id = Integer.parseInt(sId);
            Rule r = reglas.get(id);
            int i = r.match(reglas, text.substring(offset));
            if (i == -1) return -1;
            offset += i;
         }
         return offset;
      }

      @Override
      public void express(Map<Integer, Rule> reglas, List<StringBuilder> expressions) {
         for (String sId : secuencia) {
            int id = Integer.parseInt(sId);
            Rule r = reglas.get(id);
            r.express(reglas, expressions);
         }
      }

      @Override
      public String toString() {
         StringBuilder sb = new StringBuilder();
         for (String sId : secuencia) sb.append(sId).append(" ");
         return super.toString() + sb.toString();
      }
   }

   private static class RuleAlternativa extends Rule {
      String[] secuencia1;
      String[] secuencia2;

      public RuleAlternativa(Integer id) {
         super(id);
      }

      @Override
      public int match(Map<Integer, Rule> reglas, String text) {
         RuleSecuencia alternativa1 = new RuleSecuencia(-1);
         alternativa1.secuencia = secuencia1;
         int i = alternativa1.match(reglas, text);
         if (i == -1) {
            RuleSecuencia alternativa2 = new RuleSecuencia(-1);
            alternativa2.secuencia = secuencia2;
            int j = alternativa2.match(reglas, text);

            return j == -1 ? -1 : j;
         } else {
            return i;
         }
      }

      @Override
      public void express(Map<Integer, Rule> reglas, List<StringBuilder> expressions) {
         // Duplicamos la lista de expresiones recibida
         List<StringBuilder> expressions2 = new ArrayList<>(expressions.size());
         for (StringBuilder exp : expressions) {
            StringBuilder exp2 = new StringBuilder(exp.toString());
            expressions2.add(exp2);
         }
         RuleSecuencia alternativa1 = new RuleSecuencia(-1);
         alternativa1.secuencia = secuencia1;
         alternativa1.express(reglas, expressions);

         RuleSecuencia alternativa2 = new RuleSecuencia(-1);
         alternativa2.secuencia = secuencia2;
         alternativa2.express(reglas, expressions2);

         expressions.addAll(expressions2);
      }

      @Override
      public String toString() {
         StringBuilder sb = new StringBuilder();
         for (String sId : secuencia1) sb.append(sId).append(" ");
         sb.append("| ");
         for (String sId : secuencia2) sb.append(sId).append(" ");
         return super.toString() + sb.toString();
      }
   }

   private static class Mensaje {
      String text;
      int n31;
      String textWithout31;
      int n42;
      String textWithout31andWithout42;

      public Mensaje(String text) {
         this.text = text;
         n31 = 0;
         n42 = 0;
      }

      public void count31(List<String> expresiones31) {
         int n31 = 0;
         String exp31;
         String newText = this.text;
         do {
            exp31 = null;
            for (int i = 0; i < expresiones31.size(); i++) {
               if (newText.endsWith(expresiones31.get(i))) {
                  exp31 = expresiones31.get(i);
                  break;
               }
            }
            if (exp31 != null) {
               n31++;
               newText = newText.substring(0, newText.length() - exp31.length());
            }
         } while (exp31 != null);
         this.n31 = n31;
         this.textWithout31 = newText;
      }

      public void count42(List<String> expresiones42) {
         int n42 = 0;
         String exp42;
         String newText = this.textWithout31;
         do {
            exp42 = null;
            for (int i = 0; i < expresiones42.size(); i++) {
               if (newText.endsWith(expresiones42.get(i))) {
                  exp42 = expresiones42.get(i);
                  break;
               }
            }
            if (exp42 != null) {
               n42++;
               newText = newText.substring(0, newText.length() - exp42.length());
            }
         } while (exp42 != null);
         this.n42 = n42;
         this.textWithout31andWithout42 = newText;
      }
   }
}
