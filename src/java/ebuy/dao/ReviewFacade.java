/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ebuy.dao;

import ebuy.entity.Review;
import java.util.ArrayList;
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
public class ReviewFacade extends AbstractFacade<Review> {

    @PersistenceContext(unitName = "EbuyPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ReviewFacade() {
        super(Review.class);
    }

    public List<Review> findByCompraId(Integer id) {
        Query q;

        q = this.getEntityManager().createQuery("SELECT r FROM Review r WHERE r.purchaseId.purchaseId = :idCompra");
        q.setParameter("idCompra", id);
        return q.getResultList();
    }

    public List<Review> findByProductoId(Integer id) {
        Query q;

        q = this.getEntityManager().createQuery("SELECT r FROM Review r WHERE r.purchaseId.productId.productId = :idProducto");
        q.setParameter("idProducto", id);
        return q.getResultList();
    }

    public double obtenerMediaValoraciones(Integer id) {
        Query q;

        q = this.getEntityManager().createQuery("SELECT AVG(r.stars) FROM Review r WHERE r.purchaseId.productId.productId = :idProducto");
        q.setParameter("idProducto", id);
        List<Double> resultado = q.getResultList();
        List<Double> l= new ArrayList<>();
        l.add(null);

        if (resultado==null || resultado.equals(l)||  q.getResultList().isEmpty()) {
            return -1;
        } else {
            return (double)resultado.get(0);
        }
    }

}
