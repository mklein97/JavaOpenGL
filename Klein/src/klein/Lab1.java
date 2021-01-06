package klein;
import java.util.Random;

/**
 *
 * @author Matt
 */
import java.util.Random;
public class Lab1 {
    public static void main(String[] args)
    {
        Random rand = new Random();
//Part One
        int randomNumber = rand.nextInt(20) + 5;
System.out.println("+----------------+");
        int counter = 0;
        while(counter < randomNumber) {
            System.out.println("|##..##..##..##..|\n|##..##..##..##..|");
            counter++;
            if (counter < randomNumber) {
                System.out.println("|..##..##..##..##|\n|..##..##..##..##|");
                counter++;
            }
        }
System.out.println("+----------------+");
//Part Two
        int[] randomArray = new int[rand.nextInt(50)+5];
for (int i = 0; i < randomArray.length; i++) {
            randomArray[i] = rand.nextInt(1000);
        }
//average
        float average = sort_ints(randomArray);
        System.out.printf("Average: %.1f\n", average);
//standard deviation
        float newAverage = 0;
        for(int item : randomArray)
            newAverage += (item - average)*(item - average);
        newAverage /= randomArray.length;
        double standardDeviation = Math.sqrt(newAverage);
        System.out.printf("Standard Deviation: %.5f\n", standardDeviation);
    }
public static float sort_ints(int[] randomArray){
        //sorting
        boolean sorted = false;
        while (!sorted){
            int length = randomArray.length;
            sorted = true;
            for(int i = 0; i < length-1; i++){
                if (randomArray[i] > randomArray[i + 1]){
                    int itemOne = randomArray[i];
                    int itemTwo = randomArray[i+1];
                    randomArray[i] = itemTwo;
                    randomArray[i+1] = itemOne;
                    sorted = false;
                }
            }
            length--;
        }
//numbers
        for(int item: randomArray)
            System.out.println(item);
//maximum
        System.out.println("Minimum: " + randomArray[0]);
//minimum
        System.out.println("Maximum: " + randomArray[randomArray.length-1]);
//median
        System.out.println("Median: " + randomArray[(randomArray.length-1)/2]);
float average = 0.0f;
        for(int item : randomArray){
            average += item;
        }
        average /= randomArray.length;
return average;
    }
}
