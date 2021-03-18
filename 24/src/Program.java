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
        List<String> l = Files.readAllLines(Paths.get("D:\\src\\practicas\\AOC_2020\\24\\src\\problem.txt"));

        List<List<String>> rutas = parseProblemInput(l);
        Map<String, Tile> tiles = new HashMap<>();

        String result = partOne(rutas, tiles);
        System.out.println("Part one - solution: " + result);

        result = partTwo(tiles);
        System.out.println("Part two - solution: " + result);
    }

    private static String partOne(List<List<String>> rutas, Map<String, Tile> tiles) throws Exception {
        for (List<String> ruta : rutas) {
            Tile newTile = new Tile(ruta);
            Tile t = tiles.get(newTile.toString());
            if (t == null) {
                tiles.put(newTile.toString(), newTile);
            } else {
                t.flipColor();
            }
        }
        int nBlackTiles = 0;
        for (Tile t : tiles.values()) {
            if (t.color.equals("black")) {
                nBlackTiles++;
            }
        }
        return String.valueOf(nBlackTiles);
    }

    private static String partTwo(Map<String, Tile> tiles) throws Exception {
        Tile[][] tilesArray = new Tile[200][200];
        for (Tile t : tiles.values()) {
            tilesArray[100 + t.y][100 + t.x] = t;
        }

        //printTiles(tilesArray);
        for (int i = 1; i <= 100; i++) {
            dayFlip(tilesArray);
            //printTiles(tilesArray);
            //System.out.println("Day " + i + ": " + totalBlackTiles(tilesArray) + " black tiles");
        }
        return String.valueOf(totalBlackTiles(tilesArray));
    }

    private static List<List<String>> parseProblemInput(List<String> l) {
        List<List<String>> rutas = new ArrayList<>();
        for (String line : l) {
            List<String> ruta = parseRuta(line);
            rutas.add(ruta);
        }
        return rutas;
    }

    private static List<String> parseRuta(String line) {
        List<String> ruta = new ArrayList<>();
        int i = 0;
        while (i < line.length()) {
            if (line.charAt(i) == 'e' || line.charAt(i) == 'w') {
                String step = String.valueOf(line.charAt(i));
                ruta.add(step);
                i++;
            } else {
                String step = line.substring(i, i + 2);
                ruta.add(step);
                i += 2;
            }
        }
        return ruta;
    }

    private static void dayFlip(Tile[][] tilesArray) {
        Tile[][] newTilesArray = new Tile[tilesArray.length][tilesArray[0].length];
        for (int j = 0; j < tilesArray.length; j++) {
            for (int i = 0; i < tilesArray[0].length; i++) {
                if (tileGoesBlack(i, j, tilesArray)) {
                    Tile newTile = new Tile(i, j);
                    newTilesArray[i][j] = newTile;
                }
            }
        }
        for (int i = 0; i < tilesArray.length; i++) {
            for (int j = 0; j < tilesArray[0].length; j++) {
                tilesArray[i][j] = newTilesArray[i][j];
            }
        }
    }

    private static boolean tileGoesBlack(int x, int y, Tile[][] tilesArray) {
        int nBlackNeighbours = totalBlackNeighbours(x, y, tilesArray);
        Tile t = tilesArray[y][x];
        if (t == null || t.color.equals("white")) {
            return nBlackNeighbours == 2;
        } else {
            return nBlackNeighbours == 1 || nBlackNeighbours == 2;
        }
    }

    private static int totalBlackNeighbours(int x, int y, Tile[][] tilesArray) {
        int nBlackTiles = 0;
        if (x - 1 > 0 && tilesArray[y][x - 1] != null && tilesArray[y][x - 1].color.equals("black")) {
            nBlackTiles++;
        }
        if (y + 1 < tilesArray.length && tilesArray[y + 1][x] != null && tilesArray[y + 1][x].color.equals("black")) {
            nBlackTiles++;
        }
        if (x + 1 < tilesArray[0].length && y + 1 < tilesArray.length && tilesArray[y + 1][x + 1] != null && tilesArray[y + 1][x + 1].color.equals("black")) {
            nBlackTiles++;
        }
        if (x + 1 < tilesArray[0].length && tilesArray[y][x + 1] != null && tilesArray[y][x + 1].color.equals("black")) {
            nBlackTiles++;
        }
        if (y - 1 > 0 && tilesArray[y - 1][x] != null && tilesArray[y - 1][x].color.equals("black")) {
            nBlackTiles++;
        }
        if (x - 1 > 0 && y - 1 > 0 && tilesArray[y - 1][x - 1] != null && tilesArray[y - 1][x - 1].color.equals("black")) {
            nBlackTiles++;
        }
        return nBlackTiles;
    }

    private static int totalBlackTiles(Tile[][] tilesArray) {
        int nBlackTiles = 0;
        for (int i = 0; i < 200; i++) {
            for (int j = 0; j < 200; j++) {
                Tile t = tilesArray[i][j];
                if (t != null && t.color.equals("black")) {
                    nBlackTiles++;
                }
            }
        }
        return nBlackTiles;
    }

    private static void printTiles(Tile[][] tilesArray) {
        for (int j = 0; j < tilesArray.length; j++) {
            for (int i = 0; i < tilesArray[0].length; i++) {
                if (tilesArray[j][i] == null) System.out.print("   ");
                else if (tilesArray[j][i].color.equals("white")) System.out.print("|.|");
                else if (tilesArray[j][i].color.equals("black")) System.out.print("|#|");
                else System.out.print('O');
            }
            System.out.println();
        }
    }

    ///////////////////////////////

    private static class Tile {
        Integer x;
        Integer y;
        String color;

        public Tile(List<String> ruta) {
            int _x = 0;
            int _y = 0;
            for (String step : ruta) {
                switch (step) {
                    case "e":
                        _x++;
                        break;
                    case "w":
                        _x--;
                        break;
                    case "nw":
                        _y++;
                        break;
                    case "se":
                        _y--;
                        break;
                    case "ne":
                        _x++;
                        _y++;
                        break;
                    default:
                        assert step.equals("sw");
                        _x--;
                        _y--;
                        break;
                }
            }
            this.x = _x;
            this.y = _y;
            this.color = "black";
        }

        public Tile(int x, int y) {
            this.x = x;
            this.y = y;
            this.color = "black";
        }

        public void flipColor() {
            if (color.equals("black")) color = "white";
            else color = "black";
        }

        @Override
        public String toString() {
            return "[" + x + ", " + y + "]";
        }

        @Override
        public boolean equals(Object obj) {
            Tile other = (Tile) obj;
            return this.toString().equals(other.toString());
        }
    }
}
