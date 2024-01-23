package com.aiservice.constraint;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import com.aiservice.constant.AiBaseConstant;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ConditionalValidator implements ConstraintValidator<Conditional, Object> {


	private String[] conditionalFieldNames;
	private String message;


	@Override
	public void initialize(Conditional requiredIfChecked) {
		conditionalFieldNames = requiredIfChecked.conditionalFieldNames();
		message = requiredIfChecked.message();
	}


	@Override
	public boolean isValid(Object object, ConstraintValidatorContext context) {
		boolean valid = true;
		boolean isAnyFieldNotBlank=false;
		try {
			StringBuilder bld = new StringBuilder();
			bld.append(message);

			List<String> errorDescriptionList = new ArrayList<>();
			for (String propName : conditionalFieldNames) {
				Object fieldValue = BeanUtils.getProperty(object, propName);
				if (fieldValue==null || StringUtils.isBlank(fieldValue.toString())) {
					errorDescriptionList.add(getErrorCode(propName));                    
					valid=false;
				}else {
					isAnyFieldNotBlank=true;
				}   
			}
			
			if(Boolean.FALSE.equals(valid) && Boolean.FALSE.equals(isAnyFieldNotBlank)) {
				valid=true; 
			}else if(Boolean.TRUE.equals(isAnyFieldNotBlank) && Boolean.FALSE.equals(valid) ) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate(String.join(AiBaseConstant.ERROR_DESC_JOIN, errorDescriptionList)).addPropertyNode("Custom Validation").addConstraintViolation();
			}

		} catch (IllegalAccessException e) {
			log.error("Accessor method is not available for class : {}, exception : {}", object.getClass().getName(), e);

			return false;
		} catch (NoSuchMethodException e) {
			log.error("Field or method is not present on class : {}, exception : {}", object.getClass().getName(), e);

			return false;
		} catch (InvocationTargetException e) {
			log.error("An exception occurred while accessing class : {}, exception : {}", object.getClass().getName(), e);

			return false;
		}
		return valid;
	}


	private String getErrorCode(String fieldname) {
		String errorCode=null;
		// switch statement 
		switch(fieldname)
		{
		// case statements
		// values must be of same type of expression
		case "firstName" :
			// Statements
			errorCode="E00372";
			break; // break is optional

		case "lastName" :
			// Statements
			errorCode="E00374";
			break; // break is optional

		case "mobileNo" :
			// Statements
			errorCode="E00376";
			break; // break is optional

		case "contactEmailId" :
			// Statements
			errorCode="E00379";
			break; // break is optional

		case "primaryContact" :
			// Statements
			errorCode="E00382";
			break; // break is optional

		case "addressType" :
			// Statements
			errorCode="E00384";
			break; // break is optional

		case "defaultAddress" :
			// Statements
			errorCode="E00386";
			break; // break is optional

		case "addressLine1" :
			// Statements
			errorCode="E00388";
			break; // break is optional

		case "addressLine2" :
			// Statements
			errorCode="E00390";
			break; // break is optional

		case "place" :
			// Statements
			errorCode="E00392";
			break; // break is optional

		case "pincode" :
			// Statements
			errorCode="E00394";
			break; // break is optional

		case "bankNameVendor" :
			// Statements
			errorCode="E00397";
			break; // break is optional

		case "bankAddressVendor" :
			// Statements
			errorCode="E00399";
			break; // break is optional

		case "bankAccHolderNameVendor" :
			// Statements
			errorCode="E00401";
			break; // break is optional

		case "bankAccNoVendor" :
			// Statements
			errorCode="E00403";
			break; // break is optional

		case "bankIfscNoVendor" :
			// Statements
			errorCode="E00405";
			break; // break is optional

		case "primaryBank" :
			// Statements
			errorCode="E00407";
			break; // break is optional
			// We can have any number of case statements
			// below is default statement, used when none of the cases is true. 
			// No break is needed in the default case.
		default : 
			// Statements
			errorCode="Invalid Error Code";
		}        
		return errorCode;

	}

}
