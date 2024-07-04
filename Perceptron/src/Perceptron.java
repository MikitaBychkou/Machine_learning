import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Perceptron {

    private final HashMap<double[], Integer> trainData;
    private final double learningConst;
    private int vectorSize;
    private double[] weights;
    private double deviation = Math.random();

    Perceptron(String trainDataPath, double learningConst, int liczbaEpok) throws IOException {
        this.trainData = getNewDataMap(trainDataPath);
        this.learningConst = learningConst;
        this.weights = new double[vectorSize];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = Math.random();
        }
        for (int i = 0; i < liczbaEpok; i++) {
            perceptronLearning();
        }
    }


    public String classifyData(String dataPath) throws IOException {
        HashMap<double[], Integer> testData = getNewDataMap(dataPath);
        int classifyCounter = 0;
        for (Map.Entry<double[], Integer> entry : testData.entrySet()){
            Integer outputValue = getOutputValue(entry.getKey());
            if(outputValue.equals(entry.getValue()))
                classifyCounter++;
        }
        double accuracy = (((double) classifyCounter)/testData.size()) * 100;
        return "accuracy: " + accuracy + "%";
    }


    public int getOutputValue(double[] vector){
        double net = 0;
        for (int i = 0; i < vector.length; i++) {
            net += vector[i] * weights[i];
        }
        net -= deviation;
        return net >= 0 ? 1 : 0;
    }


    private void perceptronLearning(){
        for (Map.Entry<double[], Integer> entry : trainData.entrySet()){
            int outputValue = getOutputValue(entry.getKey());
            updateWeights(entry.getKey(), entry.getValue(), outputValue);
            updateDeviation(entry.getValue(), outputValue);
        }
    }

    private void updateWeights(double[] vector, int expectedValue, int outputValue){
        double[] newWeight = new double[weights.length];
        double tmp = learningConst * (expectedValue - outputValue);
        for (int i = 0; i < newWeight.length; i++) {
            newWeight[i] = weights[i] + tmp * vector[i];
        }
        weights = newWeight;
    }

    private void updateDeviation(int expectedValue, int outputValue){
        deviation -= learningConst * (expectedValue - outputValue);
    }


    private HashMap<double[], Integer> getNewDataMap(String dataPath) throws IOException {
        HashMap<double[], Integer> newMap = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(dataPath));
        String line1;
        line1 = reader.readLine();
        line1 = reader.readLine();
        String[] lineInArr1 = line1.split(",");
        vectorSize = lineInArr1.length-1;
        String nameOfClass=lineInArr1[lineInArr1.length-1];

        String line;
        while((line = reader.readLine()) != null){
            String[] lineInArr = line.split(",");
            int expectedValues;
            if (lineInArr[lineInArr.length-1].equals(nameOfClass))
                expectedValues = 0;
            else
                expectedValues = 1;
            double[] vektor = new double[lineInArr.length-1];

            for (int i = 0; i < vektor.length; i++) {
                vektor[i] = Double.parseDouble(lineInArr[i]);
            }
            if (vektor.length==vectorSize) {
                newMap.put(vektor, expectedValues);
            }
        }
        return newMap;
    }

}
