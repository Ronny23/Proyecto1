#include "iostream"
using namespace std;

template <class xType>
class xReference
{
    private:
        string ID;
        int size;
        xType type;
    public:
    xReference<xType>(string pId, int pSize,xType pType){
        ID=pId;
        size=pSize;
        type=pType;
    }

    const string &getID() const {return ID;}
    int getSize() const {return size;}
    xType getType() const {return type;}
};


/*
template<typename xType>
xReference<xType>& operator *(const xReference<xType> &reference) {}
template<typename xType>
xReference<xType>& operator ==(const xReference<xType> &reference1,  const xReference<xType> &reference2) {}
template<typename xType>
xReference<xType>& operator =(const xReference<xType> &reference) {}
template<typename xType>
xReference<xType>& operator !=(const xReference<xType> &reference1, const xReference<xType> &reference2) {}
*/