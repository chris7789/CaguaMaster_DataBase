/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Nuevo
 */
@Entity
@Table(name = "productos_has_tiendas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductosHasTiendas.findAll", query = "SELECT p FROM ProductosHasTiendas p")
    , @NamedQuery(name = "ProductosHasTiendas.findByNProducto", query = "SELECT p FROM ProductosHasTiendas p WHERE p.productosHasTiendasPK.nProducto = :nProducto")
    , @NamedQuery(name = "ProductosHasTiendas.findByNTienda", query = "SELECT p FROM ProductosHasTiendas p WHERE p.productosHasTiendasPK.nTienda = :nTienda")
    , @NamedQuery(name = "ProductosHasTiendas.findByPrecio", query = "SELECT p FROM ProductosHasTiendas p WHERE p.precio = :precio")})
public class ProductosHasTiendas implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProductosHasTiendasPK productosHasTiendasPK;
    @Basic(optional = false)
    @Column(name = "precio")
    private int precio;
    @JoinColumn(name = "n_producto", referencedColumnName = "nombre_producto", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Productos productos;
    @JoinColumn(name = "n_tienda", referencedColumnName = "nombre_tienda", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Tiendas tiendas;

    public ProductosHasTiendas() {
    }

    public ProductosHasTiendas(ProductosHasTiendasPK productosHasTiendasPK) {
        this.productosHasTiendasPK = productosHasTiendasPK;
    }

    public ProductosHasTiendas(ProductosHasTiendasPK productosHasTiendasPK, int precio) {
        this.productosHasTiendasPK = productosHasTiendasPK;
        this.precio = precio;
    }

    public ProductosHasTiendas(String nProducto, String nTienda) {
        this.productosHasTiendasPK = new ProductosHasTiendasPK(nProducto, nTienda);
    }

    public ProductosHasTiendasPK getProductosHasTiendasPK() {
        return productosHasTiendasPK;
    }

    public void setProductosHasTiendasPK(ProductosHasTiendasPK productosHasTiendasPK) {
        this.productosHasTiendasPK = productosHasTiendasPK;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public Productos getProductos() {
        return productos;
    }

    public void setProductos(Productos productos) {
        this.productos = productos;
    }

    public Tiendas getTiendas() {
        return tiendas;
    }

    public void setTiendas(Tiendas tiendas) {
        this.tiendas = tiendas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productosHasTiendasPK != null ? productosHasTiendasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductosHasTiendas)) {
            return false;
        }
        ProductosHasTiendas other = (ProductosHasTiendas) object;
        if ((this.productosHasTiendasPK == null && other.productosHasTiendasPK != null) || (this.productosHasTiendasPK != null && !this.productosHasTiendasPK.equals(other.productosHasTiendasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.ProductosHasTiendas[ productosHasTiendasPK=" + productosHasTiendasPK + " ]";
    }
    
}
