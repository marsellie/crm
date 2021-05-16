package kpfu.crm;

import kpfu.crm.exceptions.RestException;
import kpfu.crm.model.User;
import kpfu.crm.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class MainApp {

    public static void main(String[] args) throws RestException {

        ApplicationContext ctx = SpringApplication.run(MainApp.class, args);
        var userService = ctx.getBean(UserService.class);
        userService.get().forEach(user -> {
            try {
                userService.delete(user.getId());
            } catch (RestException e) {
                e.printStackTrace();
            }
        });

        String username = "admin";
        String password = "admin";
        if (args.length == 2) {
            username = args[0];
            password = args[1];
        }
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("---USERNAME SET: << " + username + " >>---");
        System.out.println();
        System.out.println();
        System.out.println();

        var user = new User();
        user.setUsername(username);
        user.setPassword(ctx.getBean(PasswordEncoder.class).encode(password));
        userService.save(user);
    }
}
