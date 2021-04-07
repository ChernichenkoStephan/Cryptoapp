package com.encryption.Model;

import java.util.List;

class BMPImage {
    final Integer offset;
    final Integer size;
    final List<Integer> headerData;
    final List<Integer> pixelData;

    BMPImage(int offset, int size, List<Integer> headerData, List<Integer> pixelData) {
        this.offset = offset;
        this.size = size;
        this.pixelData = pixelData;
        this.headerData = headerData;
    }
}
