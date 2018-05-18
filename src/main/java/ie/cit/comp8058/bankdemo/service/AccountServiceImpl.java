package ie.cit.comp8058.bankdemo.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import ie.cit.comp8058.bankdemo.dao.AccountDao;
import ie.cit.comp8058.bankdemo.entity.Account;
import ie.cit.comp8058.bankdemo.entity.BalanceChartData;
import ie.cit.comp8058.bankdemo.entity.CreditDebitChartData;
import ie.cit.comp8058.bankdemo.entity.Transaction;
import ie.cit.comp8058.bankdemo.entity.TransactionPage;
import ie.cit.comp8058.bankdemo.entity.TransactionTotal;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired AccountDao accountDao;

	@Override
	public Account[] getAllAccounts(String accessToken) {
		return accountDao.getAllAccounts(accessToken);
	}

	@Override
	public Account getAccountById(String accessToken, String id) {
		return accountDao.getAccountById(accessToken, id);
	}

	@Override
	public List<Transaction> getTransactionsByAccountId(String accessToken, String id) {
		return accountDao.getTransactionsByAccountId(accessToken, id);
	}

	@Override
	public List<Transaction> getTransactionsByAccountIdAndDate(String accessToken, String id, String fromDate,
			String toDate) {
		return accountDao.getTransactionsByAccountIdAndDate(accessToken, id, fromDate, toDate);
	}

	@Override
	public TransactionPage getTransactionPageByAccountId(String accessToken, String id, String continuationKey) {
		return accountDao.getTransactionPageByAccountId(accessToken, id, continuationKey);
	}

	@Override
	public TransactionPage getTransactionPageByAccountIdAndDate(String accessToken, String id, String fromDate,
			String toDate, String continuationKey) {
		return accountDao.getTransactionPageByAccountIdAndDate(accessToken, id, fromDate, toDate, continuationKey);
	}

	@Override
	public List<TransactionTotal> getTransactionTotals(String accessToken, String id, String fromDate, String toDate,
			String groupBy) {

		ArrayList<TransactionTotal> totals = new ArrayList<TransactionTotal>();
		List<Transaction> txns;
		
		LocalDate startDate;
		LocalDate nextDate;
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				
		if (fromDate==null || toDate==null) {
			return null;
		}
		
		// Retrieve the transactions for the date range
		txns = accountDao.getTransactionsByAccountIdAndDate(accessToken, id, fromDate, toDate);
		
		
		if (txns == null) {
			return null;
		}
		
		if (txns.isEmpty()) {
			return totals; //empty list
		}
		
		Collections.reverse(txns); // put txns in ascending date order
		
		// Identify the date range for the first grouping			
		startDate = LocalDate.parse(fromDate, formatter);				
		nextDate = getNextDate(startDate, groupBy);
		
		TransactionTotal txnTotal = new TransactionTotal();
		txnTotal.setFromDate(startDate);
		txnTotal.setToDate(nextDate.minusDays(1));
		ListIterator<Transaction> iterator = txns.listIterator();
		Transaction t;
		
		// Iterate through the transactions, grouping them according to date
		while(iterator.hasNext()) {
			t = iterator.next();
			if (LocalDate.parse(t.getBookingDate(), formatter).isBefore(nextDate)) {
				txnTotal.addTxnAmount(t.getAmount());	
			} else {
				totals.add(txnTotal);
				startDate = nextDate;
				nextDate = getNextDate(startDate, groupBy);
				txnTotal = new TransactionTotal();
				txnTotal.setFromDate(startDate);
				txnTotal.setToDate(nextDate.minusDays(1));
				iterator.previous(); // Go back and try this txn again
			}
		}
		
		if (txnTotal.getToDate().isAfter(LocalDate.parse(toDate, formatter))) {
			txnTotal.setToDate(LocalDate.parse(toDate, formatter)); // Maximum toDate
		}

		totals.add(txnTotal); // The final one after exiting the loop
		
		return totals;
		
	}
	
	// Helper function to identify date ranges according to groupBy parameter
	private LocalDate getNextDate(LocalDate currentDate, String groupBy) {
		switch (groupBy) {
		case "month":
			return currentDate.plusMonths(1);
		case "week":
			return currentDate.plusWeeks(1);
		case "day":
			return currentDate.plusDays(1);
		}
		return currentDate;
	}

	// Format transaction data for balance chart
	@Override
	public BalanceChartData getBalanceChartData(String accessToken, String id, String fromDate, String toDate) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy");
		
		// Get daily transaction totals
		List<TransactionTotal> dailyTotals = getTransactionTotals(accessToken, id, fromDate, toDate, "day");
		
		// Get current account balance
		Account account = accountDao.getAccountById(accessToken, id);

		if (dailyTotals==null || account==null) {
			return null;
		}
		
		BigDecimal currentBalance = account.getBookedBalance();
				
		BalanceChartData data = new BalanceChartData();
		data.setCurrency(account.getCurrency());
		
		BigDecimal adjustments = BigDecimal.ZERO;
		
		// Calculate the initial balance at the start of the time period
		for (TransactionTotal total: dailyTotals) {
			adjustments = adjustments.add(total.getTotal());			
		}
		
		BigDecimal balance = currentBalance.subtract(adjustments);
		
		// Work forward from the initial balance to calculate daily balances
		// and add them to the chart data object
		for (TransactionTotal total: dailyTotals) {
			data.addXValue(total.getToDate().format(formatter));
			balance = balance.add(total.getTotal());
			data.addYValue(balance);
		}
		
		return data;
		
	}

	// Format transaction data for credit/debit chart
	@Override
	public CreditDebitChartData getCreditDebitChartData(String accessToken, String id, String fromDate, String toDate,
			String groupBy) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy");
		
		//Get account details
		Account account = accountDao.getAccountById(accessToken, id);
		
		// Get transaction totals for date range, grouped according to groupBy parameter
		List<TransactionTotal> totals = getTransactionTotals(accessToken, id, fromDate, toDate, groupBy);
		
		if (account==null || totals==null) {
			return null;
		}
		
		CreditDebitChartData data = new CreditDebitChartData();
		data.setCurrency(account.getCurrency());
		
		// Add transaction totals to chart data object
		for (TransactionTotal total : totals) {
			if (groupBy=="day") {
				data.addXValue(total.getToDate().format(formatter));
			} else {
				data.addXValue(total.getFromDate().format(formatter) + " to " + total.getToDate().format(formatter));
			}
			data.addCreditValue(total.getTotalCredit());
			data.addDebitValue(total.getTotalDebit());
		}
		
		return data;
	}
	
	
	
}
;