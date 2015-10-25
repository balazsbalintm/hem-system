package hu.bme.analytics.hems.entities.repositories;

import hu.bme.analytics.hems.entities.Employee;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
	Employee findByFirstNameAndLastNameOrderByLastNameAsc(String firstName, String lastName);
}
