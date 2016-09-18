#include <jsoncpp/value.h>
#include <jsoncpp/json.h>
#include "Arreglo.cpp"
#include "Arreglo.h"


using namespace std;

string solicitarToken(){
    Json::Value jsonAenviar;
    jsonAenviar["Actividad"] = "solicitarToken";
    Json::FastWriter fastWriter;
    return fastWriter.write(jsonAenviar);
}

string solicitudxMalloc(string size){
    Json::Value jsonAenviar;
    jsonAenviar["Actividad"] = "xMalloc";
    jsonAenviar["size"] = size;
    Json::FastWriter fastWriter;
    return fastWriter.write(jsonAenviar);
}

string solicitudxMalloc2(string size,string tipo,string valor){
    Json::Value jsonAenviar;
    jsonAenviar["Actividad"] = "xMalloc2";
    jsonAenviar["size"] = size;
    jsonAenviar["type"] = tipo;
    jsonAenviar["value"] = valor;
    Json::FastWriter fastWriter;
    return fastWriter.write(jsonAenviar);
}

string interpretar(string pStr){
    Json::Value root;
    Json::Reader reader;
    bool parsingSuccessful = reader.parse( pStr, root );
    string str = (root.get("Actividad", "A Default Value if not exists" ).asString()).c_str();
    if (str=="token") {
        return (root.get("token", "A Default Value if not exists" ).asString()).c_str();
    }
    return "";
}

string parsear(int solicitud, Arreglo<string> datos){
    if (solicitud==1){
        return solicitarToken();
    }
    else if (solicitud==2){
        return solicitudxMalloc(datos.get_data(0));
    }
    else if(solicitud==3){
        return solicitudxMalloc2(datos.get_data(0),datos.get_data(1),datos.get_data(2));
    }
}