package functions;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class TabulatedFunctions implements Serializable {
    private TabulatedFunctions(){}
    private static TabulatedFunctionFactory tabulatedFunctionFactory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();
    public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return tabulatedFunctionFactory.createTabulatedFunction(leftX, rightX, pointsCount);
    }
    public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return tabulatedFunctionFactory.createTabulatedFunction(leftX, rightX, values);
    }
    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return tabulatedFunctionFactory.createTabulatedFunction(points);
    }
    public static TabulatedFunction createTabulatedFunction(Class tabulatedFunction, double leftX, double rightX, int pointsCount) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        TabulatedFunction f = null;
        try {
            f=(TabulatedFunction) tabulatedFunction.getConstructor(double.class, double.class, int.class).newInstance(leftX, rightX,pointsCount);
        }
        catch(NoSuchMethodException e){throw new IllegalArgumentException(e);}
        catch(IllegalAccessException e){throw new IllegalArgumentException(e);}
        catch (InvocationTargetException e){throw new IllegalArgumentException(e);}
        catch (InstantiationException e){throw new IllegalArgumentException(e);}
        return f;
    }
    public static TabulatedFunction createTabulatedFunction(Class tabulatedFunction, double leftX, double rightX, double[] values) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        TabulatedFunction f = null;
        try {
            f=(TabulatedFunction) tabulatedFunction.getConstructor(double.class, double.class, double[].class).newInstance(leftX, rightX, values);
        }
        catch(NoSuchMethodException e){throw new IllegalArgumentException(e);}
        catch(IllegalAccessException e){throw new IllegalArgumentException(e);}
        catch (InvocationTargetException e){throw new IllegalArgumentException(e);}
        catch (InstantiationException e){throw new IllegalArgumentException(e);}
        return f;
    }
    public static TabulatedFunction createTabulatedFunction(Class tabulatedFunction , FunctionPoint[] functionPoints) {
        TabulatedFunction f = null;
        try {
            f=(TabulatedFunction) tabulatedFunction.getConstructor(FunctionPoint[].class).newInstance((Object) functionPoints);
        }
        catch(NoSuchMethodException e){throw new IllegalArgumentException(e);}
        catch(IllegalAccessException e){throw new IllegalArgumentException(e);}
        catch (InvocationTargetException e){throw new IllegalArgumentException(e);}
        catch (InstantiationException e){throw new IllegalArgumentException(e);}
        return f;
    }

    public static void setTabulatedFunctionFactory(LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory linkedListTabulatedFunctionFactory) {
        tabulatedFunctionFactory= linkedListTabulatedFunctionFactory;
    }
    public static void setTabulatedFunctionFactory(ArrayTabulatedFunction.ArrayTabulatedFunctionFactory arrayTabulatedFunctionFactory) {
        tabulatedFunctionFactory = arrayTabulatedFunctionFactory;
    }
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount)throws IllegalArgumentException {
        if (leftX < function.getLeftDomainBorder() || function.getRightDomainBorder() < rightX)
            throw new IllegalArgumentException();
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++)
            points[i] = new FunctionPoint(leftX + i * step, function.getFunctionValue(leftX + i * step));
        return createTabulatedFunction(points);
    }
    public static TabulatedFunction tabulate(Class tabulatedFunction, Function function, double leftX, double rightX, int pointsCount) throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        if (leftX < function.getLeftDomainBorder() || function.getRightDomainBorder() < rightX)
            throw new IllegalArgumentException();
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++)
            points[i] = new FunctionPoint(leftX + i * step, function.getFunctionValue(leftX + i * step));
        return (TabulatedFunction) tabulatedFunction.getConstructor(FunctionPoint[].class).newInstance((Object) points);
    }
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        PrintStream ps= new PrintStream(out);
        ps.println(function.getPointsCount());
        for (int i=0; i<function.getPointsCount(); i++){
            ps.println(function.getPointX(i));
            ps.println(function.getPointY(i));
        }
        ps.close();
    }
    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
       Scanner sc = new Scanner(in);
       int n = sc.nextInt();
       FunctionPoint[] points = new FunctionPoint[n];
       for(int i=0; i<n; i++) {
           Double x= sc.nextDouble();
           Double y= sc.nextDouble();
           points[i]=new FunctionPoint(x,y);
       }
       sc.close();
       return createTabulatedFunction(points);
   }
   public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
       out.write(String.valueOf(function.getPointsCount()));
       out.write('\n');
       for (int i=0; i<function.getPointsCount(); i++){
           out.write(String.valueOf(function.getPointX(i)));
           out.write('\n');
           out.write(String.valueOf(function.getPointY(i)));
           out.write('\n');
       }
       out.close();
   }
   public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
       try {
           StreamTokenizer st = new StreamTokenizer(in);
           st.nextToken();
           int size = (int) st.nval;
           FunctionPoint[] points = new FunctionPoint[size];
           int i = 0;
           while (st.nextToken() != StreamTokenizer.TT_EOF) {
               if (i % 2 == 0) points[i / 2] = new FunctionPoint(st.nval, 0);
               else points[(int) i / 2].sety(st.nval);
               i++;
           }
           in.close();
           return createTabulatedFunction(points);
       }
       catch (ArrayIndexOutOfBoundsException e){
           throw new IOException("Can't read file");
       }
    }

}
