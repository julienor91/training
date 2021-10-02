<!DOCTYPE html>
<html lang="fr">
	<head>
		<meta charset="UTF-8" />
        <title>Connexion</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/css/form.css"/>"/>
	</head>
	
    <body>
    	<%-- <c:url ajoute automatiquement le contexte de l'application aux URL absolues qu'elle contient 
    	Elle renvoie l'URL entière et non plus une partie de l'URL notament juste la partie de l'application 
    	Le navigateur reconnaîtra ici une URL absolue et non plus une URL relative comme c'était le cas 
    	auparavant, et il réalisera correctement l'appel au fichier CSS ! --%>
        <form method="post" action="<c:url value="/connexion" />">
            <fieldset>
                <legend>Connexion</legend>
                <p>Vous pouvez vous connecter via ce formulaire.</p>
				
				<c:if test="${ empty sessionScope.sessionUtilisateur && !empty requestScope.intervalleConnexions }">
                	<p class="info">(Vous ne vous êtes pas connecté(e) depuis ce navigateur depuis ${requestScope.intervalleConnexions})</p>
                </c:if>
                
                <label for="userEmail">Adresse email <span class="requis">*</span></label>
                <input type="email" id="userEmail" name="userEmail" value="<c:out value="${ user.userEmail }"/>" size="20" maxlength="60" />
                <span class="erreur">${ form.errors['userEmail'] }</span>
                <br />
				
                <label for="userPassword">Mot de passe <span class="requis">*</span></label>
                <input type="password" id="userPassword" name="userPassword" value="<c:out value=""/>" size="20" maxlength="20" />
                <span class="erreur">${ form.errors['userPassword'] }</span>
                <br />
                
                <br />
                <label for="memoire">Se souvenir de moi</label>
                <input type="checkbox" id="memoire" name="memoire" />
                <br />
				
                <input type="submit" value="Connexion" class="sansLabel" />
                <br />
                
                <%-- Vérification de la présence d'un objet utilisateur en session --%>
                <c:if test="${ !empty sessionScope.sessionUtilisateur }">
                    <%-- Si l'utilisateur existe en session, alors on affiche son adresse email. --%>
                    <p class="succes">Vous êtes connecté(e) avec l'adresse : ${ sessionScope.sessionUtilisateur.userEmail }</p>
                </c:if>
                <p class="${ empty form.errors ? 'succes' : 'erreur' }">${ form.result }</p>
            </fieldset>
        </form>
    </body>
</html>