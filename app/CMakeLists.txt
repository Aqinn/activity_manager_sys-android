cmake_minimum_required(VERSION 3.10.2)

set(BASE_DIR ${CMAKE_SOURCE_DIR}/base)
set(INCLUDE_DIR ${CMAKE_SOURCE_DIR}/include)
set(THIRD_PARTY_DIR ${CMAKE_SOURCE_DIR}/third_party)
set(UTILS_DIR ${CMAKE_SOURCE_DIR}/utils)

include_directories(${BASE_DIR})
include_directories(${INCLUDE_DIR})
include_directories(${THIRD_PARTY_DIR}/include)
include_directories(${THIRD_PARTY_DIR}/opencl/include)
include_directories(${UTILS_DIR})
include_directories(${CMAKE_SOURCE_DIR}/src/main/jni/cc)
include_directories(${CMAKE_SOURCE_DIR}/src/main/jni/thirdparty)
include_directories(${CMAKE_SOURCE_DIR}/src/main/jni/thirdparty/kannarotate-android-lib/include)
include_directories(${CMAKE_SOURCE_DIR}/src/main/jni/thirdparty/kannarotate-android-lib/src)

set(CMAKE_BUILD_TYPE Release)

set(TNN_OPENCL_ENABLE ON CACHE BOOL "" FORCE)
set(TNN_ARM_ENABLE ON CACHE BOOL "" FORCE)
if (ANDROID_ABI STREQUAL "arm64-v8a")
    set(TNN_ARM82_ENABLE ON CACHE BOOL "" FORCE)
endif ()

set(TNN_BUILD_SHARED OFF CACHE BOOL "" FORCE)
set(TNN_HUAWEI_NPU_ENABLE OFF CACHE BOOL "" FORCE)

file(GLOB_RECURSE WRAPPER_SRC ${CMAKE_SOURCE_DIR}/src/main/jni/cc/*.cc)
file(GLOB_RECURSE BASE_SRC ${BASE_DIR}/*.cc)
file(GLOB_RECURSE UTILS_SRC ${UTILS_DIR}/*.cc)
file(GLOB_RECURSE THIRDPARTY_SRC ${CMAKE_SOURCE_DIR}/src/main/jni/thirdparty/kannarotate-android-lib/src/*.c)
add_library(tnn_wrapper SHARED ${WRAPPER_SRC} ${BASE_SRC} ${THIRDPARTY_SRC} ${UTILS_SRC})

########################################### 链接 TNN 框架 ###########################################
set(JNILIBS_DIR ${CMAKE_SOURCE_DIR}/src/main/jniLibs)
add_library(tnn SHARED IMPORTED)
set_target_properties(tnn PROPERTIES IMPORTED_LOCATION ${JNILIBS_DIR}/${ANDROID_ABI}/libTNN.so)
################################################ 结束 ###############################################
############################################ 链接 TNN 框架 ###########################################
#add_library(opencllib SHARED IMPORTED)
#set_target_properties(opencllib PROPERTIES IMPORTED_LOCATION ${lib_DIR}/${ANDROID_ABI}/libopencv_java3.so)
################################################ 结束 ###############################################

find_library(
        log-lib
        log)

target_link_libraries(
        tnn_wrapper
        tnn
        -ljnigraphics
        ${log-lib})
