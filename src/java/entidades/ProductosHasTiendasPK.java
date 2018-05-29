/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Nuevo
 */
@Embeddable
public class ProductosHasTiendasPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "n_producto")
    private String nProducto;
    @Basic(optional = false)
    @Column(name = "n_tienda")
    private String nTienda;

    public ProductosHasTiendasPK() {
    }

    public ProductosHasTiendasPK(String nProducto, String nTienda) {
        this.nProducto = nProducto;
        this.nTienda = nTienda;
    }

    public String getNProducto() {
        return nProducto;
    }

    public void setNProducto(String nProducto) {
        this.nProducto = nProducto;
    }

    public String getNTienda() {
        return nTienda;
    }

    public void setNTienda(String nTienda) {
        this.nTienda = nTienda;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nProducto != null ? nProducto.hashCode() : 0);
        hash += (nTienda != null ? nTienda.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductosHasTiendasPK)) {
            return false;
        }
        ProductosHasTiendasPK other = (ProductosHasTiendasPK) object;
        if ((this.nProducto == null && other.nProducto != null) || (this.nProducto != null && !this.nProducto.equals(other.nProducto))) {
            return false;
        }
        if ((this.nTienda == null && other.nTienda != null) || (this.nTienda != null && !this.nTienda.equals(other.nTienda))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.ProductosHasTiendasPK[ nProducto=" + nProducto + ", nTienda=" + nTienda + " ]";
    }
    
}
