import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.lang.Byte;

/**
 * Klasse, welche den genetischen Algorithmus repräsentiert
 */
public class genetischerAlgorithmus {
    /**
     * Wichtige Parameter für Individuenlänge, Populationsgröße, Ersetzungsmenge und Mutationsrate
     */
    private static final int individualLength = 5;
    private static final int populationSize = 20;
    private static final int substitutionSite = 10;
    private static final byte mutationRate = 10;

    private static final Random random = new Random();

    /**
     * Hauptprogramm
     * @param args Kommandozeilenargumente
     */
    public static void main(String[] args){
        byte[] optimusString = generateOptimusBitString();

        List<byte[]> population = generatePopulation();

        List<Byte> fitnessList = calculateFitness(population, optimusString);
    }

    /**
     * Generiert den Optimus-Bit-String
     * @return Optimus-Bit-String
     */
    private static byte[] generateOptimusBitString(){
        byte[] optimus = new byte[individualLength];

        for(int i = 0; i < individualLength; i++){
            optimus[i] = (byte)(random.nextInt(1));
        }

        return optimus;
    }

    /**
     * Generiert die Startpopulation
     * @return Startpopulation
     */
    private static List<byte[]> generatePopulation(){
        List<byte[]> population = new ArrayList<byte[]>();

        for(int i = 0; i < populationSize; i++){
            byte[] individual = new byte[individualLength];

            for(int j = 0; j < individualLength; j++){
                individual[j] = (byte)(random.nextInt(1));
            }

            population.add(individual);
        }

        return population;
    }

    /**
     * Calculates fitness for all individuals
     * @param population Population
     * @param optimusString Optimus-Bit-String
     * @return List with fitness for all individuals
     */
    private static List<Byte> calculateFitness(List<byte[]> population, byte[] optimusString){
        List<Byte> fitnessList = new ArrayList<Byte>();

        for(int i = 0; i < population.size(); i++){
            byte[] individual = population.get(i);

            byte fitness = compareIndividualToOptimum(individual, optimusString);

            fitnessList.add(i, fitness);
        }

        return fitnessList;
    }

    /**
     * Compares an individual to the optimusString in order to calculate fitness
     * @param individual Individual string
     * @param optimusString Optimus-Bit-String
     * @return Fitness
     */
    private static byte compareIndividualToOptimum(byte[] individual, byte[] optimusString){
        byte fitness = 0;

        for(int i = 0; i < individualLength; i++){
            if(individual[i] == optimusString[i]){
                fitness++;
            }
        }

        return fitness;
    }

    private static List<byte[]> select(int individualCount, List<byte[]> population){


    }

    /**
     * Mutates individuals
     *
     * @return Mutated individuals
     */
    private static List<byte[]> mutate(List<byte[]> toMutate){
        for(byte[] individual : toMutate){
            int randomIndex = random.nextInt(individualLength-1);

            if(individual[randomIndex] == 0){
                individual[randomIndex] = 1;
            } else if(individual[0] == 1){
                individual[randomIndex] = 0;
            }
        }

        return toMutate;
    }

    private static void genetischerAlgorithmus(){

    }
}
