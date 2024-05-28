#ifndef PHYSICSPANE_H
#define PHYSICSPANE_H

#include <QWidget>
#include <Box2D/Box2D.h>
#include <vector>
#include "contactlistener.h"

namespace Ui {
class PhysicsPane;
}

class PhysicsPane : public QWidget
{
    Q_OBJECT

public:
    explicit PhysicsPane(QWidget *parent = nullptr);
    ~PhysicsPane();

    ///
    ///@brief Paints the canvas.
    ///
    ///@param event The paint event.
    ///
    void paintEvent(QPaintEvent *event) override;

public slots:
    void updateWorld();
    void doWateringAnim();
    void doFertilizingAnim();
    void doMistingAnim();
    void doInsecticideAnim();

    //Plant body setting
    void setSnakePlantBody();
    void setPothosBody();
    void setPoinsettiaBody();
    void setStringOfPearlsBody();

private:
    Ui::PhysicsPane *ui;
    ContactListener *contactListener;
    std::vector<b2Body*> bodiesToDestroy;

    void setupPhysics();
    void clearBodies();
    void deleteFallenBodies();

    b2World world;
    b2Body *groundBody = nullptr;
    b2BodyDef groundBodyDef;
    std::vector<b2Body*> bodies;
    QImage objectImage;
    float imageScale;

    float waterYOffset = 0.0;
    float mistYOffset = 0.0;
    float fertilizerYOffset = 0.0;
    float insecticideYOffset = 0.0;

    float worldX = 0.0;
    float worldY = 0.0;
    float worldWidth = 0.0;
    float worldHeight = 0.0;
};

#endif // PHYSICSPANE_H
