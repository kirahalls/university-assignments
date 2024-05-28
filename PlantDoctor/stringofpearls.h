#ifndef STRINGOFPEARLS_H
#define STRINGOFPEARLS_H

#include <QObject>
#include "plantinterface.h"

class StringOfPearls : public PlantInterface
{

public:
    explicit StringOfPearls(QObject *parent = nullptr);

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

#endif // STRINGOFPEARLS_H
