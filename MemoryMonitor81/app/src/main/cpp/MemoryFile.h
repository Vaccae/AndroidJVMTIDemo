//
// Created by 36574 on 2022-03-25.
//
#pragma once
#include <string>

#ifndef MEMORYMONITOR_MEMORYFILE_H
#define MEMORYMONITOR_MEMORYFILE_H


class MemoryFile {
public:
    static std::string m_path;
    static int m_fd;
    static int32_t m_size;
    static int8_t *m_ptr;
    static int m_actualSize;

    static void Resize(int32_t needSize);
    static void Write(char *data, int dataLen);
    static void Init(const char* path);
    static void Release();
};


#endif //MEMORYMONITOR_MEMORYFILE_H
