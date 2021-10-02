package com.sdzee.tp.pro.beans;

public class Fichier {
	
	/* *
	 * ********** ATTRIBUTES **************************************************
	 * */
	private String description;
	private String nom;
	
	/* *
	 * ********** CONSTRUCTORS **************************************************
	 * */
	public Fichier() {
		super();
	}

	public Fichier(String description, String nom) {
		super();
		this.description = description;
		this.nom = nom;
	}

	/* *
	 * ********** GETTER METHODS **************************************************
	 * */
	public String getDescription() {
		return description;
	}

	public String getNom() {
		return nom;
	}

	/* *
	 * ********** SETTER METHODS **************************************************
	 * */
	public void setDescription(String description) {
		this.description = description;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	/* *
	 * ********** TOSTRING METHOD **************************************************
	 * */
	@Override
	public String toString() {
		return "Fichier [description=" + description + ", nom=" + nom + "]";
	}
	
}
