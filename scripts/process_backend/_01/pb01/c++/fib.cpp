#include <iostream>
using namespace std;
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
    for(int i = 0; i <= 1000000; ++i) {
        fib(40);
    }
    return 0;
}