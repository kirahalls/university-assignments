#ifndef CAR_H
#define CAR_H
#include <iostream>

class Car {
    double speed;

public: 
    Car(double speed);
    void drive();
    void slow();
    void speedUp();
};

#endif