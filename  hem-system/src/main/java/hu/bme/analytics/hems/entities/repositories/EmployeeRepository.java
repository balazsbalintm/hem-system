package hu.bme.analytics.hems.entities.repositories;

import hu.bme.analytics.hems.entities.Employee;

import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {

}
