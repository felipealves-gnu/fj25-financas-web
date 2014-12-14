package br.com.caelum.financas.mb;

import br.com.caelum.financas.dao.ContaDao;
import br.com.caelum.financas.dao.FooBean;
import br.com.caelum.financas.modelo.Conta;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class ContasBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private FooBean fooBean;
    @Inject
    private ContaDao contaDao;
	private Conta conta = new Conta();
	private List<Conta> contas;

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public void grava() {
		if(this.conta.getId() == null){
			System.out.println("Adicionando conta");
			contaDao.adiciona(conta);
		}else{
			System.out.println("Alterando conta");
			contaDao.altera(conta);
		}
		this.contas = contaDao.lista();
		limpaFormularioDoJSF();
	}

	public List<Conta> getContas() {
		System.out.println("Listando as contas");
		if(this.contas == null){
			this.contas = contaDao.lista();
		}
		return this.contas;
	}

	public void remove() {
		System.out.println("Removendo a conta");
		contaDao.remove(this.conta);
		this.contas = contaDao.lista();

		limpaFormularioDoJSF();
	}

    @PostConstruct
    public void callFooBean(){
        //fooBean.caller();
    }

	/**
	 * Esse metodo apenas limpa o formulario da forma com que o JSF espera.
	 * Invoque-o no momento em que precisar do formulario vazio.
	 */
	private void limpaFormularioDoJSF() {
		this.conta = new Conta();
	}

    public ContaDao getContaDao() {
        return contaDao;
    }

    public void setContaDao(ContaDao contaDao) {
        this.contaDao = contaDao;
    }

    public void setContas(List<Conta> contas) {
        this.contas = contas;
    }
}
