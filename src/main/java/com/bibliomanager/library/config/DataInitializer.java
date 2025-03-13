/*package com.bibliomanager.library.config;

import com.bibliomanager.library.model.*;
import com.bibliomanager.library.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            UserRepository userRepository,
            BookRepository bookRepository,
            AuthorRepository authorRepository,
            GenreRepository genreRepository,
            TypeRepository typeRepository,
            EditorRepository editorRepository
    ) {
        return args -> {

            // ===== USERS =====
            if (userRepository.findByUserUsernameIgnoreCase("admin").isEmpty()) {
                User admin = new User();
                admin.setUserName("Admin User");
                admin.setUserUsername("admin");
                admin.setUserPassword("admin123"); // Simple pour test
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
                System.out.println("Admin user créé !");
            }

            if (userRepository.findByUserUsernameIgnoreCase("reader").isEmpty()) {
                User reader = new User();
                reader.setUserName("Reader User");
                reader.setUserUsername("reader");
                reader.setUserPassword("reader123");
                reader.setRole(Role.READER);
                userRepository.save(reader);
                System.out.println("Reader user créé !");
            }

            // ===== TYPES =====
            List<Type> types = new ArrayList<>();
            String[] typeNames = {"Roman", "Essai", "Poésie", "Théâtre", "Biographie"};

            for (String typeName : typeNames) {
                Type type = new Type();
                type.setTypeName(typeName);
                typeRepository.save(type);
                types.add(type);
            }

            // ===== GENRES =====
            List<Genre> genres = new ArrayList<>();
            String[] genreNames = {"Aventure", "Science-fiction", "Drame", "Comédie", "Horreur", "Fantastique"};

            for (String genreName : genreNames) {
                Genre genre = new Genre();
                genre.setGenreName(genreName);
                genreRepository.save(genre);
                genres.add(genre);
            }

            // ===== AUTEURS =====
            List<Author> authors = new ArrayList<>();
            String[][] authorsList = {
                    {"Victor", "Hugo"},
                    {"Jules", "Verne"},
                    {"Emile", "Zola"},
                    {"George", "Sand"},
                    {"Albert", "Camus"},
                    {"Molière", "Jean-Baptiste"},
                    {"Marcel", "Proust"},
                    {"Honoré", "de Balzac"},
                    {"Simone", "de Beauvoir"},
                    {"Jean-Paul", "Sartre"}
            };

            for (String[] names : authorsList) {
                Author author = new Author();
                author.setAuthorFirstName(names[0]);
                author.setAuthorLastName(names[1]);
                authorRepository.save(author);
                authors.add(author);
            }

            // ===== EDITEURS =====
            List<Editor> editors = new ArrayList<>();
            String[] editorNames = {"Gallimard", "Flammarion", "Plon", "Fayard", "Seuil"};

            long siretStart = 10000000000000L;

            for (String editorName : editorNames) {
                Editor editor = new Editor();
                editor.setEditorName(editorName);
                editor.setEditorSIRET(siretStart++);
                // Chaque éditeur a 2 types au hasard
                editor.setTypes(Arrays.asList(
                        types.get(new Random().nextInt(types.size())),
                        types.get(new Random().nextInt(types.size()))
                ));
                editorRepository.save(editor);
                editors.add(editor);
            }

            // ===== LIVRES =====
            int bookCount = 50; // Nombre de livres à créer
            Random random = new Random();

            for (int i = 1; i <= bookCount; i++) {
                Book book = new Book();
                book.setBookTitle("Livre #" + i + " - " + randomWord());
                book.setBookDescription("Description du livre numéro " + i + ". Une histoire captivante !");
                book.setNumberOfPages(100 + random.nextInt(900));

                // Date de publication aléatoire
                Calendar cal = Calendar.getInstance();
                cal.set(1900 + random.nextInt(123), random.nextInt(12), 1);
                book.setBookPublicationDate(cal.getTime());

                // Associations aléatoires
                book.setAuthor(authors.get(random.nextInt(authors.size())));
                book.setEditor(editors.get(random.nextInt(editors.size())));
                book.setType(types.get(random.nextInt(types.size())));

                // Chaque livre a 1 à 3 genres aléatoires
                Collections.shuffle(genres);
                book.setGenres(genres.subList(0, 1 + random.nextInt(3)));

                bookRepository.save(book);
            }

            System.out.println("Création de " + bookCount + " livres terminée !");
        };
    }

    private String randomWord() {
        String[] words = {"Étoile", "Voyage", "Secret", "Mystère", "Aventure", "Ombre", "Lumière", "Rêve", "Empire", "Horizon"};
        return words[new Random().nextInt(words.length)];
    }
}*/
