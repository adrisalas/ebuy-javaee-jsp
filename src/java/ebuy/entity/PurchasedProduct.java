/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ebuy.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author adrsa
 */
@Entity
@Table(name = "PURCHASED_PRODUCT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PurchasedProduct.findAll", query = "SELECT p FROM PurchasedProduct p")
    , @NamedQuery(name = "PurchasedProduct.findByPurchaseId", query = "SELECT p FROM PurchasedProduct p WHERE p.purchaseId = :purchaseId")
    , @NamedQuery(name = "PurchasedProduct.findByPurchaseDate", query = "SELECT p FROM PurchasedProduct p WHERE p.purchaseDate = :purchaseDate")
    , @NamedQuery(name = "PurchasedProduct.findByQuantity", query = "SELECT p FROM PurchasedProduct p WHERE p.quantity = :quantity")
    , @NamedQuery(name = "PurchasedProduct.findByPrice", query = "SELECT p FROM PurchasedProduct p WHERE p.price = :price")})
public class PurchasedProduct implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PURCHASE_ID")
    private Integer purchaseId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PURCHASE_DATE")
    @Temporal(TemporalType.DATE)
    private Date purchaseDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "QUANTITY")
    private int quantity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRICE")
    private double price;
    @JoinColumn(name = "BUYER_ID", referencedColumnName = "USER_ID")
    @ManyToOne(optional = false)
    private Account buyerId;
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
    @ManyToOne(optional = false)
    private Product productId;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "purchaseId")
    private Review review;

    public PurchasedProduct() {
    }

    public PurchasedProduct(Integer purchaseId) {
        this.purchaseId = purchaseId;
    }

    public PurchasedProduct(Integer purchaseId, Date purchaseDate, int quantity, double price) {
        this.purchaseId = purchaseId;
        this.purchaseDate = purchaseDate;
        this.quantity = quantity;
        this.price = price;
    }

    public Integer getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Integer purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Account getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Account buyerId) {
        this.buyerId = buyerId;
    }

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (purchaseId != null ? purchaseId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PurchasedProduct)) {
            return false;
        }
        PurchasedProduct other = (PurchasedProduct) object;
        if ((this.purchaseId == null && other.purchaseId != null) || (this.purchaseId != null && !this.purchaseId.equals(other.purchaseId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebuy.entity.PurchasedProduct[ purchaseId=" + purchaseId + " ]";
    }
    
}
