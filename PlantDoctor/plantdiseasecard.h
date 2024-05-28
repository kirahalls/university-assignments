#ifndef PLANTDISEASECARD_H
#define PLANTDISEASECARD_H

#include <QWidget>

namespace Ui {
class PlantDiseaseCard;
}

class PlantDiseaseCard : public QWidget
{
    Q_OBJECT

public:
    explicit PlantDiseaseCard(QWidget *parent = nullptr);
    ~PlantDiseaseCard();

private:
    Ui::PlantDiseaseCard *ui;
};

#endif // PLANTDISEASECARD_H
