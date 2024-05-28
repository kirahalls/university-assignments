#include "snakeplant.h"

SnakePlant::SnakePlant(QObject *parent)
    : PlantInterface(parent)
{
    plantName = "Snake Plant";

    daysToWaterOnSeason = 14;
    daysToWaterOffSeason = 28;
    requiredLight = 100;
    requiredLightOffSeason = 100;
    daysToMistOnSeason = 999;
    daysToMistOffSeason = 999;
    daysToFertilizeOnSeason = 28;
    daysToFertilizeOffSeason = 999;

    waterTolerance = 7;
    mistTolerance = 999;
    fertilizerTolerance = 6;
    lightTolerance = 75;
    lightToleranceOffSeason = 75;
}


QString SnakePlant::getHealthyImage()
{
    return ":/SnakePlant/assets/SnakePlant/SnakePlant.png";
}

QString SnakePlant::getBugsImage()
{
    return ":/SnakePlant/assets/SnakePlant/SnakePlantBugs.png";
}

QString SnakePlant::getBugsWiltedImage()
{
    return ":/SnakePlant/assets/SnakePlant/SnakePlantBugsWilted.png";
}

QString SnakePlant::getDeadImage()
{
    return ":/SnakePlant/assets/SnakePlant/SnakePlantDead.png";
}

QString SnakePlant::getHealthyPot()
{
    return ":/SnakePlant/assets/SnakePlant/SnakePlantPot.png";
}

QString SnakePlant::getRootRotPotImage()
{
    return ":/SnakePlant/assets/SnakePlant/SnakePlantPotRootRot.png";
}

QString SnakePlant::getWiltedImage()
{
    return ":/SnakePlant/assets/SnakePlant/SnakePlantWilted.png";
}

QString SnakePlant::getYellowImage()
{
    return ":/SnakePlant/assets/SnakePlant/SnakePlantYellow.png";
}

QString SnakePlant::getWiltedYellowImage()
{
    return ":/SnakePlant/assets/SnakePlant/SnakePlantWiltedYellow.png";
}

QString SnakePlant::getComboImage()
{
    return ":/SnakePlant/assets/SnakePlant/SnakePlantPotCombo.png";
}
