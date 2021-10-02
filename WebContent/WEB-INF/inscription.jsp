<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
        <title>Inscription</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/css/form.css"/>" />
	</head>
    
    <body>
		<form method="post" action="<c:url value="/inscription" />">
            <fieldset>
                <legend>Inscription</legend>
                <p>Vous pouvez vous inscrire via ce formulaire.</p>

                <label for="email">Adresse email <span class="requis">*</span></label>
                <input type="text" id="userEmail" name="userEmail" value="<c:out value="${ utilisateur.userEmail }"/>" size="20" maxlength="60" />
                <span class="erreur">${ form.errors['userEmail'] }</span>
                <br />
				
                <label for="motdepasse">Mot de passe <span class="requis">*</span></label>
                <input type="password" id="userPassword" name="userPassword" value="<c:out value=""/>" size="20" maxlength="20" />
                <span class="erreur">${ form.errors['userPassword'] }</span>
                <br />
				
                <label for="confirmation">Confirmation du mot de passe <span class="requis">*</span></label>
                <input type="password" id="userPasswordConfirmation" name="userPasswordConfirmation" value="<c:out value=""/>" size="20" maxlength="20" />
<%--                 <span class="erreur">${ form.errors['userPasswordConfirmation'] }</span> --%>
                <br />
				
                <label for="nom">Nom d'utilisateur</label>
                <input type="text" id="username" name="username" value="<c:out value="${ utilisateur.username }"/>" size="20" maxlength="20" />
                <span class="erreur">${ form.errors['username'] }</span>
                <br />
				
                <input type="submit" value="Inscription" class="sansLabel" />
                <br />
                
                <p class="${ empty form.errors ? 'succes' : 'erreur' }">${ form.result }</p>
            </fieldset>
        </form>
    </body>
</html>