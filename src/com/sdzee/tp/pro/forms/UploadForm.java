package com.sdzee.tp.pro.forms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.sdzee.tp.pro.beans.Fichier;

/* *
 * Ajout de "final" car cette classe ne peut être héritée par une autre classe.
 * Cette classe est donc mise à part, elle ne subira aucune modification. C'est 
 * donc princpalement pour empêcher le développeur d'en faire une utilisation 
 * non appropriée dans une classe dérivée. Ainsi, outre la conception, une classe 
 * est également déclarée "final" dans un soucis d'optimisation du code. 
 * */
public final class UploadForm {
	
	/* *
	 * ********** ATTRIBUTES **************************************************
	 * */
    private static final String CHAMP_DESCRIPTION = "description";
    private static final String CHAMP_FICHIER = "fichier";
    private static final int TAILLE_TAMPON = 10240; // 10 ko
    
    private String resultat;
    private Map<String, String> erreurs = new HashMap<String, String>();
    
    /* *
	 * ********** METHODS **************************************************
	 * */
    public String getResultat() {
        return resultat;
    }
    
    public Map<String, String> getErreurs() {
        return erreurs;
    }
    
    public Fichier enregistrerFichier(HttpServletRequest request, String chemin) {
        /* *
         * Initialisation du bean représentant un fichier 
         * */
        Fichier fichier = new Fichier();
        
        /* *
         * Récupération du champ de description du formulaire 
         * */
        String description = getValeurChamp(request, CHAMP_DESCRIPTION);
        
        /* *
         * Récupération du contenu du champ fichier du formulaire. Il faut ici
         * utiliser la méthode getPart(), comme nous l'avions fait dans notre
         * servlet auparavant. La méthode getParts() avec un "s" est pour 
         * les collections.
         * */
        String nomFichier = null;
        InputStream contenuFichier = null;
        try {
        	/* *
    	     * Les données reçues sont multipart, on doit donc utiliser la méthode
    	     * getPart() pour traiter le champ d'envoi de fichiers.
    	     * Pour rendre disponible cette méthode dans une servlet, il faut compléter 
    	     * sa déclaration dans le fichier web.xml avec une section <multipart-config>
    	     * */
            Part part = request.getPart(CHAMP_FICHIER);
            /* *
             * Il faut déterminer s'il s'agit bien d'un champ de type fichier :
             * on délègue cette opération à la méthode utilitaire
             * getNomFichier().
             * */
            nomFichier = getNomFichier(part);

            /* *
             * Si la méthode a renvoyé quelque chose, il s'agit donc d'un
             * champ de type fichier (input type="file").
             * */
            if (nomFichier != null && !nomFichier.isEmpty()) {
            	/* ***** DEBUT DE LA LIGNE DE CODE NON OBLIGATOIRE 
            	 * POUR PALIER AU BUG DU NAVIGATEUR 
    	         * INTERNET-EXPLORER *****
                 * Antibug pour Internet Explorer, qui transmet pour une
                 * raison mystique le chemin du fichier local à la machine
                 * du client...
                 * 
                 * Ex : C:/dossier/sous-dossier/fichier.ext
                 * 
                 * On doit donc faire en sorte de ne sélectionner que le nom
                 * et l'extension du fichier, et de se débarrasser du
                 * superflu.
                 * */
                nomFichier = nomFichier.substring(nomFichier.lastIndexOf('/') + 1)
                        .substring( nomFichier.lastIndexOf('\\') + 1);
                /***** FIN DE LA LIGNE DE CODE NON OBLIGATOIRE POUR 
                 * PALIER AU BUG DU NAVIGATEUR 
    	         * INTERNET-EXPLORER ***** */

                /* Récupération du contenu du fichier */
                contenuFichier = part.getInputStream();
                
            }
        } catch (IllegalStateException e) {
            /* *
             * Exception retournée si la taille des données dépasse les limites
             * définies dans la section <multipart-config> de la déclaration de
             * notre servlet d'upload dans le fichier web.xml
             * */
            e.printStackTrace();
            setErreur(CHAMP_FICHIER, "Les données envoyées sont trop volumineuses.");
        } catch (IOException e) {
            /* *
             * Exception retournée si une erreur au niveau des répertoires de
             * stockage survient (répertoire inexistant, droits d'accès
             * insuffisants, etc.)
             * */
            e.printStackTrace();
            setErreur(CHAMP_FICHIER, "Erreur de configuration du serveur.");
        } catch (ServletException e) {
            /* *
             * Exception retournée si la requête n'est pas de type
             * multipart/form-data. Cela ne peut arriver que si l'utilisateur
             * essaie de contacter la servlet d'upload par un formulaire
             * différent de celui qu'on lui propose... attaque pirate ! :|
             * */
            e.printStackTrace();
            setErreur(CHAMP_FICHIER, 
                    "Ce type de requête n'est pas supporté, merci d'utiliser le formulaire prévu pour envoyer votre fichier." );
        }
        
        /* Si aucune erreur n'est survenue jusqu'à présent */
        if (erreurs.isEmpty()) {
            /* Validation du champ de description. */
            try {
                validationDescription(description);
            } catch (Exception e) {
                setErreur(CHAMP_DESCRIPTION, e.getMessage());
            }
            fichier.setDescription(description);
            
            /* Validation du champ fichier. */
            try {
                validationFichier(nomFichier, contenuFichier);
            } catch (Exception e) {
                setErreur(CHAMP_FICHIER, e.getMessage());
            }
            fichier.setNom(nomFichier);
        }
        
        /* Si aucune erreur n'est survenue jusqu'à présent */
        if (erreurs.isEmpty()) {
            /* Écriture du fichier sur le disque */
            try {
                ecrireFichier(contenuFichier, nomFichier, chemin);
            } catch (Exception e) {
                setErreur(CHAMP_FICHIER, "Erreur lors de l'écriture du fichier sur le disque.");
            }
        }
        
        /* Initialisation du résultat global de la validation. */
        if (erreurs.isEmpty()) {
            resultat = "Succès de l'envoi du fichier.";
        } else {
            resultat = "Échec de l'envoi du fichier.";
        }

        return fichier;
    }

    /*
     * Valide la description saisie.
     */
    private void validationDescription(String description) throws Exception {
        if (description != null) {
            if (description.length() < 15) {
                throw new Exception("La phrase de description du fichier doit contenir au moins 15 caractères.");
            }
        } else {
            throw new Exception("Merci d'entrer une phrase de description du fichier.");
        }
    }

    /*
     * Valide le fichier envoyé.
     */
    private void validationFichier(String nomFichier, InputStream contenuFichier) throws Exception {
        if (nomFichier == null || contenuFichier == null) {
            throw new Exception("Merci de sélectionner un fichier à envoyer.");
        }
    }

    /*
     * Ajoute un message correspondant au champ spécifié à la map des erreurs.
     */
    private void setErreur( String champ, String message ) {
        erreurs.put( champ, message );
    }

    /* *
     * Méthode utilitaire qui retourne null si un champ est vide, et son contenu sinon.
     * */
    private static String getValeurChamp(HttpServletRequest request, String nomChamp) {
        String valeur = request.getParameter(nomChamp);
        if (valeur == null || valeur.trim().length() == 0) {
            return null;
        } else {
            return valeur;
        }
    }

    /* *
     * Méthode utilitaire qui a pour unique but d'analyser l'en-tête
     * "content-disposition", et de vérifier si le paramètre "filename" y est
     * présent. Si oui, alors le champ traité est de type File et la méthode
     * retourne son nom, sinon il s'agit d'un champ de formulaire classique et
     * la méthode retourne null.
     * */
    private static String getNomFichier( Part part ) {
    	/* * 
	     * Boucle sur chacun des paramètres de l'en-tête "content-disposition". 
	     * Cela exclut tous les autres noms de paramètre du Header
	     * Exemple => 		// Pour un champ <input type="text"> nommé 'description'
		*							Content-Disposition: form-data; name="description"
		*
		*							// Pour un champ <input type="file"> nommé 'fichier'
		*							Content-Disposition: form-data; name="fichier"; filename="nom_du_fichier.ext"
	     * <input type="file"> permet donc d'avoir un paramètre "filename" et sa valeur.
	     * Donc soit il y a un paramètre "filename" et c'est bien un fichier, soit il n'y a pas de 
	     * paramètre "filename" et ce n'est pas un fichier.
	     * */
        for (String contentDisposition : part.getHeader("content-disposition").split(";")) {
            /* *
             * Recherche de l'éventuelle présence du paramètre "filename". 
             * */
            if (contentDisposition.trim().startsWith("filename")) {
                /* *
                 * Si "filename" est présent, alors renvoi de sa valeur qui est inscite après le "=" (paramètre="valeur"),
                 * c'est-à-dire du nom de fichier sans guillemets. Puis méthode pour remplacer les apostrophes.
                 * */
                return contentDisposition.substring(contentDisposition.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        /* Et pour terminer, si rien n'a été trouvé... */
        return null;
    }

    /*
     * Méthode utilitaire qui a pour but d'écrire le fichier passé en paramètre
     * sur le disque, dans le répertoire donné et avec le nom donné.
     */
    private void ecrireFichier(InputStream contenu, String nomFichier, String chemin) throws Exception {
        /* Prépare les flux. */
        BufferedInputStream entree = null;
        BufferedOutputStream sortie = null;
        try {
            /* Ouvre les flux. */
        	/* *
	    	 * Dans le flux entree, il nous suffit de récupérer le flux directement depuis la méthode getInputStream() 
	    	 * de l'objet Part. Nous décorons ensuite ce flux avec un BufferedInputStream, avec ici dans l'exemple un 
	    	 * tampon de 10 ko => ATTENTION A LA TAILLE DU FICHIER DE NE PAS DEPASSER !
	    	 * */
            entree = new BufferedInputStream(contenu, TAILLE_TAMPON);
            /* *
	    	 * Dans le flux sortie, nous devons mettre en place un fichier sur le disque, en vue d'y écrire ensuite le contenu 
	    	 * de l'entrée. Nous décorons ensuite ce flux avec un BufferedOutputStream, avec ici dans l'exemple un tampon 
	    	 * de 10 ko.
	    	 * */
            sortie = new BufferedOutputStream(new FileOutputStream(
            		new File(chemin + nomFichier)), TAILLE_TAMPON);

            /*
             * Lit le fichier reçu et écrit son contenu dans un fichier sur le
             * disque.
             */
            byte[] tampon = new byte[TAILLE_TAMPON];
            int longueur = 0;
            while ((longueur = entree.read(tampon)) > 0) {
                sortie.write(tampon, 0, longueur);
            }
            /* *
	         * Toujours ouvrir les flux dans un bloc try, et les fermer dans le bloc finally associé. Ainsi, nous nous assurons, 
	         * quoi qu'il arrive, que la fermeture de nos flux sera bien effectuée !
	         * */
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
    }
    
}
