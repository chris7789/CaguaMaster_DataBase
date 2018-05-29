package entidades;

import entidades.util.JsfUtil;
import entidades.util.PaginationHelper;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.persistence.Persistence;

@ManagedBean(name = "productosHasTiendasController")
@SessionScoped
public class ProductosHasTiendasController implements Serializable {

    private ProductosHasTiendas current;
    private DataModel items = null;
    private ProductosHasTiendasJpaController jpaController = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public ProductosHasTiendasController() {
    }

    public ProductosHasTiendas getSelected() {
        if (current == null) {
            current = new ProductosHasTiendas();
            current.setProductosHasTiendasPK(new entidades.ProductosHasTiendasPK());
            selectedItemIndex = -1;
        }
        return current;
    }

    private ProductosHasTiendasJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new ProductosHasTiendasJpaController(Persistence.createEntityManagerFactory("CaguamasterPU"));
        }
        return jpaController;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getJpaController().getProductosHasTiendasCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getJpaController().findProductosHasTiendasEntities(getPageSize(), getPageFirstItem()));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (ProductosHasTiendas) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new ProductosHasTiendas();
        current.setProductosHasTiendasPK(new entidades.ProductosHasTiendasPK());
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.getProductosHasTiendasPK().setNTienda(current.getTiendas().getNombreTienda());
            current.getProductosHasTiendasPK().setNProducto(current.getProductos().getNombreProducto());
            getJpaController().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProductosHasTiendasCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (ProductosHasTiendas) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            current.getProductosHasTiendasPK().setNTienda(current.getTiendas().getNombreTienda());
            current.getProductosHasTiendasPK().setNProducto(current.getProductos().getNombreProducto());
            getJpaController().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProductosHasTiendasUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (ProductosHasTiendas) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getJpaController().destroy(current.getProductosHasTiendasPK());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProductosHasTiendasDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getJpaController().getProductosHasTiendasCount();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getJpaController().findProductosHasTiendasEntities(1, selectedItemIndex).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findProductosHasTiendasEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findProductosHasTiendasEntities(), true);
    }

    @FacesConverter(forClass = ProductosHasTiendas.class)
    public static class ProductosHasTiendasControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProductosHasTiendasController controller = (ProductosHasTiendasController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "productosHasTiendasController");
            return controller.getJpaController().findProductosHasTiendas(getKey(value));
        }

        entidades.ProductosHasTiendasPK getKey(String value) {
            entidades.ProductosHasTiendasPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new entidades.ProductosHasTiendasPK();
            key.setNProducto(values[0]);
            key.setNTienda(values[1]);
            return key;
        }

        String getStringKey(entidades.ProductosHasTiendasPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getNProducto());
            sb.append(SEPARATOR);
            sb.append(value.getNTienda());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof ProductosHasTiendas) {
                ProductosHasTiendas o = (ProductosHasTiendas) object;
                return getStringKey(o.getProductosHasTiendasPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + ProductosHasTiendas.class.getName());
            }
        }

    }

}
