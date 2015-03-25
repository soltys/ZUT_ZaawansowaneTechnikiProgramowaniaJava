package pl.co.soltysiak.ztpj.net.model;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import junit.framework.Assert;

import org.junit.Test;

public class EmployeeParserTests {

	@Test
	public void getAllFieldsRight() {
		String input = "Employee [id=1, firstName=Pawl, lastName=Soltys, salary=100000, position=Handloweic, phone=6060606, creditCard=4tronic, costLimit=120005]";
		Employee employee = Employee.parseEmployee(input);
		
		assertEquals(1, employee.getId());
		assertEquals("Pawl", employee.getFirstName());
		assertEquals("Soltys", employee.getLastName());
		assertEquals(new BigDecimal(100000), employee.getSalary());
		assertEquals("Handloweic", employee.getPosition());
		assertEquals("6060606", employee.getPhone());
		assertEquals("4tronic", employee.getCreditCard());
		assertEquals(new BigDecimal(120005), employee.getCostLimit());
	}

}
