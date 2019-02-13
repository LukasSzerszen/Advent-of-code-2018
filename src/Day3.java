import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Day3 {

    public static void main(String[] args) {
        final ArrayList<Claim> claims = parseDay3Input(args[0]);

        Matrix<Boolean> fabric = new Matrix<>();
        System.out.println(solutionStar1(fabric, claims));

        Matrix<HashSet<Integer>> fabric2 = new Matrix<>();
        System.out.println(solutionStar2(fabric2, claims));
    }

    public static int solutionStar1(final Matrix<Boolean> fabric, final ArrayList<Claim> claims) {
        return squaredOverlap(fabric, claims);
    }

    public static int solutionStar2(final Matrix<HashSet<Integer>> fabric, final ArrayList<Claim> claims) {
        populateMatrix(fabric,claims);
        final HashSet<Integer> overlappingIDs = new HashSet<>();
        return uniqueID(fabric,overlappingIDs,claims);
    }

    public static int squaredOverlap(final Matrix<Boolean> fabric, final ArrayList<Claim> claims) {
        int squaredOverlap = 0;
        for (Claim claim : claims) {
            int topPadding = claim.topPadding;
            int leftPadding = claim.leftPadding;
            for (int i = 1; i <= claim.width; i++) {
                for (int j = 1; j <= claim.length; j++) {
                    if (fabric.empty(topPadding + j, leftPadding + i)) {
                        fabric.insert(topPadding + j, leftPadding + i, false);
                    } else if (!fabric.get(topPadding + j, leftPadding + i)) {
                        fabric.insert(topPadding + j, leftPadding + i, true);
                        squaredOverlap++;
                    }

                }
            }
        }

        return squaredOverlap;
    }

    public static int uniqueID(final Matrix<HashSet<Integer>> fabric, final ArrayList<Claim> claims) {
        for (Claim claim : claims) {
            int topPadding = claim.topPadding;
            int leftPadding = claim.leftPadding;
            for (int i = 1 + leftPadding; i <= claim.width + leftPadding; i++) {
                for (int j = 1 + topPadding; j <= claim.length + topPadding; j++) {
                    if (fabric.empty(j, i)) {
                        HashSet<Integer> idSet = new HashSet<>();
                        idSet.add(claim.id);
                        fabric.insert(j, i, idSet);
                    } else {
                        fabric.get(j, i).add(claim.id);
                    }
                }
            }
        }

        HashSet<Integer> overlappingIDs = new HashSet<>();
        for (int i = 0; i < fabric.getRows(); i++) {
            for (int j = 0; j < fabric.getColumns(); j++) {
                if (!fabric.empty(i, j) && fabric.get(i, j).size() > 1) {
                    overlappingIDs.addAll(fabric.get(i, j));
                }
            }
        }
        int uniqueId = -1;
        for (Claim claim : claims) {
            if (!overlappingIDs.contains(claim.id)) {
                uniqueId = claim.id;
            }
        }

        return uniqueId;
    }

    public static void populateMatrix(final Matrix<HashSet<Integer>> fabric, final ArrayList<Claim> claims){
        for (Claim claim : claims) {
            int topPadding = claim.topPadding;
            int leftPadding = claim.leftPadding;
            for (int i = 1 + leftPadding; i <= claim.width + leftPadding; i++) {
                for (int j = 1 + topPadding; j <= claim.length + topPadding; j++) {
                    if (fabric.empty(j, i)) {
                        HashSet<Integer> idSet = new HashSet<>();
                        idSet.add(claim.id);
                        fabric.insert(j, i, idSet);
                    } else {
                        fabric.get(j, i).add(claim.id);
                    }
                }
            }
        }
    }

    public static int uniqueID(final Matrix<HashSet<Integer>> fabric, final HashSet<Integer> overlappingIDs, final ArrayList<Claim> claims){
        for (int i = 0; i < fabric.getRows(); i++) {
            for (int j = 0; j < fabric.getColumns(); j++) {
                if (!fabric.empty(i, j) && fabric.get(i, j).size() > 1) {
                    overlappingIDs.addAll(fabric.get(i, j));
                }
            }
        }
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
        ArrayList<Claim> claims = new ArrayList<>();
        Pattern pattern = Pattern.compile("#(\\d*) @ (\\d*),(\\d*): (\\d*)x(\\d*)");
        for (String claimSpec : rawInput) {
            Matcher matcher = pattern.matcher(claimSpec);
            Claim claim = new Claim(matcher);
            claims.add(claim);
        }
        return claims;
    }

    private static class Claim {
        int id;
        int leftPadding;
        int topPadding;
        int width;
        int length;

        private Claim(Matcher matcher) {
            matcher.find();
            id = Integer.parseInt(matcher.group(1));
            leftPadding = Integer.parseInt(matcher.group(2));
            topPadding = Integer.parseInt(matcher.group(3));
            width = Integer.parseInt(matcher.group(4));
            length = Integer.parseInt(matcher.group(5));
        }

    }
}
