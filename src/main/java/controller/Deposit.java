package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.BankDao;
import dto.BankAccount;
import dto.BankTransanction;
import dto.Customer;

@WebServlet("/deposit")
public class Deposit extends HttpServlet
{
@Override
protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
Customer customer=(Customer)req.getSession().getAttribute("customer");
	
	if(customer==null)
	{
		res.getWriter().print("<h1>Session Expired Login Again</h1>");
		req.getRequestDispatcher("Login.html").include(req, res);
	}
	else
	{
	
	
double amount=Double.parseDouble(req.getParameter("amount"));
long acno=(Long)req.getSession().getAttribute("acno");

BankDao bankDao=new BankDao();
BankAccount account=bankDao.find(acno);
account.setAmount(account.getAmount()+amount);

BankTransanction bankTransanction=new BankTransanction();

bankTransanction.setDeposit(amount);
bankTransanction.setWithdraw(0);
bankTransanction.setBalance(account.getAmount());
bankTransanction.setDate(LocalDateTime.now());

List<BankTransanction> list=account.getBankTransanctions();
list.add(bankTransanction);

account.setBankTransanctions(list);



bankDao.update(account);

res.getWriter().print("<h1> Amount Added Successfully</h1>");
req.getRequestDispatcher("AccountHome.jsp").include(req, res);
}
}
}
