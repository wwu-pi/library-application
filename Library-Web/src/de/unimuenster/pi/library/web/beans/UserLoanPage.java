package de.unimuenster.pi.library.web.beans;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import de.unimuenster.pi.library.ejb.CopyService;
import de.unimuenster.pi.library.ejb.LoanService;
import de.unimuenster.pi.library.ejb.UserService;
import de.unimuenster.pi.library.jpa.Copy;
import de.unimuenster.pi.library.jpa.Loan;
import de.unimuenster.pi.library.jpa.User;
import de.unimuenster.pi.library.web.Util;

/**
 * Backing bean for user detail and loans page
 * @author Henning Heitkoetter
 *
 */
@ManagedBean
@ViewScoped
public class UserLoanPage {
	@EJB
	private UserService userEjb;
	@EJB
	private LoanService loanEjb;
	@EJB
	private CopyService copyEjb;
	
	private int userId;
	private User user;
	
	private Integer copyToLoanInvNo = null;

	public void ensureInitialized() {
		try{
			if(getUser() != null)
				// Success
				return;
		} catch(EJBException e) {
			e.printStackTrace();
		}
		Util.redirectToRoot();
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
		user = null;
		copyToLoanInvNo = null;
	}

	public User getUser() {
		if(user == null)
			user = userEjb.getUser(getUserId());
		return user;
	}
	
	public Collection<Loan> getLoans(){
		return loanEjb.getLoansOfUser(getUser());
	}
	
	public String returnLoan(Loan loanToReturn){
		loanEjb.returnLoan(loanToReturn, getUser());
		return null;
	}

	public void setCopyToLoanInvNo(Integer copyToLoanInvNo) {
		this.copyToLoanInvNo = copyToLoanInvNo;
	}

	public Integer getCopyToLoanInvNo() {
		return copyToLoanInvNo;
	}
	
	public Copy getCopyToLoan(){
		if(getCopyToLoanInvNo() != null)
			return copyEjb.getCopy(getCopyToLoanInvNo());
		return null;
	}
	
	public boolean isLoanPossible(){
		Copy copyToLoan = getCopyToLoan();
		return copyToLoan != null && copyToLoan.getLoan() == null;
	}
	
	public String getCopyToLoanDetail(){
		if(getCopyToLoanInvNo() == null)
			return "";
		Copy copyToLoan = getCopyToLoan();
		return copyToLoan == null ? "Unknown inventory number" : 
			(copyToLoan.getLoan() != null ? String.format("This copy of %s is already lent out.", copyToLoan.getMedium().getTitle()) : 
				copyToLoan.getMedium().getTitle());
	}
	
	public String loan(){
		loanEjb.loan(getUser(), getCopyToLoanInvNo());
		copyToLoanInvNo = null;
		return null;
	}
}
