/*
 * mm.c
 * 
 * Kira Halls - u1250109
 * 
 * This class mimics memory allocation and deallocation through the use of three functions, mm_init, mm_malloc, and 
 * mm_free. My implementation relies on an explicit list to track the freed blocks, allowing the program to reallocate
 * them if we can fit our new malloc request in one of the free spaces. It also includes a sentinel and terminal
 * to allow for easy page traversal without crossing over page boundaries. Each block includes a header and footer 
 * that lists the size and whether or not it is allocated. Freed blocks are immediately coalesced where applicable
 * to reduce fragmentation and optimize space usage. New pages are mapped only when there are no currently 
 * free blocks that are large enough to hold the allocation request, thereby reducing costly calls to mem_map. Finally, 
 * a split_block function divides the free blocks into an allocated section and a free section, thereby reducing 
 * wasted space that would occur from simply allocating the entire free block, even if the request was much smaller.
 * 
 */
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <unistd.h>
#include <string.h>
#include <math.h>

#include "mm.h"
#include "memlib.h"

/* always use 16-byte alignment */
#define ALIGNMENT 16

/* new pages are padded with 8 byte alignment */
#define ALIGNMENT_PADDING 8

/* page overhead is the size of sentinel, terminators, etc. */
#define PAGE_OVERHEAD (sizeof(block_header) + sizeof(block_footer) + sizeof(block_header))

/* rounds up to the nearest multiple of ALIGNMENT */
#define ALIGN(size) (((size) + (ALIGNMENT-1)) & ~(ALIGNMENT-1))

/* rounds up to the nearest multiple of mem_pagesize() */
#define PAGE_ALIGN(size) (((size) + (mem_pagesize()-1)) & ~(mem_pagesize()-1))

// This assumes you have a struct or typedef called "block_header" and "block_footer"
#define OVERHEAD (sizeof(block_header)+sizeof(block_footer))

// Given a payload pointer, get the header or footer pointer
#define HDRP(bp) ((char *)(bp) - sizeof(block_header))
#define FTRP(bp) ((char *)(bp)+GET_SIZE(HDRP(bp))-OVERHEAD)

// Given a payload pointer, get the next or previous payload pointer
#define NEXT_BLKP(bp) ((char *)(bp) + GET_SIZE(HDRP(bp)))
#define PREV_BLKP(bp) ((char *)(bp)-GET_SIZE((char *)(bp)-OVERHEAD))

// Given a pointer to a header, get or set its value
#define GET(p)      (*(size_t *)(p))
#define PUT(p, val) (*(size_t *)(p) = (val))

// Combine a size and alloc bit
#define PACK(size, alloc) ((size) | (alloc))

// Given a header pointer, get the alloc or size 
#define GET_ALLOC(p) (GET(p) & 0x1)
#define GET_SIZE(p)  (GET(p) & ~0xF)


//A struct to define a node in the free list. It holds pointers to the previous free node and the next free node
typedef struct node 
{
  struct node* prev;
  struct node* next;
} node;

// A struct to define the block header. It holds the size of the block and uses the LSB of that number as the 'allocated' bit
typedef struct block_header
{
  size_t size;
} block_header;

// A struct to define the block footer. It just holds the size of the block.
typedef struct block_footer 
{
  size_t size;
} block_footer;


int num_free_pointers = 0;
int num_pages = 0;
// Part of attempts at optimizing mem_map calls
//size_t previous_pages_allocated = 0;
//size_t max_page_size = 16384;

// The first free node in list
node* first_free = NULL;


//Forward declarations for helper functions
static void add_free(node* freed_pointer);
static void delete_node(node* allocated_pointer);
static void split_block(node* split_block, size_t allocation_size);
static void* coalesce(void* pointer);
static void* extend(size_t size);
void print_heap(void* start, int N);
static void* find_space(size_t size);

/* 
 * mm_init - initialize the malloc package. Set global variables to null and 0 as needed, then call extend
 * to create a new initial page. 
 */
int mm_init(void)
{
  first_free = NULL;
  num_free_pointers = 0;
  num_pages = 0;

  //Part of attempts at mem_map optimization
  //previous_pages_allocated = 0;

  void* ptr = extend(1);

  //Part of attempts at mem_map optimization
  //previous_pages_allocated = 1;
  
  //Return -1 if extend failed to allocate a new page
  if (ptr == NULL)
  {
    return -1;
  }
  return 0;
}

/* 
 * mm_malloc - Allocate a block by checking to see if we have a space already allocated for it using 
 * the helper function find_space. If no space exists, call extend to get a new page. Else, the returned pointer
 * will be set as the new allocated block. Update the block header and footer with the new size, and set them 
 * as allocated, done by calling split_block helper function.
 */
void *mm_malloc(size_t size)
{
  // allocation_size is part of attempts at optimizing calls to mem_map
  //int allocation_size = ALIGN(size);
  int newsize = ALIGN(size);
  node* allocated_block = find_space(newsize);

  // If there isn't enough space, call extend
  if (allocated_block == NULL)
  {
    // Attempted mem_map optimization where I map either double the num pages previous or always map four pages. Caused errors so commented out
    //int i;
    // for (i = 0; i < 4; i++)
    // {
    //   extend(PAGE_ALIGN(newsize));
    // }
    //previous_pages_allocated = i + 1;
   // printf("%s %d\n", "previous pages allocated: ", previous_pages_allocated);
    allocated_block = (node*) extend(PAGE_ALIGN(newsize * 2));
    
    split_block(allocated_block, newsize);
  }
  else 
  {
    if (newsize + (2*sizeof(block_header)) == GET_SIZE(HDRP(allocated_block)))
    {
      delete_node(allocated_block);
      PUT(HDRP(allocated_block), PACK(newsize + (2 * sizeof(block_header)), 1));
      PUT(FTRP(allocated_block), PACK(newsize + (2 * sizeof(block_footer)), 1));
    }
    else 
    {
      split_block(allocated_block, newsize);
    }   
  }
  return allocated_block;
}

/*
 * mm_free - Used to free a block. This function updates the freed block header and footers to be unallocated, 
 * adds the block to the free list using add_free, then calls coalesce to reduce fragmentation.
 */
void mm_free(void *ptr)
{
  size_t size = GET_SIZE(HDRP(ptr));
   
  PUT(HDRP(ptr), PACK(size, 0));
  add_free((node*) ptr);
  void* pointer = coalesce(ptr);

  //Check if page is completely unallocated, unmap if so
  void* left_neighbor = PREV_BLKP(pointer);
  void* right_neighbor = NEXT_BLKP(pointer);

  // Unmap checking
  if (GET(left_neighbor) == 0x11 && GET(right_neighbor) == PACK(0, 1) && num_pages > 1)
  {
    delete_node((node*) pointer);
    mem_unmap(pointer - 32, GET_SIZE(HDRP(pointer)) + PAGE_OVERHEAD + ALIGNMENT_PADDING);
    num_pages--;
  }
}

/*
* print_heap - A helper function that prints the contents of the heap starting at "start" and prints N words 
* of the heap. Used for checking the accuracy of the heap values.
*/
void print_heap(void* start, int N)
{
  printf("printing %d words of heap\n", N);
  size_t* p = (size_t*)start;

  for(int i = 0; i < N; i++)
  {
    printf("%p\t0x%zx\n", p, *p);
    p++;
  }
}

/*
* add_free - creates a new node at the beginning of the free list and changes it to be the new first_free node
*/
void add_free(node* freed_pointer)
{
  
  num_free_pointers += 1;
  freed_pointer->prev = NULL;
  freed_pointer->next = NULL;
  
  if (first_free != NULL && first_free != 0x0)
  {
    first_free->prev = freed_pointer;
    freed_pointer->next = first_free; 
  }
  else 
  {
    freed_pointer->next = NULL;
  }
  
  first_free = freed_pointer;
}

/*
* delete_node - A helper function that deals with deleting a node from the explicit free list. It checks 
* to see if the node is the first_free node, in which case it resets the first_free variable and updates the list 
* accordingly. If it isn't, then it just updates the list as needed.
*/
void delete_node(node* allocated_pointer)
{

  // if it's the first_free there's only one node in the list to adjust (first_free->next)
  if (first_free == allocated_pointer)
  {
    // If it's the only one, just replace with NULL as every call to delete is followed by an add
    if (num_free_pointers == 1)
    {
      num_free_pointers -= 1;
      first_free = NULL;
    }
    else 
    {
      num_free_pointers -= 1;
      node* next_node = first_free->next;
      next_node->prev = NULL;
      first_free = next_node;
      allocated_pointer->prev = NULL;
      allocated_pointer->next = NULL;
    } 
  }

  //If not first free, we need to alter allocated_node->next and allocated_node->prev
  else 
  {
    num_free_pointers -=1;
    node* previous_node = allocated_pointer->prev;
    node* next_node = allocated_pointer->next;
    if (next_node != NULL)
    {
      next_node->prev = previous_node;
    }
    if (previous_node != NULL) 
    {
      previous_node->next = next_node;
    }
  
    allocated_pointer->prev = NULL;
    allocated_pointer->next = NULL;
  }
}

/*
* split_block - Helper function that deals with separating a free block into two sections, the newly allocated 
* section and the remaining free section. It calls delete_node, sets the header and footer of the allocated block,
* then calls add_free and updates the free block's header and footer
*/
void split_block(node* split_block, size_t allocation_size)
{

  size_t previous_free_size = GET_SIZE(HDRP(split_block));

  // First, delete it from the free_list
  delete_node(split_block);

  // Next, set the allocated bit header and footer
  PUT(HDRP(split_block), PACK(allocation_size + (2 * sizeof(block_header)), 1));
  PUT(FTRP(split_block), PACK(allocation_size + (2*sizeof(block_header)), 1));

  // Next, add the new free node
  add_free((node*) NEXT_BLKP(split_block));
  
  // Set the header and footer of the new first_free block
  PUT(HDRP(first_free), previous_free_size - GET_SIZE(HDRP(split_block)));
  PUT(FTRP(first_free), previous_free_size - GET_SIZE(HDRP(split_block)));
}

/*
* coalesce - helper function that combines consecutive free blocks into one larger free block. 
* It checks for four different cases and calls delete_node() and updates headers and footers as needed.
*/
void* coalesce(void* pointer)
{
  size_t prev_alloc = GET_ALLOC(HDRP(PREV_BLKP(pointer)));
  size_t next_alloc = GET_ALLOC(HDRP(NEXT_BLKP(pointer)));
  size_t size = GET_SIZE(HDRP(pointer));
  

  // Case 1 - previous and next blocks are both allocated
  if (prev_alloc && next_alloc)
  {
    PUT(HDRP(pointer), PACK(size, 0));
    PUT(FTRP(pointer), PACK(size, 0));
    return pointer;
  }

  // Case 2 - previous block is allocated, next block is free
  else if (prev_alloc && !next_alloc)
  {
    delete_node((node*) NEXT_BLKP(pointer));
    size += GET_SIZE(HDRP(NEXT_BLKP(pointer)));
    void* original_pointer_footer = FTRP(pointer);
    void* next_pointer_header = HDRP(NEXT_BLKP(pointer));

    // Now set new header and footer for coalesced block
    PUT(HDRP(pointer), PACK(size, 0));
    PUT(FTRP(pointer), PACK(size, 0));

    //Want to get rid of the footer of the newly freed block and the header of the next free block
    PUT(next_pointer_header, 0);
    PUT(original_pointer_footer, 0);
    
  }

  // Case 3 - previous  block is free, next block is allocated
  else if (!prev_alloc && next_alloc)
  {
    delete_node((node*) pointer);
    size += GET_SIZE(HDRP(PREV_BLKP(pointer)));
    void* original_pointer_header = HDRP(pointer);
    void* previous_pointer_footer = FTRP(PREV_BLKP(pointer));
    void* new_final_header = HDRP(PREV_BLKP(pointer));

    // Now set the header and footer for coalesced block
    PUT(FTRP(pointer), PACK(size, 0));
    PUT(new_final_header, PACK(size, 0));
    pointer = new_final_header + sizeof(block_header);

    //Want to get rid of the footer of the previous free block and the header of the current free block
    PUT(original_pointer_header, 0);
    PUT(previous_pointer_footer, 0);
  }

  // Case 4 - both previous and next are free
  else 
  {
    delete_node((node*) NEXT_BLKP(pointer));
    delete_node((node*) pointer);
    size += GET_SIZE(HDRP(PREV_BLKP(pointer))) + GET_SIZE(HDRP(NEXT_BLKP(pointer)));
    
    void* next_block_header = HDRP(NEXT_BLKP(pointer));
    void* original_block_header = HDRP(pointer);
    void* original_block_footer = FTRP(pointer);
    void* previous_block_footer = FTRP(PREV_BLKP(pointer));
    void* final_block_header = HDRP(PREV_BLKP(pointer));

    //Now set the header and footer for coalesced block
    PUT(final_block_header, PACK(size, 0));
    PUT(FTRP(NEXT_BLKP(pointer)), PACK(size, 0));
    pointer = final_block_header + sizeof(block_header);
    //Want to get rid of header of next block, header and footer of current block, and footer of previous
    PUT(next_block_header, 0);
    PUT(original_block_header, 0);
    PUT(original_block_footer, 0);
    PUT(previous_block_footer, 0);
    
    
  }
  return pointer;
}
  
  
/*
* extend - A helper function that calls mem_map when the user has requested more space than our current pages 
* have available. 
*/
void* extend(size_t size)
{
  // Attempted implementation of allocating double the page size of previous page size, caused errors so commented out
  // size_t new_page_size = fmax(PAGE_ALIGN(size + OVERHEAD + ALIGNMENT + PAGE_OVERHEAD), previous_page_size);
  // size_t chunk_size = 0;
  // if (previous_page_size == 0) 
  // {
  //   chunk_size = (new_page_size + OVERHEAD + 8) + PAGE_OVERHEAD;
  //   chunk_size = PAGE_ALIGN(new_page_size);
  // }
  // else if (previous_page_size < max_page_size)
  // {
  //   chunk_size = fmax(new_page_size, (2 * previous_page_size));
  //   chunk_size = PAGE_ALIGN(chunk_size);
  // }
  // else 
  // {
  //   chunk_size = PAGE_ALIGN(max_page_size);
  // }

  // parenthesis is payload (size) + overhead (header footer size) + 8 (alignment padding)
  size_t chunk_size = (size + OVERHEAD + ALIGNMENT_PADDING) + PAGE_OVERHEAD;
  chunk_size = PAGE_ALIGN(chunk_size);
  
  void* new_page = mem_map(chunk_size);
  num_pages += 1;
  
  if (new_page == NULL)
  {
    return NULL;
  }

  // Add alignment padding
  PUT(new_page, 0);
  new_page += ALIGNMENT_PADDING;

  // Add sentinel
  PUT(new_page, PACK(2 * sizeof(block_header), 1));
  PUT(new_page + (sizeof(block_header)), PACK(2 * sizeof(block_footer), 1));
  new_page += 2 * sizeof(block_header);

  //Add free block 
  size_t free_block_size = chunk_size - ((PAGE_OVERHEAD)  + (ALIGNMENT_PADDING));
  PUT(new_page, PACK(free_block_size, 0));
  new_page += sizeof(block_header);
  PUT(FTRP(new_page), PACK(free_block_size, 0));
  add_free((node*) new_page);

  // Add terminator
  PUT(HDRP(NEXT_BLKP(new_page)), PACK(0, 1));

  return new_page;
}

/*
* find_space - given a parameter 'size', search through the free_list to see if there is a block big enough to hold
* the new space the user has tried to allocate
*/
void* find_space(size_t size)
{
  size_t free_space;
  node* current_node = first_free;
  while (current_node != NULL)
  {
    free_space = GET_SIZE(HDRP(current_node));

    // If the current pointer can fit the new allocation, then return that pointer
    if ((size + (2 * sizeof(block_header)))  <= free_space)
    {
      return current_node;
    }
    // If it can't fit, check the next free node
    else 
    {
      current_node = current_node->next;
    }
  }

  // return NULL if none of them are free
  return NULL;
}
