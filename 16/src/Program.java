import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
      List<String> l = Files.readAllLines(Paths.get("D:\\src\\practicas\\AOC_2020\\16\\src\\problem.txt"));

      // Parseamos por separado los "fields" y los "tickets"
      List<Field> fields = new ArrayList<>();
      int idx;
      for (idx = 0; idx < l.size(); idx++) {
         String line = l.get(idx);
         if (line.isEmpty()) break;

         fields.add(new Field(line));
      }

      idx++;
      idx++;

      Ticket myTicket = new Ticket(l.get(idx++));

      idx++;
      idx++;

      List<Ticket> nearbyTickets = new ArrayList<>();
      for (; idx < l.size(); idx++) {
         String line = l.get(idx);

         nearbyTickets.add(new Ticket(line));
      }

      String result = partOne(fields, nearbyTickets);
      System.out.println("Part one - solution: " + result);

      result = partTwo(fields, nearbyTickets, myTicket);
      System.out.println("Part two - solution: " + result);
   }

   private static String partOne(List<Field> fields, List<Ticket> tickets) {
      long er = 0;
      for (Ticket t : tickets) {
         er += t.errorRate(fields);
      }
      return String.valueOf(er);
   }

   private static String partTwo(List<Field> fields, List<Ticket> tickets, Ticket myTicket) throws Exception {
      tickets.add(myTicket);
      // Primero pasamos partOne, de manera que los tiquets válidos quedan marcados con un 'errorRate' > 0
      partOne(fields, tickets);

      // a partit de aquí tranbajamos sólo con los tiquets validos (errorRate==0)

      // Creamos una nueva lista 'posibilidades' donde, para cada elemento, le hacemos corresponder 
      // un "Set" con todos los campos que son posibles para esa posición.
      // Al principio, para cada posicion de la lista el "Set" asociado contiene todos los campos.
      // Luego, utilizando los tickets,  iremos descartando campos para cada posicion, hasta que, con un 
      // poco de suerte ;-), el "Set" de cada posición contenga sólamente un campo.
      List<Set<Field>> posibilidades = new ArrayList<>(fields.size());
      for (int i = 0; i < fields.size(); i++) {
         posibilidades.add(new HashSet<>(fields));
      }

      for (Ticket t : tickets) {
         if (t.errorRate != 0) continue;
         for (int i = 0; i < t.values.size(); i++) {
            Integer v = t.values.get(i);
            for (Field f : fields) {
               if (!f.accepts(v)) {
                  // Sabemos que en la posición "i" no puede ir el campo "f"
                  Set<Field> camposPosiblesEnPosicion_i = posibilidades.get(i);
                  camposPosiblesEnPosicion_i.remove(f);
               }
            }
         }
      }

      // En este punto ya hemos descartado los campos que no son posibles en cada posición.
      // el la lista 'posibilidades', si en alguna posición el conjunto de campos posibles es vacío,
      // el problema no tendría solución.
      // Si en alguna posición el conjunto de campos posibles tiene un sólo elemento (un solo campo)
      // quiere decir que estamos seguros de que ese campo sólo puede ir en esa posición.
      // Por lo tanto, eliminamos ese campo de los conjuntos posibles del resto de posiciones.
      // Como hemos quitado campos en algunos conjuntos posibles, alguno se habrá quedado con un sólo
      // elemento (un sólo campo) ...
      Integer pos = peekLeastFreedomPosition(posibilidades);
      if (pos == null) throw new Exception("Imposible al principio del problema!!!");
      if (pos == -1) throw new Exception("El problema no tiene solución");
      if (pos == -2) throw new Exception("El problema no tiene una solución obvia");

      do {
         // Fijamos la posición del único campo que queda en el conjunto de posibilidades.
         Field f = posibilidades.get(pos).iterator().next();
         f.position = pos;

         // Al conjunto de campos posibles del resto de posiciones, le quitamos el campo que acabanos de fijar.
         for (int i = 0; i < posibilidades.size(); i++) {
            if (i == pos) continue;
            posibilidades.get(i).remove(f);
         }

         // Repetimos la búsqueda de una posición con sólo un campo posible
         pos = peekLeastFreedomPosition(posibilidades);
         if (pos != null && pos == -1) throw new Exception("El problema no tiene solución");
         if (pos != null && pos == -2) throw new Exception("El problema no tiene una solución obvia");
      } while (pos != null);


      // Ahora hacemos el producto de los valores de mi tiquet que corresponden a campos que 
      // contienen el texto "departure"
      long producto = 1;
      for (Field f : fields) {
         if (f.name.contains("departure")) {
            int position = f.position;
            producto *= myTicket.values.get(position);
         }
      }

      return String.valueOf(producto);
   }

   // Devuelve null cuando ya todos los campos han sido fijados
   // Devuleve -1 si no hay ninguna posición con un sólo campo posible
   // Devuelve -2 si la posición con menos campos posibles tiene más de un campo posible.
   // En caso contrario, devuelve la primera posición con un sólo campo 
   // posible.
   private static Integer peekLeastFreedomPosition(List<Set<Field>> posibilidades) {
      int minSizePos = Integer.MAX_VALUE;
      int minSize = Integer.MAX_VALUE;
      for (int i = 0; i < posibilidades.size(); i++) {
         Set<Field> camposPosiblesEnPosicion_i = posibilidades.get(i);
         if (camposPosiblesEnPosicion_i.size() == 0) return -1;
         if (camposPosiblesEnPosicion_i.size() == 1) {
            Field f = camposPosiblesEnPosicion_i.iterator().next();
            if (f.position != null) continue; // Este campo ya ha sido fijado en esta posición
         }
         if (camposPosiblesEnPosicion_i.size() < minSize) {
            minSize = camposPosiblesEnPosicion_i.size();
            minSizePos = i;
         }
      }

      if (minSizePos == Integer.MAX_VALUE) return null;
      if (minSize > 1) return -2;
      return minSizePos;
   }

   /////////////////////////////////////////////

   private static class Field {
      String name;
      int min1;
      int max1;
      int min2;
      int max2;
      Integer position;

      public Field(String serializedRule) {
         String[] parts = serializedRule.split(": ");
         this.name = parts[0];

         String[] parts2 = parts[1].split(" or ");
         String[] minMax = parts2[0].split("-");
         this.min1 = Integer.parseInt(minMax[0]);
         this.max1 = Integer.parseInt(minMax[1]);
         minMax = parts2[1].split("-");
         this.min2 = Integer.parseInt(minMax[0]);
         this.max2 = Integer.parseInt(minMax[1]);
         this.position = null;
      }

      public boolean accepts(Integer v) {
         return (min1 <= v && max1 >= v) || (min2 <= v && max2 >= v);
      }

      @Override
      public String toString() {
         return this.name;
      }
   }

   private static class Ticket {
      String csv_values;
      List<Integer> values;
      Long errorRate = null;

      public Ticket(String csv_values) {
         this.csv_values = csv_values;
         values = new ArrayList<>();
         String[] parts = csv_values.split(",");
         for (String sValue : parts) {
            values.add(Integer.parseInt(sValue));
         }
      }

      public long errorRate(List<Field> fields) {
         if (errorRate != null) return errorRate;

         long er = 0L;
         for (Integer v : values) {
            boolean invalidForAll = true;
            for (Field field : fields) {
               boolean validForThis = field.accepts(v);
               if (validForThis) {
                  invalidForAll = false;
                  break;
               }
            }
            if (invalidForAll) er += v;
         }
         errorRate = er;
         return errorRate;
      }
   }
}
