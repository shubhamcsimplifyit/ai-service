package com.aiservice.exception.custom;


public class AiServiceBusinessException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public AiServiceBusinessException() {
        super();
    }
      
    public AiServiceBusinessException(Throwable cause) {

        super(cause);

    }


    public AiServiceBusinessException(String message, Throwable cause) {

        super(message, cause);


    }

    public AiServiceBusinessException(String message) {

        super(message);

    }

    
}

