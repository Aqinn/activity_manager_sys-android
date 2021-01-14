cmake_minimum_required(VERSION 3.4.1)

#include头文件目录
include_directories(${CMAKE_SOURCE_DIR}/src/main/jni/include
        ${CMAKE_SOURCE_DIR}/src/main/jni/)

#source directory源文件目录
file(GLOB FACE_SRC ${CMAKE_SOURCE_DIR}/src/main/jni/*.h
        ${CMAKE_SOURCE_DIR}/src/main/jni/*.cpp)
set(FACE_COMPILE_CODE ${FACE_SRC})


#添加ncnn库
add_library(libncnn STATIC IMPORTED)
set_target_properties(libncnn
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libncnn.a)

#编译为动态库
add_library(Face SHARED ${FACE_COMPILE_CODE})

#添加工程所依赖的库
find_library(log-lib log)
find_library(android-lib android)
target_link_libraries(Face
        libncnn
        jnigraphics
        z
        ${android-lib}
        ${log-lib})
