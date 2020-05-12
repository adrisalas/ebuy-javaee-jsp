/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ebuy.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author adrsa
 */
@Entity
@Table(name = "KEYWORD")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Keyword.findAll", query = "SELECT k FROM Keyword k")
    , @NamedQuery(name = "Keyword.findByKeywordId", query = "SELECT k FROM Keyword k WHERE k.keywordId = :keywordId")
    , @NamedQuery(name = "Keyword.findByName", query = "SELECT k FROM Keyword k WHERE k.name = :name")})
public class Keyword implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "KEYWORD_ID")
    private Integer keywordId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "NAME")
    private String name;
    @ManyToMany(mappedBy = "keywordList")
    private List<Product> productList;

    public Keyword() {
    }

    public Keyword(Integer keywordId) {
        this.keywordId = keywordId;
    }

    public Keyword(Integer keywordId, String name) {
        this.keywordId = keywordId;
        this.name = name;
    }

    public Integer getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(Integer keywordId) {
        this.keywordId = keywordId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (keywordId != null ? keywordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Keyword)) {
            return false;
        }
        Keyword other = (Keyword) object;
        if ((this.keywordId == null && other.keywordId != null) || (this.keywordId != null && !this.keywordId.equals(other.keywordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebuy.entity.Keyword[ keywordId=" + keywordId + " ]";
    }
    
}
