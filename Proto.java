import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
  
public class Proto {

    public static void main(String[] args) throws IOException {

        System.out.println("--Début de la TACHE 1--");
        try (Scanner entree1 = new Scanner(System.in)) {
            System.out.println("Entrer le path :");
            String path = entree1.nextLine(); 
            // path = "https://github.com/jfree/jfreechart"; 

            try (Scanner entree2 = new Scanner(System.in)) {
                System.out.println("Entrer le dossier :");
                String dossier = entree2.nextLine(); 
                // dossier = "jfreechart"; 
                
                ArrayList<String> id = Proto.version(path, dossier);
                System.out.println("Sous tâche 1 -done ");

                ArrayList<Integer> nc = Proto.nmbClasses(dossier, id);
                System.out.println("Sous tâche 2  - done");

                Proto.csv(id, nc);
                System.out.println("Sous tâche 3 - done");
            }
        }
    } 

    // Pour prendre les ID-version
    public static ArrayList<String> version(String path, String dossier) throws IOException {
        
        ProcessBuilder processBuilder = new ProcessBuilder();
        List<String> builderList = new ArrayList<>();
        ArrayList<String> id = new ArrayList<>();
  
        // add the list of commands to a list
        builderList.add("cmd.exe");
        builderList.add("/C");
        builderList.add("git clone " + path + " && cd " + dossier + " && git rev-list --all");
  
        try {
            // Using the list , trigger the command
            processBuilder.command(builderList);
            Process process = processBuilder.start();
  
            // To read the output list
            BufferedReader reader  = new BufferedReader(new InputStreamReader(process.getInputStream()));
  
            String line;
            while ((line = reader.readLine()) != null)
                id.add(line);
            
            int exitCode = process.waitFor();
            System.out.println("\nErreur (pas normal si != 0) : " + exitCode);
            
            return id;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return id;
    }

    // Pour prendre les informations en fonction de l'ID
    public static ArrayList<Integer> nmbClasses(String dossier, ArrayList<String> id) throws IOException{

        ArrayList<Integer> nc = new ArrayList<>();
        List<String> builderList = new ArrayList<>();

        // On compte les nombres de classes en fonction des id
        for(int i=0 ; i<id.size() ; i++){ 

            ProcessBuilder processBuilder = new ProcessBuilder();
            builderList.clear();
            builderList.add("cmd.exe");
            builderList.add("/C");
            builderList.add("cd " + dossier + " && git reset " + id.get(i));
            processBuilder.command(builderList);
            Process process = processBuilder.start();

            BufferedReader reader  = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            Integer nbrClasses = 0;
            // On compte le nombre de classes java
            while ((line = reader.readLine()) != null){
                if(line.substring(line.length()-5, line.length()).equals(".java"))
                    nbrClasses++;
            }
            nc.add(nbrClasses);
        }
        return nc;                           
    }

    // Pour mettre les informations ID et le nombre de classes dans le CSV
    public static void csv(ArrayList<String> id,ArrayList<Integer> nc){
            
        
        try (PrintWriter writer = new PrintWriter(new File("tache1.csv"))) {
            StringBuilder sb = new StringBuilder();

            // Premiière colonne des id-versions
            for(int i=0 ; i<id.size() ; i++)
                sb.append(id.get(i) + ",");

            sb.append("\n");

            // Deuxième colonne du nombre de classes associées
            for(int j=0 ; j<nc.size() ; j++)
                sb.append(nc.get(j) + ",");

            writer.write(sb.toString());
            writer.close();

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}