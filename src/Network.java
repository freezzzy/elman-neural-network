public class Network {

    private static final int CONTEXT = 0;
    private static final int HIDDEN = 1;
    private static final int OUTPUT = 2;
    private static final int LAYERS_COUNT = 4;

    private Layer[] Layers = new Layer[LAYERS_COUNT];

    public double guessNext(Matrix input, double e, double alpha, int maxStepsCount) {
        Matrix inputSet = input;
        int inputSetSize = inputSet.m;
        int inputLength = inputSet.n;
        double[] x;
        double t;
        int p = inputLength - 1;
        int m = p / 2;
        if (m < 2) {
            m = 2;
        }
        double y = Double.NaN;
        double outError = Double.MAX_VALUE;
        Matrix Sh;
        Matrix Sy;
        double Ej;

        int stepsCount = 0;

        Layers[CONTEXT] = new Layer(1, p + m);
        Matrix contextSet = new Matrix(inputSetSize, m, -0.5, 0.5);
        Matrix W1 = new Matrix(p + m, m, -0.5, 0.5);
        Layers[HIDDEN] = new Layer(1, m);
        Matrix W2 = new Matrix(m, 1, -0.5, 0.5);
        Layers[OUTPUT] = new Layer(1, 1);

        double[] context;
        double E;
        boolean done = true;

        do {
            E = 0;
            for (int i = 0; i < inputSetSize; i++) {
                x = inputSet.getLine(i, 0, p);

                t = inputSet.a[i][p];
                context = contextSet.getLine(i);

                Layers[CONTEXT].neuronsList.setLine(0, 0, p, x);
                Layers[CONTEXT].neuronsList.setLine(0, p, m, context);

                Sh = Layers[CONTEXT].neuronsList.times(W1).minus(Layers[HIDDEN].t);
                Layers[HIDDEN].neuronsList = Activate(Sh);

                Sy = Layers[HIDDEN].neuronsList.times(W2).minus(Layers[OUTPUT].t);
                Layers[OUTPUT].neuronsList = Activate(Sy);

                y = Layers[OUTPUT].neuronsList.a[0][0];

                outError = y - t;

                double[] hiddenErrors = new double[m];
                double dFSy = derivativeActivationFunction(Sy.a[0][0]);
                for (int wI = 0; wI < m; wI++ ) {
                    W2.a[wI][0] = W2.a[wI][0] - alpha * outError * dFSy * Layers[HIDDEN].neuronsList.a[0][wI];
                }
                Layers[OUTPUT].t.a[0][0] = Layers[OUTPUT].t.a[0][0] + alpha * outError * dFSy;

                Matrix dFSh = new Matrix(1, m);
                for (int j = 0; j < m; j++) {
                    dFSh.a[0][j] = derivativeActivationFunction(Sh.a[0][j]);
                    hiddenErrors[j] = outError * dFSh.a[0][j] * W2.a[j][0];
                }

                for (int wI = 0; wI < p + m; wI++) {
                    for (int wJ = 0; wJ < m; wJ++) {
                        W1.a[wI][wJ] = W1.a[wI][wJ] - alpha * hiddenErrors[wJ] * dFSh.a[0][wJ] * Layers[CONTEXT].neuronsList.a[0][wI];
                    }
                }

                for (int j = 0; j < m; j++) {
                    Layers[HIDDEN].t.a[0][j] = Layers[HIDDEN].t.a[0][j] + alpha * hiddenErrors[j] * dFSh.a[0][j];
                }
                contextSet.setLine(0, 0, m, Layers[HIDDEN].neuronsList.getLine(0));
            }
            for (int i = 0; i < inputSetSize; i++) {
                x = inputSet.getLine(i, 0, p);

                t = inputSet.a[i][p];
                context = contextSet.getLine(i);

                Layers[CONTEXT].neuronsList.setLine(0, 0, p, x);
                Layers[CONTEXT].neuronsList.setLine(0, p, m, context);

                Sh = Layers[CONTEXT].neuronsList.times(W1).minus(Layers[HIDDEN].t);
                Layers[HIDDEN].neuronsList = Activate(Sh);

                Sy = Layers[HIDDEN].neuronsList.times(W2).minus(Layers[OUTPUT].t);
                Layers[OUTPUT].neuronsList = Activate(Sy);

                y = Layers[OUTPUT].neuronsList.a[0][0];

                outError = y - t;

                Ej = outError * outError / 2;
                E += Ej;
            }
            stepsCount++;
            if(stepsCount % (maxStepsCount/10) == 0) {
                System.out.println("Iteration = "+ stepsCount + " ; E = " + E);
            }
            if (stepsCount == maxStepsCount) {
                done = false;
            }
        } while (Math.abs(E) > e && done);
        x = inputSet.getLine(inputSetSize -1 , 1, p);
        context = contextSet.getLine(inputSetSize - 1);

        Layers[CONTEXT].neuronsList.setLine(0, 0, p, x);
        Layers[CONTEXT].neuronsList.setLine(0, p, m, context);

        Sh = Layers[CONTEXT].neuronsList.times(W1).minus(Layers[HIDDEN].t);
        Layers[HIDDEN].neuronsList = Activate(Sh);

        Sy = Layers[HIDDEN].neuronsList.times(W2).minus(Layers[OUTPUT].t);
        Layers[OUTPUT].neuronsList = Activate(Sy);

        y = Layers[OUTPUT].neuronsList.a[0][0];

        W1.print();
        W2.print();

        Layers[CONTEXT].print("CONTEXT");
        Layers[HIDDEN].print("HIDDEN");
        Layers[OUTPUT].print("OUTPUT");
        System.out.println("E = " + E);
        System.out.println("ALPHA = " + alpha);
        System.out.println("StepsCount = " + stepsCount);
        return y;
    }

    private Matrix Activate(Matrix S) {
        int lineIndex = 0;
        int columnsCount = S.n;
        Matrix output = new Matrix(1, columnsCount);
        for (int j = 0; j < columnsCount; j++) {
            output.a[lineIndex][j] = activationFunction(S.a[lineIndex][j]);
        }
        return output;
    }

    private double activationFunction(double x){
        return Math.atan(x);
    }

    private double derivativeActivationFunction(double x) {
        return 1.0 / (1.0 + x * x);
    }

}
