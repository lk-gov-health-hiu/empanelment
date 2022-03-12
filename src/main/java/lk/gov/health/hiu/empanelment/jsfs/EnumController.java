/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.hiu.empanelment.jsfs;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import lk.gov.health.hiu.empanelment.entities.AreaType;

/**
 *
 * @author buddhika
 */
@Named
@SessionScoped
public class EnumController implements Serializable {

    /**
     * Creates a new instance of EnumController
     */
    public EnumController() {
    }
    
    public AreaType[] getAreaTypes(){
        return AreaType.values();
    }
    
}
