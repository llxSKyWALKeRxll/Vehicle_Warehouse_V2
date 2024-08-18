package com.search.vehicle.service;

import com.search.vehicle.dto.CarDtoV1;
import com.search.vehicle.dto.ResponseDtoV1;

import java.util.List;

/**
 * Provides the methods/functionalities related to the Cars Entity, such as
 * add car(s), search car(s), etc.
 *
 * @author Vansh Pratap Singh
 */
public interface CarService {

    /**
     * Save the provided car to the main source (includes validations as well).
     *
     * @param carToBeSaved              Car dto.
     * @return                          Response object.
     */
    public ResponseDtoV1<?> saveCar(
            CarDtoV1 carToBeSaved
    );

    /**
     * Save the provided cars to the main source (includes validations as well) in a bulk operation.
     *
     * @param carDtoList Car dto.
     * @return Response object.
     */
    public ResponseDtoV1<?> saveCars(
            List<CarDtoV1> carDtoList
    );

    /**
     * Remove a car from the source on the basis of the provided id.
     *
     * @param carId             Car id.
     * @return                  Response object.
     */
    public ResponseDtoV1<?> removeCar(
            String carId
    );

    /**
     * Method to search for cars on the basis of the provided search term and filters.
     *
     * @param searchTerm                        Search term.
     * @param brandFilters                      Brand filters.
     * @param kmsDrivenMin                      Minimum kilometres driven.
     * @param kmsDrivenMax                      Maximum kilometres driven.
     * @param transmissionFilters               Transmission filters.
     * @param featureFilters                    Feature filters.
     * @param pageNo                            Page number.
     * @param pageSize                          Page size.
     * @return                                  Response object.
     */
    public ResponseDtoV1<?> searchCars(
            String searchTerm,
            List<String> brandFilters,
            Double kmsDrivenMin,
            Double kmsDrivenMax,
            List<String> transmissionFilters,
            List<String> featureFilters,
            int pageNo,
            int pageSize
    );

}
