package invio.rn;

import invio.dao.GenericDAO;
import invio.entidade.Curriculo;
import invio.entidade.Livro;
import invio.entidade.Periodico;
import invio.entidade.Programa;
import invio.rn.pdf.QualisRN;
import java.util.List;

public class PeriodicoRN {

    private GenericDAO<Periodico> dao = new GenericDAO<Periodico>();
    private QualisRN qualisRN = new QualisRN();
    private GenericDAO<Curriculo> curriculoDAO = new GenericDAO<Curriculo>();
    
    public boolean salvarPAtual(Periodico periodico) {
       boolean salvou = false;

        if (dao.iniciarTransacao()) {
            if (periodico.getId() == null) {
                if (dao.criar(periodico)) {
                    salvou = true;
                }
            } else {
                if (dao.alterar(periodico)) {
                    salvou = true;
                }
            }
            dao.concluirTransacao();
        }
        return salvou;
    }

    public boolean salvar(Periodico periodico) {
        boolean salvou = false;
        int pt = 0;
        Curriculo curriculoDoPeriodico = curriculoDAO.obter(Curriculo.class, periodico.getCurriculo().getId());
        List<Programa> programasDoCurr = curriculoDoPeriodico.getArea().getProgramaList();
        
        for (Programa temp : programasDoCurr) {
                int ptTemp = qualisRN.obterEstrato(periodico.getRevista(), temp.getArea().getNome());
                System.out.println("ptTemp"+ptTemp);
                if (ptTemp >= pt) {
                    pt = ptTemp;
                }
        }
        periodico.setEstrato(pt);
        
        
        if (dao.iniciarTransacao()) {
            if (periodico.getId() == null) {
                if (dao.criar(periodico)) {
                    salvou = true;
                }
            } else {
                if (dao.alterar(periodico)) {
                    salvou = true;
                }
            }
            dao.concluirTransacao();
        }
        return salvou;
    }

    public boolean remover(Periodico periodico) {
        return dao.excluir(periodico);
    }

    public Periodico obter(Integer id) {
        return dao.obter(Periodico.class, id);
    }

    public List<Periodico> obterTodos() {
        return dao.obterTodos(Periodico.class);
    }
}
