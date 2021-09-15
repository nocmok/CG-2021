cmake_minimum_required(VERSION 3.20)
project(glfw_hello_world)

set(CMAKE_CXX_STANDARD 14)

add_executable(triangles main.cpp glad.c util.cpp include/util.h)

target_include_directories(triangles PUBLIC "/usr/local/include")
target_include_directories(triangles PUBLIC "include")

# shaders
file(MAKE_DIRECTORY ${CMAKE_CURRENT_BINARY_DIR}/shader)
configure_file(shader/vertex_shader.glsl ${CMAKE_CURRENT_BINARY_DIR}/shader COPYONLY)
configure_file(shader/fragment_shader.glsl ${CMAKE_CURRENT_BINARY_DIR}/shader COPYONLY)

target_link_libraries(triangles PUBLIC "/usr/local/bin/libglfw.3.dylib")