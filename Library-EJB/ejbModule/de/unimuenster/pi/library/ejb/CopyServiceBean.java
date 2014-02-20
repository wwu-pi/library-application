package de.unimuenster.pi.library.ejb;

import java.util.Collection;
import java.util.LinkedList;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.unimuenster.pi.library.jpa.Copy;
import de.unimuenster.pi.library.jpa.Medium;

/**
 * Session Bean implementation class CopyServiceBean.
 * 
 * @author Henning Heitkoetter
 */
@Stateless
public class CopyServiceBean implements CopyService {
	@PersistenceContext
	private EntityManager em;

	@Override
	public Collection<Copy> getCopiesOfMedium(Medium m) {
		return em.createQuery("FROM Copy c WHERE c.medium=:m", Copy.class).setParameter("m", m).getResultList();
	}

	@Override
	public Collection<Copy> createCopies(Medium m, int count) {
		Collection<Copy> result = new LinkedList<Copy>();
		m = em.find(Medium.class, m.getId());
		for(int i = 0; i < count; i++){
			Copy newCopy = new Copy();
			newCopy.setMedium(m);
			em.persist(newCopy);
			result.add(newCopy);
		}
		return result;
	}

	@Override
	public Copy createCopy(Medium m) {
		return createCopies(m, 1).iterator().next();
	}

	@Override
	public Copy getCopy(int invNo) {
		return em.find(Copy.class, invNo);
	}

}
