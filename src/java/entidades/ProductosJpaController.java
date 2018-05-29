/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import entidades.exceptions.IllegalOrphanException;
import entidades.exceptions.NonexistentEntityException;
import entidades.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Nuevo
 */
public class ProductosJpaController implements Serializable {

    public ProductosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Productos productos) throws PreexistingEntityException, Exception {
        if (productos.getProductosHasTiendasCollection() == null) {
            productos.setProductosHasTiendasCollection(new ArrayList<ProductosHasTiendas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<ProductosHasTiendas> attachedProductosHasTiendasCollection = new ArrayList<ProductosHasTiendas>();
            for (ProductosHasTiendas productosHasTiendasCollectionProductosHasTiendasToAttach : productos.getProductosHasTiendasCollection()) {
                productosHasTiendasCollectionProductosHasTiendasToAttach = em.getReference(productosHasTiendasCollectionProductosHasTiendasToAttach.getClass(), productosHasTiendasCollectionProductosHasTiendasToAttach.getProductosHasTiendasPK());
                attachedProductosHasTiendasCollection.add(productosHasTiendasCollectionProductosHasTiendasToAttach);
            }
            productos.setProductosHasTiendasCollection(attachedProductosHasTiendasCollection);
            em.persist(productos);
            for (ProductosHasTiendas productosHasTiendasCollectionProductosHasTiendas : productos.getProductosHasTiendasCollection()) {
                Productos oldProductosOfProductosHasTiendasCollectionProductosHasTiendas = productosHasTiendasCollectionProductosHasTiendas.getProductos();
                productosHasTiendasCollectionProductosHasTiendas.setProductos(productos);
                productosHasTiendasCollectionProductosHasTiendas = em.merge(productosHasTiendasCollectionProductosHasTiendas);
                if (oldProductosOfProductosHasTiendasCollectionProductosHasTiendas != null) {
                    oldProductosOfProductosHasTiendasCollectionProductosHasTiendas.getProductosHasTiendasCollection().remove(productosHasTiendasCollectionProductosHasTiendas);
                    oldProductosOfProductosHasTiendasCollectionProductosHasTiendas = em.merge(oldProductosOfProductosHasTiendasCollectionProductosHasTiendas);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProductos(productos.getNombreProducto()) != null) {
                throw new PreexistingEntityException("Productos " + productos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Productos productos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Productos persistentProductos = em.find(Productos.class, productos.getNombreProducto());
            Collection<ProductosHasTiendas> productosHasTiendasCollectionOld = persistentProductos.getProductosHasTiendasCollection();
            Collection<ProductosHasTiendas> productosHasTiendasCollectionNew = productos.getProductosHasTiendasCollection();
            List<String> illegalOrphanMessages = null;
            for (ProductosHasTiendas productosHasTiendasCollectionOldProductosHasTiendas : productosHasTiendasCollectionOld) {
                if (!productosHasTiendasCollectionNew.contains(productosHasTiendasCollectionOldProductosHasTiendas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProductosHasTiendas " + productosHasTiendasCollectionOldProductosHasTiendas + " since its productos field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<ProductosHasTiendas> attachedProductosHasTiendasCollectionNew = new ArrayList<ProductosHasTiendas>();
            for (ProductosHasTiendas productosHasTiendasCollectionNewProductosHasTiendasToAttach : productosHasTiendasCollectionNew) {
                productosHasTiendasCollectionNewProductosHasTiendasToAttach = em.getReference(productosHasTiendasCollectionNewProductosHasTiendasToAttach.getClass(), productosHasTiendasCollectionNewProductosHasTiendasToAttach.getProductosHasTiendasPK());
                attachedProductosHasTiendasCollectionNew.add(productosHasTiendasCollectionNewProductosHasTiendasToAttach);
            }
            productosHasTiendasCollectionNew = attachedProductosHasTiendasCollectionNew;
            productos.setProductosHasTiendasCollection(productosHasTiendasCollectionNew);
            productos = em.merge(productos);
            for (ProductosHasTiendas productosHasTiendasCollectionNewProductosHasTiendas : productosHasTiendasCollectionNew) {
                if (!productosHasTiendasCollectionOld.contains(productosHasTiendasCollectionNewProductosHasTiendas)) {
                    Productos oldProductosOfProductosHasTiendasCollectionNewProductosHasTiendas = productosHasTiendasCollectionNewProductosHasTiendas.getProductos();
                    productosHasTiendasCollectionNewProductosHasTiendas.setProductos(productos);
                    productosHasTiendasCollectionNewProductosHasTiendas = em.merge(productosHasTiendasCollectionNewProductosHasTiendas);
                    if (oldProductosOfProductosHasTiendasCollectionNewProductosHasTiendas != null && !oldProductosOfProductosHasTiendasCollectionNewProductosHasTiendas.equals(productos)) {
                        oldProductosOfProductosHasTiendasCollectionNewProductosHasTiendas.getProductosHasTiendasCollection().remove(productosHasTiendasCollectionNewProductosHasTiendas);
                        oldProductosOfProductosHasTiendasCollectionNewProductosHasTiendas = em.merge(oldProductosOfProductosHasTiendasCollectionNewProductosHasTiendas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = productos.getNombreProducto();
                if (findProductos(id) == null) {
                    throw new NonexistentEntityException("The productos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Productos productos;
            try {
                productos = em.getReference(Productos.class, id);
                productos.getNombreProducto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The productos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ProductosHasTiendas> productosHasTiendasCollectionOrphanCheck = productos.getProductosHasTiendasCollection();
            for (ProductosHasTiendas productosHasTiendasCollectionOrphanCheckProductosHasTiendas : productosHasTiendasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Productos (" + productos + ") cannot be destroyed since the ProductosHasTiendas " + productosHasTiendasCollectionOrphanCheckProductosHasTiendas + " in its productosHasTiendasCollection field has a non-nullable productos field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(productos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Productos> findProductosEntities() {
        return findProductosEntities(true, -1, -1);
    }

    public List<Productos> findProductosEntities(int maxResults, int firstResult) {
        return findProductosEntities(false, maxResults, firstResult);
    }

    private List<Productos> findProductosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Productos.class));
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

    public Productos findProductos(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Productos.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Productos> rt = cq.from(Productos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
