#include "model.h"
#include <QDebug>
#include <string>

Model::Model(QObject *parent)
    : QObject{parent}
{
    //Set dayTimer
    dayTimer = new QTimer(this);
    connect(dayTimer, SIGNAL(timeout()), this, SLOT(progressDay()));

    //Set frameTimer
    frameTimer = new QTimer(this);
    connect(frameTimer, SIGNAL(timeout()), this, SLOT(doFrame()));

    //Set intervals
    dayTimerInterval = dayLengthSeconds*1000;
    frameTimerInterval = 1000/fps;

}

Model::~Model()
{
    delete dayTimer;
    delete frameTimer;
}

void Model::startGame()
{
    //Reset game
    resetGame();

    //Start game
    gameStarted = true;
    gamePaused = false;

    //Start timers
    dayTimer->start(dayTimerInterval);
    frameTimer->start(frameTimerInterval);

    //Print message
    qDebug() << "Game started";
}

void Model::resetGame()
{
    //Set intiial day and season
    day = 1;
    season = 0;

    //Set plant state
    setYellow(false);
    setWilted(false);
    setBugs(false);
    setRootRot(false);
    setDead(false);

    //Set plant health
    lastWateredDay = 1;
    lastMistedDay = 1;
    lastFertilizedDay = 1;
    daysYellow = 0;
    daysWilted = 0;
    daysBugs = 0;
    daysRootRot = 0;

    //Set first tool use variables
    firstWater = false;
    firstMister = false;
    firstFertilizer = false;

    //Set growth vars
    totalGrowth = 0;

    //Reset counts
    waterCount = 0;
    misterCount = 0;
    fertilizerCount = 0;
    pesticideCount = 0;
    repotCount = 0;

    //Reset problems
    underWatered = false;
    overWatered = false;
    underMisted = false;
    overMisted = false;
    underFertilized = false;
    overFertilized = false;
    underLight = false;
    overLight = false;
    spiderMites = false;
    yellowProblem = false;
    wiltedProblem = false;
    bugsProblem = false;
    rootRotProblem = false;

    //Set light level average
    lightLevelDayAverage = 0;

    //Transmit day and season
    emit displayDay(day);
    emit displaySeason(season);
}

void Model::killGame()
{
    //Stop timers
    dayTimer->stop();
    frameTimer->stop();

    //Set game state
    gameStarted = false;
}

void Model::pause()
{
    //Get day remaining time
    dayPausedRemainingTime = dayTimer->remainingTime();

    //Set paused
    gamePaused = true;

    //Pause timers
    dayTimer->stop();
    frameTimer->stop();
}

void Model::resume()
{
    //Set paused
    gamePaused = false;

    //Resume timers
    dayTimer->start(dayPausedRemainingTime);
    frameTimer->start(frameTimerInterval);

    //Set day remaining time
    dayPausedRemainingTime = 0;
}

void Model::setYellow(bool isYellow)
{
    //Set yellow
    this->isYellow = isYellow;

    //Emit yellow signal
    emit plantToggleYellow(isYellow);

    //Set days to zero if false
    if (!isYellow)
    {
        daysYellow = 0;
    }
}

void Model::setWilted(bool isWilted)
{
    //Set wilted
    this->isWilted = isWilted;

    //Emit wilted signal
    emit plantToggleWilted(isWilted);

    //Set days to zero if false
    if (!isWilted)
    {
        daysWilted = 0;
    }
}

void Model::setBugs(bool hasBugs)
{
    //Set bugs
    this->hasBugs = hasBugs;

    //Emit bugs signal
    emit plantToggleBugs(hasBugs);

    //Set days to zero if false
    if (!hasBugs)
    {
        daysBugs = 0;
    }
}

void Model::setRootRot(bool hasRootRot)
{
    //Set root rot
    this->hasRootRot = hasRootRot;

    //Emit root rot signal
    emit plantToggleRootRot(hasRootRot);

    //Set days to zero if false
    if (!hasRootRot)
    {
        daysRootRot = 0;
    }
}

void Model::setDead(bool isDead)
{
    //Set dead
    this->isDead = isDead;

    //Emit dead signal
    emit plantToggleDead(isDead);

    if(isDead)
    {
        //End game
        endGame();
    }
}

void Model::progressDay()
{
    //Reset timer to correct time if paused
    dayTimer->stop();
    dayTimer->start(dayTimerInterval);

    //Set today's growth
    todaysGrowth = 0.0;

    //Add day to status counts
    if (isYellow)
    {
        daysYellow++;
    }
    if (isWilted)
    {
        daysWilted++;
    }
    if (hasBugs)
    {
        daysBugs++;
    }
    if (hasRootRot)
    {
        daysRootRot++;
    }

    //Check water
    dayCheckWater();

    //Check mister
    dayCheckMister();

    //Check fertilizer
    dayCheckFertilizer();

    //Check light level
    dayCheckLight();

    //Check fixes
    dayCheckFixes();

    //Reduce growth from status effects
    todaysGrowth -= (isYellow + isWilted + hasBugs + hasRootRot) * growthVolatility;

    //Add to total growth
    totalGrowth += todaysGrowth;

    //If negative, add to negative growth chain
    if (todaysGrowth < 0)
    {
        negativeGrowthChain += todaysGrowth;
    }
    else
    {
        negativeGrowthChain = 0;
    }

    //Print message with stats
    qDebug() << "Day" << day << "Season" << season << "Light" << lightLevelDayAverage << "Water" << waterCount << "Mister" << misterCount << "Fertilizer" << fertilizerCount << "Pesticide" << pesticideCount << "Repot" << repotCount << "Yellow" << daysYellow << "Wilted" << daysWilted << "Bugs" << daysBugs << "Root Rot" << daysRootRot << "Growth" << todaysGrowth << "Total Growth" << totalGrowth;

    //Set last day used variables
    if (waterCount > 0)
    {
        lastWateredDay = day;
        firstWater = true;
    }
    if (misterCount > 0)
    {
        lastMistedDay = day;
        firstMister = true;
    }
    if (fertilizerCount > 0)
    {
        lastFertilizedDay = day;
        firstFertilizer = true;
    }

    //Reset counts
    waterCount = 0;
    misterCount = 0;
    fertilizerCount = 0;
    pesticideCount = 0;
    repotCount = 0;

    //Increment day
    day++;

    //Check if season should change
    if (day % (seasonLengthDays + 1) == 0)
    {
        progressSeason();
    }

    //Emit day signal
    emit displayDay(day % (seasonLengthDays + 1));

    //Set plant state from underlying issues
    daySetSymptoms();

    //Set events
    daySetEvents();

    //Check day limits
    dayCheckDeathLimits();
}

void Model::dayCheckWater()
{
    //Check if watering is correct from tolerance
    if ((day - lastWateredDay < waterFrequency - waterTolerance && waterCount > 0) || (waterCount >= 2 && !underWatered))
    {
        if ((firstWater && !underWatered) || waterCount > 1)
        {
            overWatered = true;
            underWatered = false;
        }
    }
    else if (day - lastWateredDay > waterFrequency + waterTolerance)
    {
        if (overWatered)
        {
            //Check for underwatering fix
            if (waterCycleSkippedStreak % 2 == 0)
            {
                waterCycleSkippedStreak++;
            }
            else
            {
                waterCycleSkippedStreak = 0;
            }
        }
        else
        {
            underWatered = true;
        }

        //Set firstWater
        firstWater = true;

        //Set lastWateredDay to keep track of cycle despite not having been watered
        lastWateredDay = div(day, waterFrequency).quot * waterFrequency;
    }
    else if (waterCount > 0)
    {
        //In range, check for overwatering fix
        if (waterCount >= 2 && underWatered)
        {
            twoTimesWateredStreak++;
        } else {
            twoTimesWateredStreak = 0;
        }

        //Check skipped streak
        if (overWatered)
        {
            if (waterCycleSkippedStreak % 2 == 1)
            {
                waterCycleSkippedStreak++;
            }
            else
            {
                waterCycleSkippedStreak = 0;
            }
        }

        //Add to growth
        todaysGrowth += growthVolatility;
    }
}

void Model::dayCheckMister()
{
    //Check if misting is correct from tolerance
    if (((day - lastMistedDay < misterFrequency - misterTolerance) && misterCount > 0) || misterCount >= 2)
    {
        if (!underMisted && (firstMister || misterCount > 1))
        {
            overMisted = true;
        }
        else
        {
            underMisted = false;
        }
    }
    else if (day - lastMistedDay > misterFrequency + misterTolerance)
    {
        if (!overMisted)
        {
            underMisted = true;
        }
        else
        {
            overMisted = false;
        }

        //Set firstMister
        firstMister = true;

        //Set lastMistedDay to keep track of cycle despite not having been misted
        lastMistedDay = div(day, misterFrequency).quot * misterFrequency;
    }
    else if (misterCount > 0)
    {
        //Add to growth
        todaysGrowth += growthVolatility;

        //Fix issues
        if (overMisted)
        {
            overMisted = false;
        }
        if (underMisted)
        {
            underMisted = false;
        }
    }
}

void Model::dayCheckFertilizer()
{
    //Check if fertilizing is correct from tolerance
    if (((day - lastFertilizedDay < fertilizerFrequency - fertilizerTolerance) && fertilizerCount > 0) || fertilizerCount >= 2)
    {
        if (!underFertilized && (firstFertilizer || fertilizerCount > 1))
        {
            overFertilized = true;
            rootRotProblem = true;
        }
        else
        {
            underFertilized = false;
        }
    }
    else if (day - lastFertilizedDay > fertilizerFrequency + fertilizerTolerance)
    {
        if (!overFertilized)
        {
            underFertilized = true;
        }
        else
        {
            overFertilized = false;
        }

        //Set firstFertilizer
        firstFertilizer = true;

        //Set lastFertilizedDay to keep track of cycle despite not having been fertilized
        lastFertilizedDay = div(day, fertilizerFrequency).quot * fertilizerFrequency;
    }
    else if (fertilizerCount > 0)
    {
        //Add to growth
        todaysGrowth += growthVolatility;

        //Fix issues
        if (overFertilized)
        {
            overFertilized = false;
            recoveryDays = 3;
        }
    }
}

void Model::dayCheckLight()
{
    //Get light level average
    lightLevelDayAverage /= dayLengthSeconds*fps;

    //Set over and under light default values
    overLight = false;
    underLight = false;

    //Change health based on tolerance
    if (lightLevelDayAverage < requiredLightLevel - lightTolerance)
    {
        underLight = true;

        //Reduce growth
        todaysGrowth -= growthVolatility;
    }
    else if (lightLevelDayAverage > requiredLightLevel + lightTolerance)
    {
        if (recoveryDays > 0)
        {
            if (lightLevelDayAverage < requiredLightLevel + lightTolerance + lightRecoveryTolerance)
            {
                lightRecoveryStreak++;
            }
            else
            {
                lightRecoveryStreak = 0;
                overLight = true;

                //Reduce growth
                todaysGrowth -= growthVolatility;
            }
        }
        else
        {
            overLight = true;

            //Reduce growth
            todaysGrowth -= growthVolatility;
        }
    }
    else
    {
        //Add to growth
        todaysGrowth += growthVolatility;

        //Fix yellow leaves if there are no other symptoms
        if (!isWilted && !hasBugs && !hasRootRot)
        {
            yellowProblem = false;
        }
    }

    //Change plant health based on light level
    lightLevelDayAverage = 0;
}

void Model::dayCheckFixes()
{
    //Check for light recovery
    if (recoveryDays > 0)
    {
        if (lightRecoveryStreak >= 3)
        {
            lightRecoveryStreak = 0;
            recoveryDays = 0;
        }
        else
        {
            //Reduce growth and recovery days
            todaysGrowth -= growthVolatility;
            recoveryDays--;
        }
    }

    //Check for underwatering fix
    if (underWatered && twoTimesWateredStreak >= 1)
    {
        twoTimesWateredStreak = 0;
        underWatered = false;
        recoveryDays = 3;
    }

    //Check for overwatering fix
    if (overWatered && waterCycleSkippedStreak >= 1)
    {
        waterCycleSkippedStreak = 0;
        overWatered = false;
        recoveryDays = 3;
    }

    //Check for spider mites fix
    if (spiderMites && !hasBugs && !hasRootRot)
    {
        spiderMites = false;
        recoveryDays = 3;
    }
}

void Model::dayCheckDeathLimits()
{
    //Check negative growth chain
    if (abs(negativeGrowthChain) > maxNegativeGrowthChain)
    {
        qDebug() << "Plant DIED! fertilizer issue";

        setDead(true);
    }

    //Check wilted days
    if (daysWilted > maxDaysWilted)
    {
        qDebug() << "Plant DIED! wilted too long";

        setDead(true);
    }

    //Check yellow days
    if (daysYellow > maxDaysYellow)
    {
        setDead(true);
    }

    //Check bug days
    if (daysBugs > maxDaysBugs)
    {
        qDebug() << "Plant DIED! had bugs too long";

        setDead(true);
    }

    //Check root rot days
    if (daysRootRot > maxDaysRootRot)
    {
        qDebug() << "Plant DIED! had root rot too long";

        setDead(true);
    }
}

void Model::progressSeason()
{
    //Increment season
    season++;

    //Reset day
    day = 1;

    //Reset days since variables
    lastWateredDay -= seasonLengthDays;
    lastMistedDay -= seasonLengthDays;
    lastFertilizedDay -= seasonLengthDays;

    // check if it is fall season - update fertilization
    if (season == 2)
    {
        qDebug() << "updating fertilizer to off season value";
        qDebug() << "new fertilizer value: " << fertilizerFrequencyOffSeason;
        fertilizerFrequency = fertilizerFrequencyOffSeason;
    }

    // check if it is winter season - update the other frequencies
    if (season == 3)
    {
        qDebug() << "updating remaining frequencies to off season values";
        waterFrequency = waterFrequencyOffSeason;
        misterFrequency = misterFrequencyOffSeason;
        requiredLightLevel = requiredLightLevelOffSeason;
        lightTolerance = lightToleranceOffSeason;
    }

    //Check if game should end
    if (season > 3)
    {
        season = 0;
        endGame();
    }

    //Emit season signal
    emit displaySeason(season);
}

void Model::daySetEvents()
{
    //Check if dead
    if (isDead)
    {
        return;
    }

    //Check programmed events for day
    for (int i = 0; i < 8; i++)
    {
        if (problemDays[i] == day && problemSeasons[i] == season)
        {
            //Set the problem based on the problem name
            if (problemNames[i] == "spiderMites")
            {
                spiderMites = true;
            }
            else if (problemNames[i] == "yellowProblem")
            {
                yellowProblem = true;
            }
            else if (problemNames[i] == "wiltedProblem")
            {
                wiltedProblem = true;
            }
            else if (problemNames[i] == "bugsProblem")
            {
                bugsProblem = true;
            }
            else if (problemNames[i] == "rootRotProblem")
            {
                rootRotProblem = true;
            }
        }
    }
}

void Model::daySetSymptoms()
{
    //Set new symptoms
    bool newWilted = false;
    bool newYellow = false;
    bool newBugs = hasBugs;
    bool newRootRot = hasRootRot;

    std::string problems = "Current problems:\n";

    //Set symptoms caused by underlying issues
    if (underWatered)
    {
        newWilted = true;
        newYellow = true;

        problems += "Under Watered\n";
    }
    if (overWatered)
    {
        newYellow = true;
        newWilted = true;
        newBugs = true;

        problems += "Over Watered\n";
    }
    if (underMisted)
    {
        newWilted = true;

        problems += "Under Misted\n";
    }
    if (overMisted)
    {
        newWilted = true;

        problems += "Over Misted\n";
    }
    if (underFertilized)
    {
        newYellow = true;

        problems += "Under Fertilized\n";
    }
    if (overFertilized)
    {
        problems += "Over Fertilized\n";
    }
    if (underLight)
    {
        newYellow = true;

        problems += "Under Light\n";
    }
    if (overLight)
    {
        newYellow = true;

        problems += "Over Light\n";
    }
    if (spiderMites)
    {
        newBugs = true;
        newRootRot = true;
        spiderMites = false;

        problems += "Spider Mites\n";
    }
    if (yellowProblem)
    {
        newYellow = true;

        problems += "Yellow Problem\n";
    }
    if (wiltedProblem)
    {
        newWilted = true;

        problems += "Wilted Problem\n";
    }
    if (bugsProblem)
    {
        newBugs = true;

        problems += "Bugs Problem\n";
    }
    if (rootRotProblem)
    {
        newRootRot = true;
        rootRotProblem = false;

        problems += "Root Rot Problem\n";
    }

    //Set symptoms
    setWilted(newWilted);
    setYellow(newYellow);
    setBugs(newBugs);
    setRootRot(newRootRot);

    if(!isYellow && !isWilted)
        emit plantToggleHealthy();

    //Print problems
    qDebug() << problems.c_str();
    emit showCurrentProblems(problems.c_str());
}

void Model::endGame()
{
    //Stop timers
    dayTimer->stop();
    frameTimer->stop();

    //Set game state
    gameStarted = false;

    emit gameOver();
}

void Model::doFrame()
{
    //Collect light level data
    lightLevelDayAverage += lightLevel;

    //Emit day progress signal
    emit displayDayProgress(1 - dayTimer->remainingTime() / (dayTimerInterval));
}

void Model::useWater()
{
    //Fix wilted problem
    wiltedProblem = false;

    //Add to water count
    waterCount++;
    qDebug() << "waterCount: " << waterCount;

    //Set wilted to false
    setWilted(false);
}

void Model::useMister()
{
    //Add to mister count
    misterCount++;
    qDebug() << "misterCount: " << misterCount;

}

void Model::useFertilizer()
{
    //Add to fertilizer count
    fertilizerCount++;
    qDebug() << "fertilizerCount: " << fertilizerCount;

}

void Model::usePesticide()
{
    //Set bugs to false
    setBugs(false);

    //Fix bugs problem
    bugsProblem = false;

    //Add to pesticide count
    pesticideCount++;
    qDebug() << "pesticideCount: " << pesticideCount;

}

void Model::useRepot()
{

    //Set root rot to false
    setRootRot(false);

    //Fix root rot problem
    rootRotProblem = false;

    //Add to repot count
    repotCount++;
    qDebug() << "repotCount: " << repotCount;

}

void Model::setLightLevel(int lightLevel)
{
    //Set light level
    this->lightLevel = lightLevel;
}

void Model::setWaterFrequencyOnSeason(int waterFrequency)
{
    //Set water frequency
    this->waterFrequency = waterFrequency;
}

void Model::setMisterFrequencyOnSeason(int misterFrequency)
{
    //Set mister frequency
    this->misterFrequency = misterFrequency;
}

void Model::setFertilizerFrequencyOnSeason(int fertilizerFrequency)
{
    //Set fertilizer frequency
    this->fertilizerFrequency = fertilizerFrequency;
}

void Model::setRequiredLightLevelOnSeason(int requiredLightLevel)
{
    //Set required light level
    this->requiredLightLevel = requiredLightLevel;
}

void Model::setWaterFrequencyOffSeason(int waterFrequency)
{
    //Set water frequency
    this->waterFrequencyOffSeason = waterFrequency;
}

void Model::setMisterFrequencyOffSeason(int misterFrequency)
{
    //Set mister frequency
    this->misterFrequencyOffSeason = misterFrequency;
}

void Model::setFertilizerFrequencyOffSeason(int fertilizerFrequency)
{
    //Set fertilizer frequency
    this->fertilizerFrequencyOffSeason = fertilizerFrequency;
}

void Model::setRequiredLightLevelOffSeason(int requiredLightLevel)
{
    //Set required light level
    this->requiredLightLevelOffSeason = requiredLightLevel;
}

void Model::setTimedProblems(int problemDays[], int problemSeasons[], std::string problemNames[])
{
    //Set yellow days
    for (int i = 0; i < 8; i++)
    {
        this->problemDays[i] = problemDays[i];
        this->problemSeasons[i] = problemSeasons[i];
        this->problemNames[i] = problemNames[i];
    }
}

void Model::setWaterTolerance(int waterTolerance)
{
    //Set water tolerance
    this->waterTolerance = waterTolerance;
}

void Model::setMisterTolerance(int misterTolerance)
{
    //Set mister tolerance
    this->misterTolerance = misterTolerance;
}

void Model::setFertilizerTolerance(int fertilizerTolerance)
{
    //Set fertilizer tolerance
    this->fertilizerTolerance = fertilizerTolerance;
}

void Model::setLightTolerance(int lightTolerance)
{
    //Set light tolerance
    this->lightTolerance = lightTolerance;
}

void Model::setLightToleranceOffSeason(int lightTolerance)
{
    //Set light tolerance
    this->lightToleranceOffSeason = lightTolerance;
}

void Model::setGrowthVolatility(int growthVolatility)
{
    //Set growth positive volatility
    this->growthVolatility = growthVolatility;
}

void Model::setMaxNegativeGrowthChain(int maxNegativeGrowthChain)
{
    //Set negative growth chain
    this->maxNegativeGrowthChain = maxNegativeGrowthChain;
}
