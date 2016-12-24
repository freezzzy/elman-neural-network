public class Matrix {

    public double[][] a;
    public int m;
    public int n;

    public Matrix(int m) {
        this(m, m);
    }

    public Matrix(int m, int n) {
        this.m = m;
        this.n = n;
        a = new double[m][n];
    }

    public Matrix(int m, int n, double left, double right) {
        this(m, n);
        Matrix.random(this, left, right);
    }

    public Matrix(double[] matrix) {
        double[][] result = new double[1][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            result[0][i] = matrix[i];
        }
        this.m = result.length;
        this.n = result[0].length;
        this.a = result;
    }

    public void set(int i, int j, double s) {
        a[i][j] = s;
    }

    public double get(int i, int j) {
        return a[i][j];
    }

    public Matrix transpose() {
        Matrix X = new Matrix(n, m);
        double[][] C = X.a;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (Double.isNaN(a[i][j])) {
                    System.out.println("Nan");
                }
                C[j][i] = a[i][j];
            }
        }
        return X;
    }

    public Matrix times(Matrix B) {
        Matrix X = new Matrix(m, B.n);
        double[][] C = X.a;
        double[] Bcolj = new double[n];
        for (int j = 0; j < B.n; j++) {
            for (int k = 0; k < n; k++) {
                Bcolj[k] = B.a[k][j];
            }
            for (int i = 0; i < m; i++) {
                double[] Arowi = a[i];
                double s = 0;
                for (int k = 0; k < n; k++) {
                    s += Arowi[k] * Bcolj[k];
                    if (Double.isInfinite(s)) {
                        s = s > 0 ? Double.MAX_VALUE : Double.MIN_VALUE;
                    }
                }
                C[i][j] = s;
            }
        }
        return X;
    }

    public Matrix times(double s) {
        Matrix X = new Matrix(m, n);
        double[][] C = X.a;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = s * a[i][j];
                if (Double.isInfinite(C[i][j])) {
                    C[i][j] = C[i][j] > 0 ? Double.MAX_VALUE : Double.MIN_VALUE;
                }
            }
        }
        return X;
    }

    public Matrix minus(Matrix B) {
        Matrix X = new Matrix(m, n);
        double[][] C = X.a;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = a[i][j] - B.a[i][j];
                if (Double.isInfinite(C[i][j])) {
                    C[i][j] = C[i][j] > 0 ? Double.MAX_VALUE : Double.MIN_VALUE;
                }
            }
        }
        return X;
    }

    public double[] getColumn(int columnIndex) {
        double[] column = new double[m];
        for (int i = 0; i < n; i++) {
            column[i] = this.a[i][columnIndex];
        }
        return column;
    }

    public double[] getLine(int lineIndex, int beginIndex, int partSize) {
        double[] line = new double[partSize];
        int endIndex = beginIndex + partSize;
        int i = 0;
        for (int j = beginIndex; j < endIndex; j++) {
            line[i] = this.a[lineIndex][j];
            i++;
        }
        return line;
    }

    public double[] getLine(int lineIndex) {
        double[] line = new double[n];
        for (int j = 0; j < n; j++) {
            line[j] = this.a[lineIndex][j];
        }
        return line;
    }

    public void setLine(int lineIndex, int beginIndex, int partSize, double[] linePart) {
        int endIndex = beginIndex + partSize;
        int i = -1;
        for (int j = beginIndex; j < endIndex; j++) {
            i++;
            a[lineIndex][j]=linePart[i];
        }
    }

    public void setLine(int lineIndex, int beginIndex, int partSize, double value) {
        int endIndex = beginIndex + partSize;
        for (int j = beginIndex; j < endIndex; j++) {
            a[lineIndex][j]=value;
        }
    }

    public void print() {
        System.out.println();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                String s = String.valueOf((a[i][j]));
                int padding = Math.max(1, n - s.length());
                for (int k = 0; k < padding; k++)
                    System.out.print(' ');
                System.out.print(s);
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void random(Matrix matrix, double min, double max) {
        double[][] mass = matrix.a;
        for (int i = 0; i < matrix.m; i++) {
            for (int j = 0; j < matrix.n; j++) {
                mass[i][j] = (Math.random() * (max - min) + min)/10;
            }
        }
    }

}