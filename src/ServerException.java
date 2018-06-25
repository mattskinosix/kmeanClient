/**
 * Classe che indica un eccezione sollevata quando non si riesce ad effettuare
 * una connessione al server.
 * 
 * @author mirko
 *
 */
public class ServerException extends Exception {

	private static final long serialVersionUID = 1L;

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
