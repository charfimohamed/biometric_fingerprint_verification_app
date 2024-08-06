package com.st2i;
import com.st2i.model.Customer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
@RestController
@RequestMapping("api/v1/customers")
public class Main {

    private final CustomerRepository customerRepository ;

    public Main(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class,args);
    }
    @GetMapping
    public List<Customer> getCustomers(){
        return customerRepository.findAll();
    }
    record NewCustomerRequest(String name ,String village ,Integer age, Integer numberOfChildren,byte[] fingerprint){

    }
    @PostMapping
    public void addCustomer(@RequestBody NewCustomerRequest request){
        Customer customer = new Customer();
        customer.setName(request.name);
        customer.setAge(request.age);
        customer.setVillage(request.village);
        customer.setNumberOfChildren(request.numberOfChildren);
        customer.setFingerprint(request.fingerprint);
        customerRepository.save(customer);
    }

    @DeleteMapping("{customerId}")
    public void deleteCustomer(@PathVariable("customerId") Integer id){
        customerRepository.deleteById(id);
    }

    @PutMapping("{customerId}")
    public void updateCustomer(@PathVariable("customerId") Integer id,@RequestBody NewCustomerRequest request){
        Optional<Customer> optionalCustomer = customerRepository.findById(id);

            Customer customer = optionalCustomer.get();
            if (request.name != null) {
                customer.setName(request.name);
            }
            if (request.age != null) {
                customer.setAge(request.age);
            }
            if (request.village != null) {
                customer.setVillage(request.village);
            }
            if (request.numberOfChildren != null) {
            customer.setNumberOfChildren(request.numberOfChildren);
            }
            if (request.fingerprint != null) {
            customer.setFingerprint(request.fingerprint);
            }
            customerRepository.save(customer);
    }
}
