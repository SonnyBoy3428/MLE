import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.DeflaterOutputStream;

public class GeneticProgramming {
    private static final int PROGRAM_AMOUNT = 100;
    private static final double REPLACEMENT_RATE = 0.0;
    private static final double MUTATION_RATE = 0.2;
    private static final int PROGRAMM_LENGTH = 1000;
    private static final int GENERATIONS_COUNT = 1000;

    public static ArrayList<VM_neu> initAll(){
        ArrayList<VM_neu> programs = new ArrayList<>();

        for(int i = 0; i < PROGRAM_AMOUNT; i++){
            VM_neu vm = new VM_neu();
            init(vm);
            programs.add(vm);
        }

        return programs;
    }

    public static void init(VM_neu vm){
        int[] program = new int[PROGRAMM_LENGTH];

        for(int i = 0; i < PROGRAMM_LENGTH; i++){
            program[i] = (int) (Math.random()*8);
        }

        vm.setMemAndResizeMAX(program);

        int n = (int)(Math.random() * 100 + 1);
        for(int i = 0; i < PROGRAMM_LENGTH; i++){
            vm.stack[i] = n;
        }
    }

    public static void simulateAll(ArrayList<VM_neu> vms){
        for(VM_neu vm : vms){
            resetcounters(vm);
            vm.simulate();
        }
    }

    public static void resetcounters(VM_neu vm){
        vm.sp = 0;
        vm.pc = 0;
        vm.reg = 0;
        vm.primeNumbers = new ArrayList();
    }

    public static void main(String[] args){
        ArrayList<VM_neu> programs = initAll();
        int cycles = 1;
        int indexOfBestProgram;
        int fitnessOfBestProgram;
        VM_neu bestProgram;
        int lastBestFitness = 0;

        do{
            simulateAll(programs);
            ArrayList<Integer> fitness = fitnessAll(programs);
            indexOfBestProgram = getIndexOfBestProgram(fitness);
            bestProgram = programs.get(indexOfBestProgram);
            fitnessOfBestProgram = fitness(bestProgram);

            ArrayList<Double> probabilities = calculateProbabilites(fitness);
            ArrayList<VM_neu> newPrograms = new ArrayList<>();

            selection(programs, newPrograms, probabilities, indexOfBestProgram);
            mutation(newPrograms);

            if (fitnessOfBestProgram > lastBestFitness) {
                System.out.println("Generation " + cycles);
                System.out.println("Fitness: " + fitnessOfBestProgram);
                ArrayList primeNumbers = bestProgram.getPrimeNumbers();
                Collections.sort(primeNumbers);
                System.out.println(primeNumbers.toString());
                lastBestFitness = fitnessOfBestProgram;
            }

            programs = newPrograms;
            cycles++;
        }while(cycles < GENERATIONS_COUNT);

        System.out.println("------End of evolution------");
    }

    public static void selection(ArrayList<VM_neu> population, ArrayList<VM_neu> newGeneration, ArrayList<Double> probabilities, int indexBestProgram){
        boolean copiedBestProgram = false;

        int replacementAmount = (int)((1 - REPLACEMENT_RATE) * PROGRAM_AMOUNT);
        int index = selectIndex(probabilities);

        for(int i = 0; i < replacementAmount; i++){
            if(index == indexBestProgram){
                copiedBestProgram = true;
            }

            newGeneration.add(population.get(index));
        }

        if(copiedBestProgram){
            newGeneration.add(population.get(index));
        }else {
            newGeneration.add(population.get(indexBestProgram));
        }

    }

    public static int selectIndex(ArrayList<Double> probabilities){
        int index = (int)(Math.random()*(probabilities.size()-1));
        double randomNum = Math.random();
        double sum = 0;
        do{
            index++;
            index %= probabilities.size();
            sum += probabilities.get(index);
        }while(sum < randomNum);

        return index;
    }

    public static ArrayList<Double> calculateProbabilites(ArrayList<Integer> fitness){
        ArrayList<Double> probabilities = new ArrayList<>();
        int allFitness = calculateAllFitness(fitness);
        for(int i = 0; i < fitness.size(); i++){
            probabilities.add(calculateProbability(fitness.get(i), allFitness));
        }
        return probabilities;
    }

    public static int calculateAllFitness(ArrayList<Integer> fitness){
        int sum = 0;
        for(int i = 0; i < fitness.size(); i++){
            sum += fitness.get(i);
        }

        return sum;
    }

    public static double calculateProbability(int fitness, int allFitness){
        return (fitness*1./allFitness);
    }

    public static void mutation(ArrayList<VM_neu> newPrograms){
        int mutationAmount = (int)(MUTATION_RATE * newPrograms.size());

        ArrayList<Integer> fitness = fitnessAll(newPrograms);
        int indexBestProgram = getIndexOfBestProgram(fitness);

        ArrayList<Integer> indices = new ArrayList<>();
        for(int i = 0; i < mutationAmount; i++){
            int index = (int) (Math.random()*newPrograms.size());

            if(!indices.contains(index)){
                indices.add(index);
                VM_neu mutadedProgram = mutateProgram(newPrograms.get(index));

                if(index == indexBestProgram){
                    int mutatedFitness = fitness(mutadedProgram);
                    int individualFitness = fitness(newPrograms.get(indexBestProgram));

                    if(mutatedFitness > individualFitness){
                        newPrograms.set(index, mutadedProgram);
                    }
                }else{
                    newPrograms.set(index, mutadedProgram);
                }
            }else{
                i--;
            }
        }
    }

    public static ArrayList<Integer> fitnessAll(ArrayList<VM_neu> newPrograms){
        ArrayList<Integer> fitness = new ArrayList<>();

        for(VM_neu vm : newPrograms){
            fitness.add(fitness(vm));
        }

        return fitness;
    }

    public static int fitness (VM_neu vm){
        return vm.getPrimeNumbers().size();
    }

    public static int getIndexOfBestProgram(ArrayList<Integer> fitness){
        int index = 0;
        int max = 0;
        for(int i = 0; i < fitness.size(); i++){
            if(max < fitness.get(i)){
                max = fitness.get(i);
                index = i;
            }
        }

        return index;
    }

    public static VM_neu mutateProgram(VM_neu vm){
        VM_neu mutatedProgram = new VM_neu();
        mutatedProgram.mem = vm.mem.clone();

        int index = (int)(Math.random()*mutatedProgram.mem.length);
        mutatedProgram.mem[index] = (int)(Math.random()*PROGRAMM_LENGTH);

        return mutatedProgram;
    }
}
