package pl.co.soltysiak.ztpj.net.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

public class Employee {
	@Override
	public String toString() {
		return "Employee [id=" + id + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", salary=" + salary
				+ ", position=" + position + ", phone=" + phone
				+ ", creditCard=" + creditCard + ", costLimit=" + costLimit.toPlainString()
				+ "]";
	}

	public static Employee parseEmployee(String employeeString) {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		symbols.setDecimalSeparator('.');
		String pattern = "#,##0.0#";
		DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
		decimalFormat.setParseBigDecimal(true);
		Employee employee = new Employee();
		try {

			
			int start = employeeString.indexOf('[');
			int end = employeeString.indexOf(']');
			String importantPart = employeeString.substring(start + 1, end);
			String[] tokens = importantPart.split(",");
			for (String token : tokens) {
				String[] pair = token.split("=");
				String key = pair[0].trim();
				String value = pair[1].trim();
				if (key.equals("id")) {
					employee.setId(Integer.parseInt(value));
				} else if (key.equals("firstName")) {
					employee.setFirstName(value);
				} else if (key.equals("lastName")) {
					employee.setLastName(value);
				} else if (key.equals("salary")) {
					employee.setSalary((BigDecimal) decimalFormat.parse(value));
				} else if (key.equals("position")) {
					employee.setPosition(value);
				} else if (key.equals("phone")) {
					employee.setPhone(value);
				} else if (key.equals("creditCard")) {
					employee.setCreditCard(value);
				} else if (key.equals("costLimit")) {
					employee.setCostLimit((BigDecimal) decimalFormat.parse(value));
				}

			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return employee;
	}

	public Employee() {

	}

	public Employee(ResultSet rs) throws SQLException {
		id = rs.getInt("Id");
		firstName = rs.getString("FirstName");
		lastName = rs.getString("LastName");
		salary = rs.getBigDecimal("Salary");
		position = rs.getString("Position");
		phone = rs.getString("Phone");
		creditCard = rs.getString("CreditCard");
		costLimit = rs.getBigDecimal("CostLimit");
	}

	int id;
	String firstName;
	String lastName;
	BigDecimal salary;
	String position;
	String phone;
	String creditCard;
	BigDecimal costLimit;

	public int getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public String getPosition() {
		return position;
	}

	public String getPhone() {
		return phone;
	}

	public String getCreditCard() {
		return creditCard;
	}

	public BigDecimal getCostLimit() {
		return costLimit;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}

	public void setCostLimit(BigDecimal costLimit) {
		this.costLimit = costLimit;
	}
}
