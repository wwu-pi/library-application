package de.unimuenster.pi.library.ejb;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.unimuenster.pi.library.jpa.User;

/**
 * Session Bean implementation class UserServiceBean.
 * 
 * @author Henning Heitkoetter
 */
@Stateless
public class UserServiceBean implements UserService {
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public User createUser(String name, String address) {
		User newUser = new User();
		newUser.setName(name);
		newUser.setAddress(address);
		return createUser(newUser);
	}

	@Override
	public User createUser(User newUser) {
		em.persist(newUser);
		return newUser;
	}
	
	@Override
	public User getUser(int userId) {
		User user = em.find(User.class, userId);
		if(user == null)
			throw new IllegalArgumentException(String.format("User with ID %s not found", userId));
		return user;
	}

	@Override
	public Collection<User> getAllUsers() {
		return em.createQuery("FROM User", User.class).getResultList();
	}

}
