import java.util.*;
import java.util.stream.Collectors;

public class Day2 {
    private static int twoLetterIDs = 0;
    private static int threeLetterIDs = 0;
    private static HashSet<String> validIDs = new HashSet();

    public static void main(String[] args) {
        String[] input = IOutilities.readFileInputAsArray(args[0]);
        System.out.println(star1Solution(input));
        System.out.println(star2Solution());
    }

    public static int star1Solution(final String[] boxIds) {
        countTwoOrThreeLetterIDs(boxIds);
        return checksum();
    }

    public static String star2Solution(){
        //required star1Solution to have been run such that validIDs is populated
        return commonChars();
    }


    public static void countTwoOrThreeLetterIDs(final String[] boxIDs) {
        for (String boxID : boxIDs) {
            List<Character> boxIdChars = stringToCharacterList(boxID);
            boolean hasTwoLetter = false;
            boolean hasThreeLetter = false;
            for (char c : boxIdChars) {
                int freq = Collections.frequency(boxIdChars, c);
                if (freq == 2 && !hasTwoLetter) {
                    twoLetterIDs++;
                    hasTwoLetter = true;
                    validIDs.add(boxID);
                } else if (freq == 3 && !hasThreeLetter) {
                    threeLetterIDs++;
                    hasThreeLetter = true;
                    validIDs.add(boxID);
                }
            }
        }
    }

    public static String commonChars(){
        for(String boxID : validIDs){
            for(String comparingID: validIDs){
                if(boxID.equals(comparingID)){
                    continue; //the same id
                }
                String diffCharsBoxID = boxID.replaceAll( "[" + comparingID + "]", "");
                String diffCharsCompareID = comparingID.replaceAll("[" + boxID + "]", "");
                if(diffCharsBoxID.length() == 1 && diffCharsCompareID.length() == 1){
                    int boxIDIndex =boxID.indexOf(diffCharsBoxID);
                    int comparingIDIndex = comparingID.indexOf(diffCharsCompareID);
                    if(boxIDIndex == comparingIDIndex){
                        return boxID.replaceAll("[" + diffCharsBoxID + "]", "");
                    }
                }
            }
        }
        return "NO ID";
    }

    public static int checksum() {
        return twoLetterIDs * threeLetterIDs;
    }

    public static List<Character> stringToCharacterList(final String str) {
        return str.chars().mapToObj(e -> (char) e).collect(Collectors.toList());
    }


}
