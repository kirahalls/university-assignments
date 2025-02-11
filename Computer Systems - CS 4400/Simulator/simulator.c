/*
  Author: Daniel Kopta
  CS 4400, University of Utah

  * Simulator handout
  * A simple x86-like processor simulator.
  * Read in a binary file that encodes instructions to execute.
  * Simulate a processor by executing instructions one at a time and appropriately 
  * updating register and memory contents.

  * Some code and pseudo code has been provided as a starting point.

*/

#include <stdio.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>
#include "instruction.h"

// Forward declarations for helper functions
unsigned int get_file_size(int file_descriptor);
unsigned int* load_file(int file_descriptor, unsigned int size);
instruction_t* decode_instructions(unsigned int* bytes, unsigned int num_instructions);
unsigned int execute_instruction(unsigned int program_counter, instruction_t* instructions, 
				 unsigned int* registers, unsigned char* memory);
void print_instructions(instruction_t* instructions, unsigned int num_instructions);
void error_exit(const char* message);

// 17 registers
#define NUM_REGS 17
// 1024-byte stack
#define STACK_SIZE 1024

int main(int argc, char** argv)
{
  // Make sure we have enough arguments
  if(argc < 2)
    error_exit("must provide an argument specifying a binary file to execute");

  // Open the binary file
  int file_descriptor = open(argv[1], O_RDONLY);
  if (file_descriptor == -1) 
    error_exit("unable to open input file");

  // Get the size of the file
  unsigned int file_size = get_file_size(file_descriptor);
  // Make sure the file size is a multiple of 4 bytes
  // since machine code instructions are 4 bytes each
  if(file_size % 4 != 0)
    error_exit("invalid input file");

  // Load the file into memory
  // We use an unsigned int array to represent the raw bytes
  // We could use any 4-byte integer type
  unsigned int* instruction_bytes = load_file(file_descriptor, file_size);
  close(file_descriptor);

  unsigned int num_instructions = file_size / 4;


  /****************************************/
  /**** Begin code to modify/implement ****/
  /****************************************/

  // Allocate and decode instructions (left for you to fill in)
  instruction_t* instructions = decode_instructions(instruction_bytes, num_instructions);

  // Optionally print the decoded instructions for debugging
  // Will not work until you implement decode_instructions
  // Do not call this function in your handed in final version
  //print_instructions(instructions, num_instructions);


// Once you have completed part 1 (decoding instructions), uncomment the below block


  // Allocate and initialize registers
  unsigned int* registers = (unsigned int*)malloc(sizeof(unsigned int) * NUM_REGS);

  for (int i = 0; i < 17; i++) 
  {
    if (i == 8) 
    {
      registers[i] = 1024;
    }
    else 
    {
      registers[i] = 0;
    }
  }

  // Stack memory is byte-addressed, so it must be a 1-byte type
  unsigned char* memory = (unsigned char*)malloc(sizeof(unsigned char*) * 1024);

  for (int i = 0; i < 1024; i++) 
  {
    memory[i] = 0;
    //memset(memory + i, 0, sizeof(int));
  }


  // Run the simulation
  unsigned int program_counter = 0;

  // program_counter is a byte address, so we must multiply num_instructions by 4 to get the address past the last instruction
  while(program_counter != num_instructions * 4)
  {
    program_counter = execute_instruction(program_counter, instructions, registers, memory);
  }



  
  return 0;
}



/*
 * Decodes the array of raw instruction bytes into an array of instruction_t
 * Each raw instruction is encoded as a 4-byte unsigned int
*/
instruction_t* decode_instructions(unsigned int* bytes, unsigned int num_instructions)
{
  instruction_t* retval;
  instruction_t* temp_arr = (instruction_t*) malloc((sizeof(instruction_t) * num_instructions));

  unsigned int current_byte;
  unsigned char opcode;
  unsigned char first_register;
  unsigned char second_register;
  int16_t immediate;
  instruction_t current_instruct;

  for(unsigned int i = 0; i < num_instructions; i++) {
    current_byte = bytes[i];

    //Shift right 27 bits to get opcode
    opcode = current_byte >> 27;
    current_instruct.opcode = opcode;

    //Shift right 22 to get second register to end, then perform bitwise and operation with decimal equivalent of five 1 bit values
    first_register = current_byte >> 22;
    first_register = first_register & 31;
    current_instruct.first_register = first_register;

    //Shift right 17 to get second register to end, then perform bitwise and operation with decimal equivalent of five 1 bit values
    second_register = current_byte >> 17;
    second_register = second_register & 31;
    current_instruct.second_register = second_register;

    //Bitwise and operation using the decimal equivalent of sixteen 1 bit values
    immediate = current_byte & 65535;
    current_instruct.immediate = immediate;

    temp_arr[i] = current_instruct;
  }

  retval = temp_arr;
  return retval;
}


/*
 * Executes a single instruction and returns the next program counter
*/
unsigned int execute_instruction(unsigned int program_counter, instruction_t* instructions, unsigned int* registers, unsigned char* memory)
{
  // program_counter is a byte address, but instructions are 4 bytes each
  // divide by 4 to get the index into the instructions array
  instruction_t instr = instructions[program_counter / 4];

  //printf("%u\n", registers[8]);
  
  switch(instr.opcode)
  {
  case subl:
    // cast the immediate up to a 32-bit signed type
    // signed overflow will not happen since one of the operands is unsigned
    registers[instr.first_register] = registers[instr.first_register] - (int)instr.immediate;
    //memset(registers + instr.first_register, registers[instr.first_register] - instr.immediate, sizeof(unsigned int));
    break;
  case addl_reg_reg:
    registers[instr.second_register] = registers[instr.second_register] + registers[instr.first_register];
    break;
  case addl_imm_reg:
    registers[instr.first_register] = registers[instr.first_register] + (int)instr.immediate;
    break;
  case imull:
    registers[instr.second_register] = registers[instr.first_register] * registers[instr.second_register];
    break;
  case shrl:
    registers[instr.first_register] = registers[instr.first_register] >> 1;
    break;
  case movl_reg_reg:
  
    memcpy(registers + instr.second_register, registers + instr.first_register, sizeof(unsigned int));
    break;
  case movl_deref_reg:
    //printf("%u\n", registers[instr.first_register]+ instr.immediate);
    memcpy(registers + instr.second_register, memory + (registers[instr.first_register] + instr.immediate), sizeof(unsigned int));
    break;
  case movl_reg_deref:
  { 
    //printf("%u\n", memory[registers[instr.second_register] + instr.immediate]);
    memcpy(memory + (registers[instr.second_register] + instr.immediate), registers+instr.first_register, sizeof(unsigned int));
    break;
  }
  case movl_imm_reg: 
    registers[instr.first_register] = (int32_t) instr.immediate;
    break;
  case cmpl:
    {
    //Clear flags from previous iterations
    registers[0] = registers[0] & 0;

    //Casting to bigger types
    int32_t reg1 = (int32_t) registers[instr.first_register];
    int32_t reg2 = (int32_t) registers[instr.second_register];
    long long_reg1 = reg1;
    long long_reg2 = reg2;
    //Signed difference
    long difference = long_reg2 - long_reg1;

    //Setting values for determining overflow
    int32_t int_max = 2147483647;
    int32_t int_min = -2147483648;
    uint32_t uint_max = 4294967295;
    uint32_t uint_min = 0;
    //Unsigned difference
    uint64_t unsigned_difference = (uint64_t) registers[instr.second_register] - registers[instr.first_register];

    //Signed overflow
    if (difference > int_max || difference < int_min) 
    {
      registers[0] = registers[0] | 2048;
    }

    //Unsigned overflow CF
    if (unsigned_difference < uint_min || unsigned_difference > uint_max) 
    {
      registers[0] = registers[0] | 1;
    }

    //Zero flag
    if (difference == 0) 
    {
      registers[0] = registers[0] | 64;
    }

    //Significant bit
    if (abs(difference >> 31) == 1) 
    {
      registers[0] = registers[0] | 128;
    }
    break;
    }
  case je:
    if ((registers[0] & 64) == 64) 
    {
      memset(memory+registers[8], program_counter + 4, sizeof(unsigned int));
      program_counter += instr.immediate + 4;
      return program_counter;
    }
    else 
    {
      return program_counter + 4;
    }
    break;
  case jl:
    if (((registers[0] & 2048) == 2048) ^ ((registers[0] & 128)) == 128) 
    {
      memset(memory+registers[8], program_counter + 4, sizeof(unsigned int));
      program_counter += instr.immediate + 4;
      return program_counter;
    }
    else 
    {
      return program_counter + 4;
    }
    break;
  case jle:
   if ((((registers[0] & 2048) == 2048) ^ ((registers[0] & 128) == 128)) || ((registers[0] & 64) == 64)) 
    {
      memset(memory+registers[8], program_counter + 4, sizeof(unsigned int));
      program_counter += instr.immediate + 4;
      return program_counter;
    }
    else 
    {
      return program_counter + 4;
    }
    break;
  case jge:
   if (!(((registers[0] & 2048) == 2048) ^ ((registers[0] & 128)== 128))) 
    {
      memset(memory+registers[8], program_counter + 4, sizeof(unsigned int));
      program_counter += instr.immediate + 4;
      return program_counter;
    }
    else 
    {
      return program_counter + 4;
    }
    break;
  case jbe:
   if (((registers[0] & 1) == 1) || ((registers[0] & 64) == 64))
    {
      memset(memory+registers[8], program_counter + 4, sizeof(unsigned int));
      program_counter += instr.immediate + 4;
      return program_counter;
    }
    else 
    {
      return program_counter + 4;
    }
    break;
  case jmp:
    //memset(memory+registers[8], program_counter + 4, sizeof(unsigned int));
    program_counter += instr.immediate + 4;
    return program_counter;
    break;
  case call:
    registers[8] -= (unsigned int) 4;
    memset(memory+registers[8], program_counter + 4, sizeof(unsigned int));
    program_counter += instr.immediate + 4;
    return program_counter;
  case ret:
    if (registers[8] == 1024) 
    {
      exit(0);
    }
    else 
    {
      //printf("%u\n", memory[registers[8]]);
      program_counter = memory[registers[8]];
      //memcpy(&program_counter, memory + registers[8], sizeof(memory[registers[8]]));
      registers[8] += 4;
      return program_counter; 
    }
    break;
  case pushl:
    registers[8] -= 4;
    memcpy(memory + registers[8], registers + instr.first_register, sizeof(unsigned int));
    break;
  case popl:
    memcpy(registers + instr.first_register, memory + registers[8], sizeof(unsigned int));
    registers[8] += 4;
    break;
  case printr:
    printf("%d (0x%x)\n", registers[instr.first_register], registers[instr.first_register]);
    break;
  case readr:
    scanf("%d", &(registers[instr.first_register]));
    break;
 


  // TODO: Implement remaining instructions

  }

  // TODO: Don't always return program_counter + 4
  //       Some instructions will jump elsewhere

  // program_counter + 4 represents the subsequent instruction
  return program_counter + 4;
}


/***********************************************/
/**** Begin helper functions. Do not modify ****/
/***********************************************/

/*
 * Returns the file size in bytes of the file referred to by the given descriptor
*/
unsigned int get_file_size(int file_descriptor)
{
  struct stat file_stat;
  fstat(file_descriptor, &file_stat);
  return file_stat.st_size;
}

/*
 * Loads the raw bytes of a file into an array of 4-byte units
*/
unsigned int* load_file(int file_descriptor, unsigned int size)
{
  unsigned int* raw_instruction_bytes = (unsigned int*)malloc(size);
  if(raw_instruction_bytes == NULL)
    error_exit("unable to allocate memory for instruction bytes (something went really wrong)");

  int num_read = read(file_descriptor, raw_instruction_bytes, size);

  if(num_read != size)
    error_exit("unable to read file (something went really wrong)");

  return raw_instruction_bytes;
}

/*
 * Prints the opcode, register IDs, and immediate of every instruction, 
 * assuming they have been decoded into the instructions array
*/
void print_instructions(instruction_t* instructions, unsigned int num_instructions)
{
  printf("instructions: \n");
  unsigned int i;
  for(i = 0; i < num_instructions; i++)
  {
    printf("op: %d, reg1: %d, reg2: %d, imm: %d\n", 
	   instructions[i].opcode,
	   instructions[i].first_register,
	   instructions[i].second_register,
	   instructions[i].immediate);
  }
  printf("--------------\n");
}


/*
 * Prints an error and then exits the program with status 1
*/
void error_exit(const char* message)
{
  printf("Error: %s\n", message);
  exit(1);
}
