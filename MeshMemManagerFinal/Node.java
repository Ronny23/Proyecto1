package com.example.ronny.meshmemmanager.EstructurasDatos;

/**
 * Representa un nodo de una lista doblemente ligada.<br>
 * @param <Type> Tipo de dato del valor que almacena el nodo
 */
public class Node<Type> {
    Type value;
    Node<Type> previous;
    Node<Type> next;

    /**
     * Constructor a partir de un valor.
     * @param value Valor que contendr� el nodo.
     * @see #Node(Object, Node, Node)
     */
    Node(Type value) {
            this.value = value;
        }

    /**
     * Constructor a partir de un valor y posiciones "anterior" y "siguiente" conocidas.
     * @param value Valor que contendra el nodo.
     * @param previous Liga a un nodo previo.
     * @param next Liga a un nodo siguiente.
     * @see #Node(Object)
     */
    Node(Type value, Node<Type> previous, Node<Type> next) {
        this.value = value;
        this.previous = previous;
        this.next = next;
    }
}