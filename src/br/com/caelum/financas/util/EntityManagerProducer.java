package br.com.caelum.financas.util;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

/**
 * Created by felipe on 17/11/14.
 */
public class EntityManagerProducer {

    @PersistenceUnit
    private EntityManagerFactory factory;

    @Produces
    @RequestScoped
    public EntityManager getEntityManager() throws Exception{
        return factory.createEntityManager();
    }

    public void close(@Disposes EntityManager manager) throws Exception{
        manager.close();
    }
}
