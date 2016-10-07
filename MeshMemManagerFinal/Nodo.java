package com.example.ronny.meshmemmanager.EstructurasDatos;

/**
 * Clsse que representa el nodo de la lista simple
 * @param <E> tipo de dato a recibir
 */
public class Nodo<E> {
	private E info;
	private Nodo<E> siguiente;
	private E id;
	private int referecias;

	/**
	 * Constructor
	 * @param info informacion a guardar
	 * @param siguiente apuntador al siguiente
     */
	public Nodo(E info, Nodo<E> siguiente ){
		this.info=info;
		this.siguiente=siguiente;
		this.referecias=0;
	}

	/**
	 * Constructor
	 * @param info informacion a guardar
	 * @param siguiente apuntador al siguiente
	 * @param pId id del nodo
     */
	public Nodo(E info, Nodo<E> siguiente,E pId ){
		this.info=info;
		this.siguiente=siguiente;
		this.id=pId;
		this.referecias=-1;
	}

	//GETS Y SETS
	public E getInfo() {return info;}
	public void setInfo(E info) {this.info = info;}
	public Nodo<E> getSiguiente() {return siguiente;}
	public void setSiguiente(Nodo<E> siguiente) {this.siguiente = siguiente;}
	public E getId() {return id;}
	public void setId(E id) {this.id = id;}
	public int getReferecias(){return referecias;}
	public void setReferecias(int referecias){this.referecias += referecias;}
}