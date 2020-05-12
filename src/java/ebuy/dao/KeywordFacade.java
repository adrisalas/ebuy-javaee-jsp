/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ebuy.dao;

import ebuy.entity.Keyword;
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
public class KeywordFacade extends AbstractFacade<Keyword> {

    @PersistenceContext(unitName = "EbuyPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KeywordFacade() {
        super(Keyword.class);
    }
    
        
    public List<String> findAllName() {
        Query q;
 
        q = this.getEntityManager().createQuery("SELECT k.name FROM Keyword k ");
        return q.getResultList();
    }
    
    public List<Keyword> findByName(String name) {
        Query q;

        q = this.getEntityManager().createQuery("SELECT k FROM Keyword k WHERE k.name = :kName");
        q.setParameter("kName", name);
        return q.getResultList();
    }
    
}
