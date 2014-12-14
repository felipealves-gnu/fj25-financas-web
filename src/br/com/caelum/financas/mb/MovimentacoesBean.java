package br.com.caelum.financas.mb;

import br.com.caelum.financas.dao.CategoriaDao;
import br.com.caelum.financas.dao.ContaDao;
import br.com.caelum.financas.dao.MovimentacaoDao;
import br.com.caelum.financas.modelo.Categoria;
import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.modelo.Movimentacao;
import br.com.caelum.financas.modelo.TipoMovimentacao;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

@ManagedBean
@ViewScoped
public class MovimentacoesBean implements Serializable {

    private static final long serialVersionUID = 1L;


    @Inject
    private ContaDao contaDao;
    @Inject
    private MovimentacaoDao movimentacaoDao;
    @Inject
    private CategoriaDao categoriaDao;

    private List<Movimentacao> movimentacoes;
    private List<Categoria> categorias;

    private Movimentacao movimentacao = new Movimentacao();
    private Integer contaId;
    private Integer categoriaId;

//    public void adicionaCategoria(){
//        if(this.categoriaId != null && this.categoriaId > 0){
//            Categoria categoria = categoriaDao.procura(this.categoriaId);
//            this.movimentacao.getCategorias().add(categoria);
//        }
//    }

    public void grava() {
        System.out.println("Fazendo a gravacao da movimentacao");
        Conta contaRelacionada = contaDao.busca(contaId);
        movimentacao.setConta(contaRelacionada);
        movimentacaoDao.adiciona(movimentacao);

        limpaFormularioDoJSF();
    }


    public void remove() {
        System.out.println("Removendo a movimentacao");
        movimentacaoDao.remove(movimentacao);
        this.movimentacoes = movimentacaoDao.lista();
        limpaFormularioDoJSF();
    }

    public List<Movimentacao> getMovimentacoes() {
        this.movimentacoes = movimentacaoDao.lista();

        return movimentacoes;
    }

    public Movimentacao getMovimentacao() {
        if (movimentacao.getData() == null) {
            movimentacao.setData(Calendar.getInstance());
        }
        return movimentacao;
    }

    public void setMovimentacao(Movimentacao movimentacao) {
        this.movimentacao = movimentacao;
    }

    public Integer getContaId() {
        return contaId;
    }

    public void setContaId(Integer contaId) {
        this.contaId = contaId;
    }


    public Integer getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
    }

    /**
     * Esse metodo apenas limpa o formulario da forma com que o JSF espera.
     * Invoque-o no momento manager que precisar do formulario vazio.
     */
    private void limpaFormularioDoJSF() {
        this.movimentacao = new Movimentacao();
    }

    public TipoMovimentacao[] getTiposDeMovimentacao() {
        return TipoMovimentacao.values();
    }

    public List<Categoria> getCategorias() {
        if (this.categorias == null) {
            System.out.println("Listando as categorias");
            this.categorias = this.categoriaDao.lista();
        }
        return this.categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;

    }
}