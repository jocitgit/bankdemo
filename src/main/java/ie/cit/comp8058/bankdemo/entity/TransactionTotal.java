package ie.cit.comp8058.bankdemo.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionTotal {

	private LocalDate fromDate;
	private LocalDate toDate;
	private BigDecimal totalCredit;
	private BigDecimal totalDebit;
	private BigDecimal total;
	
	public TransactionTotal() {
		totalCredit = BigDecimal.ZERO;
		totalDebit = BigDecimal.ZERO;
		total = BigDecimal.ZERO;
		
	}
	
	public LocalDate getFromDate() {
		return fromDate;
	}
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}
	public LocalDate getToDate() {
		return toDate;
	}
	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}
	public BigDecimal getTotalCredit() {
		return totalCredit;
	}
	public void setTotalCredit(BigDecimal totalCredit) {
		this.totalCredit = totalCredit;
	}
	public BigDecimal getTotalDebit() {
		return totalDebit;
	}
	public void setTotalDebit(BigDecimal totalDebit) {
		this.totalDebit = totalDebit;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	public void addTxnAmount(BigDecimal amount) {
		if (amount.compareTo(BigDecimal.ZERO) < 0) {
			setTotalDebit(getTotalDebit().add(amount));
		} else {
			setTotalCredit(getTotalCredit().add(amount));
		}
		setTotal(getTotal().add(amount));
	}
	
	@Override
	public String toString() {
		return "TransactionTotal [fromDate=" + fromDate + ", toDate=" + toDate + ", totalCredit=" + totalCredit
				+ ", totalDebit=" + totalDebit + ", total=" + total + "]";
	}
	
	
}
