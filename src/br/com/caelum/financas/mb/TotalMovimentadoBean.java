package br.com.caelum.financas.mb;

import br.com.caelum.financas.dao.MovimentacaoDao;
import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.modelo.TipoMovimentacao;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;

@Named
@RequestScoped
public class TotalMovimentadoBean {

    @Inject
    private MovimentacaoDao dao;

    private BigDecimal total;
    private Conta conta = new Conta();
    private TipoMovimentacao tipoMovimentacao;

	public void calcula() {
		System.out.println("Total movimentado pela conta");
        this.total = this.dao.calculaTotalMovimentado(conta, tipoMovimentacao);
    }

	public BigDecimal getTotal() {
		return total;
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public TipoMovimentacao getTipoMovimentacao() {
		return tipoMovimentacao;
	}

	public void setTipoMovimentacao(TipoMovimentacao tipoMovimentacao) {
		this.tipoMovimentacao = tipoMovimentacao;
	}

}
