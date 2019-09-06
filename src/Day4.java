import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class Day4 {
    private static final Pattern guardIDPattern = Pattern.compile("(\\d+)");

    public static void main(String[] args){
        ArrayList<TimeEntry> timeEntries = parseInput(args[0]);
        Collections.sort(timeEntries,new TimeEntryComparator());
        HashMap<Integer, Matrix<Integer>> guardShifts = new HashMap<>();
        createGuardShifts(timeEntries,guardShifts);
        fillGuardShifts(timeEntries,guardShifts);
        int ID = mostSleptMinutesGuardIDs(guardShifts);
        int mostSleptMinute = mostSleptMinute(guardShifts.get(ID));

        HashMap<Integer, ArrayList<Integer>> guardSleepFrequency = frequentlySleptMinutes(guardShifts);
        int[] idWithMostFrequentlySlept = mostFrequentlySleptMinuteandGuardID(guardSleepFrequency);


        System.out.println(ID * mostSleptMinute);
        System.out.println(idWithMostFrequentlySlept[0] * idWithMostFrequentlySlept[1]);

    }

    public static ArrayList<TimeEntry> parseInput(final String file){
        String[] input = IOutilities.readFileInputAsArray(file);
        Pattern pattern = Pattern.compile("\\[(.*?)\\](.+)");
        return Arrays.stream(input).map(pattern::matcher).map(TimeEntry::new).collect(Collectors.toCollection(ArrayList::new));


    }

    public static void fillGuardShifts(final ArrayList<TimeEntry> entries, final HashMap<Integer, Matrix<Integer>> guardShifts){
        int currentGuardID = -1;
        Optional<LocalDateTime> from = Optional.empty();
        Optional<LocalDateTime> to = Optional.empty();
        for(TimeEntry timeEntry : entries){
            if(timeEntry.line.contains("Guard")){
                currentGuardID = extractID(timeEntry);
                continue;
            }else if (timeEntry.line.contains("asleep")){
                from = Optional.of(timeEntry.dateTime);
            }else if (timeEntry.line.contains("wakes")){
                to = Optional.of(timeEntry.dateTime);
            }
            if(from.isPresent() && to.isPresent()){
                int row = findRowForDate(from.get().getDayOfYear(), guardShifts.get(currentGuardID));
                from.get().until(to.get(), ChronoUnit.MINUTES);
                for(int i= from.get().getMinute() ; i < to.get().getMinute(); i++){
                    guardShifts.get(currentGuardID).insert(row, i, 1);
                }
                from = Optional.empty();
                to = Optional.empty();
            }

        }
    }



    public static HashMap<Integer,ArrayList<Integer>> frequentlySleptMinutes(HashMap<Integer, Matrix<Integer>> guardShifts){
        HashMap<Integer, ArrayList<Integer>> frequentlySlept = new HashMap<>();
        for(int key : guardShifts.keySet()){
            ArrayList<Integer> sleepFrequencey = new ArrayList<>(Collections.nCopies(60,0));
            Collections.fill(sleepFrequencey, 0);
            for(int i = 0; i< guardShifts.get(key).getRows(); i++){
                for(int j = 0; j< guardShifts.get(key).getColumns()-1; j++){
                    if(guardShifts.get(key).get(i,j) != null){
                        sleepFrequencey.set(j, sleepFrequencey.get(j) +1);
                    }
                }
            }
            frequentlySlept.putIfAbsent(key,sleepFrequencey);
        }

        return frequentlySlept;

    }

    public static int[] mostFrequentlySleptMinuteandGuardID(HashMap<Integer, ArrayList<Integer>> guardSleepFrequency){
        int id= -1;
        int mostFrequentlySleptMinute = -1;
        int mostFrequentlySleptValue = -1;
        for(int guardID : guardSleepFrequency.keySet()){
            for(int i = 0; i < guardSleepFrequency.get(guardID).size(); i++){
                if(mostFrequentlySleptValue < guardSleepFrequency.get(guardID).get(i)){
                    id = guardID;
                    mostFrequentlySleptMinute = i;
                    mostFrequentlySleptValue = guardSleepFrequency.get(guardID).get(i);
                }
            }
        }
        int[] result = {id,mostFrequentlySleptMinute};
        return result;
    }

    public static int mostSleptMinute( Matrix<Integer> shifts){
        int mostSleptMinute= -1;
        Matrix<Integer> minutes = new Matrix<Integer>(1,60);
        for(int i = 0; i < shifts.getRows(); i++){
            for(int j = 0; j < shifts.getColumns() -1; j++){
                if(shifts.get(i,j) != null){
                    if(minutes.get(0,j) == null){
                        minutes.insert(0,j,1);
                    }else{
                        minutes.insert(0,j, minutes.get(0,j)+1);
                    }
                }
            }
        }
        int mostSleptValue = -1;
        for(int i= 0; i < minutes.getColumns(); i++){
            if(minutes.get(0,i) !=  null && minutes.get(0,i) > mostSleptValue){
                mostSleptValue = minutes.get(0,i);
                mostSleptMinute = i;
            }

        }
        return mostSleptMinute;
    }

    public static int mostSleptMinutesGuardIDs(final HashMap<Integer, Matrix<Integer>> guardShifts){
        HashMap<Integer,Integer> IDtoMinutesSlept = new HashMap<>();
        int ID = -1;
        int sum = -1;
        for(int guardID: guardShifts.keySet()){
            Matrix<Integer> guardTimeEntires = guardShifts.get(guardID);
            int currentGuardSum = 0;
            for(int i = 0; i < guardTimeEntires.getRows(); i++){
                for(int j = 0; j< guardTimeEntires.getColumns()-1; j++){
                    if(guardTimeEntires.get(i,j) != null){
                        currentGuardSum +=  guardTimeEntires.get(i,j);
                    }

                }
            }
            IDtoMinutesSlept.putIfAbsent(guardID,currentGuardSum);
        }
        for(int key : IDtoMinutesSlept.keySet()){
            if(IDtoMinutesSlept.get(key) > sum){
                sum = IDtoMinutesSlept.get(key);
                ID = key;
            }
        }
        return ID;
    }

    public static int findRowForDate(int date, Matrix<Integer> guardShift){
        for(int i = 2; i<guardShift.getRows(); i++){
            if(guardShift.get(i, 60) != null && guardShift.get(i, 60) == date){
                return i; // row for this date exists
            }
        }

        for(int i = 2; i< guardShift.getRows(); i++){
            if(guardShift.get(i,60) == null){
                guardShift.insert(i, 60, date);
                return i; //an empty row for this date
            }
        }
        return -1; //no row was found and should brake
    }

    public static void createGuardShifts(final ArrayList<TimeEntry> entires, final HashMap<Integer, Matrix<Integer>> guardShifts){
        for(TimeEntry timeEntry: entires){
            if(timeEntry.line.contains("Guard")){
                int guardID = extractID(timeEntry);
                int shifts = 0;
                for(TimeEntry entry:entires){
                    if(entry.line.contains(Integer.toString(guardID))){
                        shifts++;
                    }
                }
                guardShifts.put(guardID, new Matrix<Integer>(2+ shifts, 61));
            }
        }

    }

    public static int extractID(final TimeEntry timeEntry){
        Matcher match = guardIDPattern.matcher(timeEntry.line);
        match.find();
        return Integer.parseInt(match.group());
    }

    private static class TimeEntry{
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime;
        String line;

        private TimeEntry(final Matcher matcher){
            matcher.find();
            this.dateTime = LocalDateTime.parse(matcher.group(1), formatter);
            this.line = matcher.group(2);
        }

    }

    private static class TimeEntryComparator implements Comparator<TimeEntry>{
        public int compare(TimeEntry entry1, TimeEntry entry2){
            return entry1.dateTime.compareTo(entry2.dateTime);
        }
    }
}
