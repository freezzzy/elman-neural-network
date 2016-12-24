public class Layer {

    public Matrix neuronsList;
    public Matrix t;

    public Layer(int linesCount, int columnsCount) {
        neuronsList = new Matrix(linesCount, columnsCount);
        t = new Matrix(linesCount, columnsCount, -0.5, 0.5);
    }

    public void print(String name) {
        System.out.println("### " + name + " ###");
        System.out.println("n: ");
        neuronsList.print();
        System.out.println("t: ");
        t.print();
    }

}
