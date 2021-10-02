package com.sdzee.tp.pro.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*
 * Cette classe filtre RestrictionFilter remplace la servlet Restriction.
 */
public class RestrictionFilter implements Filter {
	
	/*
	 * ********** ATTRIBUTES **************************************************
	 */
	public static final String ACCESS_CONNECTION = "/WEB-INF/connexion.jsp";
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
	
	/* 
	 * Cette méthode s'active lors de l'initialisation de la classe filtre RestrictionFilter. Cette classe 
	 * sera intanciée une seul fois et réutilisée pour chaque requête. Donc la méthode init() sera 
	 * utilisée lors de l'instanciation de la classe.
	 */
	public void init(FilterConfig config) throws ServletException {
		
    }
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) 
			throws IOException, ServletException {
		
		/* 
		 * Cast des objets req (request) et res (response) car la classe RestrictionFilter implemente Filter et non 
		 * pas une interface supposée HttpFilterRequest qui n'existe pas. Une classe servlet n'implémente pas 
		 * directement l'interface Servlet mais HttpServletRequest qui hérite de Servlet. Donc On doit caster les 
		 * requêtes pour que celles-ci s'occupent uniquement des requêtes de type http. Filter est trop générique.
		 */
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
        /* Récupération de la session depuis la requête request et non la requête req. C'est seulement en procédant 
         * à la conversion en HttpServletRequest que nous aurons ensuite accès à la session, qui est propre à l'objet 
         * HttpServletRequest, et qui n'existe pas dans l'objet ServletRequest.
         */
        HttpSession session = request.getSession();
        
        /* 
         * Non-filtrage des ressources statiques. C'est à dire de laisser passer les fichiers css ou js lorsque le navigateur 
         * les demandes par une requête annexe après avoir reçu le fichier html. Il faut récuprer l'URL d'appel de la requête 
         * HTTP via la méthode getRequestURI(), puis nous plaçons dans la chaîne chemin sa partie finale, c'est-à-dire la partie 
         * située après le contexte de l'application.
         */
        String chemin = request.getRequestURI().substring(request.getContextPath().length()); // Toute la longueur du context est soustrait à la chaîne de caractères de l'URL
        if (chemin.startsWith("/css")) { // Si la chaîne de caractère commence pas /css alors on ne lui applique pas le filtre. 
            chain.doFilter(request, response); // La requête poursuit son chemin en appelant la méthode doFilter() et elle n'est pas stoppée.
            return;
        }
        
        /*
         * Si l'objet utilisateur n'existe pas dans la session en cours, alors
         * l'utilisateur n'est pas connecté.
         */
        if (session.getAttribute(ATT_SESSION_USER) == null) {
            /* Redirection vers la page publique */
        	request.getRequestDispatcher(ACCESS_CONNECTION).forward(request, response);
        } else {
            /* 
             * Affichage de la page restreinte ou bien vers le filtre suivant qui sera mis dans le 
             * fichier web.xml 
             */
            chain.doFilter(request, response);
        }
	}
	
	public void destroy() {
		
    }
	
}
