import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<String> l = Files.readAllLines(Paths.get("D:\\src\\practicas\\AOC_2020\\20\\src\\problem.txt"));

        String result = partOne(l);
        System.out.println("Part one - solution: " + result);

        result = partTwo(l);
        System.out.println("Part two - solution: " + result);
    }

    private static String partOne(List<String> l) {
        Map<Integer, Tile> tiles = new HashMap<>();
        parseProblemInput(l, tiles);

        composeTileMap2(tiles);

        long producto4esquinas = 1L;
        for (Integer i : tiles.keySet()) {
            Tile t = tiles.get(i);
            Integer idCorner = t.getCorner();
            if (idCorner != null) {
                producto4esquinas *= idCorner;
            }
        }

        return String.valueOf(producto4esquinas);
    }

    private static String partTwo(List<String> l) {
        Map<Integer, Tile> tiles = new HashMap<>();
        parseProblemInput(l, tiles);

        composeTileMap2(tiles);

        char[][] monster = new char[3][];
        monster[0] = "                  # ".toCharArray();
        monster[1] = "#    ##    ##    ###".toCharArray();
        monster[2] = " #  #  #  #  #  #   ".toCharArray();

        char[][] image = imageFromTiles(tiles);

        int roughIndex = countRoughIndex(image, monster);

        return String.valueOf(roughIndex);
    }

    private static void parseProblemInput(List<String> l, Map<Integer, Tile> tiles) {
        int iTile = 0;
        do {
            String idLine = l.get(iTile);
            String[] parts = idLine.split(" ");
            String idText = parts[1].substring(0, parts[1].length() - 1);
            Tile t = new Tile(Integer.parseInt(idText));
            t.init(l.subList(iTile + 1, iTile + 11));
            tiles.put(t.id, t);
            iTile += 12;
        } while (iTile < l.size());
    }

    private static void composeTileMap2(Map<Integer, Tile> tiles) {
        Tile t = tiles.get(tiles.keySet().iterator().next());
        composeTile(t, tiles);
    }

    private static void composeTile(Tile t, Map<Integer, Tile> tiles) {
        t.composed = true;
        Tile t_right = findRightNeighbour(t, tiles);
        if (t_right != null && !t_right.composed) composeTile(t_right, tiles);
        Tile t_left = findLeftNeighbour(t, tiles);
        if (t_left != null && !t_left.composed) composeTile(t_left, tiles);
        Tile t_Top = findUpperNeighbour(t, tiles);
        if (t_Top != null && !t_Top.composed) composeTile(t_Top, tiles);
        Tile t_Bottom = findLowerNeighbour(t, tiles);
        if (t_Bottom != null && !t_Bottom.composed) composeTile(t_Bottom, tiles);
    }

    private static Tile findRightNeighbour(Tile t, Map<Integer, Tile> tiles) {
        int nNeighbours = 0;
        Tile firstNeighbour = null;
        for (Integer i : tiles.keySet()) {
            Tile t2 = tiles.get(i);
            if (t.id.equals(t2.id)) continue;

            if (isNeighbour_Right(t, t2)) {
                if (!t2.composed) {
                    arrange_T2_toFit_T(t, t2, tiles);
                }
                t.tileId_Right = t2.id;
                t2.tileId_Left = t.id;
                nNeighbours++;
                firstNeighbour = t2;
                // return t2;
            }
        }
        assert (nNeighbours == 0 || nNeighbours == 1);
        return firstNeighbour;
    }

    private static Tile findLeftNeighbour(Tile t, Map<Integer, Tile> tiles) {
        int nNeighbours = 0;
        Tile firstNeighbour = null;
        for (Integer i : tiles.keySet()) {
            Tile t2 = tiles.get(i);
            if (t.id.equals(t2.id)) continue;

            if (isNeighbour_Left(t, t2)) {
                if (!t2.composed) {
                    arrange_T2_toFit_T(t, t2, tiles);
                }
                t.tileId_Left = t2.id;
                t2.tileId_Right = t.id;
                nNeighbours++;
                firstNeighbour = t2;
                // return t2;
            }
        }
        assert (nNeighbours == 0 || nNeighbours == 1);
        return firstNeighbour;
    }

    private static Tile findUpperNeighbour(Tile t, Map<Integer, Tile> tiles) {
        int nNeighbours = 0;
        Tile firstNeighbour = null;
        for (Integer i : tiles.keySet()) {
            Tile t2 = tiles.get(i);
            if (t.id.equals(t2.id)) continue;

            if (isNeighbour_Top(t, t2)) {
                if (!t2.composed) {
                    arrange_T2_toFit_T(t, t2, tiles);
                }
                t.tileId_Up = t2.id;
                t2.tileId_Down = t.id;
                nNeighbours++;
                firstNeighbour = t2;
                // return t2;
            }
        }
        assert (nNeighbours == 0 || nNeighbours == 1);
        return firstNeighbour;
    }

    private static Tile findLowerNeighbour(Tile t, Map<Integer, Tile> tiles) {
        int nNeighbours = 0;
        Tile firstNeighbour = null;
        for (Integer i : tiles.keySet()) {
            Tile t2 = tiles.get(i);
            if (t.id.equals(t2.id)) continue;

            if (isNeighbour_Bottom(t, t2)) {
                if (!t2.composed) {
                    arrange_T2_toFit_T(t, t2, tiles);
                }
                t.tileId_Down = t2.id;
                t2.tileId_Up = t.id;
                nNeighbours++;
                firstNeighbour = t2;
                // return t2;
            }
        }
        assert (nNeighbours == 0 || nNeighbours == 1);
        return firstNeighbour;
    }

    private static boolean isNeighbour_Left(Tile t, Tile t2) {
        return t.border_left.equals(t2.border_top)
                || t.border_left.equals(t2.border_bottom)
                || t.border_left.equals(t2.border_left)
                || t.border_left.equals(t2.border_right)
                || t.border_left.equals(reverse(t2.border_top))
                || t.border_left.equals(reverse(t2.border_bottom))
                || t.border_left.equals(reverse(t2.border_left))
                || t.border_left.equals(reverse(t2.border_right));
    }

    private static boolean isNeighbour_Right(Tile t, Tile t2) {
        return t.border_right.equals(t2.border_top)
                || t.border_right.equals(t2.border_bottom)
                || t.border_right.equals(t2.border_left)
                || t.border_right.equals(t2.border_right)
                || t.border_right.equals(reverse(t2.border_top))
                || t.border_right.equals(reverse(t2.border_bottom))
                || t.border_right.equals(reverse(t2.border_left))
                || t.border_right.equals(reverse(t2.border_right));
    }

    private static boolean isNeighbour_Top(Tile t, Tile t2) {
        return t.border_top.equals(t2.border_top)
                || t.border_top.equals(t2.border_bottom)
                || t.border_top.equals(t2.border_left)
                || t.border_top.equals(t2.border_right)
                || t.border_top.equals(reverse(t2.border_top))
                || t.border_top.equals(reverse(t2.border_bottom))
                || t.border_top.equals(reverse(t2.border_left))
                || t.border_top.equals(reverse(t2.border_right));
    }

    private static boolean isNeighbour_Bottom(Tile t, Tile t2) {
        return t.border_bottom.equals(t2.border_top)
                || t.border_bottom.equals(t2.border_bottom)
                || t.border_bottom.equals(t2.border_left)
                || t.border_bottom.equals(t2.border_right)
                || t.border_bottom.equals(reverse(t2.border_top))
                || t.border_bottom.equals(reverse(t2.border_bottom))
                || t.border_bottom.equals(reverse(t2.border_left))
                || t.border_bottom.equals(reverse(t2.border_right));
    }

    private static void arrange_T2_toFit_T(Tile t, Tile t2, Map<Integer, Tile> tiles) {
        if (t.border_left.equals(t2.border_top)) {
            t2.rotate90(tiles);
        } else if (t.border_left.equals(t2.border_bottom)) {
            t2.flipH(tiles);
            t2.rotate270(tiles);
        } else if (t.border_left.equals(t2.border_left)) {
            t2.flipH(tiles);
        } else if (t.border_left.equals(reverse(t2.border_top))) {
            t2.flipH(tiles);
            t2.rotate90(tiles);
        } else if (t.border_left.equals(reverse(t2.border_bottom))) {
            t2.rotate270(tiles);
        } else if (t.border_left.equals(reverse(t2.border_left))) {
            t2.rotate180(tiles);
        } else if (t.border_left.equals(reverse(t2.border_right))) {
            t2.flipV(tiles);
        }

        if (t.border_right.equals(t2.border_top)) {
            t2.flipH(tiles);
            t2.rotate270(tiles);
        } else if (t.border_right.equals(t2.border_bottom)) {
            t2.rotate90(tiles);
        } else if (t.border_right.equals(t2.border_right)) {
            t2.flipH(tiles);
        } else if (t.border_right.equals(reverse(t2.border_top))) {
            t2.rotate270(tiles);
        } else if (t.border_right.equals(reverse(t2.border_bottom))) {
            t2.flipH(tiles);
            t2.rotate90(tiles);
        } else if (t.border_right.equals(reverse(t2.border_left))) {
            t2.flipV(tiles);
        } else if (t.border_right.equals(reverse(t2.border_right))) {
            t2.rotate180(tiles);
        }

        if (t.border_top.equals(t2.border_top)) {
            t2.flipV(tiles);
        } else if (t.border_top.equals(t2.border_left)) {
            t2.rotate270(tiles);
        } else if (t.border_top.equals(t2.border_right)) {
            t2.flipV(tiles);
            t2.rotate90(tiles);
        } else if (t.border_top.equals(reverse(t2.border_top))) {
            t2.rotate180(tiles);
        } else if (t.border_top.equals(reverse(t2.border_bottom))) {
            t2.flipH(tiles);
        } else if (t.border_top.equals(reverse(t2.border_left))) {
            t2.flipV(tiles);
            t2.rotate270(tiles);
        } else if (t.border_top.equals(reverse(t2.border_right))) {
            t2.rotate90(tiles);
        }

        if (t.border_bottom.equals(t2.border_bottom)) {
            t2.flipV(tiles);
        } else if (t.border_bottom.equals(t2.border_left)) {
            t2.flipV(tiles);
            t2.rotate90(tiles);
        } else if (t.border_bottom.equals(t2.border_right)) {
            t2.rotate270(tiles);
        } else if (t.border_bottom.equals(reverse(t2.border_top))) {
            t2.flipH(tiles);
        } else if (t.border_bottom.equals(reverse(t2.border_bottom))) {
            t2.rotate180(tiles);
        } else if (t.border_bottom.equals(reverse(t2.border_left))) {
            t2.rotate90(tiles);
        } else if (t.border_bottom.equals(reverse(t2.border_right))) {
            t2.flipV(tiles);
            t2.rotate270(tiles);
        }
    }

    private static char[][] imageFromTiles(Map<Integer, Tile> tiles) {
        // Primero averiguamos las dimensiones de la imagen resultante.
        // Para ello, localizamos la esquina superior izquierda
        // y contamos cuántas "tiles" tenemos en horizontal, y cuántas
        // en vertical.
        Tile topLeftCornerTile = getTopLeftCornerTile(tiles);

        int nHorizontalTiles = 1;
        Tile t = topLeftCornerTile;
        assert t != null;
        while (t.tileId_Right != null) {
            nHorizontalTiles++;
            t = tiles.get(t.tileId_Right);
        }
        int nVerticalTiles = 1;
        t = topLeftCornerTile;
        while (t.tileId_Down != null) {
            nVerticalTiles++;
            t = tiles.get(t.tileId_Down);
        }

        char[][] image = new char[nVerticalTiles * 8][nHorizontalTiles * 8];

        for (int h = 0; h < nHorizontalTiles; h++) {
            for (int v = 0; v < nVerticalTiles; v++) {
                t = getTile(tiles, topLeftCornerTile, h, v);
                copyTile(t, image, h * 8, v * 8);
            }
        }
        return image;
    }

    private static int countRoughIndex(char[][] image, char[][] monster) {
        char[][][] monsters = new char[8][][];
        monsters[0] = monster;
        monsters[1] = rotateImage(monster, 90);
        monsters[2] = rotateImage(monster, 180);
        monsters[3] = rotateImage(monster, 270);
        monsters[4] = flipHImage(monsters[0]);
        monsters[5] = flipHImage(monsters[1]);
        monsters[6] = flipHImage(monsters[2]);
        monsters[7] = flipHImage(monsters[3]);

        image = rotateImage(image, 90);
        for (int idx = 0; idx < monsters.length; idx++) {
            char[][] m = monsters[idx];

            int nMonsters = 0;
            for (int i = 0; i < image.length - m.length; i++) {
                for (int j = 0; j < image[0].length - m[0].length; j++) {
                    if (outlineTheMonster(image, i, j, m)) {
                        nMonsters++;
                    }
                }
            }
        }

        int roughness = 0;
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                if (image[i][j] == '#') roughness++;
            }
        }
        return roughness;
    }

    private static boolean outlineTheMonster(char[][] image, int v, int h, char[][] monster) {
        int nHashes = 0;
        // Sabemos que el monster tiene exactamente 15 caracteres '#'
        for (int i = 0; i < monster.length; i++) {
            for (int j = 0; j < monster[0].length; j++) {
                if (monster[i][j] == '#' && image[v + i][h + j] == '#') {
                    nHashes++;
                }
            }
        }

        if (nHashes != 15) return false;

        for (int i = 0; i < monster.length; i++) {
            for (int j = 0; j < monster[0].length; j++) {
                if (monster[i][j] == '#' && image[v + i][h + j] == '#') {
                    image[v + i][h + j] = 'O';
                }
            }
        }
        return true;
    }

    private static Tile getTile(Map<Integer, Tile> tiles, Tile topLeftConterTile, int nHorizontal, int nVertical) {
        Tile t = topLeftConterTile;
        int h = 0;
        while (h < nHorizontal) {
            t = tiles.get(t.tileId_Right);
            h++;
        }
        int v = 0;
        while (v < nVertical) {
            t = tiles.get(t.tileId_Down);
            v++;
        }
        return t;
    }

    private static void copyTile(Tile t, char[][] image, int fromH, int fromV) {
        for (int v = 0; v < 8; v++) {
            for (int h = 0; h < 8; h++) {
                image[fromV + v][fromH + h] = t.tileImage[v][h];
            }
        }
    }

    private static String reverse(String s) {
        return new StringBuilder().append(s.toCharArray()).reverse().toString();
    }

    private static char[][] rotateImage(char[][] matrix, int degrees) {
        if (degrees == 90) {
            char[][] newMatrix = new char[matrix[0].length][matrix.length];
            for (int i = 0; i < newMatrix.length; i++) {
                for (int j = 0; j < newMatrix[0].length; j++) {
                    newMatrix[i][j] = matrix[matrix.length - 1 - j][i];
                }
            }
            return newMatrix;
        } else if (degrees == 180) {
            char[][] newMatrix = new char[matrix.length][matrix[0].length];
            for (int i = 0; i < newMatrix.length; i++) {
                for (int j = 0; j < newMatrix[0].length; j++) {
                    newMatrix[i][j] = matrix[matrix.length - 1 - i][matrix[0].length - 1 - j];
                }
            }
            return newMatrix;
        } else { // (degrees == 270)
            char[][] newMatrix = new char[matrix[0].length][matrix.length];
            for (int i = 0; i < newMatrix.length; i++) {
                for (int j = 0; j < newMatrix[0].length; j++) {
                    newMatrix[i][j] = matrix[j][matrix[0].length - 1 - i];
                }
            }
            return newMatrix;
        }
    }

    private static char[][] flipHImage(char[][] matrix) {
        char[][] newMatrix = new char[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                newMatrix[i][j] = matrix[i][matrix[0].length - 1 - j];
            }
        }
        return newMatrix;
    }

    private static char[][] flipVImage(char[][] matrix) {
        char[][] newMatrix = new char[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                newMatrix[i][j] = matrix[matrix.length - 1 - i][j];
            }
        }
        return newMatrix;
    }

    private static void printImage(char[][] image) {
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                System.out.print(image[i][j]);
            }
            System.out.println();
        }
    }

    private static Tile getTopLeftCornerTile(Map<Integer, Tile> tiles) {
        Tile topLeftCornerTile = null;
        for (Integer i : tiles.keySet()) {
            Tile t = tiles.get(i);
            Integer idTopLeftCorner = t.getTopLeftCorner();
            if (idTopLeftCorner != null) {
                topLeftCornerTile = t;
                break;
            }
        }
        return topLeftCornerTile;
    }

    //////////////////////

    private static class Tile {
        Integer id;
        String border_top = null;
        String border_bottom = null;
        String border_left = null;
        String border_right = null;
        Integer tileId_Up = null;
        Integer tileId_Right = null;
        Integer tileId_Down = null;
        Integer tileId_Left = null;
        char[][] tileImage = null;
        boolean composed;

        public Tile(Integer id) {
            this.id = id;
        }

        public void init(List<String> tileLines) {
            border_top = tileLines.get(0);
            border_bottom = tileLines.get(9);
            char[] tmpLeft = new char[10];
            for (int i = 0; i < 10; i++) tmpLeft[i] = tileLines.get(i).charAt(0);
            border_left = new String(tmpLeft);
            char[] tmpRight = new char[10];
            for (int i = 0; i < 10; i++) tmpRight[i] = tileLines.get(i).charAt(9);
            border_right = new String(tmpRight);

            tileImage = new char[8][8];
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    tileImage[i][j] = tileLines.get(i + 1).charAt(j + 1);
                }
            }
            composed = false;
        }

        public void rotate90(Map<Integer, Tile> tiles) {
            String tmp = border_top;
            border_top = reverse(border_left);
            border_left = border_bottom;
            border_bottom = reverse(border_right);
            border_right = tmp;
            tileImage = rotateImage(tileImage, 90);
        }

        public void rotate180(Map<Integer, Tile> tiles) {
            String tmp = border_top;
            border_top = reverse(border_bottom);
            border_bottom = reverse(tmp);
            tmp = border_left;
            border_left = reverse(border_right);
            border_right = reverse(tmp);
            tileImage = rotateImage(tileImage, 180);
        }

        public void rotate270(Map<Integer, Tile> tiles) {
            String tmp = border_top;
            border_top = border_right;
            border_right = reverse(border_bottom);
            border_bottom = border_left;
            border_left = reverse(tmp);
            tileImage = rotateImage(tileImage, 270);
        }

        public void flipH(Map<Integer, Tile> tiles) {
            String tmp = border_left;
            border_left = border_right;
            border_right = tmp;
            border_top = reverse(border_top);
            border_bottom = reverse(border_bottom);
            tileImage = flipHImage(tileImage);
        }

        public void flipV(Map<Integer, Tile> tiles) {
            String tmp = border_top;
            border_top = border_bottom;
            border_bottom = tmp;
            border_left = reverse(border_left);
            border_right = reverse(border_right);
            tileImage = flipVImage(tileImage);
        }

        // Devuelve el tileId si esta tile es una esquina, null en caso contrario
        public Integer getCorner() {
            if (tileId_Down == null && tileId_Right == null) return id;
            if (tileId_Down == null && tileId_Left == null) return id;
            if (tileId_Up == null && tileId_Right == null) return id;
            if (tileId_Up == null && tileId_Left == null) return id;
            return null;
        }

        // Devuelve el tileId si esta tile es la esquina superior izquierda, null en caso contrario
        public Integer getTopLeftCorner() {
            if (tileId_Up == null && tileId_Left == null) return id;
            return null;
        }

        @Override
        public String toString() {
            return (composed ? "COMPOSED" : "") + "[" + id + "]   Top: " + tileId_Up + "   Right: " + tileId_Right +
                    "   Bottom: " + tileId_Down + "   Left: " + tileId_Left;
        }
    }

}
