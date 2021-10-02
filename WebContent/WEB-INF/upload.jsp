<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
		<link type="text/css" rel="stylesheet" href="<c:url value="/css/form.css"/>" />
	</head>
	
	<body>
		<!-- Lorsque des données sont envoyées avec le type "multipart", les méthodes telles que 
		"request.getParameter()" retournent toutes "null" pour les <input type="file"... -->
		<form action="<c:url value="/upload" />" method="post" enctype="multipart/form-data">
			<fieldset>
                <legend>Envoi de fichier</legend>
				
                <label for="description">Description du fichier</label>
                <input type="text" id="description" name="description" value="<c:out value="${ fichier.description }"/>" />
                <span class="erreur"><c:out value="${ form.erreurs['description'] }" /></span>
                <br />
				
                <label for="fichier">Emplacement du fichier <span class="requis">*</span></label>
                <input type="file" id="fichier" name="fichier" value="<c:out value="${ fichier.nom }"/>" />
                <span class="erreur"><c:out value="${ form.erreurs['fichier'] }" /></span>
                <br />
                
                <input type="submit" value="Envoyer" class="sansLabel" />
                <br />                
                
                <p class="${empty form.erreurs ? 'succes' : 'erreur'}">${form.resultat}</p>
            </fieldset>
        </form>
	</body>
</html>