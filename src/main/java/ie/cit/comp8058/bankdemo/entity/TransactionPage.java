package ie.cit.comp8058.bankdemo.entity;

import java.util.List;

public class TransactionPage {

	private List<Transaction> transactions;
	private String nextKey;
	private String previousKey;
		
	public List<Transaction> getTransactions() {
		return transactions;
	}
	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
	public String getNextKey() {
		return nextKey;
	}
	public void setNextKey(String nextKey) {
		this.nextKey = nextKey;
	}
	public String getPreviousKey() {
		return previousKey;
	}
	public void setPreviousKey(String previousKey) {
		this.previousKey = previousKey;
	}
	@Override
	public String toString() {
		return "TransactionPage [transactions=" + transactions + ", nextKey=" + nextKey
				+ ", previousKey=" + previousKey + "]";
	}
	
	
}
