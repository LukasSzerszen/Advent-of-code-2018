import com.sun.xml.internal.bind.v2.model.core.ID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 {
    private Matrix<Boolean> fabric;
    private ArrayList<Claim> claims;

    public Day3(){
        this.fabric = new Matrix<Boolean>();
        this.claims = new ArrayList<Claim>();
    }

    public static void main(String[] args){
        Day3 solution = new Day3();
        solution.claims = solution.parseDay3Input(args[0]);
        System.out.println(solution.squaredOverlap());
        System.out.println(solution.uniqueID());
    }

    public int squaredOverlap(){
        int squaredOverlap = 0;
        for(Claim claim : claims){
            int topPadding = claim.topPadding;
            int leftPadding = claim.leftPadding;
            for(int i = 1; i <= claim.width; i++){
                for(int j = 1; j <= claim.length; j++){
                    if(fabric.empty(topPadding+j, leftPadding+i)){
                        fabric.insert(topPadding +j,leftPadding + i, false);
                    }else if(!fabric.get(topPadding +j,leftPadding + i)){
                        fabric.insert(topPadding +j,leftPadding + i, true);
                        squaredOverlap++;
                    }

                }
            }
        }

        return squaredOverlap;
    }

    public int uniqueID(){
        HashMap<Integer,Boolean> IDoverlap = new HashMap<>();
        Matrix<Integer> matrix = new Matrix<>();
        for(Claim claim : claims){
            int topPadding = claim.topPadding;
            int leftPadding = claim.leftPadding;
            for(int i = 1; i <= claim.width; i++){
                for(int j = 1; j <= claim.length; j++){
                    if(matrix.empty(topPadding+j, leftPadding+i)){
                        matrix.insert(topPadding +j,leftPadding + i, claim.id);
                        if(IDoverlap.containsKey(claim.id) && IDoverlap.get(claim.id)){
                            continue;
                        }else{
                        IDoverlap.put(claim.id,false);}
                    }else{
                        int id = matrix.get(topPadding +j,leftPadding + i);
                        IDoverlap.put(claim.id,true);
                        IDoverlap.put(id,true);
                    }

                }
            }
        }
        int uniqueID = -1;
        for(int key : IDoverlap.keySet()){
            if(!IDoverlap.get(key)){
                uniqueID = key;
                break;
            }
        }
        return uniqueID;
    }



    public ArrayList<Claim> parseDay3Input(final String file){
        String[] rawInput = IOutilities.readFileInputAsArray(file);
        ArrayList<Claim> claims = new ArrayList<>();
        Pattern pattern = Pattern.compile("#(\\d*) @ (\\d*),(\\d*): (\\d*)x(\\d*)");
        for(String claimSpec : rawInput){
            Matcher matcher = pattern.matcher(claimSpec);
            Claim claim = new Claim(matcher);
            claims.add(claim);
        }
        return claims;
    }

    private class Claim{
        int id;
        int leftPadding;
        int topPadding;
        int width;
        int length;

        public Claim(Matcher matcher){
            matcher.find();
            id  = Integer.parseInt(matcher.group(1));
            leftPadding = Integer.parseInt(matcher.group(2));
            topPadding = Integer.parseInt(matcher.group(3));
            width = Integer.parseInt(matcher.group(4));
            length = Integer.parseInt(matcher.group(5));
        }

    }
}
