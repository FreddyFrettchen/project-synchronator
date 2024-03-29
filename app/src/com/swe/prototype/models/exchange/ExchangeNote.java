package com.swe.prototype.models.exchange;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.independentsoft.exchange.Task;
import com.independentsoft.exchange.TaskPropertyPath;
import com.independentsoft.exchange.FindItemResponse;
import com.independentsoft.exchange.IsEqualTo;
import com.independentsoft.exchange.Service;
import com.independentsoft.exchange.ServiceException;
import com.independentsoft.exchange.StandardFolder;
import com.independentsoft.exchange.Body;
import com.independentsoft.exchange.ItemId;
import android.R.string;

import com.swe.prototype.models.Note;
import com.swe.prototype.models.AccountBase;

public class ExchangeNote extends Note {

	private String ID;
	private String subject;
	private String body;

	public ExchangeNote(AccountBase account) {
		super(account);
	}

	public String getID()
	{
		return this.ID;
	}

	public String getBody() {
		return this.body;
	}

	public void setID(String iD) {
		this.ID = iD;
	}

	public void setTitle(String subject) {
		this.subject = subject;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String getNote() {
		// TODO Auto-generated method stub
		return this.body;
	}

/*	@Override
	public String getTitle() {
		try {
			Service service = new Service("https://myserver/ews/Exchange.asmx",
					"username", "password");

			IsEqualTo restriction = new IsEqualTo(TaskPropertyPath.IS_COMPLETE,
					true);

			FindItemResponse response = service.findItem(StandardFolder.TASKS,
					TaskPropertyPath.getAllPropertyPaths(), restriction);
			for (int i = 0; i < response.getItems().size(); i++) {
				if (response.getItems().get(i) instanceof Task) {
					Task task = (Task) response.getItems().get(i);

					System.out.println("Subject = " + task.getSubject());
					System.out
							.println("----------------------------------------------------------------");
					return task.getSubject();
				}
			}
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getXmlMessage());

			e.printStackTrace();
		}
		return "Error";
	}
*/
/*	public boolean createTask() {
		try {
			Service service = new Service("https://myserver/ews/Exchange.asmx",
					"username", "password");

			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date startTime = dateFormat.parse("2014-04-25 10:00:00");
			Date endTime = dateFormat.parse("2014-04-29 10:00:00");

			Task task = new Task();
			task.setSubject("Test");
			task.setBody(new Body("Body text"));
			task.setOwner("Bahos");
			task.setStartDate(startTime);
			task.setDueDate(endTime);
			task.setReminderIsSet(true);
			task.setReminderDueBy(startTime);

			ItemId itemId = service.createItem(task);
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getXmlMessage());

			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean getTask() {
		try {
			Service service = new Service("https://myserver/ews/Exchange.asmx",
					"username", "password");

			IsEqualTo restriction = new IsEqualTo(TaskPropertyPath.IS_COMPLETE,
					true);

			FindItemResponse response = service.findItem(StandardFolder.TASKS,
					TaskPropertyPath.getAllPropertyPaths(), restriction);

			for (int i = 0; i < response.getItems().size(); i++) {
				if (response.getItems().get(i) instanceof Task) {
					Task task = (Task) response.getItems().get(i);

					System.out.println("Subject = " + task.getSubject());
					System.out.println("StartDate = " + task.getStartDate());
					System.out.println("DueDate = " + task.getDueDate());
					System.out.println("Owner = " + task.getOwner());
					System.out.println("Body Preview = "
							+ task.getBodyPlainText());
					System.out
							.println("----------------------------------------------------------------");
				}
			}
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getXmlMessage());

			e.printStackTrace();
		}
		return false;
	}
*/
	@Override
	public String getAccountTag() {
		return "Exchange";
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUpToDate() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return this.subject;
	}
}
