public class Fib {
    static long fib(int n) {
        if (n <= 1) return n;
        long a = 0, b = 1, c = 1;
        for (int i = 2; i <= n; ++i) {
            c = a + b;
            a = b;
            b = c;
        }
        return b;
    }
    public static void main(String[] args) {
        for (int i = 0; i < 1000000; i++) {
            System.out.println("Java " + i + " " + fib(40));
//            fib(40);
        }
    }
}