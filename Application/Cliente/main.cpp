#include <fcntl.h>
#include <string.h>
#include <errno.h>
#include <stdio.h>
#include <iostream>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <jsoncpp/value.h>
#include <jsoncpp/json.h>
#include "JsonParser.cpp"


using namespace std;

int inicialize(char* ip, int   puerto){


    struct sockaddr_in socketConexion;
    int estado;
    int err;
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
    return estado;
}

int main(int argv, char** argc) {

    int server = inicialize("192.168.100.17", 8080);
    int bytecount;
    char buffer[1024];


    string str= parsear(1);

    if( (bytecount=send(server,str.c_str() , str.length()+1,0))== -1){
        fprintf(stderr, "Error sending data %d\n", errno);
    }
    if((bytecount = recv(server, buffer, sizeof(buffer), 0))== -1){
      fprintf(stderr, "Error receiving data %d\n", errno);
    }
    cout<<buffer;
    while(true){}

}