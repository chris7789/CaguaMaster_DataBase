/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Nuevo
 */
@Entity
@Table(name = "tiendas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tiendas.findAll", query = "SELECT t FROM Tiendas t")
    , @NamedQuery(name = "Tiendas.findByNombreTienda", query = "SELECT t FROM Tiendas t WHERE t.nombreTienda = :nombreTienda")})
public class Tiendas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "nombre_tienda")
    private String nombreTienda;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tiendas")
    private Collection<ProductosHasTiendas> productosHasTiendasCollection;

    public Tiendas() {
    }

    public Tiendas(String nombreTienda) {
        this.nombreTienda = nombreTienda;
    }

    public String getNombreTienda() {
        return nombreTienda;
    }

    public void setNombreTienda(String nombreTienda) {
        this.nombreTienda = nombreTienda;
    }

    @XmlTransient
    public Collection<ProductosHasTiendas> getProductosHasTiendasCollection() {
        return productosHasTiendasCollection;
    }

    public void setProductosHasTiendasCollection(Collection<ProductosHasTiendas> productosHasTiendasCollection) {
        this.productosHasTiendasCollection = productosHasTiendasCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nombreTienda != null ? nombreTienda.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tiendas)) {
            return false;
        }
        Tiendas other = (Tiendas) object;
        if ((this.nombreTienda == null && other.nombreTienda != null) || (this.nombreTienda != null && !this.nombreTienda.equals(other.nombreTienda))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Tiendas[ nombreTienda=" + nombreTienda + " ]";
    }
    
}
