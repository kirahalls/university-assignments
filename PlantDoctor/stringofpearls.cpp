#include "stringofpearls.h"

StringOfPearls::StringOfPearls(QObject *parent)
    : PlantInterface(parent)
{
    plantName = "String Of Pearls";

    daysToWaterOnSeason = 14;
    daysToWaterOffSeason = 28;
    requiredLight = 75;
    requiredLightOffSeason = 100;
    daysToMistOnSeason = 999;
    daysToMistOffSeason = 999;
    daysToFertilizeOnSeason = 28;
    daysToFertilizeOffSeason = 999;

    waterTolerance = 2;
    mistTolerance = 999;
    fertilizerTolerance = 6;
    lightTolerance = 25;
    lightToleranceOffSeason = 999;
}

QString StringOfPearls::getHealthyImage()
{
    return ":/StringOfPearls/assets/StringOfPearls/StringOfPearls.png";
}

QString StringOfPearls::getBugsImage()
{
    return ":/StringOfPearls/assets/StringOfPearls/StringOfPearlsBugs.png";
}

QString StringOfPearls::getBugsWiltedImage()
{
    return ":/StringOfPearls/assets/StringOfPearls/StringOfPearlsBugsWilted.png";
}

QString StringOfPearls::getDeadImage()
{
    return ":/StringOfPearls/assets/StringOfPearls/StringOfPearlsDead.png";
}

QString StringOfPearls::getHealthyPot()
{
    return ":/StringOfPearls/assets/StringOfPearls/StringOfPearlsPot.png";
}

QString StringOfPearls::getRootRotPotImage()
{
    return ":/StringOfPearls/assets/StringOfPearls/StringOfPearlsPotRootRot.png";
}

QString StringOfPearls::getWiltedImage()
{
    return ":/StringOfPearls/assets/StringOfPearls/StringOfPearlsWilted.png";
}

QString StringOfPearls::getYellowImage()
{
    return ":/StringOfPearls/assets/StringOfPearls/StringOfPearlsYellow.png";
}

QString StringOfPearls::getWiltedYellowImage()
{
    return ":/StringOfPearls/assets/StringOfPearls/StringOfPearlsWiltedYellow.png";
}

QString StringOfPearls::getComboImage()
{
    return ":/StringOfPearls/assets/StringOfPearls/StringOfPearlsPotCombo.png";
}
