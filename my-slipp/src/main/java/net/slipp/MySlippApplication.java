package net.slipp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.google.common.base.Predicates;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableJpaAuditing //현재 데이터가 생성될때 자동으로 생성시간을 저장해주고 그러는 것을 돕는다.
@EnableSwagger2
public class MySlippApplication {   //이 파일이 서버 실행할때 설정파일이다.

	public static void main(String[] args) {
		SpringApplication.run(MySlippApplication.class, args);
	}
	
	@Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
        		.groupName("My-slipp")
        		.apiInfo(apiInfo())
        		.select()
                .apis(Predicates.not(RequestHandlerSelectors.
                        basePackage("org.springframework.boot")))
                .paths(PathSelectors.ant("/api/**"))
                .build();
    }
	
	public ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("My slipp app")
				.description("blurbllru")
				.build();
	}

}
