package ie.cit.comp8058.bankdemo.service;

import org.springframework.stereotype.Service;

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
		//System.out.println(LocalTime.now());
		txns = accountDao.getTransactionsByAccountIdAndDate(accessToken, id, fromDate, toDate);
		//System.out.println(LocalTime.now());
		
		if (txns == null) {
			return null;
		}
		
		if (txns.isEmpty()) {
			return totals; //empty list
		}
		
		Collections.reverse(txns); // put txns in ascending order
				
		startDate = LocalDate.parse(fromDate, formatter);
		
			//startDate = LocalDate.parse(txns.get(0).getBookingDate(), formatter);
		
		nextDate = getNextDate(startDate, groupBy);
		
		TransactionTotal txnTotal = new TransactionTotal();
		txnTotal.setFromDate(startDate);
		txnTotal.setToDate(nextDate.minusDays(1));
		ListIterator<Transaction> iterator = txns.listIterator();
		Transaction t;
		
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
	
	
	
}
;