package edu.tus.car.service;

import edu.tus.car.dao.CarRepository;
import edu.tus.car.model.Car;
import edu.tus.car.errors.ErrorMessages;
import edu.tus.car.errors.ErrorValidation;
import edu.tus.car.exception.CarNotFoundException;
import edu.tus.car.exception.CarValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    // Dependencies injected via constructor
    private final ErrorValidation errorValidation;
    private final CarRepository carRepo;

    /**
     * Constructor-based dependency injection is preferred over field injection.
     * @param errorValidation Service for performing car validation checks.
     * @param carRepo Repository for accessing car data.
     */
    @Autowired // Optional if only one constructor exists, but good for clarity
    public CarService(ErrorValidation errorValidation, CarRepository carRepo) {
        this.errorValidation = errorValidation;
        this.carRepo = carRepo;
    }

    /**
     * Retrieves all cars from the repository.
     * @return A list of all cars.
     */
    public List<Car> getAllCars() {
        return carRepo.findAll();
    }

    /**
     * Retrieves a specific car by its ID.
     * @param id The ID of the car to retrieve.
     * @return An Optional containing the car if found, or an empty Optional otherwise.
     */
    public Optional<Car> getCarById(Long id) {
        return carRepo.findById(id);
    }

    /**
     * Validates and creates a new car in the repository.
     * @param car The car object to create.
     * @return The saved car object with its assigned ID.
     * @throws CarValidationException if the car details fail validation checks.
     */
    public Car createCar(Car car) throws CarValidationException {
        // Perform validation checks
        if (errorValidation.checkMakeAndModelNotAllowed(car)) {
            throw new CarValidationException(ErrorMessages.INVALID_MAKE_MODEL.getMsg());
        }
        if (errorValidation.yearNotOK(car)) {
            throw new CarValidationException(ErrorMessages.INVALID_YEAR.getMsg());
        }
        if (errorValidation.colorNotOK(car)) {
            throw new CarValidationException(ErrorMessages.INVALID_COLOR.getMsg());
        }
        // Save the car if validation passes
        return carRepo.save(car);
    }

    /**
     * Deletes a car by its ID.
     * Throws CarNotFoundException if no car with the given ID exists.
     * @param id The ID of the car to delete.
     * @throws CarNotFoundException if the car is not found.
     */
    public void deleteCar(Long id) throws CarNotFoundException {
        // Find the car or throw an exception if not found
        Car carToDelete = carRepo.findById(id)
                .orElseThrow(() -> new CarNotFoundException("Car with ID " + id + " not found. Cannot delete."));

        // Delete the car if found
        carRepo.delete(carToDelete);
    }

    /**
     * Retrieves a list of cars filtered by year.
     * @param year The manufacturing year to filter by.
     * @return A list of cars manufactured in the specified year.
     */
    public List<Car> getCarsByYear(int year) {
        // Assuming findByYear exists in CarRepository
        return carRepo.findByYear(year);
    }
}



