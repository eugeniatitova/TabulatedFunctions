package gui;

import functions.Function;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ModalWindow extends Dialog {

    static Label ldbLabel = new Label("Left domain border:");
    static TextField ldbField = new TextField();
    static Label rdbLabel = new Label("Right domain border:");
    static TextField rdbField = new TextField();
    static Label pcLabel = new Label("Points count: ");
    static final Spinner<Integer> pcSpinner = new Spinner<Integer>();
    static Button cancelButton = new Button("Cancel");
    static Button okButton = new Button("OK");

    private static double getLeftDomainBorder(){
        return Double.parseDouble(ldbField.getText());
    }
    private static double getRightDomainBorder() {
        return Double.parseDouble(rdbField.getText());
    }
    private static int getPointsCount(){
        return pcSpinner.getValue();
    }

    public static void creatingWindow(FunctionDocument functionDocument, Function function){

        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Function parameters");
        stage.initModality(Modality.APPLICATION_MODAL);

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(10, 25, 10, 25));

        ldbField.setText("0");
        rdbField.setText("9");
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.
                IntegerSpinnerValueFactory(2, Integer.MAX_VALUE, 10);
        pcSpinner.setValueFactory(valueFactory);

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                stage.close();
            }
        });

        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
               try {
                   functionDocument.tabulateFunction(function, getLeftDomainBorder(), getRightDomainBorder(),getPointsCount());
                   stage.close();
               }
               catch(NumberFormatException e){
                   Alert alert = new Alert(Alert.AlertType.ERROR);
                   alert.setTitle("Number format exception");
                   alert.setHeaderText(e.getMessage());
                   alert.showAndWait();
               }
               catch(IllegalArgumentException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Illegal argument exception");
                alert.setHeaderText("Incorrect domain borders");
                alert.showAndWait();
               }
            } });

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override public void handle(WindowEvent event) {
                stage.close();
            }
        });

        root.add(ldbLabel, 0, 1);
        root.add(ldbField, 1, 1);
        root.add(rdbLabel, 0, 2);
        root.add(rdbField, 1, 2);
        root.add(pcLabel,0,3);
        root.add(pcSpinner,1,3);
        root.add(cancelButton,0,4);
        root.add(okButton,1,4);

        Scene scene= new Scene(root,400,200);
        stage.setScene(scene);
        stage.showAndWait();
}
    public static void creatingWindow(FunctionDocument functionDocument) throws CloneNotSupportedException {

        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Function parameters");
        stage.initModality(Modality.APPLICATION_MODAL);

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(10, 25, 10, 25));

        ldbField.setText("0");
        rdbField.setText("9");
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.
                IntegerSpinnerValueFactory(2, Integer.MAX_VALUE, 10);
        pcSpinner.setValueFactory(valueFactory);

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                stage.close();
            }
        });

        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                try {
                    functionDocument.newFunction(getLeftDomainBorder(), getRightDomainBorder(),getPointsCount());
                    stage.close();
                }
                catch(NumberFormatException e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Number format exception");
                    alert.setHeaderText(e.getMessage());
                    alert.showAndWait();
                }
                catch(IllegalArgumentException e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Illegal argument exception");
                    alert.setHeaderText("Incorrect domain borders");
                    alert.showAndWait();
                }
            } });

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override public void handle(WindowEvent event) {
                System.exit(0);
            }
        });

        root.add(ldbLabel, 0, 1);
        root.add(ldbField, 1, 1);
        root.add(rdbLabel, 0, 2);
        root.add(rdbField, 1, 2);
        root.add(pcLabel,0,3);
        root.add(pcSpinner,1,3);
        root.add(cancelButton,0,4);
        root.add(okButton,1,4);

        Scene scene= new Scene(root,400,200);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
