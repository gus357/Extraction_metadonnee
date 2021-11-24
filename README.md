# Extraction_metadonnee

##############################
  Membre du Groupe E12:

	Emma Saint Hubert
	Gulsum YUKSEL

##############################

Fonctionnalité du programme gui:
	gui.jar:
	  -Ouvrir -> Ouvre un fichier (si c'est un dossier, ouvre aussi tout les sous-dossiers)
	  -Test -> Appelle à la classe d'Analyse
	  -Reset -> Réinitialise l'affichage et vide la liste
	  -Quitter -> Quitte le programme

À noter:
	-Le programme va vérifier un par un si les fichiers ouvert sont des fichiers mp3.
	-Si le type mime ne correspond pas, le programme renvoie un message d'erreur
	-Sinon il renvoie les métadonnées des fichiers
	-Le programme créer un fichier GUIPlaylist.xspf lorsqu'on quitte la JFrame
	-Si le fichier n'existe pas, le programme en crée un nouveau.

#############################

Fonctionnalité du programme cli:
	cli.jar:
	  -Utiliser les paramètres suivant en ligne d'arguments:
		-f [chemin vers un fichier] pour analyser un fichier
		-d [chemin vers un dossier] pour analyser un dossier
		-o [chemin vers un fichier] pour indiquer où sauvegarder l'analyse des fichiers

#############################

Dossier images:
	-Contient quelque fichier permettant de tester le programme
	-Ce dossier contient un sous-dossier ainsi que des fichiers trompeurs

#############################
