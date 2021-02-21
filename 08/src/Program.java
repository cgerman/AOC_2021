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
      List<String> l = Files.readAllLines(Paths.get("D:\\src\\practicas\\AOC_2020\\08\\src\\problem.txt"));

      List<Instruction> program = parseProgram(l);

      String result = partOne(program);
      System.out.println("Part one - solution: " + result);
      result = partTwo(program);
      System.out.println("Part two - solution: " + result);
   }

   private static String partOne(List<Instruction> program) {
      long accumulator = 0;
      try {
         accumulator = runProgram(program, false);
      } catch (Exception e) {
         // It does not throw
      }
      return String.valueOf(accumulator);
   }

   private static String partTwo(List<Instruction> program) {
      for (Instruction ins : program) {
         if (ins.code.equals("jmp") || ins.code.equals("nop")) {
            if (ins.code.equals("jmp")) {
               ins.code = "nop";
            } else if (ins.code.equals("nop")) {
               ins.code = "jmp";
            }
            try {
               long accumulator = runProgram(program, true);
               return String.valueOf(accumulator);
            } catch (Exception e) {
               // Infinite loop. Do nothing, try with the next program variation
               // revert the variation
               if (ins.code.equals("jmp")) {
                  ins.code = "nop";
               } else if (ins.code.equals("nop")) {
                  ins.code = "jmp";
               }
               // reset instruction counters
               for (Instruction ins2 : program) ins2.timesExecuted = 0;
            }
         }
      }
      return "No hemos conseguido reparar el programa.";
   }

   private static List<Instruction> parseProgram(List<String> l) {
      List<Instruction> program = new ArrayList<>();
      for (String line : l) {
         String[] parts = line.split(" ");
         Instruction ins = new Instruction(parts[0], Integer.parseInt(parts[1]));
         program.add(ins);
      }
      return program;
   }

   private static long runProgram(List<Instruction> program, boolean throwIfInfiniteLoop) throws Exception {
      long accumulator = 0;
      int nextInstruction = 0;
      while (nextInstruction < program.size()) {
         Instruction ins = program.get(nextInstruction);
         if (ins.timesExecuted == 1) {
            if (throwIfInfiniteLoop) throw new Exception("Infinite Loop");
            else break;
         }
         ins.timesExecuted++;
         if (ins.code.equals("acc")) {
            accumulator += ins.number;
            nextInstruction = nextInstruction + 1;
         } else if (ins.code.equals("nop")) {
            nextInstruction = nextInstruction + 1;
         } else { // "jmp"
            nextInstruction = nextInstruction + ins.number;
         }
      }
      return accumulator;
   }

   //////////////////////

   private static class Instruction {
      String code;
      int number;
      int timesExecuted;

      public Instruction(String code, int number) {
         this.code = code;
         this.number = number;
         this.timesExecuted = 0;
      }
   }
}
