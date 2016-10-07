package com.example.ronny.meshmemmanager.EstructurasDatos;

/**
 * clase para crear listas genericas utilizada para almacenar la informacion necesaria
 */
public class Lista<E> {
	//punteros para sasber donde esta el inicio y el final
	protected Nodo<E> primero;
	protected Nodo<E> ultimo;
	int cant;// cantidad de elementos en la lista

	/**
	 * Constructor
	 */
	public Lista(){
		primero=null;
		ultimo=null;
		this.cant=0;
	}

	/**
	 * Retorna la cantidad de elementos de la lista
	 * @return cantidad de elementos de la lista
     */
	public int getCant() {
		return cant;
	}

	/**
	 * Metodo que verifica si la lista NO contiene informacion es decir si esta vacia
	 * @return true en la caso de estar vacia y false en caso contrario
	 */
	public boolean estaVacia(){
		return (cant==0);
		
	}
	
	/**
	 * metodo para agregar un nodo al inicio de la lista
	 */
	public void insertarInicio(E info){
		primero = new Nodo<>(info, primero);
		if (estaVacia()){
			ultimo=primero;
		}
		cant++;	
	}

	/**
	 * metodo para agregar un nodo al inicio de la lista
	 */
	public void insertarFinal(E info){
		Nodo<E> nuevoNodo= new Nodo<>(info, null);
		if(primero!=null){
			Nodo<E> tmp = primero;
			while (tmp.getSiguiente()!=null){
				tmp=tmp.getSiguiente();
			}
			tmp.setSiguiente(nuevoNodo);
		}else{
			primero=nuevoNodo;
		}
		cant++;
	}

	/**
	 * metodo para agregar un nodo al inicio de la lista
	 */
	public void insertarFinal(E info,E id){
		Nodo<E> nuevoNodo= new Nodo<>(info, null,id);
		if(primero!=null){
			Nodo<E> tmp = primero;
			while (tmp.getSiguiente()!=null){
				tmp=tmp.getSiguiente();
			}
			tmp.setSiguiente(nuevoNodo);
		}else{
			primero=nuevoNodo;
		}
		cant++;
	}

	/**
	 * Metodo para obtener un elemento en una dada posicion
	 * @param p: indice o posicion del elemento a buscar en la lista
	 * @return el elemento en busqueda
	 */
	public E obtenerElemento(int p){
		int contador=0;
		Nodo<E> temporal = primero;
		while(contador<p){
			temporal=temporal.getSiguiente();
			contador++;
		}
		return temporal.getInfo();
	}

	public Nodo<E> Elemento(int p){
		int contador=0;
		Nodo<E> temporal = primero;
		while(contador<p){
			temporal=temporal.getSiguiente();
			contador++;
		}
		return temporal;
	}

	/**
	 * Metodo para eleminar un elemento en una dada posicion
	 * @param p: indice o posicion del elemento a eliminar en la lista
	 */
	public void eliminarElemento (int p){
		if (p==0){
			primero=primero.getSiguiente();
		}
		else{
			int contador=0;
			Nodo<E> temporal = primero;
			while(contador<p-1){
				temporal=temporal.getSiguiente();
				contador++;
			}
			temporal.setSiguiente(temporal.getSiguiente().getSiguiente());
		}
		cant--;
	}
	
	/**
	 * Metodo para eliminar el primer elemento de la lista
	 */
	public E eliminarPrimero(){
		if (estaVacia()){
			return null;
		}
		E info=primero.getInfo();
		primero=primero.getSiguiente();
		cant--;
		return info;
		
	}
	
	/**
	 * Metodo para eliminar el primer elemento de la lista
	 */
	public void eliminarUltimo(){
		E elemento=ultimo.getInfo();
	}
}
