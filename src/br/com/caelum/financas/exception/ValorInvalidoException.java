package br.com.caelum.financas.exception;

import javax.ejb.ApplicationException;

/**
 * Created by felipe on 18/11/14.
 */

@ApplicationException(rollback=true)
public class ValorInvalidoException extends RuntimeException{

    public ValorInvalidoException(String message){
        super(message);
    }
}