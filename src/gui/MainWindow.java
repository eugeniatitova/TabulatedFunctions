package gui;

import functions.FunctionPoint;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;

public class MainWindow extends Application {

    FunctionDocument functionDocument;
    FileChooser fileChooser;

    public static void main(String[] args) {
        launch(args);
    }

    @Override public void start(Stage primaryStage) throws CloneNotSupportedException {

        Scene scene = new Scene(new VBox(), 400, 350);
        primaryStage.setTitle("Tabulated function");
        functionDocument= new FunctionDocument();

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem newMenuItem = new MenuItem("New ..");
        MenuItem openMenuItem = new MenuItem("Open ..");
        MenuItem saveMenuItem = new MenuItem("Save ..");
        MenuItem saveasMenuItem = new MenuItem("Save as ..");
        MenuItem exitMenuItem = new MenuItem("Exit");

        newMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                try {
                    ModalWindow.creatingWindow(functionDocument);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                FunctionTable.updateTable(functionDocument.function);
            }
        });
        openMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event)  {
                try {
                    fileChooser = new FileChooser();
                    fileChooser.setTitle("Open file");
                    File file = fileChooser.showOpenDialog(primaryStage);
                    functionDocument.loadFunction(file.getName());
                    FunctionTable.updateTable(functionDocument.function);
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Can't open file");
                    alert.setHeaderText(e.getMessage());
                    alert.showAndWait();
                }
            }
        });
        saveMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                try {
                    fileChooser = new FileChooser();
                    fileChooser.setTitle("Save file");
                    fileChooser.setInitialFileName(functionDocument.filename);
                    File file = fileChooser.showSaveDialog(primaryStage);
                    functionDocument.saveFunction();
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Can't save file");
                    alert.setHeaderText(e.getMessage());
                    alert.showAndWait();
                }
            }
        });
        saveasMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                try {
                    fileChooser = new FileChooser();
                    fileChooser.setTitle("Save file as");
                    File file = fileChooser.showSaveDialog(primaryStage);
                    functionDocument.saveFunctionAs(file.getName());
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Can't save file");
                    alert.setHeaderText(e.getMessage());
                    alert.showAndWait();
                }
            }
        });
        exitMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                if (functionDocument.modified){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Current file will be not save");
                    alert.setHeaderText("Are you sure?");
                    alert.showAndWait().ifPresent(response->{
                        if(response ==ButtonType.OK)
                            System.exit(0);
                    });
                }
                else System.exit(0);
            }
        });
        fileMenu.getItems().addAll(newMenuItem,openMenuItem ,saveMenuItem,saveasMenuItem,exitMenuItem);

        Menu tabulateMenu = new Menu("Tabulate");
        MenuItem loadMenuItem = new MenuItem("Load ..");
        loadMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                try {
                    FunctionLoader.loadingWindow(functionDocument);
                    FunctionTable.updateTable(functionDocument.function);
                } catch (IllegalAccessException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Illegal access exception");
                    alert.setHeaderText(e.getMessage());
                    alert.showAndWait();
                } catch (InstantiationException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Instantiation exception");
                    alert.setHeaderText(e.getMessage());
                    alert.showAndWait();
                } catch (ClassNotFoundException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Class not found exception");
                    alert.setHeaderText(e.getMessage());
                    alert.showAndWait();
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Input-output exception");
                    alert.setHeaderText(e.getMessage());
                    alert.showAndWait();
                }
            }
        });

        tabulateMenu.getItems().addAll(loadMenuItem);
        menuBar.getMenus().addAll(fileMenu, tabulateMenu);

        FunctionTable functionTable= new FunctionTable(functionDocument.function);

        Label xLabel = new Label("New point x:");
        TextField xField = new TextField();
        Label yLabel = new Label("New point y:");
        TextField yField = new TextField();
        Button addButton = new Button("Add point");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                try {
                    functionTable.addPoint(new FunctionPoint(Double.parseDouble(xField.getText()),Double.parseDouble(yField.getText())));
                    xField.clear();
                    yField.clear();
                }
                catch (NumberFormatException e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Number format exception");
                    alert.setHeaderText(e.getMessage());
                    alert.showAndWait();
                }
            }
        });
        Button deleteButton = new Button("Delete point");
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                try {
                functionTable.deletePoint(functionTable.table.getSelectionModel().getSelectedIndex());
                }
                catch(NullPointerException e){}
            }
        });
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override public void handle(WindowEvent event) {
                if (functionDocument.modified){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Current file will be not save");
                    alert.setHeaderText("Are you sure?");
                    alert.showAndWait().ifPresent(response->{
                        if(response ==ButtonType.CANCEL)event.consume();
                    });
                }
            }
        });

        HBox xBox = new HBox(5);
        xBox.setAlignment(Pos.CENTER);
        xBox.setPadding(new Insets(15, 0, 10, 0));
        xBox.getChildren().add(xLabel);
        xBox.getChildren().add(xField);
        HBox yBox = new HBox(5);
        yBox.setAlignment(Pos.CENTER);
        yBox.setPadding(new Insets(0, 0, 10, 0));
        yBox.getChildren().add(yLabel);
        yBox.getChildren().add(yField);
        HBox bBox = new HBox(80);
        bBox.setAlignment(Pos.CENTER);
        bBox.setPadding(new Insets(0, 0, 25, 0));
        bBox.getChildren().add(addButton);
        bBox.getChildren().add(deleteButton);

        ((VBox) scene.getRoot()).getChildren().addAll(menuBar, FunctionTable.table, xBox, yBox, bBox);
        primaryStage.setScene(scene);
        FunctionTable.updateTable(functionDocument.function);
        primaryStage.show();
    }
}
