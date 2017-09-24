package org.unq.parser.lleca.status;

import java.util.Optional;

/**
 * Created by mrivero on 24/9/17.
 */
public class ParseResult {

    private Result resultCode;
    private String message;
    private Optional<String> errorReference; //line or word that was incorrect


    @Override
    public String toString(){
        //TODO define in order to print a better message
        return null;
    }

}
