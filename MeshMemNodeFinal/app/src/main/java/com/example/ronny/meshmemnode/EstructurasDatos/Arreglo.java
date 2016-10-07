package com.example.ronny.meshmemnode.EstructurasDatos;

/**
 * Clase para crear arreglos(Genericos)
 */

public class Arreglo<tipo>{

	public int elementos; // Cantidad de elementos del arreglo
	private int tamano;// Tamaño del arreglo
	private tipo [] arreglo;//tipo de objeto que forma el arreglo
	
	/**
	 * Constructor
	 */
	@SuppressWarnings("unchecked")
	public Arreglo() {
		super();
		this.elementos = 0;
		this.tamano = 5;
		arreglo = (tipo[]) new Object[5];
	}

	/**
	 * Metodo para agregar un dato al final del arreglo
	 * @param hilo objeto de tipo ServidorHilo que se agrega al arreglo
	 */
	public void add(tipo hilo){
		if (elementos>=tamano){
			this.resize();
		}
		arreglo[elementos]=hilo;
		elementos++;
	}

	/**
	 * Metodo para hacer mas grande el arreglo en el doble de la capacidad
	 */
	public void resize(){
		int nuevotamano = this.tamano*2;
		@SuppressWarnings("unchecked")
		tipo [] arregloNuevo = (tipo[])new Object[nuevotamano];
		System.arraycopy(arreglo, 0, arregloNuevo, 0, tamano);
		this.tamano = nuevotamano;
		arreglo=arregloNuevo;
	}

	/**
	 * Retorna el elemento en la posicion indicada
	 * @param i numero de elemento en el arreglo
	 * @return elemento del arreglo en la posicion i
	 */
	public tipo elemento(int i){
		return arreglo[i];
	}
	
	/**
	 * Retorna el numero de elementos del arreglo 
	 * @return numero de elementos del arreglo
	*/
	public int getNelementos(){
		return elementos;
	}
	
	/**
	 *Elimina un elemento del arreglo
	 *@param pos posicion en el arreglo del elemento a eliminar
	*/
	public void eliminar(int pos){
		System.arraycopy(arreglo, pos + 1, arreglo, pos, elementos - 1 - pos);
		arreglo[elementos-1]=null;
		elementos=elementos-1;
	}
}