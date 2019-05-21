package modele;
import java.lang.Boolean;


/** Passer une méthode en paramètre. 
 * On cherche à définir une méthode [forEach] attendant deux paramètres : 
 * - une collection d'objets de type [T], implémentant [Iterable<T>] 
 * - une méthode [void f(T elt)] à appliquer à chaque élément de la collection 
 * 
 * 
 * Comme on ne peut pas passer de méthode en paramètre à une autre méthode, 
 * on peut à la place utiliser un objet possédant la méthode [f] souhaitée.
 * 
 *  Nous définissons ci-dessous une interface fonctionelle [FProvider<T>] qui caractérise les classes fournissant une méthode [f] avec la bonne signature. 
 *  */

/**
 * 
 * @author arbache
 *
 * @param <T>
 */
class ForEach<T> {
	public static <T> void forEach(Iterable<T> c, FProvider<T> f) {
		Boolean b = new Boolean(true);
		for(T e: c) f.f(e);
	}
	
	interface FProvider <T>{
		void f (T elt);
	}
}


/**
 * This is used when the itarable has elemnts of type  T
 * and the function of FProvider return object of type Out
 * 
 * 
 * @author arbache
 *
 * @param <T> the type of the elements of Itarable Object
 * @param <Out> the type of returned Object if the function 'f' of FObjectProvider
 */

class ForEachObject<T,Out> {
	public static <T,Out> void forEach(Iterable<T> c, FObjectProvider<T,Out> f) {
		for(T e: c) f.f(e);
	}
	
	interface FObjectProvider <T,Out>{
		Out f (T elt);
	}	
}



/**
 * 
 * @author arbache
 *
 * @param <T>
 * 
 * 
 * @see ForEachObject, here Object= Boolean (boolean)
 */
class ForEachPredicat<T>{
	public static <T> boolean forEach(Iterable<T> c, FObjectProvider<T> f) {
		Boolean out= new Boolean(true);
		for(T e: c) { out = Boolean.TRUE && f.f(e);}
		return out;
	}
	
	interface FObjectProvider <T>{
		boolean f (T elt);
	}	
}
