package de.unimuenster.pi.library.web.beans;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;

import de.unimuenster.pi.library.ejb.UserService;
import de.unimuenster.pi.library.jpa.User;
import de.unimuenster.pi.library.web.Util;

/**
 * Backing bean for create user page.
 * @author Henning Heitkoetter
 *
 */
@ManagedBean
public class CreateUser {
	private User user;
	private User lastUser;
	
	private boolean batch;
	
	private String errorMessage;
	
	@EJB
	private UserService userService;

	public User getUser() {
		if(user == null)
			user = new User();
		return user;
	}

	public boolean isBatch() {
		return batch;
	}

	public void setBatch(boolean batch) {
		this.batch = batch;
	}
	
	public String persist(){
		// Action
		try{
			lastUser = userService.createUser(getUser());
			user = null;
			errorMessage = null;
		}
		catch(EJBException e){
			errorMessage = "User not created: " + Util.getConstraintMessage(e);
		}
		
		//Navigation
		if(isBatch() || errorMessage != null)
			return null;
		else
			return "listUsers";
	}
	
	public String getLastResult(){
		if(lastUser != null){
			return "User created: " + lastUser.toString();
		}
		return errorMessage!=null?errorMessage:"";
	}
	
	public String getSuccess(){
		return errorMessage!=null?"error":"success";
	}
}
