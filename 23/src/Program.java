import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Project     : Advent of Code
 * Headline    : Practice programming.
 * <p>
 * (C) 2021 Carlos German Romero
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * For more information, see see <http://www.gnu.org/licenses/>
 */

public class Program {
    public static void main(String[] args) throws Exception {
        List<String> l = Files.readAllLines(Paths.get("D:\\src\\practicas\\AOC_2020\\23\\src\\problem.txt"));
        Circle circle = new Circle(l.get(0));

        String result = partOne(circle);
        System.out.println("Part one - solution: " + result);

        circle = new Circle(l.get(0));
        result = partTwo(circle);
        System.out.println("Part two - solution: " + result);
    }

    private static String partOne(Circle circle) throws Exception {
        for (int i = 0; i < 100; i++) {
            doOneMove(circle);
        }
        Cup one = circle.get(1);
        Cup c = one.next;
        StringBuilder sb = new StringBuilder();
        while (!c.equals(one)) {
            sb.append(c.label);
            c = c.next;
        }
        return sb.toString();
    }

    private static String partTwo(Circle circle) throws Exception {
        circle.growToMillion();

        for (int i = 0; i < 10000000; i++) {
            doOneMove(circle);
            if (i % 1000 == 0) System.out.print("\rMoves: " + i);
        }
        System.out.print("\rMoves: " + 10000000);
        System.out.println();
        Cup one = circle.get(1);
        Cup c1 = one.next;
        Cup c2 = c1.next;
        Long c1Label = new Long(c1.label);
        Long c2Label = new Long(c2.label);
        return String.valueOf(c1Label * c2Label);
    }

    private static void doOneMove(Circle circle) {
        List<Cup> removed = circle.removeNextThree();

        Cup destination = findDestination(circle);

        circle.insertThreeAfterDestination(removed, destination);

        circle.currentCup = circle.currentCup.next;
    }

    private static Cup findDestination(Circle circle) {
        Cup c;
        Integer searchLabel = circle.currentCup.label - 1;
        do {
            c = circle.get(searchLabel);
            if (c == null) {
                searchLabel = searchLabel < circle.lowestLabel ? circle.highestLabel : searchLabel - 1;
            }
        } while (c == null);
        return c;
    }

    /////////////////////////////////////

    private static class Circle {
        private Cup currentCup;
        Integer lowestLabel;
        Integer highestLabel;
        Map<Integer, Cup> cups;

        public Circle(String s) {
            cups = new HashMap<>();
            currentCup = new Cup(s.charAt(0));
            cups.put(currentCup.label, currentCup);
            lowestLabel = currentCup.label;
            highestLabel = currentCup.label;
            Cup c = currentCup;
            for (int i = 1; i < s.length(); i++) {
                char label = s.charAt(i);
                Cup newCup = new Cup(label);
                cups.put(newCup.label, newCup);
                if (newCup.label > highestLabel) highestLabel = newCup.label;
                if (newCup.label < lowestLabel) lowestLabel = newCup.label;
                c.insertAfter(newCup);
                c = newCup;
            }
        }

        public void growToMillion() {
            Cup c = currentCup.prev;
            for (int i = highestLabel + 1; i <= 1000000; i++) {
                Cup newCup = new Cup(i);
                cups.put(newCup.label, newCup);
                c.insertAfter(newCup);
                c = newCup;
            }
            highestLabel = 1000000;
        }

        public List<Cup> removeNextThree() {
            List<Cup> removed = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                Cup c = currentCup.removeAfter();
                cups.remove(c.label);
                removed.add(c);
            }
            return removed;
        }

        public void insertThreeAfterDestination(List<Cup> toInsert, Cup destination) {
            for (Cup c : toInsert) {
                cups.put(c.label, c);
                destination.insertAfter(c);
                destination = c;
            }
        }

        public Cup get(Integer label) {
            return cups.get(label);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            Cup c = currentCup;
            for (int i = 0; i < 10; i++) {
                sb.append(c.label).append(" ");
                c = c.next;
            }
            sb.append("...");
            return sb.toString();
        }
    }

    private static class Cup {
        Integer label;
        Cup next;
        Cup prev;

        public Cup(Integer label) {
            this.label = label;
            this.next = null;
            this.prev = null;
        }

        public Cup(char label) {
            this(Integer.parseInt(String.valueOf(label)));
        }

        public void insertAfter(Cup c) {
            if (next != null) {
                Cup nextCup = next;
                next = c;
                c.prev = this;
                c.next = nextCup;
                nextCup.prev = c;
            } else {
                next = c;
                prev = c;
                c.prev = this;
                c.next = this;
            }
        }

        public Cup removeAfter() {
            Cup newNextCup = this.next.next;
            Cup c = this.next;

            this.next = newNextCup;
            newNextCup.prev = this;

            c.next = null;
            c.prev = null;

            return c;
        }

        @Override
        public boolean equals(Object obj) {
            Cup otherCup = (Cup) obj;
            return this.label.equals(((Cup) obj).label);
        }

        @Override
        public String toString() {
            return "[" + label + "] prev=" + (prev == null ? "null" : prev.label) + ", next=" + (next == null ? "null" : next.label);
        }
    }
}
