package kr.co.tipsvalley.sapsa;

import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import kr.co.tipsvalley.sapsa.model.db.SensorEntity;
import kr.co.tipsvalley.sapsa.repository.SensorRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SensorRepositoryTests {

	@Autowired
	private SensorRepository sRepository;
	
	@Test
	public void inspect() {
		Class<?> clz = sRepository.getClass();
		
		System.out.println(clz.getName());
		
		Class<?>[] interfaces = clz.getInterfaces();
		
		Stream.of(interfaces).forEach(inter -> System.out.println(inter.getName()));
		
		Class<?> superClasses = clz.getSuperclass();
		
		System.out.println(superClasses.getName());
		
		sRepository.findById("device_mac_addr").ifPresent((sensorEntity)->{System.out.println(sensorEntity);});
		
	}
	
}
