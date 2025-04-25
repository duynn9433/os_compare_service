#include <iostream>
#include <fstream>
#include <chrono>

int main(int argc, char* argv[]) {
    int lines = argc > 1 ? std::stoi(argv[1]) : 10000;
    std::string file = argc > 2 ? argv[2] : "output.csv";

    auto start = std::chrono::high_resolution_clock::now();
    std::ofstream ofs(file);
    for (int i = 1; i <= lines; ++i) {
        ofs << "Line " << i << ",Some data here\n";
    }
    ofs.close();
    auto end = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double> duration = end - start;

    std::cout << "[C++] Wrote " << lines << " lines to " << file
              << " in " << duration.count() << " seconds." << std::endl;

    return 0;
}