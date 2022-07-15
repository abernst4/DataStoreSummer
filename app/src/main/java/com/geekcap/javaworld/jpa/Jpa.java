/*
package com.geekcap.javaworld.jpa;
import com.geekcap.javaworld.jpa.model.User;
import com.geekcap.javaworld.jpa.model.Group;
import com.geekcap.javaworld.jpa.repository.UserRepository;
import com.geekcap.javaworld.jpa.repository.GroupRepository;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.support.JdbcUtils;

public class Jpa {
    public static void main(String[] args) {
    
 */
        // Create our entity manager
/*
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Books");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        
 */
        // Create our repositories
/*
        UserRepository userRepository = new UserRepository(entityManager);
        GroupRepository groupRepository = new GroupRepository(entityManager);
        
 */
        
        //create some Users
        /*
        User user1 = new User(1, "one@mail.org");
        User user2 = new User(2, "two@mail.org");
        
         */

        //create some groups
        /*
        Group g1 = new Group(3, "group1");
        g1.addUser(user1);
        g1.addUser(user2);
        Group g2 = new Group(4, "group2");
        g2.addUser(user1);
        g2.addUser(user2);

        groupRepository.save(g1);
        groupRepository.save(g2);
        
         */

        // Find all groups
        /*
        System.out.println("Groups:");
        groupRepository.findAll().forEach(group -> {
            System.out.println("Group: [" + group.getId() + "] - " + group.getName());
            group.getUsers().forEach(System.out::println);
        });
        
         */

        // Find all superheroes
        /*
        System.out.println("\nUsers:");
        userRepository.findAll().forEach(user -> {
            System.out.println(user);
            user.getGroups().forEach(System.out::println); //there is currently NO METHOD TO GET GROUPS
        });
         */
        
         // Delete a group and verify that its Users are not deleted
        /*
         groupRepository.deleteById(3);
         System.out.println("\nGroups (AFTER DELETE):");
         groupRepository.findAll().forEach(group -> {
             System.out.println("Group: [" + group.getId() + "] - " + group.getName());
             group.getUsers().forEach(System.out::println);
         });
         System.out.println("\nUsers (AFTER DELETE):");
         userRepository.findAll().forEach(user -> {
             System.out.println(user);
             user.getGroups().forEach(System.out::println);
         });
         */
 
 
         // DEBUG, dump our tables
        /*
         entityManager.unwrap(Session.class).doWork(connection ->
                 JdbcUtils.dumpTables(connection, "GROUP", "USER", "USER_GROUPS"));
        */
        /*
        Optional<Author> savedAuthor = authorRepository.save(author);
        System.out.println("Saved author: " + savedAuthor.get());
        // Find all authors
        List<Author> authors = authorRepository.findAll();
        System.out.println("Authors:");
        authors.forEach(System.out::println);
        // Find author by name
        Optional<Author> authorByName = authorRepository.findByName("Author 1");
        System.out.println("Searching for an author by name: ");
        authorByName.ifPresent(System.out::println);
        // Search for a book by ID
        Optional<Book> foundBook = bookRepository.findById(2);
        foundBook.ifPresent(System.out::println);
        // Search for a book with an invalid ID
        Optional<Book> notFoundBook = bookRepository.findById(99);
        notFoundBook.ifPresent(System.out::println);
        // List all books
        List<Book> books = bookRepository.findAll();
        System.out.println("Books in database:");
        books.forEach(System.out::println);
        // Find a book by name
        Optional<Book> queryBook1 = bookRepository.findByName("Book 2");
        System.out.println("Query for book 2:");
        queryBook1.ifPresent(System.out::println);
        // Find a book by name using a named query
        Optional<Book> queryBook2 = bookRepository.findByNameNamedQuery("Book 3");
        System.out.println("Query for book 3:");
        queryBook2.ifPresent(System.out::println);
        // Add a book to author 1
        Optional<Author> author1 = authorRepository.findById(1);
        author1.ifPresent(a -> {
            a.addBook(new Book("Book 4"));
            System.out.println("Saved author: " + authorRepository.save(a));
        });
        */

        // Close the entity manager and associated factory
/*
        entityManager.close();
        entityManagerFactory.close();
    }
}

 */