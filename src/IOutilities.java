import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.function.Supplier;
import java.util.stream.Stream;

/*
 * Class with functions for I/O
 */
public class IOutilities {

    /*
     * Takes a file path and returns contents as a array of Strings
     */
    public static String[] readFileInputAsArray(final String file){
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            return bufferedReader.lines().toArray(String[]::new);
        }catch (FileNotFoundException e){
            System.err.println("File: " + file + " not found");
        }
        return new String[]{"Empty"};
    }

    public static Stream<String> streamSupplier(final String[] input){
        return Stream.of(input);
    }


}
