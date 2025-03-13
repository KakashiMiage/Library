/*package com.bibliomanager.library.config;

import com.bibliomanager.library.model.Role;
import com.bibliomanager.library.model.User;
import com.bibliomanager.library.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AdminInitializer {

    @Bean
    public CommandLineRunner initAdminUser(UserRepository userRepository) {
        return args -> {

            // ========================
            // INITIALISATION ADMIN USER
            // ========================

            String adminUsername = "admin";

            if (userRepository.findByUserUsernameIgnoreCase(adminUsername).isEmpty()) {

                User admin = new User();
                admin.setUserName("Super Admin");
                admin.setUserUsername(adminUsername);
                admin.setUserPassword("admin"); // Pas de cryptage comme demandé
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);

                System.out.println("✅ Administrateur par défaut créé !");
                System.out.println("👉 Username : admin");
                System.out.println("👉 Password : admin");

            } else {
                System.out.println("ℹ️ L'utilisateur admin existe déjà. Aucun changement.");
            }

            // (Optionnel) Créer aussi un reader par défaut :
            String readerUsername = "jdupont";
            if (userRepository.findByUserUsernameIgnoreCase(readerUsername).isEmpty()) {

                User reader = new User();
                reader.setUserName("Default Reader");
                reader.setUserUsername(readerUsername);
                reader.setUserPassword("password123");
                reader.setRole(Role.READER);

                userRepository.save(reader);

                System.out.println("✅ Utilisateur reader par défaut créé !");
                System.out.println("👉 Username : jdupont");
                System.out.println("👉 Password : password123");

            } else {
                System.out.println("ℹ️ L'utilisateur reader existe déjà. Aucun changement.");
            }

        };
    }
}*/
