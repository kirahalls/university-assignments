#include "poinsettia.h"

Poinsettia::Poinsettia(QObject *parent)
    : PlantInterface(parent)
{
    plantName = "Poinsettia";

    daysToWaterOnSeason = 7;
    daysToWaterOffSeason = 7;
    requiredLight = 87;
    requiredLightOffSeason = 87;
    daysToMistOnSeason = 7;
    daysToMistOffSeason = 2;
    daysToFertilizeOnSeason = 21;

    waterTolerance = 1;
    mistTolerance = 1;
    fertilizerTolerance = 3;
    lightTolerance = 13;
    lightToleranceOffSeason = 13;
}

QString Poinsettia::getHealthyImage()
{
    return ":/Poinsettia/assets/Poinsettia/Poinsettia.png";
}

QString Poinsettia::getBugsImage()
{
    return ":/Poinsettia/assets/Poinsettia/PoinsettiaBugs.png";
}

QString Poinsettia::getBugsWiltedImage()
{
    return ":/Poinsettia/assets/Poinsettia/PoinsettiaBugsWilted.png";
}

QString Poinsettia::getDeadImage()
{
    return ":/Poinsettia/assets/Poinsettia/PoinsettiaDead.png";
}

QString Poinsettia::getHealthyPot()
{
    return ":/Poinsettia/assets/Poinsettia/PoinsettiaPot.png";
}

QString Poinsettia::getRootRotPotImage()
{
    return ":/Poinsettia/assets/Poinsettia/PoinsettiaPotRootRot.png";
}

QString Poinsettia::getWiltedImage()
{
    return ":/Poinsettia/assets/Poinsettia/PoinsettiaWilted.png";
}

QString Poinsettia::getYellowImage()
{
    return ":/Poinsettia/assets/Poinsettia/PoinsettiaYellow.png";
}

QString Poinsettia::getWiltedYellowImage()
{
    return ":/Poinsettia/assets/Poinsettia/PoinsettiaWiltedYellow.png";
}

QString Poinsettia::getWinterImage()
{
    return ":/Poinsettia/assets/Poinsettia/PoinsettiaWinter.png";
}

QString Poinsettia::getWinterWiltedImage()
{
    return ":/Poinsettia/assets/Poinsettia/PoinsettiaWinterWilted.png";
}

QString Poinsettia::getComboImage()
{
    return ":/Poinsettia/assets/Poinsettia/PoinsettiaPotCombo.png";
}
