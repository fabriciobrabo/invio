/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bpmlab.invio.rn;

import bpmlab.invio.dao.CurriculoDAO;
import bpmlab.invio.dao.GenericDAO;
import bpmlab.invio.entidade.Area;
import bpmlab.invio.entidade.Curriculo;
import bpmlab.invio.entidade.Livro;
import bpmlab.invio.entidade.Login;
import bpmlab.invio.entidade.Periodico;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Dir de Armas Marinha
 */
public class CurriculoRN implements Serializable {

    GenericDAO<Curriculo> dao = new GenericDAO<Curriculo>();
    CurriculoDAO curriculoDAO = new CurriculoDAO();
    GenericDAO<Area> daoArea = new GenericDAO<Area>();
    GenericDAO<Login> daoLogin = new GenericDAO<Login>();

    public boolean salvar(Curriculo c) {
        boolean salvou = false;

        if (c.getId() == null || c.getId().equals(0)) {
            c.setId(null);
            c.setFco(0);
            salvou = dao.criar(c);
        } else {
            if (c.getPeriodicoList() != null) {
                PeriodicoRN periodicoRN = new PeriodicoRN();
                for (Periodico p : c.getPeriodicoList()) {
                    periodicoRN.atribuirPontuacaoAutomaticamente(p);
                }
            }
            if (dao.alterar(c)) {
                salvou = true;
            }
        }
        return salvou;
    }

    public boolean remover(Curriculo c) {
        return dao.excluir(c);
    }

    public Curriculo obter(Integer id) {
        return dao.obter(Curriculo.class, id);
    }

    public List<Curriculo> obterTodos() {
        return dao.obterTodos(Curriculo.class);
    }

    //Aqui se obtém uma lista de Curriculo usuário por Login (Se Master ou não)
    public List<Curriculo> obterCurriculoLogin(Login login) {
        return curriculoDAO.findCurriculoByUsuario(login);
    }

    public List<Curriculo> obterTodosDesc() {
        List<Curriculo> obterTodosDesc = new ArrayList<Curriculo>();

        List<CurriculoPts> mapa = new ArrayList<CurriculoPts>();

        for (Curriculo curriculo : dao.obterTodos(Curriculo.class)) {

            int totalP = 0;
            int totalL = 0;

            for (Periodico periodico : curriculo.getPeriodicoList()) {
                totalP += periodico.getEstrato();
            }

            for (Livro livro : curriculo.getLivroList()) {
                totalL += livro.getEstrato();
            }
            CurriculoPts cpts = new CurriculoPts(curriculo, totalP + totalL);
            mapa.add(cpts);

        }

        Collections.sort(mapa);

        for (CurriculoPts curriculoPts : mapa) {
            obterTodosDesc.add(curriculoPts.getC());
        }

        return obterTodosDesc;
    }

    public List<Curriculo> obterTodosOrdenado() {
        return curriculoDAO.obterTodosOrdenado();
    }
    
}

class CurriculoPts implements Comparable<CurriculoPts> {

    private Curriculo c;
    private Integer pts;

    public CurriculoPts(Curriculo c, Integer pts) {
        this.c = c;
        this.pts = pts;
    }

    public Curriculo getC() {
        return c;
    }

    public void setC(Curriculo c) {
        this.c = c;
    }

    public Integer getPts() {
        return pts;
    }

    public void setPts(Integer pts) {
        this.pts = pts;
    }

    @Override
    public int compareTo(CurriculoPts o) {
        if (this.pts < o.getPts()) {
            return -1;
        }
        if (this.pts > o.getPts()) {
            return 1;
        }
        return 0;
    }

    public int calcularPontosProducao(Curriculo curriculo) {
        int total = 0;

        List<Livro> livros = curriculo.getLivroList();
        List<Periodico> periodicos = curriculo.getPeriodicoList();

        for (Periodico periodico : periodicos) {
            if (!periodico.getAvaliacao().trim().equals("")
                    && !periodico.getAvaliacao().trim().equals("Nao")
                    && periodico.getAvaliacao() != null) {

                total = total + periodico.getEstrato();
            }
        }

        for (Livro livro : livros) {
            if (!livro.getAvaliacao().trim().equals("")
                    && !livro.getAvaliacao().trim().equals("Nao")
                    && livro.getAvaliacao() != null) {

                total = total + livro.getEstrato();
            }
        }

        return total;
    }
    
}
