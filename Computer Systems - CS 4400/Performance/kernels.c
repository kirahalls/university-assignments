/*******************************************
 * Solutions for the CS:APP Performance Lab
 ********************************************/

#include <stdio.h>
#include <stdlib.h>
#include "defs.h"

/*
 * Please fill in the following student struct
 */
student_t student = {
    "Kira R. Halls",     /* Full name */
    "u1250109@utah.edu", /* Email address */
};

/***************
 * COMPLEX KERNEL
 ***************/

/******************************************************
 * Your different versions of the complex kernel go here
 ******************************************************/

/*
 * version1_complex - The first test version of complex with very minimal improvement
 */
// char version1_complex_descr[] = "version1_complex: First test version with minimal improvement";
// void version1_complex(int dim, pixel *src, pixel *dest)
// {
//   int i, j;

//   for(i = 0; i < dim; i++)
//     for(j = 0; j < dim; j++)
//     {
//       int value = ((int)src[RIDX(i, j, dim)].red +
// 						      (int)src[RIDX(i, j, dim)].green +
// 						      (int)src[RIDX(i, j, dim)].blue) / 3;
//       int dim1 = dim - j - 1;
//       int dim2 = dim - i - 1;
//       int ridx = RIDX(dim1, dim2, dim);
//       dest[ridx].red = value;

//       dest[ridx].green = value;

//       dest[ridx].blue = value;

//     }
// }

/*
 * version2_complex - The second test version of complex with tiling
 */
// char version2_complex_descr[] = "version2_complex: Second test version with tiling";
// __attribute__((always_inline)) void version2_complex(int dim, pixel *src, pixel *dest)
// {
//   int i,ii, j, jj;
//   int w = 16;
//   for(i = 0; i < dim; i += w)
//     for(j = 0; j < dim; j += w)
//     {

//       for(ii = i; ii < i + w; ii++)
//       {
//         int dim2 = dim - ii - 1;
//         for(jj = j; jj < j + w; jj++)
//         {
//           int ridx1 = RIDX(ii, jj, dim);
//           int value = ((int)src[ridx1].red +
// 						      (int)src[ridx1].green +
// 						      (int)src[ridx1].blue) / 3;
//           int dim1 = dim - jj - 1;
//           int ridx = RIDX(dim1, dim2, dim);
//           dest[ridx].red = value;

//           dest[ridx].green = value;

//           dest[ridx].blue = value;
//         }
//       }
//     }
// }

/*
 * version3_complex - The second test version of complex with tiling with different i and j increment vals
 */
char version3_complex_descr[] = "version3_complex: Third test version with tiling and separate i/j increment vals";
__attribute__((always_inline)) void version3_complex(int dim, pixel *src, pixel *dest)
{
  int i,ii, j, jj, dim1, dim2;
  int w = 16;
  int v = 4;
  for(i = 0; i < dim; i += w)
    for(j = 0; j < dim; j += v)
    {

      for(ii = i; ii < i + w; ii++)
      {
        dim2 = dim - ii - 1;
        for(jj = j; jj < j + v; jj++)
        {
          pixel sourcePixel = src[RIDX(ii, jj, dim)];
          dim1 = dim - jj - 1;
          int value = ((int) sourcePixel.red +
						      (int) sourcePixel.green +
						      (int) sourcePixel.blue) / 3;

          dest[RIDX(dim1, dim2, dim)].red = value;

          dest[RIDX(dim1, dim2, dim)].green = value;

          dest[RIDX(dim1, dim2, dim)].blue = value;
        }
      }
    }
}

/*
 * naive_complex - The naive baseline version of complex
 */
char naive_complex_descr[] = "naive_complex: Naive baseline implementation";
void naive_complex(int dim, pixel *src, pixel *dest)
{
  int i, j;

  for(i = 0; i < dim; i++)
    for(j = 0; j < dim; j++)
    {

      dest[RIDX(dim - j - 1, dim - i - 1, dim)].red = ((int)src[RIDX(i, j, dim)].red +
						      (int)src[RIDX(i, j, dim)].green +
						      (int)src[RIDX(i, j, dim)].blue) / 3;

      dest[RIDX(dim - j - 1, dim - i - 1, dim)].green = ((int)src[RIDX(i, j, dim)].red +
							(int)src[RIDX(i, j, dim)].green +
							(int)src[RIDX(i, j, dim)].blue) / 3;

      dest[RIDX(dim - j - 1, dim - i - 1, dim)].blue = ((int)src[RIDX(i, j, dim)].red +
						       (int)src[RIDX(i, j, dim)].green +
						       (int)src[RIDX(i, j, dim)].blue) / 3;

    }
}

/*
 * complex - Your current working version of complex
 * IMPORTANT: This is the version you will be graded on
 */
char complex_descr[] = "complex: Current working version";
void complex(int dim, pixel *src, pixel *dest)
{
   version3_complex(dim, src, dest);
}

/*********************************************************************
 * register_complex_functions - Register all of your different versions
 *     of the complex kernel with the driver by calling the
 *     add_complex_function() for each test function. When you run the
 *     driver program, it will test and report the performance of each
 *     registered test function.
 *********************************************************************/

void register_complex_functions()
{
   add_complex_function(&complex, complex_descr);
   add_complex_function(&naive_complex, naive_complex_descr);
  // add_complex_function(&version1_complex, version1_complex_descr);
  // add_complex_function(&version2_complex, version2_complex_descr);
   add_complex_function(&version3_complex, version3_complex_descr);
}

/***************
 * MOTION KERNEL
 **************/

/***************************************************************
 * Various helper functions for the motion kernel
 * You may modify these or add new ones any way you like.
 **************************************************************/

/*
 * weighted_combo - Returns new pixel value at (i,j)
 */
static pixel weighted_combo(int dim, int i, int j, pixel *src)
{
  int ii, jj;
  pixel current_pixel;

  int red, green, blue;
  red = green = blue = 0;

  int num_neighbors = 0;
  for(ii=0; ii < 3; ii++)
    for(jj=0; jj < 3; jj++)
      if ((i + ii < dim) && (j + jj < dim))
      {
	num_neighbors++;
	red += (int) src[RIDX(i+ii,j+jj,dim)].red;
	green += (int) src[RIDX(i+ii,j+jj,dim)].green;
	blue += (int) src[RIDX(i+ii,j+jj,dim)].blue;
      }

  current_pixel.red = (unsigned short) (red / num_neighbors);
  current_pixel.green = (unsigned short) (green / num_neighbors);
  current_pixel.blue = (unsigned short) (blue / num_neighbors);

  return current_pixel;
}

// /*
//  * weighted_combo2 - Returns new pixel value at (i,j), optimized with loop unrolling and inline if statements
//  */
// __attribute__((always_inline)) static pixel weighted_combo2(int dim, int i, int j, pixel *src)
// {
//   int ii, jj;
//   pixel current_pixel = {0, 0, 0};
//   int red, green, blue;
//   red = green = blue = 0;
//   int num_neighbors = 0;
//   for (ii = 0; ii < 3; ii++)
//   {
    
//       if ((i + ii < dim))
//       {
//         pixel null_pixel = {0, 0, 0};
//         num_neighbors++;
//         pixel value0 = src[RIDX(i+ii,j,dim)];
//         pixel value1 = ((j+1) < dim) ? src[RIDX(i+ii,j+1,dim)] : null_pixel;
//         num_neighbors = ((j+1) < dim) ? num_neighbors + 1 : num_neighbors;
//         pixel value2 = ((j+2) < dim) ? src[RIDX(i+ii,j+2,dim)] : null_pixel;
//         num_neighbors = ((j+2) < dim) ? num_neighbors + 1 : num_neighbors;
	      
// 	      red += (int) (value0.red + value1.red + value2.red);
// 	      green += (int) (value0.green + value1.green + value2.green);
// 	      blue += (int) (value0.blue + value1.blue + value2.blue);

        
//       }

//   current_pixel.red = (unsigned short) (red / num_neighbors);
//   current_pixel.green = (unsigned short) (green / num_neighbors);
//   current_pixel.blue = (unsigned short) (blue / num_neighbors);
//   }
   
//   return current_pixel;
// }

/*
 * weighted_comb32 - Returns new pixel value at (i,j), optimized with loop unrolling and inline if statements
 */
// __attribute__((always_inline)) static pixel weighted_combo_9_neighbors(int dim, int i, int j, pixel *src)
// {
//   int ii;
//   pixel current_pixel = {0, 0, 0};
//   int red, green, blue;
//   red = green = blue = 0;
//   for (ii = 0; ii < 3; ii++)
//   {
//       pixel value0 = src[RIDX(i+ii,j,dim)];
//       pixel value1 = src[RIDX(i+ii,j+1,dim)];
//       pixel value2 = src[RIDX(i+ii,j+2,dim)];
	      
// 	    red += (int) (value0.red + value1.red + value2.red);
// 	    green += (int) (value0.green + value1.green + value2.green);
// 	    blue += (int) (value0.blue + value1.blue + value2.blue);  
//   }
//   current_pixel.red = (unsigned short) (red / 9);
//   current_pixel.green = (unsigned short) (green / 9);
//   current_pixel.blue = (unsigned short) (blue / 9);
   
//   return current_pixel;
// }

// /*
//  * weighted_comb32 - Returns new pixel value at (i,j), optimized with loop unrolling and inline if statements
//  */
// __attribute__((always_inline)) static pixel weighted_combo_6_neighbors(int dim, int i, int j, pixel *src)
// {
//   int ii, jj;
//   pixel current_pixel = {0, 0, 0};
//   int red, green, blue;
//   red = green = blue = 0;
//   if (i != dim - 2) 
//   {
//     for (ii = 0; ii < 3; ii++)
//     {
//       pixel value0 = src[RIDX(i+ii,j,dim)];
//       pixel value1 = src[RIDX(i+ii,j+1,dim)];
	      
// 	    red += (int) (value0.red + value1.red);
// 	    green += (int) (value0.green + value1.green);
// 	    blue += (int) (value0.blue + value1.blue); 
//     }
//     current_pixel.red = (unsigned short) (red / 6);
//     current_pixel.green = (unsigned short) (green / 6);
//     current_pixel.blue = (unsigned short) (blue / 6);
//   }
//   else 
//   {
//     for (jj = 0; jj < 3; jj++)
//     {
//       pixel value0 = src[RIDX(i,j+jj,dim)];
//       pixel value1 = src[RIDX(i+1,j+jj,dim)];
	      
// 	    red += (int) (value0.red + value1.red);
// 	    green += (int) (value0.green + value1.green);
// 	    blue += (int) (value0.blue + value1.blue);

      
//     }
//     current_pixel.red = (unsigned short) (red / 6);
//     current_pixel.green = (unsigned short) (green / 6);
//     current_pixel.blue = (unsigned short) (blue / 6);
//   }
//   return current_pixel;
// }

// /*
//  * weighted_comb32 - Returns new pixel value at (i,j), optimized with loop unrolling and inline if statements
//  */
// __attribute__((always_inline)) static pixel weighted_combo_3_neighbors(int dim, int i, int j, pixel *src)
// {
//   int ii, jj;
//   pixel current_pixel = {0, 0, 0};
//   int red, green, blue;
//   red = green = blue = 0;
//   if (j == dim - 1) 
//   {
//     pixel value0 = src[RIDX(i,j,dim)];
//     pixel value1 = src[RIDX(i+1,j,dim)];
//     pixel value2 = src[RIDX(i+2,j,dim)];
	      
// 	  red += (int) (value0.red + value1.red + value2.red);
// 	  green += (int) (value0.green + value1.green + value2.green);
// 	  blue += (int) (value0.blue + value1.blue + value2.blue);

//     current_pixel.red = (unsigned short) (red / 3);
//     current_pixel.green = (unsigned short) (green / 3);
//     current_pixel.blue = (unsigned short) (blue / 3);
    
//   }
//   else 
//   {
//     pixel value0 = src[RIDX(i,j,dim)];
//     pixel value1 = src[RIDX(i,j+1,dim)];
//     pixel value2 = src[RIDX(i,j+2,dim)];
	      
// 	  red += (int) (value0.red + value1.red + value2.red);
// 	  green += (int) (value0.green + value1.green + value2.green);
// 	  blue += (int) (value0.blue + value1.blue + value2.blue);

//     current_pixel.red = (unsigned short) (red / 3);
//     current_pixel.green = (unsigned short) (green / 3);
//     current_pixel.blue = (unsigned short) (blue / 3);
//   }
//   return current_pixel;
// }

/*
 * weighted_comb32 - Returns new pixel value at (i,j), optimized with loop unrolling and inline if statements
 */
__attribute__((always_inline)) static pixel weighted_combo_4_neighbors(int dim, int i, int j, pixel *src)
{
  pixel current_pixel = {0, 0, 0};
  int red, green, blue;
  red = green = blue = 0;
  pixel value0 = src[RIDX(i,j,dim)];
  pixel value1 = src[RIDX(i,j+1,dim)];
  pixel value2 = src[RIDX(i+1,j+1,dim)];
  pixel value3 = src[RIDX(i+1,j,dim)];

  red += (int) (value0.red + value1.red + value2.red + value3.red);
	green += (int) (value0.green + value1.green + value2.green + value3.green);
	blue += (int) (value0.blue + value1.blue + value2.blue + value3.blue);

  current_pixel.red = (unsigned short) (red / 4);
  current_pixel.green = (unsigned short) (green / 4);
  current_pixel.blue = (unsigned short) (blue / 4);

  return current_pixel;
}


//   /******************************************************
//    * Your different versions of the motion kernel go here
//    ******************************************************/

//   /*
//    * version3_motion - The third test version of motion with different weighted_combo functions
//    */
//   char version3_motion_descr[] = "version3_motion: Second test version with tiling and separate i/j increment vals";
//   __attribute__((always_inline)) void version3_motion(int dim, pixel *src, pixel *dest)
//   {
//     int i, j;
//     for (i = 0; i < dim - 2; i++)
//     {
//       for (j = 0; j < dim; j++)
//       {
//         if (j == dim-2) {
//           // 6 neighbors
//           dest[RIDX(i, j, dim)] = weighted_combo_6_neighbors(dim, i, j, src);
//         }
//         else if (j == dim-1) 
//         {
//           //3 neighbors
//           dest[RIDX(i, j, dim)] = weighted_combo_3_neighbors(dim, i, j, src);
//         }
//         else 
//         {
//           // 9 neighbors
//           dest[RIDX(i, j, dim)] = weighted_combo_9_neighbors(dim, i, j, src);   
//         }
//       }
//     }

//     //Finish i
//     for (j = 0; j < dim - 2; j++) 
//     {
//       // 6 neighbors
//       dest[RIDX(i, j, dim)] = weighted_combo_6_neighbors(dim, i, j, src);
//     }

//     i++;
//     for (j = 0; j < dim - 2; j++) 
//     {
//       // 3 neighbors
//       dest[RIDX(i, j, dim)] = weighted_combo_3_neighbors(dim, i, j, src);
//     }

//     i = dim - 2;
//     j = dim - 2;

//     //call four neighbors
//     dest[RIDX(i, j, dim)] = weighted_combo_4_neighbors(dim, i, j, src);

//     i = dim - 1;
//     int red, green, blue;
//     red = green = blue = 0;
//     pixel value0 = src[RIDX(i,j,dim)];
//     pixel value1 = src[RIDX(i,j+1,dim)];
//     red += (int) (value0.red + value1.red);
// 	  green += (int) (value0.green + value1.green);
// 	  blue += (int) (value0.blue + value1.blue);

//     dest[RIDX(i, j, dim)].red = (unsigned short) (red / 2);
//     dest[RIDX(i, j, dim)].green = (unsigned short) (green / 2);
//     dest[RIDX(i, j, dim)].blue = (unsigned short) (blue / 2);

//     i = dim - 2;
//     j = dim - 1;
//     red = green = blue = 0;
//     value0 = src[RIDX(i,j,dim)];
//     value1 = src[RIDX(i+1,j,dim)];
//     red += (int) (value0.red + value1.red);
//     green += (int) (value0.green + value1.green);
//     blue += (int) (value0.blue + value1.blue);

//     dest[RIDX(i, j, dim)].red = (unsigned short) (red / 2);
//     dest[RIDX(i, j, dim)].green = (unsigned short) (green / 2);
//     dest[RIDX(i, j, dim)].blue = (unsigned short) (blue / 2);

//     dest[RIDX(dim-1, dim-1, dim)] = src[RIDX(dim-1, dim-1, dim)];

//   }

  //   /*
  //  * version2_motion - The second test version of motion with tiling with different i and j increment vals
  //  */
  // char version2_motion_descr[] = "version2_motion: Second test version with tiling and separate i/j increment vals";
  // __attribute__((always_inline)) void version2_motion(int dim, pixel *src, pixel *dest)
  // {
  //   int i, ii, j, jj, dim1, dim2;
  //   int w = 16;
  //   int v = 16;
  //   for (i = 0; i < dim; i += w)
  //     for (j = 0; j < dim; j += v)
  //     {

  //       for (ii = i; ii < i + w; ii++)
  //       {
  //         for (jj = j; jj < j + v; jj++)
  //         {

  //           dest[RIDX(ii, jj, dim)] = weighted_combo2(dim, ii, jj, src);
  //         }
  //       }
  //     }
  // }

  /*
   * version3_motion - The third test version of motion with different weighted_combo functions
   */
  char version4_motion_descr[] = "version4_motion: Fourth test version with col1, col2, and col3 values/sliding window implementation";
  __attribute__((always_inline)) void version4_motion(int dim, pixel *src, pixel *dest)
  {
    int i, j;
    int col1_red, col2_red, col3_red;
    int col1_green, col2_green, col3_green;
    int col1_blue, col2_blue, col3_blue;
    for (i = 0; i < dim - 2; i++)
    {
      
      col1_red =  (int) (src[RIDX(i,0,dim)].red + src[RIDX(i+1,0,dim)].red + src[RIDX(i+2,0,dim)].red);
      col1_green =  (int) (src[RIDX(i,0,dim)].green + src[RIDX(i+1,0,dim)].green + src[RIDX(i+2,0,dim)].green);
      col1_blue =  (int) src[RIDX(i,0,dim)].blue + src[RIDX(i+1,0,dim)].blue + src[RIDX(i+2,0,dim)].blue;

      col2_red = (int) (src[RIDX(i,1,dim)].red + src[RIDX(i+1,1,dim)].red + src[RIDX(i+2,1,dim)].red);
      col2_green = (int) (src[RIDX(i,1,dim)].green + src[RIDX(i+1,1,dim)].green + src[RIDX(i+2,1,dim)].green);
      col2_blue =  (int) (src[RIDX(i,1,dim)].blue + src[RIDX(i+1,1,dim)].blue + src[RIDX(i+2,1,dim)].blue);
      for (j = 0; j < dim - 2; j++)
      {
        col3_red = (int) (src[RIDX(i,j+2,dim)].red + src[RIDX(i+1,j+2,dim)].red + src[RIDX(i+2,j+2,dim)].red);
        col3_green = (int)  (src[RIDX(i,j+2,dim)].green + src[RIDX(i+1,j+2,dim)].green + src[RIDX(i+2,j+2,dim)].green);
        col3_blue =  (int) (src[RIDX(i,j+2,dim)].blue + src[RIDX(i+1,j+2,dim)].blue + src[RIDX(i+2,j+2,dim)].blue);


        dest[RIDX(i, j, dim)].red = (unsigned short) ((col1_red + col2_red + col3_red) / 9);
        dest[RIDX(i, j, dim)].green = (unsigned short) ((col1_green + col2_green + col3_green) / 9);
        dest[RIDX(i, j, dim)].blue = (unsigned short) ((col1_blue + col2_blue + col3_blue) / 9);

        col1_red = col2_red;
        col1_green = col2_green;
        col1_blue = col2_blue;
        col2_red = col3_red;
        col2_green = col3_green;
        col2_blue = col3_blue;
        
      }
      dest[RIDX(i, j, dim)].red = (unsigned short) ((col1_red + col2_red) / 6);
      dest[RIDX(i, j, dim)].green = (unsigned short) ((col1_green + col2_green) / 6);
      dest[RIDX(i, j, dim)].blue = (unsigned short) ((col1_blue + col2_blue) / 6);
      col1_red = col2_red;
      col1_green = col2_green;
      col1_blue = col2_blue;

      j++;
      dest[RIDX(i, j, dim)].red = (unsigned short) ((col1_red) / 3);
      dest[RIDX(i, j, dim)].green = (unsigned short) ((col1_green) / 3);
      dest[RIDX(i, j, dim)].blue = (unsigned short) ((col1_blue) / 3);

    }

    //Finish i
    col1_red = (int) (src[RIDX(i,0,dim)].red + src[RIDX(i+1,0,dim)].red);
    col1_green =  (int) (src[RIDX(i,0,dim)].green + src[RIDX(i+1,0,dim)].green);
    col1_blue = (int)  (src[RIDX(i,0,dim)].blue + src[RIDX(i+1,0,dim)].blue);

    col2_red =  (int) (src[RIDX(i,1,dim)].red + src[RIDX(i+1,1,dim)].red);
    col2_green = (int) (src[RIDX(i,1,dim)].green + src[RIDX(i+1,1,dim)].green);
    col2_blue = (int) (src[RIDX(i,1,dim)].blue + src[RIDX(i+1,1,dim)].blue);

    for (j = 0; j < dim - 2; j++) 
    {
      // 6 neighbors
      col3_red = (int) (src[RIDX(i,j+2,dim)].red + src[RIDX(i+1,j+2,dim)].red);
      col3_green =  (int) (src[RIDX(i,j+2,dim)].green + src[RIDX(i+1,j+2,dim)].green);
      col3_blue = (int) (src[RIDX(i,j+2,dim)].blue + src[RIDX(i+1,j+2,dim)].blue);


      dest[RIDX(i, j, dim)].red = (unsigned short) ((col1_red + col2_red + col3_red) / 6);
      dest[RIDX(i, j, dim)].green = (unsigned short) ((col1_green + col2_green + col3_green) / 6);
      dest[RIDX(i, j, dim)].blue = (unsigned short) ((col1_blue + col2_blue + col3_blue) / 6);

      col1_red = col2_red;
      col1_green = col2_green;
      col1_blue = col2_blue;
      col2_red = col3_red;
      col2_green = col3_green;
      col2_blue = col3_blue;
    }

    i++;
    col1_red =  (int) src[RIDX(i,0,dim)].red;
    col1_green =  (int) src[RIDX(i,0,dim)].green;
    col1_blue = (int) src[RIDX(i,0,dim)].blue;

    col2_red =  src[RIDX(i,1,dim)].red;
    col2_green =  src[RIDX(i,1,dim)].green;
    col2_blue =  src[RIDX(i,1,dim)].blue;
    for (j = 0; j < dim - 2; j++) 
    {
      // 3 neighbors
      col3_red = (int) src[RIDX(i,j+2,dim)].red;
      col3_green = (int) src[RIDX(i,j+2,dim)].green;
      col3_blue = (int) src[RIDX(i,j+2,dim)].blue;

      dest[RIDX(i, j, dim)].red = (unsigned short) ((col1_red + col2_red + col3_red) / 3);
      dest[RIDX(i, j, dim)].green = (unsigned short) ((col1_green + col2_green + col3_green) / 3);
      dest[RIDX(i, j, dim)].blue = (unsigned short) ((col1_blue + col2_blue + col3_blue) / 3);

      col1_red = col2_red;
      col1_green = col2_green;
      col1_blue = col2_blue;
      col2_red = col3_red;
      col2_green = col3_green;
      col2_blue = col3_blue;
    }

    i = dim - 2;
    j = dim - 2;

    //call four neighbors
    dest[RIDX(i, j, dim)] = weighted_combo_4_neighbors(dim, i, j, src);

    i = dim - 1;
    int red, green, blue;
    red = green = blue = 0;
    pixel value0 = src[RIDX(i,j,dim)];
    pixel value1 = src[RIDX(i,j+1,dim)];
    red += (int) (value0.red + value1.red);
	  green += (int) (value0.green + value1.green);
	  blue += (int) (value0.blue + value1.blue);

    dest[RIDX(i, j, dim)].red = (unsigned short) (red / 2);
    dest[RIDX(i, j, dim)].green = (unsigned short) (green / 2);
    dest[RIDX(i, j, dim)].blue = (unsigned short) (blue / 2);

    i = dim - 2;
    j = dim - 1;
    red = green = blue = 0;
    value0 = src[RIDX(i,j,dim)];
    value1 = src[RIDX(i+1,j,dim)];
    red += (int) (value0.red + value1.red);
    green += (int) (value0.green + value1.green);
    blue += (int) (value0.blue + value1.blue);

    dest[RIDX(i, j, dim)].red = (unsigned short) (red / 2);
    dest[RIDX(i, j, dim)].green = (unsigned short) (green / 2);
    dest[RIDX(i, j, dim)].blue = (unsigned short) (blue / 2);

    dest[RIDX(dim-1, dim-1, dim)] = src[RIDX(dim-1, dim-1, dim)];

  }

  /*
   * naive_motion - The naive baseline version of motion
   */
  char naive_motion_descr[] = "naive_motion: Naive baseline implementation";
  void naive_motion(int dim, pixel *src, pixel *dst)
  {
    int i, j;

    for (i = 0; i < dim; i++)
      for (j = 0; j < dim; j++)
        dst[RIDX(i, j, dim)] = weighted_combo(dim, i, j, src);
  }

  /*
   * motion - Your current working version of motion.
   * IMPORTANT: This is the version you will be graded on
   */
  char motion_descr[] = "motion: Current working version";
  void motion(int dim, pixel *src, pixel *dst)
  {
    version4_motion(dim, src, dst);
  }

  /*********************************************************************
   * register_motion_functions - Register all of your different versions
   *     of the motion kernel with the driver by calling the
   *     add_motion_function() for each test function.  When you run the
   *     driver program, it will test and report the performance of each
   *     registered test function.
   *********************************************************************/

  void register_motion_functions()
  {
    // add_motion_function(&motion, motion_descr);
     add_motion_function(&naive_motion, naive_motion_descr);
    //add_motion_function(&version2_motion, version2_motion_descr);
    add_motion_function(&version4_motion, version4_motion_descr);
  }
