package de.unimuenster.pi.library.ejb;
import java.util.Collection;

import javax.ejb.Remote;

import de.unimuenster.pi.library.jpa.User;

/**
 * Remote interface of session bean for user management
 * @author Henning Heitkoetter
 *
 */
@Remote
public interface UserService {
	User createUser(String name, String address);
	
	User createUser(User newUser);
	
	Collection<User> getAllUsers();

	User getUser(int userId);
}
