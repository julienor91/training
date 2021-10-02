package com.sdzee.tp.pro.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sdzee.tp.pro.beans.Fichier;
import com.sdzee.tp.pro.forms.UploadForm;

/* *
 * L'annotation @WebServlet("/Upload") d'origine est remplacé par @WebServlet("/upload") 
 * car l'URL définie dans le fichier web.xml a été modifié. La servlet Upload correspond à 
 * <url-pattern>/upload</url-pattern> du fichier web.xml 
 * */
@WebServlet("/upload")
public class Upload extends HttpServlet {
	
	/* *
	 * ********** ATTRIBUTES **************************************************
	 * */
	private static final long serialVersionUID = 1L;
	
	public static final String VIEW = "/WEB-INF/upload.jsp";
	public static final String ATT_FICHIER = "fichier";
	public static final String CHEMIN = "chemin";
	public static final String ATT_FORM = "form";
	public static final int TAILLE_TAMPON = 10240; // 10 ko
	
	/* *
	 * ********** CONSTRUCTOR **************************************************
	 * */
    public Upload() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    /* *
	 * ********** HTTP METHODS **************************************************
	 * */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		this.getServletContext().getRequestDispatcher(VIEW).forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		/* *
	     * Lecture du paramètre "chemin" passé à la servlet via la déclaration dans le web.xml
	     * <param-value>/Users/julienorrado/Desktop/</param-value>
	     * Soit => /Users/julienorrado/Desktop/
	     * */
	    String chemin = this.getServletConfig().getInitParameter(CHEMIN);
		
	    /* *
	     * Préparation de l'objet formulaire 
	     * */
        UploadForm form = new UploadForm();
        
        /* *
         * Traitement de la requête et récupération du bean en résultant 
         * */
        Fichier fichier = form.enregistrerFichier(request, chemin);
        
        /* *
         * Stockage du formulaire et du bean dans l'objet request 
         * */
        request.setAttribute(ATT_FORM, form);
        request.setAttribute(ATT_FICHIER, fichier);
        
        this.getServletContext().getRequestDispatcher(VIEW).forward( request, response );
	}
	
}
