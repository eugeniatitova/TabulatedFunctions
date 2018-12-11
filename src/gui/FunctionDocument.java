package gui;

import functions.*;
import javafx.scene.control.Alert;

import java.io.*;
import java.util.Iterator;

public class FunctionDocument<T> implements TabulatedFunction, Cloneable, Iterable {

    TabulatedFunction function;
    String filename;
    Boolean modified;

    public FunctionDocument() throws CloneNotSupportedException {
        ModalWindow.creatingWindow(this);
        filename="New Tabulated Function";
        modified=true;
    }

    public void newFunction(double leftX, double rightX, int pointsCount) {
        function = new ArrayTabulatedFunction(leftX, rightX, pointsCount);
        modified=true;
    }
    public void saveFunction() throws IOException {
        TabulatedFunctions.writeTabulatedFunction(function, new FileWriter(filename));
    }
    public void saveFunctionAs(String fileName) throws IOException {
        filename=fileName;
        TabulatedFunctions.writeTabulatedFunction(function, new FileWriter(filename));
    }
    public void loadFunction(String fileName) throws IOException {
        filename=fileName;
        function = TabulatedFunctions.readTabulatedFunction(new FileReader(filename));
    }
    public void tabulateFunction(Function f, double leftX, double rightX, int pointsCount){
        function = TabulatedFunctions.tabulate(f, leftX, rightX, pointsCount);
        modified=true;
    }

    @Override public Iterator<T> iterator() {
        return function.iterator();
    }

    @Override public int getPointsCount() {
        return function.getPointsCount();
    }
    @Override public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        return function.getPoint(index);
    }
    @Override public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        function.setPoint(index,point);
        modified=true;
    }
    @Override public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        return function.getPointX(index);
    }
    @Override public void setPointX(int index, double x) throws InappropriateFunctionPointException, FunctionPointIndexOutOfBoundsException {
        function.setPointX(index, x);
        modified=true;
    }
    @Override public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        return function.getPointY(index);
    }
    @Override public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        function.setPointY(index,y);
        modified=true;
    }
    @Override public void deletePoint(int index) throws IllegalStateException, FunctionPointIndexOutOfBoundsException {
        function.deletePoint(index);
        modified=true;
    }
    @Override public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        function.addPoint(point);
        modified=true;
    }
    public String toString(){
        return function.toString();
    }
    public boolean equals(Object o){
        if(o instanceof FunctionDocument){
            if (function.equals(o)&&modified==((FunctionDocument) o).modified&&
                    filename==((FunctionDocument) o).filename) return true;
        }
        return false;
    }
    public int hashCode(){
        return function.hashCode();
    }
    @Override public Object clone() throws CloneNotSupportedException {
        Object clone = (FunctionDocument) super.clone();
        try {
            FileOutputStream fos = new FileOutputStream(String.valueOf(hashCode()));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
            FileInputStream fis = new FileInputStream(toString());
            ObjectInputStream oin = new ObjectInputStream(fis);
            clone = oin.readObject();
            oin.close();
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("File not found exception");
            alert.setHeaderText(e.getLocalizedMessage());
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input-output exception");
            alert.setHeaderText(e.getLocalizedMessage());
            alert.showAndWait();
        } catch (ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Class not found exception");
            alert.setHeaderText(e.getLocalizedMessage());
            alert.showAndWait();
        }
        return clone;
    }
}
