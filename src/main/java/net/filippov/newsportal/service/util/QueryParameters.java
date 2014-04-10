package net.filippov.newsportal.service.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Class builds map of parameters for a named query
 * 
 * @author Oleg Filippov
 */
public class QueryParameters {

	private Map<String, Object> parameters;

	private QueryParameters(String name, Object value) {
		parameters = new HashMap<String, Object>();
		parameters.put(name, value);
	}
	
	public static QueryParameters setParam(String name, Object value) {
		return new QueryParameters(name, value);
	}
	
	public QueryParameters add(String name, Object value) {
		parameters.put(name, value);
		return this;
	}
	
	public Map<String, Object> buildMap() {
		return parameters;
	}
}
