package com.search.vehicle.util;

import com.search.vehicle.dto.ResponseDtoV1;
import org.springframework.http.HttpStatus;

/**
 * Contains utility methods such as mapping methods, conversion methods, etc.
 * @author Vansh Pratap Singh
 */
public class MapperUtil {

    /**
     * Fill the provided response object with the given values.
     *
     * @param responseDto               Response dto.
     * @param success                   Success value.
     * @param message                   Message value.
     * @param httpStatus                Http status value.
     * @return                          Response object.
     */
    public static <T> ResponseDtoV1<T> fillResponseDtoV1(
            ResponseDtoV1<T> responseDto, boolean success, String message, HttpStatus httpStatus
    ) {

        if (responseDto == null) {
            responseDto = new ResponseDtoV1<>();
        }

        responseDto.setSuccess(success);
        responseDto.setMessage(message);
        responseDto.setHttpStatus(httpStatus);

        return responseDto;

    }

}
