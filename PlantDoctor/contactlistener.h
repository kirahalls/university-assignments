#ifndef CONTACTLISTENER_H
#define CONTACTLISTENER_H

#include <QWidget>
#include <Box2D/Box2D.h>
#include <vector>

struct MyContact
{
    b2Fixture *fixtureA;
    b2Fixture *fixtureB;
    b2Body *body;
    b2Body *ground;
    bool operator==(const MyContact& other) const
    {
        return (fixtureA == other.fixtureA) && (fixtureB == other.fixtureB);
    }
};

class ContactListener: public b2ContactListener
{

public:
    std::vector<MyContact>_contacts;

    ContactListener();
    ~ContactListener();

    virtual void BeginContact(b2Contact* contact);
    virtual void EndContact(b2Contact* contact);
    virtual void PreSolve(b2Contact* contact, const b2Manifold* oldManifold);
    virtual void PostSolve(b2Contact* contact, const b2ContactImpulse* impulse);
};

#endif // CONTACTLISTENER_H
