#include <iostream>
#include <fstream>
using namespace std;
int main() {
    std::ofstream file("sample.csv"); // Ghi ra file để tránh console quá tải
    if (!file.is_open()) {
        std::cerr << "Cannot open file!\n";
        return 1;
    }

    for (long long i = 1; i <= 100000000; ++i) {
        file << "This is line number " << i << "\n";
    }

    file.close();
    std::cout << "Finished generating 100,000,000 lines.\n";
    return 0;
}