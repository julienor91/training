package com.sdzee.tp.pro.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.myProject.regularExpression.Regex;
import com.sdzee.tp.pro.beans.Utilisateur;

/* 
 * Ajout de "final" car cette classe ne peut être héritée par une autre classe.
 * Cette classe est donc mise à part, elle ne subira aucune modification. C'est 
 * donc princpalement pour empêcher le développeur d'en faire une utilisation 
 * non appropriée dans une classe dérivée. Ainsi, outre la conception, une classe 
 * est également déclarée "final" dans un soucis d'optimisation du code. 
 */
public final class InscriptionForm {
	
	/*
	 * ********** ATTRIBUTES **************************************************
	 */
	private static final String USER_EMAIL_FIELD = "userEmail";
	private static final String USER_PASSWORD_FIELD ="userPassword";
	private static final String USER_PASSWORD_CONFIRMATION_FIELD = "userPasswordConfirmation";
	private static final String USER_NAME_FIELD = "username";
	
	private static final Byte EMAIL_MAX_LETTER_NUMBER = 58;
	private static final Byte PASSWORD_MIN_LETTER_NUMBER = 5;
	private static final Byte PASSWORD_MAX_LETTER_NUMBER = 25;
	private static final Byte USERNAME_MAX_LETTER_NUMBER = 52;
	
	private static final String RESULT_SUCCESS_FIELD = "Succès de l'inscription.";
	private static final String RESULT_FAILED_FIELD = "Échec de l'inscription.";
	
	private String result;
	private Map<String, String> errors = new HashMap<String, String>();
	
	/*
	 * ********** CONSTRUCTOR **************************************************
	 */
	public InscriptionForm() {
		super();
	}
	
	/*
	 * ********** METHODS **************************************************
	 */
	public Utilisateur inscrireUtilisateur(HttpServletRequest request) {
		
		/* 
    	 * Récupération des champs du formulaire 
    	 */
	    String userEmail = getFieldValue(request, USER_EMAIL_FIELD);
	    String userPassword = getFieldValue(request, USER_PASSWORD_FIELD);
	    String userPasswordConfirmation = getFieldValue(request, USER_PASSWORD_CONFIRMATION_FIELD);
	    String username = getFieldValue(request, USER_NAME_FIELD);
	    
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
         * Validation des champs mot de passe et confirmation. 
         */
        try {
        	regex.passwordValidation(userPassword, userPasswordConfirmation, PASSWORD_MIN_LETTER_NUMBER, PASSWORD_MAX_LETTER_NUMBER);
        } catch (Exception e) {
        	setError(USER_PASSWORD_FIELD, e.getMessage());
        }
        
        /* 
         * Validation du champ nom. 
         */
        try {
        	regex.lastNameValidation(username, USERNAME_MAX_LETTER_NUMBER);
        } catch (Exception e) {
            setError(USER_NAME_FIELD, e.getMessage());
        }
        
        /* 
         * Création du bean en lui plaçant les paramètres. 
         */
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setUserEmail(userEmail);
        utilisateur.setUserPassword(userPassword);
        utilisateur.setUsername(username);
        
        /* 
         * Initialisation du résultat global de la validation. 
         */
        if (errors.isEmpty()) {
            result = RESULT_SUCCESS_FIELD;
        } else {
            result = RESULT_FAILED_FIELD;
        }
        
	    return utilisateur;
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
	        return valeur.trim();
	    }
	}
	
	/*
	 * ********** GETTER METHODS **************************************************
	* Méthode get pour permettre à la jsp de venir récupérer les données result et le tableau errors.
	*/
	public String getResult() {
		return result;
	}
	
	public Map<String, String> getErrors() {
		return errors;
	}
	
}
