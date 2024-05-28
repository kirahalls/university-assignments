#ifndef HOMESCREEN_H
#define HOMESCREEN_H

#include <QMainWindow>
#include "model.h"

QT_BEGIN_NAMESPACE
namespace Ui { class HomeScreen; }
QT_END_NAMESPACE

class HomeScreen : public QMainWindow
{
    Q_OBJECT

public:
    HomeScreen(Model& model, QWidget *parent = nullptr);
    ~HomeScreen();

private slots:

private:
    Ui::HomeScreen *ui;

};
#endif // HOMESCREEN_H
