package TP1.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LineParser {

	public static void main(String[] args) throws IOException {

		System.out.println("Essaye du nombre de ligne(s) :");
		System.out.println("Le nombre est : " + nombreLignes("./essaye.java") + "\n");
		
		System.out.println("Essaye du nombre de commentaire(s) :"); 
		System.out.println("Le nombre est : " + nombreCommentaires("./essaye.java") + "\n");
		
		System.out.println("Essaye du nombre de ligne(s) dans un paquet :");
		System.out.println("Le nombre est : " + nombreLignesPaquets("./essayeJava") + "\n");

		System.out.println("Essaye du nombre de commentaire(s) dans un paquet : ");
		System.out.println("Le nombre est : " + nombreCommentairesPaquets("./essayeJava") + "\n");

		System.out.println("Essaye densité de commentaire(s) : ");
		System.out.println("Le nombre est : " + densiteCommentaires("./essaye.java") + "\n");

		System.out.println("Essaye densité de commentaire(s) dans un paquet : ");
		System.out.println("Le nombre est : " + densiteCommentairesPaquets("./essayeJava") + "\n");
	}

	
	/**
	 * classe_LOC : nombre de lignes de code d’une classe (sans compter les lignes vides)
	 * @param path
	 * @return int lignes
	 * @throws IOException
	 */
	public static int nombreLignes(String path) throws IOException {
		
		BufferedReader reader;
        int carac = 0, lignes = 0;
		Boolean hasChar = false;
 
        try {
            reader = new BufferedReader(new FileReader(path));

            while (carac >= 0) {
                carac = reader.read();
				if(carac != 10 && carac != 13 && carac != '\t' && carac != ' ' && carac != '/')
					hasChar = true;
				
				if(carac == '\n' && hasChar){
					lignes ++;
					hasChar = false;
				}
				else if(carac == '/' ) {
					carac = reader.read();
					if(carac == '/'){ // On est dans un commentaire mono-ligne
						while(carac != '\n' && carac >= 0) // On peut donc ignorer jusqu'à la fin de la ligne ou fin du fichier
							carac = reader.read();
						if(hasChar){
							lignes ++;
							hasChar = false;
						}
					}
					else if(carac == '*'){ // On est dans un commentaire multilignes
						while(true){
							carac = reader.read();
							if(carac == '*'){ // Début de */
								carac = reader.read();
								while(carac == '*') // Dans le cas où on a \***\ on évite les *
									carac = reader.read();
								if(carac == '/' || carac < 0) // Fin du commentaire multiligne
									break;
							}
							if(carac < 0) // Fin du fichier
								break;
						}
						// On peut supposer qu'on est sorti d'un com multiligne
						while(carac != '\n' && carac >= 0){
							carac = reader.read();
							if(carac != 13 && carac != 10 && carac != ' ' && carac != '\t')
								hasChar = true;
						}
						if(hasChar)
							lignes++;
						hasChar = false;
					}
				}
            } 
            reader.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
		return lignes;
	}


	
	/**
	 * paquet_LOC : nombre de lignes de code d’un paquet (java package) -- la somme des LOC de ses classes
	 * @param path
	 * @return int lignesPaquets
	 */
	public static int nombreLignesPaquets(String path) throws IOException {

		int lignesPaquets = 0; 

		try {
			ArrayList<File> list = new ArrayList<>();
			findAllFiles(list, path);

			for (File f : list) {
				if(f.toString().substring(f.toString().length()-4, f.toString().length()).equals("java"))	
					lignesPaquets += nombreLignes(f.toString());
			}
		}catch(IOException e){
			e.getMessage();
		}

		return lignesPaquets;		
	}

	/**
	 * Fonction recursive qui met dans la liste donnée en paramètre tout les paths des fichiers et directories depuis le path donné en paramètre
	 * @param list
	 * @param path
	 */
	public static void findAllFiles(ArrayList<File> list, String path){
		File directory = new File(path);
		if(directory.isFile())
			list.add(directory);
		else{
			File[] contents = directory.listFiles();
			for (File f : contents) {
				list.add(f);
				if(f.isDirectory())
					findAllFiles(list, f.toString());
			}
		}
		
	}



	/**
	 * classe_CLOC : nombre de lignes de code d’une classe qui contiennent des commentaires
	 * @param path
	 * @return int commentaires
	 * @throws IOException
	 */
	public static int nombreCommentaires(String path) throws IOException {

		BufferedReader reader;
        int carac = 0, commentaires = 0;
 
        try {
            reader = new BufferedReader(new FileReader(path));
            while (carac >= 0) {
                carac = reader.read(); // On prend le caractère du fichier
				if(carac == '/' ) {
					carac = reader.read();
					if(carac == '/') { // On est dans un commentaire mono-ligne
						commentaires ++;
						while(carac != '\n' && carac >= 0) // On peut donc ignorer jusqu'à la fin de la ligne ou fin du fichier
						carac = reader.read();
					}
					else if(carac == '*') { // On est dans un commentaire multilignes
						commentaires ++;
						while(true){
							carac = reader.read();
							if(carac == '\n') // On est dans une nouvelle ligne toujours dans un commentaire
								commentaires++;
							if(carac == '*') { // Début de */
								carac = reader.read();
								while(carac == '*') // Dans le cas où on a \***\ on évite les *
									carac = reader.read();
								if(carac == '/' || carac < 0) // Fin du commentaire multiligne
									break;
							}
							if(carac < 0) // Fin du fichier
								break;
						}
					}
				}
            } 
            reader.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
		return commentaires;
	}

	

	/**
	 * paquet_CLOC : nombre de lignes de code d’un paquet qui contiennent des commentaires
	 * @param path
	 * @return int commentairesPaquets
	 * @throws IOException
	 */
	public static int nombreCommentairesPaquets(String path) throws IOException {
	
		int commentairesPaquets = 0; 

		try{
			ArrayList<File> list = new ArrayList<>();
			findAllFiles(list, path);

			for (File f : list) {
				if(f.toString().substring(f.toString().length()-4, f.toString().length()).equals("java"))
					commentairesPaquets += nombreCommentaires(f.toString());
			}
		}catch(IOException e){
			e.getMessage();
		}

		return commentairesPaquets;	
	}
	
	
	/**
	 * paquet_DC : densité de commentaires pour une classe : classe_DC = classe_CLOC / classe_LOC
	 * @param path
	 * @return float
	 * @throws IOException
	 */
	public static float densiteCommentaires(String path) throws IOException {

		float commentaires = nombreCommentaires(path);
		float lignes = nombreLignes(path);
		return commentaires / lignes;
	}


	/**
	 * paquet_DC : densité de commentaires pour un paquet : paquet_DC = paquet_CLOC / paquet_LOC
	 * @param path
	 * @return float densiteCommentairesPaquets
	 * @throws IOException
	 */
	public static float densiteCommentairesPaquets(String path) throws IOException {

		float densiteCommentairesPaquets = 0; 
		int taille = 0;

		try{
			ArrayList<File> list = new ArrayList<>();
			findAllFiles(list, path);

			for (File f : list) {
				if(f.toString().substring(f.toString().length()-4, f.toString().length()).equals("java")){
					densiteCommentairesPaquets += densiteCommentaires(f.toString());
					taille++;
				}
				densiteCommentairesPaquets /= taille;
			}
		}catch(IOException e){
			e.getMessage();
		}

		return densiteCommentairesPaquets;	
	}

}