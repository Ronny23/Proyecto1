#include <fcntl.h>
#include <string.h>
#include <errno.h>
#include <stdio.h>
#include <iostream>
#include <netinet/in.h>
#include <arpa/inet.h>
#include "JsonParser.cpp"
#include "xReference.cpp"

using namespace std;

string token;
int server;
Arreglo<string> datos;
typedef enum xType {INT, CHAR, FLOAT};

string enviarRecibir(string str){
    int bytecount;
    char buffer[1024];
    if( (bytecount=send(server,str.c_str() , str.length(),0))== -1){
        fprintf(stderr, "Error sending data %d\n", errno);
    }
    if((bytecount = recv(server, buffer, 1024, 0))== -1){
        fprintf(stderr, "Error receiving data %d\n", errno);
    }
    printf("Recieved bytes %d\nReceived string \"%s\"\n", bytecount, buffer);
    string respuesta = string(buffer);
    return respuesta;
}

int inicialize(char* ip, int   puerto){
    struct sockaddr_in socketConexion;
    int estado;
    int err;
    int bytecount;
    char buffer[1024];
    datos = * new Arreglo<string>();
    string str= parsear(1,datos);

    estado = socket(AF_INET, SOCK_STREAM, 0);
    if(estado == -1){
        printf("Error initializing socket %d\n",errno);
        return -1;
    }

    socketConexion.sin_family = AF_INET ;
    socketConexion.sin_port = htons(puerto);

    memset(&(socketConexion.sin_zero), 0, 8);
    socketConexion.sin_addr.s_addr = inet_addr(ip);
    if( connect( estado, (struct sockaddr*)&socketConexion, sizeof(socketConexion)) == -1 ){
        if((err = errno) != EINPROGRESS){
            fprintf(stderr, "Error connecting socket %d\n", errno);
            return -1;
        }
    }
    server = estado;
    token = interpretar(enviarRecibir(str)).c_str();
    return estado;
}

template <typename T>
xReference<T>* xMalloc(int size, T type){
    datos = * new Arreglo<string>();
    datos.insert(to_string(size));
    string str = parsear(2,datos);
    enviarRecibir(str);
    xReference<T>* thing = new xReference<T>(str, size, type);
    return thing;
}

/*
template <typename xType>
void xAssign(xReference<xType> reference,void* value){}
template <typename xType>
void xFree(xReference<xType> toFree){}
*/

int main(int argv, char** argc) {
    inicialize("192.168.100.13", 8080);
    cout<<token;
    xReference<xType>* r = xMalloc(8,INT);
    xReference<xType>* f = xMalloc(12,INT);
    while(true){}
}
