package mimeMp3;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.stream.XMLStreamException;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

import mimeMp3.FichierMp3;

/**
 * Classe qui prend en argument les fichiers et dossier à analyser ainsi que le nom du fichier dans lequel sauvegarder l'analyse
 * appelle des méthode de la classe FichierMp3.class
 * @author Emma et Gulsum
 */
public class TestFichierMp3 {
	private static String save = null;
	/**
	 * Vérifie s'il y a des arguments
	 * Si moins de 2 arguments: message d'erreur/d'aide
	 * Si -f retourne l'analyse du fichier
	 * Si -d retourne l'analyse du dossier
	 * Si -o créer un fichier .xspf
	 * Sinon: message d'erreur/d'aide
	 * @param args : fichier/dossier à analyser et nom du fichier de sauvegarde
	 * @throws UnsupportedTagException : probleme de tag
	 * @throws InvalidDataException : probleme de données
	 * @throws IOException : probleme d'autorisation
	 * @throws XMLStreamException : probleme de traitement 
	 */
	public static void main(String[] args) throws UnsupportedTagException, InvalidDataException, IOException, XMLStreamException {
		FichierMp3 mp3 = new FichierMp3();
		ArrayList<FichierMp3> listeMp3 = new ArrayList<FichierMp3>();
		String msg = "";
		if (args.length <2) {
			System.out.println("Ce programme permet d'analyser des fichiers et dossiers. Utilisez:"
					+"\n'-f' pour analyser un fichier,"
					+"\n'-d' pour analyser un dossier,"
					+"\n'-o' pour générer une playlist.\n");
		}
		else {
			for (int i = 0; i<args.length; i++) {
				if(args[i].equals("-f")) {
					msg += ("\n***Il y a un fichier: "+args[i+1]+"***");
					File f = new File(args[i+1]);
					msg+= (mp3.analyseFichier(f));
					listeMp3.add(mp3);
					i++;
				}
				else if(args[i].equals("-d")) {
					msg += ("\n***Il y a un dossier: "+args[i+1]+"***\n");
					File directory = new File(args[i+1]); 
					msg += (mp3.analyseDossier(directory));
					mp3.listeDossier(directory,listeMp3);
					i++;
				}
				else if(args[i].equals("-o")) {
					msg += ("\n***La Playlist a été générée dans: "+args[i+1]);
					save = args[i+1];
					if(save!=null) {
						mp3.writeXML(save,"playlist", listeMp3);
					}
				}
				else {
					System.out.println("Ce programme permet d'analyser des fichiers et dossiers. Utilisez:"
							+"\n'-f' pour analyser un fichier,"
							+"\n'-d' pour analyser un dossier,"
							+"\n'-o' pour générer une playlist.\n");
				}
			}
			System.out.println(msg.toString());
		}
	}
}
