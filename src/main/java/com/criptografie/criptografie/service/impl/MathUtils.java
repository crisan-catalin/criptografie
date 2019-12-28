package com.criptografie.criptografie.service.impl;

import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class MathUtils {

    public int[] stringToVector(String s) {
        return Arrays.stream(s.split(" ")).mapToInt(Integer::valueOf).toArray();
    }

    public int[][] vectorToMatrix(int[] vector, int matrixRank) {
        int[][] matrix = new int[(int) matrixRank][(int) matrixRank];
        int k = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = vector[k++];
            }
        }
        return matrix;
    }
}
