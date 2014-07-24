package net.filippov.newsportal.service.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Class builds map of parameters for a named query
 * 
 * @author Oleg Filippov
 */
public class QueryParameters {

	/**
	 * Map of parameters
	 */
	private Map<String, Object> parameters;

	/**
	 * Private constructor
	 */
	private QueryParameters(String name, Object value) {
		parameters = new HashMap<String, Object>();
		parameters.put(name, value);
	}
	
	/**
	 * Set parameter
	 * 
	 * @param name of parameter
	 * @param value of parameter
	 * @return new QueryParameters object
	 */
	public static QueryParameters setParam(String name, Object value) {
		return new QueryParameters(name, value);
	}
	
	/**
	 * Add one more parameter
	 * 
	 * @param name of parameter
	 * @param value of parameter
	 * @return this object
	 */
	public QueryParameters add(String name, Object value) {
		parameters.put(name, value);
		return this;
	}
	
	/**
	 * Build Map with parameters. Must be envoked at the end
	 * 
	 * @return result Map
	 */
	public Map<String, Object> buildMap() {
		return parameters;
	}
}
