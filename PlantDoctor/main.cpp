#include "homescreen.h"
#include "plantdiseasecard.h"
#include "model.h"

#include <QApplication>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    Model model;
    HomeScreen home(model);
    PlantDiseaseCard disease;
    home.show();
    return a.exec();
}
