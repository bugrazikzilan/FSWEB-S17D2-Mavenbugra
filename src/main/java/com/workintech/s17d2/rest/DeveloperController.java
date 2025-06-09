package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.Developer;
import com.workintech.s17d2.model.JuniorDeveloper;
import com.workintech.s17d2.model.MidDeveloper;
import com.workintech.s17d2.model.SeniorDeveloper;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;



@RestController
@RequestMapping("/developers")
public class DeveloperController {

    public Map<Integer, Developer> developers;
    private Taxable taxable;

    @Autowired
    public DeveloperController(Taxable taxable) {
        this.taxable = taxable;
    }

    @PostConstruct
    public void init() {
        developers = new HashMap<>();
    }

    public Map<Integer, Developer> getDevelopers() {
        return developers;
    }

    @GetMapping
    public List<Developer> getAllDevelopers() {
        return new ArrayList<>(developers.values());
    }

    @GetMapping("/{id}")
    public Developer getDeveloperById(@PathVariable int id) {
        return developers.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Developer createDeveloper(@RequestBody Developer dev) {
        Developer newDev;

        double salary = dev.getSalary();
        switch (dev.getExperience()) {
            case JUNIOR:
                salary -= salary * taxable.getSimpleTaxRate();
                newDev = new JuniorDeveloper(dev.getId(), dev.getName(), salary);
                break;
            case MID:
                salary -= salary * taxable.getMiddleTaxRate();
                newDev = new MidDeveloper(dev.getId(), dev.getName(), salary);
                break;
            case SENIOR:
                salary -= salary * taxable.getUpperTaxRate();
                newDev = new SeniorDeveloper(dev.getId(), dev.getName(), salary);
                break;
            default:
                throw new IllegalArgumentException("Invalid experience type");
        }

        developers.put(dev.getId(), newDev);
        return newDev;
    }

    @PutMapping("/{id}")
    public Developer updateDeveloper(@PathVariable int id, @RequestBody Developer updatedDev) {
        developers.put(id, updatedDev);
        return updatedDev;
    }

    @DeleteMapping("/{id}")
    public Developer deleteDeveloper(@PathVariable int id) {
        return developers.remove(id);
    }
}
