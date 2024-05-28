#include "plantinterface.h"
#include <QDebug>

PlantInterface::PlantInterface(QObject *parent)
    : QObject(parent)
{
}

QString PlantInterface::getPlantName()
{
    return plantName;
}

int PlantInterface::getDaysToWaterOnSeason()
{
    return daysToWaterOnSeason;
}

int PlantInterface::getDaysToWaterOffSeason()
{
    return daysToWaterOffSeason;
}

int PlantInterface::getDaysToFertilizeOnSeason()
{
    return daysToFertilizeOnSeason;
}

int PlantInterface::getDaysToFertilizeOffSeason()
{
    return daysToFertilizeOffSeason;
}

int PlantInterface::getWaterTolerance()
{
    return waterTolerance;
}

int PlantInterface::getMistTolerance()
{
    return mistTolerance;
}

int PlantInterface::getFertilizerTolerance()
{
    return fertilizerTolerance;
}

int PlantInterface::getLightTolerance()
{
    return lightTolerance;
}

int PlantInterface::getLightToleranceOffSeason()
{
    return lightToleranceOffSeason;
}

int PlantInterface::getRequiredLightOnSeason()
{
    return requiredLight;
}

int PlantInterface::getRequiredLightOffSeason()
{
    return requiredLightOffSeason;
}

int PlantInterface::getDaysToMistOnSeason()
{
    return daysToMistOnSeason;
}

int PlantInterface::getDaysToMistOffSeason()
{
    return daysToMistOffSeason;
}

