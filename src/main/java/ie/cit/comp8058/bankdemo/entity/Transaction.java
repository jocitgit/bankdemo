package ie.cit.comp8058.bankdemo.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {

	private String transactionId;
	private String _type; // CreditTransaction or DebitTransaction
	private BigDecimal amount;
	private String bookingDate;
	private String currency;
	private String message;
	private String typeDescription; // e.g. ATM withdrawal
	private String debtorName;
	private String creditorName;
	
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String get_type() {
		return _type;
	}
	public void set_type(String _type) {
		this._type = _type;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getBookingDate() {
		return bookingDate;
	}
	public void setBookingDate(String bookingDate) {
		this.bookingDate = bookingDate;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTypeDescription() {
		return typeDescription;
	}
	public void setTypeDescription(String typeDescription) {
		this.typeDescription = typeDescription;
	}
	public String getDebtorName() {
		return debtorName;
	}
	public void setDebtorName(String debtorName) {
		this.debtorName = debtorName;
	}
	public String getCreditorName() {
		return creditorName;
	}
	public void setCreditorName(String creditorName) {
		this.creditorName = creditorName;
	}
	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", _type=" + _type + ", amount=" + amount
				+ ", bookingDate=" + bookingDate + ", currency=" + currency + ", message=" + message
				+ ", typeDescription=" + typeDescription + ", debtorName=" + debtorName + ", creditorName="
				+ creditorName + "]";
	}
	
	
	
	
}
