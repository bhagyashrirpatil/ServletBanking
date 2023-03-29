package controller;

import java.io.IOException;
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

@WebServlet("/withdraw")
public class Withdraw extends HttpServlet
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
	
	if(amount>account.getAmount())
	{
		res.getWriter().print("<h1> Insufficient balance</h1>");
		req.getRequestDispatcher("AccountHome.jsp").include(req, res);
	}
	
	else
	{
		if(amount>account.getAclimit())
		{
			res.getWriter().print("<h1>Out of limit enter amount within"+account.getAclimit() +"</h1>");
			req.getRequestDispatcher("AccountHome.jsp").include(req, res);
		}
		
		else
		{
	account.setAmount(account.getAmount()-amount);

	
	BankTransanction bankTransanction=new BankTransanction();
	bankTransanction.setDeposit(0);
	bankTransanction.setWithdraw(amount);
	bankTransanction.setBalance(account.getAmount());
	bankTransanction.setDate(LocalDateTime.now());

	List<BankTransanction> list=account.getBankTransanctions();
	list.add(bankTransanction);

	account.setBankTransanctions(list);

	
	bankDao.update(account);
	
	

	res.getWriter().print("<h1> Amount withdrawed Successfully</h1>");
	req.getRequestDispatcher("AccountHome.jsp").include(req, res);
}
}
}
}
}
