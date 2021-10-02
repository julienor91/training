package com.sdzee.tp.pro.servlets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* *
 * L'annotation @WebServlet("/Download") d'origine est remplacé par @WebServlet("/download") 
 * car l'URL définie dans le fichier web.xml a été modifié. La servlet Download correspond à 
 * <url-pattern>/download</url-pattern> du fichier web.xml 
 * */
@WebServlet("/download")
public class Download extends HttpServlet {
	
	/* *
	 * ********** ATTRIBUTES **************************************************
	 * */
	private static final long serialVersionUID = 1L;
	
	private static final int DEFAULT_BUFFER_SIZE = 10240; // 10 ko
	public static final int TAILLE_TAMPON = 10240; // 10 ko
	
	/* *
	 * ********** CONSTRUCTOR **************************************************
	 * */
    public Download() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    /* *
	 * ********** HTTP METHODS **************************************************
	 * */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		/* *
		 * Le paramètre "chemin" est le nom du fichier xml que l'on a enregistré pour la servlet Download. 
		 * C'est la méthode getInitParameter() qui va récupérer le chemin d'accès (en URL absolu) nommé 
		 * "chemin", soit => /Users/julienorrado/Desktop/fichiers/
 		 * */
		String chemin = this.getServletConfig().getInitParameter("chemin");
		
		/* *
		 * Récupération du chemin du fichier demandé au sein de l'URL de la requête . La méthode getPathInfo() 
		 * retourne la fraction de l'URL qui correspond à ce qui est situé entre le chemin de base de la servlet et 
		 * les paramètres de requête.
		 * */
		String fichierRequis = request.getPathInfo();
		/* *
		 * Vérifie qu'un fichier a bien été fourni avec la méthode getPathInfo()
		 * */
		if (fichierRequis == null || "/".equals(fichierRequis)) {
		    /* *
		     * Si non, alors on envoie une erreur 404, qui signifie que la ressource demandée n'existe pas. 
		     * La méthode sendError() de l'objet HttpServletResponse permet de retourner au client les 
		     * messages et codes d'erreur HTTP souhaités.
		     * */
		    response.sendError(HttpServletResponse.SC_NOT_FOUND);
		    return;
		}
		
		/* *
		 * Décode le nom de fichier récupéré, susceptible de contenir des espaces et autres caractères spéciaux, 
		 * et prépare l'objet File 
		 * */
		fichierRequis = URLDecoder.decode(fichierRequis, "UTF-8");
		File fichier = new File(chemin, fichierRequis);
		        
		/* *
		 * Vérifie que le fichier existe bien 
		 * */
		if (!fichier.exists()) {
		    /* *
		     * Si non, alors on envoie une erreur 404, qui signifie que la ressource demandée n'existe pas 
		     * */
		    response.sendError(HttpServletResponse.SC_NOT_FOUND);
		    return;
		}
		
		/* *
		 * Récupère le type du fichier à l'aide de la méthode getMimeType(). Pour ce faire, il se base sur les types 
		 * MIME dont il a connaissance, en fonction de l'extension du fichier à retourner. Ces types sont spécifiés 
		 * dans le fichier web.xml global du conteneur, qui est situé dans le répertoire /conf/ du Tomcat Home.
		 * */
		String type = getServletContext().getMimeType(fichier.getName());

		/* *
		 * Si le type de fichier est inconnu, alors on initialise un type par défaut 
		 * */
		if (type == null) {
		    type = "application/octet-stream";
		}
		
		/* *
		 * Initialise la réponse HTTP 
		 * */
		response.reset(); // Efface littéralement l'intégralité du contenu de la réponse initiée par le conteneur
		response.setBufferSize(DEFAULT_BUFFER_SIZE); // Méthode à appeler impérativement après un reset()
		response.setContentType(type); // Spécifie le type des données contenues dans la réponse
		// nous retrouvons ensuite les deux en-têtes HTTP, qu'il faut construire "à la main" via des appels à setHeader()
		response.setHeader("Content-Length", String.valueOf(fichier.length()));
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fichier.getName() + "\"");
		
		/* *
		 * Lecture et envoi du fichier
		 * */
		/* *
		 * Prépare les flux 
		 * */
		BufferedInputStream entree = null;
		BufferedOutputStream sortie = null;
		try {
		    /* *
		     * Ouvre les flux 
		     * */
		    entree = new BufferedInputStream(new FileInputStream(fichier), TAILLE_TAMPON);
		    sortie = new BufferedOutputStream(response.getOutputStream(), TAILLE_TAMPON);
		 
		    /* ... */
		} finally {
		    try {
		        sortie.close();
		    } catch (IOException ignore) {
		    }
		    try {
		        entree.close();
		    } catch (IOException ignore) {
		    }
		}
		
		/* *
		 * Lit le fichier et écrit son contenu dans la réponse HTTP. À l'aide d'un tableau d'octets jouant le rôle de tampon, 
		 * la boucle mise en place parcourt le fichier et l'écrit, morceau par morceau, dans la réponse.
		 * */
		byte[] tampon = new byte[TAILLE_TAMPON];
		int longueur;
		while ((longueur= entree.read(tampon)) > 0) {
		    sortie.write(tampon, 0, longueur);
		}
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
}
