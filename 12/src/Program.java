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
      List<String> l = Files.readAllLines(Paths.get("D:\\src\\practicas\\AOC_2020\\12\\src\\problem.txt"));

      String result = partOne(l);
      System.out.println("Part one - solution: " + result);
      result = partTwo(l);
      System.out.println("Part two - solution: " + result);
   }

   private static String partOne(List<String> l) throws Exception {
      int pos_x = 0;
      int pos_y = 0;
      String direction = "EAST";

      for (String line : l) {
         char action = line.charAt(0);
         int value = Integer.parseInt(line.substring(1));
         switch (action) {
            case 'N': // Move NORTH, direction unchanged
               pos_y += value;
               break;
            case 'S': // Move SOUTH, direction unchanged
               pos_y -= value;
               break;
            case 'E': // Move EAST, direction unchanged
               pos_x += value;
               break;
            case 'W': // Move WEST, direction unchanged
               pos_x -= value;
               break;
            case 'L': // position unchanged, change direction to the LEFT
               direction = left(direction, value);
               break;
            case 'R': // position unchanged, change direction to the RIGHT
               direction = right(direction, value);
               break;
            case 'F': // Move FORWARD in the current direction, direction unchanged
               switch (direction) {
                  case "NORTH":
                     pos_y += value;
                     break;
                  case "SOUTH":
                     pos_y -= value;
                     break;
                  case "EAST":
                     pos_x += value;
                     break;
                  case "WEST":
                     pos_x -= value;
                     break;
               }
               break;
            default:
               throw new Exception("Unsupported action: " + action);
         }
      }
      return String.valueOf(Math.abs(pos_x) + Math.abs(pos_y));
   }

   private static String partTwo(List<String> l) throws Exception {
      int wp_x = 10;
      int wp_y = 1;
      int pos_x = 0;
      int pos_y = 0;

      for (String line : l) {
         char action = line.charAt(0);
         int value = Integer.parseInt(line.substring(1));
         switch (action) {
            case 'N': // Move wp NORTH, ship position unchanged
               wp_y += value;
               break;
            case 'S': // Move wp SOUTH, ship position unchanged
               wp_y -= value;
               break;
            case 'E': // Move wp EAST, ship position unchanged
               wp_x += value;
               break;
            case 'W': // Move wp WEST, ship position unchanged
               wp_x -= value;
               break;
            case 'L': // rotate wp to the LEFT specified degrees, ship position unchanged
            {
               int[] newWP = left(value, wp_x, wp_y);
               wp_x = newWP[0];
               wp_y = newWP[1];
            }
            break;
            case 'R': // rotate wp to the RIGHT specified degrees, ship position unchanged
            {
               int[] newWP = right(value, wp_x, wp_y);
               wp_x = newWP[0];
               wp_y = newWP[1];
            }
            break;
            case 'F': // Move the ship FORWARD to the wp specified times, wp position (relative to the ship) unchanged
               pos_x += value * wp_x;
               pos_y += value * wp_y;
               break;
            default:
               throw new Exception("Unsupported action: " + action);
         }
      }
      return String.valueOf(Math.abs(pos_x) + Math.abs(pos_y));
   }

   private static String left(String direction, int value) throws Exception {
      switch (direction) {
         case "NORTH":
            return (value == 0 ? "NORTH" : value == 90 ? "WEST" : value == 180 ? "SOUTH" : "EAST");
         case "SOUTH":
            return (value == 0 ? "SOUTH" : value == 90 ? "EAST" : value == 180 ? "NORTH" : "WEST");
         case "EAST":
            return (value == 0 ? "EAST" : value == 90 ? "NORTH" : value == 180 ? "WEST" : "SOUTH");
         case "WEST":
            return (value == 0 ? "WEST" : value == 90 ? "SOUTH" : value == 180 ? "EAST" : "NORTH");
         default:
            throw new Exception("Unsupported direction: " + direction);
      }
   }

   private static String right(String direction, int value) throws Exception {
      switch (direction) {
         case "NORTH":
            return (value == 0 ? "NORTH" : value == 90 ? "EAST" : value == 180 ? "SOUTH" : "WEST");
         case "SOUTH":
            return (value == 0 ? "SOUTH" : value == 90 ? "WEST" : value == 180 ? "NORTH" : "EAST");
         case "EAST":
            return (value == 0 ? "EAST" : value == 90 ? "SOUTH" : value == 180 ? "WEST" : "NORTH");
         case "WEST":
            return (value == 0 ? "WEST" : value == 90 ? "NORTH" : value == 180 ? "EAST" : "SOUTH");
         default:
            throw new Exception("Unsupported direction: " + direction);
      }
   }

   private static int[] left(int value, int x, int y) throws Exception {
      int[] newPos = new int[2];
      switch (value) {
         case 0:
            newPos[0] = x;
            newPos[1] = y;
            break;
         case 90:
            newPos[0] = -1 * y;
            newPos[1] = x;
            break;
         case 180:
            newPos[0] = -1 * x;
            newPos[1] = -1 * y;
            break;
         case 270:
            newPos[0] = y;
            newPos[1] = -1 * x;
            break;
         default:
            throw new Exception("Unexpected rotation value: " + value);
      }
      return newPos;
   }

   private static int[] right(int value, int x, int y) throws Exception {
      int[] newPos = new int[2];
      switch (value) {
         case 0:
            newPos[0] = x;
            newPos[1] = y;
            break;
         case 90:
            newPos[0] = y;
            newPos[1] = -1 * x;
            break;
         case 180:
            newPos[0] = -1 * x;
            newPos[1] = -1 * y;
            break;
         case 270:
            newPos[0] = -1 * y;
            newPos[1] = x;
            break;
         default:
            throw new Exception("Unexpected rotation value: " + value);
      }
      return newPos;
   }
}
