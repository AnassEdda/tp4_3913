package TP1.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CalculMetriques {
    public static void main(String[] args) throws Exception {

        System.out.println("Essaye de la complexite d'une classe :");
        System.out.println("Le nombre est : " + wmc("./essaye.java") + "\n");

        System.out.println("Essaye de la complexite d'un paquet :");
        System.out.println("Le nombre est : " + wcp("./essayeJava") + "\n");
    }

    /**
     * Weighted Methods per Class
     * 
     * @param path
     * @return int complexites
     * @throws Exception
     */
    public static int wmc(String path) throws Exception {

        // On utlise la deuxième méthode si pas de switch, c'est à dire prédicats + 1

        File javaClass = new File(path);
        int complexites = 1;

        try (BufferedReader reader = new BufferedReader(new FileReader(javaClass))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                if (ligne.contains("if"))
                    complexites++;
            }
        }
        return complexites;
    }

    /**
     * Weighted Classes per Package
     * 
     * @param path
     * @return float
     * @throws Exception
     */
    public static float wcp(String path) throws Exception {

        int complexites = 0;
        int nombreFichiers = 0;
        try {
            File directory = new File(path);
            File[] contents = directory.listFiles();

            for (File f : contents) {
                nombreFichiers++;
                if (f.toString().substring(f.toString().length() - 4, f.toString().length()).equals("java"))
                    complexites += wmc(f.toString());
            }
        } catch (IOException e) {
            e.getMessage();
        }
        return complexites / nombreFichiers;
    }

    public static float classeBC(String path) throws Exception {
        return LineParser.densiteCommentaires(path) / wmc(path);

    }

    public static float paquetBC(String path) throws Exception {
        return LineParser.densiteCommentairesPaquets(path) / wcp(path);

    }

}
