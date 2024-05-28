#include "homescreen.h"
#include "ui_homescreen.h"
#include "gamescreen.h"
#include "model.h"

HomeScreen::HomeScreen(Model &model, QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::HomeScreen)
{

    ui->setupUi(this);

    ui->tutorialButton->setIcon(QIcon(":/SnakePlant/assets/SnakePlant/SnakePlantPotCombo.png"));
    ui->tutorialButton->setIconSize(QSize(131, 111));

    ui->levelOneButton->setIcon(QIcon(":/Pothos/assets/Pothos/PothosPotCombo.png"));
    ui->levelOneButton->setIconSize(QSize(131, 100));

    ui->levelTwoButton->setIcon(QIcon(":/Poinsettia/assets/Poinsettia/PoinsettiaPotCombo.png"));
    ui->levelTwoButton->setIconSize(QSize(145, 100));

    ui->levelThreeButton->setIcon(QIcon(":/StringOfPearls/assets/StringOfPearls/StringOfPearlsPotCombo.png"));
    ui->levelThreeButton->setIconSize(QSize(145, 100));

    connect(ui->tutorialButton, &QPushButton::clicked, this, [this, &model]() {
        GameScreen *tutorial = new GameScreen(model, 1);
        this->hide();
        tutorial->show();
    });
    connect(ui->levelOneButton, &QPushButton::clicked, this, [this, &model]() {
        GameScreen *levelOne = new GameScreen(model, 2);
        this->hide();
        levelOne->show();
    });
    connect(ui->levelTwoButton, &QPushButton::clicked, this, [this, &model]() {
        GameScreen *levelTwo = new GameScreen(model, 3);
        this->hide();
        levelTwo->show();
    });
    connect(ui->levelThreeButton, &QPushButton::clicked, this, [this, &model]() {
        GameScreen *levelThree = new GameScreen(model, 4);
        this->hide();
        levelThree->show();
    });

}

HomeScreen::~HomeScreen()
{
    delete ui;

}
