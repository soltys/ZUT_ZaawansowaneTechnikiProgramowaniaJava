package pl.co.soltysiak.ztpj.net.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Employee {
	@Override
	public String toString() {
		return "Employee [id=" + id + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", salary=" + salary
				+ ", position=" + position + ", phone=" + phone
				+ ", creditCard=" + creditCard + ", costLimit=" + costLimit
				+ "]";
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
}
