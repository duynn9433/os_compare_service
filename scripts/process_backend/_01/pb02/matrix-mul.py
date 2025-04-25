import numpy as np
import time

# Sinh 2 ma trận ngẫu nhiên kích thước 1000x1000
A = np.random.rand(1000, 1000)
B = np.random.rand(1000, 1000)

# Ghi nhận thời gian bắt đầu
start = time.time()

# Nhân ma trận
C = A @ B  # hoặc np.dot(A, B)

# Ghi nhận thời gian kết thúc
end = time.time()

# In kết quả
print(f"⏱️ Time to multiply 1000x1000 matrices: {end - start:.4f} seconds")