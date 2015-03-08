package hu.bme.analytics.hems;

import hu.bme.analytics.hems.entities.Employee;
import hu.bme.analytics.hems.entities.repositories.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App implements CommandLineRunner 
{
	
	@Autowired
	EmployeeRepository repository;
	
    public static void main( String[] args )
    { 
    	SpringApplication.run(App.class);
    }
    
    @Override
    public void run(String... strings) throws Exception {
    	 repository.save(new Employee("Jack", "Bauer"));
         repository.save(new Employee("Chloe", "O'Brian"));
         repository.save(new Employee("Kim", "Bauer"));
         repository.save(new Employee("David", "Palmer"));
         repository.save(new Employee("Michelle", "Dessler"));

         // fetch all customers
         System.out.println("Customers found with findAll():");
         System.out.println("-------------------------------");
         for (Employee emp : repository.findAll()) {
             System.out.println(emp);
         }
         System.out.println();
    }
}
