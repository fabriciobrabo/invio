package invio.rn;

import invio.dao.CurriculoDAO;
import invio.dao.LoginDAO;
import invio.entidade.Login;
import java.util.List;

/**
 *
 * @author Dir de Armas Marinha
 */
public class LoginRN {

    LoginDAO dao = new LoginDAO();

    public boolean salvar(Login login) {
        boolean salvou = false;

        if (dao.iniciarTransacao()) {
            if (login.getId() == null) {
                salvou = dao.criar(login);
            } else {
                salvou = dao.alterar(login);
            }
            dao.concluirTransacao();
        }
        return salvou;
    }

    public boolean remover(Login login) {
        return dao.excluir(login);
    }

    public Login obter(Integer id) {
        return dao.obter(Login.class, id);
    }

    public Login obter(String email) {
        return dao.obter(email);
    }

    public boolean existe(String email) {
        return dao.existe(email);
    }

    public List<Login> obterTodos() {
        return dao.obterTodos(Login.class);
    }
}