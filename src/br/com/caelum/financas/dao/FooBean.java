package br.com.caelum.financas.dao;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Created by felipe on 01/12/14.
 */

@Stateless
public class FooBean {

    @Inject
    BarBean barBean;

    public void caller(){
        System.out.println("I'll call it");
        barBean.bar();
    }
}
