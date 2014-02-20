package de.unimuenster.pi.library.ejb;
import java.util.Collection;

import javax.ejb.Remote;

import de.unimuenster.pi.library.jpa.Loan;
import de.unimuenster.pi.library.jpa.User;

/**
 * Remote interface of session bean managing loans.
 * @author Henning Heitkoetter
 *
 */
@Remote
public interface LoanService {

	Collection<Loan> getLoansOfUser(User user);

	void returnLoan(Loan loanToReturn, User user);

	void loan(User user, int copyToLoanInvNo);

}
