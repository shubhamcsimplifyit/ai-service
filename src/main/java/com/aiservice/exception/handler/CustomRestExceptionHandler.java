package com.aiservice.exception.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.aiservice.constant.AiBaseConstant;
import com.aiservice.exception.apierror.ApiError;
import com.aiservice.exception.custom.AiServiceBusinessException;
import com.aiservice.exception.custom.CustomValidationViolationException;
import com.aiservice.exception.custom.ResourceNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Handles ResourceNotFoundException.
	 *
	 * @param ex the ResourceNotFoundException
	 * @return the ApiError object
	 */
	@ExceptionHandler(ResourceNotFoundException.class)
	protected ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
		ApiError apiError = new ApiError(NOT_FOUND);
		apiError.setMessage(ex.getMessage());
		log.error("Error :" + ex);
		return buildResponseEntity(apiError);
	}

	/**
	 * Handles CustomValidationViolationException.
	 *
	 * @param ex the CustomValidationViolationException
	 * @return the ApiError object
	 */
	@ExceptionHandler(CustomValidationViolationException.class)
	protected ResponseEntity<Object> handleCustomValidationViolationException(CustomValidationViolationException ex) {
		ApiError apiError = new ApiError(BAD_REQUEST);
		apiError.setMessage(AiBaseConstant.VALIDATION_ERROR);
		apiError.addValidationErrors(ex.getFieldErrors());
		log.error("Error :" + ex);
		return buildResponseEntity(apiError);
	}

	/**
	 * Handle MissingServletRequestParameterException. Triggered when a 'required'
	 * request parameter is missing.
	 *
	 * @param ex      MissingServletRequestParameterException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = ex.getParameterName() + " parameter is missing";
		log.error("Error :" + ex);
		return buildResponseEntity(new ApiError(BAD_REQUEST, error, ex));
	}

	/**
	 * Handle HttpMediaTypeNotSupportedException. Says that the specified request
	 * media type (Content type) is not supported!. This one triggers when JSON is
	 * invalid as well.
	 *
	 * @param ex      HttpMediaTypeNotSupportedException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append("media type is not supported. Supported media types are ");
		ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
		log.error("Error :" + ex);
		return buildResponseEntity(
				new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex));
	}

	/**
	 * Handle HttpRequestMethodNotSupportedException. Triggered when an unsupported
	 * Request Method in invoked.
	 *
	 * @param ex      HttpRequestMethodNotSupportedException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		StringBuilder builder = new StringBuilder();
		builder.append(ex.getMethod());
		builder.append(" method is not supported for this request.");
		log.error("Error :" + ex);
		return buildResponseEntity(
				new ApiError(HttpStatus.METHOD_NOT_ALLOWED, builder.substring(0, builder.length() - 2), ex));
	}

	/**
	 * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid
	 * validation.
	 *
	 * @param ex      the MethodArgumentNotValidException that is thrown when @Valid
	 *                validation fails
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ApiError apiError = new ApiError(BAD_REQUEST);
		log.error("Error :", ex);
		String errorMsg = setErrorMessage(ex);
		apiError.setMessage(errorMsg);
		apiError.setDebugMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}

	/**
	 * Handles javax.validation.ConstraintViolationException. Thrown when @Validated
	 * fails. Refer: CustomerPostAddAllController.java
	 *
	 * @param ex the ConstraintViolationException
	 * @return the ApiError object
	 */
	@ExceptionHandler(javax.validation.ConstraintViolationException.class)
	protected ResponseEntity<Object> handleConstraintViolation(javax.validation.ConstraintViolationException ex) {
		ApiError apiError = new ApiError(BAD_REQUEST);
		log.error("Error :", ex);

		String errorMsg = setErrorMessage(ex);
		apiError.setMessage(errorMsg);
		apiError.setDebugMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}

	/**
	 * Handle HttpMessageNotReadableException. Happens when request JSON is
	 * malformed.
	 *
	 * @param ex      HttpMessageNotReadableException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ServletWebRequest servletWebRequest = (ServletWebRequest) request;
		log.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
		log.error("Error :" + ex);
		String error = "Malformed JSON request";
		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
	}

	/**
	 * Handle HttpMessageNotWritableException.
	 * 
	 *
	 * @param ex      HttpMessageNotWritableException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("Error :" + ex);
		String error = "Error writing JSON output";
		return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
	}

	/**
	 * Handle NoHandlerFoundException. DispatcherServlet send, by default, a 404
	 * response if there is no handler for a particular request! So, to override the
	 * default behavior of our Servlet and throw NoHandlerFoundException instead, we
	 * need to add the some properties to application.properties file! (No example
	 * of this being thrown in the code)
	 *
	 * @param ex
	 * @param headers
	 * @param status
	 * @param request
	 * @return
	 */
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ApiError apiError = new ApiError(BAD_REQUEST);
		log.error("Error :", ex);
		String errorMsg = setErrorMessage(ex);
		apiError.setMessage(errorMsg);
		apiError.setDebugMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}

	/**
	 * Handle Exception, handle generic Exception.class. Thrown when a method
	 * parameter has the wrong type! Example: GET
	 * http://localhost:8080/customers/adsdada (No example of this being thrown in
	 * the code)
	 *
	 * @param ex the Exception
	 * @return the ApiError object
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest request) {
		Class<?> misMatchClass = ex.getRequiredType();
		ApiError apiError = new ApiError(BAD_REQUEST);

		if (misMatchClass != null) {
			apiError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
					ex.getName(), ex.getValue(), misMatchClass.getSimpleName()));
		} else {
			apiError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
					ex.getName(), ex.getValue(), "Class name is nullable"));
		}

		log.error("Error :", ex);

		String errorMsg = setErrorMessage(ex);
		apiError.setMessage(errorMsg);
		apiError.setDebugMessage(ex.getMessage());
		return buildResponseEntity(apiError);

	}

	@ExceptionHandler(AiServiceBusinessException.class)
	protected ResponseEntity<Object> handleVendorMasterException(AiServiceBusinessException ex,
			HttpServletRequest request) {
		log.error("Error :" + ex);
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR);
		apiError.setMessage(ex.getMessage());

		apiError.setDebugMessage(ex.getCause() != null ? ex.getCause().getMessage() : null);
		apiError.setPath(request.getRequestURI());
		return buildResponseEntity(apiError);
	}

	/**
	 * Handle DataAccessException, inspects the cause for different DB causes. (No
	 * example of this being thrown in the code)
	 *
	 * @param ex the DataAccessException
	 * @return the ApiError object
	 */

	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<Object> handleDatabaseException(DataAccessException ex, WebRequest request) {
		log.error("Error :", ex);
		ApiError apiError = new ApiError(NOT_FOUND);
		String errorMsg = setErrorMessage(ex);
		apiError.setMessage(errorMsg);
		apiError.setDebugMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
		log.error("Error:::",ex );
		ApiError apiError = new ApiError(BAD_REQUEST);
		apiError.setDebugMessage(ex.getMessage());
		String errorMsg=setErrorMessage(ex);
		apiError.setMessage(errorMsg);
		apiError.setDebugMessage(ex.getMessage());	
		return buildResponseEntity(apiError);
	}
	
	/**
	 * Handle any Exception.
	 * 
	 * @param ex      Exception
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@ExceptionHandler({ SQLException.class })
	public ResponseEntity<Object> handleSQLExceptionAll(Exception ex, WebRequest request) {
		log.error("SQL Exception:::",ex );
		ApiError apiError = new ApiError(BAD_REQUEST);
		apiError.setDebugMessage(ex.getMessage());
		String errorMsg=setErrorMessage(ex);
		apiError.setMessage(errorMsg);
		apiError.setDebugMessage(ex.getMessage());	
		return buildResponseEntity(apiError);
	}

	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getHttpStatus());
	}

	String setErrorMessage(Exception ex) {
		String errorMsg = ex.getMessage();
		if (errorMsg.contains("Exception")) {
			errorMsg = "Something went wrong, Kindly contact to support";
		}
		return errorMsg;
	}

}