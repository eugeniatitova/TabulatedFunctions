package gui;

import functions.FunctionPoint;
import functions.FunctionPointIndexOutOfBoundsException;
import functions.InappropriateFunctionPointException;
import functions.TabulatedFunction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class FunctionTable extends TableView {

    static TabulatedFunction function;
    static ObservableList<FunctionPoint>points;
    public static TableView<FunctionPoint> table = new TableView<FunctionPoint>();

    public FunctionTable(TabulatedFunction function){
        this.function = function;
        TableColumn xCol = new TableColumn("x");
        TableColumn yCol = new TableColumn("y");
        xCol.setCellValueFactory(new PropertyValueFactory<FunctionPoint,Double>("abscissa"));
        yCol.setCellValueFactory(new PropertyValueFactory<FunctionPoint,Double>("ordinate"));
        table.getColumns().addAll( xCol, yCol);
        updateTable(this.function);

    }
    public static int getRowCount() {
        return function.getPointsCount();
    }
    public int getColumnCount() {
        return 2;
    }
    public String getColumnName(int index) {
        return (index==0)? "x":"y";
    }
    public Class getColumnClass(int index) {
        return Double.class;
    }
    public static void updateTable(TabulatedFunction function){
        FunctionTable.function = function;
        points = FXCollections.observableArrayList();
        for (int i=0; i<function.getPointsCount(); i++)
            points.add(new FunctionPoint(function.getPoint(i)));
        table.setItems(points);
    }
    public  Object getValueAt(int rowIndex, int columnIndex) {
        return (columnIndex==0)? (Object) function.getPointX(rowIndex): (Object) function.getPointY(rowIndex);
    }
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        try {
            if ((columnIndex == 0)) {
                function.setPointX(rowIndex, (Double) value);
                updateTable(function);
            } else {
                function.setPointY(rowIndex, (Double) value);
                updateTable(function);
            }
            table.setItems(points);
        } catch (InappropriateFunctionPointException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Inappropriate function point exception");
            alert.setHeaderText("Can't change this value");
            alert.showAndWait();
        }
        catch (FunctionPointIndexOutOfBoundsException e ){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Function point index out of bounds exception");
            alert.setHeaderText("Can't change this value");
            alert.showAndWait();
        }
    }
    public static void addPoint(Object value){
        try {
            function.addPoint((FunctionPoint)value);
            updateTable(function);
        } catch (InappropriateFunctionPointException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Inappropriate function point exception");
            alert.setHeaderText("This point is already exist");
            alert.showAndWait();
        }
    }
    public static void deletePoint(int rowIndex){
        try {
            function.deletePoint(rowIndex);
            updateTable(function);
        }
        catch (FunctionPointIndexOutOfBoundsException e){}
        catch (ArrayIndexOutOfBoundsException e){}
        catch(IllegalStateException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Illegal state exception");
            alert.setHeaderText("The minimum point's count is 2");
            alert.showAndWait();
        }

    }
}
