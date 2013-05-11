/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package invio.entidade;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author fabio
 */
@Entity
@Table(name = "edital")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Edital.findAll", query = "SELECT e FROM Edital e"),
    @NamedQuery(name = "Edital.findById", query = "SELECT e FROM Edital e WHERE e.id = :id"),
    @NamedQuery(name = "Edital.findByTitulo", query = "SELECT e FROM Edital e WHERE e.titulo = :titulo"),
    @NamedQuery(name = "Edital.findByNumero", query = "SELECT e FROM Edital e WHERE e.numero = :numero"),
    @NamedQuery(name = "Edital.findByAno", query = "SELECT e FROM Edital e WHERE e.ano = :ano"),
    @NamedQuery(name = "Edital.findByDatainicial", query = "SELECT e FROM Edital e WHERE e.datainicial = :datainicial"),
    @NamedQuery(name = "Edital.findByDatafinal", query = "SELECT e FROM Edital e WHERE e.datafinal = :datafinal")})
public class Edital implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "titulo")
    private String titulo;
    @Basic(optional = false)
    @Column(name = "numero")
    private int numero;
    @Basic(optional = false)
    @Column(name = "ano")
    private int ano;
    @Basic(optional = false)
    @Lob
    @Column(name = "resumo")
    private String resumo;
    @Basic(optional = false)
    @Column(name = "datainicial")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datainicial;
    @Basic(optional = false)
    @Column(name = "datafinal")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datafinal;
    @JoinColumn(name = "instituicao", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Instituicao instituicao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "edital")
    private List<Plano> planoList;

    public Edital() {
    }

    public Edital(Integer id) {
        this.id = id;
    }

    public Edital(Integer id, int numero, int ano, String resumo, Date datainicial, Date datafinal) {
        this.id = id;
        this.numero = numero;
        this.ano = ano;
        this.resumo = resumo;
        this.datainicial = datainicial;
        this.datafinal = datafinal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public Date getDatainicial() {
        return datainicial;
    }

    public void setDatainicial(Date datainicial) {
        this.datainicial = datainicial;
    }

    public Date getDatafinal() {
        return datafinal;
    }

    public void setDatafinal(Date datafinal) {
        this.datafinal = datafinal;
    }

    public Instituicao getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(Instituicao instituicao) {
        this.instituicao = instituicao;
    }

    @XmlTransient
    public List<Plano> getPlanoList() {
        return planoList;
    }

    public void setPlanoList(List<Plano> planoList) {
        this.planoList = planoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Edital)) {
            return false;
        }
        Edital other = (Edital) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "invio.entidade.Edital[ id=" + id + " ]";
    }
    
}
