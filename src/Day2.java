import java.util.*;
import java.util.stream.Collectors;

public class Day2 {
    private static int twoLetterIDs = 0;
    private static int threeLetterIDs = 0;

    public static void main(String[] args) {
        String[] input = IOutilities.readFileInputAsArray(args[0]);
        System.out.println(star1Solution(input));
        System.out.println(star2Solution(input));
    }

    public static int star1Solution(final String[] boxIds) {
        countTwoOrThreeLetterIDs(boxIds);
        return checksum();
    }

    public static String star2Solution(String[] input){
        return commonChars(input).orElse("No ID was found");
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
                } else if (freq == 3 && !hasThreeLetter) {
                    threeLetterIDs++;
                    hasThreeLetter = true;
                }
            }
        }
    }

    public static Optional<String> commonChars(final String[] boxIDs){
        for(String boxID : boxIDs){
            for(String comparingID: boxIDs){
                int index = -1;
                int diff = 0;
                for(int i = 0; i< boxID.length(); i++){
                    if(boxID.charAt(i) != comparingID.charAt(i)){
                        diff++;
                        index = i;
                    }
                }
                if(diff == 1){
                    StringBuilder builder = new StringBuilder(boxID);
                   return Optional.of(builder.deleteCharAt(index).toString());
                }

            }
        }
        return Optional.empty();
    }

    public static int checksum() {
        return twoLetterIDs * threeLetterIDs;
    }

    public static List<Character> stringToCharacterList(final String str) {
        return str.chars().mapToObj(e -> (char) e).collect(Collectors.toList());
    }


}
