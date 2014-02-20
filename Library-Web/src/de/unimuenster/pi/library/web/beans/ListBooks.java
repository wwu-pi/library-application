package de.unimuenster.pi.library.web.beans;

import java.util.Collection;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import de.unimuenster.pi.library.ejb.BookService;
import de.unimuenster.pi.library.jpa.Book;

/**
 * Backing bean for book list.
 * @author Henning Heitkoetter
 *
 */
@ManagedBean
public class ListBooks {
	@EJB
	private BookService ejb;
	
	private Collection<Book> books;
	
	public Collection<Book> getBooks(){
		if(books == null)
			books = ejb.getAllBooks();
		return books;
	}
}
