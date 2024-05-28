#ifndef POINSETTIA_H
#define POINSETTIA_H

#include <QObject>
#include "plantinterface.h"

class Poinsettia : public PlantInterface
{

public:
   explicit Poinsettia(QObject *parent = nullptr);

    QString getHealthyImage() override;
    QString getBugsImage() override;
    QString getBugsWiltedImage() override;
    QString getDeadImage() override;
    QString getHealthyPot() override;
    QString getRootRotPotImage() override;
    QString getWiltedImage() override;
    QString getYellowImage() override;
    QString getWiltedYellowImage() override;
    QString getWinterImage();
    QString getWinterWiltedImage();
    QString getComboImage() override;

};

#endif // POINSETTIA_H
