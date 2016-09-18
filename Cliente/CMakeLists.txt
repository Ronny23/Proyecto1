cmake_minimum_required(VERSION 3.5)
project(Cliente)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")
list(APPEND CMAKE_MODULE_PATH "${CMAKE_CURRENT_LIST_DIR}/cmake")
set(SOURCE_FILES main.cpp xReference.cpp)
add_executable(Cliente ${SOURCE_FILES})

include(FindPkgConfig)
find_package(Jsoncpp REQUIRED)
include_directories(usr/include/jsoncpp/json.h)
target_link_libraries(Cliente /usr/lib/libjson_linux-gcc-5.4.0_libmt.so)