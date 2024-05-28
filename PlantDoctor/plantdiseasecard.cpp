#include "plantdiseasecard.h"
#include "ui_plantdiseasecard.h"

PlantDiseaseCard::PlantDiseaseCard(QWidget *parent) :
    QWidget(parent),
    ui(new Ui::PlantDiseaseCard)
{
    ui->setupUi(this);
}

PlantDiseaseCard::~PlantDiseaseCard()
{
    delete ui;
}
