package functions;

import functions.meta.*;

public class Functions {
    private Functions(){}
    public static Function shift(Function f, double shiftX, double shiftY)
    //возвращает объект функции, полученной из исходной сдвигом вдоль осей;
    {return new Shift(f, shiftX, shiftY);}
    public static Function scale(Function f, double scaleX, double scaleY)
    //возвращает объект функции, полученной из исходной масштабированием вдоль осей;
    {return new Scale(f, scaleX, scaleY);}
    public static Function power(Function f, double power)
    //возвращает объект функции, являющейся заданной степенью исходной;
    {return new Power(f,power);}
    public static Function sum(Function f1, Function f2)
    //возвращает объект функции, являющейся суммой двух исходных;
    {return new Sum(f1,f2);}
    public static Function mult(Function f1, Function f2)
    //возвращает объект функции, являющейся произведением двух исходных;
    {return new Mult(f1,f2);}
    public static Function composition(Function f1, Function f2)
    //возвращает объект функции, являющейся композицией двух исходных.
    {return new Composition(f1,f2);}

    public synchronized static double integral (Function function, double leftX, double rightX, double step) {
        double sum = .0;
        try {
            int i;
            int step_count= (int)((rightX-leftX)/step);
            if (0 == step_count) return sum;

            step = (rightX - leftX) / (1.0 * step_count);
            for ( i = 1 ; i < step_count ; ++i ) {
                sum += function.getFunctionValue (leftX + i * step);
            }
            sum += (function.getFunctionValue(leftX) + function.getFunctionValue(rightX)) / 2;
            sum *= step;
            return sum;

        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return sum;
    }

}
