package ie.cit.comp8058.bankdemo.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {
	public String _id;
	public String country;
	public AccountNumber accountNumber;
	public String currency;
	public String ownerName;
	public String accountType;
	public BigDecimal availableBalance;
	public BigDecimal bookedBalance;
	public Bank bank;
	public String status;
	public BigDecimal creditLimit;
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
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
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
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
	public BigDecimal getBookedBalance() {
		return bookedBalance;
	}
	public void setBookedBalance(BigDecimal bookedBalance) {
		this.bookedBalance = bookedBalance;
	}
	public Bank getBank() {
		return bank;
	}
	public void setBank(Bank bank) {
		this.bank = bank;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public BigDecimal getCreditLimit() {
		return creditLimit;
	}
	public void setCreditLimit(BigDecimal creditLimit) {
		this.creditLimit = creditLimit;
	}
	@Override
	public String toString() {
		return "Account [_id=" + _id + ", country=" + country + ", accountNumber=" + accountNumber + ", currency="
				+ currency + ", ownerName=" + ownerName + ", accountType=" + accountType + ", availableBalance="
				+ availableBalance + ", bookedBalance=" + bookedBalance + ", bank=" + bank + ", status=" + status + ", creditLimit=" + creditLimit + "]";
	}
	
	
	
	
	
	
}
