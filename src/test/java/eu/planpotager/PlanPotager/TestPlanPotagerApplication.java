package eu.planpotager.PlanPotager;

import org.springframework.boot.SpringApplication;

public class TestPlanPotagerApplication {

	public static void main(String[] args) {
		SpringApplication.from(PlanPotagerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
