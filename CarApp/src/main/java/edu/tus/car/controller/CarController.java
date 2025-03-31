package edu.tus.car.controller;

// Removed unused import: import java.util.Collections;

import edu.tus.car.exception.CarNotFoundException; // Make sure this is imported
import edu.tus.car.exception.CarValidationException; // Make sure this is imported
import edu.tus.car.model.Car;
import edu.tus.car.service.CarService;
import edu.tus.car.errors.ErrorMessage; // Assuming this class exists for error responses

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Use wildcard for brevity or list individually

import java.util.List;
// Removed unused Optional import if not needed elsewhere
// Removed unused ArrayList import


@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    // Constructor Injection - Preferred over field injection
    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    // L29 Fix: Provide parametrized type ResponseEntity<List<Car>>
    public ResponseEntity<List<Car>> getAllCars() {
        // L30 Fix: Use interface List<Car>, remove cast (assuming service returns List<Car>)
        List<Car> cars = carService.getAllCars();
        // L31 Fix: Use isEmpty() - Although not strictly needed now, returning OK with empty list is better than NO_CONTENT
        // Return OK (200) with the list (even if empty, returns [])
        // L35 Fix: Remove unnecessary cast
        return ResponseEntity.ok(cars);
        // L42 Fix: Removed block of commented-out code
    }

    @GetMapping("/{id}")
    // L40 Fix: Provide parametrized type ResponseEntity<Car>
    public ResponseEntity<Car> getCarById(@PathVariable("id") Long id) {
        // Retrieve Optional<Car>
        // L41 Fix (Implicit): Handle Optional correctly
        return carService.getCarById(id)
                .map(ResponseEntity::ok) // If car is present, wrap it in ResponseEntity.ok()
                .orElseGet(() -> ResponseEntity.notFound().build()); // If empty, return 404 Not Found
    }

    @PostMapping
    // L51/L65 Fix: Use specific common type Object instead of wildcard '?'
    public ResponseEntity<Object> addCar(@RequestBody Car car) {
        try {
            Car savedCar = carService.createCar(car);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCar);
            // L55 Fix: Catch the more specific exception thrown by the service if possible
        } catch (CarValidationException e) { // Catch specific validation exception
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorMessage);
        }
        // Consider adding a catch block for other potential exceptions if necessary
    }

    @DeleteMapping("/{id}")
    // L62 Fix: Parametrized type ResponseEntity<Void> (common for successful delete with no body)
    public ResponseEntity<Void> deleteCar(@PathVariable("id") Long id) {
        try {
            carService.deleteCar(id);
            // L65 Fix: Return NO_CONTENT (204) for successful DELETE with no response body
            return ResponseEntity.noContent().build();
            // L66 Fix: Catch the specific exception, not generic Exception
        } catch (CarNotFoundException e) { // Catch specific exception from service
            // L67 Fix: Return NOT_FOUND (404)
            return ResponseEntity.notFound().build();
        }
        // Consider adding a catch block for other potential exceptions if necessary
    }

    @GetMapping("/year/{year}")
    // L72 Fix: Provide parametrized type ResponseEntity<List<Car>>
    public ResponseEntity<List<Car>> getCarsByYear(@PathVariable("year") int year) {
        // L73 Fix: Use interface List<Car>, remove cast
        List<Car> cars = carService.getCarsByYear(year);
        // L74/L75 Fix: Return OK (200) with the list (which might be empty [])
        return ResponseEntity.ok(cars);
        // L101 Fix: Removed block of commented-out code
    }
}
