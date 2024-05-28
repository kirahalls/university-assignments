#include "pothos.h"

Pothos::Pothos(QObject *parent)
    : PlantInterface(parent)
{
    plantName = "Pothos";

    daysToWaterOnSeason = 14;
    daysToWaterOffSeason = 14;
    requiredLight = 75;
    requiredLightOffSeason = 75;
    daysToMistOnSeason =999; // no misting in on season
    daysToMistOffSeason = 14;
    daysToFertilizeOnSeason = 28;
    daysToFertilizeOffSeason = 999;

    waterTolerance = 3;
    mistTolerance = 3;
    fertilizerTolerance = 6;
    lightTolerance = 50;
    lightToleranceOffSeason = 50;
}

QString Pothos::getHealthyImage()
{
    return ":/Pothos/assets/Pothos/Pothos.png";
}

QString Pothos::getBugsImage()
{
    return ":/Pothos/assets/Pothos/PothosBugs.png";
}

QString Pothos::getBugsWiltedImage()
{
    return ":/Pothos/assets/Pothos/PothosBugsWilted.png";
}

QString Pothos::getDeadImage()
{
    return ":/Pothos/assets/Pothos/PothosDead.png";
}

QString Pothos::getHealthyPot()
{
    return ":/Pothos/assets/Pothos/PothosPot.png";
}

QString Pothos::getRootRotPotImage()
{
    return ":/Pothos/assets/Pothos/PothosPotRootRot.png";
}

QString Pothos::getWiltedImage()
{
    return ":/Pothos/assets/Pothos/PothosWilted.png";
}

QString Pothos::getYellowImage()
{
    return ":/Pothos/assets/Pothos/PothosYellow.png";
}

QString Pothos::getWiltedYellowImage()
{
    return ":/Pothos/assets/Pothos/PothosWiltedYellow.png";
}

QString Pothos::getComboImage()
{
    return ":/Pothos/assets/Pothos/PothosPotCombo.png";
}
