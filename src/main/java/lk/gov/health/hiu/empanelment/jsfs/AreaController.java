package lk.gov.health.hiu.empanelment.jsfs;

import lk.gov.health.hiu.empanelment.entities.Area;
import lk.gov.health.hiu.empanelment.jsfs.util.JsfUtil;
import lk.gov.health.hiu.empanelment.jsfs.util.JsfUtil.PersistAction;
import lk.gov.health.hiu.empanelment.sessions.AreaFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import lk.gov.health.hiu.empanelment.entities.AreaType;

@Named
@SessionScoped
public class AreaController implements Serializable {

    @EJB
    private lk.gov.health.hiu.empanelment.sessions.AreaFacade ejbFacade;
    private List<Area> items = null;
    private Area selected;

    public AreaController() {
    }

    public Area findArea(AreaType at, Area dis, Area dsd, String name, String sname, String tname, String aeraNumber, String areaCode) {
        String j = "select a "
                + " from Area a "
                + " where a.retired=:ret ";
        Map m = new HashMap();
        m.put("ret", false);

        if (at != null) {
            j += " and a.areaType=:at ";
            m.put("at", at);
        }
        if (dis != null) {
            j += " and a.district=:dis ";
            m.put("dis", dis);
        }
        if (dsd != null) {
            j += " and a.dsDivision=:dsd ";
            m.put("dsd", dsd);
        }
        if (name != null) {
            j += " and a.name=:name ";
            m.put("name", name);
        }
        if (sname != null) {
            j += " and a.sname=:sname ";
            m.put("sname", sname);
        }
        if (tname != null) {
            j += " and a.tname=:tname ";
            m.put("tname", tname);
        }
        if (aeraNumber != null) {
            j += " and a.aeraNumber=:aeraNumber ";
            m.put("aeraNumber", aeraNumber);
        }
        if (areaCode != null) {
            j += " and a.areaCode=:areaCode ";
            m.put("areaCode", areaCode);
        }
        System.out.println("m = " + m);
        System.out.println("j = " + j);
        Area a = getFacade().findFirstByJpql(j, m);
        System.out.println("a = " + a);
        if (a == null) {
            a = new Area();
            a.setAreaType(at);
            a.setName(name);
            a.setSname(sname);
            a.setTname(tname);
            a.setDistrict(dis);
            a.setDsDivision(dsd);
            a.setAreaCode(areaCode);
            a.setAreaNumber(aeraNumber);
            getFacade().create(a);
        }
        return a;
    }

    public Area getSelected() {
        return selected;
    }

    public void setSelected(Area selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private AreaFacade getFacade() {
        return ejbFacade;
    }

    public Area prepareCreate() {
        selected = new Area();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("AreaCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("AreaUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("AreaDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Area> completeAreas(String qry) {
        List<Area> tas = new ArrayList<>();
        if (qry == null || qry.trim().equals("")) {
            return tas;
        }

        for (Area a : getItems()) {
            if (a.getName() != null) {
                boolean contains = Pattern.compile(Pattern.quote(qry), Pattern.CASE_INSENSITIVE).matcher(a.getName()).find();
                if (contains) {
                    tas.add(a);
                }

            }
        }
        return tas;
    }

    public List<Area> getItems() {
        if (items == null) {
            String j = "select a from Area a where a.retired=:ret order by a.name";
            Map m = new HashMap();
            m.put("ret", false);
            items = getFacade().findByJpql(j, m);
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Area getArea(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Area> getItemsAvailableSelectMany() {
        return getItems();
    }

    public List<Area> getItemsAvailableSelectOne() {
        return getItems();
    }

    @FacesConverter(forClass = Area.class)
    public static class AreaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AreaController controller = (AreaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "areaController");
            return controller.getArea(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Area) {
                Area o = (Area) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Area.class.getName()});
                return null;
            }
        }

    }

}
