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
        List<String> l = Files.readAllLines(Paths.get("D:\\src\\practicas\\AOC_2020\\21\\src\\problem.txt"));

        String result = partOne(l);
        System.out.println("Part one - solution: " + result);

        result = partTwo(l);
        System.out.println("Part two - solution: " + result);
    }

    private static String partOne(List<String> l) throws Exception {
        List<Receipt> receipts = parseProblemInput(l);

        Map<String, Set<String>> allergensInIngredients = getAllAllergensWithTheirPossibleIngredients(receipts);
        Map<String, String> ingredientsWithAllergens = new HashMap<>();

        // En 'allergensInIngredients' tenemos un mapa donde, a cada alérgeno le asociamos un
        // conjunto de ingredientes que lo podrían contener.
        // Por último, en 'ingredientsWithAllergens' tenemos un mapa donde guardaremos para cada ingrediente,
        // cuál es su alérgeno conocido, a medida que los vayamos descubriendo.

        discover(ingredientsWithAllergens, allergensInIngredients);

        Set<String> safeIngredients = getAllIngredients(receipts);
        safeIngredients.removeAll(ingredientsWithAllergens.keySet());

        int nOccurrences = 0;
        for (String ingredient : safeIngredients) {
            for (Receipt r : receipts) {
                if (r.ingredients.contains(ingredient)) {
                    nOccurrences++;
                }
            }
        }
        return String.valueOf(nOccurrences);
    }

    private static String partTwo(List<String> l) throws Exception {
        List<Receipt> receipts = parseProblemInput(l);

        Map<String, Set<String>> allergensInIngredients = getAllAllergensWithTheirPossibleIngredients(receipts);
        Map<String, String> ingredientsWithAllergens = new HashMap<>();

        // En 'allergensInIngredients' tenemos un mapa donde, a cada alérgeno le asociamos un
        // conjunto de ingredientes que lo podrían contener.
        // Por último, en 'ingredientsWithAllergens' tenemos un mapa donde guardaremos para cada ingrediente,
        // cuál es su alérgeno conocido, a medida que los vayamos descubriendo.

        discover(ingredientsWithAllergens, allergensInIngredients);

        List<AllergenIngredientPair> sortableIngredientsWithAllergens = new ArrayList<>();
        for (String ingredient : ingredientsWithAllergens.keySet()) {
            String allergen = ingredientsWithAllergens.get(ingredient);
            AllergenIngredientPair touple = new AllergenIngredientPair(allergen, ingredient);
            sortableIngredientsWithAllergens.add(touple);
        }
        sortableIngredientsWithAllergens.sort(new Comparator<AllergenIngredientPair>() {
            @Override
            public int compare(AllergenIngredientPair o1, AllergenIngredientPair o2) {
                return o1.allergen.compareTo(o2.allergen);
            }
        });
        StringBuilder sb = new StringBuilder();
        for (AllergenIngredientPair touple : sortableIngredientsWithAllergens) {
            sb.append(touple.ingredient).append(",");
        }
        String result = sb.toString();
        // Hay que quitarle la coma final.
        result = result.substring(0, result.length()-1);

        return result;
    }

    private static List<Receipt> parseProblemInput(List<String> l) {
        List<Receipt> receipts = new ArrayList<>();
        for (String line : l) {
            String[] parts = line.split("contains ");
            parts[0] = parts[0].substring(0, parts[0].length() - 2);
            String[] ingr = parts[0].split(" ");
            String[] allerg = parts[1].split(", ");
            // Quitamos el paréntesis final tras el último alérgeno
            allerg[allerg.length - 1] = allerg[allerg.length - 1].substring(0, allerg[allerg.length - 1].length() - 1);
            Receipt r = new Receipt(ingr, allerg);
            receipts.add(r);
        }
        return receipts;
    }

    private static Set<String> getAllIngredients(List<Receipt> receipts) {
        Set<String> allIngredients = new HashSet<>();
        for (Receipt r : receipts) {
            Set<String> ingredients = r.ingredients();
            allIngredients.addAll(ingredients);
        }
        return allIngredients;
    }

    private static Map<String, Set<String>> getAllAllergensWithTheirPossibleIngredients(List<Receipt> receipts) {
        Map<String, Set<String>> allAllergensWithPossibleIngredients = new HashMap<>();
        for (Receipt r : receipts) {
            for (String allergen : r.allergens) {
                Set<String> possibleIngredients = allAllergensWithPossibleIngredients.get(allergen);
                if (possibleIngredients == null) {
                    possibleIngredients = new HashSet<>(r.ingredients());
                    allAllergensWithPossibleIngredients.put(allergen, possibleIngredients);
                } else {
                    possibleIngredients.retainAll(r.ingredients());
                }
            }
        }
        return allAllergensWithPossibleIngredients;
    }

    private static void discover(Map<String, String> ingredientsWithAllergens, Map<String, Set<String>> allergensInIngredients) throws Exception {
        int initialSize = allergensInIngredients.size();
        if (initialSize == 0) return;

        List<String> identifiedIngredients = new ArrayList<>();
        // Buscamos en el mapa alérgenos con un sólo ingrediente
        List<String> allergensWithOneIngredient = getAllergensWithOneIngredient(allergensInIngredients);
        for (String allergen : allergensWithOneIngredient) {
            Set<String> setWithOneIngredient = allergensInIngredients.remove(allergen);
            String ingredient = setWithOneIngredient.iterator().next();
            ingredientsWithAllergens.put(ingredient, allergen);
            identifiedIngredients.add(ingredient);
        }
        // Ahora, para cada ingrediente identificado con un alérgeno, lo eliminamos del conjunto de
        // ingredientes posibles de todos los alérgenos que quedan
        for (String ingredient : identifiedIngredients) {
            for (Set<String> ingredientesPosibles : allergensInIngredients.values()) {
                ingredientesPosibles.remove(ingredient);
            }
        }
        discover(ingredientsWithAllergens, allergensInIngredients);
    }

    private static List<String> getAllergensWithOneIngredient(Map<String, Set<String>> allergensInIngredients) throws Exception {
        List<String> allergensWithOneIngredient = new ArrayList<>();
        for (String allergen : allergensInIngredients.keySet()) {
            Set<String> ingredients = allergensInIngredients.get(allergen);
            if (ingredients.size() == 0) {
                throw new Exception("No puede ser: un alérgeno(" + allergen + ") sin ingredientes!!!");
            }
            if (ingredients.size() == 1) allergensWithOneIngredient.add(allergen);
        }
        return allergensWithOneIngredient;
    }

    /////////////////////

    private static class Receipt {
        private Set<String> ingredients;
        private Set<String> allergens;

        public Receipt(String[] ingr, String[] allerg) {
            ingredients = new HashSet<>();
            for (String i : ingr) ingredients.add(i);
            allergens = new HashSet<>();
            for (String a : allerg) allergens.add(a);
        }

        public Set<String> ingredients() {
            return new HashSet<>(ingredients);
        }

        public Set<String> allergens() {
            return new HashSet<>(allergens);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (String i : ingredients) sb.append(i).append(" ");
            sb.append("(contains ");
            for (String a : allergens) sb.append(a).append(" ");
            sb.append(")");
            return sb.toString();
        }
    }

    private static class AllergenIngredientPair {
        String allergen;
        String ingredient;

        public AllergenIngredientPair(String allergen, String ingredient) {
            this.allergen = allergen;
            this.ingredient = ingredient;
        }
    }

}
