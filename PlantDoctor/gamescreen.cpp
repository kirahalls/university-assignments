#include "gamescreen.h"
#include "homescreen.h"
#include "snakeplant.h"
#include "poinsettia.h"
#include "pothos.h"
#include "stringofpearls.h"
#include "ui_gamescreen.h"

GameScreen::GameScreen(Model &model, int whichPlant, QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::GameScreen)
{
    ui->setupUi(this);

    diseaseInfo = new PlantDiseaseCard;

    diseaseInfo->hide();

    yellow = false;
    wilt = false;
    bugs = false;

    switch(whichPlant)
    {
    case 1:
    {
        plant = new SnakePlant;

        ui->pothosInfo->hide();
        ui->poinsettiaInfo->hide();
        ui->stringOfPearlsInfo->hide();

        // Set snake plant body in physics pane
        ui->physicsPane->setSnakePlantBody();

        break;
    }

    case 2:
    {
        plant = new Pothos;

        ui->snakePlantInfo->hide();
        ui->poinsettiaInfo->hide();
        ui->stringOfPearlsInfo->hide();

        // Set pothos body in physics pane
        ui->physicsPane->setPothosBody();

        break;
    }
    case 3:
    {
        plant = new Poinsettia;

        ui->pothosInfo->hide();
        ui->snakePlantInfo->hide();
        ui->stringOfPearlsInfo->hide();

        // Set poinsettia body in physics pane
        ui->physicsPane->setPoinsettiaBody();

        break;
    }

    case 4:
    {
        plant = new StringOfPearls;

        ui->pothosInfo->hide();
        ui->poinsettiaInfo->hide();
        ui->snakePlantInfo->hide();

        // Set string of pearls body in physics pane
        ui->physicsPane->setStringOfPearlsBody();

        break;
    }
    }

    updateSunIcon();
    ui->pauseButton->setToolTip("Pause");
    ui->pauseButton->setIcon(QIcon(":/buttons/assets/Buttons/pause.png"));
    ui->pauseButton->setIconSize(QSize(21, 21));

    ui->plantPic->setPixmap(QIcon(plant->getHealthyImage()).pixmap(500, 600));
    ui->potPic->setPixmap(QIcon(plant->getHealthyPot()).pixmap(500, 600));

    ui->waterButton->setToolTip("Watering Tool");
    ui->waterButton->setIcon(QIcon(":/buttons/assets/Buttons/WateringToolButton.png"));
    ui->waterButton->setIconSize(QSize(91, 68));

    ui->fertilizeButton->setToolTip("Fertilizer Tool");
    ui->fertilizeButton->setIcon(QIcon(":/buttons/assets/Buttons/FertilizerToolButton.png"));
    ui->fertilizeButton->setIconSize(QSize(91, 68));

    ui->mistButton->setToolTip("Mist Tool");
    ui->mistButton->setIcon(QIcon(":/buttons/assets/Buttons/MisterToolButton.png"));
    ui->mistButton->setIconSize(QSize(91, 68));

    ui->insecticideButton->setToolTip("Insecticide Tool");
    ui->insecticideButton->setIcon(QIcon(":/buttons/assets/Buttons/InsecticideToolButton.png"));
    ui->insecticideButton->setIconSize(QSize(91, 68));

    ui->repotButton->setToolTip("Repot Tool");
    ui->repotButton->setIcon(QIcon(":/buttons/assets/Buttons/RepotToolButton.png"));
    ui->repotButton->setIconSize(QSize(91, 68));

    connect(ui->toggleDiseaseCard, &QPushButton::clicked, this, &GameScreen::showDiseaseCard);
    connect(ui->sunSlider, &QSlider::valueChanged, this, &GameScreen::updateSunIcon);
    connect(ui->pauseButton, &QPushButton::clicked, this, &GameScreen::pauseOrPlayTime);
    connect(ui->waterButton, &QPushButton::clicked, ui->physicsPane, &PhysicsPane::doWateringAnim);
    connect(ui->fertilizeButton, &QPushButton::clicked, ui->physicsPane, &PhysicsPane::doFertilizingAnim);
    connect(ui->mistButton, &QPushButton::clicked, ui->physicsPane, &PhysicsPane::doMistingAnim);
    connect(ui->insecticideButton, &QPushButton::clicked, ui->physicsPane, &PhysicsPane::doInsecticideAnim);

    //Connections for changing mouse icon when selecting a tool.
    connect(ui->waterButton, &QPushButton::clicked, this, &GameScreen::changeMouseToWateringCan);
    connect(ui->fertilizeButton, &QPushButton::clicked, this, &GameScreen::changeMouseToFertilizer);
    connect(ui->mistButton, &QPushButton::clicked, this, &GameScreen::changeMouseToMister);
    connect(ui->insecticideButton, &QPushButton::clicked, this, &GameScreen::changeMouseToInsecticide);
    connect(ui->repotButton, &QPushButton::clicked, this, &GameScreen::changeMouseToRepot);


    // Tool Button Connections
    connect(ui->waterButton, &QPushButton::clicked, &model, &Model::useWater);
    connect(ui->mistButton, &QPushButton::clicked, &model, &Model::useMister);
    connect(ui->fertilizeButton, &QPushButton::clicked, &model, &Model::useFertilizer);
    connect(ui->insecticideButton, &QPushButton::clicked, &model, &Model::usePesticide);
    connect(ui->repotButton, &QPushButton::clicked, &model, &Model::useRepot);
    connect(ui->sunSlider, &QSlider::valueChanged, &model, &Model::setLightLevel);

    // Model signals connecting to slots in GameScreen
    connect(&model, &Model::displaySeason, this, &GameScreen::updateSeason);
    connect(&model, &Model::displayDay, this, &GameScreen::updateOnDayChange);
    connect(&model, &Model::plantToggleHealthy, this, &GameScreen::updateOnHealthy);
    connect(&model, &Model::plantToggleYellow, this, &GameScreen::updateOnYellow);
    connect(&model, &Model::plantToggleWilted, this, &GameScreen::updateOnWilted);
    connect(&model, &Model::plantToggleBugs, this, &GameScreen::updateOnBugs);
    connect(&model, &Model::plantToggleRootRot, this, &GameScreen::updateOnRootRot);
    connect(&model, &Model::plantToggleDead, this, &GameScreen::updateOnDead);
    connect(&model, &Model::showCurrentProblems, this, &GameScreen::displayCurrentProblems);

    connect(this, &GameScreen::pauseGame, &model, &Model::pause);
    connect(this, &GameScreen::resumeGame, &model, &Model::resume);

    connect(&model, &Model::gameOver, this, [&model, this, whichPlant]() {
        GameOverScreen *gameOver = new GameOverScreen(model, whichPlant);
        this->hide();
        gameOver->show();
    });
    connect(ui->homeButton, &QPushButton::clicked, this, [this, &model]() {
        HomeScreen *goHome = new HomeScreen(model);
        model.killGame();
        this->hide();
        goHome->show();
    });

    // setting frequencies
    model.setWaterFrequencyOnSeason(plant->getDaysToWaterOnSeason());
    model.setMisterFrequencyOnSeason(plant->getDaysToMistOnSeason());
    model.setFertilizerFrequencyOnSeason(plant->getDaysToFertilizeOnSeason());
    model.setRequiredLightLevelOnSeason(plant->getRequiredLightOnSeason());

    // set off season frequencies
    model.setWaterFrequencyOffSeason(plant->getDaysToWaterOffSeason());
    model.setMisterFrequencyOffSeason(plant->getDaysToMistOffSeason());
    model.setFertilizerFrequencyOffSeason(plant->getDaysToFertilizeOffSeason());
    model.setRequiredLightLevelOffSeason(plant->getRequiredLightOffSeason());

    // setting tolerances
    model.setWaterTolerance(plant->getWaterTolerance());
    model.setMisterTolerance(plant->getMistTolerance());
    model.setFertilizerTolerance(plant->getFertilizerTolerance());
    model.setLightTolerance(plant->getLightTolerance());
    model.setLightToleranceOffSeason(plant->getLightToleranceOffSeason());

    // Tell model to start game
    model.startGame();
}

void GameScreen::updateSunIcon()
{
    int sunLevel = ui->sunSlider->value();

    //0% = full shade
    if(sunLevel >= 0 && sunLevel < 22)
    {
        ui->sunIndicator->setPixmap(QIcon(":/SunIndicator/assets/0sun.png").pixmap(58, 41));
        ui->sunIndicator->setToolTip("Full Shade");
    }

    //25% = partial sun
    if(sunLevel > 22 && sunLevel < 46)
    {
        ui->sunIndicator->setToolTip("Partial Sun");
        ui->sunIndicator->setPixmap(QIcon(":/SunIndicator/assets/25sun.png").pixmap(58, 41));
    }

    //50% = half sun
    if(sunLevel > 46 && sunLevel < 72)
    {
        ui->sunIndicator->setToolTip("Half Sun");
        ui->sunIndicator->setPixmap(QIcon(":/SunIndicator/assets/50sun.png").pixmap(58, 41));
    }

    //75% = partial shade
    if(sunLevel > 72 && sunLevel < 96)
    {
        ui->sunIndicator->setToolTip("Partial Shade");
        ui->sunIndicator->setPixmap(QIcon(":/SunIndicator/assets/75sun.png").pixmap(58, 41));
    }

    //100% = full sun
    if(sunLevel > 96 && sunLevel < 101)
    {
        ui->sunIndicator->setToolTip("Full Sun");
        ui->sunIndicator->setPixmap(QIcon(":/SunIndicator/assets/100sun.png").pixmap(58, 41));
    }
}

void GameScreen::showDiseaseCard()
{
    diseaseInfo->show();
}

void GameScreen::pauseOrPlayTime()
{
    if(timePlaying)
    {
        timePlaying = false;
        timePaused = true;
        ui->pauseButton->setToolTip("Play");
        ui->pauseButton->setIcon(QIcon(":/buttons/assets/Buttons/play.png"));
        ui->pauseButton->setIconSize(QSize(21, 21));
        emit pauseGame();
        // Disable buttons while the game is paused
        ui->waterButton->setEnabled(false);
        ui->mistButton->setEnabled(false);
        ui->fertilizeButton->setEnabled(false);
        ui->insecticideButton->setEnabled(false);
        ui->repotButton->setEnabled(false);
        ui->sunSlider->setEnabled(false);
    }
    else if(timePaused)
    {
        timePaused = false;
        timePlaying = true;
        ui->pauseButton->setToolTip("Pause");
        ui->pauseButton->setIcon(QIcon(":/buttons/assets/Buttons/pause.png"));
        ui->pauseButton->setIconSize(QSize(21, 21));
        emit resumeGame();
        // Enable buttons while the game is resumed
        ui->waterButton->setEnabled(true);
        ui->mistButton->setEnabled(true);
        ui->fertilizeButton->setEnabled(true);
        ui->insecticideButton->setEnabled(true);
        ui->repotButton->setEnabled(true);
        ui->sunSlider->setEnabled(true);
    }
}

void GameScreen::updateSeason(int season)
{
    switch (season)
    {
    case 0:
        ui->seasonIndicator->setToolTip("Spring");
        ui->seasonIndicator->setPixmap(QIcon(":/SeasonIndicator/assets/spring.png").pixmap(71, 61));
        break;
    case 1:
        ui->seasonIndicator->setToolTip("Summer");
        ui->seasonIndicator->setPixmap(QIcon(":/SeasonIndicator/assets/summer.png").pixmap(71, 61));
        break;
    case 2:
        ui->seasonIndicator->setToolTip("Fall");
        ui->seasonIndicator->setPixmap(QIcon(":/SeasonIndicator/assets/fall.png").pixmap(71, 61));
        break;
    case 3:
        ui->seasonIndicator->setToolTip("Winter");
        ui->seasonIndicator->setPixmap(QIcon(":/SeasonIndicator/assets/winter.png").pixmap(71, 61));
        break;
    }
}

void GameScreen::updateOnHealthy()
{
    if(bugs)
    {
       ui->bugPic->setPixmap(QIcon(plant->getBugsImage()).pixmap(500, 600));
    }

    qDebug() << "Plant is now HEALTHY";
    ui->plantPic->setPixmap(QIcon(plant->getHealthyImage()).pixmap(500, 600));
}

void GameScreen::updateOnYellow(bool isYellow)
{
    yellow = isYellow;

    if (isYellow && wilt)
    {
        qDebug() << "plant is now YELLOW and WILTED\n";
        ui->plantPic->setPixmap(QIcon(plant->getWiltedYellowImage()).pixmap(500, 600));
    }

    else if(isYellow)
    {
        qDebug() << "plant is now YELLOW\n";
        ui->plantPic->setPixmap(QIcon(plant->getYellowImage()).pixmap(500, 600));
    }

    else
    {
        qDebug() << "plant is NOT yellow\n";
    }
}

void GameScreen::updateOnWilted(bool isWilted)
{
    wilt = isWilted;

    if(isWilted && bugs)
    {
        qDebug() << "plant is now WILTED and BUGGED\n";
        ui->bugPic->setPixmap(QIcon(plant->getBugsWiltedImage()).pixmap(500, 600));
    }

    if (isWilted && yellow)
    {
        qDebug() << "plant is now WILTED and YELLOW\n";
        ui->plantPic->setPixmap(QIcon(plant->getWiltedYellowImage()).pixmap(500, 600));
    }

    else if(isWilted)
    {
        qDebug() << "plant is now WILTED\n";
        ui->plantPic->setPixmap(QIcon(plant->getWiltedImage()).pixmap(500, 600));
    }

    else
    {
        qDebug() << "plant is NOT wilted\n";
    }
}

void GameScreen::updateOnBugs(bool hasBugs)
{
    bugs = hasBugs;

    if(hasBugs && wilt)
    {
        qDebug() << "plant is now BUGGED and WILTED\n";
        ui->bugPic->setPixmap(QIcon(plant->getBugsWiltedImage()).pixmap(500, 600));
    }

    else if (hasBugs)
    {
        qDebug() << "plant is now has BUGS\n";
        ui->bugPic->setPixmap(QIcon(plant->getBugsImage()).pixmap(500, 600));
    }

    else {
        qDebug() << "plant is CLEAN of bugs\n";
        ui->bugPic->clear();
    }
}

void GameScreen::updateOnRootRot(bool hasRootRot)
{
    if (hasRootRot)
    {
        qDebug() << "plant now has ROOT ROT\n";
        ui->potPic->setPixmap(QIcon(plant->getRootRotPotImage()).pixmap(500, 600));
    }

    else
    {
        qDebug() << "plant is FREE from root rot\n";
        ui->potPic->setPixmap(QIcon(plant->getHealthyPot()).pixmap(500, 600));
    }
}

void GameScreen::updateOnDead(bool isDead)
{
    if (isDead)
    {
        qDebug() << "plant is now DEAD\n";
        QString filepath = plant->getDeadImage();
        qDebug() << filepath;
        ui->plantPic->setPixmap(QIcon(filepath).pixmap(500, 600));
    }

    else
    {
        qDebug() << "plant is ALIVE\n";
    }
}

void GameScreen::updateOnDayChange(int day)
{
    QString dayText = QString("Day: %1").arg(day);
    ui->timeDisplay->setText(dayText);
}

void GameScreen::displayCurrentProblems(std::string currentProblems)
{
    QString problems = QString::fromStdString(currentProblems);
    ui->currentStateUpdate->setText(problems);
}

void GameScreen::updateOnDayProgressChange(double progress)
{

}

GameScreen::~GameScreen()
{
    delete ui;
    delete diseaseInfo;
    delete plant;
}

void GameScreen::changeMouseToWateringCan()
{
    QPixmap p = QPixmap(":/buttons/assets/Buttons/watering-canMouseIcon.png");
    QCursor c = QCursor(p, 0, 0);
    setCursor(c);
}


void GameScreen::changeMouseToFertilizer()
{
    QPixmap p = QPixmap(":/buttons/assets/Buttons/FertilizerMouseIcon.png");
    QCursor c = QCursor(p, 0, 0);
    setCursor(c);
}

void GameScreen::changeMouseToMister()
{
    QPixmap p = QPixmap(":/buttons/assets/Buttons/MisterMouseIcon.png");
    QCursor c = QCursor(p, 0, 0);
    setCursor(c);
}

void GameScreen::changeMouseToRepot()
{
    QPixmap p = QPixmap(":/buttons/assets/Buttons/RepotMouseIcon.png");
    QCursor c = QCursor(p, 0, 0);
    setCursor(c);
}


void GameScreen::changeMouseToInsecticide()
{
    QPixmap p = QPixmap(":/buttons/assets/Buttons/insecticideMouseIcon.png");
    QCursor c = QCursor(p, 0, 0);
    setCursor(c);
}


