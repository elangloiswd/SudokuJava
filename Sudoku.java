import java.io.BufferedReader; 
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Sudoku {
    private static final int tableauSize = 9; // On définit la grandeur du tableau

    public static void main(String[] args) throws IOException {
        File file = new File("partie1.txt"); // Le fichier qui contient le tableau 
        int[][] tableau = new int[tableauSize][tableauSize]; // Crée un array 2D pour représenter le tableau 

        try (
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
        ) {
            String line;
            // Lecture de chaque ligne du fichier et parse les triplets 
            while ((line = bufferedReader.readLine()) != null) {
                String[] numbers = line.trim().split(" "); // On trim les espaces 
                for (String number : numbers) {
                    if (number.length() != 3) { 
                        // On vérifie si les éléments sont des triplets de chiffres 
                        System.err.println("Erreur: Le format est invalide.");
                        return;
                    }
                    // Les données extraites du fichier
                    int row = Character.getNumericValue(number.charAt(0));  
                    int col = Character.getNumericValue(number.charAt(1));
                    int value = Character.getNumericValue(number.charAt(2)); 

                    // On remplit le tableau avec les valeurs du fichier 
                    tableau[row][col] = value;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Erreur: fichier non trouvé. " + file.getName());
            return;
        } catch (IOException e) {
            System.err.println("Erreur de lecture: " + e.getMessage());
            return;
        } catch (NumberFormatException e) {
            System.err.println("Erreur: Un ou plusieurs entrées ne sont pas valides.");
            return;
        } catch (ArrayIndexOutOfBoundsException e) { 
            // Si le tableau n'est pas symétrique 
            System.err.println("Erreur: Les indices du tableau contiennent des valeurs qui ne sont pas 0-8.");
            return;
        }

        // Validation du tableau. 
        if (isValid(tableau)) {
            System.out.println("=================================");
            System.out.println("==     Le Sudoku est valide    ==");
            System.out.println("=================================");
        } else {
            System.out.println("=================================");
            System.out.println("= !Le Sudoku n'est pas valide! ==");
            System.out.println("=================================");
        }

        // Transposition du tableau et sauvegarde en fichier. 
        int[][] transposedTableau = transpose(tableau);
        saveTranspositionToFile(transposedTableau, "transposition.txt");

        // Print le tableau original 
        printGrid(tableau);
        System.out.println("=======================================");
        System.out.println("= transposition.txt a été sauvegardé =");
        System.out.println("=======================================");
        printGrid(transposedTableau);
    }

    // Méthode de validation. 
    private static boolean isValid(int[][] tableau) {
        // Vérifie les lignes. 
        for (int row = 0; row < tableauSize; row++) {
            boolean[] rowCheck = new boolean[10]; 
            // On utilise [10] pour éviter d'avoir à -1 les nombres (Plus clean et moins de chance d'erreur) 
            for (int col = 0; col < tableauSize; col++) {
                int number = tableau[row][col];
                if (number < 1 || number > 9 || rowCheck[number]) { 
                    // if le nombre est plus d'une fois dans la ligne erreur 
                    System.out.println("Sudoku invalide à la ligne " + (row + 1) + ", col " + (col + 1) + ", valeur " + number);
                    return false;
                }
                rowCheck[number] = true;
            }
        }

        // Validation des colonnes 
        for (int col = 0; col < tableauSize; col++) {
            boolean[] colCheck = new boolean[10];
            for (int row = 0; row < tableauSize; row++) {
                int number = tableau[row][col];
                if (number < 1 || number > 9 || colCheck[number]) { 
                    // if le nombre est plus d'une fois dans la colonne erreur 
                    System.out.println("Sudoku invalide à la col " + (col + 1) + ", ligne " + (row + 1) + ", valeur " + number);
                    return false;
                }
                colCheck[number] = true;
            }
        }

        // Validation de la boîte 3x3 
        for (int boxRow = 0; boxRow < tableauSize; boxRow += 3) {
            for (int boxCol = 0; boxCol < tableauSize; boxCol += 3) {
                boolean[] boxCheck = new boolean[10];
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        int number = tableau[boxRow + i][boxCol + j];
                        if (number < 1 || number > 9 || boxCheck[number]) { 
                            // if le nombre est plus d'une fois dans la boîte erreur 
                            System.out.println("Sudoku invalide à la ligne " + (boxRow + 1) + ", col " + (boxCol + 1) + ", valeur " + number);
                            return false;
                        }
                        boxCheck[number] = true; // Tous les tests ont passé, le tableau est valide. 
                    }
                }
            }
        }
        return true;
    }

    // Méthode pour la transposition. 
    private static int[][] transpose(int[][] tableau) {
        int[][] transposed = new int[tableauSize][tableauSize];
        for (int i = 0; i < tableauSize; i++) {
            for (int j = 0; j < tableauSize; j++) {
                transposed[j][i] = tableau[i][j]; // Inverse les indices pour la transposition. 
            }
        }
        return transposed;
    }

    // Méthode pour sauvegarder en fichier la transposition. 
    private static void saveTranspositionToFile(int[][] transposed, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (int row = 0; row < tableauSize; row++) {
                for (int col = 0; col < tableauSize; col++) {
                    int value = transposed[row][col];

                    // Format du triplet RCV 
                    String triplet = String.format("%d%d%d", row, col, value);
                    writer.write(triplet + " ");
                }
                writer.newLine(); // Saute une ligne pour chaque rangée 
            }
        } catch (IOException e) {
            System.err.println("Erreur à l'écriture du fichier en format triplet: " + e.getMessage());
        }
    }

    // Méthode pour imprimer le tableau en console. 
    private static void printGrid(int[][] tableau) {
        for (int i = 0; i < tableauSize; i++) {
            for (int j = 0; j < tableauSize; j++) {
                System.out.print(tableau[i][j] + " ");
            }
            System.out.println("");
        }
    }
}