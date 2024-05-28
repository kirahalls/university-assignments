#ifndef PLANTINTERFACE_H
#define PLANTINTERFACE_H

#include <QObject>
#include <QString>
#include <QVector>

class PlantInterface : public QObject
{
    Q_OBJECT
public:
    explicit PlantInterface(QObject *parent = nullptr);

    QString plantName;
    int daysToWaterOnSeason;
    int daysToWaterOffSeason;
    int daysToMistOnSeason;
    int daysToMistOffSeason;
    int daysToFertilizeOnSeason;
    int daysToFertilizeOffSeason;
    int requiredLight;
    int requiredLightOffSeason;
    int waterTolerance;
    int mistTolerance;
    int fertilizerTolerance;
    int lightTolerance;
    int lightToleranceOffSeason;

    QString getPlantName();
    int getDaysToWaterOnSeason();
    int getDaysToWaterOffSeason();
    int getDaysToFertilizeOnSeason();
    int getDaysToFertilizeOffSeason();
    int getDaysToMistOnSeason();
    int getDaysToMistOffSeason();
    int getRequiredLightOnSeason();
    int getRequiredLightOffSeason();
    int getWaterTolerance();
    int getMistTolerance();
    int getFertilizerTolerance();
    int getLightTolerance();
    int getLightToleranceOffSeason();

    virtual QString getHealthyImage() = 0;
    virtual QString getBugsImage() = 0;
    virtual QString getBugsWiltedImage() = 0;
    virtual QString getDeadImage() = 0;
    virtual QString getHealthyPot() = 0;
    virtual QString getRootRotPotImage() = 0;
    virtual QString getWiltedImage() = 0;
    virtual QString getYellowImage() = 0;
    virtual QString getWiltedYellowImage() = 0;
    virtual QString getComboImage() = 0;
};

#endif // PLANTINTERFACE_H
