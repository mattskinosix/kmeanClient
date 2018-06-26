/**
 * Classe che indica un eccezione sollevata quando non si riesce ad effettuare
 * una connessione al server.
 * 
 * @author mirko
 *
 */
@SuppressWarnings("serial")
public class ServerException extends Exception {

	/**
	 * Costruttore che utilizza il costruttore della superclasse passando come
	 * parametro una stringa.
	 * 
	 * @param result
	 */
	ServerException(String result) {
		super(result);
	}

}
