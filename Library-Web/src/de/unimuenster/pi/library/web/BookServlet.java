package de.unimuenster.pi.library.web;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import de.unimuenster.pi.library.ejb.BookService;
import de.unimuenster.pi.library.jpa.Book;

/**
 * Servlet implementation class BookServlet.
 * 
 * Parameters: title, author, isbn
 * 
 * @author Henning Heitkoetter
 */
@WebServlet("/BookServlet")
public class BookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private BookService ejb;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintStream out = new PrintStream(response.getOutputStream());
		try{
			Book newBook = ejb.createBook(request.getParameter("title"), request.getParameter("author"), request.getParameter("isbn"));
			out.println("Book successfully created.<br/>");
			out.println(newBook);
		}
		catch (EJBException e) {
			out.println("Book could not be created.<br/>");
			if(e.getCausedByException() instanceof ConstraintViolationException){
				out.println("Reason(s):<br/>");
				ConstraintViolationException cve = (ConstraintViolationException) e.getCausedByException();
				Set<ConstraintViolation<?>> violations = cve.getConstraintViolations();
				if(violations!=null)
					for(ConstraintViolation<?> cur : violations)
						out.println(cur.getMessage() + "<br/>");
				else
					out.println(cve.getMessage());
			}
		}
		finally{
			out.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
