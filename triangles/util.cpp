#include "include/util.h"

using namespace std;

char *read_file(const char *filename, int *size) {
    ifstream in(filename, fstream::binary);
    in.seekg(0, ifstream::end);
    *size = (int) in.tellg();
    in.seekg(0);
    char *buffer = (char *) malloc(sizeof(char) * *size);
    in.read(buffer, *size);
    in.close();
    return buffer;
}