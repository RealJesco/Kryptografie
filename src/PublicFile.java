public class PublicFile {
    private static int millerRabinSteps = 0;
    private static int lengthOfP1 = 0;
    private static int lengthOfP2 = 0;

    public static int getLengthOfP1() {
        return lengthOfP1;
    }

    public static int getLengthOfP2() {
        return lengthOfP2;
    }

    public static int getMillerRabinSteps() {
        return millerRabinSteps;
    }

    public synchronized static void setMillerRabinSteps(int s){
        millerRabinSteps = s;
    }

    public synchronized static void setLengthOfP1(int s){
        lengthOfP1 = s;
    }

    public synchronized static void setLengthOfP2(int s){
        lengthOfP2 = s;
    }

}
