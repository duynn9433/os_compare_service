def fib(n):
    a, b = 0, 1
    for _ in range(2, n+1):
        a, b = b, a + b
    return b

for i in range(1, 1000000):
    print(f"Python Fibonacci {i} : {fib(40)}")