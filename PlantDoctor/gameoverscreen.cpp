#include "gameoverscreen.h"
#include "ui_gameoverscreen.h"
#include "homescreen.h"


GameOverScreen::GameOverScreen(Model &model, int whichPlant, QWidget *parent) :
    QWidget(parent),
    ui(new Ui::GameOverScreen)
{
    ui->setupUi(this);

    switch(whichPlant)
    {
    case 1:
    {
        setSnakePlantDead();

        break;
    }

    case 2:
    {
        setPothosDead();

        break;
    }
    case 3:
    {
        setPoinsettiaDead();

        break;
    }

    case 4:
    {
        setStringOfPearlsDead();

        break;
    }
    }

    connect(ui->returnToHomeScreenButton, &QPushButton::clicked, this, &GameOverScreen::playAgainButtonClicked);
    connect(this, &GameOverScreen::sendUserToHomeScreen, this, [this, &model]() {
        HomeScreen *levelSelect = new HomeScreen(model);
        this->hide();
        levelSelect->show();
    });
}

void GameOverScreen::setSnakePlantDead()
{
    ui->deadPlant->setPixmap(QIcon(":/SnakePlant/assets/SnakePlant/SnakePlantDead.png").pixmap(191, 221));
}

void GameOverScreen::setPothosDead()
{
    ui->deadPlant->setPixmap(QIcon(":/Pothos/assets/Pothos/PothosDead.png").pixmap(191, 221));
}

void GameOverScreen::setPoinsettiaDead()
{
    ui->deadPlant->setPixmap(QIcon(":/Poinsettia/assets/Poinsettia/PoinsettiaDead.png").pixmap(191, 221));
}

void GameOverScreen::setStringOfPearlsDead()
{
     ui->deadPlant->setPixmap(QIcon(":/StringOfPearls/assets/StringOfPearls/StringOfPearlsDead.png").pixmap(191, 221));
}

void GameOverScreen::playAgainButtonClicked()
{
     emit sendUserToHomeScreen();
}

GameOverScreen::~GameOverScreen()
{
    delete ui;
}
