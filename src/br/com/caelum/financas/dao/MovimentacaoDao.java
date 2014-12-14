package br.com.caelum.financas.dao;

import br.com.caelum.financas.exception.ValorInvalidoException;
import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.modelo.Movimentacao;
import br.com.caelum.financas.modelo.TipoMovimentacao;
import br.com.caelum.financas.modelo.ValorPorMesEAno;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

@Stateless
// @TransactionManagement(TransactionManagementType.BEAN)
public class MovimentacaoDao {

//	@PersistenceContext(unitName = "controlefinancas") // good prectice according book EJB3 in action
    @Inject //solution to LazyInitializationException through Entity Manager per Request pattern at caelum pg. 174
	private EntityManager manager;

	// @Resource
	// private SessionContext sessionContext;

	public void adiciona(Movimentacao movimentacao) {
		// UserTransaction userTransaction =
		// sessionContext.getUserTransaction();
		// userTransaction.begin();
        manager.joinTransaction(); //Because of Entity Manager per Request pattern
		manager.persist(movimentacao);
		// userTransaction.commit();

		if (movimentacao.getValor().compareTo(BigDecimal.ZERO) < 0) { // vai dar rollback aqui
			//throw new RuntimeException("Movimentacao negativa");
            throw new ValorInvalidoException("Movimentacao negativa");
		}

	}

	public Movimentacao busca(Integer id) {
		return manager.find(Movimentacao.class, id);
	}

	public List<Movimentacao> lista() {
		return manager.createQuery("select m from Movimentacao m",
				Movimentacao.class).getResultList();
	}

	public void remove(Movimentacao movimentacao) {
		Movimentacao movimentacaoParaRemover = manager.find(Movimentacao.class, movimentacao.getId());
        manager.joinTransaction(); //Because of Entity Manager per Request pattern
		manager.remove(movimentacaoParaRemover);
	}

	public List<Movimentacao> listarTodasMovimentacoes(Conta conta) {
		String jpql = "SELECT m FROM Movimentacao m "
						+ "WHERE m.conta = :conta ORDER BY m.valor DESC";
		Query query = manager.createQuery(jpql);
		query.setParameter("conta", conta);

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Movimentacao> listarPorValorETipo(BigDecimal valor,	TipoMovimentacao tpMovimentacao) {
		String jpql = "SELECT m FROM Movimentacao m "
						+ "WHERE m.valor <= :valor AND m.tipoMovimentacao = :tipo";
		Query query = manager.createQuery(jpql);
		query.setParameter("valor", valor);
		query.setParameter("tipo", tpMovimentacao);
		return query.getResultList();
	}

    public BigDecimal calculaTotalMovimentado(Conta conta, TipoMovimentacao tipo){
        String jpql = "select sum(m.valor) from Movimentacao m where m.conta <= :conta and m.tipoMovimentacao = :tipo";
        TypedQuery<BigDecimal> query = manager.createQuery(jpql,BigDecimal.class);
        query.setParameter("conta",conta);
        query.setParameter("tipo", tipo);
        return query.getSingleResult();
    }

    public List<Movimentacao>  buscaTodasMovimentacoesDaConta(String titular){
        String jpql = "select m from Movimentacao m where m.conta.titular like :titular";
        TypedQuery<Movimentacao> query = manager.createQuery(jpql,Movimentacao.class);
        query.setParameter("titular", "%" + titular + "%");
        return query.getResultList();
    }

    public List<ValorPorMesEAno> listaMesesComMovimentacoes(Conta conta, TipoMovimentacao tipo){
        String jpql = "select new br.com.caelum.financas.modelo.ValorPorMesEAno(year(m.data),month(m.data),sum(m.valor)) " +
                "from Movimentacao m " +
                "where m.conta = :conta and m.tipoMovimentacao = :tipo " +
                "group by year(m.data) || month(m.data) " +
                "order by sum(m.valor) desc";
        Query query = manager.createQuery(jpql).
                setParameter("conta",conta).
                setParameter("tipo", tipo);

        return query.getResultList();
    }

    public List<Movimentacao> listaComCategoria(){
        String jpql = "select distinct m " +
                "from Movimentacao m " +
                "left join fetch m.categorias" ;
        return manager.createQuery(jpql).getResultList();
    }

    public List<Movimentacao> pesquisa(Conta conta, TipoMovimentacao tipoMovimentacao, Integer mes){
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Movimentacao> criteria = builder.createQuery(Movimentacao.class);
        Root<Movimentacao> root = criteria.from(Movimentacao.class);

        Predicate conjuction = builder.conjunction();

        if(conta.getId() != null){
            conjuction = builder.and(conjuction, builder.equal(root.<Conta>get("conta"), conta));
        }

        if(mes != null && mes != 0){
            Expression<Integer> expression = builder.function("month", Integer.class, root.<Calendar>get("data"));
            conjuction = builder.and(conjuction, builder.equal(expression, mes));
        }

        if(tipoMovimentacao != null){
            conjuction = builder.and(conjuction, builder.equal(root.<TipoMovimentacao>get("tipoMovimentacao"), tipoMovimentacao));
        }

        criteria.where(conjuction);

        return manager.createQuery(criteria).getResultList();
    }
}
