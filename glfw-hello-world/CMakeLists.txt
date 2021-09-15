cmake_minimum_required(VERSION 3.20)
project(glfw_hello_world)

set(CMAKE_CXX_STANDARD 14)

add_executable(glfw_hello_world main.cpp glad.c)

target_include_directories(glfw_hello_world PUBLIC "/usr/local/include")

target_link_libraries(glfw_hello_world PUBLIC "/usr/local/bin/libglfw.3.dylib")