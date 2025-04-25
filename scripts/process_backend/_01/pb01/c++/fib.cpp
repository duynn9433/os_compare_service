#include <iostream>
long long fib(int n) {
    if (n <= 1) return n;
    long long a = 0, b = 1, c;
    for (int i = 2; i <= n; ++i) {
        c = a + b;
        a = b;
        b = c;
    }
    return b;
}
int main() {
    std::cout << "C++ Fibonacci(40) = " << fib(40) << std::endl;
    return 0;
}