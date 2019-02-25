import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class Day3 {

    public static void main(String[] args) {
        final ArrayList<Claim> claims = parseDay3Input(args[0]);

        Matrix<Boolean> fabric = new Matrix<>();
        System.out.println(solutionStar1(fabric, claims));

        Matrix<Integer> fabric3 = new Matrix<>();
        System.out.println(solutionStar2(fabric3, claims));
    }

    public static int solutionStar1(final Matrix<Boolean> fabric, final ArrayList<Claim> claims) {
        return squaredOverlap(fabric, claims);
    }


    public static int solutionStar2(final Matrix<Integer> fabric, final ArrayList<Claim> claims) {
        HashSet<Integer> overlappingIds = new HashSet<>();
        resolveClaims(fabric, overlappingIds, claims);
        return uniqueId(overlappingIds, claims);
    }

    public static int squaredOverlap(final Matrix<Boolean> fabric, final ArrayList<Claim> claims) {
        int squaredOverlap = 0;
        for (Claim claim : claims) {
            for (int i = 1 + claim.x; i <= claim.width + claim.x; i++) {
                for (int j = 1 + claim.y; j <= claim.height + claim.y; j++) {
                    if (fabric.empty(j, i)) {
                        fabric.insert(j, i, false);
                    } else if (!fabric.get(j, i)) {
                        fabric.insert(j, i, true);
                        squaredOverlap++;
                    }

                }
            }
        }
        return squaredOverlap;
    }


    public static void resolveClaims(final Matrix<Integer> fabric, final HashSet<Integer> overlappingIDs, final ArrayList<Claim> claims) {
        for (Claim claim : claims) {
            for (int i = 1 + claim.x; i <= claim.width + claim.x; i++) {
                for (int j = 1 + claim.y; j <= claim.height + claim.y; j++) {
                    if (!fabric.empty(j, i)) {
                        overlappingIDs.add(fabric.get(j, i));
                        overlappingIDs.add(claim.id);
                    } else {
                        fabric.insert(j, i, claim.id);
                    }
                }
            }
        }
    }

    public static int uniqueId(final HashSet<Integer> overlappingIDs, final ArrayList<Claim> claims) {
        int uniqueId = -1;
        for (Claim claim : claims) {
            if (!overlappingIDs.contains(claim.id)) {
                uniqueId = claim.id;
            }
        }

        return uniqueId;
    }

    public static ArrayList<Claim> parseDay3Input(final String file) {
        String[] rawInput = IOutilities.readFileInputAsArray(file);
        Pattern pattern = Pattern.compile("#(\\d*) @ (\\d*),(\\d*): (\\d*)x(\\d*)");
        return Arrays.stream(rawInput).map(pattern::matcher).map(Claim::new).collect(Collectors.toCollection(ArrayList::new));
    }

    private static class Claim {
        int id;
        int x;
        int y;
        int width;
        int height;

        private Claim(Matcher matcher) {
            matcher.find();
            id = Integer.parseInt(matcher.group(1));
            x = Integer.parseInt(matcher.group(2));
            y = Integer.parseInt(matcher.group(3));
            width = Integer.parseInt(matcher.group(4));
            height = Integer.parseInt(matcher.group(5));
        }
    }
}
