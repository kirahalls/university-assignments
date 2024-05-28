#ifndef POTHOS_H
#define POTHOS_H

#include <QObject>
#include "plantinterface.h"

class Pothos : public PlantInterface
{

public:
    explicit Pothos(QObject *parent = nullptr);

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

#endif // POTHOS_H
