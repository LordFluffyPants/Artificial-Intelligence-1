cmake_minimum_required(VERSION 3.3)
project(n_queens_genetic_algorithim)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES main.cpp)
add_executable(n_queens_genetic_algorithim ${SOURCE_FILES})