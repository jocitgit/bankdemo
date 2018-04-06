package ie.cit.comp8058.bankdemo.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CreditDebitChartData {

	private List<String> XValues;
	private List<BigDecimal> creditValues;
	private List<BigDecimal> debitValues;
	private String currency;
	
	
	public CreditDebitChartData() {
		XValues = new ArrayList<String>();
		creditValues = new ArrayList<BigDecimal>();
		debitValues = new ArrayList<BigDecimal>();
		currency="";
	}


	public List<String> getXValues() {
		return XValues;
	}


	public void setXValues(List<String> xValues) {
		XValues = xValues;
	}


	public List<BigDecimal> getCreditValues() {
		return creditValues;
	}


	public void setCreditValues(List<BigDecimal> creditValues) {
		this.creditValues = creditValues;
	}


	public List<BigDecimal> getDebitValues() {
		return debitValues;
	}


	public void setDebitValues(List<BigDecimal> debitValues) {
		this.debitValues = debitValues;
	}
		
	public String getCurrency() {
		return currency;
	}


	public void setCurrency(String currency) {
		this.currency = currency;
	}


	public void addXValue(String x) {
		XValues.add(x);
	}
	
	public void addCreditValue(BigDecimal amount) {
		creditValues.add(amount);
	}
	
	public void addDebitValue(BigDecimal amount) {
		debitValues.add(amount);
	}


	@Override
	public String toString() {
		return "CreditDebitChartData [XValues=" + XValues + ", creditValues=" + creditValues + ", debitValues="
				+ debitValues + ", currency=" + currency + "]";
	}


	
	
}
