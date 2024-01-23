package com.aiservice.util;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public final class ValidateBeanUtil {

	ValidateBeanUtil(){

	}

	public static Set<ConstraintViolation<Object>> validateBean(Object object) {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.usingContext().getValidator();
		Set<ConstraintViolation<Object>> res =  validator.validate(object);
		validatorFactory.close();
		return res;
	}

}
