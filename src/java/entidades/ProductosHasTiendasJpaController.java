/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import entidades.exceptions.NonexistentEntityException;
import entidades.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Nuevo
 */
public class ProductosHasTiendasJpaController implements Serializable {

    public ProductosHasTiendasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ProductosHasTiendas productosHasTiendas) throws PreexistingEntityException, Exception {
        if (productosHasTiendas.getProductosHasTiendasPK() == null) {
            productosHasTiendas.setProductosHasTiendasPK(new ProductosHasTiendasPK());
        }
        productosHasTiendas.getProductosHasTiendasPK().setNProducto(productosHasTiendas.getProductos().getNombreProducto());
        productosHasTiendas.getProductosHasTiendasPK().setNTienda(productosHasTiendas.getTiendas().getNombreTienda());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Productos productos = productosHasTiendas.getProductos();
            if (productos != null) {
                productos = em.getReference(productos.getClass(), productos.getNombreProducto());
                productosHasTiendas.setProductos(productos);
            }
            Tiendas tiendas = productosHasTiendas.getTiendas();
            if (tiendas != null) {
                tiendas = em.getReference(tiendas.getClass(), tiendas.getNombreTienda());
                productosHasTiendas.setTiendas(tiendas);
            }
            em.persist(productosHasTiendas);
            if (productos != null) {
                productos.getProductosHasTiendasCollection().add(productosHasTiendas);
                productos = em.merge(productos);
            }
            if (tiendas != null) {
                tiendas.getProductosHasTiendasCollection().add(productosHasTiendas);
                tiendas = em.merge(tiendas);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProductosHasTiendas(productosHasTiendas.getProductosHasTiendasPK()) != null) {
                throw new PreexistingEntityException("ProductosHasTiendas " + productosHasTiendas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProductosHasTiendas productosHasTiendas) throws NonexistentEntityException, Exception {
        productosHasTiendas.getProductosHasTiendasPK().setNProducto(productosHasTiendas.getProductos().getNombreProducto());
        productosHasTiendas.getProductosHasTiendasPK().setNTienda(productosHasTiendas.getTiendas().getNombreTienda());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProductosHasTiendas persistentProductosHasTiendas = em.find(ProductosHasTiendas.class, productosHasTiendas.getProductosHasTiendasPK());
            Productos productosOld = persistentProductosHasTiendas.getProductos();
            Productos productosNew = productosHasTiendas.getProductos();
            Tiendas tiendasOld = persistentProductosHasTiendas.getTiendas();
            Tiendas tiendasNew = productosHasTiendas.getTiendas();
            if (productosNew != null) {
                productosNew = em.getReference(productosNew.getClass(), productosNew.getNombreProducto());
                productosHasTiendas.setProductos(productosNew);
            }
            if (tiendasNew != null) {
                tiendasNew = em.getReference(tiendasNew.getClass(), tiendasNew.getNombreTienda());
                productosHasTiendas.setTiendas(tiendasNew);
            }
            productosHasTiendas = em.merge(productosHasTiendas);
            if (productosOld != null && !productosOld.equals(productosNew)) {
                productosOld.getProductosHasTiendasCollection().remove(productosHasTiendas);
                productosOld = em.merge(productosOld);
            }
            if (productosNew != null && !productosNew.equals(productosOld)) {
                productosNew.getProductosHasTiendasCollection().add(productosHasTiendas);
                productosNew = em.merge(productosNew);
            }
            if (tiendasOld != null && !tiendasOld.equals(tiendasNew)) {
                tiendasOld.getProductosHasTiendasCollection().remove(productosHasTiendas);
                tiendasOld = em.merge(tiendasOld);
            }
            if (tiendasNew != null && !tiendasNew.equals(tiendasOld)) {
                tiendasNew.getProductosHasTiendasCollection().add(productosHasTiendas);
                tiendasNew = em.merge(tiendasNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ProductosHasTiendasPK id = productosHasTiendas.getProductosHasTiendasPK();
                if (findProductosHasTiendas(id) == null) {
                    throw new NonexistentEntityException("The productosHasTiendas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ProductosHasTiendasPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProductosHasTiendas productosHasTiendas;
            try {
                productosHasTiendas = em.getReference(ProductosHasTiendas.class, id);
                productosHasTiendas.getProductosHasTiendasPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The productosHasTiendas with id " + id + " no longer exists.", enfe);
            }
            Productos productos = productosHasTiendas.getProductos();
            if (productos != null) {
                productos.getProductosHasTiendasCollection().remove(productosHasTiendas);
                productos = em.merge(productos);
            }
            Tiendas tiendas = productosHasTiendas.getTiendas();
            if (tiendas != null) {
                tiendas.getProductosHasTiendasCollection().remove(productosHasTiendas);
                tiendas = em.merge(tiendas);
            }
            em.remove(productosHasTiendas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ProductosHasTiendas> findProductosHasTiendasEntities() {
        return findProductosHasTiendasEntities(true, -1, -1);
    }

    public List<ProductosHasTiendas> findProductosHasTiendasEntities(int maxResults, int firstResult) {
        return findProductosHasTiendasEntities(false, maxResults, firstResult);
    }

    private List<ProductosHasTiendas> findProductosHasTiendasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProductosHasTiendas.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public ProductosHasTiendas findProductosHasTiendas(ProductosHasTiendasPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProductosHasTiendas.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductosHasTiendasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProductosHasTiendas> rt = cq.from(ProductosHasTiendas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
