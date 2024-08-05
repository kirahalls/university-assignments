#include <iostream>

class Cat 
{
    int lives;

    public: 
   

    Cat(int initialLives) {
        lives = initialLives;
    }

    void reportLives() 
    {
        std::cout << "Number of lives: " + this->lives << std::endl;
    }

    void loseLife() 
    {
        this->lives--;
    }
};

int main() 
{
    Cat cat(9);
}