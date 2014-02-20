package de.unimuenster.pi.library.web.beans;


import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;

import de.unimuenster.pi.library.ejb.BookService;
import de.unimuenster.pi.library.jpa.Book;
import de.unimuenster.pi.library.web.Util;

/**
 * Backing bean for the create book page.
 * @author Henning Heitkoetter
 *
 */
@ManagedBean
public class CreateBook {
	private Book book;
	private Book lastBook;
	
	private boolean batch;
	
	private String errorMessage;
	
	@EJB
	private BookService bookService;

	public Book getBook() {
		if(book == null)
			book = new Book();
		return book;
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
			lastBook = bookService.createBook(getBook());
			book = null;
			errorMessage = null;
		}
		catch(EJBException e){
			errorMessage = "Book not created: " + Util.getConstraintMessage(e);
		}
		
		//Navigation
		if(isBatch() || errorMessage != null)
			return null;
		else
			return "listBooks";
	}

	public String getLastResult(){
		if(lastBook != null){
			return "Book created: " + lastBook.toString();
		}
		return errorMessage!=null?errorMessage:"";
	}
	
	public String getSuccess(){
		return errorMessage!=null?"error":"success";
	}
}
