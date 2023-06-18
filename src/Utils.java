public class Utils {  // Defines a public class named Utils
    public static String zeros(int n) {  // Defines a public static method named zeros that takes an integer parameter n and returns a string
        return new String(new char[n]).replace('\0', '0');  // Creates a string of n zeros
    }
}