package doodlejump.core.networking;

public class Log {
    public static void print(String string) {
        System.out.println("Network > " + string);
    }

    public static void printf(String string, Object... parameters) {
        System.out.printf("Network > " + string + "%n", parameters);
    }
}
