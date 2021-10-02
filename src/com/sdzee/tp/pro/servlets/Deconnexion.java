package com.sdzee.tp.pro.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/deconnexion")
public class Deconnexion extends HttpServlet {
	
	/* *
	 * ********** ATTRIBUTES **************************************************
	 * */
	private static final long serialVersionUID = 1L;
	
	/* 
	 * Demande au navigateur de renvoyer une requête vers la servlet Connexion ce qui aura pour effet de remetre de
	 * supprimer la session et ensuite de repartir sur l'URL /connexion. Dans le cas où l'on recharge la page alors 
	 * l'URL repartira de /connexion et non plus à partir de /deconnexion.
	 */
	public static final String URL_REDIRECTION = "http://localhost:8080/TP_File_rouge_pro_2/connexion";
	
	/*
	 * ********** CONSTRUCTOR **************************************************
	 */
    public Deconnexion() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    /*
	 * ********** HTTP SERVLET METHODS **************************************************
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/* Récupération et destruction de la session en cours */
        HttpSession session = request.getSession();
        session.invalidate();
        
        /* 
         * Redirection vers le Site du Zéro ! La méthode sendRedirect() renvoie au client l'url à demander. Le navigateur fera 
         * alors une requête au serveur concerné, ici mon localhost, pour demander la servlet Connexion puis redirigera la demande 
         * vers la page connexion.jsp. Tout cela sans que l'utilisateur ne s'aperçoive du chemin et des demandes.
         */
        response.sendRedirect(URL_REDIRECTION);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
}
