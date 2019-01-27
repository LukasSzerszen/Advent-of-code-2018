import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day2 {
    private static int twoLetterIDs = 0;
    private static int threeLetterIDs = 0;

    public static void main(String[] args){
        String[] input = IOutilities.readFileInputAsArray(args[0]);
        System.out.println(star1Solution(input));
    }

    public static int star1Solution(final String[] boxIds){
        countTwoOrThreeLetterIDs(boxIds);
        return checksum();
    }

    public static void countTwoOrThreeLetterIDs(final String[] boxIDs){
        for (String boxID: boxIDs){
            List<Character> boxIdChars = stringToCharacterList(boxID);
            HashSet<Character> characterHashSet = new HashSet<>();
            boolean hasTwoLetter = false;
            boolean hasThreeLetter = false;
            for (char c : boxIdChars){
                if(!characterHashSet.contains(c)){
                    int freq = Collections.frequency(boxIdChars,c);
                    if(freq == 2 && !hasTwoLetter){
                        twoLetterIDs++;
                        characterHashSet.add(c);
                        hasTwoLetter = true;
                    }else if(freq == 3 && !hasThreeLetter){
                        threeLetterIDs++;
                        characterHashSet.add(c);
                        hasThreeLetter = true;
                    }
                }
            }
        }
    }

    public static int checksum(){
        return twoLetterIDs * threeLetterIDs;
    }

    public static List<Character> stringToCharacterList(final String str){
        return str.chars().mapToObj(e -> (char)e).collect(Collectors.toList());
    }




}
