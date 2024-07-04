import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Perceptron perceptron = null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Wprowadz imie pliku treningowego:");
        String trainDataPath=scanner.nextLine();
        System.out.println("Wprowadz imie pliku testowego:");
        String testDataPath=scanner.nextLine();
        System.out.println("Wprowadz stała uczenia:");
        double learningConst=scanner.nextDouble();
        System.out.println("Wprowadz liczbe epok:");
        int liczbaEpok = scanner.nextInt();

        try {
//            "training_Iris.txt"
            perceptron = new Perceptron(trainDataPath, learningConst, liczbaEpok);
//            "test_Iris.txt"
            String accuracy = perceptron.classifyData(testDataPath);
            System.out.println(accuracy);
        } catch (IOException e) {e.printStackTrace();}


        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Zeby zakonczyc sesje nacisnij ENTER!");
            while(true){
                System.out.print("Podaj nowy vektor:\t");
                String lineVek = sc.nextLine();
                if(lineVek.equals(""))
                    System.exit(0);

                String[] lineInArr = lineVek.split(",");
                double[] vektor = new double[lineInArr.length];
                for (int i = 0; i < vektor.length; i++) {
                    vektor[i] = Double.parseDouble(lineInArr[i]);
                }
                int outputValue = perceptron.getOutputValue(vektor);
                String atrybut1="Iris-versicolor";
                String atrybut2="Iris-virginica";

                String decisionAttribute = atrybut1;
                if (outputValue == 1)
                    decisionAttribute = atrybut2;
                System.out.println("Atrybut decyzyjny dla vektora " + lineVek + ":\t" + decisionAttribute);
            }
        }catch (Exception e){
            System.out.println("Podany vektor jest w nieprawidlowym formacie");
        }
    }

}
