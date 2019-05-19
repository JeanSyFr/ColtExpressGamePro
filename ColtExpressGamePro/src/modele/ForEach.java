package modele;
import java.lang.Boolean;

class ForEach<T> {
	public static <T> void forEach(Iterable<T> c, FProvider<T> f) {
		Boolean b = new Boolean(true);
		for(T e: c) f.f(e);
	}
	
	interface FProvider <T>{
		void f (T elt);
	}
}



class ForEachObject<T,Out> {
	public static <T,Out> void forEach(Iterable<T> c, FObjectProvider<T,Out> f) {
		for(T e: c) f.f(e);
	}
	
	interface FObjectProvider <T,Out>{
		Out f (T elt);
	}	
}

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
