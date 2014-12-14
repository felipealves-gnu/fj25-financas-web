package br.com.caelum.financas.dao;

import javax.ejb.Stateless;

/**
 * Created by felipe on 01/12/14.
 */
@Stateless
public class BarBean {

    public void bar(){
        throw new RuntimeException("Erro INESPERADOOOOO");
    }
}
