package com.criptografie.criptografie.service.impl;

import com.criptografie.criptografie.Constants;
import org.springframework.stereotype.Service;

@Service
public class HillCipher {

    private static int MATRIX_GRADE = 2;

    public boolean validateVector(int[] vector, int matrixRank) {
        return vector.length == matrixRank;
    }

    public boolean validateMatrix(int[][] matrix) {
        int determinant = matrixDeterminant(matrix, matrix.length);
        return determinant > 0 || cmmdc(determinant, Constants.ALPHABET_LENGTH) == 1;
    }

    public String encrypt(String a, int[][] matrix, int[] vector) {
        MATRIX_GRADE = matrix.length;
        String cleanText = a.toUpperCase().replace(" ", "").trim();
        StringBuilder encryptedText = new StringBuilder(cleanText);
        int encryptedTextIndex = 0;
        int[] aux = new int[MATRIX_GRADE];
        int k = 0;

        for (int i = 0; i < cleanText.length(); i++) {
            aux[k++] = (cleanText.charAt(i) - 'A');
            if ((i + 1) % MATRIX_GRADE == 0) {
                matrixMultiplication(matrix, aux);
                vectorAddition(vector, aux);
                vectorModule(aux);

                for (int j = 0; j < k; j++) {
                    encryptedText.setCharAt(encryptedTextIndex++, ((char) (aux[j] + 'A')));
                }
                k = 0;
            }
        }
        if (cleanText.length() % MATRIX_GRADE != 0) {
            int extraCharsToFill = MATRIX_GRADE - (cleanText.length() % MATRIX_GRADE);
            encryptedText.setLength(cleanText.length() + extraCharsToFill);
            while (extraCharsToFill > 0) {
                aux[k++] = ('Q' - 'A');
                extraCharsToFill--;
            }
            matrixMultiplication(matrix, aux);
            vectorAddition(vector, aux);
            vectorModule(aux);

            for (int j = 0; j < k; j++) {
                encryptedText.setCharAt(encryptedTextIndex++, ((char) (aux[j] + 'A')));
            }
        }
        return encryptedText.toString();
    }

    public String decrypt(String encryptedText, int[][] matrix, int[] vector) throws Exception {
        int[][] inverseMatrix = inverseMatrix(matrix);
        StringBuilder decryptedText = new StringBuilder(encryptedText);
        int decryptedTextIndex = 0;
        int[] aux = new int[MATRIX_GRADE];
        int k = 0;

        for (int i = 0; i < encryptedText.length(); i++) {
            aux[k++] = (encryptedText.charAt(i) - 'A');
            if ((i + 1) % MATRIX_GRADE == 0) {
                vectorSubsctraction(vector, aux);
                matrixMultiplication(inverseMatrix, aux);
                vectorModule(aux);

                for (int j = 0; j < k; j++) {
                    decryptedText.setCharAt(decryptedTextIndex++, (char) (aux[j] + 'A'));
                }
                k = 0;
            }
        }

        //TODO: implement removal of fill characters

        return decryptedText.toString();
    }

    private int[][] inverseMatrix(int[][] matrix) throws Exception {
        // Find matrixDeterminant of A[][]
        int det = matrixDeterminant(matrix, matrix.length);
        if (det == 0) {
            throw new Exception("Singular matrix, can't find its inverse");
        }

        // Find adjoint
        int[][] adj = adjoint(matrix);
        int[][] inverse = new int[adj.length][adj[0].length];

        // Find Inverse using formula "inverse(A) = adj(A)/det(A)"
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix.length; j++)
                inverse[i][j] = (int) (adj[i][j] / (float) det);

        return inverse;
    }

    private int[][] adjoint(int[][] matrix) {
        int[][] adj = new int[matrix.length][matrix[0].length];
        if (matrix.length == 1) {
            adj[0][0] = 1;
            return adj;
        }

        // temp is used to store cofactors of A[][]
        int sign = 1;
        int[][] temp = new int[matrix.length][matrix[0].length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                // Get cofactor of A[i][j]
                getCofactor(matrix, temp, i, j, matrix.length);

                // sign of adj[j][i] positive if sum of row
                // and column indexes is even.
                sign = ((i + j) % 2 == 0) ? 1 : -1;

                // Interchanging rows and columns to get the
                // transpose of the cofactor matrix
                adj[j][i] = (sign) * (matrixDeterminant(temp, matrix.length - 1));
            }
        }
        return adj;
    }

    private void matrixMultiplication(int[][] matrix, int[] a) {
        int[] aux = new int[MATRIX_GRADE];
        for (int i = 0; i < MATRIX_GRADE; i++) {
            aux[i] = 0;
            for (int j = 0; j < MATRIX_GRADE; j++) {
                aux[i] += matrix[i][j] * a[j];
            }
        }
        for (int i = 0; i < MATRIX_GRADE; i++) {
            a[i] = aux[i];
        }
    }

    private void vectorModule(int[] aux) {
        for (int i = 0; i < MATRIX_GRADE; i++) {
            aux[i] = ((aux[i] % Constants.ALPHABET_LENGTH) + Constants.ALPHABET_LENGTH) % Constants.ALPHABET_LENGTH;
        }
    }

    private void vectorAddition(int[] vector, int[] aux) {
        for (int i = 0; i < MATRIX_GRADE; i++) {
            aux[i] += vector[i];
        }
    }

    private void vectorSubsctraction(int[] vector, int[] aux) {
        for (int i = 0; i < MATRIX_GRADE; i++) {
            aux[i] -= vector[i];
        }
    }

    private int matrixDeterminant(int[][] matrix, int n) {
        int result = 0; // Initialize result

        if (n == 1)
            return matrix[0][0];

        // To store cofactors
        int temp[][] = new int[matrix.length][matrix[0].length];

        // To store sign multiplier
        int sign = 1;

        // Iterate for each element of first row
        for (int j = 0; j < n; j++) {
            // Getting Cofactor of mat[0][f]
            getCofactor(matrix, temp, 0, j, n);
            result += sign * matrix[0][j] * matrixDeterminant(temp, n - 1);

            // terms are to be added with
            // alternate sign
            sign = -sign;
        }

        return result;
    }

    private void getCofactor(int mat[][], int temp[][], int p, int q, int n) {
        int i = 0, j = 0;

        // Looping for each element of
        // the matrix
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {

                // Copying into temporary matrix
                // only those element which are
                // not in given row and column
                if (row != p && col != q) {
                    temp[i][j++] = mat[row][col];

                    // Row is filled, so increase
                    // row index and reset col
                    //index
                    if (j == n - 1) {
                        j = 0;
                        i++;
                    }
                }
            }
        }
    }

    private int cmmdc(int n1, int n2) {
        return n2 == 0 ? n1 : cmmdc(n2, n1 % n2);
    }
}
