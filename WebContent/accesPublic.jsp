<!-- 
Ceci est pour empêcher la création d'un objet session ou son utilisation dans toute la jsp lorsqu'elle est retransformé en servlet. 
Cela permet de gagner en performance ainsi qu'en espace mémoire disponible. Attention, à utiliser uniquement avec ce type de jsp.
Il ne faut donc pas inscrire cela dans le taglibs.
-->
<%@ page session="false" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Accès public</title>
	</head>
	
	<body>
		<p>Vous n'avez pas accès à l'espace restreint : vous devez vous <a href="connexion">connecter</a> d'abord. </p>
	</body>
</html>