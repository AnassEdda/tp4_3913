package TP1.src;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class FileTransform {

    public static void main(String[] args) throws Exception {

        try (Scanner pathScanner = new Scanner(System.in)) {
            System.out.print("Entrez le chemin d'accès du fichier ou du répertoire :\n");
            String path = pathScanner.nextLine();
            classes(path);
            paquets(path);
        }

    }

    /**
     * 
     * @param path
     * @throws Exception
     */
    public static void classes(String path) throws Exception {

        try {
            PrintWriter writer = new PrintWriter(new File("classes.csv"));
            StringBuilder line = new StringBuilder();

            String[] names = { "chemin", "classe", "classe_LOC", "classe_CLOC", "classe_DC", "WMC", "classe_BC" };

            for (int i = 0; i < names.length; i++) { // Pour mettre les noms dans la première ligne
                line.append(names[i]);
                if (i != names.length - 1) {
                    line.append(',');
                }
            }

            line.append("\n");

            ArrayList<File> list = new ArrayList<>();
            LineParser.findAllFiles(list, path);

            for (File f : list) {
                if (f.toString().substring(f.toString().length() - 4, f.toString().length()).equals("java")) {

                    String[] data = {
                            f.getAbsolutePath(),
                            f.getName(),
                            String.valueOf(LineParser.nombreLignes(f.toString())),
                            String.valueOf(LineParser.nombreCommentaires(f.toString())),
                            String.valueOf(LineParser.densiteCommentaires(f.toString())),
                            String.valueOf(CalculMetriques.classeBC(f.toString())),
                            String.valueOf(CalculMetriques.wmc(f.toString()))
                    };

                    for (int i = 0; i < data.length; i++) {
                        line.append(data[i]);
                        if (i != data.length - 1) {
                            line.append(',');
                        }
                    }

                    line.append("\n");
                }

            }
            writer.write(line.toString());
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

    }

    /**
     * 
     * @param path
     * @throws Exception
     */
    public static void paquets(String path) throws Exception {

        try {
            PrintWriter writer = new PrintWriter(new File("paquets.csv"));
            StringBuilder line = new StringBuilder();

            String[] names = { "chemin", "paquet", "paquet_LOC", "paquet_CLOC", "paquet_DC", "WCP", "paquet_BC" };

            for (int i = 0; i < names.length; i++) { // Pour mettre les noms dans la première ligne
                line.append(names[i]);
                if (i != names.length - 1) {
                    line.append(',');
                }
            }

            line.append("\n");

            ArrayList<File> list = new ArrayList<>();
            LineParser.findAllFiles(list, path);

            list.add(new File(path));

            for (File f : list) {

                if (f.isDirectory()) {

                    String[] data = {
                            f.getAbsolutePath(),
                            f.getName(),
                            String.valueOf(LineParser.nombreLignesPaquets(f.toString())),
                            String.valueOf(LineParser.nombreCommentairesPaquets(f.toString())),
                            String.valueOf(LineParser.densiteCommentairesPaquets(f.toString())),
                            String.valueOf(CalculMetriques.paquetBC(f.toString())),
                            String.valueOf(CalculMetriques.wcp(f.toString()))

                    };

                    for (int i = 0; i < data.length; i++) {
                        line.append(data[i]);
                        if (i != data.length - 1) {
                            line.append(',');
                        }
                    }

                    line.append("\n");
                }
            }
            writer.write(line.toString());
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

    }

}
