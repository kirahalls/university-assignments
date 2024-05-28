#ifndef GAMEOVERSCREEN_H
#define GAMEOVERSCREEN_H

#include <QWidget>
#include "model.h"

namespace Ui {
class GameOverScreen;
}

class GameOverScreen : public QWidget
{
    Q_OBJECT

public:
    explicit GameOverScreen(Model &model, int whichPlant, QWidget *parent = nullptr);
    ~GameOverScreen();

signals:
    void sendUserToHomeScreen();

private:
    Ui::GameOverScreen *ui;

    void setSnakePlantDead();
    void setPothosDead();
    void setPoinsettiaDead();
    void setStringOfPearlsDead();
    void playAgainButtonClicked();
};

#endif // GAMEOVERSCREEN_H
