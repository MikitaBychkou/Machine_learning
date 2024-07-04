import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
	private static ArrayList<double[]> trainData = new ArrayList<>();
	private static ArrayList<String> trainLabels = new ArrayList<>();
	private static int K=0;

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Podaj ścieżke do pliku treningowego: ");
		String trainPath = scanner.nextLine();

		readFile(trainPath);

		while (true) {
			System.out.println("\n\nWybierz jedna z czterech opcji:" +
					" \n a) Klasyfikacja wszystkich obserwacji ze zbioru testowego" +
					" \n b) Klasyfikacja podanej obserwacji" +
					" \n c) Zmien K" +"\n wartosc K teraz: "+K+
					" \n d) Wyjscie z programu");
			String option = scanner.nextLine();

			switch (option) {
				case "a":
					System.out.println("Podaj ścieżkę do pliku testowego:");
					String testPath = scanner.nextLine();
					classifyTest(testPath);
					break;

				case "b":
					classifyUserData();
					break;
				case "c":
					System.out.println("Podaj nowa wartosc K:");
					K = scanner.nextInt();
					scanner.nextLine();
					break;
				case "d":
					return;
				default:
					System.out.println("wprowadz: 'a' lub 'b' lub 'c' lub 'd'");
			}
		}
	}

	private static void readFile(String path) {
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {

			String str;

			while ((str= br.readLine()) != null){
				String[] parts= str.split(",");
				double[] features = new double[parts.length - 1];
				for (int i = 0; i< parts.length - 1; i++) {
					features[i] = Double.parseDouble(parts[i]);
				}
					trainData.add(features);
					trainLabels.add(parts[parts.length - 1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void classifyUserData() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Podaj dane obserwacje oddzielone przecinkami: ");
		String userInput = scanner.nextLine();
		String[] userFeatures = userInput.split(",");
		double[] features =new double[userFeatures.length];
		for (int i = 0; i < userFeatures.length; i++) {
			features[i] = Double.parseDouble(userFeatures[i]);
		}

		String label = classify(features);
		System.out.println("Przewidywana etykieta dla podanej obserwacji: "+ label);
	}

	private static String classify(double[] features) {
		ArrayList<String> nearestLabels = findNearestLabels(features);


		Map<String, Integer> count = new LinkedHashMap<>();
		for (String label : nearestLabels) {
			count.put(label, count.getOrDefault(label, 0) + 1);
		}

		int maxCount = -1;
		String finalLabel = "";
		for (Map.Entry<String, Integer> entry : count.entrySet()) {
			if (entry.getValue() > maxCount) {
				finalLabel = entry.getKey();
				maxCount = entry.getValue();
			}
		}
		return finalLabel;
	}

	private static ArrayList<String> findNearestLabels(double[] features) {
		ArrayList<Double> distances = new ArrayList<>();
		for (double[] trainFeatures : trainData) {
			distances.add(findEuclideanDistance(trainFeatures, features));
		}

		ArrayList<String> nearestLabels = new ArrayList<>();
		for (int i = 0; i < K; i++) {
			int minIndex = distances.indexOf(Collections.min(distances));
			nearestLabels.add(trainLabels.get(minIndex));
			distances.set(minIndex, Double.MAX_VALUE);
		}
		return nearestLabels;
	}

	private static double findEuclideanDistance(double[] train, double[] test) {
		double sum = 0;
		for (int i = 0; i< train.length; i++) {
			sum += (train[i] - test[i])*(train[i] - test[i]);
		}
		return sum;
	}

	private static void classifyTest(String testPath) {
		ArrayList<String> predictedLabels = new ArrayList<>();
		ArrayList<String> firstLabels = new ArrayList<>();

		 try (BufferedReader br=new BufferedReader(new FileReader(testPath))){

			String str;
			while ((str = br.readLine()) != null) {
				String[] parts = str.split(",");
				double[] features = new double[parts.length- 1];
				for (int i = 0; i< parts.length-1; i++) {
					features[i]= Double.parseDouble(parts[i]);
				}
				String firstLabel = parts[parts.length - 1];
				firstLabels.add(firstLabel);

				String predictedLabel = classify(features);
				predictedLabels.add(predictedLabel);
				System.out.println("Obserwacja: " +Arrays.toString(features)+ "-> Przewidywana etykieta: " + predictedLabel);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		int countCorrectPredictions = 0;
		for (int i = 0; i < firstLabels.size(); i++) {
			if (firstLabels.get(i).equals(predictedLabels.get(i))) {
				countCorrectPredictions++;
			}
		}
		double percent = (double)100 * countCorrectPredictions / firstLabels.size();
		System.out.println("Dokladnosc: "+ percent+"%");
	}
}


