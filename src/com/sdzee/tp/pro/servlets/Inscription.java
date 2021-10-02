package com.sdzee.tp.pro.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sdzee.tp.pro.beans.Utilisateur;
import com.sdzee.tp.pro.forms.InscriptionForm;

/* 
 * L'annotation @WebServlet("/Inscription") d'origine est remplacé par @WebServlet("/inscription") 
 * car l'URL définie dans le fichier web.xml a été modifié. La servlet Inscription correspond à 
 * <url-pattern>/inscription</url-pattern> du fichier web.xml 
 */
@WebServlet("/inscription")
public class Inscription extends HttpServlet {
	
	/* 
	 * ********** ATTRIBUTES **************************************************
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String ATTRIBUTE_USER = "utilisateur";
    private static final String ATTRIBUTE_FORM = "form";
	private static final String VIEW = "/WEB-INF/inscription.jsp";
	
	/*
	 * ********** CONSTRUCTOR **************************************************
	 */
    public Inscription() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	/*
	 * ********** HTTP SERVLET METHODS **************************************************
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		this.getServletContext().getRequestDispatcher(VIEW).forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		/* 
		 * Cette méthode permet de mettre toute la requête entrante en UTF-8. 
		 * Elle est remplacer par le code <filter> et <filter-mapping> dans le web.xml
		 */
		// request.setCharacterEncoding("UTF-8");
		
		InscriptionForm form = new InscriptionForm();
		Utilisateur user = new Utilisateur();
		
		/* 
		 * Validation des données. 
		 */
		user = form.inscrireUtilisateur(request);
		
		request.setAttribute(ATTRIBUTE_USER, user);
		request.setAttribute(ATTRIBUTE_FORM, form);
		
		this.getServletContext().getRequestDispatcher(VIEW).forward(request, response);
	}
	
}
