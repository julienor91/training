package com.sdzee.tp.pro.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/* *
 * L'annotation @WebServlet("/Restriction") d'origine est remplacée par @WebServlet("/restriction") 
 * car l'URL définie dans le fichier web.xml a été modifiée. La servlet Restriction correspond à 
 * <url-pattern>/restriction</url-pattern> du fichier web.xml 
 * */
@WebServlet("/restriction")
public class Restriction extends HttpServlet {
	
	/* 
	 * ********** ATTRIBUTES **************************************************
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String ACCES_PUBLIC = "/accesPublic.jsp";
    public static final String ACCES_RESTREINT = "/restreint/accesRestreint.jsp";
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
	
    public Restriction() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    /*
     * REMPLACE PAR LA CLASSE RESTRICTIONFILTER (QUI IMPLEMENTE FILTER) 
     * Cette servlet n'a donc plus d'utilité dans l'application
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		/* 
		 * Récupération de la session depuis la requête 
		 */
        HttpSession session = request.getSession();
        
        /*
         * Si l'objet utilisateur n'existe pas dans la session en cours, alors l'utilisateur n'est pas connecté.
         */
        if (session.getAttribute(ATT_SESSION_USER) == null) {
            /* 
             * Redirection vers la page publique avec la méthode sendRedirect(). C'est donc le client qui devra refaire 
             * la demande au serveur en question via une requête HTTP.
             * En paramètre => request.getContextPath() + ACCES_PUBLIC
             * Equivaut à => /TP_File_rouge_pro_2 + /accesPublic.jsp
             * Equivaut à => /TP_File_rouge_pro_2/accesPublic.jsp
             * Contrairement au forwarding avec la méthode forward(request, response), la méthode sendRedirect() 
             * doit entrer en paramètre une URL de redirection relative à la RACINE DE L'APPLICATION. Il faut donc 
             * indiquer l'URL en entier. A l'inverse, il est impossible pour le forwarding de mettre en paramètre une URL 
             * extérieure au projet de l'application, on sait que le paramètre de l'URL commencera à partir du WebContent. 
             * Hors avec la méthode sendRedirect c'est possible, son paramètre doit obligatoirement être inscrit de manière 
             * relative pour être lisible d'une manière externe à l'application. On utilise le paramètre "request.getContextPath()" 
             * car si au cours du développement de l'application on serait amené à changer le nom du projet, on n'aurait pas 
             * besoin de rechercher tous les paramètres qui utiliseraient son nom dans les directions par exemple. Avec 
             * request.getContextPath() cela se ferait automatiquement.
             */
            response.sendRedirect(request.getContextPath() + ACCES_PUBLIC);
        } else {
            /* 
             * Affichage de la page restreinte 
             */
            this.getServletContext().getRequestDispatcher(ACCES_RESTREINT).forward( request, response );
        }
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
}
