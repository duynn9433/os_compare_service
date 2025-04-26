function fib(n) {
    let a = 0, b = 1, c;
    for (let i = 2; i <= n; i++) {
        c = a + b;
        a = b;
        b = c;
    }
    return b;
}

for (let i = 0; i < 1000000; i++) {
    console.log(i, fib(40));
}