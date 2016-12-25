import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public JLabel pLabel = new JLabel("   p");
    public JLabel maxStepLabel = new JLabel("   maxStep");
    public JLabel maxErrorLabel = new JLabel("   maxError");
    public JLabel alphaLabel = new JLabel("   alpha");
    public JLabel scaleLabel = new JLabel("   scale");
    public JLabel sequenceLabel = new JLabel("   sequence");

    public JTextField pInput = new JTextField("4");
    public JTextField maxStepInput = new JTextField("1000000");
    public JTextField maxErrorInput = new JTextField("0.0000001");
    public JTextField alphaInput = new JTextField("0.01");
    public JTextField scaleInput = new JTextField("10");
    public JTextField sequenceInput = new JTextField("1 2 3 4 5 6 7 8 9 10");
//    public JTextField sequenceInput = new JTextField("1 -1 1 -1 1 -1 1 -1 1 -1 1");
//    public JTextField sequenceInput = new JTextField("1 2 4 8 16 32 64 128");

    public Main() {
        setTitle("Elman Neutral Network");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel upPanel = new JPanel();
        upPanel.setLayout(new GridLayout(0, 2));

        upPanel.add(pLabel);
        upPanel.add(pInput);

        upPanel.add(maxStepLabel);
        upPanel.add(maxStepInput);

        upPanel.add(maxErrorLabel);
        upPanel.add(maxErrorInput);

        upPanel.add(alphaLabel);
        upPanel.add(alphaInput);

        upPanel.add(scaleLabel);
        upPanel.add(scaleInput);

        upPanel.add(sequenceLabel);
        upPanel.add(sequenceInput);

        add(upPanel, BorderLayout.CENTER);

        JButton run = new JButton("Run");

        add(run, BorderLayout.SOUTH);

        setVisible(true);

        run.addActionListener(e -> run());
    }

    public void run() {
        double[] sequence = this.getSequence(sequenceInput.getText());

        double max = findMax(sequence);
        double z = 0;
        if(max >= 1){
            if(max < 10) {
                z = 10;
            } else if(max < 100){
                z = 100;
            } else if(max < 1000){
                z = 1000;
            }
            sequence = scaleSequence(sequence, z);
        }
        Matrix input = getInputMatrix(sequence);
        System.out.println("Матрица обучения");
        input.print();
        Network network = new Network();
        double y = network.guessNext(
                input,
                Double.valueOf(maxErrorInput.getText()),
                Double.valueOf(alphaInput.getText()),
                Integer.parseInt(maxStepInput.getText())
        );
        if(max >= 1) {
            y = y * z;
        }
        System.out.println("Next Number = " + y);
    }

    private Matrix getInputMatrix(double[] sequence) {
        int windowSize = Integer.parseInt(pInput.getText());
        int sequencesLength = sequence.length;
        int step = 1;
        int windowsCount = 1 + (sequencesLength - windowSize) / step;
        int jW;
        Matrix input = new Matrix(windowsCount, windowSize);
        int begin = 0;
        for (int iW = 0; iW < windowsCount; iW++) {
            jW = 0;
            for (int j = begin; j < begin + windowSize && j < sequencesLength; j++) {
                input.a[iW][jW] = sequence[j];
                jW++;
            }
            begin += step;
        }
        return input;
    }

    private double[] getSequence(String s) {
        String[] strings = s.split(" ");
        double[] sequence = new double[strings.length];
        for (int i = 0; i < strings.length; i++) {
            sequence[i] = Double.valueOf(strings[i]);
        }
        return sequence;
    }

    private double[] scaleSequence(double[] sequence, double scale) {
        for (int i = 0; i < sequence.length; i++) {
            sequence[i] = (sequence[i]/scale);
        }
        return sequence;
    }

    private double findMax(double[] sequence) {
        double max = 0;
        for (int i = 0; i < sequence.length; i++) {
            if (Math.abs(sequence[i]) > max) {
                max = Math.abs(sequence[i]);
            }
        }
        return max;
    }

    public static void main(String[] args) {
        new Main();
    }

}
