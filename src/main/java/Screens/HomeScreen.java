package Screens;

import java.awt.List;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import Daos.AccountDao;
import Daos.UserDao;
import beans.Account;
import beans.User;

public class HomeScreen implements Screen{
	private UserDao ud = UserDao.cUserDao;
	private AccountDao ad = AccountDao.cAccountDao;
	private Scanner scan = new Scanner(System.in);
	private User cUser;
	private ArrayList<Account> cUserAccounts;
	private String output;
		
	public HomeScreen(User cUser,String output) {
			this.output = output;
			this.cUser = cUser;
			 ArrayList<Integer> cAccountNumbers = cUser.getAccounts();
			 this.cUserAccounts = new ArrayList<Account>();
				for (int accountNumber: cAccountNumbers) {
						cUserAccounts.add(ad.findAccount(accountNumber));
					}
		}
	
	public Screen start() {
		System.out.println(Screen.header);
		System.out.println("Home Screen for " + cUser.getName());
		System.out.println(output);
		System.out.println("1: Examine Existing Account"
				+"\n2: Make New Account" 
				+"\n3: Log out");
		if (cUser.isAdmin()) {
			System.out.println("Special Admin controls.\n4: access user\n5: access account\n6: Make user admin");
		}
		switch (scan.nextLine()) {
		case "1": 
			System.out.println("Which Account?");
			for (Account acc: cUserAccounts) {
				System.out.println("Account number: " + acc.getAccountNumber() + ". Accoount Type: "
						+ acc.getAccountType() + ". Balance: " + acc.getBalance());
			}
			System.out.println("Type account number:");
			int accSelect;
			try {
				accSelect = scan.nextInt();
			} catch (InputMismatchException e) {
				output = "Must be a number";
				return new HomeScreen(cUser,output);
			}
			
			for (Account acc: cUserAccounts) {
				if (acc.getAccountNumber() == accSelect) {
					return new AccountScreen(cUser, acc, "");
				}
			}
			output = "Invalid Selection";
			return new HomeScreen(cUser,output);
		case "2":
			return new AccountRegistrationScreen(cUser);
		case "3":
			return new LoginScreen("Logout Successful.");
		case "4":
			if (cUser.isAdmin()) {
			System.out.println("Enter username");
			String username = scan.nextLine();
			User accessingUser = ud.findUser(username, cUser);
			if (accessingUser != null) {
				System.out.println("Found account! Accesing Homepage...");
				return new HomeScreen(accessingUser,"Administrative Accessed");
				}
			output = "couldn't be found.";
			return new HomeScreen(cUser,output);
			}
		case "5":
			if (cUser.isAdmin()) {
			System.out.println("Enter account number");
			int accountNumber = Integer.valueOf(scan.nextLine());
			Account accessingAccount = ad.findAccount(accountNumber);
			if (accessingAccount != null) {
				System.out.println("Found account! Accesing Homepage...");
				return new AccountScreen(cUser, accessingAccount,"");
				}
			output = "couldn't be found.";
			return new HomeScreen(cUser,output);
			}
		case "6":
			if (cUser.isAdmin()) {
			System.out.println("Enter username");
			String username1 = scan.nextLine();
			User accessingUser1 = ud.findUser(username1, cUser);
			if (accessingUser1 != null) {
				System.out.println("Found account! Making Admin...");
				accessingUser1.giveAdmin(cUser);
				ud.updateUser(accessingUser1);
				}
			output = "couldn't be found.";
			return new HomeScreen(cUser,output);
			}
		case "7":
			
		default:
			output = "Invalid selection";
			return new HomeScreen(cUser,output);
		}
	}
}
