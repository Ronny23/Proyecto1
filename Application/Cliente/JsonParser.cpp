#include <jsoncpp/value.h>
#include <jsoncpp/json.h>


using namespace std;


string solicitarToken(){
    Json::Value jsonAenviar;
    jsonAenviar["Actividad"] = "solicitarToken";
    Json::FastWriter fastWriter;
    return fastWriter.write(jsonAenviar);
}

string parsear(int solicitud){
    if (solicitud==1){
        return solicitarToken();
    }
}

