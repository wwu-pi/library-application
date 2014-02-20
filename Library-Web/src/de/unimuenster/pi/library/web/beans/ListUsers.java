package de.unimuenster.pi.library.web.beans;

import java.util.Collection;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import de.unimuenster.pi.library.ejb.UserService;
import de.unimuenster.pi.library.jpa.User;

/**
 * Backing bean for user list.
 * @author Henning Heitkoetter
 *
 */
@ManagedBean
public class ListUsers {
	@EJB
	private UserService ejb;
	
	private Collection<User> users;
	
	public Collection<User> getUsers(){
		if(users == null)
			users = ejb.getAllUsers();
		return users;
	}
}
