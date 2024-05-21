// licketyBillSplit
// A tool for splitting the bill

import java.util.*;
import java.text.DecimalFormat;
public class Main {

    // Step-by-step calculator for bill splitting
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        // Step 1: Asks for the number of people dining and their names
        System.out.print("How many people are dining?: ");
        int personCount = scanner.nextInt();
        scanner.nextLine();

        Map<String, Double> subTotalCostPerPerson = new HashMap<>();

        String[] person = new String[personCount];
        for (int i = 0; i < personCount; i++) {
            System.out.print("Person " + (i + 1) + ": ");
            person[i] = scanner.nextLine();
            subTotalCostPerPerson.put(person[i], null);
        }


        // Step 2: Asks for the itemized bill and costs
        System.out.print("\nHow many items are on the bill?: ");
        int numItems = scanner.nextInt();
        scanner.nextLine();

        Map<String, Double> foodCost = new HashMap<>();
        Map<String, Integer> foodCount = new HashMap<>();

        double subTotal = 0;
        for (int i = 0; i < numItems; i++) {
            System.out.print("Name of item " + (i + 1) + ": ");
            String itemName = scanner.nextLine();

            System.out.print("Cost of " + itemName + ": $");
            double itemCost = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("\n");

            foodCost.put(itemName, itemCost);
            foodCount.put(itemName, 0);

            subTotal += itemCost;
        }


        // Step 3: Shows the itemized list
        System.out.print("List of items:" + "\n");
        int numFoodList = 1;
        for (Map.Entry<String, Double> entry : foodCost.entrySet()) {
            System.out.println(numFoodList + ". " + entry.getKey() + " is $" + round.format(entry.getValue()));
            numFoodList++;
        }


        // Step 4: Asks for what each person ate
        Map<String, List<String>> itemsByPerson = new HashMap<>();
        System.out.println("\nWhat did each person eat? (enter the numbers separated by a space)");
        for (int i = 0; i < personCount; i++) {
            System.out.print(person[i] + ": ");
            String foodSelected = scanner.nextLine();
            String[] foodSelectedArray = foodSelected.split(" ");

            for (String itemNumberString : foodSelectedArray) {
                int itemNumberSelected = Integer.parseInt(itemNumberString);
                String itemNameSelected = getItemNameByNumber(itemNumberSelected, foodCost);

                if (!itemsByPerson.containsKey(person[i])) {
                    itemsByPerson.put(person[i], new ArrayList<>());
                }
                itemsByPerson.get(person[i]).add(itemNameSelected);
                foodCount.merge(itemNameSelected, 1, Integer::sum);
            }
        }
        splitItems(foodCost, foodCount);


        // Step 5: Calculates the subtotal cost per person
        for (String name : itemsByPerson.keySet()) {
            List<String> foods = itemsByPerson.get(name);
            double personSubTotal = 0.0;
            for (String food : foods) {
                personSubTotal += foodCost.getOrDefault(food, 0.0);
            }
            subTotalCostPerPerson.put(name, personSubTotal);
        }


        // Step 6: Calculates the tip per person
        HashMap<String, Double> finalCostPerPerson = new HashMap<>();
        System.out.println("\nHow much does each person want to tip?");
        for (String name : subTotalCostPerPerson.keySet()) {
            System.out.print(name + ": %");
            double tipPercentages = scanner.nextDouble();
            double personTipTotal = subTotalCostPerPerson.get(name) * (1 + (tipPercentages / 100));
            finalCostPerPerson.put(name, personTipTotal);
        }


        // Step 7: Calculates the tax per person
        System.out.print("\nHow much is the total tax?: $");
        double totalTax = scanner.nextDouble();
        scanner.nextLine();

        for (String name : itemsByPerson.keySet()) {
            double personTaxTotal = (subTotalCostPerPerson.get(name) / subTotal) * totalTax;
            finalCostPerPerson.merge(name, personTaxTotal, Double::sum);
        }


        // Step 8: Displays the final amount owed by each person
        double grandTotal = 0.0;
        for (String name : finalCostPerPerson.keySet()) {
            System.out.println(name + " owes $" + round.format(finalCostPerPerson.get(name)) + ". ");
            double finalValue = finalCostPerPerson.get(name);
            grandTotal += finalValue;
        }
        double totalTip = grandTotal - (totalTax + subTotal);
    
        System.out.print("\n");
        String format = "%-25s%s%n";
        System.out.printf(format, "Subtotal:", "$" + round.format(subTotal));
        System.out.printf(format, "Total Tax:", "$" + round.format(totalTax));
        System.out.printf(format, "Total pre-Tip:", "$" + round.format(totalTax + subTotal));
        System.out.printf(format, "Tip:", "$" + round.format(totalTip) + " (" + String.valueOf(round.format(100 * (totalTip/subTotal) )) + "%)");
        System.out.printf(format, "Grand Total:", "$" + round.format(grandTotal));
        }

    // Splits the costs of shared items among those who shared
    public static void splitItems(Map<String, Double> foodCount, Map<String, Integer> foodCost) {
        for (Map.Entry<String, Integer> entry : foodCost.entrySet()) {
            String key = entry.getKey();
            Integer valueInt = entry.getValue();

            Double valueDouble = valueInt.doubleValue();

            foodCount.put(key, foodCount.get(key) / valueDouble);
        }
    }

    // Finds the items according to their number rather than their name
    private static String getItemNameByNumber(int number, Map<String, Double> foodCost) {
        int i = 1;
        for (Map.Entry<String, Double> entry : foodCost.entrySet()) {
            if (i == number) {
                return entry.getKey();
            }
            i++;
        }
        return null;
    }

    // Rounds values to two decimal places
    private static final DecimalFormat round = new DecimalFormat("0.00");
}