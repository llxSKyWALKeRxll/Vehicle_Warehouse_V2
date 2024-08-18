package com.search.vehicle.service;

import com.search.vehicle.dto.CarDtoV1;
import com.search.vehicle.dto.CarDtoV2;
import com.search.vehicle.dto.ResponseDtoV1;
import com.search.vehicle.util.MapperUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.Valid;
import java.util.*;

/**
 * Provides the methods/functionalities related to the Cars Entity, such as
 * add car(s), search car(s), etc.
 *
 * @author Vansh Pratap Singh
 */
@Service
public class CarServiceImpl implements CarService {

    private static final Logger logger = LoggerFactory.getLogger(CarServiceImpl.class);

    @Value("#{${cars.search.field.boost.map}}")
    private Map<String, Float> fieldBoostMap;

    @Value("${elasticsearch.cars.index}")
    private String elasticsearchCarsIndex;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * Save the provided car to the main source (includes validations as well).
     *
     * @param carToBeSaved            Car dto.
     * @return                  Response object.
     */
    @Override
    public ResponseDtoV1<?> saveCar(
            @Valid CarDtoV1 carToBeSaved
    ) {

        ResponseDtoV1<CarDtoV1> responseDto = new ResponseDtoV1<>();

        if (carToBeSaved == null) {
            return MapperUtil.fillResponseDtoV1(responseDto, false,
                    "Request body must not be null.", HttpStatus.BAD_REQUEST);
        }

        if (carToBeSaved.getKmsDriven() < 0) {
            return MapperUtil.fillResponseDtoV1(responseDto, false,
                    "Kms driven cannot have a negative value for any vehicle.", HttpStatus.BAD_REQUEST);
        }

        if (carToBeSaved.getKmsDriven() == null) {
            carToBeSaved.setKmsDriven(0.0);
        }

        if (!carToBeSaved.getTransmission().equalsIgnoreCase("automatic")
        && !carToBeSaved.getTransmission().equalsIgnoreCase("manual")) {
            return MapperUtil.fillResponseDtoV1(responseDto, false,
                    "Invalid transmission value.", HttpStatus.BAD_REQUEST);
        }

        try {

            carToBeSaved = elasticsearchRestTemplate.save(carToBeSaved, IndexCoordinates.of(elasticsearchCarsIndex));

        } catch (Exception ex) {

            logger.error("*** Exception occurred while attempting to save a car to ES *** \n" +
                    "Exception is => {}", ExceptionUtils.getStackTrace(ex));

            return MapperUtil.fillResponseDtoV1(responseDto, false,
                    ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

        responseDto.setData(carToBeSaved);
        responseDto.setSuccess(true);
        responseDto.setMessage("Performed operation successfully.");
        responseDto.setHttpStatus(HttpStatus.CREATED);

        return responseDto;

    }

    /**
     * Save the provided cars to the main source (includes validations as well) in a bulk operation.
     *
     * @param carDtoList Car dto.
     * @return Response object.
     */
    @Override
    public ResponseDtoV1<?> saveCars(List<CarDtoV1> carDtoList) {

        ResponseDtoV1<List<CarDtoV1>> responseDto = new ResponseDtoV1<>();

        if (CollectionUtils.isEmpty(carDtoList)) {
            return MapperUtil.fillResponseDtoV1(responseDto, false,
                    "Request body must not be empty.", HttpStatus.BAD_REQUEST);
        }

        if (carDtoList.size() > 150) {
            return MapperUtil.fillResponseDtoV1(responseDto, false,
                    "List size cannot be greater than 150.", HttpStatus.BAD_REQUEST);
        }

        for (CarDtoV1 carToBeSaved: carDtoList) {

            if (carToBeSaved == null) {
                return MapperUtil.fillResponseDtoV1(responseDto, false,
                        "One or more of the request body(s) is null.", HttpStatus.BAD_REQUEST);
            }

            if (carToBeSaved.getKmsDriven() < 0) {
                return MapperUtil.fillResponseDtoV1(responseDto, false,
                        "Kms driven cannot have a negative value for any vehicle.", HttpStatus.BAD_REQUEST);
            }

            if (carToBeSaved.getKmsDriven() == null) {
                carToBeSaved.setKmsDriven(0.0);
            }

            if (!carToBeSaved.getTransmission().equalsIgnoreCase("automatic")
                    && !carToBeSaved.getTransmission().equalsIgnoreCase("manual")) {
                return MapperUtil.fillResponseDtoV1(responseDto, false,
                        "Invalid transmission value for one or more of the request body(s).", HttpStatus.BAD_REQUEST);
            }

        }

        try {

            elasticsearchRestTemplate.save(carDtoList, IndexCoordinates.of(elasticsearchCarsIndex));

        } catch (Exception ex) {

            logger.error("*** Exception occurred while attempting to save cars to ES in a bulk operation *** \n" +
                    "Exception is => {}", ExceptionUtils.getStackTrace(ex));

            return MapperUtil.fillResponseDtoV1(responseDto, false,
                    ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

        responseDto.setSuccess(true);
        responseDto.setMessage("Performed operation successfully.");
        responseDto.setData(carDtoList);
        responseDto.setHttpStatus(HttpStatus.CREATED);

        return responseDto;

    }

    /**
     * Remove a car from the source on the basis of the provided id.
     *
     * @param carId Car id.
     * @return Response object.
     */
    @Override
    public ResponseDtoV1<?> removeCar(String carId) {

        ResponseDtoV1<String> responseDto = new ResponseDtoV1<>();

        if (StringUtils.isEmpty(carId)) {
            return MapperUtil.fillResponseDtoV1(responseDto, false,
                    "Car id must not be empty.", HttpStatus.BAD_REQUEST);
        }

        try {

            String removedCarId = elasticsearchRestTemplate.delete(carId, IndexCoordinates.of(elasticsearchCarsIndex));

            if (StringUtils.isEmpty(removedCarId)) {
                return MapperUtil.fillResponseDtoV1(responseDto, false,
                        "No car exists for the given car id.", HttpStatus.NOT_FOUND);
            }

        } catch (Exception ex) {

            logger.error("*** Exception occurred while attempting to remove a car with id {} from ES ***\n" +
                    "Exception is => {}", carId, ExceptionUtils.getStackTrace(ex));

        }

        responseDto.setSuccess(true);
        responseDto.setMessage("Performed operation successfully.");
        responseDto.setHttpStatus(HttpStatus.OK);

        return responseDto;
    }

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
    @Override
    public ResponseDtoV1<?> searchCars(
            String searchTerm,
            List<String> brandFilters,
            Double kmsDrivenMin,
            Double kmsDrivenMax,
            List<String> transmissionFilters,
            List<String> featureFilters,
            int pageNo,
            int pageSize
    ) {

        ResponseDtoV1<Map<String, Object>> responseDto = new ResponseDtoV1<>();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (!StringUtils.isEmpty(searchTerm)) {

            List<MultiMatchQueryBuilder> multiMatchQueryBuildersList = new ArrayList<>();

            multiMatchQueryBuildersList.add(QueryBuilders.multiMatchQuery(searchTerm).type(MultiMatchQueryBuilder.Type.BEST_FIELDS).operator(Operator.AND).fuzziness("AUTO"));
            multiMatchQueryBuildersList.add(QueryBuilders.multiMatchQuery(searchTerm).type(MultiMatchQueryBuilder.Type.PHRASE));
            multiMatchQueryBuildersList.add(QueryBuilders.multiMatchQuery(searchTerm).type(MultiMatchQueryBuilder.Type.PHRASE_PREFIX));
            multiMatchQueryBuildersList.add(QueryBuilders.multiMatchQuery(searchTerm).type(MultiMatchQueryBuilder.Type.MOST_FIELDS).fuzziness("AUTO"));
            multiMatchQueryBuildersList.add(QueryBuilders.multiMatchQuery(searchTerm).type(MultiMatchQueryBuilder.Type.CROSS_FIELDS));

            BoolQueryBuilder shouldQuery = org.elasticsearch.index.query.QueryBuilders.boolQuery();

            for (MultiMatchQueryBuilder multiMatchQueryBuilder : multiMatchQueryBuildersList) {

                multiMatchQueryBuilder.analyzer("standard");

                fieldBoostMap.forEach(multiMatchQueryBuilder::field);

                shouldQuery.should(multiMatchQueryBuilder);

            }

            boolQueryBuilder.must(shouldQuery);

        }

        if (!CollectionUtils.isEmpty(brandFilters)) {

            boolQueryBuilder.filter(
                    QueryBuilders.termsQuery("brand.keyword", brandFilters)
            );

        }

        if (kmsDrivenMin != null) {

            boolQueryBuilder.filter(
                    QueryBuilders.rangeQuery(
                            "kmsDriven"
                    )
                            .gte(kmsDrivenMin)
            );

        }

        if (kmsDrivenMax != null ) {

            boolQueryBuilder.filter(
                    QueryBuilders.rangeQuery(
                            "kmsDriven"
                    )
                            .lte(kmsDrivenMax)
            );

        }

        if (!CollectionUtils.isEmpty(transmissionFilters)) {

            boolQueryBuilder.filter(
                    QueryBuilders.termsQuery("transmission.keyword", transmissionFilters)
            );

        }

        if (!CollectionUtils.isEmpty(featureFilters)) {

            boolQueryBuilder.filter(
                    QueryBuilders.termsQuery("features.keyword", featureFilters)
            );

        }

        NativeSearchQuery searchQuery = new NativeSearchQuery(boolQueryBuilder).setPageable(Pageable.ofSize(pageSize).withPage(pageNo));

        SearchHits<CarDtoV1> carHitsFromEs = null;

        try {

            carHitsFromEs = elasticsearchRestTemplate.search(searchQuery, CarDtoV1.class, IndexCoordinates.of(elasticsearchCarsIndex));

        } catch (Exception ex) {

            logger.error("*** Exception occurred while fetching data from ES ***\n" +
                    "Exception is => {}", ExceptionUtils.getStackTrace(ex));

            return MapperUtil.fillResponseDtoV1(responseDto, false,
                    ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

        Map<String, Object> dataMap = new LinkedHashMap<>();

        List<CarDtoV2> dataList = new ArrayList<>();

        List<SearchHit<CarDtoV1>> searchHits = carHitsFromEs.getSearchHits();

        dataMap.put("count", carHitsFromEs.getTotalHits());

        for (SearchHit<CarDtoV1> searchHit: searchHits) {

            if (searchHit == null) {
                continue;
            }

            CarDtoV1 content = searchHit.getContent();

            dataList.add(new CarDtoV2(searchHit.getId(), content));

        }

        dataMap.put("content", dataList);

        responseDto.setSuccess(true);
        responseDto.setMessage("Performed operation successfully.");
        responseDto.setData(dataMap);
        responseDto.setHttpStatus(HttpStatus.OK);

        return responseDto;

    }

}
