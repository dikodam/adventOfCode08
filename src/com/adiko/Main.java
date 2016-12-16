package com.adiko;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.adiko.CommandType;

public class Main extends Application {

    private List<String> lines;
    private List<Command> commands;

    public static void main(String[] args) {
        launch(args);
    }

    Stage window;
    TextField tfOutput;

    boolean[][] screen = new boolean[6][50];

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        tfOutput = new TextField();
        Button btnGo = new Button("GO");
        btnGo.setOnAction(e -> processInput());

        layout.getChildren().addAll(btnGo, tfOutput);

        StackPane root = new StackPane();
        root.getChildren().add(layout);
        window.setScene(new Scene(root, 300, 300));
        window.show();
    }

    private void processInput() {
        parseInput();
        parseCommands();
        executeCommands();
        tfOutput.setText(Integer.toString(countActivePixels()));

    }

    private int countActivePixels() {
        int active = 0;
        for (int y = 0; y < screen.length; y++) {
            for (int x = 0; x < screen[0].length; x++) {
                if (screen[y][x]) {
                    active++;
                }
            }
        }
        return active;
    }

    private void executeCommands() {
        for (Command command : commands) {
            executeCommand(command);
            printScreen();
        }
    }

    private void executeCommand(Command command) {
        switch (command.getCommandType()) {
            case RECT:
                createRect(command.getX(), command.getY());
                break;
            case ROTATE_COLUMN:
                rotateColumn(command.getX(), command.getValue());
                break;
            case ROTATE_ROW:
                rotateRow(command.getY(), command.getValue());
                break;
            default:
                break;
        }
    }

    private void rotateRow(int y, int value) {
        boolean[] oldRow = screen[y].clone();
        int rowLength = oldRow.length;
        for (int x = 0; x < rowLength; x++) {
            int pos = (value + x) % rowLength;
            screen[y][pos] = oldRow[x];
        }
    }

    private void rotateColumn(int x, int value) {
        int columnLength = screen.length;
        boolean[] oldColumn = new boolean[columnLength];
        for (int y = 0; y < columnLength; y++) {
            // no Boolean.valueOf() needed?
            oldColumn[y] = screen[y][x];
        }
        for (int y = 0; y < columnLength; y++) {
            int pos = (value + y) % columnLength;
            screen[pos][x] = oldColumn[y];
        }
    }

    private void createRect(int x, int y) {
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                screen[i][j] = true;
            }
        }
    }

    private void parseCommands() {
        commands = new ArrayList<>();
        for (String line : lines) {
            commands.add(new Command(line));
        }
    }

    private void printScreen() {
        int counter = 0;
        for (int y = 0; y < screen.length; y++) {
            for (int x = 0; x < screen[0].length; x++) {
                System.out.print(toChar(screen[y][x]));
                if(counter == 4){
                    System.out.print(" ");
                }
                counter = (counter + 1) % 5;
//                System.out.print(toInt(screen[y][x]) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private char toChar(boolean b) {
        return b ? 'X' : ' ';
    }

    private int toInt(boolean b) {
        return b ? 1 : 0;
    }

    private void parseInput() {
        String line;
        lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File("input.txt")))) {
            line = br.readLine();
            while (line != null) {
                lines.add(line);
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            // e.printStackTrace();
            System.out.println("FILE NOT FOUND: " + e.getMessage());
        } catch (IOException e) {
            // e.printStackTrace();
            System.out.println("IOEXCEPTION: " + e.getMessage());
        }
    }
}
