package br.com.caelum.financas.dao;

import br.com.caelum.financas.modelo.Categoria;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by felipe on 17/11/14.
 */
public class CategoriaDao {

    @PersistenceContext
    private EntityManager manager;

    public Categoria procura(Integer id){
        this.manager.joinTransaction();
        return this.manager.find(Categoria.class,id);
    }

    public List<Categoria> lista(){
//        this.manager.joinTransaction();
        return this.manager.createQuery("select c from Categoria c", Categoria.class)
                .getResultList();
    }
}
