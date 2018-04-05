package ie.cit.comp8058.bankdemo.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BalanceChartData {

	private List<LocalDate> XValues;
	private List<BigDecimal> YValues;
	
	public BalanceChartData() {
		XValues = new ArrayList<LocalDate>();
		YValues = new ArrayList<BigDecimal>();
	}
	
	public List<LocalDate> getXValues() {
		return XValues;
	}
	public void setXValues(List<LocalDate> XValues) {
		this.XValues = XValues;
	}
	public List<BigDecimal> getYValues() {
		return YValues;
	}
	public void setYValues(List<BigDecimal> YValues) {
		this.YValues = YValues;
	}
	
	public void addXValue(LocalDate date) {
		XValues.add(date);
	}
	
	public void addYValue(BigDecimal amount) {
		YValues.add(amount);
	}
	
	@Override
	public String toString() {
		return "BalanceChartData [XValues=" + XValues + ", YValues=" + YValues + "]";
	}
	
	
}
