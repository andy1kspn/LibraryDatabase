import java.io.*;

public class BibliotecaDatabase {

    public static void main(String[] args) {
        System.out.println("STARTED...");

        // Create an instance of the MeniuPrincipal class
        MeniuPrincipal meniu = new MeniuPrincipal();

        createTextFile();
        // Call a method to generate a new window
        meniu.generateWindow();
    }

    public static void createTextFile() {
        String content = "Numar de ordine | Numar de identificare | Denumire | Autor 1 | Autor 2 | Editura | Status | Data | Ora | Persoana";
        String content2 = "=====================================================================================\n";
        String filePath = System.getProperty("user.home") + File.separator + "DATABASE.txt";

        File file = new File(filePath);

        if (file.exists() && containsLine(file, content)) {
            System.out.println("Fisierul " +filePath + " deja exista!");
            return;
        } else {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(content);
                writer.newLine();
                writer.write(content2);
                System.out.println("Fisierul este prezent la adresa: " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean containsLine(File file, String line) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.equals(line)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
