package com.example.ronny.meshmemnode.EstructurasDatos;

    /**
     * Representa un nodo de una lista doblemente ligada
     * @param <Type> Tipo de dato del valor que almacena el nodo
     */
    public class Node<Type> {
        Type value;
        String _id;
        Node<Type> previous;
        Node<Type> next;

        /**
         * Constructor a partir de un valor.
         * @param value Valor que contendr� el nodo.
         * @see #Node(Object, Node, Node)
         */
        Node(Type value, String id) {
            _id = id;
            this.value = value;
        }

        /**
         * Constructor a partir de un valor y posiciones "anterior" y "siguiente" conocidas.
         * @param value Valor que contendra el nodo.
         * @param previous Liga a un nodo previo.
         * @param next Liga a un nodo siguiente.
         */
        Node(Type value, Node<Type> previous, Node<Type> next) {
            this.value = value;
            this.previous = previous;
            this.next = next;
        }
        /* ----------------------GETS Y SETS ---------------*/
        public String get_id() {return _id;}
        public Type getValue() {return value;}
        public void set_id(String _id) {this._id = _id;}
        public void setValue(Type value) {this.value = value;}
    }