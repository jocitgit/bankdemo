package ie.cit.comp8058.bankdemo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountNumber {

	private String value;
	private String _type;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String get_type() {
		return _type;
	}
	public void set_type(String _type) {
		this._type = _type;
	}
	
	@Override
	public String toString() {
		return "AccountNumber [value=" + value + ", _type=" + _type + "]";
	}
	
	
	
	
}
