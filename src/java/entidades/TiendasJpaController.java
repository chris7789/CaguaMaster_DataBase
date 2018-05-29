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
public class TiendasJpaController implements Serializable {

    public TiendasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tiendas tiendas) throws PreexistingEntityException, Exception {
        if (tiendas.getProductosHasTiendasCollection() == null) {
            tiendas.setProductosHasTiendasCollection(new ArrayList<ProductosHasTiendas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<ProductosHasTiendas> attachedProductosHasTiendasCollection = new ArrayList<ProductosHasTiendas>();
            for (ProductosHasTiendas productosHasTiendasCollectionProductosHasTiendasToAttach : tiendas.getProductosHasTiendasCollection()) {
                productosHasTiendasCollectionProductosHasTiendasToAttach = em.getReference(productosHasTiendasCollectionProductosHasTiendasToAttach.getClass(), productosHasTiendasCollectionProductosHasTiendasToAttach.getProductosHasTiendasPK());
                attachedProductosHasTiendasCollection.add(productosHasTiendasCollectionProductosHasTiendasToAttach);
            }
            tiendas.setProductosHasTiendasCollection(attachedProductosHasTiendasCollection);
            em.persist(tiendas);
            for (ProductosHasTiendas productosHasTiendasCollectionProductosHasTiendas : tiendas.getProductosHasTiendasCollection()) {
                Tiendas oldTiendasOfProductosHasTiendasCollectionProductosHasTiendas = productosHasTiendasCollectionProductosHasTiendas.getTiendas();
                productosHasTiendasCollectionProductosHasTiendas.setTiendas(tiendas);
                productosHasTiendasCollectionProductosHasTiendas = em.merge(productosHasTiendasCollectionProductosHasTiendas);
                if (oldTiendasOfProductosHasTiendasCollectionProductosHasTiendas != null) {
                    oldTiendasOfProductosHasTiendasCollectionProductosHasTiendas.getProductosHasTiendasCollection().remove(productosHasTiendasCollectionProductosHasTiendas);
                    oldTiendasOfProductosHasTiendasCollectionProductosHasTiendas = em.merge(oldTiendasOfProductosHasTiendasCollectionProductosHasTiendas);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTiendas(tiendas.getNombreTienda()) != null) {
                throw new PreexistingEntityException("Tiendas " + tiendas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tiendas tiendas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tiendas persistentTiendas = em.find(Tiendas.class, tiendas.getNombreTienda());
            Collection<ProductosHasTiendas> productosHasTiendasCollectionOld = persistentTiendas.getProductosHasTiendasCollection();
            Collection<ProductosHasTiendas> productosHasTiendasCollectionNew = tiendas.getProductosHasTiendasCollection();
            List<String> illegalOrphanMessages = null;
            for (ProductosHasTiendas productosHasTiendasCollectionOldProductosHasTiendas : productosHasTiendasCollectionOld) {
                if (!productosHasTiendasCollectionNew.contains(productosHasTiendasCollectionOldProductosHasTiendas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProductosHasTiendas " + productosHasTiendasCollectionOldProductosHasTiendas + " since its tiendas field is not nullable.");
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
            tiendas.setProductosHasTiendasCollection(productosHasTiendasCollectionNew);
            tiendas = em.merge(tiendas);
            for (ProductosHasTiendas productosHasTiendasCollectionNewProductosHasTiendas : productosHasTiendasCollectionNew) {
                if (!productosHasTiendasCollectionOld.contains(productosHasTiendasCollectionNewProductosHasTiendas)) {
                    Tiendas oldTiendasOfProductosHasTiendasCollectionNewProductosHasTiendas = productosHasTiendasCollectionNewProductosHasTiendas.getTiendas();
                    productosHasTiendasCollectionNewProductosHasTiendas.setTiendas(tiendas);
                    productosHasTiendasCollectionNewProductosHasTiendas = em.merge(productosHasTiendasCollectionNewProductosHasTiendas);
                    if (oldTiendasOfProductosHasTiendasCollectionNewProductosHasTiendas != null && !oldTiendasOfProductosHasTiendasCollectionNewProductosHasTiendas.equals(tiendas)) {
                        oldTiendasOfProductosHasTiendasCollectionNewProductosHasTiendas.getProductosHasTiendasCollection().remove(productosHasTiendasCollectionNewProductosHasTiendas);
                        oldTiendasOfProductosHasTiendasCollectionNewProductosHasTiendas = em.merge(oldTiendasOfProductosHasTiendasCollectionNewProductosHasTiendas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = tiendas.getNombreTienda();
                if (findTiendas(id) == null) {
                    throw new NonexistentEntityException("The tiendas with id " + id + " no longer exists.");
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
            Tiendas tiendas;
            try {
                tiendas = em.getReference(Tiendas.class, id);
                tiendas.getNombreTienda();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tiendas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ProductosHasTiendas> productosHasTiendasCollectionOrphanCheck = tiendas.getProductosHasTiendasCollection();
            for (ProductosHasTiendas productosHasTiendasCollectionOrphanCheckProductosHasTiendas : productosHasTiendasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tiendas (" + tiendas + ") cannot be destroyed since the ProductosHasTiendas " + productosHasTiendasCollectionOrphanCheckProductosHasTiendas + " in its productosHasTiendasCollection field has a non-nullable tiendas field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tiendas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tiendas> findTiendasEntities() {
        return findTiendasEntities(true, -1, -1);
    }

    public List<Tiendas> findTiendasEntities(int maxResults, int firstResult) {
        return findTiendasEntities(false, maxResults, firstResult);
    }

    private List<Tiendas> findTiendasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tiendas.class));
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

    public Tiendas findTiendas(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tiendas.class, id);
        } finally {
            em.close();
        }
    }

    public int getTiendasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tiendas> rt = cq.from(Tiendas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
