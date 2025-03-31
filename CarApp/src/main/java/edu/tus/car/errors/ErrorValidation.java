

package edu.tus.car.errors;

import edu.tus.car.model.Car;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet; // Import HashSet
import java.util.Set;     // Import Set

@Component
public class ErrorValidation {

    // Use final for fields initialized in constructor and not reassigned
    private final HashMap<String, String> makeModels;
    // L15 Fix: Use Set<String> for parametrized type and better 'contains' performance/semantics
    private final Set<String> colorsList;

    public ErrorValidation() {
        // L18 Fix: Use diamond operator <>
        makeModels = new HashMap<>();
        makeModels.put("MERCEDES", "E220");
        makeModels.put("AUDI", "A6");
        makeModels.put("VOLKSVAGEN", "ARTEON"); // Note: Potential typo "VOLKSWAGEN"?
        makeModels.put("BMW", "320");
        makeModels.put("FERRARI", "F40");
        makeModels.put("PROSCHE", "GT4");     // Note: Potential typo "PORSCHE"?

        // L15 Fix: Use diamond operator <> for HashSet instantiation
        colorsList = new HashSet<>();
        // Add colors (stored uppercase for case-insensitive comparison later)
        colorsList.add("RED");
        colorsList.add("GREEN");
        colorsList.add("BLACK");
        colorsList.add("SILVER");
    }

    /**
     * Checks if the car's make and model combination is disallowed based on a predefined map.
     * Comparison is case-insensitive.
     * @param car The car to check.
     * @return true if the make/model is not allowed or the make is not found, false otherwise.
     */
    public boolean checkMakeAndModelNotAllowed(Car car) {
        // Ensure car and its properties are not null before calling methods
        if (car == null || car.getMake() == null || car.getModel() == null) {
            return true; // Consider null input as not allowed
        }
        String model = makeModels.get(car.getMake().toUpperCase());
        // If make not found (model is null) OR model doesn't match (case-insensitive)
        return (model == null || (!model.equals(car.getModel().toUpperCase())));
    }

    /**
     * Checks if the car's year is older than the allowed threshold (2020).
     * @param car The car to check.
     * @return true if the year is less than 2020, false otherwise.
     */
    public boolean yearNotOK(Car car) {
        // Ensure car is not null
        if (car == null) {
            return true; // Consider null input as not OK
        }
        // L38 Fix: Remove useless parentheses
        return car.getYear() < 2020;
    }

    /**
     * Checks if the car's color is not in the allowed list (case-insensitive).
     * @param car The car to check.
     * @return true if the color is not allowed, false otherwise.
     */
    public boolean colorNotOK(Car car) {
        // Ensure car and color are not null
        if (car == null || car.getColor() == null) {
            return true; // Consider null input as not OK
        }
        // L42 Fix: Remove useless parentheses
        // Check against the Set (case-insensitive as input color is converted to uppercase)
        return !colorsList.contains(car.getColor().toUpperCase());
    }
}
