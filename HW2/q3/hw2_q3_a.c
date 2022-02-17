#include <stdio.h>
#include <omp.h>
#include <stdlib.h>
#include <string.h>



void MatrixMult(char file1[],char file2[],int T)
{
  //Write your code here
  omp_set_num_threads(T); 

  // ************* CREATE MATRIX 1 *************
  char * line = NULL;
  size_t len = 0;
  ssize_t read;
  FILE *fp = fopen(file1, "r"); // "r" for read

  int m;
  int n;
  while ((read = getline(&line, &len, fp)) != -1) {
      //printf("Retrieved line of length %zu:\n", read);
      // Find m and n
      // printf("m and n are %s", line); 

      char* pch;
      pch = strtok (line, " ");
      int col = 0;
      while (pch != NULL){
        if ( col == 0 ){
          m = atoi(pch);
        }
        else if ( col == 1 ){
          n = atoi(pch);
        }
        // printf ("%s\n", pch);
        pch = strtok (NULL, " ");
        col++;
      }
      break;
  }

  float matrix1[m][n];
  for ( int i = 0; i < m; i++ ){
    for ( int j = 0; j < n; j++){
      matrix1[i][j] = 0;
      //printf("%d %d %f \n", i, j, matrix1[i][j]);
    }
    //printf("\n");
  }

  int row = 0;
  while ((read = getline(&line, &len, fp)) != -1) {
      //printf("Retrieved line of length %zu:\n", read);
      // Find m and n

      char* pch;
      pch = strtok (line, " ");
      int col = 0;
      while (pch != NULL){
        //printf ("%s\n", pch); // pch is the individual number
        float num = atof(pch);
        matrix1[row][col] = num;
        col++;
        pch = strtok (NULL, " ");
      }
      row++;
  }

  // Print Matrix 1
  // for ( int i = 0; i < m; i++ ){
  //   for ( int j = 0; j < n; j++){
  //     printf("%f ", matrix1[i][j]);
  //   }
  //   printf("\n");
  // }

  fclose(fp);
  if (line)
      free(line);
  //exit(EXIT_SUCCESS);

  // printf("End reading file 1\n");

  // ********* CREATE MATRIX 2 *****************
  char* line2 = NULL;
  len = 0;
  //read = NULL;
  FILE *fp2 = fopen(file2, "r"); // "r" for read

  int m2;
  int n2;
  while ((read = getline(&line2, &len, fp2)) != -1) {
      //printf("Retrieved line of length %zu:\n", read);
      // Find m2 and n2
      // printf("m2 and n2 are %s", line2); 

      char* pch;
      pch = strtok (line2, " ");
      int col = 0;
      while (pch != NULL){
        if ( col == 0 ){
          m2 = atoi(pch);
        }
        else if ( col == 1 ){
          n2 = atoi(pch);
        }
        // printf ("%s\n", pch);
        pch = strtok (NULL, " ");
        col++;
      }
      break;
  }

  float matrix2[m2][n2];
  for ( int i = 0; i < m2; i++ ){
    for ( int j = 0; j < n2; j++){
      matrix2[i][j] = 0;
      //printf("%d %d %f \n", i, j, matrix1[i][j]);
    }
    //printf("\n");
  }

  row = 0;
  while ((read = getline(&line2, &len, fp2)) != -1) {
      //printf("Retrieved line of length %zu:\n", read);

      char* pch;
      pch = strtok (line2, " ");
      int col = 0;
      while (pch != NULL){
        //printf ("%s\n", pch); // pch is the individual number
        float num = atof(pch);
        matrix2[row][col] = num;
        col++;
        pch = strtok (NULL, " ");
      }
      row++;
  }

  // Print Matrix 2
  // for ( int i = 0; i < m2; i++ ){
  //   for ( int j = 0; j < n2; j++){
  //     printf("%f ", matrix2[i][j]);
  //   }
  //   printf("\n");
  // }

  fclose(fp2);
  if (line2)
      free(line2);
  //exit(EXIT_SUCCESS);

  // printf("End reading file 2\n");

  
  // ** CREATE RESULT MATRIX **
  float result[m][n2];
  for (int i = 0; i < m; ++i) {
    for (int j = 0; j < n2; ++j) {
        result[i][j] = 0;
    }
  }

  // *** NOW BOTH MATRIXES ARE CREATED, MULTIPLY THEM ******
  // Multiplying first and second matrices and storing it in result
  #pragma omp parallel for shared(result, matrix1, matrix2)
  for (int i = 0; i < m; ++i) {
    for (int j = 0; j < n2; ++j) {
        for (int k = 0; k < n; ++k) {
          result[i][j] += matrix1[i][k] * matrix2[k][j];
        }
    }
  }

  // Print Result Matrix
  // for ( int i = 0; i < m; i++ ){
  //   for ( int j = 0; j < n2; j++){
  //     printf("%f ", result[i][j]);
  //   }
  //   printf("\n");
  // }

  // *** STORE IN FILE ***
  FILE *fp3 = fopen("output_a.txt", "w"); // "w" for write

  fprintf(fp3, "%d %d", m, n2);
  fprintf(fp3, "\n");

  //#pragma omp parallel for shared(fp3, result)
  for( int i=0;i<m;i++) {
    for( int j=0;j<n2;j++) {
        fprintf(fp3,"%f  ", result[i][j]);
    }
    fprintf(fp3,"\n");
  }
  
}




void main(int argc, char *argv[])
{
  char *file1, *file2;
  file1=argv[1];
  file2=argv[2];
  int T=atoi(argv[3]); // num threads to use
  MatrixMult(file1,file2,T);


}


