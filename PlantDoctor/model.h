#ifndef MODEL_H
#define MODEL_H

#include <QObject>
#include <QTimer>
#include <string>

/**
 * @brief Handles game logic, plant states, plant stats, and timing.
 */
class Model : public QObject
{
    Q_OBJECT

public:
    /**
     * @brief Constructs a Model object.
     * @param parent The parent object.
     */
    explicit Model(QObject *parent = 0);

    /**
     * @brief Destroys the Model object.
     */
    ~Model();

public slots:
    //Game logic methods

    /**
     * @brief Starts the game.
     */
    void startGame();

    /**
     * @brief Ends the entire game.
     */
    void killGame();

    /**
     * @brief Pauses the game.
     */
    void pause();

    /**
     * @brief Resumes the game.
     */
    void resume();

    /**
     * @brief Performs actions for each frame.
     */
    void doFrame();

    /**
     * @brief Progresses the day and assesses plant.
     */
    void progressDay();

    /**
     * @brief Uses water on the plant.
     */
    void useWater();

    /**
     * @brief Uses a mister on the plant.
     */
    void useMister();

    /**
     * @brief Uses fertilizer on the plant.
     */
    void useFertilizer();

    /**
     * @brief Uses pesticide on the plant.
     */
    void usePesticide();

    /**
     * @brief Repots the plant.
     */
    void useRepot();

    /**
     * @brief Sets the light level.
     * @param lightLevel The light level to set.
     */
    void setLightLevel(int lightLevel);

    /**
     * @brief Sets how often the plant should be watered in days for on season.
     * @param waterFrequency The water frequency to set.
     */
    void setWaterFrequencyOnSeason(int waterFrequency);

    /**
     * @brief Sets how often the plant should be misted in days for on season.
     * @param misterFrequency The mister frequency to set.
     */
    void setMisterFrequencyOnSeason(int misterFrequency);

    /**
     * @brief Sets how often the plant should be fertilized in days for on season.
     * @param fertilizerFrequency The fertilizer frequency to set.
     */
    void setFertilizerFrequencyOnSeason(int fertilizerFrequency);

    /**
     * @brief Sets the required light level for on season.
     * @param requiredLightLevel The required light level to set.
     */
    void setRequiredLightLevelOnSeason(int requiredLightLevel);

    /**
     * @brief Sets how often the plant should be watered in days for off season.
     * @param waterFrequency The water frequency to set.
     */
    void setWaterFrequencyOffSeason(int waterFrequency);

    /**
     * @brief Sets how often the plant should be misted in days for off season.
     * @param misterFrequency The mister frequency to set.
     */
    void setMisterFrequencyOffSeason(int misterFrequency);

    /**
     * @brief Sets how often the plant should be fertilized in days for off season.
     * @param fertilizerFrequency The fertilizer frequency to set.
     */
    void setFertilizerFrequencyOffSeason(int fertilizerFrequency);

    /**
     * @brief Sets the required light level for off season.
     * @param requiredLightLevel The required light level to set.
     */
    void setRequiredLightLevelOffSeason(int requiredLightLevel);

    /**
     * @brief Sets the amount of days the player can stray from the water frequency.
     * @param waterTolerance The water tolerance to set.
     */
    void setWaterTolerance(int waterTolerance);

    /**
     * @brief Sets the amount of days the player can stray from the mister frequency.
     * @param misterTolerance The mister tolerance to set.
     */
    void setMisterTolerance(int misterTolerance);

    /**
     * @brief Sets the amount of days the player can stray from the fertilizer frequency.
     * @param fertilizerTolerance The fertilizer tolerance to set.
     */
    void setFertilizerTolerance(int fertilizerTolerance);

    /**
     * @brief Sets the amount the player can stray from the required light level.
     * @param lightTolerance The light tolerance to set.
     */
    void setLightTolerance(int lightTolerance);

    /**
     * @brief Sets the amount the player can stray from the required light level for off season.
     * @param lightTolerance The light tolerance to set.
     */
    void setLightToleranceOffSeason(int lightTolerance);

    /**
     * @brief Sets the days, seasons, and names of timed problems. Will be shortened to 8 items.
     * @param problemDays The problem days to set.
     * @param problemSeasons The problem seasons to set.
     * @param problemNames The problem names to set.
     */
    void setTimedProblems(int problemDays[], int problemSeasons[], std::string problemNames[]);

    /**
     * @brief Sets the growth volatility. This is how much the growth will increase or decrease.
     * @param growtheVolatility The growth volatility to set.
     */
    void setGrowthVolatility(int growtheVolatility);

    /**
     * @brief Sets the max negative growth chain. The plant will die if it shrinks by this much in a row.
     * @param maxNegativeGrowthChain The max negative growth chain to set.
     */
    void setMaxNegativeGrowthChain(int maxNegativeGrowthChain);

signals:
    //Plant states

    /**
     * @brief Toggles the yellow state of the plant.
     * @param isYellow Whether the plant is yellow or not.
     */
    void plantToggleYellow(bool isYellow);

    /**
     * @brief Toggles the wilted state of the plant.
     * @param isWilted Whether the plant is wilted or not.
     */
    void plantToggleWilted(bool isWilted);

    /**
     * @brief Toggles the bug state of the plant.
     * @param hasBugs Whether the plant has bugs or not.
     */
    void plantToggleBugs(bool hasBugs);

    /**
     * @brief Toggles the root rot state of the plant.
     * @param hasRootRot Whether the plant has root rot or not.
     */
    void plantToggleRootRot(bool hasRootRot);

    /**
     * @brief Toggles the dead state of the plant.
     * @param isDead Whether the plant is dead or not.
     */
    void plantToggleDead(bool isDead);

    /**
     * @brief Toggles the healthy state of the plant
     */
    void plantToggleHealthy();

    /**
     * @brief showCurrentProblems Tell the game screen the problems with the plant
     * @param currentProblems
     */
    void showCurrentProblems(std::string currentProblems);

    //Day info

    /**
     * @brief Emits the current day.
     * @param day The current day.
     */
    void displayDay(int day);

    /**
     * @brief Emits the current season.
     * @param season The current season.
     */
    void displaySeason(int season);

    /**
     * @brief Emits the progress of the current day.
     * @param dayProgress The progress of the current day.
     */
    void displayDayProgress(double dayProgress);

    /**
     * @brief gameOver Emits that the game has concluded to the game screen to show the game over screen.
     */
    void gameOver();

private:
    //Game logic methods

    /**
     * @brief Progresses the season.
     */
    void progressSeason();

    /**
     * @brief Ends the game. To be used when completed or dead.
     */
    void endGame();

    /**
     * @brief Resets the game.
     */
    void resetGame();

    /**
     * @brief Sets the yellow state of the plant.
     * @param isYellow Whether the plant is yellow or not.
     */
    void setYellow(bool isYellow);

    /**
     * @brief Sets the wilted state of the plant.
     * @param isWilted Whether the plant is wilted or not.
     */
    void setWilted(bool isWilted);

    /**
     * @brief Sets the bug state of the plant.
     * @param hasBugs Whether the plant has bugs or not.
     */
    void setBugs(bool hasBugs);

    /**
     * @brief Sets the root rot state of the plant.
     * @param hasRootRot Whether the plant has root rot or not.
     */
    void setRootRot(bool hasRootRot);

    /**
     * @brief Sets the dead state of the plant.
     * @param isDead Whether the plant is dead or not.
     */
    void setDead(bool isDead);

    // Day processes

    /**
     * @brief Checks the water for the day.
     */
    void dayCheckWater();

    /**
     * @brief Checks the mister for the day.
     */
    void dayCheckMister();

    /**
     * @brief Checks the fertilizer for the day.
     */
    void dayCheckFertilizer();

    /**
     * @brief Checks the light for the day.
     */
    void dayCheckLight();

    /**
     * @brief Checks for problem fixes.
    */
    void dayCheckFixes();

    /**
     * @brief Checks the death limits for the day.
     */
    void dayCheckDeathLimits();

    /**
     * @brief Triggers the events for the day.
     */
    void daySetEvents();

    /**
     * @brief Sets the plant symptoms for the day.
     */
    void daySetSymptoms();

    // Game state
    bool gameStarted = false;

    // Plant states
    bool isYellow = false;
    bool isWilted = false;
    bool hasBugs = false;
    bool hasRootRot = false;
    bool isDead = false;

    // Tool usage amounts for the day
    int waterCount = 0;
    int misterCount = 0;
    int fertilizerCount = 0;
    int pesticideCount = 0;
    int repotCount = 0;

    // Plant stats
    int waterFrequency = 0;
    int misterFrequency = 0;
    int fertilizerFrequency = 0;
    int requiredLightLevel = 0;

    int waterFrequencyOffSeason = 0;
    int misterFrequencyOffSeason = 0;
    int fertilizerFrequencyOffSeason = 0;
    int requiredLightLevelOffSeason = 0;

    // Timed problems
    int problemDays[8]{-1,-1,-1,-1,-1,-1,-1,-1};
    int problemSeasons[8]{-1,-1,-1,-1,-1,-1,-1,-1};
    std::string problemNames[8]{"","","","","","","",""};

    // Plant problem vars
    bool overWatered = false;
    bool underWatered = false;
    bool overMisted = false;
    bool underMisted = false;
    bool overFertilized = false;
    bool underFertilized = false;
    bool overLight = false;
    bool underLight = false;
    bool spiderMites = false;
    bool yellowProblem = false;
    bool wiltedProblem = false;
    bool bugsProblem = false;
    bool rootRotProblem = false;
    
    // Growth
    int totalGrowth = 0;
    int todaysGrowth = 0;
    int growthVolatility = 1;
    int negativeGrowthChain = 0;
    int maxNegativeGrowthChain = 127;

    // Plant stat tolerance
    int waterTolerance = 127;
    int misterTolerance = 127;
    int fertilizerTolerance = 127;
    int lightTolerance = 127;
    int lightToleranceOffSeason = 127;

    // Plant health
    int lastWateredDay = 0;
    int lastMistedDay = 0;
    int lastFertilizedDay = 0;
    int daysYellow = 0;
    int daysWilted = 0;
    int daysBugs = 0;
    int daysRootRot = 0;
    int maxDaysYellow = 28;
    int maxDaysWilted = 28;
    int maxDaysBugs = 28;
    int maxDaysRootRot = 28;

    // Set first tool use vars
    bool firstWater = false;
    bool firstMister = false;
    bool firstFertilizer = false;

    // Healing
    int recoveryDays = 0;
    int lightRecoveryStreak = 0;
    int lightRecoveryTolerance = 0;
    int twoTimesWateredStreak = 0;
    int waterCycleSkippedStreak = 0;

    // Environment
    int lightLevel = 0;
    int lightLevelDayAverage = 0;

    // Time
    int day = 0;
    int season = 0;
    QTimer *dayTimer = nullptr;
    QTimer *frameTimer = nullptr;
    int dayTimerInterval = 0;
    int frameTimerInterval = 0;
    int dayPausedRemainingTime = 0;
    bool gamePaused = false;
    int fps = 60;

    // Time specifics
    int dayLengthSeconds = 5;
    int seasonLengthDays = 28;
};

#endif // MODEL_H
