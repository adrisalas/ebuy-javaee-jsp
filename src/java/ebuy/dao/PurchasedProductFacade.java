/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ebuy.dao;

import ebuy.entity.PurchasedProduct;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author victoria
 */
@Stateless
public class PurchasedProductFacade extends AbstractFacade<PurchasedProduct> {

    @PersistenceContext(unitName = "EbuyPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PurchasedProductFacade() {
        super(PurchasedProduct.class);
    }
    
    public List<PurchasedProduct> findByBuyerId(Integer id) {
        Query q;

        q = this.getEntityManager().createQuery("SELECT p FROM PurchasedProduct p WHERE p.buyerId.userId = :idUsuario order by p.purchaseDate desc");
        q.setParameter("idUsuario", id);
        return q.getResultList();
    }
        
}
