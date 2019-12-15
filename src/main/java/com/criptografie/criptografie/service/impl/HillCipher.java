package com.criptografie.criptografie.service.impl;

import com.criptografie.criptografie.Constants;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class HillCipher {

    private static final int MATRIX_GRADE = 2;
    private int[][] matrix;
    private int[] vector;

    public boolean validate(int[][] matrix, int[] vector) {
        return validateVector(vector) && validateMatrix(matrix);
    }

    public String encrypt(String plainText, int[][] matrix, int[] vector) {
        this.matrix = matrix;
        this.vector = vector;

        String cleanText = plainText.toUpperCase().trim();
        StringBuilder copy = new StringBuilder(cleanText.length());
        int[] aux = new int[MATRIX_GRADE];
        int k = 0;

        for (int i = 0; i < matrix.length; i++) {
            aux[k++] = (cleanText.charAt(i) - 'A');
            if ((i + 1) % MATRIX_GRADE == 0) {
                matrixMultiplication(matrix, aux);
                adunareVector(vector, aux);
                modulVector(aux);

                for (int j = 0; j < k; j++) {
                    copy.append(aux[j] + 'A');
                }
                k = 0;
            } else {
                int catSaCompensez = MATRIX_GRADE - (cleanText.length() % MATRIX_GRADE);
                copy.setLength(cleanText.length() + catSaCompensez);
                while (catSaCompensez > 0) {
                    aux[k++] = ('Q' - 'A');
                    catSaCompensez--;
                }
                matrixMultiplication(matrix, aux);
                adunareVector(vector, aux);
                modulVector(aux);

                for (int j = 0; j < k; j++) {
                    copy.append(aux[j] + 'A');
                }
                k = 0;
            }
        }

        return copy.toString();
    }

    public String decrypt(String encryptedText) throws Exception {
        StringBuilder copy = new StringBuilder(encryptedText.length());
        int[][] inverseMatrix = inverseMatrix(this.matrix);
        int[] aux = new int[MATRIX_GRADE];
        int k = 0;

        for (int i = 0; i < encryptedText.length(); i++) {
            aux[k++] = (encryptedText.charAt(i) - 'A');
            if ((i + 1) % MATRIX_GRADE == 0) {
                scadereVector(vector, aux);
                matrixMultiplication(inverseMatrix, aux);
                modulVector(aux);

                for (int j = 0; j < k; j++) {
                    copy.append(aux[j] + 'A');
                }
                k = 0;
            }
        }

        return null;
    }

    private int[][] inverseMatrix(int[][] matrix) throws Exception {
        // Find determinant of A[][]
        int det = determinant(matrix, matrix.length);
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
                adj[j][i] = (sign) * (determinant(temp, matrix.length - 1));
            }
        }
        return adj;
    }

    private void matrixMultiplication(int[][] matrix, int[] a) {
        int[] aux = new int[MATRIX_GRADE];
        for (int i = 0; i < MATRIX_GRADE; i++) {
            aux[i] = 0;
            for (int k = 0; k < MATRIX_GRADE; k++) {
                aux[i] += matrix[i][k] * a[k];
            }
        }
        for (int i = 0; i < MATRIX_GRADE; i++) {
            a[i] = aux[i];
        }
    }


    private void modulVector(int[] aux) {
        for (int i = 0; i < MATRIX_GRADE; i++) {
            aux[i] = ((aux[i] % Constants.ALPHABET_LENGTH) + Constants.ALPHABET_LENGTH) % Constants.ALPHABET_LENGTH;
        }
    }

    private void adunareVector(int[] vector, int[] aux) {
        for (int i = 0; i < MATRIX_GRADE; i++) {
            aux[i] += vector[i];
        }
    }

    private void scadereVector(int[] vector, int[] aux) {
        for (int i = 0; i < MATRIX_GRADE; i++) {
            aux[i] -= vector[i];
        }
    }

    private boolean validateVector(int[] vector) {
        return vector.length == MATRIX_GRADE;
    }

    private boolean validateMatrix(int[][] matrix) {
        int determinant = determinant(matrix, matrix.length);
        return determinant > 0 || cmmdc(determinant, Constants.ALPHABET_LENGTH) == 1;
    }

    private int[][] transpose(int[][] matrix) {
        int[][] transposeMatrix = Arrays.copyOf(matrix, matrix.length);
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix.length; j++)
                transposeMatrix[i][j] = matrix[j][i];
        return transposeMatrix;
    }

    private int cmmdc(int n1, int n2) {
        return n2 == 0 ? n1 : cmmdc(n2, n1 % n2);
    }

    private int determinant(int[][] matrix, int n) {
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
            result += sign * matrix[0][j] * determinant(temp, n - 1);

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

}
