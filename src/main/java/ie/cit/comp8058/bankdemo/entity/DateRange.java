package ie.cit.comp8058.bankdemo.entity;

import java.util.Date;

public class DateRange {

	private Date fromDate;
	private Date toDate;
	
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	@Override
	public String toString() {
		return "DateRange [fromDate=" + fromDate + ", toDate=" + toDate + "]";
	}
	
	
	
	
}
