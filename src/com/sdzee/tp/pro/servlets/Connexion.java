package com.sdzee.tp.pro.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.sdzee.tp.pro.beans.Utilisateur;
import com.sdzee.tp.pro.forms.ConnexionForm;

/* *
 * L'annotation @WebServlet("/Connexion") d'origine est remplacé par @WebServlet("/connexion") 
 * car l'URL définie dans le fichier web.xml a été modifié. La servlet Connexion correspond à 
 * <url-pattern>/connexion</url-pattern> du fichier web.xml 
 * */
@WebServlet("/connexion")
public class Connexion extends HttpServlet {
	
	/* *
	 * ********** ATTRIBUTES **************************************************
	 * */
	private static final long serialVersionUID = 1L;
	
    public static final String ATTRIBUTE_USER = "user";
    public static final String ATTRIBUTE_FORM = "form";
    public static final String ATTRIBUTE_SESSION_USER = "sessionUtilisateur";
    public static final String VIEW = "/WEB-INF/connexion.jsp";
    
    public static final String ATT_INTERVALLE_CONNEXIONS = "intervalleConnexions";
    public static final String COOKIE_DERNIERE_CONNEXION = "derniereConnexion";
    public static final String FORMAT_DATE = "dd/MM/yyyy HH:mm:ss";
    
    public static final String CHAMP_MEMOIRE = "memoire";
    public static final int COOKIE_MAX_AGE = 60 * 60 * 24 * 365;  // 1 an
    
    /* *
	 * ********** CONSTRUCTOR **************************************************
	 * */
    public Connexion() {
    	super();
    }
    
    /* *
	 * ********** HTTP SERVLET METHODS **************************************************
	 * */
    public void doGet(HttpServletRequest request, HttpServletResponse response) 
    		throws ServletException, IOException {
    	
    	/* *
    	 * Tentative de récupération du cookie depuis la requête 
    	 * */
        String derniereConnexion = getCookieValue(request, COOKIE_DERNIERE_CONNEXION);
        /* *
         * Si le cookie existe, alors calcul de la durée 
         * */
        if (derniereConnexion != null) {
            /* *
             * Récupération de la date courante 
             * */
            DateTime dtCourante = new DateTime();
            /* *
             * Récupération de la date présente dans le cookie 
             * */
            DateTimeFormatter formatter = DateTimeFormat.forPattern(FORMAT_DATE);
            DateTime dtDerniereConnexion = formatter.parseDateTime(derniereConnexion);
            /* *
             * Calcul de la durée de l'intervalle 
             * */
            Period periode = new Period(dtDerniereConnexion, dtCourante);
            /* *
             * Formatage de la durée de l'intervalle 
             * */
            PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
                    .appendYears().appendSuffix( " an ", " ans " )
                    .appendMonths().appendSuffix( " mois " )
                    .appendDays().appendSuffix( " jour ", " jours " )
                    .appendHours().appendSuffix( " heure ", " heures " )
                    .appendMinutes().appendSuffix( " minute ", " minutes " )
                    .appendSeparator( "et " )
                    .appendSeconds().appendSuffix( " seconde", " secondes" )
                    .toFormatter();
            String intervalleConnexions = periodFormatter.print(periode);
            /* *
             * Ajout de l'intervalle en tant qu'attribut de la requête 
             * */
            request.setAttribute(ATT_INTERVALLE_CONNEXIONS, intervalleConnexions);
        }
    	/* *
    	 * Affichage de la page de connexion 
    	 * */
        this.getServletContext().getRequestDispatcher(VIEW).forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) 
    		throws ServletException, IOException {
    	
        /* *
         * Préparation de l'objet formulaire.
         * */
        ConnexionForm form = new ConnexionForm();

        /* *
         * Traitement de la requête et récupération du bean en résultant.
         * */
        Utilisateur user = form.connecterUtilisateur(request);

        /* *
         * Récupération de la session depuis la requête.
         * */
        HttpSession session = request.getSession();

        /* *
         * Si aucune erreur de validation n'a eu lieu, alors ajout du bean
         * Utilisateur à la session, sinon suppression du bean de la session.
         * */
        if (form.getErrors().isEmpty()) {
            session.setAttribute(ATTRIBUTE_SESSION_USER, user);
        } else {
            session.setAttribute(ATTRIBUTE_SESSION_USER, null);
        }
        
        /* *
         * Stockage du formulaire et du bean dans l'objet request 
         * */
        request.setAttribute(ATTRIBUTE_USER, user);
        request.setAttribute(ATTRIBUTE_FORM, form);
        
        /* *
         * Si et seulement si la case du formulaire est cochée 
         * */
        if (request.getParameter(CHAMP_MEMOIRE) != null) {
            /* *
             * Récupération de la date courante 
             * */
            DateTime dt = new DateTime();
            /* *
             * Formatage de la date et conversion en texte 
             * */
            DateTimeFormatter formatter = DateTimeFormat.forPattern(FORMAT_DATE);
            String dateDerniereConnexion = dt.toString(formatter);
            /* *
             * Création du cookie, et ajout à la réponse HTTP 
             * */
            setCookie(response, COOKIE_DERNIERE_CONNEXION, dateDerniereConnexion, COOKIE_MAX_AGE);
        } else {
            /* *
             * Demande de suppression du cookie du navigateur avec une période de 0 secondes. 
             * zéro, qui permet de supprimer simplement le cookie du navigateur.
             * */
            setCookie(response, COOKIE_DERNIERE_CONNEXION, "", 0);
        }
        
        this.getServletContext().getRequestDispatcher(VIEW).forward(request, response);
    }
    
    /* *
     * Méthode utilitaire gérant la récupération de la valeur d'un cookie donné depuis la requête HTTP.
     * */
    private static String getCookieValue(HttpServletRequest request, String nom) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie != null && nom.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    
    /* *
     * Méthode utilitaire gérant la création d'un cookie et son ajout à la réponse HTTP.
     * */
    private static void setCookie(HttpServletResponse response, String nom, String valeur, int maxAge) {
        Cookie cookie = new Cookie(nom, valeur);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
    
}
