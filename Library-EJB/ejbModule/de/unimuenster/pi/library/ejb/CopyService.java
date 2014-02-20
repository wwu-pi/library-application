package de.unimuenster.pi.library.ejb;
import java.util.Collection;

import javax.ejb.Remote;

import de.unimuenster.pi.library.jpa.Copy;
import de.unimuenster.pi.library.jpa.Medium;

/**
 * Remote interface of session bean for managing copies of mediums.
 * @author Henning Heitkoetter
 *
 */
@Remote
public interface CopyService {
	Collection<Copy> getCopiesOfMedium(Medium m);
	
	Collection<Copy> createCopies(Medium m, int count);
	
	Copy createCopy(Medium m);

	Copy getCopy(int invNo);
}
