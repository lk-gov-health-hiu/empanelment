/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.hiu.empanelment.sessions;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lk.gov.health.hiu.empanelment.entities.Area;

/**
 *
 * @author buddhika
 */
@Stateless
public class AreaFacade extends AbstractFacade<Area> {

    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AreaFacade() {
        super(Area.class);
    }
    
}
