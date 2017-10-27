package org.unq.parser.lleca.status;

import java.util.Optional;

/**
 * Created by mrivero on 24/9/17.
 */
public class ParseResult {

    private Result resultCode;
    private String message;
    private Optional<String> errorReference; //line or word that was incorrect

    public ParseResult(Result code, String description, Optional<String> reference){
        resultCode = code;
        message = description;
        errorReference = reference;
    }


    private String getErrorReference(){
        return errorReference.isPresent()? " when process " + errorReference.get() : "";
    }

    public Result getResult(){
        return resultCode;
    }

    @Override
    public String toString(){
        return "[" + resultCode  + "] " + message + " " + getErrorReference();
    }

}
