#ifndef GAMESCREEN_H
#define GAMESCREEN_H

#include <QMainWindow>
#include "plantinterface.h"
#include "plantdiseasecard.h"
#include "model.h"
#include "gameoverscreen.h"

QT_BEGIN_NAMESPACE
namespace Ui { class GameScreen; }

QT_END_NAMESPACE
class GameScreen : public QMainWindow
{
    Q_OBJECT

public:

    explicit GameScreen(Model& model, int whichPlant, QWidget *parent = nullptr);
    ~GameScreen();

signals:
    void pauseGame();
    void resumeGame();

public slots:
    void showDiseaseCard();
    void updateSunIcon();
    void pauseOrPlayTime();
    void updateOnDayChange(int day);
    void updateOnDayProgressChange(double progress);
    void updateSeason(int season);
    void updateOnHealthy();
    void updateOnYellow(bool isYellow);
    void updateOnWilted(bool isWilted);
    void updateOnBugs(bool hasBugs);
    void updateOnRootRot(bool hasRootRot);
    void updateOnDead(bool isDead);
    void displayCurrentProblems(std::string currentProblems);

private slots:
    void changeMouseToWateringCan();
    void changeMouseToFertilizer();
    void changeMouseToMister();
    void changeMouseToInsecticide();
    void changeMouseToRepot();

private:
    Ui::GameScreen *ui;
    PlantDiseaseCard *diseaseInfo;
    PlantInterface *plant;
    bool yellow;
    bool wilt;
    bool bugs;
    bool timePlaying = true;
    bool timePaused = false;
};

#endif // GAMESCREEN_H
