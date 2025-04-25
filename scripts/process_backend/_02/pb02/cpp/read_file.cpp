#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <chrono>

int main(int argc, char* argv[]) {
    std::string filename = (argc > 1) ? argv[1] : "sample.csv";
    std::ifstream file(filename);
    std::vector<std::string> lines;
    std::string line;

    auto start = std::chrono::high_resolution_clock::now();

    while (std::getline(file, line)) {
        lines.push_back(line);
    }

    auto end = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double> duration = end - start;

    std::cout << "✅ C++ read " << lines.size() << " rows in " << duration.count() << " seconds" << std::endl;
    return 0;
}