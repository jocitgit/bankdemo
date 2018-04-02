package ie.cit.comp8058.bankdemo.entity;

import java.util.List;

public class TransactionPage {

	private List<Transaction> transactions;
	private String continuationKey;
		
	public List<Transaction> getTransactions() {
		return transactions;
	}
	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
	public String getContinuationKey() {
		return continuationKey;
	}
	public void setContinuationKey(String continuationKey) {
		this.continuationKey = continuationKey;
	}
		
	@Override
	public String toString() {
		return "TransactionPage [transactions=" + transactions + ", continuationKey=" + continuationKey + "]";
	}
	
	
}
