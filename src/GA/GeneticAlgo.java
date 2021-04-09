package GA;

import titan.*;
import java.util.ArrayList;
import java.util.Random;

public class GeneticAlgo {

    ArrayList<ArrayList<Individual>> populationGenerations = new ArrayList<>();

    private final double MUTATION_RATE = 0.1;

    private void getBestProbe(){
        makeGen1(100);

        System.out.println("gen1 created");

        double fittest = 0;
        for(Individual i : populationGenerations.get(0)){
//            System.out.println("indiv p = "+i.getInitPosition().toString());
//            System.out.println("indiv v = "+i.getInitVelocity().toString());
//            System.out.println();
            i.calcFitness();
            if(i.getFitness() > fittest)
                fittest = i.getFitness();
        }

        System.out.println("fittest in gen1 = "+fittest);

//        for(Individual i : populationGenerations.get(1)) {
//            System.out.println("indiv' p = " + i.getInitPosition().toString());
//            System.out.println("indiv' v = " + i.getInitVelocity().toString());
//            System.out.println();
//        }

        System.out.println("while loop entered");
        while(!solutionFound()){
            makeNewGeneration();
            System.out.println("new gen created");
            System.out.println();
        }
        ArrayList<Individual> lastGen = populationGenerations.get(populationGenerations.size()-1);
        for(Individual i : lastGen){
            if(i.getFitness() == 1) {
                System.out.println("found ittt");
                System.out.println(i.getInitPosition());
                System.out.println(i.getInitVelocity());
            }
        }
    }

    private void makeNewGeneration(){
        ArrayList<Individual> lastGen = populationGenerations.get(populationGenerations.size()-1);
        ArrayList<Individual> newGen = new ArrayList<>();

        Individual fittestInd = lastGen.get(0);

        for(Individual i : lastGen){
            Individual[] parents = parentSelection(lastGen);// get parents
//            System.out.println("parent 1 p = "+parents[0].getInitPosition().toString());
//            System.out.println("parent 1 v = "+parents[0].getInitVelocity().toString());
//            System.out.println("parent 2 p = "+parents[1].getInitPosition().toString());
//            System.out.println("parent 2 v = "+parents[1].getInitVelocity().toString());

//
//            System.out.println("new kid p = "+newKid.getInitPosition().toString());
//            System.out.println("new kid v = "+newKid.getInitVelocity().toString());

            Individual newKid = mutate(crossover(parents[0], parents[1]), MUTATION_RATE);
//            System.out.println("mutated kid p = "+newKid.getInitPosition().toString());
//            System.out.println("mutated new kid v = "+newKid.getInitVelocity().toString());
//            System.out.println();
            newGen.add(newKid);
            newKid.calcFitness();
            if(newKid.getFitness() > fittestInd.getFitness())
                fittestInd = newKid;
        }



        System.out.println("fittest pv = "+ fittestInd.getInitPosition());
        System.out.println("fittest pv = "+ fittestInd.getInitVelocity());
        System.out.println("minimum distance = "+fittestInd.getMinDistance());
        System.out.println();
        populationGenerations.add(newGen);
    }

    private void makeGen1(int initPopulation){
        populationGenerations.add(new ArrayList<>());
        for(int i = 0; i < initPopulation; i++)
            populationGenerations.get(0).add(new Individual());
    }

    private boolean solutionFound(){
        ArrayList<Individual> lastGen = populationGenerations.get(populationGenerations.size()-1);
        for(Individual i : lastGen){
            if(i.getFitness() == 1)
                return true;
        }
        return false;
    }// check if fitness are good enough to terminate

    private Individual[] parentSelection(ArrayList<Individual> population){
        Individual[] parents = new Individual[2];
        ArrayList<Double> probabilities = new ArrayList<>();

        double sum = 0;
        for(Individual i : population){
            sum += i.getFitness();
        }
        double sumOfProbabilities = 0;
        probabilities.add(sumOfProbabilities);

        for(Individual i : population){
            double probabilityOfIndividual = (i.getFitness()/sum);
            sumOfProbabilities+=probabilityOfIndividual;
            probabilities.add(sumOfProbabilities);
        }

//        for(double d : probabilities)
//            System.out.print(d+", ");
//        System.out.println();

        for(int parentNum = 0; parentNum<2; parentNum++){
            double wheelPoint = Math.random();
//            System.out.println("wheelpoint = "+wheelPoint);
            for(int i = 0; i < probabilities.size(); i++){
                if(probabilities.get(i) < wheelPoint){
                    if(i != probabilities.size()-1){
                        if(wheelPoint < probabilities.get(i+1))
                            parents[parentNum] = population.get(i);
                    }
                    else
                        parents[parentNum] = population.get(i);
                }
            }
        }
        return parents;
    }// returns selected parents (roulette wheel)

    private Individual crossover(Individual parent1, Individual parent2) {

        Vector3dInterface offspringPV;
        Vector3dInterface offspringVV;

        int rand1 = new Random().nextInt(2);
        int rand2 = new Random().nextInt(2);

        if(rand1 == 0)
            offspringPV = parent1.getInitPosition();
        else
            offspringPV = parent2.getInitPosition();
        if(rand2 == 0)
            offspringVV = parent1.getInitVelocity();
        else
            offspringVV = parent2.getInitVelocity();

        return new Individual(offspringPV, offspringVV);

    }

    private Individual mutate(Individual individual, double mutationRate){
        double random = Math.random();
//        System.out.println("random for mutation = "+random);
        if(random < mutationRate)
        {
//            System.out.println("mutates");
            int geneToMutate = new Random().nextInt(6);
            if(geneToMutate == 0)
                individual.getInitPosition().setX(individual.getInitPosition().getX()*Math.random());
            else if(geneToMutate == 1)
                individual.getInitPosition().setY(individual.getInitPosition().getY()*Math.random());
            else if(geneToMutate == 2)
                individual.getInitPosition().setZ(individual.getInitPosition().getZ()*Math.random());
            else if(geneToMutate == 3)
                individual.getInitVelocity().setX(individual.getInitVelocity().getX()*Math.random());
            else if(geneToMutate == 4)
                individual.getInitVelocity().setY(individual.getInitVelocity().getY()*Math.random());
            else individual.getInitVelocity().setZ(individual.getInitVelocity().getZ()*Math.random());
        }
//        System.out.println("doesnt mutate");
        return individual;
    }

    public static void main(String[] args) {
        GeneticAlgo ga = new GeneticAlgo();
        ga.getBestProbe();
    }

    public GeneticAlgo(){ }

}

