package ie.cit.comp8058.bankdemo.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {
	public String _id;
	public AccountNumber accountNumber;
	public String currency;
	public String accountType;
	public BigDecimal availableBalance;
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public AccountNumber getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(AccountNumber accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public BigDecimal getAvailableBalance() {
		return availableBalance;
	}
	public void setAvailableBalance(BigDecimal availableBalance) {
		this.availableBalance = availableBalance;
	}
	@Override
	public String toString() {
		return "Account [_id=" + _id + ", accountNumber=" + accountNumber + ", currency=" + currency + ", accountType="
				+ accountType + ", availableBalance=" + availableBalance + "]";
	}
	
	
	
	
	
}
