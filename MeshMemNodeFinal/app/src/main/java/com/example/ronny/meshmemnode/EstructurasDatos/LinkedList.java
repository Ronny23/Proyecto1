package com.example.ronny.meshmemnode.EstructurasDatos;

 import java.util.Iterator;

/**
     ** MATERIAL PURAMENTE EDUCATIVO **<br>
     * -------------------------------<br>
     * Implementacion de un lista doblemente ligada.<br>
     * Permite:<br>
     *  - Insertar elementos con complejidad constante.<br>
     *  - Eliminar elementos con complejidad constante.<br>
     *  - Acceso aleatorio a los elementos de la lista con complejidad O(n/2).<br>
     *  - Iterar la lista con la logica de iteradores de Java.<br>
     * Puede ser utilizada como pila o cola.
     * @author chrishendrix
     * @param <Type> Tipo de dato de los elementos que componen la lista.
     */
    public class LinkedList<Type> implements Iterable<Type> {
        private Node<Type> first;
        private Node<Type> last;
        private int length=0;

    /**
         * Constructor vacio:<br>
         *  - Crea una lista sin elementos
         */
        public LinkedList() {}




        /**
         * Agrega un nuevo elemento al final de la lista.
         * Complejidad: O(1)
         * @param value Valor del nuevo elemento
         */
        public void add(Type value, String pId) {
            Node<Type> newNode = new Node<>(value,pId);
            if (length == 0) {
                first = newNode;
                last = newNode;
            } else {
                newNode.previous = last;
                last.next = newNode;
                last = newNode;
            }
            length++;
        }



        /**
         * Elimina el �ltimo elemento de la lista.<br>
         *  - Complejidad: O(1)
         * @return true si hay por lo menos un elemento que eliminar, false en caso contrario.
         * @see #popFirst()
         */
        public boolean pop() {
            if (length > 0) {
                if (length == 1) {
                    first = null;
                    last = null;
                } else {
                    last.previous.next = null;
                    last = last.previous;
                }
                length--;
                return true;
            }
            return false;
        }

        /**
         * Agrega un elemento a inicio de la lista.<br>
         *  - Complejidad: O(1)
         * @param value Valor del elemento a agregar.
         */
        public void pushFirst(Type value, String pId) {
            Node<Type> newNode = new Node<>(value, pId);
            if (length == 0) {
                first = newNode;
                last = newNode;
            } else {
                newNode.next = first;
                first.previous = newNode;
                first = newNode;
            }
            length++;
        }





        /**
         * Elimina el primer elemento de la lista.<br>
         *  - Complejidad: O(1)
         * @return true si hay por lo menos un elemento que eliminar, false en caso contrario.
         * @see #pop()
         */
        public boolean popFirst() {
            if (length > 0) {
                if (length == 1) {
                    first = null;
                    last = null;
                } else {
                    first.next.previous = null;
                    first = first.next;
                }
                length--;
                return true;
            }
            return false;
        }

        /**
         * Crea un nuevo nodo y lo inserta en una posicion en especifico.<br>
         *  - Complejidad: f(n/2) = O(n)
         * @param index Posicion del nodo a insertar.
         * @param value Valor del nodo.
         * @throws IndexOutOfBoundsException En caso de que el index no coincida con ningun elemento.
         */
        public void insert(int index, Type value, String pId) throws IndexOutOfBoundsException {
            if (index == 0) {
                pushFirst(value, pId);
            } else if (index == length - 1) {
                add(value, pId);
            } else {
                Node<Type> nodeInPos = getNode(index);
                Node<Type> newNode = new Node<>(value, nodeInPos.previous, nodeInPos);
                nodeInPos.previous.next = newNode;
                nodeInPos.previous = newNode;
                length++;
            }
        }

        /**
         * Sobre-escribe un elemento de la lista.
         * @param Id Posicion del elemento a sobre-escribir.
         * @param value Nuevo valor del elemento.
         * @see #get(int)
         * @throws IndexOutOfBoundsException En caso de que el index no coincida con ningun elemento.
         */
        public int set(String Id, Type value, int cant) throws IndexOutOfBoundsException {
            int iniciostr = 0;
            int finalstr = 4;
            Node tmp,tmp2;
            tmp = first;
            int pos = 0;
            if (value.equals("") && Id.equals("")) {
                while (tmp != null) {
                    if (tmp.get_id().equals(Id)) {
                        getNode(pos).setValue(value);
                        getNode(pos).set_id("");
                    }
                    pos++;
                    tmp = tmp.next;
                }
                return pos-1;
            }
            else{
                int igual=1;
                while (tmp != null) {
                    if(tmp.get_id().equals("")){
                        tmp2 = tmp;
                        while (tmp2!=null && (tmp2.get_id().equals(""))){
                            if(igual==cant){
                                for (int i=0; i<cant; i++){
                                    if (value.equals("")){}
                                    else {
                                        getNode(pos).setValue((Type) (((String) value).substring(iniciostr, finalstr)));
                                    }getNode(pos).set_id(Id);
                                    iniciostr+=4;
                                    finalstr+=4;
                                    tmp = tmp.next;
                                    pos++;
                                }
                                return pos-1;
                            }
                            igual++;
                            tmp2 = tmp2.next;
                        }
                        tmp = tmp2;
                    }
                    assert tmp != null;
                    tmp = tmp.next;
                    pos+=igual;
                    igual=1;
                }
            }
            return -1;
        }

        /**
         * Obtiene un elemento de la lista.
         * @param index Posicion del elemento a obtener.
         * @return El valor del elemento.
         * @see #get(int)
         * @throws IndexOutOfBoundsException En caso de que el index no coincida con ningun elemento.
         */
        public Type get(int index) throws IndexOutOfBoundsException {
            return getNode(index).value;
        }

        /**
         * Informa si un elemento es parte de la lista mediante el metodo equals.
         * @param value Valor a saber si se encuentra en la lista.
         * @return True en caso de que se encuentre el valor, false en caso contrario.
         */
        public boolean contains(Type value) {
            for (Type v : this) {
                if (v.equals(value)) {
                    return true;
                }
            }
            return false;
        }

       public String getValor(String valor) {
           Node tmp ;
           String datos = "";
           tmp = first;
           while(tmp!=null) {
               if (tmp.get_id().equals(valor)) {
                   datos = datos + tmp.getValue();
               }
               tmp = tmp.next;
           }
           return datos;
       }
        /**
         * Elimina todos los elementos de la lista, dejandola vacia.<br>
         *  - Complejidad: O(n)
         * @see #pop()
         */
        public void clear() {
            while (pop());
        }

        /**
         * Informa la longitud de la lista.
         * @return Longitud de la lista.
         */
        public int size() {
            return length;
        }

        @Override
        public Iterator<Type> iterator() {
            // TODO Auto-generated method stub
            return null;
        }

        /**
         * Informa si la lista se encuentra vacia.
         * @return True en caso de que la lista este vacia, false en caso contrario.
         */
        public boolean isEmpty() {
            return length == 0;
        }

        /**
         * Convierte cada uno de los valores en la lista a su representaci�n en String.<br>
         *  - Complejidad: O(n)
         * @return String que representa a la lista.
         */
        @Override
        public String toString() {
            String str = "";
            for(Node<Type> node = first; node != null; node = node.next) {
                str += node.value.toString() + " ";
            }
            return str;
        }






        /**
         * Obtiene una referencia a un nodo existente en la lista.<br>
         *  - Complejidad: f(n/2) = O(n)
         * @param pos La posicion/index del nodo.
         * @return Una referencia a nu nodo.
         * @throws IndexOutOfBoundsException En caso de que el index este fuera de los limites de la lista.
         */
        public Node<Type> getNode(int pos) throws IndexOutOfBoundsException {
            if (length == 0 || pos < 0 || pos >= length) {
                throw new IndexOutOfBoundsException("Te has salido de los limites de la lista");
            }
            int middle = length / 2;
            Node<Type> nodeToReturn;
            if (pos <= middle) {
                nodeToReturn = first;
                for (int i = 0; i < pos; i++) {
                    nodeToReturn = nodeToReturn.next;
                }
            } else {
                nodeToReturn = last;
                for (int i = length - 1; i > pos; i--) {
                    nodeToReturn = nodeToReturn.previous;
                }
            }
            return nodeToReturn;
        }


    }


