#include <iostream>
#include "physicspane.h"
#include "ui_physicspane.h"

#include <QTimer>
#include <QPainter>
#include <QDebug>
#include <set>

PhysicsPane::PhysicsPane(QWidget *parent) :
    QWidget(parent),
    ui(new Ui::PhysicsPane),
    world(b2World(b2Vec2(0.0f, 10.0f)))
{
    ui->setupUi(this);

    contactListener = new ContactListener();
    world.SetContactListener(contactListener);

    //Set widget width and height
    setMinimumWidth(400);
    setMinimumHeight(500);


    setupPhysics();
}

PhysicsPane::~PhysicsPane()
{
    delete ui;
    delete contactListener;
}

void PhysicsPane::setupPhysics()
{
    //Set world size to widget size
    worldWidth = width();
    worldHeight = height();

    //Set snake plant body as default
    setSnakePlantBody();

    //Create timer to call slot updateWorld every 1/60 seconds
    QTimer *timer = new QTimer(this);
    connect(timer, SIGNAL(timeout()), this, SLOT(updateWorld()));
    timer->start(1000/60);

    //Run a test animation
    //    doWateringAnim();
    //    doFertilizingAnim();
    //    doInsecticideAnim();
    //    doMistingAnim();

}

void PhysicsPane::updateWorld()
{
    //Step the world
    world.Step(1.0f/10.0f, 10, 10);

    //Update the screen
    update();

    //check contacts
    std::vector<MyContact>::iterator pos;
    for(pos = contactListener->_contacts.begin(); pos != contactListener->_contacts.end(); ++pos)
    {
        MyContact contact = *pos;

        //Destroy other body
        if (contact.fixtureA->GetBody() == groundBody)
        {
            bodiesToDestroy.push_back(contact.fixtureB->GetBody());
        }
        else if (contact.fixtureB->GetBody() == groundBody)
        {
            bodiesToDestroy.push_back(contact.fixtureA->GetBody());
        }
    }

    //Delete fallen bodies
    deleteFallenBodies();
}

void PhysicsPane::deleteFallenBodies()
{
    for(auto it = bodiesToDestroy.begin(); it != bodiesToDestroy.end(); it++)
    {
        world.DestroyBody(*it);
        bodies.erase(std::remove(bodies.begin(), bodies.end(), *it), bodies.end());
    }

    bodiesToDestroy.clear();
}

void PhysicsPane::paintEvent(QPaintEvent *event)
{
    //Create a painter to draw on the screen
    QPainter painter(this);

    //Draw the bodies
    for(int i = 0; i < bodies.size(); i++)
    {
        b2Vec2 position = bodies[i]->GetPosition();
        float32 angle = bodies[i]->GetAngle();

        //Draw the image so that it's centered around the fixture with location rotation and scale
        painter.translate(position.x, position.y);
        painter.rotate(angle * 180.0 / M_PI);
        painter.scale(imageScale, imageScale);
        painter.drawImage(-objectImage.width()/2, -objectImage.height()/2, objectImage);
        painter.resetTransform();
    }
}

void PhysicsPane::clearBodies()
{
    for(int i = 0; i < bodies.size(); i++)
    {
        world.DestroyBody(bodies[i]);
    }
    bodies.clear();
}

void PhysicsPane::doWateringAnim()
{
    //Clear the bodies
    clearBodies();

    // Define the dynamic body. We set its position and call the body factory.
    b2BodyDef bodyDef;
    bodyDef.type = b2_dynamicBody;

    // Define a circle shape for our dynamic body.
    b2CircleShape dynamicCircle;
    dynamicCircle.m_radius = 0.5f;

    // Define the dynamic body fixture.
    b2FixtureDef fixtureDef;
    fixtureDef.shape = &dynamicCircle;

    // Set the box density to be non-zero, so it will be dynamic.
    fixtureDef.density = 0.2f;

    // Override the default friction.
    fixtureDef.friction = 0.1f;

    // Set restitution
    fixtureDef.restitution = 0.9f;

    for (int i = 0; i < 50; i++)
    {
        float xPos = worldWidth/4.0f + rand() % 200;
        float yPos = worldHeight - 500.0f + rand() % 150;
        bodyDef.position.Set(xPos, yPos + waterYOffset);
        b2Body* body = world.CreateBody(&bodyDef);
        // Add the shape to the body.
        body->CreateFixture(&fixtureDef);

        //Decrease linear damping to increase speed
        body->SetLinearDamping(0.0f);

        //Increase angular damping to slow down rotation
        body->SetAngularDamping(0.5f);

        //Add force
        body->ApplyForce(b2Vec2((worldWidth/2.0f - body->GetPosition().x) * 100.0f, (worldHeight/2.0f - body->GetPosition().y) * 100.0f),
                         b2Vec2(body->GetPosition().x, body->GetPosition().y), true);

        //Add the body and label to their respective vectors
        bodies.push_back(body);
    }

    //Set current physics object image
    objectImage = QImage(":/Animation/assets/waterdrop.png");
    imageScale = 0.05;
}

void PhysicsPane::doFertilizingAnim()
{
    //Clear the bodies
    clearBodies();

    // Define the dynamic body. We set its position and call the body factory.
    b2BodyDef bodyDef;
    bodyDef.type = b2_dynamicBody;

    // Define a circle shape for our dynamic body.
    b2CircleShape dynamicCircle;
    dynamicCircle.m_radius = 0.8f;

    // Define the dynamic body fixture.
    b2FixtureDef fixtureDef;
    fixtureDef.shape = &dynamicCircle;

    // Set the box density to be non-zero, so it will be dynamic.
    fixtureDef.density = 0.2f;

    // Override the default friction.
    fixtureDef.friction = 0.1f;

    // Set restitution
    fixtureDef.restitution = 0.9f;

    for (int i = 0; i < 25; i++)
    {
        float xPos = worldWidth/4.0f + rand() % 200;
        float yPos = worldHeight - 300.0f + rand() % 150;
        bodyDef.position.Set(xPos, yPos + fertilizerYOffset);
        b2Body* body = world.CreateBody(&bodyDef);
        // Add the shape to the body.
        body->CreateFixture(&fixtureDef);

        //Decrease linear damping to increase speed
        body->SetLinearDamping(0.0f);

        //Increase angular damping to slow down rotation
        body->SetAngularDamping(1.0f);

        //Add force
        body->ApplyForce(b2Vec2((worldWidth/2.0f - body->GetPosition().x) * 100.0f, (worldHeight/2.0f - body->GetPosition().y) * 100.0f),
                         b2Vec2(body->GetPosition().x, body->GetPosition().y), true);

        //Add the body and label to their respective vectors
        bodies.push_back(body);
        //        timer.singleShot(700 + i*500, this, [=]() {bodiesToDestroy.push_back(body);});
    }

    //Set current physics object image
    objectImage = QImage(":/Animation/assets/fertilizer.png");
    imageScale = 0.5;

}

void PhysicsPane::doMistingAnim()
{
    //Clear the bodies
    clearBodies();

    // Define the dynamic body. We set its position and call the body factory.
    b2BodyDef bodyDef;
    bodyDef.type = b2_dynamicBody;

    // Define a circle shape for our dynamic body.
    b2CircleShape dynamicCircle;
    dynamicCircle.m_radius = 2.5f;

    // Define the dynamic body fixture.
    b2FixtureDef fixtureDef;
    fixtureDef.shape = &dynamicCircle;

    // Set the box density to be non-zero, so it will be dynamic.
    fixtureDef.density = 0.2f;

    // Override the default friction.
    fixtureDef.friction = 0.1f;

    // Set restitution
    fixtureDef.restitution = 0.9f;

    for (int i = 0; i < 10; i++)
    {
        float xPos = worldWidth/2.0f + rand() % 200;
        float yPos = worldHeight - 400.0f + rand() % 150;
        bodyDef.position.Set(xPos, yPos + mistYOffset);
        b2Body* body = world.CreateBody(&bodyDef);
        // Add the shape to the body.
        body->CreateFixture(&fixtureDef);

        //Decrease linear damping to increase speed
        body->SetLinearDamping(0.0f);

        //Increase angular damping to slow down rotation
        body->SetAngularDamping(0.5f);

        //Add force
        body->ApplyForce(b2Vec2((worldWidth/2.0f - body->GetPosition().x) * 1000.0f, (worldHeight/2.0f - body->GetPosition().y) * 800.0f),
                         b2Vec2(body->GetPosition().x, body->GetPosition().y), true);

        //Add the body and label to their respective vectors
        bodies.push_back(body);
        //        timer.singleShot(700 + i*500, this, [=]() {bodiesToDestroy.push_back(body);});
    }

    for (int i = 0; i < 10; i++)
    {
        float xPos = worldWidth/-5.0f + rand() % 200;
        float yPos = worldHeight - 400.0f + rand() % 150;
        bodyDef.position.Set(xPos, yPos + mistYOffset);
        b2Body* body = world.CreateBody(&bodyDef);
        // Add the shape to the body.
        body->CreateFixture(&fixtureDef);

        //Decrease linear damping to increase speed
        body->SetLinearDamping(0.0f);

        //Increase angular damping to slow down rotation
        body->SetAngularDamping(0.5f);

        //Add force
        body->ApplyForce(b2Vec2((worldWidth/2.0f - body->GetPosition().x) * 1000.0f, (worldHeight/2.0f - body->GetPosition().y) * 800.0f),
                         b2Vec2(body->GetPosition().x, body->GetPosition().y), true);

        //Add the body and label to their respective vectors
        bodies.push_back(body);
        //        timer.singleShot(700 + i*500, this, [=]() {bodiesToDestroy.push_back(body);});
    }

    //Set current physics object image
    objectImage = QImage(":/Animation/assets/mist.png");
    imageScale = 2.0;
}

void PhysicsPane::doInsecticideAnim()
{
    //Clear the bodies
    clearBodies();

    // Define the dynamic body. We set its position and call the body factory.
    b2BodyDef bodyDef;
    bodyDef.type = b2_dynamicBody;

    // Define a circle shape for our dynamic body.
    b2CircleShape dynamicCircle;
    dynamicCircle.m_radius = 0.1f;

    // Define the dynamic body fixture.
    b2FixtureDef fixtureDef;
    fixtureDef.shape = &dynamicCircle;

    // Set the box density to be non-zero, so it will be dynamic.
    fixtureDef.density = 0.2f;

    // Override the default friction.
    fixtureDef.friction = 0.1f;

    // Set restitution
    fixtureDef.restitution = 0.9f;

    for (int i = 0; i < 50; i++)
    {
        float xPos = worldWidth/3.0f + rand() % 200;
        float yPos = worldHeight - 500.0f + rand() % 150;
        bodyDef.position.Set(xPos, yPos + insecticideYOffset);
        b2Body* body = world.CreateBody(&bodyDef);
        // Add the shape to the body.
        body->CreateFixture(&fixtureDef);

        //Decrease linear damping to increase speed
        body->SetLinearDamping(0.0f);

        //Increase angular damping to slow down rotation
        body->SetAngularDamping(0.5f);

        //Add force
        body->ApplyForce(b2Vec2((worldWidth/2.0f - body->GetPosition().x) * 1000.0f, (worldHeight/2.0f - body->GetPosition().y) * 800.0f),
                         b2Vec2(body->GetPosition().x, body->GetPosition().y), true);

        //Add the body and label to their respective vectors
        bodies.push_back(body);
    }

    for (int i = 0; i < 50; i++)
    {
        float xPos = worldWidth/-30.0f + rand() % 200;
        float yPos = worldHeight - 500.0f + rand() % 150;
        bodyDef.position.Set(xPos, yPos + insecticideYOffset);
        b2Body* body = world.CreateBody(&bodyDef);
        // Add the shape to the body.
        body->CreateFixture(&fixtureDef);

        //Decrease linear damping to increase speed
        body->SetLinearDamping(0.0f);

        //Increase angular damping to slow down rotation
        body->SetAngularDamping(0.5f);

        //Add force
        body->ApplyForce(b2Vec2((worldWidth/2.0f - body->GetPosition().x) * 1000.0f, (worldHeight/2.0f - body->GetPosition().y) * 800.0f),
                         b2Vec2(body->GetPosition().x, body->GetPosition().y), true);

        //Add the body and label to their respective vectors
        bodies.push_back(body);
    }

    //Set current physics object image
    objectImage = QImage(":/Animation/assets/insecticide.png");
    imageScale = 0.0099;
}

void PhysicsPane::setSnakePlantBody()
{
    //Clear existing ground body if it exists
    if (groundBody != nullptr)
    {
        world.DestroyBody(groundBody);
    }

    // Define the ground body.
    groundBodyDef.position.Set(worldWidth/2.0f, worldHeight - 100);

    // Define the ground box shape.
    b2PolygonShape groundBox;
    groundBox.SetAsBox(100.0f, 60.0f);

    // Create ground body
    groundBody = world.CreateBody(&groundBodyDef);
    groundBody->CreateFixture(&groundBox, 0.0f);

    //Set friction
    groundBody->GetFixtureList()->SetFriction(0.1f);

    //Set offsets
    waterYOffset = 0.0;
    mistYOffset = 0.0;
    fertilizerYOffset = 0.0;
    insecticideYOffset = 0.0;
}

void PhysicsPane::setPothosBody()
{
    //Clear existing ground body if it exists
    if (groundBody != nullptr)
    {
        world.DestroyBody(groundBody);
    }

    // Define the ground body.
    groundBodyDef.position.Set(worldWidth/2.0f, worldHeight - 100);

    // Define the ground box shape.
    b2PolygonShape groundBox;
    groundBox.SetAsBox(100.0f, 50.0f);

    // Create ground body
    groundBody = world.CreateBody(&groundBodyDef);
    groundBody->CreateFixture(&groundBox, 0.0f);

    //Set friction
    groundBody->GetFixtureList()->SetFriction(0.1f);

    //Set offsets
    waterYOffset = 0.0;
    mistYOffset = 0.0;
    fertilizerYOffset = 0.0;
    insecticideYOffset = 0.0;
}

void PhysicsPane::setPoinsettiaBody()
{
    //Clear existing ground body if it exists
    if (groundBody != nullptr)
    {
        world.DestroyBody(groundBody);
    }

    // Define the ground body.
    groundBodyDef.position.Set(worldWidth/2.0f, worldHeight - 100);

    // Define the ground box shape.
    b2PolygonShape groundBox;
    groundBox.SetAsBox(100.0f, 60.0f);

    // Create ground body
    groundBody = world.CreateBody(&groundBodyDef);
    groundBody->CreateFixture(&groundBox, 0.0f);

    //Set friction
    groundBody->GetFixtureList()->SetFriction(0.1f);

    //Set offsets
    waterYOffset = 0.0;
    mistYOffset = 0.0;
    fertilizerYOffset = 0.0;
    insecticideYOffset = 0.0;
}

void PhysicsPane::setStringOfPearlsBody()
{
    //Clear existing ground body if it exists
    if (groundBody != nullptr)
    {
        world.DestroyBody(groundBody);
    }

    // Define the ground body.
    groundBodyDef.position.Set(worldWidth/2.0f, worldHeight - 400);

    // Define the ground box shape.
    b2PolygonShape groundBox;
    groundBox.SetAsBox(100.0f, 30.0f);

    // Create ground body
    groundBody = world.CreateBody(&groundBodyDef);
    groundBody->CreateFixture(&groundBox, 0.0f);

    //Set friction
    groundBody->GetFixtureList()->SetFriction(0.1f);

    //Set offsets
    waterYOffset = -100.0;
    fertilizerYOffset = -250.0;
    insecticideYOffset = -100.0;
}
