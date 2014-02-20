package de.unimuenster.pi.library.web.beans;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import de.unimuenster.pi.library.ejb.BookService;
import de.unimuenster.pi.library.ejb.CopyService;
import de.unimuenster.pi.library.jpa.Book;
import de.unimuenster.pi.library.jpa.Copy;
import de.unimuenster.pi.library.web.Util;

/**
 * Backing bean for the book detail page (book.xhtml).
 * @author Henning Heitkoetter
 *
 */
@ManagedBean
@ViewScoped
public class BookPage {
	@EJB
	private BookService bookEjb;
	@EJB
	private CopyService copyEjb;
	
	private int bookId;
	private Book book;

	public void ensureInitialized() {
		try{
			if(getBook() != null)
				// Success
				return;
		} catch(EJBException e) {
			e.printStackTrace();
		}
		Util.redirectToRoot();
	}
	
	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
		book = null;
	}

	public Book getBook() {
		if(book == null)
			book = bookEjb.getBook(getBookId());
		return book;
	}
	
	public Collection<Copy> getCopies(){
		return copyEjb.getCopiesOfMedium(getBook());
	}
	
	public void addCopy(){
		copyEjb.createCopy(book);
	}
}
