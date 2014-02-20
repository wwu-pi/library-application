package de.unimuenster.pi.library.ejb;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.unimuenster.pi.library.jpa.Copy;
import de.unimuenster.pi.library.jpa.Loan;
import de.unimuenster.pi.library.jpa.User;

/**
 * Session Bean implementation class LoanServiceBean
 * 
 * @author Henning Heitkoetter
 */
@Stateless
public class LoanServiceBean implements LoanService {
	@PersistenceContext
	private EntityManager em;

	@Override
	public Collection<Loan> getLoansOfUser(User user) {
		return em.createQuery("FROM Loan l WHERE l.user = :user", Loan.class).setParameter("user", user).getResultList();
	}

	@Override
	public void returnLoan(Loan loanToReturn, User user) {
		loanToReturn = em.find(Loan.class, loanToReturn.getId());
		user = em.find(User.class, user.getUid());
		if(loanToReturn.getUser().equals(user)){
			loanToReturn.setCopy(null);
			loanToReturn.setUser(null);
			em.remove(loanToReturn);
		}
		else
			throw new IllegalArgumentException("Argument 'user' and user of argument 'loanToReturn' do not match.");
	}

	@Override
	public void loan(User user, int copyToLoanInvNo) {
		Copy copyToLoan = em.find(Copy.class, copyToLoanInvNo);
		if(copyToLoan == null)
			throw new IllegalArgumentException("Invalid inventory number.");
		if(copyToLoan.getLoan() != null)
			throw new IllegalArgumentException("This copy is already lent out.");
		
		user = em.find(User.class, user.getUid());
		
		Loan newLoan = new Loan();
		newLoan.setUser(user);
		newLoan.setCopy(copyToLoan);
		
		// Ensure that date is in the past
		Calendar now = new GregorianCalendar();
		now.add(Calendar.MILLISECOND, -1);
		newLoan.setDate(now.getTime());
		em.persist(newLoan);
	}

}
