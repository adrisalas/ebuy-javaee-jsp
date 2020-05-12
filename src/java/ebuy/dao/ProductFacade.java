/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ebuy.dao;

import ebuy.entity.Product;
import java.util.Date;
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
public class ProductFacade extends AbstractFacade<Product> {

    @PersistenceContext(unitName = "EbuyPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductFacade() {
        super(Product.class);
    }

    public List<Product> allProductsOrderByDate(){
      Query q;

        q = this.getEntityManager().createQuery("SELECT p FROM Product p order by p.creationDate desc, p.creationTime desc");
        
        return q.getResultList();
    }
    public List<Product> findByVendorId(Integer id) {
        Query q;

        q = this.getEntityManager().createQuery("SELECT p FROM Product p WHERE p.vendorId.userId = :idUsuario order by p.creationDate desc, p.creationTime desc");
        q.setParameter("idUsuario", id);
        return q.getResultList();
    }

    public List<Product> findByCategoria(Integer codigoCategoria) {
        Query q;

        q = this.getEntityManager().createQuery("SELECT p  FROM Product p WHERE p.subcategoryId.categoryId.categoryId = :codigo");
        q.setParameter("codigo", codigoCategoria);
        return q.getResultList();
    }

    public List<Product> findByKeyword(Integer codigoKeyword) {
        Query q;

        q = this.getEntityManager().createQuery("SELECT p  FROM Product p inner join p.keywordList k where k.keywordId = :codigo");
        q.setParameter("codigo", codigoKeyword);
        return q.getResultList();
    }

    public List<Product> findByDate(Date date) {
        Query q;
 
        q = this.getEntityManager().createQuery("SELECT p  FROM Product p where p.creationDate = :codigo");
        q.setParameter("codigo", date);
        return q.getResultList();
    }

    public List<Product> findByTime(Date time) {
        Query q;

        q = this.getEntityManager().createQuery("SELECT p  FROM Product p where p.creationTime = :codigo");
        q.setParameter("codigo", time);
        return q.getResultList();
    }

    
    public List<Product> findByTitulo(String titulo) {
        Query q;
 
        q = this.getEntityManager().createQuery("SELECT p FROM Product p WHERE  UPPER(p.title) LIKE  UPPER(:titulo)");
         q.setParameter("titulo", "%" + titulo + "%"); 
        return q.getResultList();
    }

    public List<Product> findByDescripcion(String descripcion) {
        Query q;
 
        q = this.getEntityManager().createQuery("SELECT p FROM Product p WHERE  UPPER(p.description) LIKE  UPPER(:descripcion)");
         q.setParameter("descripcion", "%" + descripcion + "%"); 
        return q.getResultList();
    }

}
