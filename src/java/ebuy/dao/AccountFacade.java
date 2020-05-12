/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ebuy.dao;

import ebuy.entity.Account;
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
public class AccountFacade extends AbstractFacade<Account> {

    @PersistenceContext(unitName = "EbuyPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccountFacade() {
        super(Account.class);
    }
     public Account findByEmail (String email) {
        Query q;
        Account user = null;
        List<Account> list;

        // @NamedQuery(name = "Account.findByEmail", query = "SELECT a FROM Account a WHERE a.email = :email")
        q = this.getEntityManager().createNamedQuery("Account.findByEmail");
        q.setParameter("email", email);

        list = q.getResultList(); 
        if (list != null && !list.isEmpty()) { 
            user = list.get(0);
        }
        return user;
    }
    
    public Account findByNickname (String nickname) {
        Query q;
        Account user = null;
        List<Account> list;

        //@NamedQuery(name = "Account.findByNickname", query = "SELECT a FROM Account a WHERE a.nickname = :nickname")
        q = this.getEntityManager().createNamedQuery("Account.findByNickname");
        q.setParameter("nickname", nickname);

        list = q.getResultList(); 
        if (list != null && !list.isEmpty()) { 
            user = list.get(0);
        }
        return user;
    }
}
