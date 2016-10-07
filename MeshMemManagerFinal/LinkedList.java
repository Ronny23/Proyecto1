package com.example.ronny.meshmemmanager.EstructurasDatos;

//importaciones
import java.util.Iterator;

/**
 * Implementacion de un lista doblemente ligada
 * @param <Type> Tipo de dato de los elementos que componen la lista.
 */
public class LinkedList<Type> implements Iterable<Type> {
    private Node<Type> first;
    private Node<Type> last;
    private int length;

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
    public void add(Type value) {
        Node<Type> newNode = new Node<>(value);
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
    public void pushFirst(Type value) {
        Node<Type> newNode = new Node<>(value);
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
    public void insert(int index, Type value) throws IndexOutOfBoundsException {
        if (index == 0) {
            pushFirst(value);
        } else if (index == length - 1) {
            add(value);
        } else {
            Node<Type> nodeInPos = getNode(index);
            Node<Type> newNode = new Node<>(value, nodeInPos.previous, nodeInPos);
            nodeInPos.previous.next = newNode;
            nodeInPos.previous = newNode;
            length++;
        }
    }

    /**
     * Elimina un elemento de la lista.
     * @param index Posicion del elemento a eliminar.
     * @throws IndexOutOfBoundsException En caso de que el index no coincida con ningun elemento.
     */
    public void delete(int index) throws IndexOutOfBoundsException {
        if (length == 0)
            throw new IndexOutOfBoundsException("Te has salido de los limites de la lista");
        if (index == 0) {
            popFirst();
        } else if (index == length - 1) {
            pop();
        } else {
            Node<Type> nodeToDelete = getNode(index);
            nodeToDelete.previous.next = nodeToDelete.next;
            nodeToDelete.next.previous = nodeToDelete.previous;
            length--;
        }
    }

    /**
     * Sobre-escribe un elemento de la lista.
     * @param index Posicion del elemento a sobre-escribir.
     * @param value Nuevo valor del elemento.
     * @see #get(int)
     * @throws IndexOutOfBoundsException En caso de que el index no coincida con ningun elemento.
     */
    public void set(int index, Type value) throws IndexOutOfBoundsException {
        getNode(index).value = value;
    }

    /**
     * Obtiene un elemento de la lista.
     * @param index Posicion del elemento a obtener.
     * @return El valor del elemento.
     * @see #set(int, Object)
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
    private Node<Type> getNode(int pos) throws IndexOutOfBoundsException {
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