package com.sdzee.tp.pro.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.myProject.regularExpression.Regex;
import com.sdzee.tp.pro.beans.Utilisateur;

public final class ConnexionForm {
	
	/*
	 * ********** ATTRIBUTES **************************************************
	 */
	private static final String USER_EMAIL_FIELD = "userEmail";
	private static final String USER_PASSWORD_FIELD ="userPassword";
	
	private static final Byte EMAIL_MAX_LETTER_NUMBER = 58;
	
	private static final String RESULT_SUCCESS_FIELD = "Succès de la connexion.";
	private static final String RESULT_FAILED_FIELD = "Échec de la connexion.";
	
    private String result;
    private Map<String, String> errors = new HashMap<String, String>();
    
    /*
	 * ********** CONSTRUCTOR **************************************************
	 */
    public ConnexionForm() {
    	super();
    }

    /*
	 * ********** METHODS **************************************************
	 */
    public Utilisateur connecterUtilisateur(HttpServletRequest request) {
        
    	/* 
    	 * Récupération des champs du formulaire 
    	 */
        String userEmail = getFieldValue(request, USER_EMAIL_FIELD);
        String userPassword = getFieldValue(request, USER_PASSWORD_FIELD);
        
        Regex regex = new Regex();
        
        /* 
	     * Validation du champ email. 
	     */
        try {
        	regex.mailingAddressValidation(userEmail, EMAIL_MAX_LETTER_NUMBER);
        } catch (Exception e) {
        	setError(USER_EMAIL_FIELD, e.getMessage());
        }
       
        /* 
         * Validation du champ mot de passe sans utliser la classe Regex.
         */
        try {
            validationMotDePasse(userPassword);
        } catch ( Exception e ) {
            setError(USER_PASSWORD_FIELD, e.getMessage() );
        }
        
        /* 
         * Création du bean en lui plaçant les paramètres. 
         */
        Utilisateur user = new Utilisateur();
        user.setUserEmail(userEmail);
        user.setUserPassword(userPassword);
        
        /* 
         * Initialisation du résultat global de la validation. 
         */
        if (errors.isEmpty()) {
            result = RESULT_SUCCESS_FIELD;
        } else {
            result = RESULT_FAILED_FIELD;
        }
        
        return user;
    }
    
    /**
     * Valide le mot de passe saisi car pour le moment on ne possède pas de BDD.
     */
    private void validationMotDePasse(String motDePasse) throws Exception {
        if (motDePasse != null) {
            if (motDePasse.length() < 3) {
                throw new Exception( "Le mot de passe doit contenir au moins 3 caractères." );
            }
        } else {
            throw new Exception( "Merci de saisir votre mot de passe." );
        }
    }
    
    /*
     * Ajoute un message correspondant au champ spécifié à la map des erreurs.
     */
    private void setError(String champ, String message) {
        errors.put(champ, message);
    }
    
    /*
     * Méthode utilitaire qui retourne null si un champ est vide, et son contenu sinon.
     */
    private static String getFieldValue(HttpServletRequest request, String nomChamp) {
        String valeur = request.getParameter(nomChamp);
        if (valeur == null || valeur.trim().length() == 0) {
            return null;
        } else {
            return valeur;
        }
    }
    
    /*
	 * ********** GETTER METHODS **************************************************
	*/
    public String getResult() {
        return result;
    }
    
    public Map<String, String> getErrors() {
        return errors;
    }
    
}