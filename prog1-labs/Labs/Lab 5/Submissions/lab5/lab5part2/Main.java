import java.util.HashSet;
import java.util.Iterator;

//the main method creates two word groups
//separates each word, stores them in an array
//and outputs them using a for loop
//it also creates a set of words from both group
//and outputs it
public class Main {
    public static void main (String[] args) {
        //creating two new WordGroup objects and initialising them with the test strings
        WordGroup plato = new WordGroup("You can discover more about a person in an hour of play than in a year of conversation");
        WordGroup roosevelt = new WordGroup("When you play play hard when you work dont play at all");

        //creating two new string arrays to hold the split up test strings
        String[] platoArray = plato.getWordArray();
        String[] rooseveltArray = roosevelt.getWordArray();

        //looping through the array containing the plato quote
        for (int i = 0; i < platoArray.length; i++) {
            System.out.println(platoArray[i]);
        }

        //looping through the array containing the roosevelt quote
        for (int i = 0; i < rooseveltArray.length; i++) {
            System.out.println(rooseveltArray[i]);
        }

        //creating a new set which is a combination of words
        //from the two groups
        HashSet<String> combinedSet = plato.getWordSet(roosevelt);

        //creating an iterator to look through the new set
        Iterator<String> setIt = combinedSet.iterator();

        //outputting each element in the set
        while (setIt.hasNext()) {
            String currentWord = setIt.next();

            System.out.println(currentWord);
        }
    }
}