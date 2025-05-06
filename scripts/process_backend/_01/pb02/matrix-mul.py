import numpy as np
import timeit



# Định nghĩa hàm thực hiện nhân ma trận
def multiply():
    # Sinh 2 ma trận ngẫu nhiên kích thước 1000x1000
    A = np.random.rand(1000, 1000)
    B = np.random.rand(1000, 1000)
    np.dot(A, B)  # hoặc A @ B

# Số lần lặp
n = 1000

# Đo thời gian với timeit
execution_time = timeit.timeit(multiply, number=n)

# In kết quả
print(f"⏱️ Total time for {n} runs: {execution_time:.4f} seconds")
print(f"📊 Average time per multiplication: {execution_time / n:.4f} seconds")