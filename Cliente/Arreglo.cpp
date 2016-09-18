#include "Arreglo.h"

//
// Created by bryan on 04/09/16.
//
template <typename T>
Arreglo<T>::Arreglo(){
    _index=0;
    _size=10;
    _array = new T[_size];
}

template <typename T>
int Arreglo<T>::get_size(){
    cout<<_index<<endl;
    return _index;
}

template <typename T>
void Arreglo<T>::resize(T* pArray){
    int new_size = _size*2;
    T* new_array = new T[new_size];
    for(int i=0;i<_size;i++){
        new_array[i]=pArray[i];
    }
    *_array=*new_array;
    _size=new_size;
}

template <typename T>
void Arreglo<T>::insert(T pData){
    if(_index>=_size){
        resize(_array);
    }
    _array[_index]=pData;
    _index++;
}

template <typename T>
void Arreglo<T>::edit(int pIndex, T pNewData) {
    if(pIndex<0||pIndex>=_index){
        cout<<"Fuera de rango";
    }else{
        _array[pIndex]=pNewData;
    }
}

template <typename T>
void Arreglo<T>::remove(int pIndex){
    if(pIndex<0||pIndex>=_index){
        cout<<"Fuera de rango";
    }else{
        for(int i=pIndex;i<_index;i++){
            _array[i]=_array[i+1];
        }
        _index--;
    }
}

template <typename T> T Arreglo<T>::get_data(int pIndex) {
    if(pIndex<0||pIndex>=_index){
        cout<<"Fuera de rango";
    }else {
        cout<<_array[pIndex]<<endl;
        return _array[pIndex];
    }
}

template <typename T>
T Arreglo<T>::get_first(){
    if(_index!=0) {cout<<_array[0]; return _array[0];}
    else return NULL; // qué retorno???
}

template <typename T>
T Arreglo<T>::get_last(){
    if(_index!=0) {cout<<_array[_index-1];return _array[_index-1];}
    else return NULL; // qué retorno???
}

template <typename T>
void Arreglo<T>::show_array() {
    for(int i=0;i<_index;i++){
        cout<<_array[i]<<endl;
    }
}

template <typename T>
int Arreglo<T>::get_index() const {
    cout<<_index<<endl;
    return _index;
}

