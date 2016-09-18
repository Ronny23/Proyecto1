//
// Created by bryan on 04/09/16.
//

#ifndef DATASTRUCTURES_ARREGLO_H
#define DATASTRUCTURES_ARREGLO_H
#include <iostream>

using namespace std;

template <class T>
class Arreglo{
public:
    Arreglo();
    int get_size();
    void resize(T*);
    void insert(T);
    void edit(int,T);
    void remove(int);
    void show_array();
    T get_data(int);
    T get_last();
    T get_first();
    int get_index() const;

private:
    int _size;
    int _index;
    T* _array;
};




#endif //DATASTRUCTURES_ARREGLO_H
