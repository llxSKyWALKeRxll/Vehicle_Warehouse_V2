package com.search.vehicle.controller;

import com.search.vehicle.dto.CarDtoV1;
import com.search.vehicle.dto.ResponseDtoV1;
import com.search.vehicle.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Exposes APIs related to cars (add car, search car, etc.).
 * @author Vansh Pratap Singh
 */
@RestController
@RequestMapping(value = "/v1/car")
@Validated
public class CarController {

    @Autowired
    private CarService carService;

    /**
     * API to add a car to the source.
     *
     * @param carDto            Car to be added.
     * @return                  Response object.
     */
    @PostMapping()
    public ResponseEntity<?> addCar(
            @RequestBody @Valid CarDtoV1 carDto
    ) {

        ResponseDtoV1<?> response = carService.saveCar(carDto);
        return new ResponseEntity<>(response, response.getHttpStatus());

    }

    /**
     * API to add cars in bulk to the source.
     *
     * @param carDtoList            List of cars to be added.
     * @return                      Response object.
     */
    @PostMapping(value = "/bulk")
    public ResponseEntity<?> addCars(
            @RequestBody @Valid List<CarDtoV1> carDtoList
    ) {

        ResponseDtoV1<?> response = carService.saveCars(carDtoList);
        return new ResponseEntity<>(response, response.getHttpStatus());

    }

    @DeleteMapping()
    public ResponseEntity<?> removeCar(
            @RequestParam(value = "carId") String carId
    ) {

        ResponseDtoV1<?> response = carService.removeCar(carId);
        return new ResponseEntity<>(response, response.getHttpStatus());

    }

    /**
     * API to search for cars on the basis of the provided search term and filters.
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
    @GetMapping(value = "/search")
    public ResponseEntity<?> t1(
            @RequestParam(name = "searchTerm", required = false) String searchTerm,
            @RequestParam(name = "brandFilters", required = false) List<String> brandFilters,
            @RequestParam(name = "kmsDrivenMin", required = false) Double kmsDrivenMin,
            @RequestParam(name = "kmsDrivenMax", required = false) Double kmsDrivenMax,
            @RequestParam(name = "transmissionFilters", required = false) List<String> transmissionFilters,
            @RequestParam(name = "featureFilters", required = false) List<String> featureFilters,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize
    ) {

        ResponseDtoV1<?> response = carService.searchCars(searchTerm, brandFilters, kmsDrivenMin, kmsDrivenMax, transmissionFilters, featureFilters, pageNo, pageSize);
        return new ResponseEntity<>(response, response.getHttpStatus());

    }

}
