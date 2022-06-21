package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

public class TaskManager {
    static final String FILE_NAME = "tasks.csv";
    static final String[] OPTIONS = {"add", "remove", "list", "exit"};
    static String[][] tasks;
    public static int getTheNumber() { //pobiera numer taska do usunięcia
        Scanner scan = new Scanner(System.in);
        System.out.println("Please select number of task you want to remove.");

        String n = scan.nextLine();
        while (!isNumberGreaterEqualZero(n)) {
            System.out.println("Incorrect argument passed. Please give number greater or equal 0");
            scan.nextLine();
        }
        return Integer.parseInt(n);
    }
    private static void remove(String[][] tab, int index) { //sprawdza czy task istnieje; jeśli tak usuwa go
        try {
            if (index < tab.length) {
                tasks = ArrayUtils.remove(tab, index);
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Element not exist in tab");
        }
    }
    public static boolean isNumberGreaterEqualZero(String input) { //sprawdza czy podany numer taska jest większy od zera
        if (NumberUtils.isParsable(input)) {
            return Integer.parseInt(input) >= 0;
        }
        return false;
    }
    private static void add(){ //odpowiada za dodawanie tasków
        Scanner scan = new Scanner(System.in);
        System.out.println("Please add task description: ");
        String desc = scan.nextLine();
        System.out.println("Please add task due date: ");
        String date = scan.nextLine();
        System.out.println("Is your task important? Write true or false: ");
        String important = scan.nextLine();

        tasks =  Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length-1] = new String[3];
        tasks[tasks.length-1][0] = desc;
        tasks[tasks.length-1][1] = date;
        tasks[tasks.length-1][2] = important;
    }
    public static void printTab(String[][] tab) { //wyświetla listę tasków
        for (int i = 0; i < tab.length; i++) {
            System.out.print(i + " : ");
            for (int j = 0; j < tab[i].length; j++) {
                System.out.print(tab[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static String[][] loadDataToTab(String fileName) { //ładuje dane z pliku do programu
        Path dir = Paths.get(fileName);
        if(!Files.exists(dir)){
            System.out.println("File not exists");
            System.exit(0);
        }String[][] tab = null;
        List<String> strings = null;
        try {
            strings = Files.readAllLines(dir);
            tab = new String[strings.size()][strings.get(0).split(",").length];
            for (int i = 0; i < strings.size(); i++) {
                String[] split = strings.get(i).split(",");
                for (int j = 0; j < split.length; j++) {
                    tab[i][j] = split[j];
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tab;
    }
    public static void printOptions(String[] tab) { //Wyświetla dostępne opcje
            System.out.println(ConsoleColors.BLUE);
            System.out.println("Please select an option: " + ConsoleColors.RESET);
            for (String option : tab) {
                   System.out.println(option);
                }
        }
    public static void saveTabToFile(String fileName, String[][] tab) { //Nadpisuje dane w pliku
        Path dir = Paths.get(fileName);

        String[] lines = new String[tasks.length];
        for (int i = 0; i < tab.length; i++) {
            lines[i] = String.join(", ", tab[i]);
        }

        try {
            Files.write(dir, Arrays.asList(lines));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) { //Główna część programu
        printOptions(OPTIONS);
        tasks = loadDataToTab(FILE_NAME);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            switch (input) {
                case "exit":
                    saveTabToFile(FILE_NAME, tasks);
                    System.out.println(ConsoleColors.RED + "Bye mate.");
                    System.exit(0);
                    break;
                case "add":
                    add();
                    break;
                case "remove":
                    remove(tasks,getTheNumber());
                    System.out.println("Task succesfully removed.");
                    break;
                case "list":
                    printTab(tasks);
                    break;
                default:
                    System.out.println("Please select a correct option.");
            }
            printOptions(OPTIONS);
        }
    }
}