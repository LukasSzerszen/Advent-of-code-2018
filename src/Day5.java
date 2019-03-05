public class Day5 {
    public static void main(String[] args){
        String polymer = parseInput(args[0]);
        System.out.println(recurse(polymer).length());

    }


    public static String recurse(String polymer){
        String[] subPolymers = {polymer.substring(0, polymer.length()/2), polymer.substring(polymer.length()/2)};
        StringBuilder sb1 = new StringBuilder(subPolymers[0]);
        StringBuilder sb2 = new StringBuilder(subPolymers[1]);
        sb1 = reducePolymer(sb1);
        sb1.append(reducePolymer(sb2));
        return reducePolymer(sb1).toString();
    }

    public static StringBuilder reducePolymer(StringBuilder polymer){
        for(int i = 0; i< polymer.length()-1; i++){
            if(Character.isUpperCase(polymer.charAt(i)) && Character.isLowerCase(polymer.charAt(i+1))
                    ||Character.isLowerCase(polymer.charAt(i)) && Character.isUpperCase(polymer.charAt(i+1))){
                if(Character.toUpperCase(polymer.charAt(i)) == Character.toUpperCase(polymer.charAt(i+1))){
                    polymer.deleteCharAt(i+1);
                    polymer.deleteCharAt(i);
                    return reducePolymer(polymer);
                }
            }
        }
        return polymer;
    }

    public static String parseInput(String file){
        String[] input = IOutilities.readFileInputAsArray(file);
        return input[0];
    }
}
