package com.aiservice.constraint;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BucketValidator implements ConstraintValidator<Bucket, Object>{


    private String selectedFieldName;
    private String message;
    private String bucketName;


    @Override
    public void initialize(Bucket requiredIfChecked) {
        selectedFieldName = requiredIfChecked.selectedFieldName();
        message = requiredIfChecked.message();
        bucketName= requiredIfChecked.bucketName();
    }


    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        Boolean valid = true;
        try {
            Object seletedFieldValue = BeanUtils.getProperty(object, selectedFieldName);            
            if(seletedFieldValue!=null && StringUtils.isNotBlank(seletedFieldValue.toString())){
                String[] bucketValues = BeanUtils.getArrayProperty(object, bucketName);
                if(bucketValues!=null  && bucketValues.length>0) {
                    valid = Arrays.stream(bucketValues).anyMatch(seletedFieldValue.toString()::equals);
                    if(Boolean.FALSE.equals(valid)) {
                        context.disableDefaultConstraintViolation();   
                        context.buildConstraintViolationWithTemplate(getErrorCode(selectedFieldName)).addPropertyNode(selectedFieldName).addConstraintViolation();
                    }
                }else {
                    valid=false;
                    context.disableDefaultConstraintViolation();
                    message = message + java.text.MessageFormat.format("No data in the bucket {0} to match, contact to admin",bucketName);
                    context.buildConstraintViolationWithTemplate(message).addPropertyNode(selectedFieldName).addConstraintViolation();
                } 

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
           case "primaryContact" :
              // Statements
               errorCode="E00383";
              break; // break is optional
           case "addressType" :
              // Statements
               errorCode="E00385";
              break; // break is optional
           
           case "defaultAddress" :
               // Statements
               errorCode="E00387";
               break; // break is optional
           
           case "primaryBank" :
               // Statements
               errorCode="E00408";
               break; // break is optional
               
           case "gstinTaxpayer" :
               // Statements
               errorCode="E00424";
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
