package gui;

import functions.Function;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FunctionLoader extends ClassLoader {

    private java.util.Map classesHash= new java.util.HashMap();
    public final String[] classPath;

    static Function cos;
    static Function sin;
    static Function tan;
    static Function exp;
    static Function log;

    public FunctionLoader(String[] classPath){
        this.classPath= classPath;
    }
    protected synchronized Class loadClass(String name,boolean resolve) throws ClassNotFoundException {
        Class result= findClass(name);
        if (resolve) resolveClass(result);
        return result;
    }
    protected Class findClass(String name) throws ClassNotFoundException {
        Class result= (Class)classesHash.get(name);
        if (result!=null) return result;
        File f= findFile(name.replace('.','/'),".class");
        if (f==null) return findSystemClass(name);
        try {
            byte[] classBytes= loadFileAsBytes(f);
            result= defineClass(name,classBytes,0,classBytes.length);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Class nor found exception");
            alert.setHeaderText("Cannot load class "+name+": "+e);
            alert.showAndWait();
        } catch (ClassFormatError e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Class nor found exception");
            alert.setHeaderText("Format of class file incorrect for class "+name+": "+e);
            alert.showAndWait();
        }
        classesHash.put(name,result);
        return result;
    }
    protected java.net.URL findResource(String name)
    {
        File f= findFile(name, "");
        if (f==null) return null;
        try {
            return f.toURL();
        } catch(java.net.MalformedURLException e) {
            return null;
        }
    }
    private File findFile(String name, String extension)
    {
        for (int k=0; k <classPath.length; k++) {
            File f= new File((new File(classPath[k])).getPath() +
                    File.separatorChar + name.replace('/',File.separatorChar) + extension);
            if (f.exists())
                return f;
        }
        return null;
    }
    public static byte[] loadFileAsBytes(File file) throws IOException
    {
        byte[] result = new byte[(int)file.length()];
        FileInputStream f = new FileInputStream(file);
        try {
            f.read(result,0,result.length);
        } finally {
            try {
                f.close();
            } catch (Exception e) { };
        }
        return result;
    }

    public static void loadingWindow(FunctionDocument functionDocument) throws IllegalAccessException, InstantiationException, ClassNotFoundException, IOException {

        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Tabulate function");
        stage.initModality(Modality.APPLICATION_MODAL);

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(20, 0, 10, 0));

        ClassLoader loader= new FunctionLoader(new String[] {"functions/basic"});
        ObservableList<Class> functions = FXCollections.observableArrayList(
                Class.forName("functions.basic.Cos",true,loader),
                Class.forName("functions.basic.Sin",true,loader),
                Class.forName("functions.basic.Tan",true,loader),
                Class.forName("functions.basic.Exp",true,loader),
                Class.forName("functions.basic.Log",true,loader)
                );
        ListView<Class> functionsList = new ListView<Class>(functions);

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event->stage.close());

        Button okButton = new Button("OK");
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                try {
                    ModalWindow.creatingWindow(functionDocument, (Function) functionsList.getSelectionModel().getSelectedItem().newInstance());
                } catch (InstantiationException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Instantiation exception");
                    alert.setHeaderText(e.getMessage());
                    alert.showAndWait();
                } catch (IllegalAccessException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Illegal access exception");
                    alert.setHeaderText(e.getMessage());
                    alert.showAndWait();
                }
                catch (NullPointerException e){ }
                stage.close();
            }
        });

        HBox buttonBox = new HBox(150);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 10, 0));
        buttonBox.getChildren().add(cancelButton);
        buttonBox.getChildren().add(okButton);

        root.add(functionsList,0,0);
        root.add(buttonBox,0,1);

        Scene scene= new Scene(root,300,200);
        stage.setScene(scene);
        stage.showAndWait();

    }
    public static double getLogBase(){

        final Double[] logBase = new Double[1];
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Base of logarithm");
        stage.initModality(Modality.WINDOW_MODAL);

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(10, 0, 10, 0));

        TextField base = new TextField();
        base.setText(String.valueOf(Math.E));

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                stage.close();
            }
        });

        Button okButton = new Button("OK");
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                try {
                    logBase[0] = Double.parseDouble(base.getText());
                    stage.close();
                }
                catch(NumberFormatException e){
                    if(e.getMessage()=="empty String"){
                        logBase[0] = Math.E;
                        stage.close();
                    }
                    else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Number format exception");
                        alert.setHeaderText(e.getMessage());
                        alert.showAndWait();
                    }
                }
            }
        });

        HBox buttonBox = new HBox(80);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(0, 0, 10, 0));
        buttonBox.getChildren().add(cancelButton);
        buttonBox.getChildren().add(okButton);

        root.add(base, 0,0);
        root.add(buttonBox,0,1);

        Scene scene= new Scene(root,225,120);
        stage.setScene(scene);
        stage.showAndWait();
        return logBase[0];
    }
}
