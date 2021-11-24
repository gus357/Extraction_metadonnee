 package mimeMp3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
/**
 * Classe contenant les méthodes nécessaire à l'analyse de fichier et dossier ainsi que l'écriture de fichier xspf
 * @author gulsum et Emma
 */
public class FichierMp3 {
	private String artiste=null;
	private String titre=null;
	private String album=null;
	private String annee=null;
	private String genre=null;
	private String commentaire=null;
	private String duree=null;
	private byte[] image=null;
	private String chemin=null;

	/**
	 * Analyse fichier, renvoie métadonnées de fichier mp3 ou message d'erreur
	 * @author Emma
	 * @param fichier : on passe en parametre le fichier a analyser
	 * @return msg : String des métadonnées
	 * @throws UnsupportedTagException : probleme de tag
	 * @throws InvalidDataException : probleme de données
	 * @throws IOException : probleme d'autorisation
	 * @throws XMLStreamException : probleme de tratiment
	 */
	 public String analyseFichier(File fichier) throws UnsupportedTagException, InvalidDataException, IOException, XMLStreamException {
		String msg="";
		if(!fichier.exists()) {
			msg+= ("Le fichier n'existe pas!");
		}else if(estUnMp3(fichier)==1) {
			chemin=fichier.getAbsolutePath();
			Mp3File mp3file = new Mp3File(fichier);
			if (mp3file.hasId3v2Tag()) {
				com.mpatric.mp3agic.ID3v2 id3v2Tag = mp3file.getId3v2Tag();
				artiste=id3v2Tag.getArtist();
				titre=id3v2Tag.getTitle();
			    album=id3v2Tag.getAlbum();
				annee=id3v2Tag.getYear();
				genre=id3v2Tag.getGenre() + " (" + id3v2Tag.getGenreDescription() + ")";
				commentaire=id3v2Tag.getComment();
			    duree=Long.toString(mp3file.getLengthInSeconds());
				image=id3v2Tag.getAlbumImage();
				msg+= ("\nCe fichier possède des tags ID3v2:"
			    +"\n\tArtiste: " +artiste
			    +";\n\tTitre: "+titre
			    +";\n\tAlbum: "+album
			    +";\n\tAnnée: "+annee
				+";\n\tGenre: "+genre
				+";\n\tCommentaire: "+commentaire
			    +";\n\tDurée: "+duree+"secondes"
				+";\n\tImage:"+image);
			}
		}else {
			msg+= ("Ce n'est pas un fichier MP3!!");
		}
        return msg; 
	}
	
	
	/**
	 * Analyse de dossier et sous-dossier
	 * renvoie les métadonnées des fichiers mp3 inclue dans le dossier
	 * @author Gulsum
	 * @param directory : dossier à analyser 
	 * @return msg = String des métadonnées
	 * @throws UnsupportedTagException : probleme de tag
	 * @throws InvalidDataException : probleme de données
	 * @throws IOException : probleme d'autorisation
	 * @throws XMLStreamException : probleme de traitement
	 */
	public String analyseDossier(File directory) throws UnsupportedTagException, InvalidDataException, IOException, XMLStreamException {
		String msg = "";
		if(!directory.exists()) { 
			return("Le dossier '"+directory+"' n'existe pas\n"); 
		}
		else if(!directory.isDirectory()) {
				return("Le chemin '"+directory+"' correspond à un fichier et non à un répertoire,"
						+ "\n-Voici son analyse: "+analyseFichier(directory)+"\n"); 
		}
		else{  
			File[] subfiles = directory.listFiles(); 
			msg += ("Le répertoire '"+directory+"' contient "+ 
								subfiles.length+" fichier"+(subfiles.length>1?"s\n":"\n")); 
			for(int j=0 ; j<subfiles.length; j++){ 
				msg += (" "+subfiles[j].getAbsolutePath()+"\n");
				if(subfiles[j].isFile()) {
					File f = subfiles[j];
					msg += (" L'analyse de ce fichier: "+analyseFichier(f)+"\n");
				}
				else {
					msg += ("\nC'est un sous dossier: "+subfiles[j]+
							"\nEt voici son analyse: "+ analyseDossier(subfiles[j])+"\n");
				}
			}
		}
		return msg;
	}
	
	/**
	 * Enregistrement des fichier dans un ArrayList pour la rédaction du fichier .xspf
	 * @author Emma
	 * @param directory : dossier à analyser
	 * @param res : ArrayList des fichiers analyser 
	 * @throws UnsupportedTagException : probleme de tag
	 * @throws InvalidDataException : probleme de données
	 * @throws IOException : probleme d'autorisation 
	 * @throws XMLStreamException : probleme de traitement
	 */
	public void listeDossier(File directory,ArrayList<FichierMp3> res) throws UnsupportedTagException, InvalidDataException, IOException, XMLStreamException {
		String msg="";
		if(directory.exists() && directory.isDirectory()){  
			File[] subfiles = directory.listFiles(); 
			for(int j=0 ; j<subfiles.length; j++){ 
				if(subfiles[j].isFile()) {
				    File f = subfiles[j];
					FichierMp3 mp3 = new FichierMp3();
				    msg= mp3.analyseFichier(f);
					if (mp3.estUnMp3(f)==1) {
						res.add(mp3);
					}
				}
				else if(subfiles[j].isDirectory()) {
					listeDossier(subfiles[j],res);
				}
			}
		}
	}	
	
	/**
	 * Ecriture de fichier .xspf
	 * @author Gulsum Emma
	 * @param fileName : nom du fichier xspf
	 * @param rootElement : élément de création du fichier xspf
	 * @param songs : Arraylist des fichiers à inclure dans le fichier xspf
	 */
	public void writeXML(String fileName, String rootElement, ArrayList<FichierMp3> songs) {
		XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
		try{
			XMLStreamWriter w = xmlOutputFactory.createXMLStreamWriter(new FileOutputStream(fileName), "UTF-8");
			//start writing xml file
            w.writeStartDocument("UTF-8", "1.0");
            w.writeCharacters("\n");
            
            //write id as attribute
            w.writeStartElement(rootElement);
            w.writeAttribute("xmlns", "http://xspf.org/ns/0/");
            w.writeAttribute("version", "1");
                      
            w.writeCharacters("\n\t");
            w.writeStartElement("trackList");
            
            // write other elements   
            for (int i = 0; i < songs.size(); i++) {
            	w.writeCharacters("\n\t\t");
                w.writeStartElement("track");
                
                w.writeCharacters("\n\t\t\t");
                w.writeStartElement("location");
                w.writeCharacters("\t"+"file:\\"+ songs.get(i).getChemin());
                w.writeEndElement();
                
                w.writeCharacters("\n\t\t\t");
                w.writeStartElement("creator");
                w.writeCharacters("\t"+songs.get(i).getArtiste());
                w.writeEndElement();
                
                w.writeCharacters("\n\t\t\t");
                w.writeStartElement("title");
                w.writeCharacters("\t"+songs.get(i).getTitre());
                w.writeEndElement();
                
                w.writeCharacters("\n\t\t\t");
                w.writeStartElement("album");
                w.writeCharacters("\t"+songs.get(i).getAlbum());
                w.writeEndElement();
                
                w.writeCharacters("\n\t\t\t");
                w.writeStartElement("annotation");
                w.writeCharacters("\t"+songs.get(i).getCommentaire());
                w.writeEndElement();
               
                w.writeCharacters("\n\t\t\t");
                w.writeStartElement("duration");
                w.writeCharacters("\t"+songs.get(i).getDuree());
                w.writeEndElement();
                
                w.writeCharacters("\n\t\t");
                w.writeEndElement(); // fin track
            }
            
            w.writeCharacters("\n\t");
            w.writeEndElement(); // fin track list
            
            w.writeCharacters("\n");
            w.writeEndElement();
            w.writeEndDocument(); // fin playlist
            
            w.flush();
            w.close();
            
		}catch(XMLStreamException | FileNotFoundException e){
			e.printStackTrace();
		}
	}

	/**
	 * Vérifier si le type mime est correct
	 * @author Emma
	 * @param fichier : fichier à vérifier
	 * @return 0 si fichier inexistant, 1 si aucune erreur/format mp3 valide, 2 si erreur Entrée/Sortie, 3 si format mp3 incorrect
	 */
	public int estUnMp3(File fichier) {
		try {
			new Mp3File(fichier);
		}catch(FileNotFoundException e){
			return 0;
		} catch( IOException exception ) {             
	        return 2;
		} catch (Exception e){
			return 3;
		}
		return 1;
	}
	
	public String getChemin() {
		return chemin;
	}
	public String getArtiste() {
		return artiste;
	}
	public String getTitre() {
		return titre;
	}
	public String getAlbum() {
		return album;
	}
	public String getAnnee() {
		return annee;
	}
	public String getGenre() {
		return genre;
	}
	public String getCommentaire() {
		return commentaire;
	}
	public String getDuree() {
		return duree;
	}
	public byte[]getAlbumImage() {
		return image;
	}
}