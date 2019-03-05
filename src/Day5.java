public class Day5 {

    public static void main(String[] args) {
        String polymer = IOutilities.readFileInputAsArray(args[0])[0];
        System.out.println(recurse(polymer).length());


        //star2
        String[] shortenedPolymers = removeUnit(polymer);
        int shortest = Integer.MAX_VALUE;
        for (int i = 0; i < shortenedPolymers.length; i++) {
            if (shortenedPolymers[i] != null) {
                String reactedPolymer = recurse(shortenedPolymers[i]);
                if (shortest > reactedPolymer.length()) {
                    shortest = reactedPolymer.length();
                }
            }
        }
        System.out.println(shortest);
    }

    public static String[] removeUnit(final String polymer) {
        final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String[] shortestPolymers = new String[alphabet.length()];
        int smallestLength = Integer.MAX_VALUE;
        for (int i = 0; i < alphabet.length(); i++) {
            String reactedPolymer = removeOccurenceOf(alphabet.charAt(i), polymer);
            if (reactedPolymer.length() <= smallestLength) {
                smallestLength = reactedPolymer.length();
                shortestPolymers[i] = reactedPolymer;
            }
        }
        return shortestPolymers;
    }

    public static String removeOccurenceOf(char c, String polymer) {
        polymer = polymer.replaceAll(Character.toString(c), "");
        polymer = polymer.replaceAll(Character.toString(c).toLowerCase(), "");
        return polymer;
    }

    public static String recurse(String polymer) {
        String[] subPolymers = {polymer.substring(0, polymer.length() / 2), polymer.substring(polymer.length() / 2)};
        StringBuilder sb1 = new StringBuilder(subPolymers[0]);
        StringBuilder sb2 = new StringBuilder(subPolymers[1]);
        sb1 = reducePolymer(sb1);
        sb1.append(reducePolymer(sb2));
        return reducePolymer(sb1).toString();
    }

    public static StringBuilder reducePolymer(StringBuilder polymer) {
        for (int i = 0; i < polymer.length() - 1; i++) {
            if (Character.isUpperCase(polymer.charAt(i)) && Character.isLowerCase(polymer.charAt(i + 1))
                    || Character.isLowerCase(polymer.charAt(i)) && Character.isUpperCase(polymer.charAt(i + 1))) {
                if (Character.toUpperCase(polymer.charAt(i)) == Character.toUpperCase(polymer.charAt(i + 1))) {
                    polymer.deleteCharAt(i + 1);
                    polymer.deleteCharAt(i);
                    return reducePolymer(polymer);
                }
            }
        }
        return polymer;
    }
}
