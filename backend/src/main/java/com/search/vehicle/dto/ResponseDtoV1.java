package com.search.vehicle.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

/**
 * Represents a generic response dto - V1.
 * @author Vansh Pratap Singh
 *
 * @param <T> Data type.
 */
public class ResponseDtoV1<T> {

    private boolean success;

    private String message;

    private T data;

    @JsonIgnore
    private HttpStatus httpStatus;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

}
