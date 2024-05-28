#ifndef SNAKEPLANT_H
#define SNAKEPLANT_H

#include <QObject>
#include "plantinterface.h"

class SnakePlant : public PlantInterface
{

public:
   explicit SnakePlant(QObject *parent = nullptr);

    QString getHealthyImage() override;
    QString getBugsImage() override;
    QString getBugsWiltedImage() override;
    QString getDeadImage() override;
    QString getHealthyPot() override;
    QString getRootRotPotImage() override;
    QString getWiltedImage() override;
    QString getYellowImage() override;
    QString getWiltedYellowImage() override;
    QString getComboImage() override;
};


#endif // SNAKEPLANT_H
