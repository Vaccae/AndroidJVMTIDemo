//
// Created by 36574 on 2022-03-25.
//
#pragma once
#include <cstdint>
#include <unistd.h>
#include <cstring>
#include <fcntl.h>
#include <sys/mman.h>
#include <mutex>
#include "MemoryFile.h"

std::mutex mtx;

//系统给我们提供真正的内存时，用页为单位提供
//内存分页大小 一分页的大小
int32_t DEFAULT_FILE_SIZE = getpagesize();
std::string MemoryFile::m_path = "111";
int MemoryFile::m_fd =0 ;
int32_t MemoryFile::m_size=0;
int8_t * MemoryFile::m_ptr= nullptr;
int MemoryFile::m_actualSize=0;

void MemoryFile::Write(char *data, int dataLen) {
    mtx.lock();
    if(m_actualSize + dataLen >= m_size){
        Resize(m_actualSize+dataLen);
    }
    //将data的datalen长度的数据 拷贝到 m_ptr + m_actualSize;
    //操作内存，通过内存映射就写入文件了
    memcpy(m_ptr + m_actualSize, data, dataLen);
    //重新设置最初位置
    m_actualSize += dataLen;
    mtx.unlock();
}

void MemoryFile::Resize(int32_t needSize) {
    int32_t oldSize = m_size;
    do{
        m_size *=2;
    } while (m_size<needSize);
    //设置文件大小
    ftruncate(m_fd, m_size);
    //解除映射
    munmap(m_ptr, oldSize);
    //重新进行mmap内存映射
    m_ptr = static_cast<int8_t *>(mmap(0,m_size,PROT_READ | PROT_WRITE, MAP_SHARED, m_fd, 0));

}

void MemoryFile::Release() {
    munmap(m_ptr, m_size);
    close(m_fd);
}

void MemoryFile::Init(const char *path) {
    //if(m_ptr == nullptr) {
        m_path = path;
        m_fd = open(m_path.c_str(), O_RDWR | O_CREAT, S_IRWXU);
        m_size = DEFAULT_FILE_SIZE;

        //将文件设置为m_size大小
        ftruncate(m_fd, m_size);
        //mmap内存映射
        m_ptr = static_cast<int8_t *>(mmap(0, m_size, PROT_READ | PROT_WRITE, MAP_SHARED, m_fd, 0));
        //初始化m_actualSize为0
        m_actualSize = 0;
    //}
}
