package de.unimuenster.pi.library.ejb;

import java.util.Collection;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import de.unimuenster.pi.library.jpa.Book;

/**
 * Session Bean implementation class BookService.
 * 
 * Also provides an exemplary web service with two operations.
 * @author Henning Heitkoetter
 */
@Stateless
@WebService(name = "BookWebService", serviceName = "BookService", portName = "BookServicePort", 
		targetNamespace = "http://ws.library.pi.unimuenster.de/")
public class BookServiceBean implements BookService {
	@PersistenceContext
	private EntityManager em;

	@Override
	public Book createBook(@WebParam(name = "name") String name,
			@WebParam(name = "author") String author,
			@WebParam(name = "isbn") String isbn) {
		Book newBook = new Book();
		newBook.setTitle(name);
		newBook.setAuthor(author);
		newBook.setIsbn(isbn);
		return createBook(newBook);
	}

	@Override
	@WebMethod(exclude = true)
	public Book createBook(Book book) {
		// Normalize ISBN
		if (book.getIsbn() != null)
			book.setIsbn(book.getIsbn().replaceAll("[\\- ]", ""));

		if (em.createQuery("SELECT COUNT(*) FROM Book WHERE ISBN=:isbn", Long.class).setParameter("isbn", book.getIsbn())
				.getSingleResult() > 0)
			throw new EJBException(new ConstraintViolationException(
					"ISBN already in database", null));

		em.persist(book);
		return book;
	}

	@Override
	public Book getBook(int bookId) {
		Book book = em.find(Book.class, bookId);
		if (book == null)
			throw new IllegalArgumentException(String.format(
					"Book with ID %s not found", bookId));
		return book;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<Book> getAllBooks() {
		return em.createQuery("FROM Book", Book.class).getResultList();
	}
}
